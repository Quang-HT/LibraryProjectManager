package com.library.controller;

import com.library.dao.impl.*;
import com.library.model.*;
import com.library.view.AddBookFormView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddBookController {

    private final AddBookFormView view;

    private Category_DAO categoryDAO;
    private Author_DAO authorDAO;
    private Publisher_DAO publisherDAO;

    private List<Category_Entity> listCate = new ArrayList<>();
    private List<Author_Entity> listAuth = new ArrayList<>();
    private List<Publisher_Entity> listPub = new ArrayList<>();

    /**
     * Hàm Controller của Add Book
     * Thực thi các danh sách công việc cứ thực thi
     */
    public AddBookController(AddBookFormView view) {
        this.view = view;
        initDaos();
        loadComboboxData();
        bindEvents();
        view.setVisible(true);
    }

    /**
     * Tạo các DAO
     * Thừa mà thôi kệ =))
     */
    private void initDaos() {
        categoryDAO = new Category_DAO();
        authorDAO = new Author_DAO();
        publisherDAO = new Publisher_DAO();
    }

    /**
     * Đăng ký các sự kiện ActionListener cho các Button ở View
     */
    private void bindEvents() {
        view.getBtnSave().addActionListener(e -> saveBook());
        view.getBtnCancel().addActionListener(e -> cancel());
    }

    /**
     * Đưa các Option ở ComboBox để người dùng lựa chọn
     */
    private void loadComboboxData() {
        List<String> categories = new ArrayList<>();
        categories.add("Chọn thể loại");
        try {
            listCate = categoryDAO.findAll();
            for (Category_Entity c : listCate) categories.add(c.getCategory_name());
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách thể loại.\nVui lòng kiểm tra kết nối CSDL.");
            listCate = new ArrayList<>();
        }
        view.setCategoryItems(categories);

        List<String> authors = new ArrayList<>();
        authors.add("Chọn tác giả");
        try {
            listAuth = authorDAO.findAll();
            for (Author_Entity a : listAuth) authors.add(a.getAuthor_name());
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách tác giả.\nVui lòng kiểm tra kết nối CSDL.");
            listAuth = new ArrayList<>();
        }
        view.setAuthorItems(authors);

        List<String> publishers = new ArrayList<>();
        publishers.add("Chọn nhà xuất bản");
        try {
            listPub = publisherDAO.findAll();
            for (Publisher_Entity p : listPub) publishers.add(p.getPublisher_name());
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách nhà xuất bản.\nVui lòng kiểm tra kết nối CSDL.");
            listPub = new ArrayList<>();
        }
        view.setPublisherItems(publishers);
    }


    /**
     * Hủy phiếu thêm sách
     */
    private void cancel() {
        boolean ok = view.confirm("Bạn có chắc chắn muốn hủy? Dữ liệu chưa lưu sẽ bị mất.");
        if (ok) view.dispose();
    }

    /**
     * Lưu sách từ dữ liệu người dùng nhập vào
     */
    private void saveBook() {
        if (view.getBookName().isEmpty()) {
            view.showError("Vui lòng nhập tên sách!");
            view.requestFocusBookName();
            return;
        }
        if (view.getSelectedCategoryIndex() == 0) {
            view.showError("Vui lòng chọn thể loại!");
            return;
        }
        if (view.getSelectedAuthorIndex() == 0) {
            view.showError("Vui lòng chọn tác giả!");
            return;
        }
        if (view.getSelectedPublisherIndex() == 0) {
            view.showError("Vui lòng chọn nhà xuất bản!");
            return;
        }

        int publishYear;
        try {
            publishYear = Integer.parseInt(view.getPublishYearText());
        } catch (Exception e) {
            view.showError("Năm xuất bản phải là số!");
            view.requestFocusPublishYear();
            return;
        }

        double copyPrice;
        try {
            copyPrice = Double.parseDouble(view.getCopyPriceText());
        } catch (Exception e) {
            view.showError("Giá trị bản sao phải là số!");
            view.requestFocusCopyPrice();
            return;
        }

        int copyCount;
        try {
            copyCount = Integer.parseInt(view.getCopyCountText());
            if (copyCount <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            view.showError("Số lượng bản sao phải là số nguyên dương!");
            view.requestFocusCopyCount();
            return;
        }

        int cateIndex = view.getSelectedCategoryIndex();
        int authIndex = view.getSelectedAuthorIndex();
        int pubIndex  = view.getSelectedPublisherIndex();

        if (listCate == null || listCate.isEmpty() || cateIndex - 1 >= listCate.size()) {
            view.showError("Dữ liệu thể loại không hợp lệ!");
            return;
        }
        if (listAuth == null || listAuth.isEmpty() || authIndex - 1 >= listAuth.size()) {
            view.showError("Dữ liệu tác giả không hợp lệ!");
            return;
        }
        if (listPub == null || listPub.isEmpty() || pubIndex - 1 >= listPub.size()) {
            view.showError("Dữ liệu nhà xuất bản không hợp lệ!");
            return;
        }

        Category_Entity selectedCategory  = listCate.get(cateIndex - 1);
        Author_Entity selectedAuthor      = listAuth.get(authIndex - 1);
        Publisher_Entity selectedPublisher= listPub.get(pubIndex - 1);

        int categoryId  = selectedCategory.getCategory_id();
        int authorId    = selectedAuthor.getAuthor_id();
        int publisherId = selectedPublisher.getPublisher_id();

        Book_Entity book = new Book_Entity();
        book.setTitle(view.getBookName());

        book.setPublishYear(Date.valueOf(publishYear + "-01-01"));

        book.setDescription(view.getDescription());
        book.setPublisher_id(publisherId);
        book.setLocation(view.ggetLocation());

        Book_DAO bookDao = new Book_DAO();
        Copy_DAO copyDao = new Copy_DAO();
        Book_Category_DAO bookCategoryDao = new Book_Category_DAO();
        Author_Book_DAO authorBookDao = new Author_Book_DAO();

        try {
            bookDao.create(book);
            Optional<Book_Entity> optBook = bookDao.findByName(book.getTitle());
            if (optBook.isEmpty()) {
                view.showError("Không tìm được sách vừa tạo. Vui lòng kiểm tra lại.");
                return;
            }
            int newBookId = optBook.get().getBook_id();

            Book_Category_Entity bc = new Book_Category_Entity();
            bc.setBook_id(newBookId);
            bc.setCategory_id(categoryId);
            bookCategoryDao.create(bc);

            Author_Book_Entity ab = new Author_Book_Entity();
            ab.setBook_id(newBookId);
            ab.setAuthor_id(authorId);
            authorBookDao.create(ab);

            createCopies(copyCount,newBookId,(int) copyPrice);

            view.showInfo("Đã thêm sách thành công!\nTên: " + view.getBookName());
            view.clearForm();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi lưu dữ liệu: " + ex.getMessage());
        }
    }

    /**
     * Tạo đúng số lượng bản copy mà người dùng nhập vào
     */
    private void createCopies(int copyCount, int newBookId, int copyPrice){
        for (int i = 0; i < copyCount; i++) {
            Copy_Entity cp = new Copy_Entity();
            cp.setBook_id(newBookId);
            cp.setCost(copyPrice);
            cp.setLocation(view.ggetLocation());
            cp.setStatus("Available");
            new Copy_DAO().create(cp);
        }
    }
}
