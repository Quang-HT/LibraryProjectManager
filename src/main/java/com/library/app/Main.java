package com.library.app;

import com.library.controller.LoginController;
import javax.swing.SwingUtilities;

/**
 * Main class - Entry point của ứng dụng
 */
public class Main {
    public static void main(String[] args) {
        // Chạy UI trên Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Khởi tạo Controller - Controller sẽ tự tạo View
            new LoginController();
        });
    }
}