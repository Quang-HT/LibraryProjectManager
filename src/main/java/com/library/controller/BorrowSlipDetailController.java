package com.library.controller;

import com.library.dao.impl.Book_DAO;
import com.library.dao.impl.Copy_BorrowSlip_DAO;
import com.library.dao.impl.copy_DAO;
import com.library.model.Book_Entity;
import com.library.model.Copy_BorrowSlip_Entity;
import com.library.model.copy_Entity;
import com.library.model.display.BorrowSlipSummary;
import com.library.service.FIND;
import com.library.view.BorrowSlipDetailView;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * CONTROLLER: Chi tiết phiếu mượn
 * - Load sách mượn từ DAO
 * - Xử lý In/Đóng
 */
public class BorrowSlipDetailController {

    private final BorrowSlipDetailView view;
    private final BorrowSlipSummary slip;
    private final double rate;

    private final DecimalFormat fmt = new DecimalFormat("#,###");

    private final Copy_BorrowSlip_DAO copyBorrowDAO = new Copy_BorrowSlip_DAO();
    private final copy_DAO copyDAO = new copy_DAO();
    private final Book_DAO bookDAO = new Book_DAO();
    private final FIND findService = new FIND();

    public BorrowSlipDetailController(BorrowSlipSummary slip, double rate) {
        this.slip = slip;
        this.rate = rate;
        this.view = new BorrowSlipDetailView();

        bindEvents();
        loadAndRender();

        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    private void bindEvents() {
        view.getBtnClose().addActionListener(e -> view.dispose());
        view.getBtnPrint().addActionListener(e -> printPreview());
    }

    private void loadAndRender() {
        view.setHeader(slip.id);

        view.setInfo(
                slip.readerId,
                slip.readerName,
                safe(slip.borrowDate),
                safe(slip.dueDate),
                (slip.returnDate != null && !slip.returnDate.isBlank()) ? slip.returnDate : "-"
        );

        boolean hasPenalty = slip.penaltyAmount > 0;
        view.setPenaltyVisible(hasPenalty);
        if (hasPenalty) {
            view.setPenalty(slip.overdueDays, rate, slip.penaltyAmount, slip.penaltyPaid);
        }

        // Load book list
        try {
            int borrowId = Integer.parseInt(slip.id);
            List<Object[]> rows = loadBookRows(borrowId);
            view.setBooks(rows);
        } catch (Exception ex) {
            view.showError("Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private List<Object[]> loadBookRows(int borrowId) {
        List<Object[]> rows = new ArrayList<>();

        List<Copy_BorrowSlip_Entity> list = copyBorrowDAO.findByIdBorrow(borrowId);
        for (Copy_BorrowSlip_Entity cb : list) {
            copy_Entity copy = copyDAO.findById(cb.getCopy_id()).orElse(null);
            if (copy == null) continue;

            Book_Entity book = bookDAO.findById(copy.getBook_id()).orElse(null);
            if (book == null) continue;

            rows.add(new Object[]{
                    cb.getCopy_id(),
                    copy.getBook_id(),
                    book.getTitle(),
                    findService.FindNameAuthor(book.getBook_id()),
                    findService.FindNameCategory(book.getBook_id()),
                    copy.getStatus()
            });
        }
        return rows;
    }

    private void printPreview() {
        StringBuilder txt = new StringBuilder();
        txt.append("═══════════════════════════════════════\n");
        txt.append("         PHIẾU MƯỢN SÁCH\n");
        txt.append("═══════════════════════════════════════\n\n");
        txt.append("Mã phiếu: ").append(slip.id).append("\n");
        txt.append("Mã độc giả: ").append(slip.readerId).append("\n");
        txt.append("Tên độc giả: ").append(slip.readerName).append("\n");
        txt.append("Ngày mượn: ").append(safe(slip.borrowDate)).append("\n");
        txt.append("Hạn trả: ").append(safe(slip.dueDate)).append("\n");

        if (slip.returnDate != null && !slip.returnDate.isBlank()) {
            txt.append("Ngày trả: ").append(slip.returnDate).append("\n");
        }

        if (slip.penaltyAmount > 0) {
            txt.append("\n───────────────────────────────────────\n");
            txt.append("          THÔNG TIN PHẠT\n");
            txt.append("───────────────────────────────────────\n");
            txt.append("Số ngày trễ: ").append(slip.overdueDays).append(" ngày\n");
            txt.append("Mức phạt/ngày: ").append(fmt.format(rate)).append(" VNĐ\n");
            txt.append("Tiền phạt: ").append(fmt.format(slip.penaltyAmount)).append(" VNĐ\n");
            txt.append("Thanh toán: ").append(slip.penaltyPaid ? "Đã thanh toán" : "Chưa thanh toán").append("\n");
        }

        txt.append("\n═══════════════════════════════════════\n");
        view.showPrintPreview(txt.toString());
    }

    private String safe(String s) { return (s == null) ? "" : s; }

}
