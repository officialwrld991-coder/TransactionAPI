package com.example.TransactionAPI.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long id;

 @Column(nullable = false, unique = true, length = 10)
 private String accountNumber;

 @Column(nullable = false, precision = 19, scale = 2)
 private BigDecimal balance = BigDecimal.ZERO;

 @Column(nullable = false)
 private String name;

// @ManyToOne(optional = false)
// @JoinColumn(name = "customer_id")
// private Customer customer;

}