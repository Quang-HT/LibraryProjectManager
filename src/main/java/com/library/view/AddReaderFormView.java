package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.library.service.ImagineApp.createBookIcon;

public class AddReaderFormView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    private JTextField txtFullName;
    private JTextField txtPhoneNumber;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtIDCard;
    private JTextField txtDateOfBirth; // dd/MM/yyyy
    private JComboBox<String> cmbGender;
    private JTextArea txtNotes;

    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnClear;
    @Getter
    private JButton btnCancel;

    private final String[] genders = {"Chọn giới tính", "Nam", "Nữ", "Khác"};

    public AddReaderFormView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Đăng ký Độc giả mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 700);
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

        JLabel titleLabel = new JLabel("Đăng ký Độc giả mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(280, 20, 400, 35);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Vui lòng điền đầy đủ thông tin bên dưới");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setBounds(310, 55, 300, 20);
        mainPanel.add(subtitleLabel);

        createPersonalInfoSection(mainPanel);
        createNotesSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createPersonalInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 100;
        int spacing = 50;
        int labelWidth = 160;
        int fieldWidth = 280;
        int height = 30;

        JLabel lblFullName = createLabel("Họ và tên: *", labelX, startY, labelWidth, height);
        lblFullName.setForeground(new Color(220, 53, 69));
        parent.add(lblFullName);

        txtFullName = createTextField(fieldX, startY, fieldWidth, height);
        txtFullName.setToolTipText("Nhập họ và tên đầy đủ");
        parent.add(txtFullName);

        JLabel lblPhoneNumber = createLabel("Số điện thoại: *", labelX, startY + spacing, labelWidth, height);
        lblPhoneNumber.setForeground(new Color(220, 53, 69));
        parent.add(lblPhoneNumber);

        txtPhoneNumber = createTextField(fieldX, startY + spacing, fieldWidth, height);
        txtPhoneNumber.setToolTipText("Nhập số điện thoại 10 số, bắt đầu bằng 0");
        parent.add(txtPhoneNumber);

        JLabel lblEmail = createLabel("Email:", labelX, startY + spacing * 2, labelWidth, height);
        parent.add(lblEmail);

        txtEmail = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        txtEmail.setToolTipText("example@gmail.com");
        parent.add(txtEmail);

        JLabel lblAddress = createLabel("Địa chỉ: *", labelX, startY + spacing * 3, labelWidth, height);
        lblAddress.setForeground(new Color(220, 53, 69));
        parent.add(lblAddress);

        txtAddress = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        txtAddress.setToolTipText("Nhập địa chỉ đầy đủ");
        parent.add(txtAddress);

        JLabel lblIDCard = createLabel("CMND/CCCD:", labelX, startY + spacing * 4, labelWidth, height);
        parent.add(lblIDCard);

        txtIDCard = createTextField(fieldX, startY + spacing * 4, fieldWidth, height);
        txtIDCard.setToolTipText("Số chứng minh nhân dân hoặc căn cước");
        parent.add(txtIDCard);

        JLabel lblDateOfBirth = createLabel("Ngày sinh:", labelX, startY + spacing * 5, labelWidth, height);
        parent.add(lblDateOfBirth);

        txtDateOfBirth = createTextField(fieldX, startY + spacing * 5, fieldWidth, height);
        txtDateOfBirth.setToolTipText("dd/MM/yyyy - Ví dụ: 15/01/1995 (có thể để trống)");
        parent.add(txtDateOfBirth);

        JLabel lblGender = createLabel("Giới tính: *", labelX, startY + spacing * 6, labelWidth, height);
        lblGender.setForeground(new Color(220, 53, 69));
        parent.add(lblGender);

        cmbGender = createComboBox(genders, fieldX, startY + spacing * 6, fieldWidth, height);
        parent.add(cmbGender);
    }

    private void createNotesSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 450;
        int labelWidth = 160;
        int fieldWidth = 560;
        int fieldHeight = 100;

        JLabel lblNotes = createLabel("Ghi chú:", labelX, startY, labelWidth, 30);
        parent.add(lblNotes);

        txtNotes = new JTextArea();
        txtNotes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtNotes.setToolTipText("Ghi chú thêm về độc giả (tùy chọn)");

        JScrollPane scrollPane = new JScrollPane(txtNotes);
        scrollPane.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 130;
        int buttonHeight = 42;
        int buttonY = 580;

        btnSave = new JButton("Lưu");
        btnSave.setBounds(370, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSave);

        btnClear = new JButton("Xóa form");
        btnClear.setBounds(510, buttonY, buttonWidth, buttonHeight);
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClear.setBackground(new Color(255, 193, 7));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnClear);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(650, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }


    public String getFullName() { return txtFullName.getText().trim(); }
    public String getPhone() { return txtPhoneNumber.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getAddress() { return txtAddress.getText().trim(); }
    public String getCccd() { return txtIDCard.getText().trim(); }
    public String getDobText() { return txtDateOfBirth.getText().trim(); } // dd/MM/yyyy
    public int getGenderIndex() { return cmbGender.getSelectedIndex(); }
    public String getGenderText() { return String.valueOf(cmbGender.getSelectedItem()); }
    public String getNotes() { return txtNotes.getText().trim(); }

    public void clearFormNoConfirm() {
        txtFullName.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtIDCard.setText("");
        txtDateOfBirth.setText("");
        cmbGender.setSelectedIndex(0);
        txtNotes.setText("");
        txtFullName.requestFocus();
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
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
