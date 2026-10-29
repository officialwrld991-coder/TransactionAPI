package com.example.TransactionAPI.dtos.requests;

import com.example.TransactionAPI.data.models.NetworkProvider;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class AirtimeRequest {
    private String accountNumber;
    private NetworkProvider networkName;
    private BigDecimal airtimeAmount;
    private String phoneNumber;
    private String description;
    private LocalDateTime dateTime;


}
