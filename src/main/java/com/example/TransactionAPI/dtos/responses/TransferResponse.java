package com.example.TransactionAPI.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferResponse {
    private String description;
    private String senderAccount;
    private BigDecimal amount;
    private String recipientAccount;
    private long transactionId;
    private LocalDateTime transactionDate;
}
