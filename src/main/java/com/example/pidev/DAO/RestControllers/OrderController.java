package com.example.pidev.DAO.RestControllers;

import com.example.pidev.DAO.Entities.Order;
import com.example.pidev.DAO.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ProjectService projectService;

    @Autowired
    public OrderController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = projectService.findOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = projectService.saveOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setSaleId(id);
        Order updatedOrder = projectService.saveOrder(order);
        if (updatedOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        projectService.deleteOrderById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/OpenSaleBySaleId")
    public ResponseEntity<Order> getOrderBySaleId(@RequestParam Long saleId) {
        Order order = projectService.findOrderBySaleId(saleId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/GetAllSaleByCustomerId")
    public ResponseEntity<List<Order>> getAllSalesByCustomerId(@RequestParam Long id) {
        List<Order> orders = projectService.getAllSalesByCustomerId(id);
        return ResponseEntity.ok(orders);
    }


    @PutMapping("/cancel-order/{saleId}")
    public ResponseEntity<Void> cancelOrderBySaleId(@PathVariable Long saleId) {
        projectService.cancelOrderBySaleId(saleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}