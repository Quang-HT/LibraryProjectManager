package com.library.view;

import com.library.dao.impl.Staff_DAO;
import com.library.model.Staff_Entity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditStaffForm extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    private JTextField txtStaffId;
    private JTextField txtStaffName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> cmbPosition;
    private JCheckBox chkIsWorking;
    private JButton btnSave;
    private JButton btnCancel;

    private final String[] positions = {"Quản lý", "Nhân viên", "Thủ thư"};

    private final Staff_Entity staff;
    private final StaffManagementView parent;

    public EditStaffForm(Staff_Entity staff, StaffManagementView parent) {
        this.staff = staff;
        this.parent = parent;
        initializeFrame();
        createComponents();
        loadStaffData();
    }

    private void initializeFrame() {
        setTitle("Chỉnh sửa Nhân viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Chỉnh sửa Nhân viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(160, 20, 300, 35);
        mainPanel.add(titleLabel);

        createFormFields(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createFormFields(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 80;
        int spacing = 60;
        int labelWidth = 160;
        int fieldWidth = 280;
        int height = 35;

        // Staff ID
        JLabel lblStaffId = createLabel("ID Nhân viên:", labelX, startY, labelWidth, height);
        parent.add(lblStaffId);

        txtStaffId = createTextField(fieldX, startY, fieldWidth, height);
        txtStaffId.setEditable(false);
        txtStaffId.setBackground(new Color(245, 245, 245));
        parent.add(txtStaffId);

        // Staff Name
        JLabel lblStaffName = createLabel("Tên nhân viên:", labelX, startY + spacing, labelWidth, height);
        addRequiredMark(parent, lblStaffName);
        parent.add(lblStaffName);

        txtStaffName = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtStaffName);

        // Position
        JLabel lblPosition = createLabel("Chức vụ:", labelX, startY + spacing * 2, labelWidth, height);
        addRequiredMark(parent, lblPosition);
        parent.add(lblPosition);

        cmbPosition = createComboBox(positions, fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(cmbPosition);

        // Email
        JLabel lblEmail = createLabel("Email:", labelX, startY + spacing * 3, labelWidth, height);
        addRequiredMark(parent, lblEmail);
        parent.add(lblEmail);

        txtEmail = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        parent.add(txtEmail);

        // Phone
        JLabel lblPhone = createLabel("Số điện thoại:", labelX, startY + spacing * 4, labelWidth, height);
        parent.add(lblPhone);

        txtPhone = createTextField(fieldX, startY + spacing * 4, fieldWidth, height);
        parent.add(txtPhone);

        // Is Working
        JLabel lblIsWorking = createLabel("Trạng thái:", labelX, startY + spacing * 5, labelWidth, height);
        parent.add(lblIsWorking);

        chkIsWorking = new JCheckBox("Đang làm việc");
        chkIsWorking.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkIsWorking.setBounds(fieldX, startY + spacing * 5, fieldWidth, height);
        chkIsWorking.setBackground(BG_COLOR);
        parent.add(chkIsWorking);
    }

    private void loadStaffData() {
        txtStaffId.setText(String.valueOf(staff.getStaff_id()));
        txtStaffName.setText(staff.getStaff_name());
        txtEmail.setText(staff.getEmail() != null ? staff.getEmail() : "");
        txtPhone.setText(staff.getPhone() != null ? staff.getPhone() : "");
        chkIsWorking.setSelected(staff.getIs_working() == 1);

        // Set position
        String position = staff.getPosition();
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].equals(position)) {
                cmbPosition.setSelectedIndex(i);
                break;
            }
        }
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 130;
        int buttonHeight = 42;
        int buttonY = 440;

        btnSave = new JButton("Lưu");
        btnSave.setBounds(230, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> saveStaff());
        parent.add(btnSave);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(370, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        parent.add(btnCancel);
    }

    private void saveStaff() {
        // Validation
        if (txtStaffName.getText().trim().isEmpty()) {
            showError("Vui lòng nhập tên nhân viên!");
            txtStaffName.requestFocus();
            return;
        }

        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            showError("Vui lòng nhập email!");
            txtEmail.requestFocus();
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showError("Email không hợp lệ!");
            txtEmail.requestFocus();
            return;
        }

        String phone = txtPhone.getText().trim();
        if (!phone.isEmpty() && !phone.matches("^\\d{10}$")) {
            showError("Số điện thoại phải có 10 chữ số!");
            txtPhone.requestFocus();
            return;
        }

        try {
            Staff_DAO dao = new Staff_DAO();

            // Check if email exists (for other staff)
            Staff_Entity existingStaff = dao.findEmail(email).orElse(null);
            if (existingStaff != null && existingStaff.getStaff_id() != staff.getStaff_id()) {
                showError("Email đã được sử dụng bởi nhân viên khác!");
                txtEmail.requestFocus();
                return;
            }

            staff.setStaff_name(txtStaffName.getText().trim());
            staff.setPosition((String) cmbPosition.getSelectedItem());
            staff.setEmail(email);
            staff.setPhone(phone.isEmpty() ? null : phone);
            staff.setIs_working(chkIsWorking.isSelected() ? (byte) 1 : (byte) 0);

            dao.update(staff);

            JOptionPane.showMessageDialog(this,
                    "✅ Cập nhật nhân viên thành công!\n\n" +
                            "ID: " + staff.getStaff_id() + "\n" +
                            "Tên: " + staff.getStaff_name() + "\n" +
                            "Email: " + staff.getEmail(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            if (parent != null) {

            }
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Lỗi khi cập nhật CSDL:\n" + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void addRequiredMark(JPanel parent, JLabel label) {
        JLabel required = new JLabel("*");
        required.setFont(new Font("Segoe UI", Font.BOLD, 16));
        required.setForeground(Color.RED);
        required.setBounds(label.getX() + label.getWidth() - 15, label.getY(), 15, label.getHeight());
        parent.add(required);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        label.setBounds(x, y, width, height);
        return label;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBounds(x, y, width, height);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JComboBox<String> createComboBox(String[] items, int x, int y, int width, int height) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBounds(x, y, width, height);
        comboBox.setBackground(Color.WHITE);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return comboBox;
    }
}
