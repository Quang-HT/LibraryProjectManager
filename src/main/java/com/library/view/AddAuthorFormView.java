package com.library.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddAuthorFormView extends JFrame {

    // Color Scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    // Form components
    private JTextField txtAuthorName;
    private JTextField txtCountry;
    private JTextField txtBirthYear;
    private JTextField txtDeathYear;
    private JTextArea txtBiography;

    // Buttons
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnReset;

    public AddAuthorFormView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Thêm Tác giả Mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("➕ Thêm Tác giả Mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(200, 20, 300, 35);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Vui lòng điền đầy đủ thông tin bên dưới");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setBounds(195, 55, 350, 20);
        mainPanel.add(subtitleLabel);

        createAuthorInfoSection(mainPanel);
        createBiographySection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createAuthorInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int startY = 100;
        int spacing = 60;
        int labelWidth = 140;
        int fieldWidth = 400;
        int height = 35;

        JLabel lblAuthorName = createLabel("Tên tác giả: *", labelX, startY, labelWidth, height);
        parent.add(lblAuthorName);

        txtAuthorName = createTextField(fieldX, startY, fieldWidth, height);
        txtAuthorName.setToolTipText("Nhập tên đầy đủ của tác giả");
        parent.add(txtAuthorName);

        JLabel lblCountry = createLabel("Quốc gia: *", labelX, startY + spacing, labelWidth, height);
        parent.add(lblCountry);

        txtCountry = createTextField(fieldX, startY + spacing, fieldWidth, height);
        txtCountry.setToolTipText("Quốc gia của tác giả");
        parent.add(txtCountry);

        JLabel lblBirthYear = createLabel("Năm sinh: *", labelX, startY + spacing * 2, labelWidth, height);
        parent.add(lblBirthYear);

        txtBirthYear = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        txtBirthYear.setToolTipText("Nhập năm sinh (VD: 1970)");
        parent.add(txtBirthYear);

        JLabel lblDeathYear = createLabel("Năm mất:", labelX, startY + spacing * 3, labelWidth, height);
        parent.add(lblDeathYear);

        txtDeathYear = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        txtDeathYear.setToolTipText("Để trống nếu còn sống");
        parent.add(txtDeathYear);
    }

    private void createBiographySection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int startY = 340;
        int labelWidth = 140;
        int fieldWidth = 400;
        int fieldHeight = 120;

        JLabel lblBiography = createLabel("Tiểu sử:", labelX, startY, labelWidth, 30);
        parent.add(lblBiography);

        txtBiography = new JTextArea();
        txtBiography.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBiography.setLineWrap(true);
        txtBiography.setWrapStyleWord(true);
        txtBiography.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtBiography.setToolTipText("Mô tả về tác giả, sự nghiệp, tác phẩm nổi bật...");

        JScrollPane scrollPane = new JScrollPane(txtBiography);
        scrollPane.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblNote = new JLabel("* Trường bắt buộc");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNote.setForeground(Color.RED);
        lblNote.setBounds(200, startY + fieldHeight + 10, 200, 20);
        parent.add(lblNote);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 500;
        int centerX = 320;

        btnReset = new JButton("Làm mới");
        btnReset.setBounds(centerX - 250, buttonY, buttonWidth, buttonHeight);
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.setBackground(new Color(158, 158, 158));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnReset);

        btnSave = new JButton("Lưu");
        btnSave.setBounds(centerX - 60, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSave);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(centerX + 70, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }

    /* ===================== VIEW API ===================== */

    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
    public JButton getBtnReset() { return btnReset; }

    public String getAuthorName() { return txtAuthorName.getText().trim(); }
    public String getCountry() { return txtCountry.getText().trim(); }
    public String getBirthYearText() { return txtBirthYear.getText().trim(); }
    public String getDeathYearText() { return txtDeathYear.getText().trim(); }
    public String getBiography() { return txtBiography.getText().trim(); }

    public void focusAuthorName() { txtAuthorName.requestFocus(); }
    public void focusBirthYear() { txtBirthYear.requestFocus(); }
    public void focusDeathYear() { txtDeathYear.requestFocus(); }
    public void focusCountry() { txtCountry.requestFocus(); }

    public void resetForm() {
        txtAuthorName.setText("");
        txtCountry.setText("");
        txtBirthYear.setText("");
        txtDeathYear.setText("");
        txtBiography.setText("");
        focusAuthorName();
    }

    public boolean hasAnyData() {
        return !getAuthorName().isEmpty()
                || !getCountry().isEmpty()
                || !getBirthYearText().isEmpty()
                || !getDeathYearText().isEmpty()
                || !getBiography().isEmpty();
    }

    public boolean confirm(String msg, String title) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return confirm == JOptionPane.YES_OPTION;
    }

    public boolean confirmWarning(String msg, String title) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return confirm == JOptionPane.YES_OPTION;
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
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
}
