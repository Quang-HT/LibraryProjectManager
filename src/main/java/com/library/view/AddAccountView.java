package com.library.view;

import com.library.model.Staff_Entity;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VIEW: Tạo tài khoản mới (UI only)
 */
@Getter
public class AddAccountView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JComboBox<Staff_Entity> cmbEmployee;
    private JButton btnSave;
    private JButton btnCancel;

    public AddAccountView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Tạo Tài khoản mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Tạo Tài khoản mới");
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

        JLabel lblUsername = createLabel("Username:", labelX, startY, labelWidth, height);
        addRequiredMark(parent, lblUsername);
        parent.add(lblUsername);

        txtUsername = createTextField(fieldX, startY, fieldWidth, height);
        txtUsername.setToolTipText("Tên đăng nhập (không dấu, không khoảng trắng)");
        parent.add(txtUsername);

        JLabel lblPassword = createLabel("Password:", labelX, startY + spacing, labelWidth, height);
        addRequiredMark(parent, lblPassword);
        parent.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(fieldX, startY + spacing, fieldWidth, height);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtPassword.setToolTipText("Mật khẩu (tối thiểu 6 ký tự)");
        parent.add(txtPassword);

        JLabel lblConfirmPassword = createLabel("Xác nhận Password:", labelX, startY + spacing * 2, labelWidth, height);
        addRequiredMark(parent, lblConfirmPassword);
        parent.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setBounds(fieldX, startY + spacing * 2, fieldWidth, height);
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        parent.add(txtConfirmPassword);

        JLabel lblEmployee = createLabel("Nhân viên:", labelX, startY + spacing * 3, labelWidth, height);
        addRequiredMark(parent, lblEmployee);
        parent.add(lblEmployee);

        cmbEmployee = new JComboBox<>();
        cmbEmployee.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbEmployee.setBounds(fieldX, startY + spacing * 3, fieldWidth, height);
        cmbEmployee.setBackground(Color.WHITE);
        cmbEmployee.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmbEmployee.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Staff_Entity) {
                    Staff_Entity staff = (Staff_Entity) value;
                    setText(staff.getStaff_id() + " - " + staff.getStaff_name() + " (" + staff.getEmail() + ")");
                } else if (value == null) {
                    setText("Chọn nhân viên");
                }
                return this;
            }
        });
        parent.add(cmbEmployee);

        JLabel lblInfo = new JLabel("Account ID sẽ tự động bằng Staff ID");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 100, 100));
        lblInfo.setBounds(fieldX, startY + spacing * 4, fieldWidth, 20);
        parent.add(lblInfo);
    }

    private void createButtons(JPanel parent) {
        int buttonWidth = 130;
        int buttonHeight = 42;
        int buttonY = 370;

        btnSave = new JButton("Tạo TK");
        btnSave.setBounds(210   , buttonY, buttonWidth, buttonHeight);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSave);

        btnCancel = new JButton("Hủy");
        btnCancel.setBounds(370, buttonY, buttonWidth, buttonHeight);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.setBackground(new Color(200, 200, 200));
        btnCancel.setForeground(TEXT_COLOR);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnCancel);
    }


    public void setStaffList(java.util.List<Staff_Entity> staffs) {
        cmbEmployee.removeAllItems();
        cmbEmployee.addItem(null);
        if (staffs != null) {
            for (Staff_Entity s : staffs) cmbEmployee.addItem(s);
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
}
