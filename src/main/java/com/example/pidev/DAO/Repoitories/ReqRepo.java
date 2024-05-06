package com.example.pidev.DAO.Repoitories;

import com.example.pidev.DAO.Entities.AddToCartRequest;
import com.example.pidev.DAO.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReqRepo extends JpaRepository<AddToCartRequest, Long> {
    List<AddToCartRequest> findByCart(Cart cart);
}
