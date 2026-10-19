package com.example.TransactionAPI.services;

import com.example.TransactionAPI.dtos.requests.AirtimeRequest;
import com.example.TransactionAPI.dtos.requests.TransferRequest;
import com.example.TransactionAPI.dtos.responses.AirtimeResponse;
import com.example.TransactionAPI.dtos.responses.TransferResponse;

public interface TransactionService {
    TransferResponse transfer (TransferRequest transferRequest);
    AirtimeResponse airtime (AirtimeRequest airtimeRequest);
    void transactions();
}
