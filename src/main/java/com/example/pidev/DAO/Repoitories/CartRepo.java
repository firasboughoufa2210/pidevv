package com.example.pidev.DAO.Repoitories;

import com.example.pidev.DAO.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Long> {
   // Cart findByCustIdAndProductId(Long custId, Long productId);
   Optional<Cart> findByCustId(Long custId);

}
