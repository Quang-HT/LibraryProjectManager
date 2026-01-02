package com.library.controller;

import com.library.dao.impl.Author_DAO;
import com.library.model.Author_Entity;
import com.library.view.AuthorListView;
import com.library.view.HomeView;

import java.util.ArrayList;
import java.util.List;

public class AuthorManagerController {

    private final AuthorListView view;

    private final Author_DAO authorDAO = new Author_DAO();

    private List<Author_Entity> authorList = new ArrayList<>();

    private Author_Entity selectedAuthor;

    /**
     * Hàm Controller của AuthorManager
     * Thực thi thứ tự các danh sách công việc
     */
    public AuthorManagerController(AuthorListView view) {
        this.view = view;
        bindEvents();
        loadDataFromDatabase();
        view.setVisible(true);
    }

    /**
     * Đăng ký các sự kiện cho các Button
     */
    private void bindEvents() {
        view.getBtnSearch().addActionListener(e -> searchAuthors());

        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAuthors();
            }
        });

        view.getBtnAddAuthor().addActionListener(e -> openAddAuthorForm());

        view.getBtnRefresh().addActionListener(e -> refreshData());

        view.getBtnClose().addActionListener(e -> goHomeAndClose());

        view.getTblAuthors().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onAuthorSelected();
        });

        view.getTblAuthors().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openAuthorDetails();
                }
            }
        });
    }

    /**
     * Đưa dữ liệu danh sách các tác giả lên trên View
     */
    private void loadDataFromDatabase() {
        try {
            authorList = authorDAO.findAll();
            view.setAuthors(authorList);
            selectedAuthor = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách tác giả từ CSDL.\n" + ex.getMessage());
        }
    }

    /**
     * Làm mới lại dữ liệu
     */
    private void refreshData() {
        view.clearSearch();
        loadDataFromDatabase();
        view.showInfo("Đã làm mới danh sách!");
    }

    /**
     * Tìm kiếm tác giả
     */
    private void searchAuthors() {
        String keyword = view.getTxtSearch().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            view.setAuthors(authorList);
            selectedAuthor = null;
            return;
        }

        List<Author_Entity> filtered = new ArrayList<>();
        for (Author_Entity a : authorList) {
            String name = (a.getAuthor_name() == null) ? "" : a.getAuthor_name().toLowerCase();

            if (name.contains(keyword)) {
                filtered.add(a);
            }
        }

        view.setAuthors(filtered);
        selectedAuthor = null;
    }

    /**
     * Khi người dùng click vào tác giả -> mở form Detail Author xem thông tin tác giả
     */
    private void onAuthorSelected() {
        selectedAuthor = view.getSelectedAuthorEntityOrNull();
        System.out.println("Selected author: " + (selectedAuthor != null ? selectedAuthor.getAuthor_name() : "null"));
    }

    /**
     * Mở View xem chi tiết tác giả
     */
    private void openAuthorDetails() {
        if (selectedAuthor == null) {
            view.showError("Vui lòng chọn 1 tác giả trước khi xem chi tiết!");
            return;
        }
        new AuthorDetailController(selectedAuthor.getAuthor_id());

    }

    /**
     * Mở View thêm mới tác giả
     */
    private void openAddAuthorForm() {
        new AddAuthorController(this::refreshData);

    }

    /**
     * Thoát View AuthorManager
     */
    private void goHomeAndClose() {
        new HomeView().setVisible(true);
        view.closeWindow();
    }

}
