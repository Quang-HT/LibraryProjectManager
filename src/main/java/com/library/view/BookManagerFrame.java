package com.library.view;

import com.library.controller.BookManagerController;
import com.library.model.display.BookDisplayModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * VIEW - Chỉ chịu trách nhiệm hiển thị UI và nhận input từ user
 * Không chứa logic xử lý dữ liệu
 */
public class BookManagerFrame extends JFrame {

    // Color Scheme
    private static final Color PRIMARY_BG   = new Color(245, 245, 245);
    private static final Color SECONDARY_BG = Color.WHITE;
    private static final Color SIDEBAR_BG   = new Color(250, 250, 250);
    private static final Color BUTTON_BG    = new Color(240, 240, 240);
    private static final Color BUTTON_HOVER = new Color(220, 220, 220);
    private static final Color SELECTED_BG  = new Color(25, 118, 210);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color CARD_HOVER   = new Color(240, 245, 250);

    // Components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel booksContainer;
    private JScrollPane booksScrollPane;
    private JTextField searchField;
    private JButton topBorrowedButton;
    private JComboBox<String> categoryComboBox;
    private JLabel totalBooksLabel;

    // Controller reference
    private BookManagerController bookManagerController;

    public BookManagerFrame() {
        initializeFrame();
        createComponents();
    }

    /**
     * Thiết lập controller cho view này
     */
    public void setController(BookManagerController controller) {
        this.bookManagerController = controller;
    }

