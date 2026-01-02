package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.model.Account_Entity;
import com.library.service.SessionID;
import com.library.view.HomeView;
import com.library.view.LoginView;

import javax.swing.*;
import java.util.Optional;


public class LoginController {

    /**
     * Gọi ra view để:
     * Đưa dữ liệu từ Controller lên view
     * Lấy dữ liệu từ view về Controller
     */
    private LoginView view;
    private Account_DAO accountDAO;

    /**
     * Hàm Controller gọi thứ tự lần lượt các danh sách hàm excute theo thứ tự
     */
    public LoginController() {
        view = new LoginView();
        view.setController(this);

        accountDAO = new Account_DAO();
        view.setVisible(true);
    }

    /**
     * Xử lý sự kiện đăng nhập
     * 1. Kiểm tra người dùng đã nhập chưa
     * 2. Tìm kiếm tài khoản từ DB
     * 3. Kiểm tra xem tài khoản còn quyền hoạt động không
     * 4. Xử lý đăng nhập thành công
     */
    public void handleLogin() {
        String username = view.getUsername();
        char[] passwordChars = view.getPassword();
        String password = new String(passwordChars);

        if (!validateInput(username, password)) {
            return;
        }

        Optional<Account_Entity> optionalAccount = accountDAO.findByName(username);

        if (optionalAccount.isEmpty()) {
            handleLoginFailure("Tên đăng nhập hoặc mật khẩu không đúng!");
            return;
        }

        Account_Entity account = optionalAccount.get();

        if (!password.equals(account.getPassword())) {
            handleLoginFailure("Tên đăng nhập hoặc mật khẩu không đúng!");
            return;
        }

        if (!isAccountActive(account)) {
            view.showMessage(
                    "Tài khoản đang bị khóa hoạt động, hãy liên hệ admin!",
                    "Thông báo",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        handleLoginSuccess(account);
    }

    /**
     * Validate dữ liệu đầu vào
     * Kiểm tra xem người dùng đã nhập thông tin chưa
     * Nhập rồi trả về true
     * Chưa nhập trả về false
     */
    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showMessage(
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }

    /**
     * Kiểm tra tài khoản có đang hoạt động không
     */
    private boolean isAccountActive(Account_Entity account) {

        return account.getIs_active() == 1; // Bằng 1 là hoạt động || Bằng 0 là khóa hoạt động
    }

    /**
     * Xử lý khi đăng nhập thất bại
     * Hiển thị thông báo lỗi và xóa mật khẩu tại ô đăng nhập
     */
    private void handleLoginFailure(String message) {
        view.showMessage(
                message,
                "Lỗi",
                JOptionPane.ERROR_MESSAGE
        );
        view.clearPassword();
    }

    /**
     * Xử lý khi đăng nhập thành công
     * Lưu Session (Lưu id nhân viên)
     */
    private void handleLoginSuccess(Account_Entity account) {
        SessionID.setSession(account.getAccount_id());

        view.closeFrame();
        new HomeController(new HomeView());
    }

    /**
     * Xử lý khi người dùng click "Quên mật khẩu"
     */
    public void handleForgotPassword() {
        new ForgotPasswordController();
    }

    /**
     * Xử lý khi người dùng click "Giao Diện Độc Giả"
     */
    public void handleOpenReaderInterface() {
        new ReaderController();
        view.dispose();
    }
}