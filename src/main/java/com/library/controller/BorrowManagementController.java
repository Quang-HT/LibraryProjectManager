package com.library.controller;

import com.library.dao.impl.*;
import com.library.model.*;
import com.library.model.display.BorrowSlipSummary;
import com.library.model.display.BorrowSlipVM;
import com.library.service.SendEmail;
import com.library.service.SessionID;
import com.library.view.*;

import javax.swing.*;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CONTROLLER: Quản lý Phiếu mượn & Phạt
 * - Load data từ DAO
 * - Tính phạt / trả sách / thanh toán / lọc / gửi email
 */
public class BorrowManagementController {

    private final BorrowManagementView view;

    private final BorrowSlip_DAO borrowDAO = new BorrowSlip_DAO();
    private final Copy_BorrowSlip_DAO copyBorrowDAO = new Copy_BorrowSlip_DAO();
    private final Reader_DAO readerDAO = new Reader_DAO();
    private final Copy_DAO copyDAO = new Copy_DAO();

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public static double penaltyRate = 5000.0;

    private final DateTimeFormatter dbFormatter   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter viewFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<BorrowSlipVM> slipList = new ArrayList<>();
    private BorrowSlipVM selected = null;

    /**
     * Hàm Controller của BorrowManagement
     * Thực hiện danh sách các hàm cần thực thi
     */
    public BorrowManagementController() {
        this.view = new BorrowManagementView();

        initYearOptions();
        view.setPenaltyRateText(moneyFormat.format(penaltyRate));

        bindEvents();
        reloadAll();

        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Tạo ra các Option về năm trong 10 năm trước cho tới năm hiện tại
     */
    private void initYearOptions() {
        int currentYear = LocalDate.now().getYear();
        String[] years = new String[11];
        years[0] = "Tất cả";
        for (int i = 1; i <= 10; i++) years[i] = String.valueOf(currentYear - 10 + i);
        view.setYearOptions(years, 10);
    }

    /**
     * Đăng ký các sự kiện
     */
    private void bindEvents() {
        view.getBtnSearch().addActionListener(e -> applyFilter());
        view.getBtnClear().addActionListener(e -> {
            view.clearFilters();
            applyFilter();
        });

        view.getBtnUpdateRate().addActionListener(e -> updateRate());

        view.getBtnAutoGenerate().addActionListener(e -> autoGeneratePenalty());
        view.getBtnCreateSlip().addActionListener(e -> createSlip());
        view.getBtnSendEmail().addActionListener(e -> sendEmailPP());

        view.getBtnViewDetails().addActionListener(e -> viewDetails());
        view.getBtnReturn().addActionListener(e -> returnBooks());
        view.getBtnPayPenalty().addActionListener(e -> payPenalty());

        view.getBtnRefresh().addActionListener(e -> reloadAll());
        view.getBtnClose().addActionListener(e -> {
            new HomeView().setVisible(true);
            view.dispose();
        });

        view.getTable().getSelectionModel().addListSelectionListener(this::onSelect);
        view.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) viewDetails();
            }
        });

    }

    /**
     * Làm mới lại dữ liệu sau khi có dữ liệu nào đấy thay đổi
     */
    private void reloadAll() {
        loadDataFromDB();
        renderTable();
        applyFilter();
        updateStats();
        view.setActionButtonsEnabled(false, false, false);
    }

    /**
     * Đưa dữ liệu từ DB lên View để hiển thị
      */
    private void loadDataFromDB() {
        slipList.clear();

        List<BorrowSlip_Entity> list = borrowDAO.findAll();

        for (BorrowSlip_Entity bs : list) {
            int borrowId = bs.getBorrow_id();
            int readerId = bs.getReader_id();

            int bookCount = 0;
            try {
                bookCount = copyBorrowDAO.findByIdBorrow(borrowId).size();
            } catch (Exception ignore) {}

            String readerName = readerDAO.findById(readerId)
                    .map(Reader_Entity::getReader_name)
                    .orElse("Không xác định");

            BorrowSlipVM vm = new BorrowSlipVM();
            vm.setId(String.valueOf(borrowId));
            vm.setReaderId(String.valueOf(readerId));
            vm.setReaderName(readerName);
            vm.setBookCount(bookCount);

            vm.setBorrowDateDb((bs.getBorrow_date() != null) ? bs.getBorrow_date().toLocalDate().format(dbFormatter) : "");
            vm.setDueDateDb((bs.getDue_date() != null) ? bs.getDue_date().toLocalDate().format(dbFormatter) : "");
            vm.setReturnDateDb((bs.getReturn_date() != null) ? bs.getReturn_date().toLocalDate().format(dbFormatter) : null);

            calcPenalty(vm);
            slipList.add(vm);
        }
    }

    private void calcPenalty(BorrowSlipVM vm) {
        vm.setOverdueDays(0);
        vm.setPenaltyAmount(0);
        vm.setPenaltyPaid(false);

        /**
         * Kiểm tra điều kiện ngày trả về
         * vm.getDueDateDb == null (Xem có rỗng không)
         * vm.getDueDateDb.isBlank() (Xem có toàn ký tứ khoảng trắng không
         */
        if (vm.getDueDateDb() == null || vm.getDueDateDb().isBlank()) return;

        try {
            LocalDate due = LocalDate.parse(vm.getDueDateDb(), dbFormatter);
            LocalDate check = (vm.getReturnDateDb() != null && !vm.getReturnDateDb().isBlank())
                    ? LocalDate.parse(vm.getReturnDateDb(), dbFormatter)
                    : LocalDate.now();

            if (check.isAfter(due)) {
                long days = ChronoUnit.DAYS.between(due, check); // Tính khoảng cách giữa due -> check xem baon hiêu ngày
                vm.setOverdueDays((int) days);
                vm.setPenaltyAmount(days * penaltyRate);
                vm.setPenaltyPaid(false);
            }
        } catch (Exception ignore) {
            System.out.println("Lỗi tại BorrowManagementController");
            System.out.println("Fix ngay fix ngay");
        }
    }

    /**
     * Đưa dữ liệu vào thành bảng thông qua Object[]
     * Đưa lên View để hiển thị
     */
    private void renderTable() {
        List<Object[]> rows = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (BorrowSlipVM s : slipList) {
            LocalDate borrowD = parseDbDate(s.getBorrowDateDb());
            LocalDate dueD    = parseDbDate(s.getDueDateDb());
            LocalDate returnD = (s.getReturnDateDb() != null && !s.getReturnDateDb().isBlank()) ? parseDbDate(s.getReturnDateDb()) : null;

            String borrowDisplay = (borrowD != null) ? borrowD.format(viewFormatter) : "";
            String dueDisplay    = (dueD != null) ? dueD.format(viewFormatter) : "";
            String returnDisplay = (returnD != null) ? returnD.format(viewFormatter) : "";

            String paymentStatus = computePaymentStatus(dueD, returnD, today);
            String statusText    = computeStatusText(s, dueD, returnD, today);

            rows.add(new Object[]{
                    s.getId(),
                    s.getReaderId(),
                    s.getReaderName(),
                    s.getBookCount(),
                    borrowDisplay,
                    dueDisplay,
                    returnDisplay,
                    (s.getOverdueDays() > 0 ? String.valueOf(s.getOverdueDays()) : "0"),
                    (s.getPenaltyAmount() > 0 ? moneyFormat.format(s.getPenaltyAmount()) : "0"),
                    paymentStatus,
                    statusText
            });
        }

        view.setTableRows(rows);
    }

    /**
     *Chuyển đổi từ kiểu ngày String -> LocalDate
     */
    private LocalDate parseDbDate(String db) {
        if (db == null || db.isBlank()) return null;
        try { return LocalDate.parse(db, dbFormatter); }
        catch (Exception e) { return null; }
    }

    /**
     *
     * @param dueD
     * @param returnD
     * @param today
     * @return
     * Trả về trạng thái Thanh Toán Đã TT/Chưa TT/ -
     */
    private String computePaymentStatus(LocalDate dueD, LocalDate returnD, LocalDate today) {
        if (returnD != null) return "✅ Đã TT";
        if (dueD == null) return "-";
        if (today.isAfter(dueD)) return "❌ Chưa TT";
        return "-";
    }

    /**
     * Trả về trạng thái của phiếu mượn
     */
    private String computeStatusText(BorrowSlipVM s, LocalDate dueD, LocalDate returnD, LocalDate today) {
        if (returnD != null) {
            return s.getOverdueDays() > 0 ? "Đã trả - Trễ " + s.getOverdueDays() + " ngày" : "Đã trả - Đúng hạn";
        }
        if (dueD == null) return "Lỗi";
        if (today.isAfter(dueD)) {
            long days = ChronoUnit.DAYS.between(dueD, today);
            return "Quá hạn " + days + " ngày";
        }
        long remain = ChronoUnit.DAYS.between(today, dueD);
        return "Đang mượn (còn " + remain + " ngày)";
    }

    /**
     * Search dữ liệu đầu vào
     */
    private void applyFilter() {
        String searchReader = view.getSearchReader().trim().toLowerCase();
        String month = view.getSelectedMonth();
        String year  = view.getSelectedYear();
        String status = view.getSelectedStatus();

        var sorter = view.getSorter();
        if (sorter == null) return;

        if (searchReader.isEmpty() && month.equals("Tất cả") && year.equals("Tất cả") && status.equals("Tất cả")) {
            sorter.setRowFilter(null);
            return;
        }

        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                boolean matchReader = searchReader.isEmpty();
                if (!matchReader) {
                    String readerName = entry.getStringValue(2).toLowerCase(); // .getStringValue(2) là lấy giá trị của cột 2 trong bảng TableModelDefault
                    matchReader = readerName.contains(searchReader);
                }

                boolean matchDate = true;
                if (!month.equals("Tất cả") || !year.equals("Tất cả")) {
                    try {
                        String borrowDateView = entry.getStringValue(4);
                        if (borrowDateView == null || borrowDateView.isBlank()) return false;

                        LocalDate d = LocalDate.parse(borrowDateView, viewFormatter);
                        if (!month.equals("Tất cả")) matchDate &= (d.getMonthValue() == Integer.parseInt(month));
                        if (!year.equals("Tất cả"))  matchDate &= (d.getYear() == Integer.parseInt(year));
                    } catch (Exception e) {
                        return false;
                    }
                }

                boolean matchStatus = status.equals("Tất cả");
                if (!matchStatus) {
                    String st  = entry.getStringValue(10);
                    String pay = entry.getStringValue(9);
                    matchStatus = switch (status) {
                        case "Đang mượn" -> st.contains("Đang mượn");
                        case "Quá hạn" -> st.contains("Quá hạn");
                        case "Đã trả - Đúng hạn" -> st.contains("Đúng hạn");
                        case "Đã trả - Trễ (Đã TT)" -> st.contains("Trễ") && "✅ Đã TT".equals(pay);
                        case "Đã trả - Trễ (Chưa TT)" -> st.contains("Trễ") && "❌ Chưa TT".equals(pay);
                        default -> false;
                    };
                }

                return matchReader && matchDate && matchStatus;
            }
        });
    }

    /**
     * Cập nhật dữ liệu tổng quan các phiếu mượn
     */
    private void updateStats() {
        int total = slipList.size();
        int active = 0, expired = 0, returned = 0;
        double totalPen = 0, unpaidPen = 0;

        LocalDate today = LocalDate.now();

        for (BorrowSlipVM s : slipList) {
            if (s.getReturnDateDb() != null) returned++;
            else {
                LocalDate due = parseDbDate(s.getDueDateDb());
                if (due != null && today.isAfter(due)) expired++;
                else active++;
            }

            if (s.getPenaltyAmount() > 0) {
                totalPen += s.getPenaltyAmount();
                if (!s.isPenaltyPaid()) unpaidPen += s.getPenaltyAmount();
            }
        }

        view.setStats(total, active, expired, returned, totalPen, unpaidPen);
    }

    /**
     *Hàm từ sự lựa chọn của người dùng lấy ra BorrowSlipVM
     */
    private void onSelect(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;

        int rowView = view.getTable().getSelectedRow();
        if (rowView < 0) {
            selected = null;
            view.setActionButtonsEnabled(false, false, false);
            return;
        }

        int rowModel = view.getTable().convertRowIndexToModel(rowView); // Chuyển từ Row View -> RowModel để lấy vị trí index thật khi dùng filters
        String id = view.getTableModel().getValueAt(rowModel, 0).toString(); // Từ RowModel lấy id trong cột id

        selected = slipList.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        if (selected == null) {
            view.setActionButtonsEnabled(false, false, false);
            return;
        }

        boolean canReturn = (selected.getReturnDateDb() == null);
        boolean canPay    = (selected.getPenaltyAmount() > 0 && !selected.isPenaltyPaid());
        view.setActionButtonsEnabled(true, canReturn, canPay);
    }

    /**
     * Hàm mở xem chi tiết phiếu mượn
     * Có 2 cách:
     * - Cách 1: Click vào phiếu mượn 2 lần
     * - Cách 2: Click 1 lần và ấn nút "Xem chi tiết"
     * Hàm lấy lấy dữ liệu của phiếu đưa lên View để người dùng xem
     */
    private void viewDetails() {
        if (selected == null) return;

        int rowView = view.getTable().getSelectedRow();
        if (rowView < 0) return;

        int rowModel = view.getTable().convertRowIndexToModel(rowView);
        DefaultTableModel model = view.getTableModel();

        String borrowDateView = model.getValueAt(rowModel, 4).toString();
        String dueDateView    = model.getValueAt(rowModel, 5).toString();
        String returnDateView = model.getValueAt(rowModel, 6).toString();
        if (returnDateView.isBlank()) returnDateView = null;

        BorrowSlipSummary dto =
                new BorrowSlipSummary(
                        selected.getId(),
                        selected.getReaderId(),
                        selected.getReaderName(),
                        borrowDateView,
                        dueDateView,
                        returnDateView,
                        selected.getOverdueDays(),
                        selected.getPenaltyAmount(),
                        selected.isPenaltyPaid()
                );

        new BorrowSlipDetailController(dto, penaltyRate);
    }

    /**
     * Dùng cho Button trả sách khi người dùng chọn trả sách
     */
    private void returnBooks() {
        if (selected == null) {
            view.showError("⚠️ Bạn chưa chọn phiếu mượn nào!");
            return;
        }
        if (selected.getReturnDateDb() != null) {
            view.showWarn("⚠️ Phiếu này đã trả sách rồi!");
            return;
        }

        String msg = "Xác nhận trả sách?\n\nMã: " + selected.getId() +
                "\nĐộc giả: " + selected.getReaderName();

        if (selected.getOverdueDays() > 0) {
            msg += "\n\n⚠️ Trễ " + selected.getOverdueDays() + " ngày\nPhạt: " +
                    moneyFormat.format(selected.getPenaltyAmount()) + " VNĐ";
        }

        if (view.askConfirm(msg, "Xác nhận") != JOptionPane.YES_OPTION) return;

        try {
            LocalDate now = LocalDate.now();

            Optional<BorrowSlip_Entity> opt = borrowDAO.findById(Integer.parseInt(selected.getId()));
            if (opt.isEmpty()) {
                view.showError("Không tìm thấy phiếu mượn!");
                return;
            }

            BorrowSlip_Entity entity = opt.get();
            entity.setReturn_date(java.sql.Date.valueOf(now));
            entity.setState("Returned");
            borrowDAO.update(entity);

            // update copies
            setAvailableBook(Integer.parseInt(selected.getId()));

            // update VM
            selected.setReturnDateDb(now.format(dbFormatter));
            calcPenalty(selected);

            view.showInfo("✅ Đã trả sách!" + (selected.getPenaltyAmount() > 0 ? "\n⚠️ Phạt: " + moneyFormat.format(selected.getPenaltyAmount()) + " VNĐ" : ""));
            renderTable();
            applyFilter();
            updateStats();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi trả sách: " + ex.getMessage());
        }
    }

    /**
     * Dùng cho Button thanh toán phiếu phạt
     */
    private void payPenalty() {
        if (selected == null || selected.getPenaltyAmount() <= 0 || selected.isPenaltyPaid()) return;

        String msg = "💳 Thanh toán phạt?\n\nMã: " + selected.getId() +
                "\nĐộc giả: " + selected.getReaderName() +
                "\nTrễ: " + selected.getOverdueDays() + " ngày" +
                "\nPhạt: " + moneyFormat.format(selected.getPenaltyAmount()) + " VNĐ";

        if (view.askConfirm(msg, "Xác nhận") != JOptionPane.YES_OPTION) return;

        try {
            LocalDate now = LocalDate.now();

            selected.setPenaltyPaid(true);
            selected.setPaymentDateDb(now.format(dbFormatter));

            renderTable();
            applyFilter();
            updateStats();

            view.showInfo("✅ Thanh toán thành công!");
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi thanh toán: " + ex.getMessage());
        }
    }

    /**
     * Mỗi khi người dùng trả sách các sách được trả sẽ được set trạng thái "Available" - Sãn sàng dử dụng cho các lần mượn khác
     */
    private void setAvailableBook(int idBorrow) {
        List<Copy_BorrowSlip_Entity> list = copyBorrowDAO.findByIdBorrow(idBorrow);
        for (Copy_BorrowSlip_Entity cbs : list) {
            Copy_Entity copy = copyDAO.findById(cbs.getCopy_id()).orElse(null);
            if (copy != null) {
                copy.setStatus("Available");
                copyDAO.update(copy);
            }
        }
    }

    /**
     * Button tính phiếu phạt cho những phiếu quá hạn
     */
    private void autoGeneratePenalty() {
        if (view.askConfirm("Tự động tính phạt cho phiếu quá hạn?", "Xác nhận") != JOptionPane.YES_OPTION) return;

        int count = 0;
        for (BorrowSlipVM s : slipList) {
            if (s.getReturnDateDb() == null) {
                double before = s.getPenaltyAmount();
                calcPenalty(s);
                if (before == 0 && s.getPenaltyAmount() > 0) count++;
            }
        }
        renderTable();
        applyFilter();
        updateStats();
        view.showInfo("✅ Đã tính phạt " + count + " phiếu!");
    }

    /**
     * Mức phạt do người dùng set mặc định 5000đ
     */
    private void updateRate() {
        try {
            double rate = Double.parseDouble(view.getPenaltyRateText().replaceAll("[^0-9]", ""));
            if (rate < 1000 || rate > 100000) throw new IllegalArgumentException("Rate out of range");

            String msg = "Đổi mức phạt?\n\nCũ: " + moneyFormat.format(penaltyRate) +
                    "\nMới: " + moneyFormat.format(rate) +
                    "\n\nLưu ý: Phạt của các phiếu hiện tại sẽ được tính lại!";

            if (view.askConfirm(msg, "Xác nhận") != JOptionPane.YES_OPTION) {
                view.setPenaltyRateText(moneyFormat.format(penaltyRate));
                return;
            }

            penaltyRate = rate;
            for (BorrowSlipVM s : slipList) calcPenalty(s);

            renderTable();
            applyFilter();
            updateStats();
            view.showInfo("✅ Đã cập nhật mức phạt!\n\nTất cả phiếu phạt đã được tính lại.");
        } catch (Exception ex) {
            view.showError("Nhập số từ 1,000-100,000!");
            view.setPenaltyRateText(moneyFormat.format(penaltyRate));
        }
    }

    /**
     * Tạo View thêm phiếu mượn mới
     */
    private void createSlip() {
        new AddBorrowSlipController(SessionID.getCurrentAccountId());
    }

    /**
     * Gửi email cho các phiếu phạt để thông báo trả sách + thanh toán phiếu
     */
    private void sendEmailPP() {
        try {
            List<BorrowSlip_Entity> listBr = borrowDAO.findAll();
            for (BorrowSlip_Entity br : listBr) {
                if (br.getReturn_date() == null) {
                    Reader_Entity rd = readerDAO.findById(br.getReader_id()).orElse(null);
                    if (rd != null && rd.getEmail() != null && !rd.getEmail().isBlank()) {
                        new SendEmail().sendEmailToPP(rd.getEmail(), rd.getReader_name());
                    }
                }
            }
            view.showInfo("Đã gửi email thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Gửi email thất bại: " + ex.getMessage());
        }
    }
}
