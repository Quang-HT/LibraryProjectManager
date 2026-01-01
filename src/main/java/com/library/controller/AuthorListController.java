package com.library.controller;

import com.library.dao.impl.Author_DAO;
import com.library.model.Author_Entity;
import com.library.view.AuthorListView;
import com.library.view.HomeView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorListController {

    private final AuthorListView view;

    private final Author_DAO authorDAO = new Author_DAO();

    // Danh sách gốc từ DB (full)
    private List<Author_Entity> authorList = new ArrayList<>();

    // author đang chọn
    private Author_Entity selectedAuthor;

    public AuthorListController(AuthorListView view) {
        this.view = view;
        bindEvents();
        loadDataFromDatabase();
        view.setVisible(true);
    }

    private void bindEvents() {
        // Search click
        view.getBtnSearch().addActionListener(e -> searchAuthors());

        // Search typing
        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchAuthors();
            }
        });

        // Add
        view.getBtnAddAuthor().addActionListener(e -> openAddAuthorForm());

        // Refresh
        view.getBtnRefresh().addActionListener(e -> refreshData());

        // Close
        view.getBtnClose().addActionListener(e -> goHomeAndClose());

        // Selection
        view.getTblAuthors().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onAuthorSelected();
        });

        // Double click
        view.getTblAuthors().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openAuthorDetails();
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        try {
            authorList = authorDAO.findAll();     // full list
            view.setAuthors(authorList);          // show full
            selectedAuthor = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách tác giả từ CSDL.\n" + ex.getMessage());
        }
    }

    private void refreshData() {
        view.clearSearch();
        loadDataFromDatabase();
        view.showInfo("Đã làm mới danh sách!");
    }

    // ✅ Search thủ công: lọc list rồi đổ lại table
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
            // nếu muốn search cả mô tả thì bật thêm:
            // String desc = (a.getDescription() == null) ? "" : a.getDescription().toLowerCase();

            if (name.contains(keyword)) {
                filtered.add(a);
            }
        }

        view.setAuthors(filtered);
        selectedAuthor = null;
    }

    private void onAuthorSelected() {
        // Lấy trực tiếp entity đang chọn từ view (chuẩn khi đã lọc)
        selectedAuthor = view.getSelectedAuthorEntityOrNull();
        System.out.println("Selected author: " + (selectedAuthor != null ? selectedAuthor.getAuthor_name() : "null"));
    }

    private void openAuthorDetails() {
        if (selectedAuthor == null) {
            view.showError("Vui lòng chọn 1 tác giả trước khi xem chi tiết!");
            return;
        }
        new com.library.controller.AuthorDetailController(selectedAuthor.getAuthor_id());

    }

    private void openAddAuthorForm() {
        new com.library.controller.AddAuthorController(() -> {
            // nếu muốn refresh danh sách tác giả thì gọi ở đây
        });

    }

    private void goHomeAndClose() {
        new HomeView().setVisible(true);
        view.closeWindow();
    }

    public Author_Entity getSelectedAuthor() {
        return selectedAuthor;
    }

    // Main test (tuỳ bạn có cần không)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            AuthorListView view = new AuthorListView();
            new AuthorListController(view);
            view.setVisible(true);
        });
    }
}
