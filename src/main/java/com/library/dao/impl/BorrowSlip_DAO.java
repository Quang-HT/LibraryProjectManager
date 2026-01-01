package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.BorrowSlip_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BorrowSlip_DAO extends AbstractDAO<BorrowSlip_Entity, Integer> {

    @Override
    protected BorrowSlip_Entity mapRow(ResultSet rs) throws SQLException {
        BorrowSlip_Entity BorrowSlip = new BorrowSlip_Entity();
        BorrowSlip.setBorrow_id(rs.getInt("borrow_id"));
        BorrowSlip.setBorrow_date(rs.getDate("borrow_date"));
        BorrowSlip.setReturn_date(rs.getDate("return_date"));
        BorrowSlip.setDue_date(rs.getDate("due_date"));
        BorrowSlip.setState(rs.getString("state"));
        BorrowSlip.setAccount_id(rs.getInt("account_id"));
        BorrowSlip.setReader_id(rs.getInt("reader_id"));
        return BorrowSlip;
    }

    @Override
    public List<BorrowSlip_Entity> findAll() {
        return query("SELECT * FROM BorrowSlip");
    }

    @Override
    public void create(BorrowSlip_Entity entity) {
        String sql = "INSERT INTO BorrowSlip (borrow_date, return_date, due_date, state, account_id, reader_id) VALUES (?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, entity.getBorrow_date(), entity.getReturn_date(), entity.getDue_date(), entity.getState(), entity.getAccount_id(), entity.getReader_id());
    }

    @Override
    public void update(BorrowSlip_Entity entity) {
        String sql = "UPDATE BorrowSlip SET borrow_date = ?, return_date = ?, due_date = ?, state = ?, account_id = ?, reader_id = ? WHERE borrow_id = ?";
        executeUpdate(sql, entity.getBorrow_date(), entity.getReturn_date(), entity.getDue_date(), entity.getState(), entity.getAccount_id(), entity.getReader_id(), entity.getBorrow_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM BorrowSlip WHERE borrow_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<BorrowSlip_Entity> findById(Integer id) {   
        return querySingle("SELECT * FROM BorrowSlip WHERE borrow_id = ?", id);
    }

    public List<BorrowSlip_Entity> findCountInMonth(int month, int year){
            return query("SELECT * FROM BorrowSlip WHERE MONTH(borrow_date) = ? AND YEAR(borrow_date) = ?;",month, year);
    }
    public List<BorrowSlip_Entity> findByIdReader(int id){
        return query("SELECT * FROM BorrowSlip WHERE reader_id = ?",id);
    }

}
