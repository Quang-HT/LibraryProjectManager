package com.library.dao;

import com.library.config.ConnectDB;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDAO<T, ID extends Serializable> implements GenericsDAO<T, ID> {

    // 1. Mỗi DAO con sẽ tự map ResultSet -> Entity
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    // 2. Lấy Connection từ ConnectDB
    protected Connection getConnection() throws SQLException {
        return ConnectDB.connection();
    }

    // 3. HÀM DÙNG CHUNG: SELECT nhiều dòng
    protected List<T> query(String sql, Object... params) {
        List<T> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParams(ps, params);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 4. HÀM DÙNG CHUNG: SELECT 1 dòng
    protected Optional<T> querySingle(String sql, Object... params) {
        List<T> list = query(sql, params);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    // 5. HÀM DÙNG CHUNG: INSERT/UPDATE/DELETE
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParams(ps, params);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 6. Set tham số cho PreparedStatement
    private void setParams(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    // 7. Các method của GenericsDAO để DAO con override theo nhu cầu

    @Override
    public void create(T entity) {
        throw new UnsupportedOperationException("Subclass phải override create()");
    }

    @Override
    public void update(T entity) {
        throw new UnsupportedOperationException("Subclass phải override update()");
    }

    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException("Subclass phải override findAll()");
    }

    @Override
    public Optional<T> findById(ID id) {
        throw new UnsupportedOperationException("Subclass phải override findById()");
    }

    @Override
    public void delete(ID id) {
        throw new UnsupportedOperationException("Subclass phải override delete()");
    }

    
}
