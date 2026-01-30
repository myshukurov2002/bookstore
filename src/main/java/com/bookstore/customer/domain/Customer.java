package com.bookstore.customer.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalPurchase = BigDecimal.ZERO;

    @Version
    private Long version;

    public void addToTotalPurchase(BigDecimal amount) {
        this.totalPurchase = this.totalPurchase.add(amount);
    }
}
