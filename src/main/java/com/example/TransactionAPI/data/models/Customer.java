package com.example.TransactionAPI.data.models;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor

public class Customer {

 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private String id;

 @Column(nullable=false)
 private String fullName;

 @Enumerated(EnumType.STRING) @Column(nullable=false)
 private CustomerType customerType;

 @Column(nullable=false)
 private LocalDateTime joinedDate;

 @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
 private List<Account> accounts =new ArrayList<>();

}
