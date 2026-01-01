package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author_Book_Entity {
    private int author_id;
    private int book_id;

    public Author_Book_Entity() {
    }

    public Author_Book_Entity(int author_id, int book_id) {
        this.author_id = author_id;
        this.book_id = book_id;
    }
}
