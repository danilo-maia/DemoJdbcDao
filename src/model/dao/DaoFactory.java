package model.dao;

import model.Seller;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
    public static Dao<Seller> createSellerDao(){
        return new SellerDaoJDBC();
    }
}
