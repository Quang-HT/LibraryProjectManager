package com.library.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Author_Entity {
    private int author_id;
    private String author_name;
    private Date birth_date;
    private String description;
    private Date death_date;
    private String country;

    public Author_Entity() {
    }

    public Author_Entity(int author_id, String author_name, Date birth_date, String description, Date birthDeath) {
        this.author_name = author_name;
        this.birth_date = birth_date;
        this.description = description;
        this.death_date = birthDeath;
    }
}
