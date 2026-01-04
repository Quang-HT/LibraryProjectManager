package com.library.view;

import com.library.model.display.BookDisplayModel;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;

/**
 * Book Detail Form - View only (UI)
 */
public class BookDetailForm extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color DISABLED_BG = new Color(245, 245, 245);

    private JTextField txtBookId;
    private JTextField txtBookName;
    private JTextField txtPublishYear;
    private JComboBox<String> cmbCategory;
    private JComboBox<String> cmbAuthor;
    private JComboBox<String> cmbPublisher;
    private JTextField txtCopyPrice;
    private JTextField txtLocation;
    private JTextField txtCopyCount;
    private JTextArea txtDescription;

    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnEdit;

    @Getter
    private boolean isEditMode = false;

    public BookDetailForm() {
        initializeFrame();
        createComponents();
        setEditMode(false);
    }


    public void setBookDetail(BookDisplayModel dto) {
        txtBookId.setText(String.valueOf(dto.getBookId()));
        txtBookName.setText(dto.getTitle() == null ? "" : dto.getTitle());
        txtPublishYear.setText(String.valueOf(dto.getYear()));

        if (dto.getCategory() != null) cmbCategory.setSelectedItem(dto.getCategory());
        if (dto.getAuthor() != null) cmbAuthor.setSelectedItem(dto.getAuthor());

        String pub = (dto.getPublisher() == null) ? "Không có nhà xuất bản" : dto.getPublisher();
        cmbPublisher.setSelectedItem(pub);

        txtCopyPrice.setText(String.valueOf(dto.getCost()));
        txtLocation.setText(dto.getLocation() == null ? "" : dto.getLocation());
        txtCopyCount.setText(String.valueOf(dto.getCopyCount()));
        txtDescription.setText(dto.getDescription() == null ? "" : dto.getDescription());
    }

    public BookDisplayModel getFormData() {
        if (txtBookName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        if (cmbCategory.getSelectedIndex() <0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thể loại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        if (cmbAuthor.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tác giả!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        BookDisplayModel dto = new BookDisplayModel();
        dto.setBookId(parseIntSafe(txtBookId.getText()));
        dto.setTitle(txtBookName.getText().trim());
        dto.setYear(parseIntSafe(txtPublishYear.getText()));
        dto.setCategory((String) cmbCategory.getSelectedItem());
        dto.setAuthor((String) cmbAuthor.getSelectedItem());
        dto.setPublisher((String) cmbPublisher.getSelectedItem());
        dto.setCost(parseIntSafe(txtCopyPrice.getText()));
        dto.setLocation(txtLocation.getText().trim());
        dto.setCopyCount(parseIntSafe(txtCopyCount.getText()));
        dto.setDescription(txtDescription.getText().trim());
        return dto;
    }

    public void setCategories(List<String> items) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (items != null) for (String s : items) model.addElement(s);
        model.addElement("Tất cả");
        cmbCategory.setModel(model);
    }

    public void setAuthors(List<String> items) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (items != null) for (String s : items) model.addElement(s);
        cmbAuthor.setModel(model);
    }

    public void setPublishers(List<String> items) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (items != null) for (String s : items) model.addElement(s);
        model.addElement("Không có nhà xuất bản");
        cmbPublisher.setModel(model);
    }

    public void onEdit(Runnable action) { btnEdit.addActionListener(e -> action.run()); }
    public void onSave(Runnable action) { btnSave.addActionListener(e -> action.run()); }
    public void onCancel(Runnable action) { btnCancel.addActionListener(e -> action.run()); }

    public void setEditMode(boolean editable) {
        isEditMode = editable;

        txtBookName.setEditable(editable);
        txtPublishYear.setEditable(editable);
        cmbCategory.setEnabled(editable);
        cmbAuthor.setEnabled(editable);
        cmbPublisher.setEnabled(editable);
        txtCopyPrice.setEditable(editable);
        txtLocation.setEditable(editable);
        txtCopyCount.setEditable(editable);
        txtDescription.setEditable(editable);


        Color bgColor = editable ? Color.WHITE : DISABLED_BG;
        txtBookName.setBackground(bgColor);
        txtPublishYear.setBackground(bgColor);
        txtCopyPrice.setBackground(bgColor);
        txtLocation.setBackground(bgColor);
        txtCopyCount.setBackground(bgColor);
        txtDescription.setBackground(bgColor);

        btnEdit.setVisible(!editable);
        btnSave.setVisible(editable);
        btnCancel.setText(editable ? "Hủy" : "Đóng");
    }

    public void closeForm() { dispose(); }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }


    private void initializeFrame() {
        setTitle("Chi tiết thông tin sách");
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

        JLabel titleLabel = new JLabel("Chi tiết thông tin sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(350, 20, 350, 35);
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

        parent.add(createLabel("ID Sách:", labelX, startY, labelWidth, height));
        txtBookId = createTextField(fieldX, startY, fieldWidth, height);
        txtBookId.setEditable(false);
        txtBookId.setBackground(DISABLED_BG);
        parent.add(txtBookId);

        parent.add(createLabel("Tên sách:", labelX, startY + spacing, labelWidth, height));
        txtBookName = createTextField(fieldX, startY + spacing, fieldWidth, height);
        parent.add(txtBookName);

        parent.add(createLabel("Năm xuất bản:", labelX, startY + spacing * 2, labelWidth, height));
        txtPublishYear = createTextField(fieldX, startY + spacing * 2, fieldWidth, height);
        parent.add(txtPublishYear);

        parent.add(createLabel("Thuộc thể loại:", labelX, startY + spacing * 3, labelWidth, height));
        cmbCategory = new JComboBox<>();
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbCategory.setBounds(fieldX, startY + spacing * 3, fieldWidth, height);
        parent.add(cmbCategory);

        parent.add(createLabel("Tác giả:", labelX, startY + spacing * 4, labelWidth, height));
        cmbAuthor = new JComboBox<>();
        cmbAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbAuthor.setBounds(fieldX, startY + spacing * 4, fieldWidth, height);
        parent.add(cmbAuthor);

        parent.add(createLabel("Nhà xuất bản:", labelX, startY + spacing * 5, labelWidth, height));
        cmbPublisher = new JComboBox<>();
        cmbPublisher.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbPublisher.setBounds(fieldX, startY + spacing * 5, fieldWidth, height);
        parent.add(cmbPublisher);
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
    }

    private void createDescriptionSection(JPanel parent) {
        int labelX = 50;
        int fieldX = 220;
        int startY = 380;
        int labelWidth = 150;
        int fieldWidth = 670;
        int fieldHeight = 100;

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

        btnEdit = new JButton("Chỉnh sửa");
        btnEdit.setBounds(640, buttonY, buttonWidth, buttonHeight);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setBackground(new Color(255, 152, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnEdit);

        btnSave = new JButton("Lưu");
        btnSave.setBounds(640, buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(25, 118, 210));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setVisible(false);
        parent.add(btnSave);

        btnCancel = new JButton("Đóng");
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

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }
}
