package com.library.service;

import com.library.model.Account_Entity;

import java.util.Optional;

public interface FINDINFORMATION {
    String FindNameAuthor(int book_id);
    String FindNameCategory(int book_id);
    String FindNamePublisher(int book_id);
    String FindLocation(int book_id);
    double FindCost(int book_id);
    Optional<Account_Entity> findAccountByStaffEmail(String email);
    int countBorrowed();
    int countCopy(int idBook);
    int countBorrowByIdBook(int idBook);
}
