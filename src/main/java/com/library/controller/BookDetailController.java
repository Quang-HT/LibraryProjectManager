package com.library.controller;

import com.library.dao.impl.*;
import com.library.model.*;
import com.library.model.display.BookDisplayModel;
import com.library.service.FIND;
import com.library.view.BookDetailForm;

import java.sql.Date;
import java.util.*;


public class BookDetailController {
    private BookDetailForm view;
    private BookDisplayModel model;
    private int bookId;

    private Book_DAO bookDAO;
    private Copy_DAO copyDAO;
    private Category_DAO categoryDAO;
    private Author_DAO authorDAO;
    private Publisher_DAO publisherDAO;
    private Book_Category_DAO bookCategoryDAO;
    private Author_Book_DAO authorBookDAO;
    private FIND findService;

    public BookDetailController(int bookId) {
        this.bookId = bookId;
        this.model = new BookDisplayModel();
        this.view = new BookDetailForm();

        this.bookDAO = new Book_DAO();
        this.copyDAO = new Copy_DAO();
        this.categoryDAO = new Category_DAO();
        this.authorDAO = new Author_DAO();
        this.publisherDAO = new Publisher_DAO();
        this.bookCategoryDAO = new Book_Category_DAO();
        this.authorBookDAO = new Author_Book_DAO();
        this.findService = new FIND();

        initializeData();
        loadBookData();
        registerEventHandlers();
        showView();
    }

