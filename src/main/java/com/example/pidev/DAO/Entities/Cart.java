package com.example.pidev.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id") // specify the column name
    private Long cartId;

    private Long custId;
    private Long productId;
    private int quantity;
    private LocalDateTime addedDate;
    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<AddToCartRequest> cartreq = new ArrayList<>();
    // Constructors if needed

    // No need to manually create getters, setters, toString, equals, and hashCode
}
