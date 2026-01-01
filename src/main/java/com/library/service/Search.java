package com.library.service;

import com.library.model.Book_Entity;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Search {

    // Chuẩn hóa chuỗi: bỏ dấu, viết thường, trim
    public static String normalize(String s) {
        if (s == null) return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // bỏ dấu
        return normalized.toLowerCase().trim();
    }

    // Hàm tính khoảng cách Levenshtein (giữ y nguyên của bạn)
    public static int levenshtein(String a, String b) {
        int n = a.length();
        int m = b.length();

        if (n == 0) return m;
        if (m == 0) return n;

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = b.charAt(j - 1);
                int cost = (ca == cb) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,    // xoá
                                dp[i][j - 1] + 1     // chèn
                        ),
                        dp[i - 1][j - 1] + cost   // thay
                );
            }
        }

        return dp[n][m];
    }

    // Tìm sách gần đúng theo tên
    public List<Book_Entity> search(
            String keyword,
            List<Book_Entity> allBooks,
            int maxResult
    ) {
        String normalizedKeyword = normalize(keyword);

        if (normalizedKeyword.isEmpty()) {
            return Collections.emptyList();
        }

        // Lớp tạm để lưu (sách, điểm giống)
        class ScoredBook {
            Book_Entity book;
            double score; // càng nhỏ càng tốt

            ScoredBook(Book_Entity book, double score) {
                this.book = book;
                this.score = score;
            }
        }

        List<ScoredBook> scored = new ArrayList<>();

        for (Book_Entity book : allBooks) {
            String name = normalize(book.getTitle());

            double score;

            // 1️⃣ Nếu tên sách chứa nguyên cụm từ khóa → cho score = 0 (ưu tiên nhất)
            if (name.contains(normalizedKeyword)) {
                score = 0.0;
            } else {
                // 2️⃣ Ngược lại, tính Levenshtein và CHUẨN HÓA
                int dist = levenshtein(normalizedKeyword, name);
                int maxLen = Math.max(normalizedKeyword.length(), name.length());
                score = (double) dist / maxLen; // chuẩn hóa về [0,1]
            }

            scored.add(new ScoredBook(book, score));
        }

        // Có thể lọc bỏ những sách quá xa (tuỳ bạn chỉnh ngưỡng)
        double MAX_SCORE = 0.8; // chỉ lấy các sách có độ giống tương đối
        return scored.stream()
                .filter(sb -> sb.score <= MAX_SCORE)
                .sorted(Comparator.comparingDouble(sb -> sb.score)) // score nhỏ trước
                .limit(maxResult)
                .map(sb -> sb.book)
                .collect(Collectors.toList());
    }
}
