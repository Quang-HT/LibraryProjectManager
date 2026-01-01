package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.copy_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class copy_DAO extends AbstractDAO<copy_Entity, Integer> {

    @Override
    protected copy_Entity mapRow(ResultSet rs) throws SQLException {
        copy_Entity copy = new copy_Entity();
        copy.setCopy_id(rs.getInt("copy_id"));
        copy.setBook_id(rs.getInt("book_Id"));
        copy.setCost(rs.getInt("cost"));
        copy.setStatus(rs.getString("status"));
        copy.setLocation(rs.getString("location"));
        return copy;
    }

    @Override
    public List<copy_Entity> findAll() {
        return query("SELECT * FROM copy");
    }

    @Override
    public void delete(Integer id) {
        executeUpdate("DELETE FROM copy WHERE copy_id = ?", id);
    }

    @Override
        public Optional<copy_Entity> findById(Integer id) {
            return querySingle("SELECT * FROM copy WHERE copy_id = ?", id);
        }


    @Override
    public void create(copy_Entity entity) {
        String sql = "INSERT INTO copy (status, location, cost, book_Id) VALUES (?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getStatus(),
                entity.getLocation(),
                entity.getCost(),
                entity.getBook_id()
                );
    }

    @Override
    public void update(copy_Entity entity) {
        String sql = """
        UPDATE copy 
        SET status = ?, location = ?, cost = ?, book_Id = ?
        WHERE copy_id = ?
    """;

        executeUpdate(sql,
                entity.getStatus(),
                entity.getLocation(),
                entity.getCost(),
                entity.getBook_id(),
                entity.getCopy_id()        // vị trí thứ 6 cho WHERE
        );
    }
    public Optional<copy_Entity> QuantityByBookId(int book_id){
        return querySingle("SELECT COUNT(*) FROM copy WHERE book_id=?", book_id);
    }
    public List<copy_Entity> FindBorrow(){
        return query("SELECT * FROM copy WHERE status= 'Borrowed'");
    }
    public List<copy_Entity> findByIdBook(int book_id){
        return query("SELECT * FROM copy WHERE book_id = ?",book_id);
    }
}
