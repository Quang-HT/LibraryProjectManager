package com.library.controller;

import com.library.dao.impl.Staff_DAO;
import com.library.model.Staff_Entity;
import com.library.view.StaffDetailView;
import com.library.view.StaffManagementView;

import javax.swing.*;

public class StaffDetailController {

    private final StaffDetailView view;
    private final Staff_DAO staffDAO;

    private final int staffId;
    private final StaffManagementView parent;

    /**
     * @param staffId
     * @param parent
     * Hàm Controller của Staff Detail truyền vào id Staff - StaffManagementView Chính
     */
    public StaffDetailController(int staffId, StaffManagementView parent) {
        this.staffId = staffId;
        this.parent = parent;

        this.view = new StaffDetailView();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadStaffData();
        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Đăng ký sự kiện
     * view.dispose()
     */
    private void bindEvents() {
        view.getBtnClose().addActionListener(e -> view.dispose());
    }

    /**
     * Lấy dữ liệu nhân viên từ Database thông qua id Staff đã được truyền vào
     */
    private void loadStaffData() {
        try {
            Staff_Entity staff = staffDAO.findById(staffId).orElse(null);

            if (staff == null) {
                JOptionPane.showMessageDialog(parent,
                        "Không tìm thấy nhân viên với ID = " + staffId,
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                view.dispose();
                return;
            }

            view.setStaffIdText(String.valueOf(staff.getStaff_id()));
            view.setStaffNameText(staff.getStaff_name());
            view.setPositionText(staff.getPosition() != null ? staff.getPosition() : "N/A");
            view.setEmailText(staff.getEmail() != null ? staff.getEmail() : "N/A");
            view.setPhoneText(staff.getPhone() != null ? staff.getPhone() : "N/A");
            view.setStatusText(staff.getIs_working() == 1 ? "Đang làm việc" : "Nghỉ việc");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Lỗi khi tải dữ liệu nhân viên: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            view.dispose();
        }
    }
}
