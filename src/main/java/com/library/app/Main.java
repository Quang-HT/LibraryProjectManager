package com.library.app;

import com.library.controller.LoginController;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginController::new);
    }
}