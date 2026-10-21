package com.example.TransactionAPI.data.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// One row of an account's history, whether it came from an airtime purchase or a transfer.
// "type" tells them apart, because an airtime id and a transfer id can be the same number.
public record TransactionItem(
        Long id,
        String type,
        String direction,
        BigDecimal amount,
        String description,
        LocalDateTime transactionDate
) { }