package com.library.controller;

import com.library.dao.impl.Book_DAO;
import com.library.model.Book_Entity;
import com.library.model.display.BookDisplayModel;
import com.library.service.FIND;
import com.library.view.AddBookFormView;
import com.library.view.BookManagerFrame;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CONTROLLER - Điều phối giữa View và Model
 * Xử lý logic nghiệp vụ và các sự kiện từ View
 * Cập nhật View khi dữ liệu thay đổi
 */
public class BookManagerController {
    private BookManagerFrame view;

    private Book_DAO bookDAO;
    private FIND findService;

    private List<BookDisplayModel> allBooks;

    private String currentCategory = "Tất cả";
    private String searchQuery = "";
    private boolean showTopBorrowed = false;

    public BookManagerController() {
        view = new BookManagerFrame();
        view.setController(this);

        bookDAO = new Book_DAO();
        findService = new FIND();

        allBooks = new ArrayList<>();
        initializeData();

        updateCategoryComboBox();
        updateView();

        view.setVisible(true);
    }

    /**
     * Khởi tạo dữ liệu từ database
     */
    private void initializeData() {
        List<Book_Entity> listBook = bookDAO.findAll();
        allBooks.clear();

        for (Book_Entity book : listBook) {
            String author = findService.FindNameAuthor(book.getBook_id());
            String category = findService.FindNameCategory(book.getBook_id());
            int year = book.getPublish_year();
            int cost = (int) findService.FindCost(book.getBook_id());
            String location = book.getLocation();
            int borrowCount = findService.countBorrowByIdBook(book.getBook_id());

            allBooks.add(new BookDisplayModel(
                    book.getBook_id(),
                    book.getTitle(),
                    author,
                    category,
                    year,
                    cost,
                    location,
                    borrowCount
            ));
        }

        allBooks.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
    }

    /**
     * Lọc danh sách sách theo các điều kiện hiện tại
     */
    private List<BookDisplayModel> getFilteredBooks() {
        List<BookDisplayModel> filtered = allBooks.stream()
                .filter(book -> {
                    // Lọc theo thể loại
                    boolean categoryMatch = currentCategory.equals("Tất cả")
                            || book.getCategory().equals(currentCategory);

                    // Lọc theo từ khóa tìm kiếm (tên hoặc tác giả)
                    boolean searchMatch = searchQuery.isEmpty()
                            || book.getTitle().toLowerCase().contains(searchQuery.toLowerCase())
                            || book.getAuthor().toLowerCase().contains(searchQuery.toLowerCase());

                    return categoryMatch && searchMatch;
                })
                .collect(Collectors.toList());

        // Nếu bật xem Top → sắp xếp theo số lượt mượn (giảm dần)
        if (showTopBorrowed) {
            filtered.sort((b1, b2) -> Integer.compare(b2.getBorrowCount(), b1.getBorrowCount()));
        }

        return filtered;
    }

    /**
     * Lấy danh sách các thể loại duy nhất
     */
    private String[] getUniqueCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("Tất cả");

        allBooks.stream()
                .map(BookDisplayModel::getCategory)
                .distinct()
                .sorted()
                .forEach(categories::add);

        return categories.toArray(new String[0]);
    }

    /**
     * Cập nhật View với dữ liệu mới
     */
    private void updateView() {
        List<BookDisplayModel> filteredBooks = getFilteredBooks();
        view.displayBooks(filteredBooks);
        view.updateTotalBooksLabel(filteredBooks.size());
    }

    /**
     * Cập nhật ComboBox thể loại trong View
     */
    private void updateCategoryComboBox() {
        String[] categories = getUniqueCategories();
        view.updateCategoryComboBox(categories, currentCategory);
    }

    /**
     * Làm mới toàn bộ dữ liệu
     */
    public void refreshData() {
        initializeData();
        updateCategoryComboBox();
        updateView();
    }

    /**
     * EVENT HANDLERS - Xử lý các sự kiện từ View
     */

    /**
     * Xử lý khi người dùng thay đổi text tìm kiếm
     */
    public void handleSearchTextChanged(String newSearchQuery) {
        this.searchQuery = newSearchQuery;
        updateView();
    }

    /**
     * Xử lý khi người dùng thay đổi thể loại
     */
    public void handleCategoryChanged(String newCategory) {
        this.currentCategory = (newCategory != null) ? newCategory : "Tất cả";
        updateView();
    }

    /**
     * Xử lý khi người dùng toggle nút Top sách mượn nhiều
     */
    public void handleToggleTopBorrowed() {
        showTopBorrowed = !showTopBorrowed;
        view.updateTopBorrowedButton(showTopBorrowed);
        updateView();
    }

    /**
     * Xử lý khi người dùng click vào một cuốn sách
     */
    public void handleBookClicked(int bookId) {
        BookDetailController dialog = new BookDetailController(bookId);
    }

    /**
     * Xử lý khi người dùng click nút Thêm sách
     */
    public void handleAddBook() {
        try {
            AddBookController addBookController = new AddBookController(new AddBookFormView());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Không thể mở form thêm sách: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}