package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.dao.impl.Staff_DAO;
import com.library.model.Account_Entity;
import com.library.model.Staff_Entity;
import com.library.view.*;

import javax.swing.*;
import java.util.*;


public class AccountManagementController {

    private final AccountManagementView view;
    private final Account_DAO accountDAO;
    private final Staff_DAO staffDAO;

    private List<Account_Entity> accountList = new ArrayList<>();
    private Account_Entity selectedAccount;

    public AccountManagementController() {
        this.view = new AccountManagementView();
        this.accountDAO = new Account_DAO();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadDataFromDatabase();

        view.setVisible(true);
    }


    private void bindEvents() {
        // Search realtime
        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchAccounts();
            }
        });

        // Click nút search
        view.getBtnSearch().addActionListener(e -> searchAccounts());

        // Chọn dòng -> enable delete
        view.getTblAccounts().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onAccountSelected();
        });

        // Double click -> detail
        view.getTblAccounts().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openAccountDetails();
            }
        });

        // Add
        view.getBtnAddAccount().addActionListener(e -> openAddAccountForm());

        // Refresh
        view.getBtnRefresh().addActionListener(e -> refreshData(true));

        // Delete
        view.getBtnDelete().addActionListener(e -> deleteAccount());

        // Close
        view.getBtnClose().addActionListener(e -> {
            view.dispose();
            new HomeView().setVisible(true);
        });
    }

    private void loadDataFromDatabase() {
        try {
            List<Account_Entity> accounts = accountDAO.findAll();

            if (accounts != null && !accounts.isEmpty()) {
                accountList = accounts;
                renderToView(accountList);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Không có dữ liệu tài khoản trong hệ thống!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                accountList = new ArrayList<>();
                renderToView(accountList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải dữ liệu từ database: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderToView(List<Account_Entity> accounts) {
        // Chuẩn bị map staff theo accountId để View không phải gọi DAO
        Map<Integer, Staff_Entity> staffByAccountId = new HashMap<>();
        if (accounts != null) {
            for (Account_Entity a : accounts) {
                try {
                    Staff_Entity s = staffDAO.findByAccountId(a.getAccount_id()).orElse(null);
                    if (s != null) staffByAccountId.put(a.getAccount_id(), s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        view.renderAccounts(accounts, staffByAccountId);
        view.setDeleteEnabled(false);
        selectedAccount = null;
    }

    private void searchAccounts() {
        String searchText = view.getTxtSearch().getText();
        view.applySearchFilter(searchText);
    }

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

    private void openAccountDetails() {
        if (selectedAccount != null) {
            // Mở theo MVC: Controller sẽ tự tạo View và show
            new AccountDetailController(selectedAccount.getAccount_id(), this);
        } else {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn một tài khoản!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }


    private void openAddAccountForm() {
        try {
            List<Staff_Entity> availableStaff = staffDAO.findStaffWithoutAccount();

            if (availableStaff == null || availableStaff.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "⚠️ Không có nhân viên nào chưa có tài khoản!\nVui lòng tạo nhân viên mới trước.",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            AddAccountController addForm = new AddAccountController(this);


        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi mở form tạo tài khoản: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reloadAfterChildChanged() {
        refreshData(false);
    }

    private void deleteAccount() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn tài khoản cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Staff_Entity staff = staffDAO.findByAccountId(selectedAccount.getAccount_id()).orElse(null);
            String staffName = staff != null ? staff.getStaff_name() : "N/A";

            int confirm = JOptionPane.showConfirmDialog(view,
                    "⚠️ Bạn có chắc chắn muốn xóa tài khoản?\n\n" +
                            "Username: " + selectedAccount.getUsername() + "\n" +
                            "Nhân viên: " + staffName + "\n\n" +
                            "Hành động này không thể hoàn tác!",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) return;

            accountDAO.delete(selectedAccount.getAccount_id());

            // Update local list
            accountList.remove(selectedAccount);
            selectedAccount = null;

            renderToView(accountList);

            JOptionPane.showMessageDialog(view,
                    "✅ Đã xóa tài khoản thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "❌ Không thể xóa tài khoản!\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData(boolean showMessage) {
        try {
            List<Account_Entity> accounts = accountDAO.findAll();
            accountList = (accounts == null) ? new ArrayList<>() : accounts;

            renderToView(accountList);
            view.clearSearch();

            if (showMessage) {
                JOptionPane.showMessageDialog(view,
                        "✅ Đã làm mới danh sách thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi làm mới dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public void disposeView(){
        view.dispose();
    }

}
