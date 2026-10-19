package com.example.TransactionAPI.dtos.responses;

import com.example.TransactionAPI.data.models.NetworkProvider;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class AirtimeResponse {
    private String description;
    private LocalDateTime transactionDate;
    private String transactionId;
    private String phoneNumber;
    private NetworkProvider networkName;
    private BigDecimal airtimeAmount;
}
