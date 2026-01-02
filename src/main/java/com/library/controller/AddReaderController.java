package com.library.controller;

import com.library.dao.impl.Reader_DAO;
import com.library.model.Reader_Entity;
import com.library.view.AddReaderFormView;

import javax.swing.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddReaderController {

    private final AddReaderFormView view;
    private final Runnable onReaderAdded;

    public AddReaderController() {
        this(null); // Gọi đến Constructer AddReaderController(Runnable onReaderAdded) và truyền cho onReaderAdded = null
    }

    /**
     * Hàm Constructer thực thi công việc theo thứ tự
     * @param onReaderAdded
     * Một biến để thực thi câu lệnh sau khi hàm Constructer xong việc (Runnable)
     */
    public AddReaderController(Runnable onReaderAdded) {
        this.view = new AddReaderFormView();
        this.onReaderAdded = onReaderAdded;

        bindEvents();
        view.setVisible(true);
    }

    /**
     * Đăng ký các sự kiện với View
     */
    private void bindEvents() {
        view.getBtnSave().addActionListener(e -> saveReader());
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnCancel().addActionListener(e -> cancelForm());
    }

    /**
     * Lấy thông tin tư người dùng nhập trên View
     * Kiểm tra + lưu lại thông tin về người đọc
     */
    private void saveReader() {
        if (view.getFullName().isEmpty()) {
            view.showError("Vui lòng nhập họ và tên!");
            return;
        }

        if (view.getPhone().isEmpty()) {
            view.showError("Vui lòng nhập số điện thoại!");
            return;
        }

        String phone = view.getPhone();
        if (!phone.matches("^0\\d{9}$")) {
            view.showError("Số điện thoại không hợp lệ! Phải có 10 số và bắt đầu bằng 0.");
            return;
        }

        if (view.getAddress().isEmpty()) {
            view.showError("Vui lòng nhập địa chỉ!");
            return;
        }

        String email = view.getEmail();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            view.showError("Email không hợp lệ!");
            return;
        }

        if (view.getGenderIndex() == 0) {
            view.showError("Vui lòng chọn giới tính!");
            return;
        }

        Date birthDateSql = null;
        String dobText = view.getDobText();
        if (!dobText.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date utilDob = sdf.parse(dobText);
                birthDateSql = new Date(utilDob.getTime());
            } catch (ParseException ex) {
                view.showError("Ngày sinh không đúng định dạng! Vui lòng nhập theo dd/MM/yyyy");
                return;
            }
        }

        if (!view.confirm("Bạn có chắc chắn muốn đăng ký độc giả này?")) {
            return;
        }

        try {
            Reader_Entity re = new Reader_Entity();
            re.setReader_name(view.getFullName());
            re.setPhone(view.getPhone());
            re.setAddress(view.getAddress());
            re.setEmail(email);
            re.setCccd(view.getCccd());

            re.setRegistered_date(new Date(System.currentTimeMillis()));
            re.setBirth_date(birthDateSql);

            new Reader_DAO().create(re);

            String message = "✅ Đăng ký độc giả thành công!\n\n" +
                    "Họ và tên: " + view.getFullName() + "\n" +
                    "Số điện thoại: " + view.getPhone() + "\n" +
                    "Email: " + (email.isEmpty() ? "Không có" : email) + "\n" +
                    "Địa chỉ: " + view.getAddress() + "\n" +
                    "CCCD: " + (view.getCccd().isEmpty() ? "Không có" : view.getCccd()) + "\n" +
                    "Giới tính: " + view.getGenderText() + "\n" +
                    "Ngày sinh: " + (dobText.isEmpty() ? "Không có" : dobText) + "\n" +
                    "Ngày đăng ký: " + new Date(System.currentTimeMillis());

            view.showInfo(message);

            if (onReaderAdded != null) onReaderAdded.run();

            boolean addMore = view.confirm("Bạn có muốn đăng ký thêm độc giả khác không?");
            if (addMore) view.clearFormNoConfirm();
            else view.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi thêm độc giả: " + ex.getMessage());
        }
    }

    /**
     * Thực thi xóa toàn bộ dữ liệu có trên form nhập dữ liệu
     */
    private void clearForm() {
        boolean ok = view.confirm("Bạn có chắc chắn muốn xóa toàn bộ thông tin đã nhập?");
        if (ok) view.clearFormNoConfirm();
    }

    /**
     * Hủy thêm người đọc
     */
    private void cancelForm() {
        boolean ok = view.confirm("Bạn có chắc chắn muốn hủy? Dữ liệu chưa lưu sẽ bị mất.");
        if (ok) view.dispose();
    }
}