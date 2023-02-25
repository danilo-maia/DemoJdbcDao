package application;

import model.Department;
import model.Seller;
import model.dao.Dao;
import model.dao.DaoFactory;

import java.time.LocalDate;

public class Program {
    public static void main(String[] args) {
        Dao<Seller> sellerDao = DaoFactory.createSellerDao();

        Seller seller = sellerDao.findById(8);

        System.out.println(seller);
    }
}
