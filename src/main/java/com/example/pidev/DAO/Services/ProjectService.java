package com.example.pidev.DAO.Services;

import com.example.pidev.DAO.Entities.*;
import com.example.pidev.DAO.Repoitories.*;
import com.example.pidev.DAO.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class ProjectService implements ProjectIService {

    private final ProductRepo productRepository;
    private final OrderRepo orderRepository;
    private final JuryRepo juryRepository;
    private final CartItemRepo cartItemRepository;
    private final CartRepo cartRepository;
    private final CategoryRepo categoryRepository;
    private final ReqRepo addToCartRequestRepository;

    // Cart CRUD operations

    public Cart createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart);
    }

    public List<Cart> getAllCarts()
    {
        return cartRepository.findAll();
    }
    public void deleteProductById(Long Id) {
        productRepository.deleteById(Id);
    }


    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }
    public void editProduct(Product updatedProduct) {
        Product existingProduct = productRepository.findById(updatedProduct.getId())
                .orElseThrow(() -> new RuntimeException("Product not found")); // You can create a custom exception here
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductSku(updatedProduct.getProductSku());
        existingProduct.setProductPrice(updatedProduct.getProductPrice());
        existingProduct.setProductShortName(updatedProduct.getProductShortName());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
        existingProduct.setCreatedDate(updatedProduct.getCreatedDate());
        existingProduct.setDeliveryTimeSpan(updatedProduct.getDeliveryTimeSpan());
        existingProduct.setProductImageUrl(updatedProduct.getProductImageUrl());
        // Set other fields as needed
        productRepository.save(existingProduct);
    }


    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    public void addToCart(AddToCartRequest request) {
        // Retrieve or create the cart
        Cart cart = cartRepository.findById(request.getCartidd())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        // Retrieve the product
        Product product = productRepository.findById(request.getProductidd())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Create a new cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setAddedDate(request.getAddedDate());

        // Add the cart item to the cart
        cart.getCartItems().add(cartItem);

        // Save the cart
        cartRepository.save(cart);
    }

    /*private void updateCartTotalPrice(Cart cart) {
        double totalPrice = 0;
        for (CartItem item : cart.getCartItems()) {
            totalPrice += item.getProduct().getProductPrice() * item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
    }*/


    // Rest of your methods here...

    public List<AddToCartRequest> getCartProductsByCustomerId(Long customerId) {
        Cart cart = cartRepository.findByCustId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for customer"));
        return addToCartRequestRepository.findByCart(cart);
    }


    public List<Product> getAllProductscart(Long cartId) {
        Cart cart = cartRepository.findByCustId(cartId).orElse(null);

        return cartItemRepository.findProductsByCartId(cart.getCartId());
    }

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CartItem not found"));
    }

    public CartItem updateCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void deleteProductFromCartById(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Jury CRUD operations

    public List<Jury> findAllJurys() {
        return juryRepository.findAll();
    }

    public Jury findJuryById(Long id) {
        return juryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Jury not found"));
    }

    public Jury saveJury(Jury jury) {
        return juryRepository.save(jury);
    }

    public void deleteJuryById(Long id) {
        juryRepository.deleteById(id);
    }

    // Product CRUD operations

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }



    // Order CRUD operations

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public void openSaleBySaleId(Long saleId) {
        Order order = orderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale order not found"));
        order.setCanceled(false); // Set sale as not canceled (i.e., open)
        orderRepository.save(order);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    // Category CRUD operations

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    // Open Sale by Sale ID

    public Order findOrderBySaleId(Long saleId) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findOrderBySaleId(saleId));
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new ResourceNotFoundException("Order not found");
        }
    }


    // Cancel Order by Sale ID

    public void cancelOrderBySaleId(Long saleId) {
        Order order = orderRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setCanceled(true); // Set order as canceled
        orderRepository.save(order);
    }

    // Place Order

    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }
    public List<Order> getAllSalesByCustomerId(Long customerId) {
        return orderRepository.findOrderBycustId(customerId);
    }

}
