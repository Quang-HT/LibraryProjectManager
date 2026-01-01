package com.library.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Reader_Entity {
    private int reader_id;
    private String reader_name;
    private Date birth_date;
    private String email;
    private String phone;
    private Date registered_date;
    private String cccd;
    private String address;

    public Reader_Entity() {
    }

    public Reader_Entity(String reader_name, Date birth_date, String email,
                         String phone, Date registered_date, String cccd, String address) {
        this.reader_name = reader_name;
        this.birth_date = birth_date;
        this.email = email;
        this.phone = phone;
        this.registered_date = registered_date;
        this.cccd = cccd;
        this.address = address;
    }

}
