package com.library.view;

import com.library.model.Account_Entity;
import com.library.model.Staff_Entity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * VIEW: Quản lý Tài khoản (UI only)
 * - Không gọi DAO
 * - Không xử lý nghiệp vụ
 */
public class AccountManagementView extends JFrame {

    // Color Scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(220, 48, 35);

    // Components
    private JTextField txtSearch;
    private JTable tblAccounts;
    private DefaultTableModel tableModel;
    private JButton btnAddAccount;
    private JButton btnRefresh;
    private JButton btnDelete;
    private JButton btnSearch;
    private JButton btnClose;
    private TableRowSorter<DefaultTableModel> sorter;

    public AccountManagementView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Quản lý Tài khoản");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
    }

    private void createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Title
        JLabel titleLabel = new JLabel("Quản lý Tài khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(30, 15, 350, 40);
        mainPanel.add(titleLabel);

        createSearchSection(mainPanel);
        createTableSection(mainPanel);
        createButtonsSection(mainPanel);

        add(mainPanel);
    }

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

        btnSearch = new JButton("Tìm");
        btnSearch.setBounds(530, sectionY, 50, 35);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSearch);

        btnAddAccount = new JButton("+ Tạo Tài khoản mới");
        btnAddAccount.setBounds(850, sectionY, 190, 35);
        btnAddAccount.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddAccount.setBackground(SUCCESS_COLOR);
        btnAddAccount.setForeground(Color.WHITE);
        btnAddAccount.setFocusPainted(false);
        btnAddAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnAddAccount);
    }

    private void createTableSection(JPanel parent) {
        int tableY = 130;
        int tableHeight = 400;

        String[] columnNames = {"ID", "Username", "Tên nhân viên", "Email", "Chức vụ", "Trạng thái TK", "Trạng thái NV"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblAccounts = new JTable(tableModel);
        tblAccounts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblAccounts.setRowHeight(35);
        tblAccounts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblAccounts.getTableHeader().setBackground(new Color(240, 240, 240));
        tblAccounts.getTableHeader().setForeground(TEXT_COLOR);
        tblAccounts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAccounts.setSelectionBackground(new Color(200, 230, 255));
        tblAccounts.setGridColor(BORDER_COLOR);

        tblAccounts.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblAccounts.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblAccounts.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblAccounts.getColumnModel().getColumn(3).setPreferredWidth(180);
        tblAccounts.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblAccounts.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblAccounts.getColumnModel().getColumn(6).setPreferredWidth(100);

        sorter = new TableRowSorter<>(tableModel);
        tblAccounts.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tblAccounts);
        scrollPane.setBounds(30, tableY, 1000, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel("Nhấp đúp vào tài khoản để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(30, tableY + tableHeight + 5, 400, 25);
        parent.add(lblInfo);
    }

    private void createButtonsSection(JPanel parent) {
        int buttonY = 560;

        btnDelete = new JButton("Xóa TK");
        btnDelete.setBounds(600, buttonY, 130, 40);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setBackground(DANGER_COLOR);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setEnabled(false);
        parent.add(btnDelete);

        btnRefresh = new JButton("Làm mới");
        btnRefresh.setBounds(750, buttonY, 130, 40);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(96, 125, 139));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnRefresh);

        btnClose = new JButton("Đóng");
        btnClose.setBounds(900, buttonY, 130, 40);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setBackground(new Color(200, 200, 200));
        btnClose.setForeground(TEXT_COLOR);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnClose);
    }

    // ===================== VIEW HELPERS =====================

    public void setDeleteEnabled(boolean enabled) {
        btnDelete.setEnabled(enabled);
    }

    public void clearSearch() {
        txtSearch.setText("");
        sorter.setRowFilter(null);
    }

    public void applySearchFilter(String searchText) {
        String st = (searchText == null) ? "" : searchText.trim();
        if (st.isEmpty()) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + st));
    }

    /**
     * Đổ dữ liệu ra bảng.
     * staffByAccountId: Map<AccountId, Staff_Entity> đã được controller chuẩn bị sẵn (để tránh view gọi DAO).
     */
    public void renderAccounts(List<Account_Entity> accounts, Map<Integer, Staff_Entity> staffByAccountId) {
        tableModel.setRowCount(0);

        if (accounts == null) return;

        for (Account_Entity account : accounts) {
            Staff_Entity staff = (staffByAccountId == null) ? null : staffByAccountId.get(account.getAccount_id());
            if (staff == null) continue;

            String accountStatus = account.getIs_active() == 1 ? "Hoạt động" : "Khóa";
            String staffStatus = staff.getIs_working() == 1 ? "Đang làm" : "Nghỉ việc";

            Object[] row = {
                    account.getAccount_id(),
                    account.getUsername(),
                    staff.getStaff_name(),
                    staff.getEmail() != null ? staff.getEmail() : "N/A",
                    staff.getPosition() != null ? staff.getPosition() : "N/A",
                    accountStatus,
                    staffStatus
            };
            tableModel.addRow(row);
        }
    }

    // ===================== GETTERS =====================

    public JTextField getTxtSearch() { return txtSearch; }
    public JTable getTblAccounts() { return tblAccounts; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public TableRowSorter<DefaultTableModel> getSorter() { return sorter; }

    public JButton getBtnAddAccount() { return btnAddAccount; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnClose() { return btnClose; }
}
