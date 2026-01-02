package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.dao.impl.Staff_DAO;
import com.library.model.Account_Entity;
import com.library.model.Staff_Entity;
import com.library.view.*;

import javax.swing.*;
import java.util.*;

/**
 * Controller quản lý danh sách tài khoản trong hệ thống.
 *
 * Chức năng chính:
 * - Hiển thị danh sách tài khoản
 * - Tìm kiếm tài khoản
 * - Xem chi tiết tài khoản
 * - Thêm tài khoản mới
 * - Xóa tài khoản
 * - Làm mới dữ liệu
 */
public class AccountManagementController {

    private final AccountManagementView view;
    private final Account_DAO accountDAO;
    private final Staff_DAO staffDAO;

    private List<Account_Entity> accountList = new ArrayList<>();
    private Account_Entity selectedAccount;

    /**
     * Constructor khởi tạo màn hình quản lý tài khoản.
     * - Khởi tạo View, DAO
     * - Bind sự kiện
     * - Load dữ liệu ban đầu từ database
     */
    public AccountManagementController() {
        this.view = new AccountManagementView();
        this.accountDAO = new Account_DAO();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadDataFromDatabase();

        view.setVisible(true);
    }

    /**
     * Gắn toàn bộ sự kiện cho các thành phần trong View:
     * - Tìm kiếm
     * - Chọn dòng
     * - Double click xem chi tiết
     * - Thêm / Xóa / Refresh
     * - Đóng màn hình
     */
    private void bindEvents() {

        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchAccounts();
            }
        });

        view.getBtnSearch().addActionListener(e -> searchAccounts());
        view.getTblAccounts().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onAccountSelected();
        });

        view.getTblAccounts().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openAccountDetails();
            }
        });

        view.getBtnAddAccount().addActionListener(e -> openAddAccountForm());
        view.getBtnRefresh().addActionListener(e -> refreshData(true));
        view.getBtnDelete().addActionListener(e -> deleteAccount());
        view.getBtnClose().addActionListener(e -> {
            view.dispose();
            new HomeView().setVisible(true);
        });
    }

    /**
     * Load danh sách tài khoản từ database
     * và hiển thị lên View.
     */
    private void loadDataFromDatabase() {
        try {
            List<Account_Entity> accounts = accountDAO.findAll();

            if (accounts != null && !accounts.isEmpty()) {
                accountList = accounts;
                renderToView(accountList);
            } else {
                JOptionPane.showMessageDialog(
                        view,
                        "Không có dữ liệu tài khoản trong hệ thống!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                accountList = new ArrayList<>();
                renderToView(accountList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Lỗi khi tải dữ liệu từ database: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Đổ dữ liệu tài khoản lên View.
     * Chuẩn bị dữ liệu Staff tương ứng để View
     * không phải gọi DAO trực tiếp.
     *
     * @param accounts danh sách tài khoản cần hiển thị
     */
    private void renderToView(List<Account_Entity> accounts) {

        Map<Integer, Staff_Entity> staffByAccountId = new HashMap<>();

        if (accounts != null) {
            for (Account_Entity a : accounts) {
                try {
                    Staff_Entity s = staffDAO
                            .findByAccountId(a.getAccount_id())
                            .orElse(null);
                    if (s != null)
                        staffByAccountId.put(a.getAccount_id(), s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        view.renderAccounts(accounts, staffByAccountId);
        view.setDeleteEnabled(false);
        selectedAccount = null;
    }

    /**
     * Thực hiện tìm kiếm tài khoản theo từ khóa
     * người dùng nhập.
     */
    private void searchAccounts() {
        String searchText = view.getTxtSearch().getText();
        view.applySearchFilter(searchText);
    }

    /**
     * Xử lý khi người dùng chọn một dòng trong bảng.
     * Dùng để xác định tài khoản đang được chọn
     * và bật/tắt nút xóa.
     */
    private void onAccountSelected() {
        int selectedRow = view.getTblAccounts().getSelectedRow();

        if (selectedRow >= 0) {
            int modelRow = view.getTblAccounts().convertRowIndexToModel(selectedRow);
            int accountId = (int) view.getTableModel().getValueAt(modelRow, 0);

            selectedAccount = accountList.stream()
                    .filter(a -> a.getAccount_id() == accountId)
                    .findFirst()
                    .orElse(null);

            view.setDeleteEnabled(selectedAccount != null);
        } else {
            selectedAccount = null;
            view.setDeleteEnabled(false);
        }
    }

    /**
     * Mở màn hình chi tiết tài khoản
     * khi người dùng double click vào bảng.
     */
    private void openAccountDetails() {
        if (selectedAccount != null) {
            new AccountDetailController(
                    selectedAccount.getAccount_id(),
                    this
            );
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Vui lòng chọn một tài khoản!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    /**
     * Mở form thêm tài khoản mới.
     * Kiểm tra trước xem còn nhân viên nào
     * chưa có tài khoản hay không.
     */
    private void openAddAccountForm() {
        try {
            List<Staff_Entity> availableStaff =
                    staffDAO.findStaffWithoutAccount();

            if (availableStaff == null || availableStaff.isEmpty()) {
                JOptionPane.showMessageDialog(
                        view,
                        "⚠️ Không có nhân viên nào chưa có tài khoản!\n" +
                                "Vui lòng tạo nhân viên mới trước.",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            new AddAccountController(this);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Lỗi khi mở form tạo tài khoản: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Callback được gọi từ các controller con
     * sau khi dữ liệu tài khoản thay đổi.
     */
    public void reloadAfterChildChanged() {
        refreshData(false);
    }

    /**
     * Xóa tài khoản đang được chọn.
     * Có xác nhận trước khi thực hiện.
     */
    private void deleteAccount() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(
                    view,
                    "Vui lòng chọn tài khoản cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Staff_Entity staff =
                    staffDAO.findByAccountId(selectedAccount.getAccount_id())
                            .orElse(null);

            String staffName =
                    staff != null ? staff.getStaff_name() : "N/A";

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "⚠️ Bạn có chắc chắn muốn xóa tài khoản?\n\n" +
                            "Username: " + selectedAccount.getUsername() + "\n" +
                            "Nhân viên: " + staffName + "\n\n" +
                            "Hành động này không thể hoàn tác!",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            accountDAO.delete(selectedAccount.getAccount_id());

            accountList.remove(selectedAccount);
            selectedAccount = null;

            renderToView(accountList);

            JOptionPane.showMessageDialog(
                    view,
                    "✅ Đã xóa tài khoản thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "❌ Không thể xóa tài khoản!\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Làm mới dữ liệu tài khoản từ database.
     */
    private void refreshData(boolean showMessage) {
        try {
            List<Account_Entity> accounts = accountDAO.findAll();
            accountList = (accounts == null)
                    ? new ArrayList<>()
                    : accounts;

            renderToView(accountList);
            view.clearSearch();

            if (showMessage) {
                JOptionPane.showMessageDialog(
                        view,
                        "✅ Đã làm mới danh sách thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    view,
                    "Lỗi khi làm mới dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
