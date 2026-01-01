package com.library.controller;

import com.library.dao.impl.Author_DAO;
import com.library.model.Author_Entity;
import com.library.view.AddAuthorFormView;

import javax.swing.*;
import java.sql.Date;

public class AddAuthorController {

    private final AddAuthorFormView view;
    private final Author_DAO authorDAO = new Author_DAO();

    private final Runnable onAuthorAdded;

    public AddAuthorController(Runnable onAuthorAdded) {
        this.onAuthorAdded = onAuthorAdded;

        this.view = new AddAuthorFormView();
        bindEvents();

        view.setVisible(true);
    }

    private void bindEvents() {
        view.getBtnReset().addActionListener(e -> view.resetForm());
        view.getBtnSave().addActionListener(e -> saveAuthor());
        view.getBtnCancel().addActionListener(e -> cancelForm());
    }

    private void saveAuthor() {
        String authorName = view.getAuthorName();
        String birthYearStr = view.getBirthYearText();
        String country = view.getCountry();
        String deathYearStr = view.getDeathYearText();
        String biography = view.getBiography();

        // Validate required
        if (authorName.isEmpty()) {
            view.showError("Vui lòng nhập tên tác giả!");
            view.focusAuthorName();
            return;
        }
        if (country.isEmpty()) {
            view.showError("Vui lòng nhập quốc gia!");
            view.focusCountry();
            return;
        }
        if (birthYearStr.isEmpty()) {
            view.showError("Vui lòng nhập năm sinh!");
            view.focusBirthYear();
            return;
        }

        // Validate birth year
        int birthYear;
        try {
            birthYear = Integer.parseInt(birthYearStr);
            if (birthYear < 1000 || birthYear > 2100) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            view.showError("Năm sinh phải là số hợp lệ (1000-2100)!");
            view.focusBirthYear();
            return;
        }

        // Validate death year (optional)
        Integer deathYear = null;
        if (!deathYearStr.isEmpty()) {
            try {
                int d = Integer.parseInt(deathYearStr);
                if (d < birthYear) {
                    view.showError("Năm mất phải lớn hơn hoặc bằng năm sinh!");
                    view.focusDeathYear();
                    return;
                }
                if (d > 2100) throw new NumberFormatException();
                deathYear = d;
            } catch (NumberFormatException e) {
                view.showError("Năm mất phải là số hợp lệ!");
                view.focusDeathYear();
                return;
            }
        }

        // Confirm
        boolean ok = view.confirm("Bạn có chắc chắn muốn thêm tác giả này?", "Xác nhận");
        if (!ok) return;

        // Save DB
        try {
            Author_Entity newAuthor = new Author_Entity();
            newAuthor.setAuthor_name(authorName);
            newAuthor.setCountry(country);
            newAuthor.setBirth_date(Date.valueOf(birthYear + "-01-01"));

            if (deathYear == null) newAuthor.setDeath_date(null);
            else newAuthor.setDeath_date(Date.valueOf(deathYear + "-01-01"));

            if (!biography.isEmpty()) newAuthor.setDescription(biography);

            authorDAO.create(newAuthor);

            view.showInfo("Đã thêm tác giả '" + authorName + "' thành công!");

            if (onAuthorAdded != null) onAuthorAdded.run();

            boolean addMore = view.confirm("Bạn có muốn thêm tác giả khác không?", "Thêm tác giả khác");
            if (addMore) {
                view.resetForm();
            } else {
                view.dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            view.showError("Lỗi khi thêm tác giả: " + ex.getMessage());
        }
    }

    private void cancelForm() {
        if (view.hasAnyData()) {
            boolean ok = view.confirmWarning(
                    "Bạn có chắc chắn muốn hủy? Dữ liệu đã nhập sẽ bị mất.",
                    "Xác nhận hủy"
            );
            if (ok) view.dispose();
        } else {
            view.dispose();
        }
    }

    // Main test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            new AddAuthorController(() -> System.out.println("Author added - refresh list here!"));
        });
    }
}
