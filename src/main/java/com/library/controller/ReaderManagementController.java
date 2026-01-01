package com.library.controller;

import com.library.dao.impl.Reader_DAO;
import com.library.model.Reader_Entity;
import com.library.model.display.ReaderRow;
import com.library.view.HomeView;
import com.library.view.ReaderManagementView;

import java.util.ArrayList;
import java.util.List;

public class ReaderManagementController {

    private final ReaderManagementView view;
    private List<ReaderRow> allReaders = new ArrayList<>();
    private ReaderRow selectedReader;

    /**
     * Hàm Controller của Quản lý Độc giả
     */
    public ReaderManagementController() {
        this.view = new ReaderManagementView();
        bindEvents();
        loadFromDatabase();
        view.setVisible(true);
    }

    /**
     * Thêm các nút sự kiến cho các Button
     */
    private void bindEvents() {
        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchReaders();
            }
        });

        view.getBtnSearch().addActionListener(e -> searchReaders());
        view.getBtnAddReader().addActionListener(e -> openAddReaderForm());
        view.getBtnRefresh().addActionListener(e -> refreshData());
        view.getBtnClose().addActionListener(e -> goHomeAndClose());
        view.getTblReaders().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onReaderSelected();
        });

        // double click -> details
        view.getTblReaders().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) openReaderDetails(); // Click 2 lần để chọn xem thông tin Độc giả
            }
        });
    }

    /**
     * Lấy dữ liệu cần thiết từ Database cho Reader
     */
    private void loadFromDatabase() {
        try {
            List<Reader_Entity> list = new Reader_DAO().findAll();
            List<ReaderRow> rows = new ArrayList<>();

            for (Reader_Entity r : list) {
                rows.add(new ReaderRow(
                        r.getReader_id(),
                        r.getReader_name(),
                        r.getCccd(),
                        r.getPhone(),
                        r.getEmail(),
                        r.getAddress(),
                        String.valueOf(r.getRegistered_date()),
                        "Hoạt động"
                ));
            }

            allReaders = rows;
            view.setReaders(allReaders);
            selectedReader = null;

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Không thể tải danh sách độc giả từ CSDL.\n" + ex.getMessage());
        }
    }

    /**
     * Tìm kiếm người đọc
     */
    private void searchReaders() {
        String keyword = view.getTxtSearch().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            view.setReaders(allReaders);
            selectedReader = null;
            return;
        }

        List<ReaderRow> filtered = new ArrayList<>();
        for (ReaderRow r : allReaders) {
            String name = safeLower(r.getFullName());
            String cccd = safeLower(r.getCccd());
            String phone = safeLower(r.getPhoneNumber());
            String email = safeLower(r.getEmail());
            String addr = safeLower(r.getAddress());

            if (name.contains(keyword)
                    || cccd.contains(keyword)
                    || phone.contains(keyword)
                    || email.contains(keyword)
                    || addr.contains(keyword)) {
                filtered.add(r);
            }
        }

        view.setReaders(filtered);
        selectedReader = null;
    }

    /**
     * Kiểm tra an toàn trước khi sử dụng
     */
    private String safeLower(String s) {
        return (s == null) ? "" : s.toLowerCase();
    }

    /**
     * Lấy Độc giả được chọn
     */
    private void onReaderSelected() {
        selectedReader = view.getSelectedReaderOrNull();
        System.out.println("Selected reader: " + (selectedReader != null ? selectedReader.getFullName() : "null"));
    }

    /**
     * Mở Bảng thông tin độc giả đã được chọn
     */
    private void openReaderDetails() {
        if (selectedReader == null) {
            view.showWarning("Vui lòng chọn một độc giả!");
            return;
        }
        new ReaderDetailController(selectedReader.getId());

    }

    /**
     * Mở View thêm Độc giả
     */
    private void openAddReaderForm() {
        new AddReaderController(this::refreshData);
    }

    /**
     * Làm mới dữ liệu
     */
    private void refreshData() {
        view.clearSearch();
        loadFromDatabase();
        view.showInfo("Đã làm mới danh sách!");
    }

    /**
     * Thoát khỏi View
     */
    private void goHomeAndClose() {
        new HomeView().setVisible(true);
        view.dispose();
    }
}
