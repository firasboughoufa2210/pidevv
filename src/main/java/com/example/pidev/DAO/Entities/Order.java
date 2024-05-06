package com.example.pidev.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity(name = "sale_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    private Long custId;

    private LocalDate saleDate;

    private Double totalInvoiceAmount;

    private Double discount;

    private String paymentNaration;

    private String deliveryAddress1;

    private String deliveryAddress2;

    private String deliveryCity;

    private String deliveryPinCode;

    private String deliveryLandMark;

    private boolean isCanceled;



    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId")
    private Cart cart;



}
