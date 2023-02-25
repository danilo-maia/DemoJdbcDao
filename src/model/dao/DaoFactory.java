package model.dao;

import db.DB;
import model.Seller;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static Dao<Seller> createSellerDao(){
        return new SellerDaoJDBC(DB.getConnection());
    }
}
