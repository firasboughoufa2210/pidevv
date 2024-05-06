package com.example.pidev.RestControllers;

import com.example.pidev.DAO.Entities.Product;
import com.example.pidev.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProjectService projectService;

    @Autowired
    public ProductController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = projectService.findProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/CreateProduct")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = projectService.saveProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping("/UpdateProduct")
    public ResponseEntity<String> editProduct(@RequestBody Product updatedProduct) {
        try {
            projectService.editProduct(updatedProduct);
            return ResponseEntity.ok("Product edited successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to edit product");
        }
    }
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long productId) {
        try {
            projectService.deleteProductById(productId);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete product");
        }
    }







    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = projectService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}