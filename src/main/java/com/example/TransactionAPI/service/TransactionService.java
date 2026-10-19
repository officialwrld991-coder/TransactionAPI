//package com.example.bank.service;
//import com.example.TransactionAPI.data.models.Transaction;
//import com.example.TransactionAPI.data.repositories.AccountRepository;
//import com.example.TransactionAPI.data.repositories.TransactionRepository;
//import com.example.bank.discount.DiscountCalculator; import com.example.bank.dto.*; import com.example.bank.model.*; import com.example.bank.repository.*;
//import java.math.BigDecimal; import java.math.RoundingMode; import java.time.*; import java.util.List;
//import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import org.springframework.web.server.ResponseStatusException; import org.springframework.http.HttpStatus;
//@Service public class TransactionService {
// private final AccountRepository accounts; private final TransactionRepository transactions; private final DiscountCalculator discounts;
// public TransactionService(AccountRepository accounts, TransactionRepository transactions, DiscountCalculator discounts){this.accounts=accounts;this.transactions=transactions;this.discounts=discounts;}
// @Transactional public TransactionResponse transfer(TransferRequest request){
//  if(request.sourceAccount().equals(request.destinationAccount())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Source and destination accounts must differ");
//  Account source=account(request.sourceAccount()), destination=account(request.destinationAccount()); LocalDateTime now=LocalDateTime.now();
//  long count=monthlyCount(source.getCustomer().getId(),now); BigDecimal rate=discounts.calculate(source.getCustomer(),request.amount(),count,now);
//  BigDecimal discount=request.amount().multiply(rate).setScale(2,RoundingMode.HALF_UP); BigDecimal charged=request.amount().subtract(discount).setScale(2,RoundingMode.HALF_UP);
//  Transaction tx=transactions.save(new Transaction(TransactionType.TRANSFER,source,destination,null,null,request.amount().setScale(2,RoundingMode.HALF_UP),discount,charged,now)); return TransactionResponse.from(tx);
// }
// @Transactional public TransactionResponse airtime(AirtimeRequest request){Account source=account(request.sourceAccount()); LocalDateTime now=LocalDateTime.now(); BigDecimal amount=request.amount().setScale(2,RoundingMode.HALF_UP);
//  Transaction tx=transactions.save(new Transaction(TransactionType.AIRTIME,source,null,request.networkProvider(),request.phoneNumber(),amount,BigDecimal.ZERO.setScale(2),amount,now)); return TransactionResponse.from(tx);
// }
// @Transactional(readOnly=true) public List<TransactionResponse> history(String accountNumber){account(accountNumber);return transactions.findBySourceAccount_AccountNumberOrDestinationAccount_AccountNumberOrderByCreatedAtDesc(accountNumber,accountNumber).stream().map(TransactionResponse::from).toList();}
// private Account account(String number){return accounts.findByAccountNumber(number).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found: "+number));}
// private long monthlyCount(Long customerId,LocalDateTime now){LocalDateTime start=now.toLocalDate().withDayOfMonth(1).atStartOfDay();LocalDateTime end=start.plusMonths(1);return transactions.countBySourceAccount_Customer_IdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(customerId,start,end);}
//}
