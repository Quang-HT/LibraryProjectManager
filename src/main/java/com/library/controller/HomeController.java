package com.library.controller;

import com.library.dao.impl.Account_DAO;
import com.library.dao.impl.Staff_DAO;
import com.library.model.Account_Entity;
import com.library.service.SessionID;
import com.library.view.*;

import javax.swing.*;
import java.util.Optional;

public class HomeController {
    private final HomeView homeView;

    /**
     * @param homeView
     * Thiết lập thứ tự hàm cần thiết
     * Mở giao diện + Lấy Session đăng nhập
     */
    public HomeController(HomeView homeView) {
        this.homeView = homeView;
        getSessionAccount();
        this.homeView.setVisible(true);
    }

    /**
     * Lấy ID nhân viên trong Session trong Service
     * Hàm lấy về thông tin nhân viên đăng nhập
     */
    public void getSessionAccount(){
        try {
            int id = SessionID.getCurrentAccountId();
            System.out.println(id);
            if (id == 0) {
                return;
            }

            Optional<Account_Entity> accOpt = new Account_DAO().findById(id);
            if (accOpt.isPresent()) {
                homeView.setCurrentAccount(accOpt.get());
                setPermission();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Kiểm tra Permission
     * Nếu là "Manager" cho phép thêm quyền chỉnh sửa nhân viên và tài khoản
     */
    public void setPermission(){
        String position = new Staff_DAO().findById(this.homeView.getCurrentAccount().getStaff_id()).get().getPosition();
        if(position.equals("Manager")){
            this.homeView.setManager(true);
        }
    }

    /**
     * Tạo View để tạo phiếu mượn mới
     */
    public void createNewBorrowSlip() {
        new AddBorrowSlipController(SessionID.getCurrentAccountId());
    }

    /**
     * Tạo View để tạo đầu sách mới
     */
    public void createNewBook() {
        new AddBookController(new AddBookFormView());
    }

    /**
     * Tạo View để tạo Độc giả mới
     */
    public void createNewReader() {
        new AddReaderController();
    }

    /**
     * Tạo View để tạo Tác giả mới
     */
    public void createNewAuthor() {
        new AddAuthorController(() -> {
            //TODO: Tạo thêm refresh danh sách (Runnable)
        });
    }

    /**
     * Tạo View để tạo Nhân viên mới
     */
    public void createNewStaff() {
        new AddStaffController();
    }

    /**
     * Tạo View để tạo Tài khoản mới
     */
    public void createNewAccount() {
        new AddAccountController();
    }

    /**
     * Hàm đăng xuất tài khoản
     * Xét Session = null (AccountSession_Id = 0)
     */
    public void logout() {
        if (JOptionPane.showConfirmDialog(this.homeView, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            SessionID.setSession(0);
            new LoginView().setVisible(true);
            this.homeView.dispose();
        }
    }

    /**
     * Mở View xem thông tin Tài khoản đăng đăng nhập
     */
    public void openAccountInfo() {
        if (this.homeView.getCurrentAccount() != null) {
            new AccountDetailController(this.homeView.getCurrentAccount().getAccount_id(), null);
        }
    }

    /**
     * Mở View để mở "Quản lý đầu sách"
     */
    public void openBookManagement() {
        new BookManagerController();
        this.homeView.dispose();
    }

    /**
     * Mở View để mở "Quản lý Tác giả"
     */
    public void openAuthorManagement() {
        new AuthorManagerController(new AuthorListView());
        this.homeView.dispose();
    }

    /**
     * Mở View để mở quản lý Độc giả
     */
    public void openReaderManagement() {
        new com.library.controller.ReaderManagementController();
        this.homeView.dispose();
    }

    /**
     * Mở View để mở "Quản lý Phiếu mượn - phạt"
     */
    public void openBorrowManagement() {
        new BorrowManagementController();
        this.homeView.dispose();
    }

    /**
     * Mở View để mở "Quản lý nhân viên"
     */
    public void openStaffManagement() {
        new StaffManagementController();
        this.homeView.dispose();
    }

    /**
     * Mở View để mở "Quản lý tài khoản"
     */
    public void openAccountManagement() {
        new AccountManagementController();
        this.homeView.dispose();
    }
}
