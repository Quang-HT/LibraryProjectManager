package com.library.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class copy_Entity {
    private int copy_id;
    private String status;
    private int cost;
    private int book_id;
    private String location; 

    public copy_Entity() {
    }

    public copy_Entity(String status, int cost, int book_id, String location) {
        this.status = status;
        this.cost = cost;
        this.book_id = book_id;
        this.location = location;
    }

}
