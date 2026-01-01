package com.library.service;

public interface FINDINFORMATION {
    String FindNameAuthor(int book_id);
    String FindNameCategory(int book_id);
    String FindNamePublisher(int book_id);
    String FindLocation(int book_id);
    double FindCost(int book_id);
}
