package application;

import model.Department;
import model.Seller;
import model.dao.Dao;
import model.dao.DaoFactory;

import java.time.LocalDate;

public class Program {
    public static void main(String[] args) {
        Department obj = new Department(1, "Books");
        Seller seller = new Seller(21, "Bob", "bob@gmail.com", LocalDate.now(), 3000.0, obj);

        Dao<Seller> sellerDao = DaoFactory.createSellerDao();

        System.out.println(seller);
    }
}
