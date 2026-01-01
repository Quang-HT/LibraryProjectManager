package com.library.controller;

import com.library.dao.impl.BorrowSlip_DAO;
import com.library.dao.impl.Reader_DAO;
import com.library.model.BorrowSlip_Entity;
import com.library.model.Reader_Entity;

import com.library.model.display.BorrowSlipSummary;
import com.library.view.ReaderDetailView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReaderDetailController {

    private final ReaderDetailView view;
    private final int readerId;

    private boolean isEditMode = false;
    private List<BorrowSlip_Entity> borrowSlipList = new ArrayList<>();

    /**
     * Controller của Reader Detail
     * Xét thứ tự công việc cần thực thi
     */
    public ReaderDetailController(int readerId) {
        this.readerId = readerId;
        this.view = new ReaderDetailView();

        bindEvents();
        loadReaderData(readerId);
        view.setEditMode(false);

        view.setVisible(true);
    }

    /**
     * Đăng ký các sự kiện khi người dúng ấn Button
     */
    private void bindEvents() {
        view.getBtnEdit().addActionListener(e -> enableEditMode());
        view.getBtnSave().addActionListener(e -> saveReader());
        view.getBtnCancel().addActionListener(e -> cancelForm());

        view.getTblBorrowSlips().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openBorrowSlipDetail();
                }
            }
        });
    }

    /**
     * Đưa dữ liệu cần thiết lên View
     */
    private void loadReaderData(int readerId) {
        try {
            Reader_Entity reader = new Reader_DAO().findById(readerId).orElse(null);

            if (reader == null) {
                view.showError("Không tìm thấy độc giả với ID = " + readerId);
                view.dispose();
                return;
            }

            view.setReaderId(String.valueOf(readerId));
            view.setFullName(reader.getReader_name());
            view.setPhone(reader.getPhone());
            view.setEmail(reader.getEmail());
            view.setAddress(reader.getAddress());
            view.setRegistrationDate(String.valueOf(reader.getRegistered_date()));
            view.setCccd(reader.getCccd());

            // Nếu DB chưa có status/notes thì để mặc định
            view.setStatusIndex(0);
            if (view.getNotesText().isEmpty()) {
                view.setNotes("Độc giả thân thiết. Thường mượn sách văn học.");
            }

            loadBorrowSlips(readerId);

        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Lỗi khi tải thông tin độc giả: " + e.getMessage());
        }
    }

    /**
     * Cho phép xem các phiếu mà người dùng đã mượn
     */
    private void loadBorrowSlips(int readerId) {
        try {
            borrowSlipList = new BorrowSlip_DAO().findByIdReader(readerId);

            List<Object[]> rows = new ArrayList<>();
            if (borrowSlipList != null) {
                for (BorrowSlip_Entity slip : borrowSlipList) {
                    String status = determineSlipStatus(slip);
                    Object[] row = {
                            slip.getBorrow_id(),
                            slip.getBorrow_date() != null ? slip.getBorrow_date().toString() : "",
                            slip.getDue_date() != null ? slip.getDue_date().toString() : "",
                            slip.getReturn_date() != null ? slip.getReturn_date().toString() : "Chưa trả",
                            status
                    };
                    rows.add(row);
                }
            }

            view.setBorrowSlips(borrowSlipList, rows);

        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Lỗi khi tải danh sách phiếu mượn: " + e.getMessage());
        }
    }

    /**
     * Trả về trạng thái của phiếu mượn Đã trả/Quá hạn/Đang mượn/Không xác định
     */
    private String determineSlipStatus(BorrowSlip_Entity slip) {
        try {
            if (slip.getReturn_date() != null) return "Đã trả";
            if (slip.getDue_date() != null && slip.getDue_date().before(new Date())) return "Quá hạn";
            return "Đang mượn";
        } catch (Exception e) {
            return "Không xác định";
        }
    }

    /**
     * Mở phiếu mượn chi tiết trong danh sách Đọc giả đã mượn
     */
    private void openBorrowSlipDetail() {
        BorrowSlip_Entity slip = view.getSelectedSlipOrNull();
        if (slip == null) {
            view.showWarning("Vui lòng chọn một phiếu mượn!");
            return;
        }

        try {
            BorrowSlip_Entity bs = new BorrowSlip_DAO().findById(slip.getBorrow_id()).orElse(null);
            if (bs == null) {
                view.showError("Không tìm thấy phiếu mượn!");
                return;
            }

            Reader_Entity r = new Reader_DAO().findById(bs.getReader_id()).orElse(null);
            if (r == null) {
                view.showError("Không tìm thấy độc giả của phiếu mượn!");
                return;
            }
            boolean paid = bs.getReturn_date() != null;
            long diffMs = bs.getReturn_date().getTime() - bs.getDue_date().getTime();
            long lateDays = diffMs / (1000 * 60 * 60 * 24);
            BorrowSlipSummary dto =
                    new BorrowSlipSummary(
                            String.valueOf(bs.getBorrow_id()),
                            String.valueOf(bs.getReader_id()),
                            r.getReader_name(),
                            String.valueOf(bs.getBorrow_date()),
                            String.valueOf(bs.getDue_date()),
                            String.valueOf(bs.getReturn_date()),
                            (int) lateDays,
                            Double.valueOf(lateDays* BorrowManagementController.penaltyRate),
                            paid
                    );

            BorrowSlipDetailController detail = new BorrowSlipDetailController(dto, BorrowManagementController.penaltyRate);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi mở chi tiết phiếu mượn: " + ex.getMessage());
        }
    }

    /**
     * Cho phép chỉnh sửa
     */
    private void enableEditMode() {
        isEditMode = true;
        view.setEditMode(true);
    }

    /**
     * Lưu lại thông tin sau khi người dùng chỉnh sửa
     */
    private void saveReader() {
        if (view.getFullNameText().isEmpty()) {
            view.showError("Vui lòng nhập họ tên!");
            return;
        }

        Reader_DAO rd = new Reader_DAO();
        Reader_Entity re = new Reader_Entity();

        re.setReader_id(parseIntSafe(view.getReaderIdText()));
        re.setReader_name(view.getFullNameText());
        re.setPhone(view.getPhoneText());
        re.setEmail(view.getEmailText());
        re.setAddress(view.getAddressText());
        re.setCccd(view.getCccdText());

        try {
            String s = view.getRegistrationDateText();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            Date utilDate = sdf.parse(s);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            re.setRegistered_date(sqlDate);
        } catch (ParseException e) {
            view.showError("Ngày không đúng định dạng! (yyyy-MM-dd)");
            return;
        }

        try {
            rd.update(re);
            view.showInfo("Đã cập nhật thông tin độc giả!");

            isEditMode = false;
            view.setEditMode(false);

            loadReaderData(readerId);

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi cập nhật độc giả: " + ex.getMessage());
        }
    }

    /**
     *Chuyển đổi String -> Int an toàn
     */
    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); }
        catch (Exception e) { return 0; }
    }

    /**
     * Thoát View
     */
    private void cancelForm() {
        if (isEditMode) {
            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Bạn có chắc chắn muốn hủy? Các thay đổi chưa lưu sẽ bị mất.",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                loadReaderData(readerId);
                isEditMode = false;
                view.setEditMode(false);
            }
        } else {
            view.dispose();
        }
    }
}
