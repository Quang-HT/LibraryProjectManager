package com.library.view;

import com.library.model.Author_Entity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorListView extends JFrame {

    // Color Scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    // Components
    private JTextField txtSearch;
    private JTable tblAuthors;
    private DefaultTableModel tableModel;
    private JButton btnAddAuthor;
    private JButton btnRefresh;
    private JButton btnSearch;
    private JButton btnClose;

    // Data currently displayed in table (để map selection -> entity chính xác khi lọc)
    private List<Author_Entity> displayedAuthors = new ArrayList<>();

    public AuthorListView() {
        initializeFrame();
        createComponents();
    }

    // ============================== FRAME SETUP ==============================
    private void initializeFrame() {
        setTitle("Danh sách Tác giả");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Danh sách Tác giả");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(30, 15, 300, 40);
        mainPanel.add(titleLabel);

        createSearchSection(mainPanel);
        createTableSection(mainPanel);
        createButtonsSection(mainPanel);

        add(mainPanel);
    }

    // ============================== SEARCH SECTION ==============================
    private void createSearchSection(JPanel parent) {
        int sectionY = 70;

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(TEXT_COLOR);
        lblSearch.setBounds(30, sectionY, 80, 30);
        parent.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBounds(120, sectionY, 400, 35);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        parent.add(txtSearch);

        btnSearch = new JButton("🔍");
        btnSearch.setBounds(530, sectionY, 50, 35);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSearch);

        btnAddAuthor = new JButton("+ Tạo mới Tác giả");
        btnAddAuthor.setBounds(650, sectionY, 180, 35);
        btnAddAuthor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddAuthor.setBackground(SUCCESS_COLOR);
        btnAddAuthor.setForeground(Color.WHITE);
        btnAddAuthor.setFocusPainted(false);
        btnAddAuthor.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnAddAuthor);
    }

    // ============================== TABLE SECTION ==============================
    private void createTableSection(JPanel parent) {
        int tableY = 130;
        int tableHeight = 400;

        String[] columnNames = {"ID", "Tên Tác giả", "Ngày sinh", "Mô tả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblAuthors = new JTable(tableModel);
        tblAuthors.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblAuthors.setRowHeight(35);
        tblAuthors.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblAuthors.getTableHeader().setBackground(new Color(240, 240, 240));
        tblAuthors.getTableHeader().setForeground(TEXT_COLOR);
        tblAuthors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAuthors.setSelectionBackground(new Color(200, 230, 255));
        tblAuthors.setGridColor(BORDER_COLOR);

        tblAuthors.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblAuthors.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblAuthors.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblAuthors.getColumnModel().getColumn(3).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(tblAuthors);
        scrollPane.setBounds(30, tableY, 800, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel("💡 Nhấp đúp vào tác giả để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(30, tableY + tableHeight + 5, 400, 25);
        parent.add(lblInfo);
    }

    // ============================== BUTTONS SECTION ==============================
    private void createButtonsSection(JPanel parent) {
        int buttonY = 560;

        btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.setBounds(550, buttonY, 130, 40);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(96, 125, 139));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnRefresh);

        btnClose = new JButton("Đóng");
        btnClose.setBounds(700, buttonY, 130, 40);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(200, 200, 200));
        btnClose.setForeground(TEXT_COLOR);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnClose);
    }

    /* ============================== VIEW API ============================== */

    public JButton getBtnAddAuthor() { return btnAddAuthor; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnClose() { return btnClose; }

    public JTextField getTxtSearch() { return txtSearch; }
    public JTable getTblAuthors() { return tblAuthors; }

    public void clearSearch() {
        txtSearch.setText("");
    }

    // đổ dữ liệu lên bảng + lưu danh sách đang hiển thị
    public void setAuthors(List<Author_Entity> authors) {
        displayedAuthors = (authors != null) ? authors : new ArrayList<>();
        tableModel.setRowCount(0);

        for (Author_Entity a : displayedAuthors) {
            Object[] row = {
                    a.getAuthor_id(),
                    a.getAuthor_name(),
                    a.getBirth_date(),
                    a.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    // Lấy authorId đang chọn từ bảng
    public Integer getSelectedAuthorIdOrNull() {
        int selectedRow = tblAuthors.getSelectedRow();
        if (selectedRow < 0) return null;

        Object val = tableModel.getValueAt(selectedRow, 0);
        if (val == null) return null;

        if (val instanceof Integer) return (Integer) val;
        return Integer.parseInt(val.toString());
    }

    // Lấy entity đang chọn dựa trên row đang hiển thị (an toàn khi đã lọc)
    public Author_Entity getSelectedAuthorEntityOrNull() {
        int selectedRow = tblAuthors.getSelectedRow();
        if (selectedRow < 0) return null;
        if (selectedRow >= displayedAuthors.size()) return null;
        return displayedAuthors.get(selectedRow);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void closeWindow() {
        dispose();
    }
}
