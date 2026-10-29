package com.example.TransactionAPI.services;

import com.example.TransactionAPI.dtos.requests.AirtimeRequest;
import com.example.TransactionAPI.dtos.requests.CreateRequest;
import com.example.TransactionAPI.dtos.requests.TransferRequest;
import com.example.TransactionAPI.dtos.responses.AirtimeResponse;
import com.example.TransactionAPI.dtos.responses.CreateResponse;
import com.example.TransactionAPI.dtos.responses.TransferResponse;
import com.example.TransactionAPI.data.models.TransactionItem;

import java.util.List;

public interface TransactionService {
    CreateResponse createAccount (CreateRequest createRequest);
    TransferResponse transfer (TransferRequest transferRequest);
    AirtimeResponse airtime (AirtimeRequest airtimeRequest);
    List<TransactionItem> transactions(String accountNumber);
}
