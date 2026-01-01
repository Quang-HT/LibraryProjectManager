package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Fine_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Fine_DAO extends AbstractDAO<Fine_Entity, Integer> {

    @Override
    protected Fine_Entity mapRow(ResultSet rs) throws SQLException {
        Fine_Entity Fine = new Fine_Entity();
        Fine.setFine_id(rs.getInt("fine_id"));
        Fine.setAmount(rs.getDouble("amount"));
        Fine.setStatus(rs.getString("status"));
        Fine.setPayment_method(rs.getString("payment_method"));
        Fine.setCreated_at(rs.getTimestamp("created_at"));
        Fine.setReason(rs.getString("reason"));
        Fine.setBorrow_id(rs.getInt("borrow_id"));
        Fine.setAccount_id(rs.getInt("account_id"));
        return Fine;
    }

    @Override
    public List<Fine_Entity> findAll() {
        return query("SELECT * FROM Fine");
    }

    @Override
    public void create(Fine_Entity entity) {
        String sql = "INSERT INTO Fine (amount, status, payment_method, reason, borrow_id, account_id) VALUES (?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, entity.getAmount(), entity.getStatus(), entity.getPayment_method(), entity.getReason(), entity.getBorrow_id(), entity.getAccount_id());
    }

    @Override
    public void update(Fine_Entity entity) {
        String sql = "UPDATE Fine SET amount = ?, status = ?, payment_method = ?, reason = ?, borrow_id = ?, account_id = ? WHERE fine_id = ?";
        executeUpdate(sql, entity.getAmount(), entity.getStatus(), entity.getPayment_method(), entity.getReason(), entity.getBorrow_id(), entity.getAccount_id(), entity.getFine_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Fine WHERE fine_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Fine_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Fine WHERE fine_id = ?", id);
    }

}