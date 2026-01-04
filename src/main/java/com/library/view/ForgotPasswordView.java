package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * VIEW: Forgot Password (UI only)
 * - Không gọi DAO
 * - Không xử lý nghiệp vụ
 */
public class ForgotPasswordView extends JFrame {

    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private static final Color PRIMARY_COLOR = new Color(13, 110, 253);
    private static final Color SUCCESS_COLOR = new Color(13, 110, 253);
    private static final Color WARNING_COLOR = new Color(207, 83, 141);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    @Getter
    private JTextField txtEmail;
    @Getter
    private JTextField txtCode;
    @Getter
    private JPasswordField txtNewPassword;
    @Getter
    private JPasswordField txtConfirmPassword;

    @Getter
    private JButton btnVerifyEmail;
    @Getter
    private JButton btnSendCode;
    @Getter
    private JButton btnVerifyCode;
    @Getter
    private JButton btnChangePassword;
    @Getter
    private JButton btnCancel;

    private JLabel step1Num, step2Num, step3Num;

    public ForgotPasswordView() {
        initializeFrame();
        createComponents();
        setInitialState();
    }

    private void initializeFrame() {
        setTitle("Khôi phục mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 650);
        setMinimumSize(new Dimension(550, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createContent(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("Khôi phục mật khẩu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel stepPanel = createStepIndicator();

        panel.add(Box.createVerticalStrut(0));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(stepPanel);

        return panel;
    }

    private JPanel createStepIndicator() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(BG_COLOR);

        step1Num = createStepBadge("1", true);

        JLabel arrow1 = new JLabel("→");
        arrow1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        arrow1.setForeground(TEXT_SECONDARY);

        step2Num = createStepBadge("2", false);

        JLabel arrow2 = new JLabel("→");
        arrow2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        arrow2.setForeground(TEXT_SECONDARY);

        step3Num = createStepBadge("3", false);

        panel.add(step1Num);
        panel.add(arrow1);
        panel.add(step2Num);
        panel.add(arrow2);
        panel.add(step3Num);

        return panel;
    }

    private JLabel createStepBadge(String number, boolean active) {
        JLabel label = new JLabel(number);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(active ? Color.WHITE : TEXT_SECONDARY);
        label.setOpaque(true);
        label.setBackground(active ? PRIMARY_COLOR : new Color(206, 212, 218));
        label.setPreferredSize(new Dimension(35, 35));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(active ? PRIMARY_COLOR : new Color(206, 212, 218), 2));
        return label;
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        panel.add(createStep1Panel());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(15));

        panel.add(createStep2Panel());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createSeparator());
        panel.add(Box.createVerticalStrut(15));

        panel.add(createStep3Panel());
        panel.add(Box.createVerticalStrut(20));

        panel.add(createButtonPanel());

        return panel;
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBackground(CARD_BG);

        JLabel title = new JLabel("Bước 1: Xác thực Email       ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setBackground(CARD_BG);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        btnVerifyEmail = createButton("Xác thực", PRIMARY_COLOR);
        btnVerifyEmail.setPreferredSize(new Dimension(110, 38));

        inputPanel.add(txtEmail, BorderLayout.CENTER);
        inputPanel.add(btnVerifyEmail, BorderLayout.EAST);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(inputPanel);

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(CARD_BG);

        JLabel title = new JLabel("Bước 2: Xác nhận mã OTP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_SECONDARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputPanel.setBackground(CARD_BG);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        txtCode = new JTextField(12);
        txtCode.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCode.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtCode.setPreferredSize(new Dimension(150, 38));

        btnSendCode = createButton("Gửi mã", WARNING_COLOR);
        btnSendCode.setPreferredSize(new Dimension(100, 38));

        btnVerifyCode = createButton("✓ Xác nhận", SUCCESS_COLOR);
        btnVerifyCode.setPreferredSize(new Dimension(110, 38));

        inputPanel.add(txtCode);
        inputPanel.add(btnSendCode);
        inputPanel.add(btnVerifyCode);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(inputPanel);

        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(CARD_BG);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Bước 3: Mật khẩu mới");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(TEXT_SECONDARY);

        headerPanel.add(title);
        panel.add(headerPanel);

        JLabel label1 = new JLabel("Mật khẩu mới:");
        label1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label1.setForeground(TEXT_PRIMARY);
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNewPassword = new JPasswordField();
        txtNewPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNewPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtNewPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel label2 = new JLabel("Xác nhận mật khẩu:");
        label2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label2.setForeground(TEXT_PRIMARY);
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtConfirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        panel.add(label1);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtNewPassword);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label2);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtConfirmPassword);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panel.setBackground(CARD_BG);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnChangePassword = createButton("🔐 Đổi mật khẩu", SUCCESS_COLOR);
        btnChangePassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChangePassword.setPreferredSize(new Dimension(170, 42));

        btnCancel = createButton("Hủy bỏ", new Color(108, 117, 125));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setPreferredSize(new Dimension(100, 42));

        panel.add(btnChangePassword);
        panel.add(btnCancel);

        return panel;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }


