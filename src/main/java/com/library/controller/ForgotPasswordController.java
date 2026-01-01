package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.model.Account_Entity;
import com.library.service.FIND;
import com.library.service.SendEmail;
import com.library.view.ForgotPasswordView;

import javax.swing.*;
import java.util.Optional;
import java.util.Random;

public class ForgotPasswordController {

    private final ForgotPasswordView view;
    private final Account_DAO accountDAO;

    private String currentCode;
    private int accountId;

    private boolean emailVerified = false;
    private boolean codeSent = false;
    private boolean codeVerified = false;

    /**
     * Hàm Controller tạo danh sách công việc thực hiện theo thứ tự
     */
    public ForgotPasswordController() {
        this.view = new ForgotPasswordView();
        this.accountDAO = new Account_DAO();

        bindEvents();
        view.setVisible(true);
    }

    /**
     * Thêm các hàm ActionListener để gọi ra các sự kiện
     */
    private void bindEvents() {
        view.getBtnVerifyEmail().addActionListener(e -> verifyEmail());
        view.getBtnSendCode().addActionListener(e -> sendVerificationCode());
        view.getBtnVerifyCode().addActionListener(e -> verifyCode());
        view.getBtnChangePassword().addActionListener(e -> changePassword());
        view.getBtnCancel().addActionListener(e -> view.dispose());
    }

    /**
     * Xác thực thông tin Email nhập vào
     * Kiểm tra Email này có trong Database không
     * Nếu có thì gửi mã code xác nhận
     */
    private void verifyEmail() {
        String email = view.getTxtEmail().getText().trim();

        if (email.isEmpty()) {
            view.showError("Vui lòng nhập địa chỉ email!");
            view.getTxtEmail().requestFocus();
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) { // Sử dụng Regex để kiểm tra đầu vào của Email
            view.showError("Email không đúng định dạng!");
            view.getTxtEmail().requestFocus();
            return;
        }

        Optional<Account_Entity> op = new FIND().findAccountByStaffEmail(email);
        if (op.isEmpty()) {
            view.showError("Không tìm thấy tài khoản với email này!");
            return;
        }

        Account_Entity account = op.get();
        accountId = account.getAccount_id();
        emailVerified = true;

        view.lockEmailVerified();
        view.showSuccess("Email hợp lệ! Vui lòng nhấn 'Gửi mã' để nhận mã xác nhận.");
        view.getBtnSendCode().requestFocus(); //Trỏ con chuột tới nút "Send code - Gửi mã" để có thể ấn Enter
    }

    /**
     * Gửi mã code để xác nhận Email
     */
    private void sendVerificationCode() {
        if (!emailVerified) {
            view.showError("Vui lòng xác thực email trước!");
            return;
        }

        String email = view.getTxtEmail().getText().trim();
        currentCode = String.format("%06d", new Random().nextInt(1_000_000)); // Tạo mã code random 6 chữ số
        codeSent = true;

        // Gọi tới Service gửi Email
        new SendEmail().sendEmailTo(email, currentCode);

        view.markCodeSent(); // View xác nhận đã gửi
        view.showSuccess("Mã xác nhận đã được gửi đến email của bạn!");
        view.getTxtCode().requestFocus();

        // Cho phép gửi lại code sau 60s
        Timer timer = new Timer(60000, e -> view.enableResendCode());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Hàm xác nhận code người dùng nhập vào
     * So sách code đã gửi && code người dùng nhập
     */
    private void verifyCode() {
        if (!codeSent) {
            view.showError("Vui lòng nhấn 'Gửi mã' để nhận mã xác nhận!");
            return;
        }

        String inputCode = view.getTxtCode().getText().trim();
        if (inputCode.isEmpty()) {
            view.showError("Vui lòng nhập mã xác nhận!");
            view.getTxtCode().requestFocus();
            return;
        }

        if (!inputCode.matches("\\d{6}")) {
            view.showError("Mã xác nhận phải là 6 chữ số!");
            view.getTxtCode().requestFocus();
            return;
        }

        if (!inputCode.equals(currentCode)) {
            view.showError("Mã xác nhận không chính xác!");
            view.getTxtCode().requestFocus();
            return;
        }

        codeVerified = true;
        view.lockCodeVerifiedEnableStep3();
        view.showSuccess("Mã xác nhận chính xác! Bây giờ bạn có thể tạo mật khẩu mới.");
        view.getTxtNewPassword().requestFocus();
    }

    /**
     * Hàm thay đổi mặt khẩu sau khi xác nhận được người dùng
     */
    private void changePassword() {
        if (!codeVerified) {
            view.showError("Vui lòng xác nhận mã OTP trước!");
            return;
        }

        String newPass = new String(view.getTxtNewPassword().getPassword());
        String confirmPass = new String(view.getTxtConfirmPassword().getPassword());

        if (newPass.isEmpty()) {
            view.showError("Vui lòng nhập mật khẩu mới!");
            view.getTxtNewPassword().requestFocus();
            return;
        }

        if (newPass.length() < 6) {
            view.showError("Mật khẩu phải có ít nhất 6 ký tự!");
            view.getTxtNewPassword().requestFocus();
            return;
        }

        if (confirmPass.isEmpty()) {
            view.showError("Vui lòng xác nhận mật khẩu!");
            view.getTxtConfirmPassword().requestFocus();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            view.showError("Mật khẩu xác nhận không khớp!");
            view.getTxtConfirmPassword().selectAll();
            view.getTxtConfirmPassword().requestFocus();
            return;
        }

        Optional<Account_Entity> op = accountDAO.findById(accountId);
        if (op.isEmpty()) {
            view.showError("Không tìm thấy tài khoản!");
            return;
        }

        Account_Entity account = op.get();
        account.setPassword(confirmPass);
        accountDAO.update(account);

        view.markStep3Done();
        view.showSuccess("✅ Đổi mật khẩu thành công!\n\nBạn có thể đăng nhập với mật khẩu mới.");
        view.dispose();
    }
}
