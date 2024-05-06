package com.example.pidev.DAO.Services;

import com.example.pidev.DAO.Entities.Category;
import com.example.pidev.DAO.Entities.Jury;
import com.example.pidev.DAO.Entities.Order;
import com.example.pidev.DAO.Entities.Product;
import com.example.pidev.DAO.Entities.*;

import java.util.List;

public interface ProjectIService {

    // Product CRUD operations
    List<Product> findAllProducts();

    Product findProductById(Long id);

    Product saveProduct(Product product);

   // void deleteProductById(Long id);

    // Order CRUD operations
    List<Order> findAllOrders();

    Order findOrderById(Long id);

    Order saveOrder(Order order);

    void deleteOrderById(Long id);
    public Order findOrderBySaleId(Long saleId);


    // Jury CRUD operations
    List<Jury> findAllJurys();

    Jury findJuryById(Long id);

    Jury saveJury(Jury jury);

    void deleteJuryById(Long id);

    // Category CRUD operations
     List<Product> getAllProductscart(Long cartId);


    Category saveCategory(Category category);

    void deleteCategoryById(Long id);
}
