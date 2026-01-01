package com.library.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class BorrowSlip_Entity {
    private int borrow_id;
    private Date borrow_date;
    private Date return_date;
    private Date due_date;
    private String state;
    private int account_id;
    private int reader_id;

    public BorrowSlip_Entity() {
    }

    public BorrowSlip_Entity(Date borrow_date, Date return_date, Date due_date,
                            String state, int account_id, int reader_id) {
        this.borrow_date = borrow_date;
        this.return_date = return_date;
        this.due_date = due_date;
        this.state = state;
        this.account_id = account_id;
        this.reader_id = reader_id;
    }


}
