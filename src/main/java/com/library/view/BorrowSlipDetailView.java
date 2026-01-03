package com.library.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * VIEW: Chi tiết phiếu mượn (UI only)
 * - Không gọi DAO
 * - Không xử lý nghiệp vụ
 */
public class BorrowSlipDetailView extends JFrame {

    public static final Color PRIMARY = new Color(25, 118, 210);
    public static final Color DANGER  = new Color(220, 48, 35);
    public static final Color WARNING = new Color(255, 152, 0);

    private final DecimalFormat fmt = new DecimalFormat("#,###");

    private JLabel lblTitle;

    private JLabel valSlipId, valReaderId, valReaderName, valBorrowDate, valDueDate, valReturnDate;
    private JPanel penaltyPanel;
    private JLabel valOverdueDays, valRatePerDay, valTotalPenalty, valPaidStatus;

    private JTable tblBooks;
    private DefaultTableModel bookModel;

    private JButton btnPrint, btnClose;

    public BorrowSlipDetailView() {
        initFrame();
        createUI();
    }

    private void initFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(20, 25, 20, 25));

        lblTitle = new JLabel("Chi tiết Phiếu mượn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        main.add(lblTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel info = createInfoPanel();
        centerPanel.add(info, BorderLayout.NORTH);

        penaltyPanel = createPenaltyPanel();
        centerPanel.add(penaltyPanel, BorderLayout.CENTER);
        penaltyPanel.setVisible(false); // default hide

        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel lblBooks = new JLabel("Danh sách sách mượn:");
        lblBooks.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBooks.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tablePanel.add(lblBooks, BorderLayout.NORTH);

        tblBooks = createBooksTable();
        JScrollPane sp = new JScrollPane(tblBooks);
        sp.setPreferredSize(new Dimension(0, 250));
        tablePanel.add(sp, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.SOUTH);

        main.add(centerPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPrint = createButton("In phiếu", PRIMARY);
        btnClose = createButton("Đóng", new Color(200, 200, 200));
        btnPanel.add(btnPrint);
        btnPanel.add(btnClose);

        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createInfoPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 15, 10));
        p.setBackground(new Color(250, 250, 250));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        valSlipId = addInfoRow(p, "Mã phiếu:", "-", true);
        valReaderId = addInfoRow(p, "Mã độc giả:", "-", false);
        valReaderName = addInfoRow(p, "Tên độc giả:", "-", false);
        valBorrowDate = addInfoRow(p, "Ngày mượn:", "-", false);
        valDueDate = addInfoRow(p, "Hạn trả:", "-", false);
        valReturnDate = addInfoRow(p, "Ngày trả:", "-", false);

        return p;
    }

    private JLabel addInfoRow(JPanel p, String label, String value, boolean highlight) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", highlight ? Font.BOLD : Font.PLAIN, highlight ? 16 : 14));
        if (highlight) lblValue.setForeground(PRIMARY);

        p.add(lblLabel);
        p.add(lblValue);
        return lblValue;
    }

    private JPanel createPenaltyPanel() {
        JPanel p = new JPanel(new GridLayout(5, 2, 15, 10));
        p.setBackground(new Color(255, 248, 225));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNING, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("THÔNG TIN PHẠT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(DANGER);
        p.add(lblTitle);
        p.add(new JLabel(""));

        valOverdueDays = addPenaltyRow(p, "Số ngày trễ:", "-", DANGER);
        valRatePerDay  = addPenaltyRow(p, "Mức phạt/ngày:", "-", Color.BLACK);
        valTotalPenalty= addPenaltyRow(p, "Tổng tiền phạt:", "-", DANGER);
        valPaidStatus  = addPenaltyRow(p, "Thanh toán:", "-", Color.BLACK);

        return p;
    }

    private JLabel addPenaltyRow(JPanel p, String label, String value, Color valueColor) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValue.setForeground(valueColor);

        p.add(lblLabel);
        p.add(lblValue);
        return lblValue;
    }

    private JTable createBooksTable() {
        String[] cols = {"ID Bản sao", "Mã sách", "Tên sách", "Tác giả", "Thể loại", "Trạng thái"};
        bookModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tbl = new JTable(bookModel);
        tbl.setRowHeight(38);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tbl.getTableHeader().setBackground(new Color(240, 240, 240));
        return tbl;
    }


    public JButton getBtnPrint() { return btnPrint; }
    public JButton getBtnClose() { return btnClose; }

    public void setHeader(String slipId) {
        setTitle("Chi tiết Phiếu mượn #" + slipId);
        lblTitle.setText("Chi tiết Phiếu mượn #" + slipId);
        valSlipId.setText(slipId);
    }

    public void setInfo(String readerId, String readerName, String borrowDate, String dueDate, String returnDateOrDash) {
        valReaderId.setText(readerId);
        valReaderName.setText(readerName);
        valBorrowDate.setText(borrowDate);
        valDueDate.setText(dueDate);
        valReturnDate.setText(returnDateOrDash);
    }

    public void setPenaltyVisible(boolean visible) {
        penaltyPanel.setVisible(visible);
        // resize theo có phạt hay không
        setSize(950, visible ? 850 : 750);
    }

    public void setPenalty(int overdueDays, double rate, double totalPenalty, boolean paid) {
        valOverdueDays.setText(overdueDays + " ngày");
        valRatePerDay.setText(fmt.format(rate) + " VNĐ");
        valTotalPenalty.setText(fmt.format(totalPenalty) + " VNĐ");
        valPaidStatus.setText(paid ? "Đã thanh toán" : "Chưa thanh toán");
    }

    public void setBooks(List<Object[]> rows) {
        bookModel.setRowCount(0);
        for (Object[] r : rows) bookModel.addRow(r);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public void showPrintPreview(String text) {
        JTextArea ta = new JTextArea(text);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(450, 500));
        JOptionPane.showMessageDialog(this, sp, "Xem trước in", JOptionPane.PLAIN_MESSAGE);
    }
}
