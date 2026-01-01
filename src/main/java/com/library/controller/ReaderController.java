package com.library.controller;

import com.library.dao.impl.Book_DAO;
import com.library.model.Book_Entity;
import com.library.model.display.BookDisplayModel;
import com.library.service.FIND;
import com.library.view.ReaderView;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.stream.Collectors;

public class ReaderController {

    private final ReaderView view;

    private final List<BookDisplayModel> allBooks = new ArrayList<>();
    private List<BookDisplayModel> filteredBooks = new ArrayList<>();

    private String currentCategory = "Tất cả";
    private String currentAuthor = "Tất cả";
    private String searchQuery = "";
    private boolean showTopBorrowed = false;

    /**
     * Hàm Controller xét các thứ tự hàm cần thực thi
     * Lấy hết dữ liệu cần thiết từ DB lên
     */
    public ReaderController() {
        this.view = new ReaderView();
        loadDataFromDatabase();
        initFilterOptions();
        bindEvents();
        applyFilters();
        view.setVisible(true);
    }


    /**
     * Lấy hết dữ liệu cần thiết từ DB lên rồi gắn cho BookDisplayModel
     */
    private void loadDataFromDatabase() {
        allBooks.clear();

        Book_DAO bookDao = new Book_DAO();
        List<Book_Entity> listBook = bookDao.findAll();
        FIND find = new FIND();

        for (Book_Entity book : listBook) {
            String author = find.FindNameAuthor(book.getBook_id());
            String category = find.FindNameCategory(book.getBook_id());
            int year = book.getPublish_year();
            int copyCount = find.countCopy(book.getBook_id());
            String location = book.getLocation();
            int borrowCount = find.countBorrowByIdBook(book.getBook_id());

            BookDisplayModel m = new BookDisplayModel();
            m.setBookId(book.getBook_id());
            m.setTitle(book.getTitle());
            m.setAuthor(author);
            m.setCategory(category);
            m.setYear(year);
            m.setCost(0);
            m.setLocation(location);
            m.setBorrowCount(borrowCount);
            m.setCopyCount(copyCount);
            m.setDescription(book.getDescription());
            m.setPublisher("");

            allBooks.add(m);
        }

        filteredBooks = new ArrayList<>(allBooks);
    }

    /**
     * Đưa lên các Option về Categories/Authors
     */
    private void initFilterOptions() {
        List<String> categories = new ArrayList<>();
        categories.add("Tất cả");
        categories.addAll(
                allBooks.stream()// Lấy tất cả các sách
                        .map(BookDisplayModel::getCategory) // Lấy tất cả các Category có trong sách
                        .filter(Objects::nonNull) // Bỏ null
                        .distinct() // Bỏ trùng
                        .sorted() // Sắp xếp
                        .toList() // Chuyển danh sách sang List
        );

        List<String> authors = new ArrayList<>();
        authors.add("Tất cả");
        authors.addAll(
                allBooks.stream()
                        .map(BookDisplayModel::getAuthor)
                        .filter(Objects::nonNull)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );

        view.setCategoryOptions(categories);
        view.setAuthorOptions(authors);
    }

    /**
     *Hàm đăng ký sự kiện
     * - Tìm kiếm
     * - Tìm kiếm dựa trên Category
     * - Tìm kiếm dựa trên Tác giả
     */
    private void bindEvents() {
        view.getSearchField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { // Tìm kiếm ngay trong khi nhập
                searchQuery = view.getSearchField().getText().trim();
                applyFilters();
            }
        });

        view.getCategoryComboBox().addActionListener(e -> {
            Object sel = view.getCategoryComboBox().getSelectedItem();
            currentCategory = (sel == null) ? "Tất cả" : sel.toString();
            applyFilters();
        });

        view.getAuthorComboBox().addActionListener(e -> {
            Object sel = view.getAuthorComboBox().getSelectedItem();
            currentAuthor = (sel == null) ? "Tất cả" : sel.toString();
            applyFilters();
        });

        view.getTopBorrowedButton().addActionListener(e -> toggleTopBorrowed());
        view.getResetButton().addActionListener(e -> resetFilters());

        // Thoát ra trang Login
        view.getExitButton().addActionListener(e -> {
            new LoginController();
            view.dispose();
        });
    }

    /**
     * Phân loại sách theo từng tiêu chí
     * Category = "Tất cả" || Book.getCategory = Lụa chọn của người dùng
     * Author = "Tất cả" || Book.getAuthor = lựa chọn của người dùng
     * Dựa trên tìm kiếm mà người dùng đã ghi trên thanh tìm kiếm
     * Thỏa mãn 3 điều kiện trên, sách mới được cho hiển thị
     */
    private void applyFilters() {
        filteredBooks = allBooks.stream()
                .filter(book -> {
                    boolean categoryMatch = currentCategory.equals("Tất cả")
                            || safeEquals(book.getCategory(), currentCategory);

                    boolean authorMatch = currentAuthor.equals("Tất cả")
                            || safeEquals(book.getAuthor(), currentAuthor);

                    boolean searchMatch = searchQuery.isEmpty()
                            || String.valueOf(book.getBookId()).contains(searchQuery)
                            || (book.getTitle() != null && book.getTitle().toLowerCase().contains(searchQuery.toLowerCase()));

                    return categoryMatch && authorMatch && searchMatch;
                })
                .collect(Collectors.toList());

        if (showTopBorrowed) {
            filteredBooks.sort(Comparator.comparingInt(BookDisplayModel::getBorrowCount).reversed());
        }

        view.setTopBorrowedState(showTopBorrowed);
        view.renderBooks(filteredBooks);
    }

    /**
     * Hàm kiểm tra top được mượn
     */
    private void toggleTopBorrowed() {
        showTopBorrowed = !showTopBorrowed;

        if (showTopBorrowed) {
            boolean allZero = allBooks.stream().allMatch(b -> b.getBorrowCount() == 0);
            if (allZero) view.showInfo("Chưa có dữ liệu số lượt mượn để xem Top!");
        }

        applyFilters();
    }

    /**
     * Reset lại các lựa chọn
     */
    private void resetFilters() {
        searchQuery = "";
        currentCategory = "Tất cả";
        currentAuthor = "Tất cả";
        showTopBorrowed = false;

        view.getSearchField().setText("");
        view.getCategoryComboBox().setSelectedIndex(0);
        view.getAuthorComboBox().setSelectedIndex(0);

        applyFilters();
    }

    /**
     * So sánh an toàn
     */
    private boolean safeEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }
}
