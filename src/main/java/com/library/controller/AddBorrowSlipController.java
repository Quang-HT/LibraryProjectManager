package com.library.controller;

import com.library.dao.impl.*;
import com.library.model.*;
import com.library.service.FIND;
import com.library.view.AddBorrowSlipFormView;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddBorrowSlipController {

    private final AddBorrowSlipFormView view;
    private final int accountId;

    private final Reader_DAO readerDAO = new Reader_DAO();
    private final BorrowSlip_DAO borrowDAO = new BorrowSlip_DAO();
    private final Copy_BorrowSlip_DAO copyBorrowDAO = new Copy_BorrowSlip_DAO();
    private final Copy_DAO copyDAO = new Copy_DAO();
    private final Book_DAO bookDAO = new Book_DAO();
    private final FIND find = new FIND();

    private Reader_Entity currentReader;
    private final List<Copy_Entity> selectedCopies = new ArrayList<>();

    /**
     * Hàm Constructer của Add BorrowSlip
     * Thực thi thứ tự các hàm cần thực thi
     */
    public AddBorrowSlipController(int accountId) {
        this.accountId = accountId;
        this.view = new AddBorrowSlipFormView();

        bindEvents();
        updateDates();
        loadAvailableBooks("");

        SwingUtilities.invokeLater(() -> view.setVisible(true));
    }

    /**
     * Thêm các sự kiện với các nút Button ở View
     */
    private void bindEvents() {
        view.getBtnFindReader().addActionListener(e -> searchReader());
        view.getBtnNewReader().addActionListener(e -> new AddReaderController());

        view.getCmbBorrowDays().addActionListener(e -> updateDates());

        view.getBtnSearchBook().addActionListener(e -> loadAvailableBooks(view.getBookSearchText()));
        view.getBtnAddBook().addActionListener(e -> addBookToSlip());
        view.getBtnRemoveBook().addActionListener(e -> removeBookFromSlip());

        view.getBtnSave().addActionListener(e -> saveSlip());
        view.getBtnReset().addActionListener(e -> reset());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    /**
     * Set Date hiện tại cho View
     */
    private void updateDates() {
        LocalDate borrowDate = LocalDate.now();
        int days = view.getBorrowDays();
        LocalDate dueDate = borrowDate.plusDays(days);

        view.setBorrowDate(borrowDate.toString());
        view.setDueDate(dueDate.toString());
    }

    /**
     * Tìm độc giả thông qua mã cá nhân
     */
    private void searchReader() {
        String idText = view.getReaderIdText().trim();
        if (idText.isEmpty()) {
            view.showError("Vui lòng nhập mã độc giả!");
            return;
        }

        try {
            int readerId = Integer.parseInt(idText);
            var opt = readerDAO.findById(readerId);
            if (opt.isEmpty()) {
                currentReader = null;
                view.setReaderInfo(null);
                view.showError("❌ Không tìm thấy độc giả với mã: " + readerId);
                return;
            }

            currentReader = opt.get();
            view.setReaderInfo(currentReader);
            view.showInfo("✅ Tìm thấy độc giả: " + currentReader.getReader_name());
        } catch (NumberFormatException ex) {
            view.showError("Mã độc giả phải là số!");
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi tìm độc giả: " + ex.getMessage());
        }
    }

    /**
     * Đưa các đầu sách (Copies) có status = "Available" lên để cho người dùng lựa chọn các cuốn muốn mượn
     */
    private void loadAvailableBooks(String keyword) {
        String search = (keyword == null) ? "" : keyword.trim().toLowerCase();

        view.clearAvailableBooks();
        try {
            List<Copy_Entity> copies = copyDAO.findAll();
            for (Copy_Entity copy : copies) {
                if (!"Available".equals(copy.getStatus())) continue;

                var bookOpt = bookDAO.findById(copy.getBook_id()); // var == Optional
                if (bookOpt.isEmpty()) continue;

                Book_Entity b = bookOpt.get();
                String author = find.FindNameAuthor(b.getBook_id());
                String category = find.FindNameCategory(b.getBook_id());

                boolean match = search.isEmpty()                      // Ô tìm kiếm trống || ô tìm kiếm == null
                        || b.getTitle().toLowerCase().contains(search)// Tên sách -> lowerCase -> có chưa (Search)?
                        || author.toLowerCase().contains(search)      // Tên tác giả -> lowerCase -> Có chưa (Search)?
                        || category.toLowerCase().contains(search)    // Category -> lowerCase -> có chứa (Search)?
                        || String.valueOf(b.getBook_id()).contains(search); // Id_book có chứa (Search)?

                boolean alreadySelected = selectedCopies
                        .stream()
                        .anyMatch(c -> c.getCopy_id() == copy.getCopy_id()); //For (Copy_Entity c : selectedCopies.stream())
                if (match && !alreadySelected) {
                    view.getAvailableModel().addRow(new Object[]{
                            copy.getCopy_id(),
                            b.getBook_id(),
                            b.getTitle(),
                            author,
                            category
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi tải danh sách sách: " + ex.getMessage());
        }
    }

    /**
     * Thêm sách đã chọn vào phiếu mượn
     */
    private void addBookToSlip() {
        int row = view.getTblAvailableBooks().getSelectedRow();
        if (row < 0) {
            view.showWarn("Vui lòng chọn sách cần thêm!");
            return;
        }
        if (selectedCopies.size() >= 5) {
            view.showWarn("Đã đạt giới hạn tối đa 5 cuốn sách/phiếu!");
            return;
        }

        try {
            int copyId = (int) view.getAvailableModel().getValueAt(row, 0);

            boolean exists = selectedCopies.stream().anyMatch(c -> c.getCopy_id() == copyId);
            if (exists) {
                view.showWarn("Sách này đã được thêm vào danh sách!");
                return;
            }

            var copyOpt = copyDAO.findById(copyId);
            if (copyOpt.isEmpty()) return;

            Copy_Entity copy = copyOpt.get();
            selectedCopies.add(copy);

            view.getSelectedModel().addRow(new Object[]{
                    view.getAvailableModel().getValueAt(row, 0),
                    view.getAvailableModel().getValueAt(row, 1),
                    view.getAvailableModel().getValueAt(row, 2),
                    view.getAvailableModel().getValueAt(row, 3),
            });

            view.getAvailableModel().removeRow(row);

            view.showInfo("✅ Đã thêm sách vào phiếu mượn");
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi thêm sách: " + ex.getMessage());
        }
    }

    /**
     * Xóa sách khỏi phiếu mượn
     */
    private void removeBookFromSlip() {
        int row = view.getTblSelectedBooks().getSelectedRow();
        if (row < 0) {
            view.showWarn("Vui lòng chọn sách cần xóa!");
            return;
        }

        try {
            int copyId = (int) view.getSelectedModel().getValueAt(row, 0);

            selectedCopies.removeIf(c -> c.getCopy_id() == copyId);
            view.getSelectedModel().removeRow(row);

            loadAvailableBooks(view.getBookSearchText());
            view.showInfo("✅ Đã xóa sách khỏi phiếu mượn");
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi xóa sách: " + ex.getMessage());
        }
    }

    /**
     * Sau khi thêm sách thì lưu lại sách vào phiếu mượn đó
     */
    private void saveSlip() {
        if (currentReader == null) {
            view.showError("Vui lòng chọn độc giả!");
            return;
        }
        if (selectedCopies.isEmpty()) {
            view.showError("Vui lòng chọn ít nhất 1 cuốn sách!");
            return;
        }
        if (accountId <= 0) {
            view.showError("Không xác định tài khoản đăng nhập (accountId). Không thể tạo phiếu mượn!");
            return;
        }

        try {
            List<BorrowSlip_Entity> listbs = borrowDAO.findByIdReader(currentReader.getReader_id());
            boolean hasUnreturned = false;
            for (BorrowSlip_Entity bs : listbs) {
                if (bs.getReturn_date() == null) { hasUnreturned = true; break; }
            }
            if (hasUnreturned) {
                view.showInfo("Bạn có phiếu mượn chưa trả");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi kiểm tra phiếu mượn cũ: " + ex.getMessage());
            return;
        }

        String msg = "Xác nhận tạo phiếu mượn?\n\n" +
                "Độc giả: " + currentReader.getReader_name() + "\n" +
                "Số sách: " + selectedCopies.size() + " cuốn\n" +
                "Ngày mượn: " + view.getBorrowDateText() + "\n" +
                "Hạn trả: " + view.getDueDateText();

        if (view.askConfirm(msg, "Xác nhận") != JOptionPane.YES_OPTION) return;

        try {
            BorrowSlip_Entity borrowSlip = new BorrowSlip_Entity();
            borrowSlip.setReader_id(currentReader.getReader_id());
            borrowSlip.setBorrow_date(Date.valueOf(view.getBorrowDateText()));
            borrowSlip.setDue_date(Date.valueOf(view.getDueDateText()));
            borrowSlip.setState("Borrowing");

            borrowSlip.setAccount_id(accountId);

            borrowDAO.create(borrowSlip);

            int newSlipId = borrowDAO.findAll().get(borrowDAO.findAll().size() - 1).getBorrow_id();

            for (Copy_Entity copy : selectedCopies) {
                Copy_BorrowSlip_Entity cbs = new Copy_BorrowSlip_Entity();
                cbs.setBorrow_id(newSlipId);
                cbs.setCopy_id(copy.getCopy_id());
                copyBorrowDAO.create(cbs);

                copy.setStatus("Borrowed");
                copyDAO.update(copy);
            }

            view.showInfo("✅ Tạo phiếu mượn thành công!\n\nMã phiếu: " + newSlipId +
                    "\nĐộc giả: " + currentReader.getReader_name() +
                    "\nSố sách: " + selectedCopies.size() + " cuốn");

            reset();
        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi lưu phiếu mượn: " + ex.getMessage() +
                    "\n\nGợi ý: Kiểm tra accountId=" + accountId + " có tồn tại trong bảng account không.");
        }
    }

    /**
     * Tải lại trạng thái ban đầu của View
     */
    private void reset() {
        view.setReaderIdText("");
        currentReader = null;
        view.setReaderInfo(null);

        selectedCopies.clear();
        view.clearSelectedBooks();

        view.getCmbBorrowDays().setSelectedItem(14);
        updateDates();

        loadAvailableBooks("");
    }
}