    private void setInitialState() {
        setStep1ActiveUI();

        txtCode.setEnabled(false);
        btnSendCode.setEnabled(false);
        btnVerifyCode.setEnabled(false);

        txtNewPassword.setEnabled(false);
        txtConfirmPassword.setEnabled(false);
        btnChangePassword.setEnabled(false);
    }

    public void lockEmailVerified() {
        txtEmail.setEnabled(false);
        btnVerifyEmail.setEnabled(false);
        btnVerifyEmail.setText("✓ Đã xác thực");
        btnVerifyEmail.setBackground(SUCCESS_COLOR);

        // Step2 enable
        txtCode.setEnabled(true);
        btnSendCode.setEnabled(true);
        btnVerifyCode.setEnabled(false);

        setStep1DoneStep2ActiveUI();
    }

    public void markCodeSent() {
        btnSendCode.setEnabled(false);
        btnSendCode.setText("⏳ Đã gửi");
        btnVerifyCode.setEnabled(true);
    }

    public void enableResendCode() {
        btnSendCode.setEnabled(true);
        btnSendCode.setText("📨 Gửi lại");
    }

    public void lockCodeVerifiedEnableStep3() {
        txtCode.setEnabled(false);
        btnVerifyCode.setEnabled(false);
        btnVerifyCode.setText("✓ Đã xác nhận");
        btnSendCode.setEnabled(false);

        txtNewPassword.setEnabled(true);
        txtConfirmPassword.setEnabled(true);
        btnChangePassword.setEnabled(true);

        setStep2DoneStep3ActiveUI();
    }

    public void markStep3Done() {
        step3Num.setText("✓");
        step3Num.setBackground(SUCCESS_COLOR);
        step3Num.setForeground(Color.WHITE);
        step3Num.setBorder(BorderFactory.createLineBorder(SUCCESS_COLOR, 2));
    }

    private void setStep1ActiveUI() {
        // reset badges
        step1Num.setText("1");
        step1Num.setBackground(PRIMARY_COLOR);
        step1Num.setForeground(Color.WHITE);
        step1Num.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));

        step2Num.setText("2");
        step2Num.setBackground(new Color(206, 212, 218));
        step2Num.setForeground(TEXT_SECONDARY);
        step2Num.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 2));

        step3Num.setText("3");
        step3Num.setBackground(new Color(206, 212, 218));
        step3Num.setForeground(TEXT_SECONDARY);
        step3Num.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 2));
    }

    private void setStep1DoneStep2ActiveUI() {
        step1Num.setText("✓");
        step1Num.setBackground(SUCCESS_COLOR);
        step1Num.setForeground(Color.WHITE);
        step1Num.setBorder(BorderFactory.createLineBorder(SUCCESS_COLOR, 2));

        step2Num.setBackground(PRIMARY_COLOR);
        step2Num.setForeground(Color.WHITE);
        step2Num.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
    }

    private void setStep2DoneStep3ActiveUI() {
        step2Num.setText("✓");
        step2Num.setBackground(SUCCESS_COLOR);
        step2Num.setForeground(Color.WHITE);
        step2Num.setBorder(BorderFactory.createLineBorder(SUCCESS_COLOR, 2));

        step3Num.setBackground(PRIMARY_COLOR);
        step3Num.setForeground(Color.WHITE);
        step3Num.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
    }


    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

}
