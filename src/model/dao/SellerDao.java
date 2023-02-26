package model.dao;

import model.Department;
import model.Seller;

import java.util.List;

public interface SellerDao extends GenericDao<Seller> {
    List<Seller> findByDepartment(Department department);
}
