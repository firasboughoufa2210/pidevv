package com.example.pidev.DAO.Repoitories;

import com.example.pidev.DAO.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByCategoryCategoryId(Long categoryId);

}
