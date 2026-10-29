package com.example.TransactionAPI.data.models;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionItem {
    private long id;
    private String type;
    private String senderAccount;
    private String recipientAccount;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
}