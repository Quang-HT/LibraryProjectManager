package com.library.controller;

import com.library.dao.impl.Staff_DAO;
import com.library.model.Staff_Entity;
import com.library.view.*;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class StaffManagementController {

    private final StaffManagementView view;
    private final Staff_DAO staffDAO;

    private List<Staff_Entity> staffList = new ArrayList<>();
    private Staff_Entity selectedStaff = null;

    /**
     * Hàm Controller của Quản lý nhân viên
     * Thực thi thứ tự các danh sách hàm cần xử lý
     */
    public StaffManagementController() {
        this.view = new StaffManagementView();
        this.staffDAO = new Staff_DAO();

        bindEvents();
        loadDataFromDatabase();
        showView();
    }

    /**
     * Hiển thị View
     */
    public void showView() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            view.setVisible(true);
        });
    }

    /**
     * Đăng ký các sự kiện với bên View
     */
    private void bindEvents() {
        view.getTxtSearch().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                applySearchFilter();
            }
        }); // Search song song với nhập

        view.getBtnSearch().addActionListener(e -> applySearchFilter()); // Search khi người dúng bấm nút Button

        view.getTblStaff().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onStaffSelected();
        }); // Lựa chọn nhân viên

        view.getTblStaff().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openStaffDetails();
            }
        });


        view.getBtnAddStaff().addActionListener(e -> openAddStaffForm());
        view.getBtnEdit().addActionListener(e -> openEditStaffForm());
        view.getBtnDelete().addActionListener(e -> deleteStaff());
        view.getBtnRefresh().addActionListener(e -> refreshData(true));

        view.getBtnClose().addActionListener(e -> {
            new HomeView().setVisible(true);
            view.dispose();
        });
    }

    /**
     * Đưa bảng dữ liệu nhân viên lên bảng
     */
    private void loadStaffToTable(List<Staff_Entity> staffs) {
        this.staffList = staffs;

        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);

        for (Staff_Entity staff : staffs) {
            String status = staff.getIs_working() == 1 ? "Đang làm" : "Nghỉ việc";

            Object[] row = {
                    staff.getStaff_id(),
                    staff.getStaff_name(),
                    staff.getPosition() != null ? staff.getPosition() : "N/A",
                    staff.getEmail() != null ? staff.getEmail() : "N/A",
                    staff.getPhone() != null ? staff.getPhone() : "N/A",
                    status
            };
            model.addRow(row);
        }
    }

    /**
     * Hàm search theo người dùng nhập
     */
    private void applySearchFilter() {
        String searchText = view.getTxtSearch().getText().trim();

        TableRowSorter<DefaultTableModel> sorter = view.getSorter();// TableRowSorter: công cụ sắp xếp và lọc dữ liệu qua câu lệch .setRowFilter bên dưới
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    /**
     * Lấy đối tường nhân viên (Staff) được người dùng chọn
     */
    private void onStaffSelected() {
        int selectedRow = view.getTblStaff().getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = view.getTblStaff().convertRowIndexToModel(selectedRow); // Lấy STT của bảng
            int staffId = (int) view.getTableModel().getValueAt(modelRow, 0); // Lấy Id nhân viên qua bảng

            selectedStaff = staffList.stream()
                    .filter(s -> s.getStaff_id() == staffId)
                    .findFirst()
                    .orElse(null);

            view.getBtnEdit().setEnabled(selectedStaff != null);
            view.getBtnDelete().setEnabled(selectedStaff != null);
        } else {
            selectedStaff = null;
            view.getBtnEdit().setEnabled(false);
            view.getBtnDelete().setEnabled(false);
        }
    }

    /**
     * Mở bảng xem chi tiết nhân viên đã được chọn
     */
    private void openStaffDetails() {
        if (selectedStaff != null) {
            new com.library.controller.StaffDetailController(selectedStaff.getStaff_id(), view);

        } else {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn một nhân viên!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Mở View tạo mới nhân viên
     */
    private void openAddStaffForm() {
        new AddStaffController(this);
    }

    /**
     * Mở quyền chỉnh sửa thông tin nhân viên
     */
    private void openEditStaffForm() {
        if (selectedStaff != null) {
            EditStaffForm editForm = new EditStaffForm(selectedStaff, view);
            editForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn nhân viên cần chỉnh sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Để form con gọi về khi add/edit xong
     */
    public void reloadAfterChildChanged() {
        refreshData(false);
    }

    /**
     * Xóa nhân viên
     */
    private void deleteStaff() {
        if (selectedStaff == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn nhân viên cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "⚠️ Bạn có chắc chắn muốn xóa nhân viên?\n\n" +
                        "ID: " + selectedStaff.getStaff_id() + "\n" +
                        "Tên: " + selectedStaff.getStaff_name() + "\n" +
                        "Email: " + selectedStaff.getEmail() + "\n\n" +
                        "Hành động này không thể hoàn tác!",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                staffDAO.delete(selectedStaff.getStaff_id());

                staffList.remove(selectedStaff);
                loadStaffToTable(staffList);

                selectedStaff = null;
                view.getBtnEdit().setEnabled(false);
                view.getBtnDelete().setEnabled(false);

                JOptionPane.showMessageDialog(view,
                        "✅ Đã xóa nhân viên thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view,
                        "❌ Không thể xóa nhân viên!\n" + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData(boolean showMessage) {
        try {
            List<Staff_Entity> staffs = staffDAO.findAll();

            if (staffs != null) {
                loadStaffToTable(staffs);
                view.getTxtSearch().setText("");
                view.getSorter().setRowFilter(null);

                if (showMessage) {
                    JOptionPane.showMessageDialog(view,
                            "✅ Đã làm mới danh sách thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi làm mới dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tìm nhân viên từ Database
     */
    private void loadDataFromDatabase() {
        try {
            List<Staff_Entity> staffs = staffDAO.findAll();
            if (staffs != null && !staffs.isEmpty()) {
                loadStaffToTable(staffs);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Không có dữ liệu nhân viên trong hệ thống!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải dữ liệu từ database: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
