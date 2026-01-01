package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Publisher_Entity {
    private int publisher_id;
    private String publisher_name;
    private String address;
    private String phone;

    public Publisher_Entity() {
    }

    public Publisher_Entity(int publisher_id, String publisher_name, String address, String phone) {
        this.publisher_id = publisher_id;
        this.publisher_name = publisher_name;
        this.address = address;
        this.phone = phone;
    }

}
