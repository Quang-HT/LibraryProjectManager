package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Account_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Account_DAO extends AbstractDAO<Account_Entity, Integer> {

    @Override
    protected Account_Entity mapRow(ResultSet rs) throws SQLException {
        Account_Entity acc = new Account_Entity();
        acc.setAccount_id(rs.getInt("account_id"));
        acc.setUsername(rs.getString("username"));
        acc.setPassword(rs.getString("password"));
        acc.setIs_active(rs.getByte("is_active"));

        try {
            int staffId = rs.getInt("staff_id");
            if (!rs.wasNull()) {
                acc.setStaff_id(staffId);
            }
        } catch (SQLException ignored) {
        }

        return acc;
    }

    @Override
    public List<Account_Entity> findAll() {
        return query("SELECT * FROM Account");
    }

    @Override
    public void create(Account_Entity entity) {
        // ⚠️ KHÔNG CHÈN account_id nữa, để DB tự AUTO_INCREMENT
        String sql = "INSERT INTO Account (username, password, is_active, staff_id) " +
                "VALUES (?, ?, ?, ?)";

        executeUpdate(sql,
                entity.getUsername(),
                entity.getPassword(),
                entity.getIs_active(),
                entity.getStaff_id()
        );
    }

    @Override
    public void update(Account_Entity entity) {
        String sql = "UPDATE Account " +
                "SET username = ?, password = ?, is_active = ?, staff_id = ? " +
                "WHERE account_id = ?";

        executeUpdate(sql,
                entity.getUsername(),
                entity.getPassword(),
                entity.getIs_active(),
                entity.getStaff_id(),
                entity.getAccount_id()
        );
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Account WHERE account_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Account_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Account WHERE account_id = ?", id);
    }

    public Optional<Account_Entity> findByName(String name) {
        return querySingle("SELECT * FROM Account WHERE username = ?", name);
    }

    public Optional<Account_Entity> findByStaffEmail(String email) {
        String sql = """
            SELECT a.*
            FROM Staff s
            JOIN Account a ON a.staff_id = s.staff_id
            WHERE s.email = ?
            """;
        return querySingle(sql, email);
    }
}
