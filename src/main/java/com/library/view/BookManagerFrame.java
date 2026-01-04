package com.library.view;

import com.library.controller.BookManagerController;
import com.library.model.display.BookDisplayModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BookManagerFrame extends JFrame {
    // Colors
    private static final Color PRIMARY = new Color(29, 92, 241);
    private static final Color PRIMARY_DARK = new Color(33, 44, 142);
    private static final Color PRIMARY_LIGHT = new Color(238, 242, 255);
    private static final Color WARNING = new Color(251, 146, 60);
    private static final Color BG_MAIN = new Color(249, 250, 251);
    private static final Color BG_CARD = Color.WHITE;
    private static final Color BG_HOVER = new Color(243, 244, 246);
    private static final Color TEXT_PRIMARY = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private static final Color TEXT_MUTED = new Color(156, 163, 175);
    private static final Color BORDER_LIGHT = new Color(229, 231, 235);
    private static final Color BORDER_DEFAULT = new Color(209, 213, 219);


    private JPanel booksContainer;
    private JTextField searchField;
    private JButton topBorrowedButton;
    private JComboBox<String> categoryComboBox;
    private JLabel totalBooksLabel;
    private BookManagerController bookManagerController;

    public BookManagerFrame() {
        initFrame();
        add(createSidebar(), BorderLayout.WEST);
        add(createContent(), BorderLayout.CENTER);
    }

    public void setController(BookManagerController controller) {
        this.bookManagerController = controller;
    }

    private void initFrame() {
        setTitle("Quản lý Thư viện");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_MAIN);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(320, getHeight()));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_LIGHT),
                new EmptyBorder(30, 25, 30, 25)
        ));

        sidebar.add(createLabel("Thư Viện", 28, Font.BOLD, PRIMARY));
        sidebar.add(createLabel("Hệ thống quản lý sách", 13, Font.PLAIN, TEXT_SECONDARY));
        sidebar.add(Box.createRigidArea(new Dimension(0, 35)));

        JButton addBtn = createButton("+ Thêm đầu sách mới", PRIMARY, Color.WHITE, PRIMARY_DARK);
        addBtn.addActionListener(e -> {
            if (bookManagerController != null) bookManagerController.handleAddBook();
        });
        sidebar.add(addBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createDivider());
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        sidebar.add(createLabel("BỘ LỌC & TÌM KIẾM", 11, Font.BOLD, TEXT_MUTED));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        sidebar.add(createLabel("Tìm kiếm", 13, Font.BOLD, TEXT_PRIMARY));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        searchField = createTextField();
        sidebar.add(searchField);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        sidebar.add(createLabel("Thể loại", 13, Font.BOLD, TEXT_PRIMARY));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        categoryComboBox = createComboBox();
        sidebar.add(categoryComboBox);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        topBorrowedButton = createButton("Top sách mượn nhiều", BG_MAIN, TEXT_PRIMARY, BG_HOVER);
        topBorrowedButton.setHorizontalAlignment(SwingConstants.LEFT);
        topBorrowedButton.addActionListener(e -> {
            if (bookManagerController != null) bookManagerController.handleToggleTopBorrowed();
        });
        sidebar.add(topBorrowedButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));

        totalBooksLabel = new JLabel("Tổng số: 0 sách");
        totalBooksLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalBooksLabel.setForeground(TEXT_SECONDARY);
        totalBooksLabel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true), new EmptyBorder(12, 16, 12, 16)
        ));
        totalBooksLabel.setMaximumSize(new Dimension(270, 44));
        totalBooksLabel.setOpaque(true);
        totalBooksLabel.setBackground(PRIMARY_LIGHT);
        totalBooksLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(totalBooksLabel);

        sidebar.add(Box.createVerticalGlue());

        JButton backBtn = createButton("Quay lại trang chủ", BG_MAIN, TEXT_SECONDARY, BG_HOVER);
        backBtn.setHorizontalAlignment(SwingConstants.LEFT);
        backBtn.addActionListener(e -> goHome());
        sidebar.add(backBtn);

        return sidebar;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG_MAIN);
        content.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_MAIN);
        header.setBorder(new EmptyBorder(0, 0, 30, 0));
        header.add(createLabel("Danh mục sách", 32, Font.BOLD, TEXT_PRIMARY));
        header.add(createLabel("Khám phá kho tàng tri thức", 14, Font.PLAIN, TEXT_SECONDARY));
        content.add(header, BorderLayout.NORTH);

        booksContainer = new JPanel();
        booksContainer.setLayout(new BoxLayout(booksContainer, BoxLayout.Y_AXIS));
        booksContainer.setBackground(BG_MAIN);

        JScrollPane scroll = new JScrollPane(booksContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        content.add(scroll, BorderLayout.CENTER);
        return content;
    }

    private JLabel createLabel(String text, int size, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createButton(String text, Color bg, Color fg, Color hoverBg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(14, 20, 14, 20));
        btn.setMaximumSize(new Dimension(270, 48));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverBg); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(BG_MAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_DEFAULT, 1, true), new EmptyBorder(10, 12, 10, 12)
        ));
        field.setMaximumSize(new Dimension(270, 42));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (bookManagerController != null) {
                    bookManagerController.handleSearchTextChanged(field.getText().trim());
                }
            }
        });

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY, 2, true), new EmptyBorder(10, 12, 10, 12)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_DEFAULT, 1, true), new EmptyBorder(10, 12, 10, 12)
                ));
            }
        });
        return field;
    }

    private JComboBox<String> createComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(BG_MAIN);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(new EmptyBorder(8, 8, 8, 8));
        combo.setMaximumSize(new Dimension(270, 42));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.addActionListener(e -> {
            if (bookManagerController != null) {
                bookManagerController.handleCategoryChanged((String) combo.getSelectedItem());
            }
        });
        return combo;
    }

    private JSeparator createDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_LIGHT);
        sep.setMaximumSize(new Dimension(270, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    public void displayBooks(List<BookDisplayModel> books) {
        booksContainer.removeAll();
        if (books.isEmpty()) {
            booksContainer.add(createEmptyState());
        } else {
            for (BookDisplayModel book : books) {
                booksContainer.add(createBookCard(book));
                booksContainer.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }
        booksContainer.revalidate();
        booksContainer.repaint();
    }

    private JPanel createEmptyState() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(100, 0, 0, 0));

        JLabel icon = new JLabel("📭");
        icon.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel text = createLabel("Không tìm thấy sách phù hợp", 18, Font.BOLD, TEXT_SECONDARY);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = createLabel("Hãy thử thay đổi bộ lọc hoặc từ khóa tìm kiếm", 13, Font.PLAIN, TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(icon);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        panel.add(text);
        panel.add(sub);
        return panel;
    }

    private JPanel createBookCard(BookDisplayModel book) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_LIGHT, 1, true), new EmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(BG_CARD);

        JLabel title = createLabel(book.getTitle(), 17, Font.BOLD, TEXT_PRIMARY);
        JLabel author = createLabel("Tác giả: " + book.getAuthor(), 13, Font.PLAIN, TEXT_SECONDARY);
        JLabel category = createLabel("Thuộc loại: " + book.getCategory() + " || Năm: " + book.getYear(), 13, Font.PLAIN, TEXT_MUTED);
        String loca = book.getLocation() == null ? "Không xác định":book.getLocation();
        JLabel location = createLabel("Vị trí: " + loca, 13, Font.PLAIN, TEXT_MUTED);

        JLabel borrowBadge = new JLabel("" + book.getBorrowCount() + " lượt mượn ");
        borrowBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        borrowBadge.setForeground(WARNING);
        borrowBadge.setOpaque(true);
        borrowBadge.setBackground(new Color(255, 247, 237));
        borrowBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        borrowBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(title);
        info.add(Box.createRigidArea(new Dimension(0, 7)));
        info.add(author);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(category);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(location);
        info.add(Box.createRigidArea(new Dimension(0, 6)));
        info.add(borrowBadge);

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBackground(BG_CARD);
        pricePanel.add(createLabel(String.format("%,d đ", book.getCost()), 20, Font.BOLD, PRIMARY));
        pricePanel.add(createLabel("Giá trị", 11, Font.PLAIN, TEXT_MUTED));

        card.add(info, BorderLayout.CENTER);
        card.add(pricePanel, BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(PRIMARY_LIGHT);
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(PRIMARY, 1, true), new EmptyBorder(20, 25, 20, 25)
                ));
                updateBg(card, PRIMARY_LIGHT);
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(BG_CARD);
                card.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_LIGHT, 1, true), new EmptyBorder(20, 25, 20, 25)
                ));
                updateBg(card, BG_CARD);
            }
            public void mouseClicked(MouseEvent e) {
                if (bookManagerController != null) {
                    bookManagerController.handleBookClicked(book.getBookId());
                }
            }
        });

        return card;
    }

    private void updateBg(Container c, Color color) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(color);
                if (comp instanceof Container) updateBg((Container) comp, color);
            }
        }
    }

    public void updateTotalBooksLabel(int count) {
        totalBooksLabel.setText("Tổng số đầu sách: " + count + " sách");
    }

    public void updateTopBorrowedButton(boolean isActive) {
        if (isActive) {
            topBorrowedButton.setBackground(PRIMARY);
            topBorrowedButton.setForeground(Color.WHITE);
            topBorrowedButton.setText("Đang xem Top mượn");
        } else {
            topBorrowedButton.setBackground(BG_MAIN);
            topBorrowedButton.setForeground(TEXT_PRIMARY);
            topBorrowedButton.setText("Top sách mượn nhiều");
        }
    }

    public void updateCategoryComboBox(String[] categories, String selectedCategory) {
        categoryComboBox.removeAllItems();
        for (String cat : categories) categoryComboBox.addItem(cat);
        if (selectedCategory != null) categoryComboBox.setSelectedItem(selectedCategory);
    }

    private void goHome() {
        new HomeView().setVisible(true);
        dispose();
    }

    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        protected void configureScrollBarColors() {
            this.thumbColor = BORDER_DEFAULT;
            this.trackColor = BG_MAIN;
        }

        protected JButton createDecreaseButton(int o) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }

        protected JButton createIncreaseButton(int o) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            return btn;
        }

        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 2, r.y, r.width - 4, r.height, 6, 6);
            g2.dispose();
        }
    }

}