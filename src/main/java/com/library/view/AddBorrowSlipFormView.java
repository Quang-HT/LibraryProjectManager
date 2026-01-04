package com.library.view;

import com.library.model.Reader_Entity;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

import static com.library.service.ImagineApp.createBookIcon;

public class AddBorrowSlipFormView extends JFrame {

    private static final Color PRIMARY = new Color(25, 118, 210);
    private static final Color SUCCESS = new Color(76, 175, 80);
    private static final Color DANGER  = new Color(220, 48, 35);
    private static final Color WARNING = new Color(255, 152, 0);
    private static final Color BORDER  = new Color(180, 180, 180);

    private JTextField txtReaderId, txtReaderName, txtPhone, txtEmail;
    private JTextField txtBookSearch;
    @Getter
    private JComboBox<Integer> cmbBorrowDays;
    private JLabel lblBorrowDate, lblDueDate;

    @Getter
    private JTable tblAvailableBooks;
    @Getter
    private JTable tblSelectedBooks;
    @Getter
    private DefaultTableModel availableModel;
    @Getter
    private DefaultTableModel selectedModel;

    @Getter
    private JButton btnFindReader;
    @Getter
    private JButton btnNewReader;
    @Getter
    private JButton btnAddBook;
    @Getter
    private JButton btnRemoveBook;
    @Getter
    private JButton btnSave;
    @Getter
    private JButton btnReset;
    @Getter
    private JButton btnCancel;
    @Getter
    private JButton btnSearchBook;

    public AddBorrowSlipFormView() {
        initFrame();
        createUI();
        setBorrowDate(LocalDate.now().toString());
    }

