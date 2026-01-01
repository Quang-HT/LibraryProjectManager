package com.library.model.display;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowSlipSummary {
    public String id;
    public String readerId;
    public String readerName;
    public String borrowDate; // string display
    public String dueDate;    // string display
    public String returnDate; // string display / null

    public int overdueDays;
    public double penaltyAmount;
    public boolean penaltyPaid;

    public BorrowSlipSummary(String id, String readerId, String readerName,
                             String borrowDate, String dueDate, String returnDate,
                             int overdueDays, double penaltyAmount, boolean penaltyPaid) {
        this.id = id;
        this.readerId = readerId;
        this.readerName = readerName;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.overdueDays = overdueDays;
        this.penaltyAmount = penaltyAmount;
        this.penaltyPaid = penaltyPaid;
    }
}
