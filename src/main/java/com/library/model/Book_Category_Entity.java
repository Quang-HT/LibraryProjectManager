package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book_Category_Entity {
    private int book_id;
    private int category_id;

    public Book_Category_Entity() {
    }

    public Book_Category_Entity(int book_id, int category_id) {
        this.book_id = book_id;
        this.category_id = category_id;
    }
}
