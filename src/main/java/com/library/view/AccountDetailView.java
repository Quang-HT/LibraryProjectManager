package com.library.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VIEW: Thông tin tài khoản (UI only)
 * - Không gọi DAO
 * - Không xử lý nghiệp vụ
 */
public class AccountDetailView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color DISABLED_BG = new Color(245, 245, 245);

    private JTextField txtAccountId;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtStaffName;
    private JTextField txtStaffId;
    private JTextField txtPosition;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JCheckBox chkIsActive;

    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnCancel;

    public AccountDetailView() {
        initializeFrame();
        createComponents();
        setFieldsEditable(false);
        setEditMode(false);
    }

    private void initializeFrame() {
        setTitle("Thông tin tài khoản");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel(" Thông tin tài khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(200, 20, 300, 35);
        mainPanel.add(titleLabel);

        createAccountInfoSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createAccountInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 140;
        int fieldWidth = 320;
        int height = 30;

        // Account ID
        parent.add(createLabel("ID Tài khoản:", labelX, startY, labelWidth, height));
        txtAccountId = createTextField(fieldX, startY, fieldWidth, height);
        txtAccountId.setEditable(false);
        txtAccountId.setBackground(DISABLED_BG);
        parent.add(txtAccountId);

        // Username
        parent.add(createLabel("Username:", labelX, startY + spacing, labelWidth, height));
        txtUsername = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtUsername);

        // Password
        parent.add(createLabel("Password:", labelX, startY + spacing * 2, labelWidth, height));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(fieldX, startY + spacing * 2, fieldWidth, height);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        parent.add(txtPassword);

        // Staff Name
        parent.add(createLabel("Tên nhân viên:", labelX, startY + spacing * 3, labelWidth, height));
        txtStaffName = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        txtStaffName.setEditable(false);
        txtStaffName.setBackground(DISABLED_BG);
        parent.add(txtStaffName);

        // Staff ID
        parent.add(createLabel("ID Nhân viên:", labelX, startY + spacing * 4, labelWidth, height));
        txtStaffId = createTextField(fieldX, startY + spacing * 4, fieldWidth, height);
        txtStaffId.setEditable(false);
        txtStaffId.setBackground(DISABLED_BG);
        parent.add(txtStaffId);

        // Position
        parent.add(createLabel("Chức vụ:", labelX, startY + spacing * 5, labelWidth, height));
        txtPosition = createTextField(fieldX, startY + spacing * 5, fieldWidth, height);
        txtPosition.setEditable(false);
        txtPosition.setBackground(DISABLED_BG);
        parent.add(txtPosition);

        // Email
        parent.add(createLabel("Email:", labelX, startY + spacing * 6, labelWidth, height));
        txtEmail = createTextField(fieldX, startY + spacing * 6, fieldWidth, height);
        txtEmail.setEditable(false);
        txtEmail.setBackground(DISABLED_BG);
        parent.add(txtEmail);

        // Phone
        parent.add(createLabel("Số Điện Thoại:", labelX, startY + spacing * 7, labelWidth, height));
        txtPhoneNumber = createTextField(fieldX, startY + spacing * 7, fieldWidth, height);
        txtPhoneNumber.setEditable(false);
        txtPhoneNumber.setBackground(DISABLED_BG);
        parent.add(txtPhoneNumber);

        // Status
        parent.add(createLabel("Trạng thái:", labelX, startY + spacing * 8, labelWidth, height));
        chkIsActive = new JCheckBox("Tài khoản hoạt động");
        chkIsActive.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkIsActive.setBounds(fieldX, startY + spacing * 8, fieldWidth, height);
        chkIsActive.setBackground(BG_COLOR);
        parent.add(chkIsActive);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 550;

        btnEdit = new JButton(" Chỉnh sửa");
        btnEdit.setBounds(270, buttonY, buttonWidth, buttonHeight);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setBackground(new Color(255, 152, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnEdit);

        btnSave = new JButton(" Lưu");
        btnSave.setBounds(270, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(76, 175, 80));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setVisible(false);
        parent.add(btnSave);

        btnCancel = new JButton("Đóng");
        btnCancel.setBounds(400, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }

    // ================== VIEW API ==================

    public void setEditMode(boolean isEditMode) {
        btnEdit.setVisible(!isEditMode);
        btnSave.setVisible(isEditMode);
        btnCancel.setText(isEditMode ? " Hủy" : "Đóng");
        setFieldsEditable(isEditMode);
    }

    public void setFieldsEditable(boolean editable) {
        txtUsername.setEditable(editable);
        txtPassword.setEditable(editable);
        chkIsActive.setEnabled(editable);

        Color bgColor = editable ? Color.WHITE : DISABLED_BG;
        txtUsername.setBackground(bgColor);
        txtPassword.setBackground(bgColor);
    }

    public void fillData(
            int accountId,
            String username,
            String password,
            boolean isActive,
            Integer staffId,
            String staffName,
            String position,
            String email,
            String phone
    ) {
        txtAccountId.setText(String.valueOf(accountId));
        txtUsername.setText(username != null ? username : "");
        txtPassword.setText(password != null ? password : "");
        chkIsActive.setSelected(isActive);

        txtStaffId.setText(staffId != null ? String.valueOf(staffId) : "");
        txtStaffName.setText(staffName != null ? staffName : "N/A");
        txtPosition.setText(position != null ? position : "N/A");
        txtEmail.setText(email != null ? email : "N/A");
        txtPhoneNumber.setText(phone != null ? phone : "N/A");
    }

    // ================== Getters ==================

    public JTextField getTxtAccountId() { return txtAccountId; }
    public JTextField getTxtUsername() { return txtUsername; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JTextField getTxtStaffId() { return txtStaffId; }
    public JCheckBox getChkIsActive() { return chkIsActive; }

    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }

    // ================== UI helpers ==================

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
}
