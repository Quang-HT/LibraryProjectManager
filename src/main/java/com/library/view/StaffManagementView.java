package com.library.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class StaffManagementView extends JFrame {

    // Color Scheme
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(220, 48, 35);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);

    // Components
    private JTextField txtSearch;
    private JTable tblStaff;
    private DefaultTableModel tableModel;
    private JButton btnAddStaff;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnRefresh;
    private JButton btnClose;
    private JButton btnSearch;

    private TableRowSorter<DefaultTableModel> sorter;

    public StaffManagementView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Quản lý Nhân viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
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
        JLabel titleLabel = new JLabel("👥 Quản lý Nhân viên");
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

        btnSearch = new JButton("🔍");
        btnSearch.setBounds(530, sectionY, 50, 35);
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btnSearch.setBackground(PRIMARY_COLOR);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnSearch);

        btnAddStaff = new JButton("+ Thêm Nhân viên");
        btnAddStaff.setBounds(850, sectionY, 190, 35);
        btnAddStaff.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddStaff.setBackground(SUCCESS_COLOR);
        btnAddStaff.setForeground(Color.WHITE);
        btnAddStaff.setFocusPainted(false);
        btnAddStaff.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnAddStaff);
    }

    private void createTableSection(JPanel parent) {
        int tableY = 130;
        int tableHeight = 400;

        String[] columnNames = {"ID", "Tên nhân viên", "Chức vụ", "Email", "Số điện thoại", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblStaff = new JTable(tableModel);
        tblStaff.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblStaff.setRowHeight(35);
        tblStaff.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblStaff.getTableHeader().setBackground(new Color(240, 240, 240));
        tblStaff.getTableHeader().setForeground(TEXT_COLOR);
        tblStaff.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblStaff.setSelectionBackground(new Color(200, 230, 255));
        tblStaff.setGridColor(BORDER_COLOR);

        tblStaff.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblStaff.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblStaff.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblStaff.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblStaff.getColumnModel().getColumn(4).setPreferredWidth(130);
        tblStaff.getColumnModel().getColumn(5).setPreferredWidth(100);

        sorter = new TableRowSorter<>(tableModel);
        tblStaff.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(tblStaff);
        scrollPane.setBounds(30, tableY, 1000, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel("💡 Nhấp đúp vào nhân viên để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(30, tableY + tableHeight + 5, 400, 25);
        parent.add(lblInfo);
    }

    private void createButtonsSection(JPanel parent) {
        int buttonY = 560;

        btnEdit = new JButton("✏️ Chỉnh sửa");
        btnEdit.setBounds(470, buttonY, 130, 40);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEdit.setBackground(WARNING_COLOR);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setEnabled(false);
        parent.add(btnEdit);

        btnDelete = new JButton("🗑️ Xóa");
        btnDelete.setBounds(610, buttonY, 130, 40);
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setBackground(DANGER_COLOR);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setEnabled(false);
        parent.add(btnDelete);

        btnRefresh = new JButton("🔄 Làm mới");
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

    // ===== Getters cho Controller =====
    public JTextField getTxtSearch() { return txtSearch; }
    public JTable getTblStaff() { return tblStaff; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public TableRowSorter<DefaultTableModel> getSorter() { return sorter; }

    public JButton getBtnAddStaff() { return btnAddStaff; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnClose() { return btnClose; }
    public JButton getBtnSearch() { return btnSearch; }
}
