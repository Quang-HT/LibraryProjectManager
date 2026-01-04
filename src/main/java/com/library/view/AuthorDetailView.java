package com.library.view;

import com.library.model.Author_Entity;
import com.library.model.Book_Entity;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;

public class AuthorDetailView extends JFrame {

    // Color Scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color DISABLED_BG = new Color(245, 245, 245);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);

    // Form components
    private JTextField txtAuthorId;
    private JTextField txtAuthorName;
    private JTextField txtCountry;
    private JTextField txtBirthYear;
    private JTextField txtDeathYear;
    private JTextArea txtBiography;

    // Table
    // Table
    @Getter
    private JTable tblBooks;
    private DefaultTableModel tableModel;

    // Buttons
    // Buttons
    @Getter
    private JButton btnEdit;
    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnCancel;

    private List<Integer> displayedBookIds = new ArrayList<>();

    public AuthorDetailView() {
        initializeFrame();
        createComponents();
        setFieldsEditable(false);
        setEditMode(false);
    }

    private void initializeFrame() {
        setTitle("Chi tiết Tác giả");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
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

        JLabel titleLabel = new JLabel("Chi tiết Tác giả");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(370, 20, 300, 35);
        mainPanel.add(titleLabel);

        createAuthorInfoSection(mainPanel);
        createBiographySection(mainPanel);
        createBooksTableSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createAuthorInfoSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int labelX2 = 500;
        int fieldX2 = 650;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 140;
        int fieldWidth = 240;
        int height = 30;

        parent.add(createLabel("ID Tác giả:", labelX, startY, labelWidth, height));
        txtAuthorId = createTextField(fieldX, startY, fieldWidth, height);
        txtAuthorId.setEditable(false);
        txtAuthorId.setBackground(DISABLED_BG);
        parent.add(txtAuthorId);

        parent.add(createLabel("Tên tác giả:", labelX, startY + spacing, labelWidth, height));
        txtAuthorName = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtAuthorName);

        parent.add(createLabel("Quốc gia:", labelX, startY + spacing * 2, labelWidth, height));
        txtCountry = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(txtCountry);

        parent.add(createLabel("Năm sinh:", labelX2, startY, labelWidth, height));
        txtBirthYear = createTextField(fieldX2, startY, fieldWidth, height);
        parent.add(txtBirthYear);

        parent.add(createLabel("Năm mất:", labelX2, startY + spacing, labelWidth, height));
        txtDeathYear = createTextField(fieldX2, startY + spacing, fieldWidth, height);
        txtDeathYear.setToolTipText("Để trống nếu còn sống");
        parent.add(txtDeathYear);
    }

    private void createBiographySection(JPanel parent) {
        int labelX = 50;
        int fieldX = 200;
        int startY = 230;
        int labelWidth = 140;
        int fieldWidth = 690;
        int fieldHeight = 100;

        parent.add(createLabel("Tiểu sử:", labelX, startY, labelWidth, 30));

        txtBiography = new JTextArea();
        txtBiography.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtBiography.setLineWrap(true);
        txtBiography.setWrapStyleWord(true);
        txtBiography.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(txtBiography);
        scrollPane.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);
    }

    private void createBooksTableSection(JPanel parent) {
        int sectionY = 360;
        int tableHeight = 250;

        JLabel lblBooksTitle = new JLabel("📚 Danh sách sách của tác giả");
        lblBooksTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBooksTitle.setForeground(TEXT_COLOR);
        lblBooksTitle.setBounds(50, sectionY, 400, 30);
        parent.add(lblBooksTitle);

        String[] columnNames = {"ID", "Tên sách", "Thể loại", "Năm xuất bản", "Số bản sao"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblBooks = new JTable(tableModel);
        tblBooks.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblBooks.setRowHeight(32);
        tblBooks.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblBooks.getTableHeader().setBackground(new Color(240, 240, 240));
        tblBooks.getTableHeader().setForeground(TEXT_COLOR);
        tblBooks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblBooks.setSelectionBackground(new Color(200, 230, 255));
        tblBooks.setGridColor(BORDER_COLOR);

        tblBooks.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblBooks.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblBooks.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblBooks.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblBooks.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(tblBooks);
        scrollPane.setBounds(50, sectionY + 40, 840, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel("💡 Nhấp đúp vào sách để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(50, sectionY + tableHeight + 45, 400, 20);
        parent.add(lblInfo);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 660;

        btnEdit = new JButton("Chỉnh sửa");
        btnEdit.setBounds(630, buttonY, buttonWidth, buttonHeight);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setBackground(new Color(255, 152, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnEdit);

        btnSave = new JButton("Lưu");
        btnSave.setBounds(630, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(PRIMARY_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setVisible(false);
        parent.add(btnSave);

        btnCancel = new JButton("Đóng");
        btnCancel.setBounds(760, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }


    // Get values
    public String getAuthorName() { return txtAuthorName.getText().trim(); }
    public String getBirthYearText() { return txtBirthYear.getText().trim(); }
    public String getDeathYearText() { return txtDeathYear.getText().trim(); }
    public String getCountry() { return txtCountry.getText().trim(); }
    public String getBiography() { return txtBiography.getText().trim(); }

    public void setAuthorId(int id) { txtAuthorId.setText(String.valueOf(id)); }
    public void setAuthorName(String name) { txtAuthorName.setText(name != null ? name : ""); }
    public void setCountry(String c) { txtCountry.setText(c != null ? c : ""); }
    public void setBiography(String bio) { txtBiography.setText(bio != null ? bio : ""); }

    public void setBirthYearFromDate(Date birthDate) {
        if (birthDate == null) { txtBirthYear.setText(""); return; }
        txtBirthYear.setText(String.valueOf(birthDate.toLocalDate().getYear()));
    }
    public void setDeathYearFromDate(Date deathDate) {
        if (deathDate == null) { txtDeathYear.setText(""); return; }
        txtDeathYear.setText(String.valueOf(deathDate.toLocalDate().getYear()));
    }
    public void setFieldsEditable(boolean editable) {
        txtAuthorName.setEditable(editable);
        txtBirthYear.setEditable(editable);
        txtBiography.setEditable(editable);
        txtCountry.setEditable(editable);
        txtDeathYear.setEditable(editable);

        Color bgColor = editable ? Color.WHITE : DISABLED_BG;
        txtAuthorName.setBackground(bgColor);
        txtBirthYear.setBackground(bgColor);
        txtBiography.setBackground(bgColor);
        txtCountry.setBackground(bgColor);
        txtDeathYear.setBackground(bgColor);
    }
    public void setEditMode(boolean editMode) {
        btnEdit.setVisible(!editMode);
        btnSave.setVisible(editMode);
        btnCancel.setText(editMode ? "Hủy" : "Đóng");
    }

    public void setBooksTableRows(List<Object[]> rows, List<Integer> bookIds) {
        displayedBookIds = (bookIds != null) ? bookIds : new ArrayList<>();
        tableModel.setRowCount(0);
        if (rows != null) {
            for (Object[] r : rows) tableModel.addRow(r);
        }
    }

    public Integer getSelectedBookIdOrNull() {
        int row = tblBooks.getSelectedRow();
        if (row < 0) return null;
        Object v = tableModel.getValueAt(row, 0);
        if (v == null) return null;
        if (v instanceof Integer) return (Integer) v;
        return Integer.parseInt(v.toString());
    }
    public boolean confirm(String msg) {
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
