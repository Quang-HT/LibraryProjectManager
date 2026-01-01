package com.library.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Book_Entity {

    private int book_id;
    private String title;
    private Integer publish_year;    // YEAR -> Integer
    private String description;
    private Integer publisher_id;
    private String location;

    public Book_Entity() {}

    public Book_Entity(String location, Integer publisher_id, String description, Integer publish_year, String title, int book_id) {
        this.location = location;
        this.publisher_id = publisher_id;
        this.description = description;
        this.publish_year = publish_year;
        this.title = title;
        this.book_id = book_id;
    }

    public void setPublishYear(Date publish_year) {
        this.publish_year = publish_year.toLocalDate().getYear();
    }

}
