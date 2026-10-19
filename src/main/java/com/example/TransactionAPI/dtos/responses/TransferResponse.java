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
    private String transactionId;
    private LocalDateTime transactionDate;
}
