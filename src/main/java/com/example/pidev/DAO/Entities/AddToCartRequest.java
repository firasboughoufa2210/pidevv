package com.example.pidev.DAO.Entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idreq;
        private Long custId;
        private Long cartidd;
        private Long productidd;
        private Long quantity;
        private LocalDateTime addedDate;

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cart_id")
        private Cart cart;
    }
