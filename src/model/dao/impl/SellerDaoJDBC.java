package model.dao.impl;

import db.DB;
import db.DbException;
import model.Department;
import model.Seller;
import model.dao.SellerDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private final Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        try (
            PreparedStatement st = conn.prepareStatement("""
            INSERT INTO seller
            (Name, Email, BirthDate, BaseSalary, DepartmentId)
            VALUES
            (?, ?, ?, ?, ?)
            """,
            Statement.RETURN_GENERATED_KEYS)
        ) {
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, Date.valueOf(obj.getBirthDate()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                try (ResultSet rs = st.getGeneratedKeys()){
                    if (rs.next()) {
                        obj.setId(rs.getInt(1));
                    }
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }



        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
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
            try (ResultSet rs = st.executeQuery()){
                if (rs.next()) {
                    Department dep = instantiateDepartment(rs);
                    return instantiateSeller(rs, dep);
                }
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
        try (
                PreparedStatement st = conn.prepareStatement("""
                        SELECT seller.*, department.Name as DepName
                        FROM seller
                        INNER JOIN department ON seller.DepartmentId = department.Id
                        ORDER BY seller.name;
                        """)
        ) {
            try (ResultSet rs = st.executeQuery()){
                Map<Integer, Department> map = new HashMap<>();

                List<Seller> sellers = new ArrayList<>();
                while (rs.next()) {
                    Department dep = map.get(rs.getInt("DepartmentId"));

                    if (dep == null) {
                        dep = instantiateDepartment(rs);
                        map.put(dep.getId(), dep);
                    }

                    sellers.add(instantiateSeller(rs, dep));
                }
                return sellers;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
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

            try (ResultSet rs = st.executeQuery()){
                List<Seller> sellers = new ArrayList<>();
                while (rs.next()) {
                    sellers.add(instantiateSeller(rs, department));
                }
                return sellers;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }
}
