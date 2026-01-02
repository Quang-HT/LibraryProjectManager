package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.dao.impl.Staff_DAO;
import com.library.model.Account_Entity;
import com.library.model.Staff_Entity;
import com.library.view.AddAccountView;

import java.util.List;

/**
 * Controller xử lý chức năng THÊM TÀI KHOẢN cho nhân viên.
 *
 * Chức năng chính:
 * - Load danh sách nhân viên chưa có tài khoản
 * - Validate dữ liệu account (username, password)
 * - Tạo tài khoản mới và lưu vào CSDL
 * - Thông báo kết quả và cập nhật màn hình cha (nếu có)
 */
public class AddAccountController {

    private final AddAccountView view;
    private final Account_DAO accountDAO;
    private final Staff_DAO staffDAO;

    private final AccountManagementController parent;

    /**
     * Constructor dùng khi mở form từ màn hình quản lý tài khoản.
     *
     * @param parent controller cha để callback reload dữ liệu
     */
    public AddAccountController(AccountManagementController parent) {
        this.parent = parent;
        this.view = new AddAccountView();
        this.accountDAO = new Account_DAO();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadEmployeesWithoutAccount();

        view.setVisible(true);
    }

    /**
     * Constructor mặc định (không có controller cha).
     * Thường dùng khi mở form độc lập.
     */
    public AddAccountController() {
        parent = null;
        this.view = new AddAccountView();
        this.accountDAO = new Account_DAO();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadEmployeesWithoutAccount();

        view.setVisible(true);
    }

    /**
     * Gắn sự kiện cho các nút trong form.
     */
    private void bindEvents() {
        view.getBtnSave().addActionListener(e -> saveAccount());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    /**
     * Load danh sách nhân viên chưa có tài khoản
     * để hiển thị lên combobox.
     */
    private void loadEmployeesWithoutAccount() {
        try {
            List<Staff_Entity> availableStaff = staffDAO.findStaffWithoutAccount();
            view.setStaffList(availableStaff);

            if (availableStaff == null || availableStaff.isEmpty()) {
                view.showInfo(
                        "⚠️ Không có nhân viên nào chưa có tài khoản!\n" +
                                "Vui lòng tạo nhân viên mới trước."
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Lỗi khi tải danh sách nhân viên: " + e.getMessage());
        }
    }

    /**
     * Xử lý khi người dùng nhấn nút "Lưu".
     * Bao gồm:
     * - Validate dữ liệu nhập
     * - Kiểm tra username trùng
     * - Tạo tài khoản mới
     * - Lưu vào CSDL
     */
    private void saveAccount() {

        // Validate Username
        String username = view.getTxtUsername().getText().trim();
        if (username.isEmpty()) {
            view.showError("Vui lòng nhập username!");
            view.getTxtUsername().requestFocus();
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            view.showError("Username chỉ được chứa chữ, số và dấu gạch dưới!");
            view.getTxtUsername().requestFocus();
            return;
        }

        char[] pw1 = view.getTxtPassword().getPassword();
        if (pw1 == null || pw1.length == 0) {
            view.showError("Vui lòng nhập password!");
            view.getTxtPassword().requestFocus();
            return;
        }
        String password = new String(pw1);
        if (password.length() < 6) {
            view.showError("Password phải có ít nhất 6 ký tự!");
            view.getTxtPassword().requestFocus();
            return;
        }

        String confirm = new String(view.getTxtConfirmPassword().getPassword());
        if (!password.equals(confirm)) {
            view.showError("Xác nhận password không khớp!");
            view.getTxtConfirmPassword().requestFocus();
            return;
        }

        Staff_Entity selectedStaff =
                (Staff_Entity) view.getCmbEmployee().getSelectedItem();
        if (selectedStaff == null) {
            view.showError("Vui lòng chọn nhân viên!");
            view.getCmbEmployee().requestFocus();
            return;
        }

        try {
            if (accountDAO.findByName(username).isPresent()) {
                view.showError("Username đã tồn tại! Vui lòng chọn username khác.");
                view.getTxtUsername().requestFocus();
                return;
            }

            Account_Entity newAccount = new Account_Entity();
            newAccount.setUsername(username);
            newAccount.setPassword(password);
            newAccount.setStaff_id(selectedStaff.getStaff_id());
            newAccount.setIs_active((byte) 1);

            accountDAO.create(newAccount);

            view.showInfo(
                    "✅ Tạo tài khoản thành công!\n\n" +
                            "Account ID: " + selectedStaff.getStaff_id() + "\n" +
                            "Username: " + username + "\n" +
                            "Nhân viên: " + selectedStaff.getStaff_name() + "\n" +
                            "Email: " + selectedStaff.getEmail()
            );

            if (parent != null)
                parent.reloadAfterChildChanged();

            view.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi lưu vào CSDL:\n" + ex.getMessage());
        }
    }
}
