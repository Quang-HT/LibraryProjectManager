package com.library.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category_Entity {
    private int category_id;
    private String category_name;
    private String description;

    public Category_Entity() {
    }

    public Category_Entity(String category_name, String description) {
        this.category_name = category_name;
        this.description = description;
    }

}
