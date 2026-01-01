package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Staff_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Staff_DAO extends AbstractDAO<Staff_Entity, Integer> {

    @Override
    protected Staff_Entity mapRow(ResultSet rs) throws SQLException {
        Staff_Entity staff = new Staff_Entity();
        staff.setStaff_id(rs.getInt("staff_id"));
        staff.setStaff_name(rs.getString("staff_name"));
        staff.setPhone(rs.getString("phone"));
        staff.setPosition(rs.getString("position"));
        staff.setEmail(rs.getString("email"));
        staff.setIs_working(rs.getByte("is_working"));
        return staff;
    }

    @Override
    public List<Staff_Entity> findAll() {
        String sql = "SELECT * FROM Staff";
        return query(sql);
    }

    @Override
    public void create(Staff_Entity entity) {
        String sql = "INSERT INTO Staff (staff_name, position, email, phone, is_working) " +
                "VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getStaff_name(),
                entity.getPosition(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getIs_working()
        );
    }

    @Override
    public void update(Staff_Entity entity) {
        String sql = "UPDATE Staff " +
                "SET staff_name = ?, position = ?, email = ?, phone = ?, is_working = ? " +
                "WHERE staff_id = ?";
        executeUpdate(sql,
                entity.getStaff_name(),
                entity.getPosition(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getIs_working(),
                entity.getStaff_id()
        );
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Staff WHERE staff_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Staff_Entity> findById(Integer id) {
        String sql = "SELECT * FROM Staff WHERE staff_id = ?";
        return querySingle(sql, id);
    }

    public Optional<Staff_Entity> findEmail(String email) {
        String sql = "SELECT * FROM Staff WHERE email = ?";
        return querySingle(sql, email);
    }

    /**
     * Tìm Staff theo account_id
     * JOIN đúng theo khóa ngoại Account.staff_id → Staff.staff_id
     */
    public Optional<Staff_Entity> findByAccountId(Integer accountId) {
        String sql = """
            SELECT s.*
            FROM Staff s
            JOIN Account a ON a.staff_id = s.staff_id
            WHERE a.account_id = ?
            """;
        return querySingle(sql, accountId);
    }

    /**
     * Lấy danh sách nhân viên chưa có tài khoản
     */
    public List<Staff_Entity> findStaffWithoutAccount() {
        String sql = """
            SELECT s.*
            FROM Staff s
            LEFT JOIN Account a ON a.staff_id = s.staff_id
            WHERE a.account_id IS NULL
            """;
        return query(sql);
    }
}
