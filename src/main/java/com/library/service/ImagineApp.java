package com.library.service;

import java.awt.*;

import static com.library.view.LoginView.PRIMARY_BLUE;
import static java.awt.Color.WHITE;

public class ImagineApp {
    private static final Color PRIMARY_BLUE = new Color(25, 118, 210);

    public static Image createBookIcon() {
        int size = 32;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(PRIMARY_BLUE);
        g2.fillRoundRect(4, 2, 24, 28, 4, 4);
        g2.setColor(WHITE);
        g2.fillRect(8, 6, 16, 2);
        g2.fillRect(8, 12, 16, 2);
        g2.fillRect(8, 18, 12, 2);
        g2.dispose();
        return img;
    }
}
