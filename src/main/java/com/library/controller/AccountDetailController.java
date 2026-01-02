package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.dao.impl.Staff_DAO;
import com.library.model.Account_Entity;
import com.library.model.Staff_Entity;
import com.library.view.AccountDetailView;

import javax.swing.*;

/**
 * Controller quản lý màn hình chi tiết tài khoản (Account Detail).
 * <p>
 * Chức năng:
 * <ul>
 *   <li>Load dữ liệu account + staff theo accountId</li>
 *   <li>Cho phép bật chế độ chỉnh sửa (Edit mode)</li>
 *   <li>Lưu cập nhật (Save)</li>
 *   <li>Hủy chỉnh sửa hoặc đóng form (Cancel/Close)</li>
 * </ul>
 */
public class AccountDetailController {

    private final AccountDetailView view;
    private final Account_DAO accountDAO;
    private final Staff_DAO staffDAO;
    private final int accountId;
    private boolean isEditMode = false;
    private final AccountManagementController parent;

    /**
     * Khởi tạo controller chi tiết tài khoản.
     */
    public AccountDetailController(int accountId, AccountManagementController parent) {
        this.accountId = accountId;
        this.parent = parent;

        this.view = new AccountDetailView();
        this.accountDAO = new Account_DAO();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadAccountData(accountId);
        view.setEditMode(false);

        view.setVisible(true);
    }

    /**
     * Gắn sự kiện cho các nút Edit / Save / Cancel.
     */
    private void bindEvents() {
        view.getBtnEdit().addActionListener(e -> enableEditMode());
        view.getBtnSave().addActionListener(e -> saveAccount());
        view.getBtnCancel().addActionListener(e -> cancelOrClose());
    }

    /**
     * Tải dữ liệu account và staff theo accountId, sau đó fill vào view.
     * Nếu không tìm thấy account thì báo lỗi và đóng form.
     */
    private void loadAccountData(int accountId) {
        try {
            Account_Entity account = accountDAO.findById(accountId).orElse(null);

            if (account == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy tài khoản với ID = " + accountId,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.dispose();
                return;
            }

            Staff_Entity staff = staffDAO.findByAccountId(accountId).orElse(null);

            Integer staffId = (staff != null) ? staff.getStaff_id() : null;
            String staffName = (staff != null) ? staff.getStaff_name() : null;
            String position = (staff != null) ? staff.getPosition() : null;
            String email = (staff != null) ? staff.getEmail() : null;
            String phone = (staff != null) ? staff.getPhone() : null;

            view.fillData(
                    account.getAccount_id(),
                    account.getUsername(),
                    account.getPassword(),
                    account.getIs_active() == 1,
                    staffId,
                    staffName,
                    position,
                    email,
                    phone
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải dữ liệu tài khoản: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Bật chế độ chỉnh sửa: cho phép nhập liệu và hiển thị các control phù hợp.
     */
    private void enableEditMode() {
        isEditMode = true;
        view.setEditMode(true);
    }

    /**
     * Validate dữ liệu và cập nhật account vào database.
     * Sau khi lưu thành công:
     * - tắt edit mode
     * - gọi parent reload lại danh sách (nếu có)
     */
    private void saveAccount() {
        String username = view.getTxtUsername().getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập username!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getTxtUsername().requestFocus();
            return;
        }

        char[] pwChars = view.getTxtPassword().getPassword();
        if (pwChars == null || pwChars.length == 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập password!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getTxtPassword().requestFocus();
            return;
        }

        String password = new String(pwChars);
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(view, "Password phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getTxtPassword().requestFocus();
            return;
        }

        try {
            int id = Integer.parseInt(view.getTxtAccountId().getText().trim());
            String staffIdText = view.getTxtStaffId().getText().trim();
            if (staffIdText.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Không xác định được StaffId của tài khoản này!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int staffId = Integer.parseInt(staffIdText);

            Account_Entity account = new Account_Entity();
            account.setAccount_id(id);
            account.setUsername(username);
            account.setPassword(password);
            account.setIs_active(view.getChkIsActive().isSelected() ? (byte) 1 : (byte) 0);
            account.setStaff_id(staffId);

            accountDAO.update(account);

            String status = view.getChkIsActive().isSelected() ? "Hoạt động" : "Khóa";
            JOptionPane.showMessageDialog(view,
                    "✅ Đã cập nhật thông tin tài khoản thành công!\n\n" +
                            "ID Tài khoản: " + account.getAccount_id() + "\n" +
                            "Username: " + account.getUsername() + "\n" +
                            "Trạng thái: " + status,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            isEditMode = false;
            view.setEditMode(false);

            if (parent != null) {
                parent.reloadAfterChildChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "❌ Lỗi khi cập nhật tài khoản:\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Nút Cancel có 2 hành vi:
     * <ul>
     *   <li>Nếu không ở edit mode: đóng form</li>
     *   <li>Nếu đang edit mode: hỏi xác nhận, nếu đồng ý thì reload lại dữ liệu và thoát edit mode</li>
     * </ul>
     */
    private void cancelOrClose() {
        if (!isEditMode) {
            view.dispose();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "⚠️ Bạn có chắc chắn muốn hủy?\nCác thay đổi chưa lưu sẽ bị mất.",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            loadAccountData(accountId);
            isEditMode = false;
            view.setEditMode(false);
        }
    }
}
