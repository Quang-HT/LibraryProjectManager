package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account_Entity {
    private int account_id;
    private String username;
    private String password;

    private int staff_id;
    private  byte is_active;

    public Account_Entity() {
    }

    public Account_Entity(String username, String password,int staff_id,byte is_active) {
        this.username = username;
        this.password = password;
        this.staff_id = staff_id;
        this.is_active =is_active;
    }
}
