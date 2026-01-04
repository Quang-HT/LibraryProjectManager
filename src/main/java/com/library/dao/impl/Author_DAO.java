package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Author_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Author_DAO extends AbstractDAO<Author_Entity, Integer> {

    @Override
    protected Author_Entity mapRow(ResultSet rs) throws SQLException {
        Author_Entity Author = new Author_Entity();
        Author.setAuthor_id(rs.getInt("author_id"));
        Author.setAuthor_name(rs.getString("author_name"));
        Author.setBirth_date(rs.getDate("birth_date"));
        Author.setDescription(rs.getString("description"));
        Author.setDeath_date(rs.getDate("death_date"));
        Author.setCountry(rs.getString("country"));
        return Author;
    }

    @Override
    public List<Author_Entity> findAll() {
        return query("SELECT * FROM Author");
    }

    @Override
    public void create(Author_Entity entity) {
        String sql = "INSERT INTO Author (author_name, birth_date,death_date,country, description) VALUES (?,?,?,?,?)";
        executeUpdate(sql, entity.getAuthor_name(), entity.getBirth_date(),entity.getDeath_date(),entity.getCountry(), entity.getDescription());
    }

    @Override
    public void update(Author_Entity entity) {
        String sql = "UPDATE Author SET author_name = ?, birth_date = ?,death_date=?,country=?, description = ? WHERE author_id = ?";
        executeUpdate(sql, entity.getAuthor_name(), entity.getBirth_date(),entity.getDeath_date(),entity.getCountry(), entity.getDescription(), entity.getAuthor_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Author WHERE author_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Author_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Author WHERE author_id = ?", id);
    }
}
