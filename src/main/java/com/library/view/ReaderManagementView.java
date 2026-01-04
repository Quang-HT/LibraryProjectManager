package com.library.view;

import com.library.model.display.ReaderRow;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderManagementView extends JFrame {

    private static final Color BG_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);

    @Getter
    private JTextField txtSearch;
    @Getter
    private JTable tblReaders;
    private DefaultTableModel tableModel;
    @Getter
    private JButton btnAddReader;
    @Getter
    private JButton btnRefresh;
    @Getter
    private JButton btnSearch;
    @Getter
    private JButton btnClose;

    private List<ReaderRow> displayedReaders = new ArrayList<>();

    public ReaderManagementView() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Quản lý Độc giả");
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

        JLabel titleLabel = new JLabel("Quản lý Độc giả");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBounds(30, 15, 300, 40);
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

        btnAddReader = new JButton("+ Đăng ký Độc giả mới");
        btnAddReader.setBounds(850, sectionY, 190, 35);
        btnAddReader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddReader.setBackground(SUCCESS_COLOR);
        btnAddReader.setForeground(Color.WHITE);
        btnAddReader.setFocusPainted(false);
        btnAddReader.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parent.add(btnAddReader);
    }

    private void createTableSection(JPanel parent) {
        int tableY = 130;
        int tableHeight = 400;

        String[] columnNames = {
                "ID", "Họ và tên", "CCCD",
                "Số điện thoại", "Email", "Địa chỉ",
                "Ngày đăng ký", "Trạng thái"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblReaders = new JTable(tableModel);
        tblReaders.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblReaders.setRowHeight(35);
        tblReaders.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblReaders.getTableHeader().setBackground(new Color(240, 240, 240));
        tblReaders.getTableHeader().setForeground(TEXT_COLOR);
        tblReaders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReaders.setSelectionBackground(new Color(200, 230, 255));
        tblReaders.setGridColor(BORDER_COLOR);

        tblReaders.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblReaders.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblReaders.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblReaders.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblReaders.getColumnModel().getColumn(4).setPreferredWidth(180);
        tblReaders.getColumnModel().getColumn(5).setPreferredWidth(200);
        tblReaders.getColumnModel().getColumn(6).setPreferredWidth(100);
        tblReaders.getColumnModel().getColumn(7).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(tblReaders);
        scrollPane.setBounds(30, tableY, 1000, tableHeight);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        parent.add(scrollPane);

        JLabel lblInfo = new JLabel("Nhấp đúp vào độc giả để xem chi tiết");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(30, tableY + tableHeight + 5, 400, 25);
        parent.add(lblInfo);
    }

    private void createButtonsSection(JPanel parent) {
        int buttonY = 560;

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

    public void clearSearch() {
        txtSearch.setText("");
    }

    public void setReaders(List<ReaderRow> readers) {
        displayedReaders = (readers != null) ? readers : new ArrayList<>();
        tableModel.setRowCount(0);

        for (ReaderRow r : displayedReaders) {
            Object[] row = {
                    r.getId(),
                    r.getFullName(),
                    r.getCccd(),
                    r.getPhoneNumber(),
                    r.getEmail(),
                    r.getAddress(),
                    r.getRegistrationDate(),
                    r.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    public ReaderRow getSelectedReaderOrNull() {
        int row = tblReaders.getSelectedRow();
        if (row < 0) return null;
        if (row >= displayedReaders.size()) return null;
        return displayedReaders.get(row);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

}