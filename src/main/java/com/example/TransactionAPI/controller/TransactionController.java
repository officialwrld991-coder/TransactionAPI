package com.example.TransactionAPI.controller;

import com.example.TransactionAPI.dtos.requests.AirtimeRequest;
import com.example.TransactionAPI.dtos.requests.TransferRequest;
import com.example.TransactionAPI.exceptions.AirtimeException;
import com.example.TransactionAPI.exceptions.TransferException;
import com.example.TransactionAPI.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            return ResponseEntity.ok(transactionService.transfer(request));
        } catch (TransferException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong. Please try again.");
        }
    }

    @PostMapping("/airtime")
    public ResponseEntity<?> airtime(@RequestBody AirtimeRequest request) {
        try {
            return ResponseEntity.ok(transactionService.airtime(request));
        } catch (AirtimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong. Please try again.");
        }
    }
}