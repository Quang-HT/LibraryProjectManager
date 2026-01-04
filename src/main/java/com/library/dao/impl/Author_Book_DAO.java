package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Author_Book_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Author_Book_DAO extends AbstractDAO<Author_Book_Entity, Integer> {

    @Override
    protected Author_Book_Entity mapRow(ResultSet rs) throws SQLException {
        Author_Book_Entity Author_Book = new Author_Book_Entity();
        Author_Book.setAuthor_id(rs.getInt("author_id"));
        Author_Book.setBook_id(rs.getInt("book_id"));
        return Author_Book;
    }

    @Override
    public List<Author_Book_Entity> findAll() {
        return query("SELECT * FROM Author_Book");
    }

    @Override
    public void create(Author_Book_Entity entity) {
        String sql = "INSERT INTO Author_Book (author_id, book_id) VALUES (?, ?)";
        executeUpdate(sql, entity.getAuthor_id(), entity.getBook_id());
    }

    @Override
    public void update(Author_Book_Entity entity) {
        String sql = "UPDATE Author_Book SET author_id = ?, book_id = ? WHERE author_id = ? AND book_id = ?";
        executeUpdate(sql, entity.getAuthor_id(), entity.getBook_id(), entity.getAuthor_id(), entity.getBook_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Author_Book WHERE author_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Author_Book_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Author_Book WHERE author_id = ?", id);
    }
    public List<Author_Book_Entity> findAuthors_BookByAuthorId(int id){
        return query("SELECT * FROM author_book WHERE author_id = ?", id);
    }
    public List<Author_Book_Entity> findAuthors_BookByBookId(int id){
        return query("SELECT * FROM author_book WHERE book_id = ?", id);
    }
}
