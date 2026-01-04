package com.library.view;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

import static com.library.service.ImagineApp.createBookIcon;

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

    @Getter
    private JButton btnPrint;
    @Getter
    private JButton btnClose;

    public BorrowSlipDetailView() {
        initFrame();
        createUI();
    }

    private void initFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        try {
            setIconImage(createBookIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(20, 25, 20, 25));

        lblTitle = new JLabel("Chi tiết Phiếu mượn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        main.add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JPanel info = createInfoPanel();
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(info);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        penaltyPanel = createPenaltyPanel();
        penaltyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        penaltyPanel.setVisible(false);
        content.add(penaltyPanel);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBooks = new JLabel("Danh sách sách mượn:");
        lblBooks.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBooks.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(lblBooks, BorderLayout.NORTH);

        tblBooks = createBooksTable();
        JScrollPane sp = new JScrollPane(tblBooks);
        sp.setPreferredSize(new Dimension(0, 250));
        tablePanel.add(sp, BorderLayout.CENTER);

        content.add(tablePanel);

        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setBorder(null);
        contentScroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(contentScroll, BorderLayout.CENTER);

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
        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setBackground(new Color(255, 248, 225));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(WARNING, 2),
                BorderFactory.createEmptyBorder(13, 18, 13, 18)
        ));

        JLabel lblTitle = new JLabel("THÔNG TIN PHẠT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(DANGER);
        wrapper.add(lblTitle, BorderLayout.NORTH);

        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 0, 3, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        valOverdueDays  = addPenaltyRow(p, gbc, 0, "Số ngày trễ:", "-", DANGER);
        valRatePerDay   = addPenaltyRow(p, gbc, 1, "Mức phạt/ngày:", "-", Color.BLACK);
        valTotalPenalty = addPenaltyRow(p, gbc, 2, "Tổng tiền phạt:", "-", DANGER);
        valPaidStatus   = addPenaltyRow(p, gbc, 3, "Thanh toán:", "-", Color.BLACK);

        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel addPenaltyRow(JPanel p, GridBagConstraints gbc, int row,
                                 String label, String value, Color valueColor) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblValue.setForeground(valueColor);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        p.add(lblLabel, gbc);

        // cột 1: value
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        p.add(lblValue, gbc);

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
        penaltyPanel.revalidate();
        penaltyPanel.repaint();

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
