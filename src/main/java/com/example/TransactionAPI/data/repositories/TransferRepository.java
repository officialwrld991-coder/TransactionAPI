package com.example.TransactionAPI.data.repositories;

import com.example.TransactionAPI.data.models.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<TransferTransaction,String> {
    List<TransferTransaction> findAllBysenderAccount(String senderAccount);
}
