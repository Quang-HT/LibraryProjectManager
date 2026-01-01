package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Category_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Category_DAO extends AbstractDAO<Category_Entity, Integer> {

    @Override
    protected Category_Entity mapRow(ResultSet rs) throws SQLException {
        Category_Entity Category = new Category_Entity();
        Category.setCategory_id(rs.getInt("category_id"));
        Category.setCategory_name(rs.getString("category_name"));
        Category.setDescription(rs.getString("description"));
        return Category;
    }

    @Override
    public List<Category_Entity> findAll() {
        return query("SELECT * FROM Category");
    }

    @Override
    public void create(Category_Entity entity) {
        String sql = "INSERT INTO Category (category_name, description) VALUES (?, ?)";
        executeUpdate(sql, entity.getCategory_name(), entity.getDescription());
    }

    @Override
    public void update(Category_Entity entity) {
        String sql = "UPDATE Category SET category_name = ?, description = ? WHERE category_id = ?";
        executeUpdate(sql, entity.getCategory_name(), entity.getDescription(), entity.getCategory_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Category WHERE category_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Category_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Category WHERE category_id = ?", id);
    }
}
