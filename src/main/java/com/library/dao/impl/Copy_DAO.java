package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Copy_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Copy_DAO extends AbstractDAO<Copy_Entity, Integer> {

    @Override
    protected Copy_Entity mapRow(ResultSet rs) throws SQLException {
        Copy_Entity copy = new Copy_Entity();
        copy.setCopy_id(rs.getInt("copy_id"));
        copy.setBook_id(rs.getInt("book_Id"));
        copy.setCost(rs.getInt("cost"));
        copy.setStatus(rs.getString("status"));
        copy.setLocation(rs.getString("location"));
        return copy;
    }

    @Override
    public List<Copy_Entity> findAll() {
        return query("SELECT * FROM copy");
    }

    @Override
    public void delete(Integer id) {
        executeUpdate("DELETE FROM copy WHERE copy_id = ?", id);
    }

    @Override
        public Optional<Copy_Entity> findById(Integer id) {
            return querySingle("SELECT * FROM copy WHERE copy_id = ?", id);
        }


    @Override
    public void create(Copy_Entity entity) {
        String sql = "INSERT INTO copy (status, location, cost, book_Id) VALUES (?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getStatus(),
                entity.getLocation(),
                entity.getCost(),
                entity.getBook_id()
                );
    }

    @Override
    public void update(Copy_Entity entity) {
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
    public Optional<Copy_Entity> QuantityByBookId(int book_id){
        return querySingle("SELECT COUNT(*) FROM copy WHERE book_id=?", book_id);
    }
    public List<Copy_Entity> FindBorrow(){
        return query("SELECT * FROM copy WHERE status= 'Borrowed'");
    }
    public List<Copy_Entity> findByIdBook(int book_id){
        return query("SELECT * FROM copy WHERE book_id = ?",book_id);
    }
}
