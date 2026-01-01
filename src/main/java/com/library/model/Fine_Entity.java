package com.library.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Fine_Entity {
    private int fine_id;
    private Double amount;
    private String status;
    private String payment_method;
    private Timestamp created_at;
    private String reason;
    private int borrow_id;
    private int account_id;

    public Fine_Entity() {
    }

    public Fine_Entity(int fine_id, Double amount, String status,
                            String payment_method, Timestamp created_at,
                            String reason, int borrow_id, int account_id) {
        this.fine_id = fine_id;
        this.amount = amount;
        this.status = status;
        this.payment_method = payment_method;
        this.created_at = created_at;
        this.reason = reason;
        this.borrow_id = borrow_id;
        this.account_id = account_id;
    }
}
