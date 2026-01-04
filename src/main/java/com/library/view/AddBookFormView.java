package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;

public class AddBookFormView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);

    private JTextField txtBookName;
    private JTextField txtPublishYear;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbAuthor;
    private JComboBox<String> cmbPublisher;
    private JTextField txtCopyPrice;
    private JTextField txtLocation;
    private JTextField txtCopyCount;
    private JTextArea txtDescription;
    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnCancel;

    public AddBookFormView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Thêm đầu sách mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
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

        JLabel titleLabel = new JLabel("Thêm đầu sách mới");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(380, 20, 300, 35);
        mainPanel.add(titleLabel);

        createLeftColumn(mainPanel);
        createRightColumn(mainPanel);
        createDescriptionSection(mainPanel);
        createButtons(mainPanel);

        add(mainPanel);
    }

    private void createLeftColumn(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 150;
        int fieldWidth = 200;
        int height = 30;

        parent.add(createLabel("Tên sách:", labelX, startY, labelWidth, height));
        txtBookName = createTextField(fieldX, startY, fieldWidth, height);
        parent.add(txtBookName);

        parent.add(createLabel("Năm xuất bản:", labelX, startY + spacing, labelWidth, height));
        txtPublishYear = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtPublishYear);

        parent.add(createLabel("Thuộc thể loại:", labelX, startY + spacing * 2, labelWidth, height));
        cmbCategory = createComboBox(List.of("Chọn thể loại"), fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(cmbCategory);

        parent.add(createLabel("Tác giả:", labelX, startY + spacing * 3, labelWidth, height));
        cmbAuthor = createComboBox(List.of("Chọn tác giả"), fieldX, startY + spacing * 3, fieldWidth, height);
        parent.add(cmbAuthor);
    }

    private void createRightColumn(JPanel parent) {
        int labelX = 520;
        int fieldX = 690;
        int startY = 80;
        int spacing = 50;
        int labelWidth = 150;
        int fieldWidth = 200;
        int height = 30;

        parent.add(createLabel("Giá trị bản sao:", labelX, startY, labelWidth, height));
        txtCopyPrice = createTextField(fieldX, startY, fieldWidth, height);
        parent.add(txtCopyPrice);

        parent.add(createLabel("Vị trí:", labelX, startY + spacing, labelWidth, height));
        txtLocation = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtLocation);

        parent.add(createLabel("Số lượng bản sao:", labelX, startY + spacing * 2, labelWidth, height));
        txtCopyCount = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(txtCopyCount);

        parent.add(createLabel("Nhà xuất bản:", labelX, startY + spacing * 3, labelWidth, height));
        cmbPublisher = createComboBox(List.of("Chọn nhà xuất bản"), fieldX, startY + spacing * 3, fieldWidth, height);
        parent.add(cmbPublisher);
    }


    private void createDescriptionSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 330;
        int labelWidth = 150;
        int fieldWidth = 670;
        int fieldHeight = 200;

        parent.add(createLabel("Mô tả:", labelX, startY, labelWidth, 30));

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JScrollPane scrollPane = new JScrollPane(txtDescription);
        scrollPane.setBounds(fieldX, startY, fieldWidth, fieldHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 120;
        int buttonHeight = 40;
        int buttonY = 570;

        btnSave = new JButton("Lưu");
        btnSave.setBounds(640, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(25, 118, 210));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSave);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(770, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
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
    private JComboBox<String> createComboBox(List<String> items, int x, int y, int width, int height) {
        JComboBox<String> comboBox = new JComboBox<>(items.toArray(new String[0]));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBounds(x, y, width, height);
        comboBox.setBackground(Color.WHITE);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return comboBox;
    }


    public String getBookName() { return txtBookName.getText().trim(); }
    public String getPublishYearText() { return txtPublishYear.getText().trim(); }
    public String getCopyPriceText() { return txtCopyPrice.getText().trim(); }
    public String ggetLocation() { return txtLocation.getText().trim(); }
    public String getCopyCountText() { return txtCopyCount.getText().trim(); }
    public String getDescription() { return txtDescription.getText().trim(); }

    public int getSelectedCategoryIndex() { return cmbCategory.getSelectedIndex(); }
    public int getSelectedAuthorIndex() { return cmbAuthor.getSelectedIndex(); }
    public int getSelectedPublisherIndex() { return cmbPublisher.getSelectedIndex(); }

    public void setCategoryItems(List<String> items) {
        cmbCategory.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
        cmbCategory.setSelectedIndex(0);
    }

    public void setAuthorItems(List<String> items) {
        cmbAuthor.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
        cmbAuthor.setSelectedIndex(0);
    }

    public void setPublisherItems(List<String> items) {
        cmbPublisher.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
        cmbPublisher.setSelectedIndex(0);
    }
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    public boolean confirm(String msg) {
        int confirm = JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION);
        return confirm == JOptionPane.YES_OPTION;
    }

    public void requestFocusBookName() { txtBookName.requestFocus(); }
    public void requestFocusPublishYear() { txtPublishYear.requestFocus(); }
    public void requestFocusCopyPrice() { txtCopyPrice.requestFocus(); }
    public void requestFocusCopyCount() { txtCopyCount.requestFocus(); }

    public void clearForm() {
        txtBookName.setText("");
        txtPublishYear.setText("");
        cmbCategory.setSelectedIndex(0);
        cmbAuthor.setSelectedIndex(0);
        cmbPublisher.setSelectedIndex(0);
        txtCopyPrice.setText("");
        txtLocation.setText("");
        txtCopyCount.setText("");
        txtDescription.setText("");
    }
}
