package model.dao.impl;

import db.DbException;
import model.Department;
import model.Seller;
import model.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        try (
            PreparedStatement st = conn.prepareStatement("""
                SELECT seller.*, department.Name as DepName
                FROM seller
                INNER JOIN department ON seller.departmentId = department.Id
                WHERE seller.Id = ?
                """)
        ) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs, dep);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
        obj.setDepartment(dep);
        return obj;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        try (
            PreparedStatement st = conn.prepareStatement("""
                    SELECT seller.*, department.Name as DepName
                    FROM seller
                    INNER JOIN department ON seller.DepartmentId = department.Id
                    WHERE DepartmentId = ?
                    ORDER BY seller.name;
                    """)
        ) {
            st.setInt(1, department.getId());

            ResultSet rs = st.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            while (rs.next()){
                sellers.add(instantiateSeller(rs, department));
            }
            return sellers;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
