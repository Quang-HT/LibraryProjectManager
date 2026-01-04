package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.library.service.ImagineApp.createBookIcon;

public class AddStaffFormView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    @Getter
    private JTextField txtStaffName;
    @Getter
    private JTextField txtEmail;
    @Getter
    private JTextField txtPhone;
    @Getter
    private JComboBox<String> cmbPosition;
    @Getter
    private JCheckBox chkIsWorking;
    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnCancel;

    private final String[] positions = {"Chọn chức vụ", "Manager", "Staff", "Librarian"};

    public AddStaffFormView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Thêm Nhân viên mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 520);
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

        JLabel titleLabel = new JLabel("Thêm Nhân viên mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(160, 20, 300, 35);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Vui lòng điền đầy đủ thông tin");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setBounds(190, 55, 250, 20);
        mainPanel.add(subtitleLabel);

        createFormFields(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createFormFields(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 100;
        int spacing = 60;
        int labelWidth = 160;
        int fieldWidth = 280;
        int height = 35;

        JLabel lblStaffName = createLabel("Tên nhân viên:", labelX, startY, labelWidth, height);
        addRequiredMark(parent, lblStaffName);
        parent.add(lblStaffName);

        txtStaffName = createTextField(fieldX, startY, fieldWidth, height);
        parent.add(txtStaffName);

        JLabel lblPosition = createLabel("Chức vụ:", labelX, startY + spacing, labelWidth, height);
        addRequiredMark(parent, lblPosition);
        parent.add(lblPosition);

        cmbPosition = createComboBox(positions, fieldX, startY + spacing, fieldWidth, height);
        parent.add(cmbPosition);

        JLabel lblEmail = createLabel("Email:", labelX, startY + spacing * 2, labelWidth, height);
        addRequiredMark(parent, lblEmail);
        parent.add(lblEmail);

        txtEmail = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        txtEmail.setToolTipText("Ví dụ: nguyenvana@example.com");
        parent.add(txtEmail);

        JLabel lblPhone = createLabel("Số điện thoại:", labelX, startY + spacing * 3, labelWidth, height);
        parent.add(lblPhone);

        txtPhone = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        txtPhone.setToolTipText("Số điện thoại 10 số");
        parent.add(txtPhone);

        JLabel lblIsWorking = createLabel("Trạng thái:", labelX, startY + spacing * 4, labelWidth, height);
        parent.add(lblIsWorking);

        chkIsWorking = new JCheckBox("Đang làm việc");
        chkIsWorking.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkIsWorking.setBounds(fieldX, startY + spacing * 4, fieldWidth, height);
        chkIsWorking.setBackground(BG_COLOR);
        chkIsWorking.setSelected(true);
        parent.add(chkIsWorking);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 130;
        int buttonHeight = 42;
        int buttonY = 410;

        btnSave = new JButton("Lưu");
        btnSave.setBounds(240, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSave);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(380, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
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
