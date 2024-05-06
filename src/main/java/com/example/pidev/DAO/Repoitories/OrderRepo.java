package com.example.pidev.DAO.Repoitories;

import com.example.pidev.DAO.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Long> {
    Order findOrderBySaleId(Long saleId);
    List<Order> findOrderBycustId(Long custId);

}
