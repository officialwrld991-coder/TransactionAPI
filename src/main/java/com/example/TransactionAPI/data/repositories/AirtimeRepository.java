package com.example.TransactionAPI.data.repositories;

import com.example.TransactionAPI.data.models.AirtimeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirtimeRepository extends JpaRepository<AirtimeTransaction,String> {
    List<AirtimeTransaction> findAllByAccountId(String accountId);
}
