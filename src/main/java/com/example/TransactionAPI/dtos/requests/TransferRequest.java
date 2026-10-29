package com.example.TransactionAPI.dtos.requests;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferRequest {
    private String senderAccount;
    private String recipientAccount;
    private String accountNumber;
    private String networkName;
    private String phoneNumber;
    private BigDecimal amount;
    private String description;
    private LocalDateTime dateTime;

}
