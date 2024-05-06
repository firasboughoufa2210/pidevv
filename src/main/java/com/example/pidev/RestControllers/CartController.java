package com.example.pidev.RestControllers;

import com.example.pidev.DAO.Entities.AddToCartRequest;
import com.example.pidev.DAO.Entities.Cart;
import com.example.pidev.DAO.Entities.Product;
import com.example.pidev.Services.ProjectService;
import com.example.pidev.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ProjectService projectService;

    @Autowired
    public CartController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Cart cart = projectService.getCartById(id);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @GetMapping("byCart/{id}")
    public ResponseEntity<List<Product>> getProductsCartById(@PathVariable Long id) {
        Cart cart = projectService.getCartById(id);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Product> prods = projectService.getAllProductscart(id);
        return new ResponseEntity<>(prods, HttpStatus.OK);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Void> deleteProductFromCartById(@PathVariable Long cartItemId) {
        projectService.deleteProductFromCartById(cartItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart createdCart = projectService.createCart();
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        projectService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/api/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
        try {
            projectService.addToCart(request);
            return ResponseEntity.ok("Product added to cart successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}