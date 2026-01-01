package com.library.model.display;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BorrowSlipVM {
    String id;
    String readerId;
    String readerName;

    int bookCount;

    String borrowDateDb;
    String dueDateDb;
    String returnDateDb;

    int overdueDays;
    double penaltyAmount;

    boolean penaltyPaid;
    String paymentDateDb;
    public BorrowSlipVM(){}

    public BorrowSlipVM(String id, String readerId, String readerName, int bookCount, String borrowDateDb, String dueDateDb, String returnDateDb) {
        this.id = id;
        this.readerId = readerId;
        this.readerName = readerName;
        this.bookCount = bookCount;
        this.borrowDateDb = borrowDateDb;
        this.dueDateDb = dueDateDb;
        this.returnDateDb = returnDateDb;
    }
}
