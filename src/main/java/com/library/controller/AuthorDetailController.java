package com.library.controller;

import com.library.dao.impl.Author_DAO;
import com.library.dao.impl.Book_DAO;
import com.library.dao.impl.Copy_DAO;
import com.library.model.Author_Entity;
import com.library.model.Book_Entity;
import com.library.service.FIND;
import com.library.view.AuthorDetailView;

import javax.swing.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDetailController {

    private final AuthorDetailView view;

    private final int authorId;
    private boolean isEditMode = false;

    private final Author_DAO authorDAO = new Author_DAO();
    private final Book_DAO bookDAO = new Book_DAO();
    private final Copy_DAO copyDAO = new Copy_DAO();

    /**
     * Hàm Controller của Author Detail truyền vào ID_Author để hiển thị dữ liệu
     * THực thi thứ tự các hàm cần thực thi
     */
    public AuthorDetailController(int authorId) {
        this.authorId = authorId;

        this.view = new AuthorDetailView();
        bindEvents();

        loadAuthorData(authorId);
        view.setFieldsEditable(false);
        view.setEditMode(false);

        view.setVisible(true);
    }

    /**
     * Đăng ký sự kiện với Action Listener
     */
    private void bindEvents() {
        view.getBtnEdit().addActionListener(e -> enableEditMode());
        view.getBtnSave().addActionListener(e -> saveAuthor());
        view.getBtnCancel().addActionListener(e -> cancelOrClose());

        view.getTblBooks().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) openBookDetails();
            }
        });
    }

    /**
     * Lấy Id Author để tìm dữ liệu từ DB
     * Đưa dữ liệu Author lên View
     */
    private void loadAuthorData(int authorId) {
        try {
            Optional<Author_Entity> opt = authorDAO.findById(authorId);
            if (opt.isEmpty()) {
                view.showError("Không tìm thấy tác giả với ID = " + authorId);
                view.dispose();
                return;
            }

            Author_Entity author = opt.get();

            view.setAuthorId(author.getAuthor_id());
            view.setAuthorName(author.getAuthor_name());
            view.setBirthYearFromDate(author.getBirth_date());
            view.setDeathYearFromDate(author.getDeath_date());
            view.setCountry(author.getCountry());
            view.setBiography(author.getDescription());
            System.out.println(author.getCountry());

            loadAuthorBooks(authorId);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi tải dữ liệu tác giả: " + ex.getMessage());
        }
    }

    /**
     * Hiển thị các sách thuộc tác giả đó
     */
    private void loadAuthorBooks(int authorId) {
        try {
            List<Book_Entity> books = bookDAO.findAuthorsById(authorId);

            List<Object[]> rows = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();

            FIND find = new FIND();

            for (Book_Entity b : books) {
                int bookId = b.getBook_id();
                String categoryName = find.FindNameCategory(bookId);
                int copyCount = copyDAO.findByIdBook(bookId).size();

                Object[] row = {
                        bookId,
                        b.getTitle(),
                        categoryName,
                        b.getPublish_year(),
                        copyCount
                };
                rows.add(row);
                ids.add(bookId);
            }

            view.setBooksTableRows(rows, ids);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi tải danh sách sách của tác giả: " + ex.getMessage());
        }
    }

    /**
     * Hàm cho phép chỉnh sửa thông tin tác giả
     */
    private void enableEditMode() {
        isEditMode = true;
        view.setFieldsEditable(true);
        view.setEditMode(true);
    }

    /**
     * Sau khi chỉnh sửa hàm saveAuthor() được gọi để lưu lại những thông tin thay đổi
     */
    private void saveAuthor() {
        if (view.getAuthorName().isEmpty()) {
            view.showError("Vui lòng nhập tên tác giả!");
            return;
        }
        if (view.getBirthYearText().isEmpty()) {
            view.showError("Vui lòng nhập năm sinh!");
            return;
        }

        int birthYear;
        try {
            birthYear = Integer.parseInt(view.getBirthYearText());
            if (birthYear < 1000 || birthYear > 2100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            view.showError("Năm sinh phải là số hợp lệ!");
            return;
        }

        Integer deathYear = null;
        String deathText = view.getDeathYearText();
        if (!deathText.isEmpty()) {
            try {
                int d = Integer.parseInt(deathText);
                if (d < birthYear || d > 2100) throw new NumberFormatException();
                deathYear = d;
            } catch (NumberFormatException e) {
                view.showError("Năm mất phải là số hợp lệ (>= năm sinh, <= 2100) hoặc để trống!");
                return;
            }
        }

        try {
            Optional<Author_Entity> opt = authorDAO.findById(authorId);
            if (opt.isEmpty()) {
                view.showError("Không tìm thấy tác giả để cập nhật.");
                return;
            }

            Author_Entity author = opt.get();
            author.setAuthor_name(view.getAuthorName());
            author.setBirth_date(Date.valueOf(String.valueOf(birthYear)));
            author.setDescription(view.getBiography());
            author.setCountry(view.getCountry());

            if (deathYear == null) {
                author.setDeath_date(null);
            } else {
                author.setDeath_date(Date.valueOf(String.valueOf(deathYear)));
            }

            authorDAO.update(author);

            view.showInfo("Đã cập nhật thông tin tác giả thành công!");

            isEditMode = false;
            view.setFieldsEditable(false);
            view.setEditMode(false);

            loadAuthorData(authorId);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi cập nhật tác giả: " + ex.getMessage());
        }
    }

    /**
     * Hủy thay đổi và đóng form xem chi tiết tác giả
     */
    private void cancelOrClose() {
        if (isEditMode) {
            boolean ok = view.confirm("Bạn có chắc chắn muốn hủy? Các thay đổi chưa lưu sẽ bị mất.");
            if (!ok) return;

            loadAuthorData(authorId);
            isEditMode = false;
            view.setFieldsEditable(false);
            view.setEditMode(false);
        } else {
            view.dispose();
        }
    }

    /**
     * Khi người dùng click vào sách thuộc sang tác của tác giả
     * View chi tiết thông tin sách sẽ hiện lên
     */
    private void openBookDetails() {
        Integer bookId = view.getSelectedBookIdOrNull();
        if (bookId == null) return;

        SwingUtilities.invokeLater(() -> new BookDetailController(bookId));
    }

}
