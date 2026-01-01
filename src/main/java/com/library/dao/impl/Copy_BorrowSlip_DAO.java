package com.library.dao.impl;

import com.library.dao.AbstractDAO;
import com.library.model.Copy_BorrowSlip_Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Copy_BorrowSlip_DAO extends AbstractDAO<Copy_BorrowSlip_Entity, Integer> {

    @Override
    protected Copy_BorrowSlip_Entity mapRow(ResultSet rs) throws SQLException {
        Copy_BorrowSlip_Entity Copy_ = new Copy_BorrowSlip_Entity();
        Copy_.setBorrow_id(rs.getInt("borrow_id"));
        Copy_.setCopy_id(rs.getInt("copy_id"));
        return Copy_;
    }

    @Override
    public List<Copy_BorrowSlip_Entity> findAll() {
        return query("SELECT * FROM Copy_BorrowSlip");
    }

    @Override
    public void create(Copy_BorrowSlip_Entity entity) {
        String sql = "INSERT INTO Copy_BorrowSlip (borrow_id, copy_id) VALUES (?, ?)";
        executeUpdate(sql, entity.getBorrow_id(), entity.getCopy_id());
    }

    @Override
    public void update(Copy_BorrowSlip_Entity entity) {
        String sql = "UPDATE Copy_BorrowSlip SET borrow_id = ?, copy_id = ? WHERE borrow_id = ? AND copy_id = ?";
        executeUpdate(sql, entity.getBorrow_id(), entity.getCopy_id(), entity.getBorrow_id(), entity.getCopy_id());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Copy_BorrowSlip WHERE borrow_id = ?";
        executeUpdate(sql, id);
    }


    public List<Copy_BorrowSlip_Entity> findByIdBorrow(Integer id) {
        return query("SELECT * FROM Copy_BorrowSlip WHERE borrow_id = ?", id);
    }

    public List<Copy_BorrowSlip_Entity> findByIdCopy(int idCopy){
        return query("SELECT *FROM Copy_BorrowSlip Where copy_id = ?",idCopy);
    }
}
