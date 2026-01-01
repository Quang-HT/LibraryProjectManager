package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Copy_BorrowSlip_Entity {
    private int borrow_id;
    private int copy_id;

    public Copy_BorrowSlip_Entity() {
    }

    public Copy_BorrowSlip_Entity(int borrow_id, int copy_id) {
        this.borrow_id = borrow_id;
        this.copy_id = copy_id;
    }

}
