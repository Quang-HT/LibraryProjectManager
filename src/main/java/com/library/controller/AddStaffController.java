package com.library.controller;

import com.library.dao.impl.Staff_DAO;
import com.library.model.Staff_Entity;
import com.library.view.AddStaffFormView;
import com.library.view.StaffManagementView;

import javax.swing.*;

public class AddStaffController {

    private final AddStaffFormView view;
    private final Staff_DAO dao;
    private final StaffManagementController parentController; // chuẩn nhất để gọi reload

    // Nếu bạn chưa có parentController thì có thể truyền StaffManagementController thay vì View
    public AddStaffController(StaffManagementController parentController) {
        this.parentController = parentController;
        this.view = new AddStaffFormView();
        this.dao = new Staff_DAO();

        bindEvents();
    }

    // Nếu bạn vẫn muốn truyền parentView (ít chuẩn hơn) thì dùng constructor này:
    public AddStaffController(StaffManagementView parentView) {
        this.parentController = null;
        this.view = new AddStaffFormView();
        this.dao = new Staff_DAO();

        bindEvents();
    }

    public void showView() {
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    private void bindEvents() {
        view.getBtnSave().addActionListener(e -> saveStaff());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    private void saveStaff() {
        // Validation
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

            // Reload list
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
