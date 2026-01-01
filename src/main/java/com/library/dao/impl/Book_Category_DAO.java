package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Book_Category_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Book_Category_DAO extends AbstractDAO<Book_Category_Entity, Integer> {

    @Override
    protected Book_Category_Entity mapRow(ResultSet rs) throws SQLException {
        Book_Category_Entity Book_Category = new Book_Category_Entity();
        Book_Category.setBook_id(rs.getInt("book_id"));
        Book_Category.setCategory_id(rs.getInt("category_id"));
        return Book_Category;
    }

    @Override
    public List<Book_Category_Entity> findAll() {
        return query("SELECT * FROM Book_Category");
    }

    @Override
    public void create(Book_Category_Entity entity) {
        String sql = "INSERT INTO Book_Category (book_id, category_id) VALUES (?, ?)";
        executeUpdate(sql, entity.getBook_id(), entity.getCategory_id());
    }

    @Override
    public void update(Book_Category_Entity entity) {
        String sql = "UPDATE Book_Category SET book_id = ?, category_id = ? WHERE book_id = ? AND category_id = ?";
        executeUpdate(sql, entity.getBook_id(), entity.getCategory_id(), entity.getBook_id(), entity.getCategory_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Book_Category WHERE book_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Book_Category_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Book_Category WHERE book_id = ?", id);
    }
}
