package application;

import model.Seller;
import model.dao.Dao;
import model.dao.DaoFactory;

public class Program {
    public static void main(String[] args) {
        Dao<Seller> sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(8);
        System.out.println(seller);
    }
}
