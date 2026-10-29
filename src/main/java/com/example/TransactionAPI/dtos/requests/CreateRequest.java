package com.example.TransactionAPI.dtos.requests;

import lombok.Data;

@Data
public class CreateRequest {
    private String name;
    private String accountNumber;
}
