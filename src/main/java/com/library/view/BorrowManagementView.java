package com.library.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * VIEW: Quản lý Phiếu mượn & Phạt (UI only)
 * - Không gọi DAO
 * - Không tính phạt
 * - Không xử lý nghiệp vụ
 */
public class BorrowManagementView extends JFrame {

    public static final Color PRIMARY = new Color(25, 118, 210);
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color DANGER  = new Color(220, 48, 35);
    public static final Color BORDER  = new Color(180, 180, 180);

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    private JTextField txtSearchReader, txtPenaltyRate;
    private JTable tblBorrowSlips;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JComboBox<String> cmbStatus, cmbMonth, cmbYear;

    private JButton btnViewDetails, btnReturn, btnPayPenalty;
    private JButton btnAutoGenerate, btnCreateSlip, btnSendEmail;
    private JButton btnUpdateRate, btnSearch, btnClear, btnRefresh, btnClose;

    private JLabel lblTotalSlips, lblActiveSlips, lblExpiredSlips, lblReturnedSlips, lblTotalPenalty, lblUnpaidPenalty;

    public BorrowManagementView() {
        initFrame();
        createUI();
    }

    private void initFrame() {
        setTitle("Quản lý Phiếu mượn & Phạt - MVC");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(1400, 800));
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(15, 20, 15, 20));

        main.add(createTopPanel(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(createStatsPanel(), BorderLayout.NORTH);
        center.add(createSearchAndTablePanel(), BorderLayout.CENTER);
        main.add(center, BorderLayout.CENTER);

        main.add(createBottomPanel(), BorderLayout.SOUTH);

        add(main);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("📋 Quản lý Phiếu mượn & Phạt");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        panel.add(lblTitle, BorderLayout.WEST);

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JLabel lblRate = new JLabel("Mức phạt:");
        lblRate.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtPenaltyRate = new JTextField("5,000", 12);
        txtPenaltyRate.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblCurrency = new JLabel("VNĐ/ngày");
        lblCurrency.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnUpdateRate = createStyledButton("Cập nhật", WARNING, 100, 30);

        settingsPanel.add(lblRate);
        settingsPanel.add(txtPenaltyRate);
        settingsPanel.add(lblCurrency);
        settingsPanel.add(btnUpdateRate);

        panel.add(settingsPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 6, 15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        lblTotalSlips    = addStatCard(panel, "📊 Tổng phiếu", "0", PRIMARY);
        lblActiveSlips   = addStatCard(panel, "✅ Đang mượn", "0", SUCCESS);
        lblExpiredSlips  = addStatCard(panel, "⚠️ Quá hạn", "0", DANGER);
        lblReturnedSlips = addStatCard(panel, "📦 Đã trả", "0", new Color(96, 125, 139));
        lblTotalPenalty  = addStatCard(panel, "💰 Tổng phạt", "0 VNĐ", WARNING);
        lblUnpaidPenalty = addStatCard(panel, "❌ Chưa TT", "0 VNĐ", DANGER);

        lblTotalPenalty.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUnpaidPenalty.setFont(new Font("Segoe UI", Font.BOLD, 16));

        return panel;
    }

    private JPanel createSearchAndTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                "Bộ lọc tìm kiếm",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)
        ));

        JLabel lblSearchReader = new JLabel("Tên độc giả:");
        lblSearchReader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtSearchReader = new JTextField(20);
        txtSearchReader.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblMonth = new JLabel("Tháng:");
        lblMonth.setFont(new Font("Segoe UI", Font.BOLD, 13));
        String[] months = {"Tất cả", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        cmbMonth = new JComboBox<>(months);
        cmbMonth.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMonth.setPreferredSize(new Dimension(90, 30));

        JLabel lblYear = new JLabel("Năm:");
        lblYear.setFont(new Font("Segoe UI", Font.BOLD, 13));
        // controller sẽ set model year cho phù hợp (view chỉ giữ combobox)
        cmbYear = new JComboBox<>(new String[]{"Tất cả"});
        cmbYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbYear.setPreferredSize(new Dimension(90, 30));

        JLabel lblStatus = new JLabel("Trạng thái:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbStatus = new JComboBox<>(new String[]{
                "Tất cả", "Đang mượn", "Quá hạn", "Đã trả - Đúng hạn",
                "Đã trả - Trễ (Đã TT)", "Đã trả - Trễ (Chưa TT)"
        });
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbStatus.setPreferredSize(new Dimension(180, 30));

        btnSearch = createStyledButton("🔍 Tìm kiếm", PRIMARY, 120, 30);
        btnClear  = createStyledButton("✖ Xóa lọc", new Color(150, 150, 150), 100, 30);

        filterPanel.add(lblSearchReader);
        filterPanel.add(txtSearchReader);
        filterPanel.add(lblMonth);
        filterPanel.add(cmbMonth);
        filterPanel.add(lblYear);
        filterPanel.add(cmbYear);
        filterPanel.add(lblStatus);
        filterPanel.add(cmbStatus);
        filterPanel.add(btnSearch);
        filterPanel.add(btnClear);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnAutoGenerate = createStyledButton("⚡ Tạo phiếu phạt tự động", WARNING, 200, 35);
        btnCreateSlip   = createStyledButton("+ Tạo phiếu mượn", SUCCESS, 160, 35);
        btnSendEmail    = createStyledButton("📧 Gửi Email", new Color(33, 150, 243), 150, 35);

        actionPanel.add(btnAutoGenerate);
        actionPanel.add(btnCreateSlip);
        actionPanel.add(btnSendEmail);

        mainPanel.add(actionPanel, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        createTable(tablePanel);

        JLabel lblInfo = new JLabel("💡 Nhấp đúp vào dòng để xem chi tiết phiếu mượn");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        tablePanel.add(lblInfo, BorderLayout.SOUTH);

        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void createTable(JPanel parent) {
        String[] cols = {"ID", "Mã ĐG", "Tên độc giả", "SL", "Ngày mượn", "Hạn trả", "Ngày trả", "Trễ", "Tiền phạt", "TT", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblBorrowSlips = new JTable(tableModel);
        tblBorrowSlips.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblBorrowSlips.setRowHeight(40);
        tblBorrowSlips.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblBorrowSlips.getTableHeader().setBackground(new Color(240, 240, 240));
        tblBorrowSlips.setSelectionBackground(new Color(200, 230, 255));
        tblBorrowSlips.setGridColor(new Color(220, 220, 220));

        int[] widths = {60, 80, 180, 50, 100, 100, 100, 60, 110, 90, 180};
        for (int i = 0; i < widths.length; i++) {
            tblBorrowSlips.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Penalty renderer
        tblBorrowSlips.getColumnModel().getColumn(8).setCellRenderer((t, v, sel, foc, r, c) -> {
            JLabel l = new JLabel(String.valueOf(v), SwingConstants.RIGHT);
            l.setOpaque(true);
            l.setFont(new Font("Segoe UI", Font.BOLD, 12));
            l.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
            l.setBackground(sel ? t.getSelectionBackground() : Color.WHITE);
            String s = String.valueOf(v);
            l.setForeground(sel ? t.getSelectionForeground() : (s.equals("0") || s.equals("0.0") ? Color.BLACK : DANGER));
            return l;
        });

        // Payment status renderer
        tblBorrowSlips.getColumnModel().getColumn(9).setCellRenderer((t, v, sel, foc, r, c) -> {
            JLabel l = new JLabel(String.valueOf(v), SwingConstants.CENTER);
            l.setOpaque(true);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            if (sel) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                String s = String.valueOf(v);
                if (s.equals("✅ Đã TT")) {
                    l.setBackground(new Color(200, 250, 205));
                    l.setForeground(new Color(27, 94, 32));
                } else if (s.equals("❌ Chưa TT")) {
                    l.setBackground(new Color(255, 205, 210));
                    l.setForeground(new Color(183, 28, 28));
                } else {
                    l.setBackground(Color.WHITE);
                    l.setForeground(Color.DARK_GRAY);
                }
            }
            return l;
        });

        // Status renderer
        tblBorrowSlips.getColumnModel().getColumn(10).setCellRenderer((t, v, sel, foc, r, c) -> {
            JLabel l = new JLabel(String.valueOf(v), SwingConstants.CENTER);
            l.setOpaque(true);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            if (sel) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                String s = String.valueOf(v);
                if (s.contains("Đang mượn")) {
                    l.setBackground(new Color(200, 250, 205));
                    l.setForeground(new Color(27, 94, 32));
                } else if (s.contains("Quá hạn")) {
                    l.setBackground(new Color(255, 205, 210));
                    l.setForeground(new Color(183, 28, 28));
                } else {
                    l.setBackground(new Color(224, 224, 224));
                    l.setForeground(new Color(66, 66, 66));
                }
            }
            return l;
        });

        sorter = new TableRowSorter<>(tableModel);
        tblBorrowSlips.setRowSorter(sorter);

        JScrollPane sp = new JScrollPane(tblBorrowSlips);
        sp.setPreferredSize(new Dimension(0, 400));
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        parent.add(sp, BorderLayout.CENTER);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        btnViewDetails = createStyledButton("👁️ Xem chi tiết", PRIMARY, 140, 40);
        btnReturn      = createStyledButton("📥 Trả sách", SUCCESS, 130, 40);
        btnPayPenalty  = createStyledButton("💳 Thanh toán", WARNING, 140, 40);
        btnRefresh     = createStyledButton("🔄 Làm mới", new Color(96, 125, 139), 120, 40);
        btnClose       = createStyledButton("Đóng", new Color(200, 200, 200), 100, 40);

        btnViewDetails.setEnabled(false);
        btnReturn.setEnabled(false);
        btnPayPenalty.setEnabled(false);

        panel.add(btnViewDetails);
        panel.add(btnReturn);
        panel.add(btnPayPenalty);
        panel.add(btnRefresh);
        panel.add(btnClose);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return btn;
    }

    private JLabel addStatCard(JPanel parent, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.WHITE);
        card.add(lblTitle, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(Color.WHITE);
        card.add(lblValue, BorderLayout.CENTER);

        parent.add(card);
        return lblValue;
    }

    // ========================= API cho Controller =========================

    public void setPenaltyRateText(String text) { txtPenaltyRate.setText(text); }
    public String getPenaltyRateText() { return txtPenaltyRate.getText(); }

    public void setYearOptions(String[] years, int selectedIndex) {
        cmbYear.setModel(new DefaultComboBoxModel<>(years));
        cmbYear.setSelectedIndex(Math.max(0, Math.min(selectedIndex, years.length - 1)));
    }

    public String getSearchReader() { return txtSearchReader.getText(); }
    public String getSelectedMonth() { return String.valueOf(cmbMonth.getSelectedItem()); }
    public String getSelectedYear() { return String.valueOf(cmbYear.getSelectedItem()); }
    public String getSelectedStatus() { return String.valueOf(cmbStatus.getSelectedItem()); }

    public JTable getTable() { return tblBorrowSlips; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public TableRowSorter<DefaultTableModel> getSorter() { return sorter; }

    public JButton getBtnUpdateRate() { return btnUpdateRate; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnAutoGenerate() { return btnAutoGenerate; }
    public JButton getBtnCreateSlip() { return btnCreateSlip; }
    public JButton getBtnSendEmail() { return btnSendEmail; }

    public JButton getBtnViewDetails() { return btnViewDetails; }
    public JButton getBtnReturn() { return btnReturn; }
    public JButton getBtnPayPenalty() { return btnPayPenalty; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnClose() { return btnClose; }

    public void setActionButtonsEnabled(boolean viewDetails, boolean canReturn, boolean canPay) {
        btnViewDetails.setEnabled(viewDetails);
        btnReturn.setEnabled(canReturn);
        btnPayPenalty.setEnabled(canPay);
    }

    public void setStats(int total, int active, int expired, int returned, double totalPenalty, double unpaidPenalty) {
        lblTotalSlips.setText(String.valueOf(total));
        lblActiveSlips.setText(String.valueOf(active));
        lblExpiredSlips.setText(String.valueOf(expired));
        lblReturnedSlips.setText(String.valueOf(returned));
        lblTotalPenalty.setText(moneyFormat.format(totalPenalty) + " VNĐ");
        lblUnpaidPenalty.setText(moneyFormat.format(unpaidPenalty) + " VNĐ");
    }

    public void setTableRows(List<Object[]> rows) {
        tableModel.setRowCount(0);
        for (Object[] r : rows) tableModel.addRow(r);
    }

    public void clearFilters() {
        txtSearchReader.setText("");
        cmbMonth.setSelectedIndex(0);
        cmbYear.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    public void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public int askConfirm(String msg, String title) {
        return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
    }
}
