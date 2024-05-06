package com.example.pidev.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String productSku;

    private Double productPrice;

    private String productShortName;

    private String productDescription;

    private LocalDateTime createdDate;

    private String deliveryTimeSpan;

    private String productImageUrl;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;

}