    private void initFrame() {
        setTitle("Tạo Phiếu Mượn Sách Mới");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        try {
            setIconImage(createBookIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUI() {
        JPanel main = new JPanel(null);
        main.setBackground(Color.WHITE);
        main.setBorder(new EmptyBorder(20, 30, 20, 30));

        addLabel(main, "Tạo Phiếu Mượn Sách", 30, 10, 400, 40, 26, true, PRIMARY);

        createReaderSection(main);
        createBorrowInfoSection(main);
        createBookSelectionSection(main);
        createSelectedBooksSection(main);
        createActionButtons(main);

        add(main);
    }

    private void createReaderSection(JPanel parent) {
        JPanel panel = new JPanel(null);
        panel.setBounds(30, 60, 600, 180);
        panel.setBackground(new Color(245, 248, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 2),
                "Thông tin Độc giả",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY
        ));

        addLabel(panel, "Mã độc giả:", 20, 35, 100, 25, 13, true, Color.BLACK);
        txtReaderId = addTextField(panel, "", 130, 35, 150, 30);

        btnFindReader = addButton(panel, "Tìm", 290, 35, 80, 30, PRIMARY);
        btnNewReader  = addButton(panel, "Mới", 380, 35, 80, 30, SUCCESS);

        addLabel(panel, "Tên độc giả:", 20, 75, 100, 25, 13, true, Color.BLACK);
        txtReaderName = addTextField(panel, "", 130, 75, 330, 30);
        lockField(txtReaderName);

        addLabel(panel, "Điện thoại:", 20, 115, 100, 25, 13, true, Color.BLACK);
        txtPhone = addTextField(panel, "", 130, 115, 150, 30);
        lockField(txtPhone);

        addLabel(panel, "Email:", 290, 115, 60, 25, 13, true, Color.BLACK);
        txtEmail = addTextField(panel, "", 350, 115, 210, 30);
        lockField(txtEmail);

        parent.add(panel);
    }

    private void createBorrowInfoSection(JPanel parent) {
        JPanel panel = new JPanel(null);
        panel.setBounds(650, 60, 590, 180);
        panel.setBackground(new Color(245, 248, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 2),
                "Thông tin Mượn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY
        ));

        addLabel(panel, "Ngày mượn:", 20, 35, 100, 25, 13, true, Color.BLACK);
        lblBorrowDate = addLabel(panel, LocalDate.now().toString(), 130, 35, 200, 30, 13, false, Color.BLACK);
        lblBorrowDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        addLabel(panel, "Số ngày mượn:", 20, 80, 100, 25, 13, true, Color.BLACK);
        cmbBorrowDays = new JComboBox<>(new Integer[]{7, 14, 21, 30});
        cmbBorrowDays.setBounds(130, 80, 100, 30);
        cmbBorrowDays.setSelectedItem(14);
        panel.add(cmbBorrowDays);

        addLabel(panel, "Hạn trả:", 20, 125, 100, 25, 13, true, Color.BLACK);
        lblDueDate = addLabel(panel, "", 130, 125, 200, 30, 13, true, DANGER);
        lblDueDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel note = new JLabel("ℹTối đa 5 cuốn/lần mượn");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(new Color(100, 100, 100));
        note.setBounds(350, 35, 200, 25);
        panel.add(note);

        parent.add(panel);
    }

    private void createBookSelectionSection(JPanel parent) {
        JPanel panel = new JPanel(null);
        panel.setBounds(30, 260, 600, 440);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 2),
                "Danh sách Sách có sẵn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                PRIMARY
        ));

        addLabel(panel, "Tìm sách:", 15, 30, 80, 30, 13, true, Color.BLACK);
        txtBookSearch = addTextField(panel, "", 95, 30, 370, 30);
        btnSearchBook = addButton(panel, "", 475, 30, 50, 30, PRIMARY);

        String[] cols = {"ID Bản sao", "Mã sách", "Tên sách", "Tác giả", "Thể loại"};
        availableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblAvailableBooks = new JTable(availableModel);
        tblAvailableBooks.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblAvailableBooks.setRowHeight(32);
        tblAvailableBooks.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblAvailableBooks.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane sp = new JScrollPane(tblAvailableBooks);
        sp.setBounds(15, 75, 570, 300);
        panel.add(sp);

        btnAddBook = addButton(panel, "Thêm vào phiếu", 180, 390, 240, 35, SUCCESS);
        btnAddBook.setFont(new Font("Segoe UI", Font.BOLD, 14));

        parent.add(panel);
    }

    private void createSelectedBooksSection(JPanel parent) {
        JPanel panel = new JPanel(null);
        panel.setBounds(650, 260, 590, 440);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 2),
                "Sách đã chọn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                SUCCESS
        ));

        String[] cols = {"ID Bản sao", "Mã sách", "Tên sách", "Tác giả"};
        selectedModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSelectedBooks = new JTable(selectedModel);
        tblSelectedBooks.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblSelectedBooks.setRowHeight(32);
        tblSelectedBooks.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblSelectedBooks.setSelectionBackground(new Color(255, 220, 220));

        JScrollPane sp = new JScrollPane(tblSelectedBooks);
        sp.setBounds(15, 30, 560, 345);
        panel.add(sp);

        btnRemoveBook = addButton(panel, "Xóa khỏi danh sách", 170, 390, 240, 35, DANGER);
        btnRemoveBook.setFont(new Font("Segoe UI", Font.BOLD, 14));

        parent.add(panel);
    }

    private void createActionButtons(JPanel parent) {
        btnSave = addButton(parent, "Lưu Phiếu Mượn", 850, 720, 200, 45, PRIMARY);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnReset  = addButton(parent, "Làm mới", 1060, 720, 120, 45, WARNING);
        btnCancel = addButton(parent, "Hủy", 1190, 720, 80, 45, new Color(200, 200, 200));
    }

    private void lockField(JTextField tf) {
        tf.setEditable(false);
        tf.setBackground(new Color(240, 240, 240));
    }


    public String getReaderIdText() { return txtReaderId.getText(); }
    public void setReaderIdText(String s) { txtReaderId.setText(s); }
    public String getBookSearchText() { return txtBookSearch.getText(); }
    public Integer getBorrowDays() { return (Integer) cmbBorrowDays.getSelectedItem(); }
    public String getBorrowDateText() { return lblBorrowDate.getText(); }
    public String getDueDateText() { return lblDueDate.getText(); }
    public void setBorrowDate(String s) { lblBorrowDate.setText(s); }
    public void setDueDate(String s) { lblDueDate.setText(s); }

    public void setReaderInfo(Reader_Entity r) {
        if (r == null) {
            txtReaderName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            return;
        }
        txtReaderName.setText(r.getReader_name());
        txtPhone.setText(r.getPhone());
        txtEmail.setText(r.getEmail());
    }

    public void clearSelectedBooks() { selectedModel.setRowCount(0); }
    public void clearAvailableBooks() { availableModel.setRowCount(0); }

    public int askConfirm(String msg, String title) {
        return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public void showInfo(String msg) { JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE); }
    public void showWarn(String msg) { JOptionPane.showMessageDialog(this, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE); }
    public void showError(String msg){ JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE); }


    private JLabel addLabel(JPanel p, String txt, int x, int y, int w, int h, int size, boolean bold, Color color) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        l.setForeground(color);
        l.setBounds(x, y, w, h);
        p.add(l);
        return l;
    }
    private JTextField addTextField(JPanel p, String txt, int x, int y, int w, int h) {
        JTextField tf = new JTextField(txt);
        tf.setBounds(x, y, w, h);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        p.add(tf);
        return tf;
    }
    private JButton addButton(JPanel p, String txt, int x, int y, int w, int h, Color c) {
        JButton b = new JButton(txt);
        b.setBounds(x, y, w, h);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p.add(b);
        return b;
    }
}
