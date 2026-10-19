package com.example.TransactionAPI.dtos.requests;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    private String senderAccount;
    private String recipientAccount;
    private BigDecimal amount;
    private String description;

}
