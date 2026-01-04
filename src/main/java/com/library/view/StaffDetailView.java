package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.library.service.ImagineApp.createBookIcon;

public class StaffDetailView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color DISABLED_BG = new Color(245, 245, 245);

    private JTextField txtStaffId;
    private JTextField txtStaffName;
    private JTextField txtPosition;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtStatus;

    @Getter
    private JButton btnClose;

    public StaffDetailView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Thông tin Nhân viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
        try {
            setIconImage(createBookIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Thông tin Nhân viên");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(170, 20, 300, 35);
        mainPanel.add(titleLabel);

        createInfoSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 140;
        int fieldWidth = 280;
        int height = 30;

        parent.add(createLabel("ID Nhân viên:", labelX, startY, labelWidth, height));
        txtStaffId = createTextField(fieldX, startY, fieldWidth, height);
        makeReadOnly(txtStaffId);
        parent.add(txtStaffId);

        parent.add(createLabel("Tên nhân viên:", labelX, startY + spacing, labelWidth, height));
        txtStaffName = createTextField(fieldX, startY + spacing, fieldWidth, height);
        makeReadOnly(txtStaffName);
        parent.add(txtStaffName);

        parent.add(createLabel("Chức vụ:", labelX, startY + spacing * 2, labelWidth, height));
        txtPosition = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        makeReadOnly(txtPosition);
        parent.add(txtPosition);

        parent.add(createLabel("Email:", labelX, startY + spacing * 3, labelWidth, height));
        txtEmail = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        makeReadOnly(txtEmail);
        parent.add(txtEmail);

        parent.add(createLabel("Số điện thoại:", labelX, startY + spacing * 4, labelWidth, height));
        txtPhone = createTextField(fieldX, startY + spacing * 4, fieldWidth, height);
        makeReadOnly(txtPhone);
        parent.add(txtPhone);

        parent.add(createLabel("Trạng thái:", labelX, startY + spacing * 5, labelWidth, height));
        txtStatus = createTextField(fieldX, startY + spacing * 5, fieldWidth, height);
        makeReadOnly(txtStatus);
        parent.add(txtStatus);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 390;

        btnClose = new JButton("Đóng");
        btnClose.setBounds(230, buttonY, buttonWidth, buttonHeight);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(200, 200, 200));
        btnClose.setForeground(TEXT_COLOR);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnClose);
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

    private void makeReadOnly(JTextField tf) {
        tf.setEditable(false);
        tf.setBackground(DISABLED_BG);
    }

    public void setStaffIdText(String v) { txtStaffId.setText(v); }
    public void setStaffNameText(String v) { txtStaffName.setText(v); }
    public void setPositionText(String v) { txtPosition.setText(v); }
    public void setEmailText(String v) { txtEmail.setText(v); }
    public void setPhoneText(String v) { txtPhone.setText(v); }
    public void setStatusText(String v) { txtStatus.setText(v); }
}
