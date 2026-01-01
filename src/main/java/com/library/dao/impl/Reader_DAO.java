package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Reader_Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Reader_DAO extends AbstractDAO<Reader_Entity, Integer> {

    @Override
    protected Reader_Entity mapRow(ResultSet rs) throws SQLException {
        Reader_Entity reader = new Reader_Entity();
        reader.setReader_id(rs.getInt("reader_id"));
        reader.setReader_name(rs.getString("reader_name"));
        reader.setBirth_date(rs.getDate("birth_date"));
        reader.setEmail(rs.getString("email"));
        reader.setPhone(rs.getString("phone_number"));
        reader.setRegistered_date(rs.getDate("registered_date"));
        reader.setAddress(rs.getString("address"));
        reader.setCccd(rs.getString("cccd"));
        return reader;
    }

    @Override
    public List<Reader_Entity> findAll() {
        return query("SELECT * FROM Reader");
    }

    @Override
    public void create(Reader_Entity entity) {
        String sql = "INSERT INTO Reader " +
                "(reader_name, birth_date, email, phone_number, registered_date, address, cccd) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getReader_name(),
                entity.getBirth_date(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRegistered_date(),
                entity.getAddress(),
                entity.getCccd()
        );
    }

    @Override
    public void update(Reader_Entity entity) {
        String sql = "UPDATE Reader SET " +
                "reader_name = ?, " +
                "birth_date = ?, " +
                "email = ?, " +
                "phone_number = ?, " +
                "registered_date = ?, " +
                "address = ?, " +
                "cccd = ? " +
                "WHERE reader_id = ?";
        executeUpdate(sql,
                entity.getReader_name(),
                entity.getBirth_date(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRegistered_date(),
                entity.getAddress(),
                entity.getCccd(),
                entity.getReader_id()
        );
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Reader WHERE reader_id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public Optional<Reader_Entity> findById(Integer id) {
        return querySingle("SELECT * FROM Reader WHERE reader_id = ?", id);
    }
}