    private void initializeFrame() {
        setTitle("Danh mục sách");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_BG);
    }

    private void createComponents() {
        createSidebar();
        createContentArea();
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(280, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // ====== THAO TÁC ======================================================
        JLabel actionsLabel = new JLabel("Thao tác");
        actionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        actionsLabel.setForeground(TEXT_PRIMARY);
        actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(actionsLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Nút thêm sách
        SidebarButton addBookBtn = new SidebarButton("+ Thêm đầu sách");
        addBookBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBookBtn.setBackground(new Color(25, 118, 210));
        addBookBtn.setForeground(Color.WHITE);
        addBookBtn.addActionListener(e -> {
            if (bookManagerController != null) {
                bookManagerController.handleAddBook();
            }
        });
        addBookBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                addBookBtn.setBackground(new Color(21, 101, 192));
            }
            public void mouseExited(MouseEvent evt) {
                addBookBtn.setBackground(new Color(25, 118, 210));
            }
        });
        sidebarPanel.add(addBookBtn);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(230, 1));
        separator.setForeground(BORDER_COLOR);
        JPanel sepPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        sepPanel.setBackground(SIDEBAR_BG);
        sepPanel.setMaximumSize(new Dimension(280, 10));
        sepPanel.add(separator);
        sidebarPanel.add(sepPanel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // ====== BỘ LỌC ========================================================
        JLabel filterLabel = new JLabel("Bộ lọc");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        filterLabel.setForeground(TEXT_PRIMARY);
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(filterLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tìm kiếm theo từ khóa
        JLabel searchLabel = new JLabel("Tìm kiếm(tên/tác giả):");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setMaximumSize(new Dimension(220,35));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchLabel.setBorder(new EmptyBorder(0, 0, 0, 0    ));
        sidebarPanel.add(searchLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(230,35));
        searchField.setMaximumSize(new Dimension(230, 900));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (bookManagerController != null) {
                    bookManagerController.handleSearchTextChanged(searchField.getText().trim());
                }
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        searchPanel.setBackground(SIDEBAR_BG);
        searchPanel.setMaximumSize(new Dimension(280, 40));
        searchPanel.add(searchField);
        sidebarPanel.add(searchPanel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Thể loại
        JLabel categoryLabel = new JLabel("Thể loại:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryLabel.setForeground(TEXT_PRIMARY);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(categoryLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryComboBox.setMaximumSize(new Dimension(230, 35));
        categoryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryComboBox.addActionListener(e -> {
            if (bookManagerController != null) {
                bookManagerController.handleCategoryChanged((String) categoryComboBox.getSelectedItem());
            }
        });

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        comboPanel.setBackground(SIDEBAR_BG);
        comboPanel.setMaximumSize(new Dimension(280, 40));
        comboPanel.add(categoryComboBox);
        sidebarPanel.add(comboPanel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Nút Top sách mượn nhiều
        topBorrowedButton = new JButton("Top sách mượn nhiều");
        topBorrowedButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topBorrowedButton.setFocusPainted(false);
        topBorrowedButton.setForeground(TEXT_PRIMARY);
        topBorrowedButton.setBackground(BUTTON_BG);
        topBorrowedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        topBorrowedButton.setBorder(new EmptyBorder(10, 20, 10, 20));

        topBorrowedButton.addActionListener(e -> {
            if (bookManagerController != null) {
                bookManagerController.handleToggleTopBorrowed();
            }
        });
        topBorrowedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (topBorrowedButton.getBackground().equals(BUTTON_BG)) {
                    topBorrowedButton.setBackground(BUTTON_HOVER);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (topBorrowedButton.getBackground().equals(BUTTON_HOVER)) {
                    topBorrowedButton.setBackground(BUTTON_BG);
                }
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        topPanel.setBackground(SIDEBAR_BG);
        topPanel.setMaximumSize(new Dimension(280, 45));
        topPanel.add(topBorrowedButton);
        sidebarPanel.add(topPanel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Tổng số sách
        totalBooksLabel = new JLabel("Tổng số: 0 sách");
        totalBooksLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        totalBooksLabel.setForeground(new Color(100, 100, 100));
        totalBooksLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalBooksLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(totalBooksLabel);

        sidebarPanel.add(Box.createVerticalGlue());

        // Nút quay lại
        SidebarButton homeBtn = new SidebarButton("← Quay lại");
        homeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        homeBtn.addActionListener(e -> goHome());
        sidebarPanel.add(homeBtn);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebarPanel, BorderLayout.WEST);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PRIMARY_BG);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel headerLabel = new JLabel("Danh mục sách");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(TEXT_PRIMARY);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Books Container
        booksContainer = new JPanel();
        booksContainer.setLayout(new BoxLayout(booksContainer, BoxLayout.Y_AXIS));
        booksContainer.setBackground(PRIMARY_BG);

        booksScrollPane = new JScrollPane(booksContainer);
        booksScrollPane.setBorder(null);
        booksScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        booksScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(booksScrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * PUBLIC METHODS - Interface cho Controller
     */

    public void displayBooks(List<BookDisplayModel> books) {
        booksContainer.removeAll();

        if (books.isEmpty()) {
            JLabel noResultLabel = new JLabel("Không có sách phù hợp với bộ lọc hiện tại");
            noResultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noResultLabel.setForeground(Color.GRAY);
            noResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            booksContainer.add(Box.createRigidArea(new Dimension(0, 50)));
            booksContainer.add(noResultLabel);
        } else {
            for (BookDisplayModel book : books) {
                JPanel bookCard = createBookCard(book);
                booksContainer.add(bookCard);
                booksContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        booksContainer.revalidate();
        booksContainer.repaint();
    }

    public void updateTotalBooksLabel(int count) {
        totalBooksLabel.setText("Tổng số: " + count + " sách");
    }

    public void updateTopBorrowedButton(boolean isActive) {
        if (isActive) {
            topBorrowedButton.setBackground(SELECTED_BG);
            topBorrowedButton.setForeground(Color.WHITE);
            topBorrowedButton.setText("✓ Đang xem Top mượn");
        } else {
            topBorrowedButton.setBackground(BUTTON_BG);
            topBorrowedButton.setForeground(TEXT_PRIMARY);
            topBorrowedButton.setText("⭐ Top sách mượn nhiều");
        }
    }

    public void updateCategoryComboBox(String[] categories, String selectedCategory) {
        categoryComboBox.removeAllItems();
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
        if (selectedCategory != null) {
            categoryComboBox.setSelectedItem(selectedCategory);
        }
    }

    private JPanel createBookCard(BookDisplayModel book) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SECONDARY_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 25, 10, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Left side - Book info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(SECONDARY_BG);

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("Tác giả: " + book.getAuthor());
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        authorLabel.setForeground(new Color(100, 100, 100));
        authorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        authorLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel categoryLabel = new JLabel("Thể loại: " + book.getCategory() + " | Năm: " + book.getYear());
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        categoryLabel.setForeground(new Color(100, 100, 100));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryLabel.setBorder(new EmptyBorder(3, 0, 0, 0));

        JLabel locationLabel = new JLabel("Vị trí: " + book.getLocation());
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        locationLabel.setForeground(new Color(100, 100, 100));
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        locationLabel.setBorder(new EmptyBorder(3, 0, 0, 0));

        JLabel borrowLabel = new JLabel("Lượt mượn: " + book.getBorrowCount());
        borrowLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        borrowLabel.setForeground(new Color(255, 140, 0));
        borrowLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        borrowLabel.setBorder(new EmptyBorder(3, 0, 0, 0));

        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(locationLabel);
        infoPanel.add(borrowLabel);

        // Right side - Price
        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setBackground(SECONDARY_BG);

        JLabel priceLabel = new JLabel(String.format("%,d VNĐ", book.getCost()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLabel.setForeground(new Color(25, 118, 210));
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        priceLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        pricePanel.add(priceLabel, BorderLayout.NORTH);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(pricePanel, BorderLayout.EAST);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(CARD_HOVER);
                infoPanel.setBackground(CARD_HOVER);
                pricePanel.setBackground(CARD_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(SECONDARY_BG);
                infoPanel.setBackground(SECONDARY_BG);
                pricePanel.setBackground(SECONDARY_BG);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (bookManagerController != null) {
                    bookManagerController.handleBookClicked(book.getBookId());
                    System.out.println(book.getBookId());
                }
            }
        });

        return card;
    }

    private void goHome() {
        HomeView mf = new HomeView();
        mf.setVisible(true);
        this.dispose();
    }

    // Custom Sidebar Button
    private class SidebarButton extends JButton {
        public SidebarButton(String text) {
            super(text);
            setMaximumSize(new Dimension(280, 45));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setBackground(BUTTON_BG);
            setForeground(TEXT_PRIMARY);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(12, 25, 12, 25));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    setBackground(BUTTON_HOVER);
                }
                public void mouseExited(MouseEvent evt) {
                    setBackground(BUTTON_BG);
                }
            });
        }
    }
}