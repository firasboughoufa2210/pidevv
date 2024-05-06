package com.example.pidev.DAO.RestControllers;

import com.example.pidev.DAO.Entities.CartItem;
import com.example.pidev.DAO.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/cart-items/")
public class CartItemController {

    private final ProjectService projectService;

    @Autowired
    public CartItemController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long id) {
        CartItem cartItem = projectService.getCartItemById(id);
        if (cartItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    /*@PostMapping("/ADDTOCART/")
    public ResponseEntity<String> addToCart(@RequestBody Cart request) {
        ResponseEntity<String> response = projectService.addToCart(request);
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        projectService.deleteCartItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
