package com.library.view;

import com.library.controller.HomeController;
import com.library.dao.impl.*;
import com.library.model.Account_Entity;
import com.library.service.FIND;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

import static com.library.service.ImagineApp.createBookIcon;

public class HomeView extends JFrame {

    private static final Color PRIMARY_BG = new Color(230, 240, 250);
    private static final Color SECONDARY_BG = Color.WHITE;
    private static final Color SIDEBAR_BG = new Color(25, 118, 210);
    private static final Color BUTTON_BG = new Color(30, 136, 229);
    private static final Color BUTTON_HOVER = new Color(21, 101, 192);
    private static final Color SUCCESS_BG = new Color(76, 175, 80);
    private static final Color SUCCESS_HOVER = new Color(56, 142, 60);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 200, 200);

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel toolbarPanel;

    @Getter
    @Setter
    private Account_Entity currentAccount;


    @Setter
    private boolean isManager = false;
    private String currentModule = "Dashboard";

    public HomeView() {
        HomeController controller = new HomeController(this);
        initializeFrame();
        createComponents();
    }



    private void initializeFrame() {
        setTitle("Hệ thống Quản lý Thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_BG);
        try {
            setIconImage(createBookIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createComponents() {
        createSidebar();
        createHeader();
        createToolbar();
        createContentArea();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));

        HomeController controller = new HomeController(this);

        JPanel headerSection = new JPanel();
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        headerSection.setBackground(SIDEBAR_BG);
        headerSection.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(" Thư viện");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerSection.add(titleLabel);

        if (currentAccount != null) {
            headerSection.add(Box.createRigidArea(new Dimension(0, 10)));

            JLabel userLabel = new JLabel(" " + currentAccount.getUsername());
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            userLabel.setForeground(new Color(200, 230, 255));
            userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            headerSection.add(userLabel);


            if (isManager) {
                headerSection.add(Box.createRigidArea(new Dimension(0, 5)));

                JLabel roleLabel = new JLabel(" Quản lý");
                roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                roleLabel.setForeground(new Color(255, 235, 59));
                roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                headerSection.add(roleLabel);
            }
        }

        sidebarPanel.add(headerSection);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        addNavButton(" Thông tin tài khoản", "Account", controller::openAccountInfo);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addSeparator();

        addNavButton(" Quản lý đầu sách", "Books", controller::openBookManagement);
        addNavButton(" Quản lý tác giả", "Authors", controller::openAuthorManagement);
        addNavButton(" Quản lý độc giả", "Readers", controller::openReaderManagement);
        addNavButton(" Quản lý mượn/trả", "Borrow", controller::openBorrowManagement);

        if (isManager) {
            addSeparator();
            addNavButton(" Quản lý nhân viên", "Staff", controller::openStaffManagement);
            addNavButton(" Quản lý tài khoản", "Accounts", controller::openAccountManagement);
        }

        sidebarPanel.add(Box.createVerticalGlue());
        addSeparator();

        SidebarButton logoutBtn = new SidebarButton(" Đăng xuất");
        logoutBtn.setBackground(new Color(13, 71, 161));
        logoutBtn.addActionListener(e -> controller.logout());
        sidebarPanel.add(logoutBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(sidebarPanel, BorderLayout.WEST);
    }


    private void addNavButton(String text, String module, Runnable action) {
        SidebarButton btn = new SidebarButton(text);
        btn.addActionListener(e -> {
            currentModule = module;
            action.run();
        });
        sidebarPanel.add(btn);
    }

    private void addSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(100, 150, 200));
        sep.setMaximumSize(new Dimension(250, 1));
        sidebarPanel.add(sep);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void createHeader() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SECONDARY_BG);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel welcomeLabel = new JLabel("Hệ thống Quản lý Thư viện");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomeLabel.setBorder(new EmptyBorder(0, 25, 0, 0));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createToolbar() {
        toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        toolbarPanel.setBackground(new Color(245, 250, 255));
        toolbarPanel.setPreferredSize(new Dimension(getWidth(), 65));
        toolbarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(220, 230, 240)));

        HomeController controller = new HomeController(this);
        addToolbarButton("➕ Tạo phiếu mượn",  controller::createNewBorrowSlip);
        addToolbarButton("➕ Thêm đầu sách",  controller::createNewBook);
        addToolbarButton("➕ Thêm độc giả" ,controller::createNewReader);
        addToolbarButton("➕ Thêm tác giả", controller::createNewAuthor);

        if (isManager) {
            addToolbarButton("➕ Thêm nhân viên", controller::createNewStaff);
            addToolbarButton("➕ Tạo tài khoản", controller::createNewAccount);
        }

        add(toolbarPanel, BorderLayout.CENTER);
    }

    private void addToolbarButton(String text, Runnable action) {
        JButton btn = new JButton("<html><center>" + "<br><small>" + text + "</small></center></html>");
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setBackground(SUCCESS_BG);
        btn.setForeground(TEXT_WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(SUCCESS_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(SUCCESS_BG);
            }
        });

        btn.addActionListener(e -> action.run());
        toolbarPanel.add(btn);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(SECONDARY_BG);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        showDefaultContent();

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(toolbarPanel, BorderLayout.NORTH);
        wrapper.add(contentPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }


    private void showDefaultContent() {
        contentPanel.removeAll();

        JPanel defaultPanel = new JPanel();
        defaultPanel.setLayout(new BoxLayout(defaultPanel, BoxLayout.Y_AXIS));
        defaultPanel.setBackground(SECONDARY_BG);

        JLabel titleLabel = new JLabel("     Thống kê hệ thống");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        defaultPanel.add(titleLabel);
        defaultPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        try {
            int totalBooks = new Book_DAO().findAll().size();
            int totalCopies = new Copy_DAO().findAll().size();
            int borrowedCount = new FIND().countBorrowed();
            int monthlyBorrows = new BorrowSlip_DAO()
                    .findCountInMonth(LocalDate.now().getMonthValue(), LocalDate.now().getYear())
                    .size();

            defaultPanel.add(createStatCard(" Tổng đầu sách", String.valueOf(totalBooks)));
            defaultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            defaultPanel.add(createStatCard(" Tổng bản sao", String.valueOf(totalCopies)));
            defaultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            defaultPanel.add(createStatCard(" Sách đang cho mượn", String.valueOf(borrowedCount)));
            defaultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            defaultPanel.add(createStatCard(
                    " Lượt mượn tháng " + LocalDate.now().getMonthValue() + "/" + LocalDate.now().getYear(),
                    String.valueOf(monthlyBorrows)
            ));

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel(" Không thể tải thống kê");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            errorLabel.setForeground(Color.RED);
            defaultPanel.add(errorLabel);
        }

        contentPanel.add(defaultPanel, BorderLayout.NORTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 250, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(900, 80));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(25, 118, 210));

        card.add(titleLabel, BorderLayout.WEST);
        card.add(valueLabel, BorderLayout.EAST);
        return card;
    }

    private class SidebarButton extends JButton {
        public SidebarButton(String text) {
            super(text);
            setMaximumSize(new Dimension(250, 45));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setBackground(BUTTON_BG);
            setForeground(TEXT_WHITE);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(BUTTON_HOVER);
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(BUTTON_BG);
                }
            });
        }
    }
}