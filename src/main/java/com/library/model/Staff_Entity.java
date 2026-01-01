package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Staff_Entity {
    private int staff_id;
    private String staff_name;
    private String phone;
    private String position;
    private String email;
    private  byte is_working;

    public Staff_Entity() {
    }

    public Staff_Entity( String staff_name, String phone, String position) {
        this.staff_name = staff_name;
        this.phone = phone;
        this.position = position;
    }
}
