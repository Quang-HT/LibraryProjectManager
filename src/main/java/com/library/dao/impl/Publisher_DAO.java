package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Publisher_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Publisher_DAO extends AbstractDAO<Publisher_Entity, Integer> {

    @Override
    protected Publisher_Entity mapRow(ResultSet rs) throws SQLException {
        Publisher_Entity nxb = new Publisher_Entity();
        nxb.setPublisher_id(rs.getInt("publisher_id"));
        nxb.setPublisher_name(rs.getString("publisher_name"));
        nxb.setAddress(rs.getString("address"));
        nxb.setPhone(rs.getString("phone"));
        return nxb;
    }

    @Override
    public List<Publisher_Entity> findAll() {
        return query("SELECT * FROM Publisher");
    }

    @Override
    public void create(Publisher_Entity entity) {
        String sql = "INSERT INTO Publisher (publisher_name, address, phone) VALUES (?, ?, ?)";
        executeUpdate(sql, entity.getPublisher_name(), entity.getAddress(), entity.getPhone());
    }

    @Override
    public void update(Publisher_Entity entity) {
        String sql = "UPDATE Publisher SET publisher_name = ?, address = ?, phone = ? WHERE publisher_id = ?";
        executeUpdate(sql, entity.getPublisher_name(), entity.getAddress(), entity.getPhone(), entity.getPublisher_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Publisher WHERE publisher_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Publisher_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Publisher WHERE publisher_id = ?", id);
    }
}
