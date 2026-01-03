package com.library.controller;

import com.library.dao.impl.Staff_DAO;
import com.library.model.Staff_Entity;
import com.library.view.AddStaffFormView;


import javax.swing.*;

public class AddStaffController {

    private final AddStaffFormView view;
    private final Staff_DAO dao;
    private final StaffManagementController parentController;

    /**
     * Hàm Controller của Thêm nhân viên mới
     * THực thi thứ tự danh sách các công việc cần thực thi
     */
    public AddStaffController(StaffManagementController parentController) {
        this.parentController = parentController;
        this.view = new AddStaffFormView();
        this.dao = new Staff_DAO();

        bindEvents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Hàm Controller của Thêm nhân viên mới
     * Hàm này dùng riêng cho Button ở Home View mở chỉ View Add Staff
     * Thực thi thứ tự danh sách các công việc cần thực thi
     */
    public AddStaffController() {
        this.parentController = null;
        this.view = new AddStaffFormView();
        this.dao = new Staff_DAO();

        bindEvents();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Đăng ký các sự kiện ActionListener với View
     */
    private void bindEvents() {
        view.getBtnSave().addActionListener(e -> saveStaff());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    /**
     * Hàm nhân thông tin từ người dùng và lưu nhân viên mới
     */
    private void saveStaff() {
        String name = view.getTxtStaffName().getText().trim();
        if (name.isEmpty()) {
            view.showError("Vui lòng nhập tên nhân viên!");
            view.getTxtStaffName().requestFocus();
            return;
        }

        if (view.getCmbPosition().getSelectedIndex() == 0) {
            view.showError("Vui lòng chọn chức vụ!");
            view.getCmbPosition().requestFocus();
            return;
        }

        String email = view.getTxtEmail().getText().trim();
        if (email.isEmpty()) {
            view.showError("Vui lòng nhập email!");
            view.getTxtEmail().requestFocus();
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            view.showError("Email không hợp lệ!");
            view.getTxtEmail().requestFocus();
            return;
        }

        String phone = view.getTxtPhone().getText().trim();
        if (!phone.isEmpty() && !phone.matches("^\\d{10}$")) {
            view.showError("Số điện thoại phải có 10 chữ số!");
            view.getTxtPhone().requestFocus();
            return;
        }

        try {
            // Check email exists
            if (dao.findEmail(email).isPresent()) {
                view.showError("Email đã tồn tại! Vui lòng sử dụng email khác.");
                view.getTxtEmail().requestFocus();
                return;
            }

            Staff_Entity newStaff = new Staff_Entity();
            newStaff.setStaff_name(name);
            newStaff.setPosition((String) view.getCmbPosition().getSelectedItem());
            newStaff.setEmail(email);
            newStaff.setPhone(phone.isEmpty() ? null : phone);
            newStaff.setIs_working(view.getChkIsWorking().isSelected() ? (byte) 1 : (byte) 0);

            dao.create(newStaff);

            view.showInfo(
                    "✅ Thêm nhân viên thành công!\n\n" +
                            "Tên: " + newStaff.getStaff_name() + "\n" +
                            "Chức vụ: " + newStaff.getPosition() + "\n" +
                            "Email: " + newStaff.getEmail()
            );

            if (parentController != null) {
                parentController.reloadAfterChildChanged();
            }

            view.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi lưu vào CSDL:\n" + ex.getMessage());
        }
    }
}
