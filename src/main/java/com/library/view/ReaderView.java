package com.library.view;

import com.library.model.display.BookDisplayModel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;


public class ReaderView extends JFrame {

    private static final Color PRIMARY_BG = new Color(240, 242, 245);
    private static final Color SECONDARY_BG = Color.WHITE;
    private static final Color SIDEBAR_BG = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(66, 133, 244);
    private static final Color TEXT_PRIMARY = new Color(32, 33, 36);
    private static final Color TEXT_SECONDARY = new Color(95, 99, 104);
    private static final Color BORDER_COLOR = new Color(218, 220, 224);
    private static final Color CARD_HOVER = new Color(248, 249, 250);
    private static final Color SUCCESS_COLOR = new Color(52, 168, 83);

    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private JPanel booksGridPanel;
    private JScrollPane booksScrollPane;

    @Getter
    private JTextField searchField;
    @Getter
    private JComboBox<String> categoryComboBox;
    @Getter
    private JComboBox<String> authorComboBox;
    @Getter
    private JButton topBorrowedButton;
    @Getter
    private JButton resetButton;
    @Getter
    private JButton exitButton;
    private JLabel resultCountLabel;

    private int columns = 3;

    private BookDisplayModel bookDisplayModel;

    public ReaderView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Thư viện - Giao diện độc giả");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 750);
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
        createTopBar();
        createSidebar();
        createContentArea();
    }

    private void createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(SECONDARY_BG);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                new EmptyBorder(15, 30, 15, 30)
        ));

        JLabel titleLabel = new JLabel("📚 Thư viện sách");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(SECONDARY_BG);
        searchPanel.setPreferredSize(new Dimension(500, 40));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(5, 15, 5, 15)
        ));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(null);
        searchField.setBackground(SECONDARY_BG);

        searchPanel.add(new JLabel("🔍"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(searchPanel, BorderLayout.CENTER);

        exitButton = new JButton("Thoát");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitButton.setBackground(new Color(230, 57, 70));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(SECONDARY_BG);
        rightPanel.add(exitButton);
        topBar.add(rightPanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(300, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel filterLabel = new JLabel("Bộ lọc");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        filterLabel.setForeground(TEXT_PRIMARY);
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterLabel.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(filterLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        categoryComboBox = new JComboBox<>(new String[]{"Tất cả"});
        authorComboBox = new JComboBox<>(new String[]{"Tất cả"});

        addFilterSection("Thể loại", categoryComboBox);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        addFilterSection("Tác giả", authorComboBox);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        addSeparator();
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        topBorrowedButton = new JButton("⭐ Top được mượn nhiều");
        topBorrowedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topBorrowedButton.setBackground(new Color(255, 193, 7));
        topBorrowedButton.setForeground(Color.WHITE);
        topBorrowedButton.setMaximumSize(new Dimension(250, 45));
        topBorrowedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        topBorrowedButton.setFocusPainted(false);
        topBorrowedButton.setBorderPainted(false);
        topBorrowedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(SIDEBAR_BG);
        btnPanel.setMaximumSize(new Dimension(300, 60));
        btnPanel.add(topBorrowedButton);
        sidebarPanel.add(btnPanel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        resetButton = new JButton("🔄 Đặt lại bộ lọc");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetButton.setBackground(SECONDARY_BG);
        resetButton.setForeground(TEXT_PRIMARY);
        resetButton.setMaximumSize(new Dimension(250, 40));
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetPanel.setBackground(SIDEBAR_BG);
        resetPanel.setMaximumSize(new Dimension(300, 55));
        resetPanel.add(resetButton);
        sidebarPanel.add(resetPanel);

        sidebarPanel.add(Box.createVerticalGlue());

        resultCountLabel = new JLabel("Hiển thị: 0 sách");
        resultCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        resultCountLabel.setForeground(ACCENT_COLOR);
        resultCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebarPanel.add(resultCountLabel);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        add(sidebarPanel, BorderLayout.WEST);

        topBorrowedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                topBorrowedButton.setBackground(new Color(255, 179, 0));
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    private void addFilterSection(String title, JComboBox<String> comboBox) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 25, 0, 25));
        sidebarPanel.add(label);

        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setPreferredSize(new Dimension(250, 35));
        comboBox.setBackground(SECONDARY_BG);

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 0));
        comboPanel.setBackground(SIDEBAR_BG);
        comboPanel.setMaximumSize(new Dimension(300, 40));
        comboPanel.add(comboBox);
        sidebarPanel.add(comboPanel);
    }

    private void addSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(250, 1));
        sep.setForeground(BORDER_COLOR);

        JPanel sepPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sepPanel.setBackground(SIDEBAR_BG);
        sepPanel.setMaximumSize(new Dimension(300, 10));
        sepPanel.add(sep);
        sidebarPanel.add(sepPanel);
    }

    private void createContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PRIMARY_BG);
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        booksGridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        booksGridPanel.setBackground(PRIMARY_BG);

        booksScrollPane = new JScrollPane(booksGridPanel);
        booksScrollPane.setBorder(null);
        booksScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        booksScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPanel.add(booksScrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void setCategoryOptions(List<String> categories) {
        categoryComboBox.setModel(new DefaultComboBoxModel<>(categories.toArray(new String[0])));
    }

    public void setAuthorOptions(List<String> authors) {
        authorComboBox.setModel(new DefaultComboBoxModel<>(authors.toArray(new String[0])));
    }

    public void setResultCount(int count) {
        resultCountLabel.setText("Hiển thị: " + count + " sách");
    }

    public void setTopBorrowedState(boolean enabled) {
        if (enabled) {
            topBorrowedButton.setBackground(new Color(255, 87, 34));
            topBorrowedButton.setText("Đang xem Top");
        } else {
            topBorrowedButton.setBackground(new Color(255, 193, 7));
            topBorrowedButton.setText("⭐ Top được mượn nhiều");
        }
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void renderBooks(List<BookDisplayModel> books) {
        booksGridPanel.removeAll();
        booksGridPanel.setLayout(new BoxLayout(booksGridPanel, BoxLayout.Y_AXIS));

        if (books == null || books.isEmpty()) {
            JLabel noResultLabel = new JLabel("Không tìm thấy sách phù hợp");
            noResultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
            noResultLabel.setForeground(TEXT_SECONDARY);
            noResultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            booksGridPanel.add(noResultLabel);
        } else {
            JPanel rowPanel = null;
            int count = 0;

            for (BookDisplayModel book : books) {
                if (count % columns == 0) {
                    rowPanel = new JPanel(new GridLayout(1, columns, 20, 20));
                    rowPanel.setBackground(PRIMARY_BG);
                    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
                    booksGridPanel.add(rowPanel);
                }

                JPanel card = createBookCard(book);
                rowPanel.add(card);
                count++;
            }
        }

        setResultCount(books == null ? 0 : books.size());
        booksGridPanel.revalidate();
        booksGridPanel.repaint();
    }


    private JPanel createBookCard(BookDisplayModel book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SECONDARY_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(new EmptyBorder(6, 8, 6, 8));

        JLabel titleLabel = new JLabel("<html><b>" + truncateText(book.getTitle(), 45) + "</b></html>");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        card.add(headerPanel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row1.setBackground(SECONDARY_BG);

        JLabel authorLabel = new JLabel("👤 " + truncateText(book.getAuthor(), 28));
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setForeground(TEXT_SECONDARY);

        JLabel yearLabel = new JLabel(" • " + book.getYear());
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearLabel.setForeground(TEXT_SECONDARY);

        row1.add(authorLabel);
        row1.add(yearLabel);
        card.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        row2.setBackground(SECONDARY_BG);

        JLabel categoryLabel = new JLabel("📚 " + truncateText(book.getCategory(), 22));
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryLabel.setForeground(TEXT_SECONDARY);

        JLabel locationLabel = new JLabel(" • Vị trí: " + truncateText(book.getLocation(), 18));
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        locationLabel.setForeground(TEXT_SECONDARY);

        row2.add(categoryLabel);
        row2.add(locationLabel);
        card.add(row2);

        card.add(Box.createRigidArea(new Dimension(0, 4)));

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        row3.setBackground(SECONDARY_BG);

        String availableText = book.getCopyCount() > 0
                ? "✅ Còn " + book.getCopyCount() + " bản"
                : "❌ Hết sách";

        JLabel availableLabel = new JLabel(availableText);
        availableLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        availableLabel.setForeground(book.getCopyCount() > 0 ? SUCCESS_COLOR : Color.RED);
        row3.add(availableLabel);

        if (book.getBorrowCount() > 0) {
            JLabel borrowLabel = new JLabel("   ⭐ " + book.getBorrowCount() + " lượt mượn");
            borrowLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            borrowLabel.setForeground(new Color(255, 152, 0));
            row3.add(borrowLabel);
        }

        card.add(row3);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(CARD_HOVER);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_COLOR, 2, true),
                        new EmptyBorder(9, 11, 9, 11)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(SECONDARY_BG);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                        new EmptyBorder(10, 12, 10, 12)
                ));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showBookDetail(book);
            }
        });

        return card;
    }

    public void showBookDetail(BookDisplayModel book) {
        JDialog dialog = new JDialog(this, "Chi tiết sách", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(SECONDARY_BG);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        addDetailRow(mainPanel, "Mã sách:", String.valueOf(book.getBookId()));
        addDetailRow(mainPanel, "Tác giả:", book.getAuthor());
        addDetailRow(mainPanel, "Thể loại:", book.getCategory());
        addDetailRow(mainPanel, "Năm xuất bản:", String.valueOf(book.getYear()));
        addDetailRow(mainPanel, "Số bản còn lại:", String.valueOf(book.getCopyCount()));
        addDetailRow(mainPanel, "Vị trí:", book.getLocation());

        if (book.getBorrowCount() > 0) {
            addDetailRow(mainPanel, "Số lượt mượn:", book.getBorrowCount() + " lần");
        }

        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        if (book.getDescription() != null && !book.getDescription().isEmpty()) {
            JLabel descLabel = new JLabel("Mô tả:");
            descLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            descLabel.setForeground(TEXT_PRIMARY);
            descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(descLabel);

            mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

            JTextArea descArea = new JTextArea(book.getDescription());
            descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            descArea.setForeground(TEXT_SECONDARY);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setBackground(SECONDARY_BG);
            descArea.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            descArea.setPreferredSize(new Dimension(520, 100));

            JScrollPane descScroll = new JScrollPane(descArea);
            descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(descScroll);
        }

        mainPanel.add(Box.createVerticalGlue());

        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(BORDER_COLOR);
        closeButton.setForeground(TEXT_PRIMARY);
        closeButton.setPreferredSize(new Dimension(120, 40));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(SECONDARY_BG);
        buttonPanel.add(closeButton);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addDetailRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(SECONDARY_BG);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(TEXT_PRIMARY);
        labelComp.setPreferredSize(new Dimension(150, 25));

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setForeground(TEXT_SECONDARY);

        row.add(labelComp);
        row.add(valueComp);
        parent.add(row);
    }

    private String truncateText(String text, int maxLength) {
        if (text == null || text.isEmpty()) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

}
