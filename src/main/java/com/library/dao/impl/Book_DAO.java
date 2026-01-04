package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Author_Book_Entity;
import com.library.model.Book_Entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Book_DAO extends AbstractDAO<Book_Entity, Integer> {

    @Override
    protected Book_Entity mapRow(ResultSet rs) throws SQLException {
        Book_Entity b = new Book_Entity();
        b.setBook_id(rs.getInt("book_id"));
        b.setTitle(rs.getString("title"));
        b.setPublishYear((Date) rs.getObject("publish_year"));
        b.setDescription(rs.getString("description"));
        b.setPublisher_id((Integer) rs.getObject("publisher_id"));
        b.setLocation(rs.getString("location"));
        return b;
    }

    @Override
    public List<Book_Entity> findAll() {
        return query("SELECT * FROM Book");
    }

    @Override
    public void create(Book_Entity entity) {
        String sql = """
            INSERT INTO Book (title, publish_year, description, publisher_id, location)
            VALUES (?, ?, ?, ?, ?)
        """;

        executeUpdate(sql,
                entity.getTitle(),
                entity.getPublish_year(),
                entity.getDescription(),
                entity.getPublisher_id(),
                entity.getLocation()
        );
    }

    @Override
    public void update(Book_Entity entity) {
        String sql = """
            UPDATE Book
            SET title = ?, publish_year = ?, description = ?, publisher_id = ?, location = ?
            WHERE book_id = ?
        """;

        executeUpdate(sql,
                entity.getTitle(),
                entity.getPublish_year(),
                entity.getDescription(),
                entity.getPublisher_id(),
                entity.getLocation(),
                entity.getBook_id()        // đúng vị trí cuối cùng
        );
    }

    @Override
    public void delete(Integer id) {
        executeUpdate("DELETE FROM Book WHERE book_id = ?", id);
    }

    @Override
    public Optional<Book_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Book WHERE book_id = ?", id);
    }

    public Optional<Book_Entity> findByName(String name) {
        return querySingle("SELECT * FROM Book WHERE title = ?", name);
    }


    public List<Book_Entity> findAuthorsById(int id){
        List<Author_Book_Entity> listAB = new Author_Book_DAO().findAuthors_BookByAuthorId(id);
        List<Book_Entity> listb = new ArrayList<>();
        for(Author_Book_Entity e : listAB){
            Optional<Book_Entity> op = new Book_DAO().findById(e.getBook_id());
            Book_Entity book = op.get();
            listb.add(book);
        }
        return listb;
    }
}
