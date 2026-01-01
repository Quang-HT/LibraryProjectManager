package com.library.model.display;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderRow {
    private int id;
    private String fullName;
    private String cccd;
    private String phoneNumber;
    private String email;
    private String address;
    private String registrationDate;
    private String status;

    public ReaderRow(){}
    public ReaderRow(int id, String fullName, String cccd, String phoneNumber, String email,
                     String address, String registrationDate, String status) {
        this.id = id;
        this.fullName = fullName;
        this.cccd = cccd;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.registrationDate = registrationDate;
        this.status = status;
    }
}