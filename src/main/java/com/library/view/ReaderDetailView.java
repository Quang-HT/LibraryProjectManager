package com.library.view;

import com.library.model.BorrowSlip_Entity;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;

public class ReaderDetailView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color DISABLED_BG = new Color(245, 245, 245);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);

    private JTextField txtReaderId;
    private JTextField txtFullName;
    private JTextField txtPhoneNumber;
    private JTextField txtEmail;
    private JTextField txtAddress;
    private JTextField txtRegistrationDate;
    private JTextField txtCccd;
    private JComboBox<String> cmbStatus;
    private JTextArea txtNotes;

    @Getter
    private JTable tblBorrowSlips;
    private DefaultTableModel tableModel;

    @Getter
    private JButton btnEdit;
    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnCancel;

    private List<BorrowSlip_Entity> displayedSlips = new ArrayList<>();

    private final String[] statuses = {"Hoạt động", "Tạm khóa", "Vi phạm"};

    public ReaderDetailView() {
        initializeFrame();
        createComponents();
        setFieldsEditable(false);
    }

    private void initializeFrame() {
        setTitle("Chi tiết Độc giả");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
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

        JLabel titleLabel = new JLabel("Chi tiết Độc giả");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(370, 20, 300, 35);
        mainPanel.add(titleLabel);

        createReaderInfoSection(mainPanel);
        createNotesSection(mainPanel);
        createBorrowSlipsSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createReaderInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int labelX2 = 500;
        int fieldX2 = 670;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 160;
        int fieldWidth = 240;
        int height = 30;

        JLabel lblReaderId = createLabel("ID Độc giả:", labelX, startY, labelWidth, height);
        parent.add(lblReaderId);

        txtReaderId = createTextField(fieldX, startY, fieldWidth, height);
        txtReaderId.setEditable(false);
        txtReaderId.setBackground(DISABLED_BG);
        parent.add(txtReaderId);

        JLabel lblFullName = createLabel("Họ và tên:", labelX, startY + spacing, labelWidth, height);
        parent.add(lblFullName);

        txtFullName = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtFullName);

        JLabel lblPhoneNumber = createLabel("Số điện thoại:", labelX, startY + spacing * 2, labelWidth, height);
        parent.add(lblPhoneNumber);

        txtPhoneNumber = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(txtPhoneNumber);

        JLabel lblEmail = createLabel("Email:", labelX, startY + spacing * 3, labelWidth, height);
        parent.add(lblEmail);

        txtEmail = createTextField(fieldX, startY + spacing * 3, fieldWidth, height);
        parent.add(txtEmail);

        JLabel lblAddress = createLabel("Địa chỉ:", labelX2, startY, labelWidth, height);
        parent.add(lblAddress);

        txtAddress = createTextField(fieldX2, startY, fieldWidth, height);
        parent.add(txtAddress);

        JLabel lblRegistrationDate = createLabel("Ngày đăng ký:", labelX2, startY + spacing, labelWidth, height);
        parent.add(lblRegistrationDate);

        txtRegistrationDate = createTextField(fieldX2, startY + spacing, fieldWidth, height);
        txtRegistrationDate.setEditable(false);
        txtRegistrationDate.setBackground(DISABLED_BG);
        parent.add(txtRegistrationDate);

        JLabel lblStatus = createLabel("Trạng thái:", labelX2, startY + spacing * 2, labelWidth, height);
        parent.add(lblStatus);

        cmbStatus = createComboBox(statuses, fieldX2, startY + spacing * 2, fieldWidth, height);
        parent.add(cmbStatus);

        JLabel lblCccd = createLabel("CCCD:", labelX2, startY + spacing * 3, labelWidth, height);
        parent.add(lblCccd);

        txtCccd = createTextField(fieldX2, startY + spacing * 3, fieldWidth, height);
        parent.add(txtCccd);
    }

    private void createNotesSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 280;
        int labelWidth = 160;
        int fieldWidth = 690;
        int fieldHeight = 80;

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

        JScrollPane scrollPane = new JScrollPane(txtNotes);
        scrollPane.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);
    }

    private void createBorrowSlipsSection(JPanel parent) {
        int sectionY = 390;
        int tableHeight = 250;

        JLabel lblSlipsTitle = new JLabel(" Danh sách phiếu mượn");
        lblSlipsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSlipsTitle.setForeground(TEXT_COLOR);
        lblSlipsTitle.setBounds(50, sectionY, 400, 30);
        parent.add(lblSlipsTitle);

        String[] columnNames = {"ID Phiếu", "Ngày mượn", "Hạn trả", "Ngày trả", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblBorrowSlips = new JTable(tableModel);
        tblBorrowSlips.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblBorrowSlips.setRowHeight(32);
        tblBorrowSlips.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblBorrowSlips.getTableHeader().setBackground(new Color(240, 240, 240));
        tblBorrowSlips.getTableHeader().setForeground(TEXT_COLOR);
        tblBorrowSlips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBorrowSlips.setSelectionBackground(new Color(200, 230, 255));
        tblBorrowSlips.setGridColor(BORDER_COLOR);

        JScrollPane scrollPane = new JScrollPane(tblBorrowSlips);
        scrollPane.setBounds(50, sectionY + 40, 860, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel(" Nhấp đúp vào phiếu mượn để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(50, sectionY + tableHeight + 45, 400, 25);
        parent.add(lblInfo);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 700;

        btnEdit = new JButton("Chỉnh sửa");
        btnEdit.setBounds(630, buttonY, buttonWidth, buttonHeight);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setBackground(new Color(255, 152, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnEdit);

        btnSave = new JButton("Lưu");
        btnSave.setBounds(660, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(PRIMARY_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setVisible(false);
        parent.add(btnSave);

        btnCancel = new JButton("Đóng");
        btnCancel.setBounds(790, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }


    public String getReaderIdText() { return txtReaderId.getText().trim(); }
    public String getFullNameText() { return txtFullName.getText().trim(); }
    public String getPhoneText() { return txtPhoneNumber.getText().trim(); }
    public String getEmailText() { return txtEmail.getText().trim(); }
    public String getAddressText() { return txtAddress.getText().trim(); }
    public String getRegistrationDateText() { return txtRegistrationDate.getText().trim(); }
    public String getCccdText() { return txtCccd.getText().trim(); }
    public String getNotesText() { return txtNotes.getText().trim(); }

    public void setReaderId(String v) { txtReaderId.setText(v); }
    public void setFullName(String v) { txtFullName.setText(v); }
    public void setPhone(String v) { txtPhoneNumber.setText(v); }
    public void setEmail(String v) { txtEmail.setText(v); }
    public void setAddress(String v) { txtAddress.setText(v); }
    public void setRegistrationDate(String v) { txtRegistrationDate.setText(v); }
    public void setCccd(String v) { txtCccd.setText(v); }
    public void setStatusIndex(int idx) { cmbStatus.setSelectedIndex(Math.max(0, idx)); }
    public void setNotes(String v) { txtNotes.setText(v); }

    public void setEditMode(boolean editing) {
        btnEdit.setVisible(!editing);
        btnSave.setVisible(editing);
        btnCancel.setText(editing ? "Hủy" : "Đóng");
        setFieldsEditable(editing);
    }

    public void setFieldsEditable(boolean editable) {
        txtFullName.setEditable(editable);
        txtPhoneNumber.setEditable(editable);
        txtEmail.setEditable(editable);
        txtAddress.setEditable(editable);
        txtCccd.setEditable(editable);
        cmbStatus.setEnabled(editable);
        txtNotes.setEditable(editable);

        Color bgColor = editable ? Color.WHITE : DISABLED_BG;
        txtFullName.setBackground(bgColor);
        txtPhoneNumber.setBackground(bgColor);
        txtEmail.setBackground(bgColor);
        txtAddress.setBackground(bgColor);
        txtCccd.setBackground(bgColor);
        txtNotes.setBackground(bgColor);
    }
    public void setBorrowSlips(List<BorrowSlip_Entity> slips, List<Object[]> rows) {
        displayedSlips = (slips != null) ? slips : new ArrayList<>();
        tableModel.setRowCount(0);

        if (rows != null) {
            for (Object[] r : rows) tableModel.addRow(r);
        }
    }

    public BorrowSlip_Entity getSelectedSlipOrNull() {
        int row = tblBorrowSlips.getSelectedRow();
        if (row < 0 || row >= displayedSlips.size()) return null;
        return displayedSlips.get(row);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.WARNING_MESSAGE);
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
    private JComboBox<String> createComboBox(String[] items, int x, int y, int width, int height) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBounds(x, y, width, height);
        comboBox.setBackground(Color.WHITE);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return comboBox;
    }
}
