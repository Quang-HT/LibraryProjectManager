package com.library.model.display;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDisplayModel {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private int year;
    private int cost;
    private String location;
    private int borrowCount;
    private String publisher;
    private int copyCount;
    private String description;

    public BookDisplayModel(){}
    public BookDisplayModel(int bookId, String title, String author, String category,
                            int year, int cost, String location, int borrowCount) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.year = year;
        this.cost = cost;
        this.location = location;
        this.borrowCount = borrowCount;
    }
}