    /**
     * Khởi tạo dữ liệu cho các ComboBox
     */
    private void initializeData() {
        try {
            // Load categories
            List<String> categories = new ArrayList<>();
            for(Category_Entity e : categoryDAO.findAll()) {
                categories.add(e.getCategory_name());
            }
            view.setCategories(categories);

            // Load authors
            List<String> authors = new ArrayList<>();
            for(Author_Entity a : authorDAO.findAll()) {
                authors.add(a.getAuthor_name());
            }
            view.setAuthors(authors);

            // Load publishers
            List<String> publishers = new ArrayList<>();
            for(Publisher_Entity p : publisherDAO.findAll()) {
                publishers.add(p.getPublisher_name());
            }
            view.setPublishers(publishers);
        } catch (Exception e) {
            view.showError("Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load dữ liệu sách từ database vào model và hiển thị lên view
     */
    private void loadBookData() {
        try {
            Optional<Book_Entity> bookOpt = bookDAO.findById(bookId);
            if (!bookOpt.isPresent()) {
                view.showError("Không tìm thấy sách với ID: " + bookId);
                return;
            }

            Book_Entity book = bookOpt.get();
            List<Copy_Entity> copies = copyDAO.findByIdBook(bookId);

            // Cập nhật model
            model.setBookId(bookId);
            model.setTitle(book.getTitle());
            model.setYear(book.getPublish_year());
            model.setCategory(findService.FindNameCategory(bookId));
            model.setAuthor(findService.FindNameAuthor(bookId));

            // Load publisher
            if(book.getPublisher_id() != null) {
                Optional<Publisher_Entity> publisher = publisherDAO.findById(book.getPublisher_id());
                if(publisher.isPresent()) {
                    model.setPublisher(publisher.get().getPublisher_name());
                }
            } else {
                model.setPublisher("Không có nhà xuất bản");
            }

            // Load thông tin copy
            if (!copies.isEmpty()) {
                model.setCost(copies.get(0).getCost());
                model.setLocation(copies.get(0).getLocation());
            } else {
                model.setCost(0);
                model.setLocation("");
            }
            model.setCopyCount(findService.countCopy(bookId));
            model.setDescription(book.getDescription());

            // Hiển thị lên view
            view.setBookDetail(model);

        } catch (Exception e) {
            view.showError("Lỗi khi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Đăng ký các sự kiện từ view
     */
    private void registerEventHandlers() {
        view.onEdit(() -> handleEdit());
        view.onSave(() -> handleSave());
        view.onCancel(() -> handleCancel());
    }

    /**
     * Xử lý sự kiện Edit
     */
    private void handleEdit() {
        view.setEditMode(true);
    }

    /**
     * Xử lý sự kiện Save
     */
    private void handleSave() {
        try {
            // Lấy dữ liệu từ view
            BookDisplayModel formData = view.getFormData();
            if (formData == null) {
                return; // Validation failed
            }

            // Validate dữ liệu
            if (!validateFormData(formData)) {
                return;
            }

            // Cập nhật book
            Optional<Book_Entity> bookOpt = bookDAO.findById(bookId);
            if (!bookOpt.isPresent()) {
                view.showError("Không tìm thấy sách!");
                return;
            }

            Book_Entity book = bookOpt.get();
            book.setTitle(formData.getTitle().trim());
            book.setDescription(formData.getDescription() != null ? formData.getDescription().trim() : "");

            // Chuyển đổi năm sang Date
            Calendar calendar = Calendar.getInstance();
            calendar.set(formData.getYear(), 0, 1);
            Date publishDate = new Date(calendar.getTime().getTime());
            book.setPublishYear(publishDate);

            // Cập nhật publisher
            updatePublisher(book, formData.getPublisher());

            // Lưu book vào database
            bookDAO.update(book);

            // Cập nhật quan hệ Category
            updateBookCategory(formData.getCategory());

            // Cập nhật quan hệ Author
            updateBookAuthor(formData.getAuthor());

            // Cập nhật copies
            updateCopies(formData);

            // Cập nhật model
            this.model = formData;

            view.showInfo("Lưu thông tin sách thành công!");
            view.setEditMode(false);

            // Reload dữ liệu để đồng bộ
            loadBookData();

        } catch (Exception e) {
            view.showError("Lỗi khi lưu dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validate dữ liệu form
     */
    private boolean validateFormData(BookDisplayModel formData) {
        if (formData.getTitle() == null || formData.getTitle().trim().isEmpty()) {
            view.showError("Tên sách không được để trống!");
            return false;
        }

        if (formData.getYear() < 1000 || formData.getYear() > Calendar.getInstance().get(Calendar.YEAR) + 1) {
            view.showError("Năm xuất bản không hợp lệ!");
            return false;
        }

        if (formData.getCategory() == null || formData.getCategory().equals("Tất cả")) {
            view.showError("Vui lòng chọn thể loại!");
            return false;
        }

        if (formData.getCopyCount() < 0) {
            view.showError("Số lượng bản sao không hợp lệ!");
            return false;
        }

        if (formData.getCost() < 0) {
            view.showError("Giá trị bản sao không hợp lệ!");
            return false;
        }

        return true;
    }

    /**
     * Cập nhật Publisher cho sách
     */
    private void updatePublisher(Book_Entity book, String publisherName) {
        if(publisherName != null && !publisherName.equals("Không có nhà xuất bản")) {
            for(Publisher_Entity p : publisherDAO.findAll()) {
                if(p.getPublisher_name().equals(publisherName)) {
                    book.setPublisher_id(p.getPublisher_id());
                    return;
                }
            }
        }
        book.setPublisher_id(null);
    }

    /**
     * Cập nhật quan hệ Book_Category
     * Xóa quan hệ cũ và tạo quan hệ mới
     */
    private void updateBookCategory(String categoryName) {
        try {
            if (categoryName == null || categoryName.equals("Tất cả")) {
                return;
            }

            // Tìm categoryId từ tên
            Integer categoryId = null;
            for (Category_Entity category : categoryDAO.findAll()) {
                if (category.getCategory_name().equals(categoryName)) {
                    categoryId = category.getCategory_id();
                    break;
                }
            }

            if (categoryId == null) {
                view.showError("Không tìm thấy thể loại: " + categoryName);
                return;
            }

            // Xóa tất cả quan hệ cũ của sách này
            bookCategoryDAO.delete(bookId);

            // Tạo quan hệ mới
            Book_Category_Entity newRelation = new Book_Category_Entity();
            newRelation.setBook_id(bookId);
            newRelation.setCategory_id(categoryId);
            bookCategoryDAO.create(newRelation);

        } catch (Exception e) {
            view.showError("Lỗi khi cập nhật thể loại: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật quan hệ Book_Author
     * Xóa quan hệ cũ và tạo quan hệ mới
     */
    private void updateBookAuthor(String authorName) {
        try {
            // Xóa tất cả quan hệ cũ của sách này trước
            List<Author_Book_Entity> oldRelations = authorBookDAO.findAuthors_BookByBookId(bookId);
            for (Author_Book_Entity relation : oldRelations) {
                authorBookDAO.delete(relation.getAuthor_id());
            }

            // Nếu chọn "Không có tác giả" thì dừng tại đây
            if (authorName == null || authorName.equals("Không có tác giả")) {
                return;
            }

            //TODO: Thêm hàm tìm by tên vào đây
            Integer authorId = null;
            for (Author_Entity author : authorDAO.findAll()) {
                if (author.getAuthor_name().equals(authorName)) {
                    authorId = author.getAuthor_id();
                    break;
                }
            }

            if (authorId == null) {
                view.showError("Không tìm thấy tác giả: " + authorName);
                return;
            }

            // Tạo quan hệ mới
            Author_Book_Entity newRelation = new Author_Book_Entity();
            newRelation.setBook_id(bookId);
            newRelation.setAuthor_id(authorId);
            authorBookDAO.create(newRelation);

        } catch (Exception e) {
            view.showError("Lỗi khi cập nhật tác giả: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thông tin copies
     */
    private void updateCopies(BookDisplayModel formData) {
        try {
            List<Copy_Entity> listCopy = copyDAO.findByIdBook(bookId);
            int currentCopyCount = listCopy.size();
            int newCopyCount = formData.getCopyCount();

            // Cập nhật thông tin cho tất cả copies hiện có
            for(Copy_Entity c : listCopy) {
                c.setLocation(formData.getLocation() != null ? formData.getLocation().trim() : "");
                c.setCost(formData.getCost());
                copyDAO.update(c);
            }

            // Xử lý thêm/xóa copies
            if (newCopyCount < currentCopyCount) {
                // Xóa copies thừa (chỉ xóa những bản Available)
                int toDelete = currentCopyCount - newCopyCount;
                int deleted = 0;
                for (Copy_Entity c : listCopy) {
                    if (deleted >= toDelete) break;
                    if ("Available".equals(c.getStatus())) {
                        copyDAO.delete(c.getCopy_id());
                        deleted++;
                    }
                }

                if (deleted < toDelete) {
                    view.showInfo("Chỉ có thể xóa " + deleted + " bản sao (các bản còn lại đang được mượn)");
                }

            } else if (newCopyCount > currentCopyCount) {
                // Thêm copies mới
                int toAdd = newCopyCount - currentCopyCount;
                for(int i = 0; i < toAdd; i++) {
                    Copy_Entity cp = new Copy_Entity();
                    cp.setBook_id(bookId);
                    cp.setCost(formData.getCost());
                    cp.setLocation(formData.getLocation() != null ? formData.getLocation().trim() : "");
                    cp.setStatus("Available");
                    copyDAO.create(cp);
                }
            }
        } catch (Exception e) {
            view.showError("Lỗi khi cập nhật bản sao: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện Cancel
     */
    private void handleCancel() {
        if (view.isEditMode()) {
            // Đang ở chế độ edit, hỏi xác nhận
            if (view.confirm("Bạn có chắc chắn muốn hủy các thay đổi?")) {
                view.setEditMode(false);
                loadBookData(); // Reload dữ liệu gốc
            }
        } else {
            // Đóng form
            view.closeForm();
        }
    }

    /**
     * Hiển thị view
     */
    public void showView() {
        view.setVisible(true);
    }
}