package com.library.view;

import com.library.controller.LoginController;
import lombok.Setter;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

import static com.library.service.ImagineApp.createBookIcon;

public class LoginView extends JFrame {

    public static final Color PRIMARY_BLUE = new Color(25, 118, 210);
    private static final Color DARK_BLUE = new Color(13, 71, 161);
    private static final Color LIGHT_BLUE = new Color(227, 242, 253);
    private static final Color ACCENT_BLUE = new Color(66, 165, 245);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_GRAY = new Color(97, 97, 97);
    private static final Color BORDER_GRAY = new Color(189, 189, 189);

    private JTextField usernameField;
    private JPasswordField passwordField;


    @Setter
    private LoginController controller;

    public LoginView() {
        initializeFrame();
        createUI();
    }

    private void initializeFrame() {
        setTitle("Hệ Thống Quản Lý Thư Viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        try {
            setIconImage(createBookIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LIGHT_BLUE);

        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 6, getHeight() - 6, 30, 30));

                g2.dispose();
            }
        };
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(950, 550));

        JPanel leftPanel = createLeftPanel();

        JPanel rightPanel = createRightPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.55;
        contentPanel.add(rightPanel, gbc);

        mainPanel.add(contentPanel);
        add(mainPanel);
    }
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, 0, getHeight(), DARK_BLUE);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2.setColor(WHITE);
                g2.fillOval(-50, -50, 200, 200);
                g2.fillOval(getWidth() - 100, getHeight() - 150, 180, 180);
                g2.fillOval(-30, getHeight() - 100, 120, 120);

                g2.dispose();
            }
        };
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int bookW = 80, bookH = 100;
                int x = (w - bookW) / 2, y = (h - bookH) / 2;

                g2.setColor(new Color(0, 0, 0, 40));
                g2.fill(new RoundRectangle2D.Double(x + 5, y + 5, bookW, bookH, 8, 8));

                g2.setColor(WHITE);
                g2.fill(new RoundRectangle2D.Double(x, y, bookW, bookH, 8, 8));

                g2.setColor(LIGHT_BLUE);
                g2.fillRect(x, y, 15, bookH);

                g2.setColor(ACCENT_BLUE);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(x + 25, y + 25, x + 65, y + 25);
                g2.drawLine(x + 25, y + 40, x + 65, y + 40);
                g2.drawLine(x + 25, y + 55, x + 55, y + 55);

                // Bookmark
                g2.setColor(new Color(239, 83, 80));
                int[] xPoints = {x + 60, x + 72, x + 66};
                int[] yPoints = {y - 5, y - 5, y + 15};
                g2.fillPolygon(xPoints, yPoints, 3);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 130);
            }
        };
        iconPanel.setOpaque(false);

        gbc.gridy = 0;
        gbc.insets = new Insets(40, 30, 20, 30);
        panel.add(iconPanel, gbc);

        JLabel titleLabel = new JLabel("<html><center>HỆ THỐNG<br/>QUẢN LÝ THƯ VIỆN</center></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 30, 10, 30);
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("<html><center>Giải pháp quản lý thư viện<br/>hiện đại và hiệu quả</center></html>");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 2;
        panel.add(subtitleLabel, gbc);

        // Features
        String[] features = {" Quản lý sách và tài liệu", " Quản lý độc giả", " Thống kê và báo cáo"};
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);

        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            featureLabel.setForeground(new Color(255, 255, 255, 180));
            featureLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            featureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featuresPanel.add(featureLabel);
        }

        gbc.gridy = 3;
        gbc.insets = new Insets(30, 30, 40, 30);
        panel.add(featuresPanel, gbc);

        return panel;
    }
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
                g2.dispose();
            }
        };
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 60, 5, 60);

        // Welcome text
        JLabel welcomeLabel = new JLabel("Chào mừng trở lại!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(DARK_BLUE);

        gbc.gridy = 0;
        gbc.insets = new Insets(40, 60, 5, 60);
        panel.add(welcomeLabel, gbc);

        JLabel instructionLabel = new JLabel("Vui lòng đăng nhập để tiếp tục");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(TEXT_GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 60, 25, 60);
        panel.add(instructionLabel, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(TEXT_GRAY);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 60, 5, 60);
        panel.add(usernameLabel, gbc);

        usernameField = createStyledTextField();
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 60, 10, 60);
        panel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(TEXT_GRAY);

        gbc.gridy = 4;
        gbc.insets = new Insets(10, 60, 5, 60);
        panel.add(passwordLabel, gbc);

        passwordField = createStyledPasswordField();

        // Add Enter key listener to password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && controller != null) {
                    controller.handleLogin();
                }
            }
        });

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 60, 10, 60);
        panel.add(passwordField, gbc);

        // Remember & Forgot
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);

        JCheckBox rememberCheckbox = new JCheckBox("Ghi nhớ đăng nhập");
        rememberCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberCheckbox.setForeground(TEXT_GRAY);
        rememberCheckbox.setOpaque(false);
        rememberCheckbox.setFocusPainted(false);

        JButton forgotButton = new JButton("<html><u>Quên mật khẩu?</u></html>");
        forgotButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleForgotPassword();
            }
        });

        forgotButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotButton.setForeground(PRIMARY_BLUE);
        forgotButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotButton.setFocusPainted(false);
        forgotButton.setBorderPainted(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setOpaque(false);

        forgotButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotButton.setForeground(DARK_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                forgotButton.setForeground(PRIMARY_BLUE);
            }
        });

        optionsPanel.add(rememberCheckbox, BorderLayout.WEST);
        optionsPanel.add(forgotButton, BorderLayout.EAST);

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 60, 15, 60);
        panel.add(optionsPanel, gbc);

        // Login button
        JButton loginButton = createStyledButton("ĐĂNG NHẬP", PRIMARY_BLUE, WHITE);
        loginButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleLogin();
            }
        });

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 60, 10, 60);
        panel.add(loginButton, gbc);

        // Reader button
        JButton readerButton = createStyledButton("Giao Diện Độc Giả", WHITE, PRIMARY_BLUE);
        readerButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY_BLUE, 2, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        readerButton.addActionListener(e -> {
            if (controller != null) {
                controller.handleOpenReaderInterface();
            }
        });

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 60, 30, 60);
        panel.add(readerButton, gbc);

        // Footer
        JLabel footerLabel = new JLabel("© 2026 Library Management System");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(BORDER_GRAY);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 9;
        gbc.insets = new Insets(20, 60, 20, 60);
        panel.add(footerLabel, gbc);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(BORDER_GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString("Nhập tên đăng nhập...", getInsets().left,
                            g.getFontMetrics().getMaxAscent() + getInsets().top);
                    g2.dispose();
                }
            }
        };

        styleTextField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(BORDER_GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString("Nhập mật khẩu...", getInsets().left,
                            g.getFontMetrics().getMaxAscent() + getInsets().top);
                    g2.dispose();
                }
            }
        };

        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY_BLUE, 2, true),
                        BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
                field.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_GRAY, 1, true),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                field.repaint();
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.equals(WHITE) ? LIGHT_BLUE : ACCENT_BLUE);
                } else {
                    g2.setColor(bgColor);
                }

                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));

                g2.setColor(fgColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 48));
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }


    public String getUsername() {
        return usernameField.getText().trim();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void closeFrame() {
        this.dispose();
    }
}