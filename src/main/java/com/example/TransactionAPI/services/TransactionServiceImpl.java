package com.example.TransactionAPI.services;
import com.example.TransactionAPI.data.models.Account;
import com.example.TransactionAPI.data.models.AirtimeTransaction;
import com.example.TransactionAPI.data.models.TransferTransaction;
import com.example.TransactionAPI.data.repositories.AccountRepository;
import com.example.TransactionAPI.data.repositories.AirtimeRepository;
import com.example.TransactionAPI.data.repositories.TransferRepository;
import com.example.TransactionAPI.dtos.requests.AirtimeRequest;
import com.example.TransactionAPI.dtos.requests.TransferRequest;
import com.example.TransactionAPI.dtos.responses.AirtimeResponse;
import com.example.TransactionAPI.dtos.responses.TransferResponse;
import com.example.TransactionAPI.exceptions.AirtimeException;
import com.example.TransactionAPI.exceptions.TransferException;
import com.example.TransactionAPI.data.models.TransactionItem;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AirtimeRepository airtimeRepository;

    private final TransferRepository transferRepository;

    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransferRepository transferRepository, AirtimeRepository airtimeRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.airtimeRepository = airtimeRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransferResponse transfer(TransferRequest transferRequest) {

        if (transferRequest.getSenderAccount() == null || transferRequest.getSenderAccount().length() != 10) {
            throw new TransferException("Sender account must be exactly 10 digits");
        }
        if (transferRequest.getRecipientAccount() == null || transferRequest.getRecipientAccount().length() != 10) {
            throw new TransferException("Recipient account must be exactly 10 digits");
        }
        if (transferRequest.getRecipientAccount().equals(transferRequest.getSenderAccount())) {
            throw new TransferException("Sender and recipient accounts must be different");
        }
        if (transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException("Amount must be greater than 0");
        }

        Account sender = accountRepository.findByAccountNumber(transferRequest.getSenderAccount())
                .orElseThrow(() -> new TransferException("Sender account not found"));
        Account recipient = accountRepository.findByAccountNumber(transferRequest.getRecipientAccount())
                .orElseThrow(() -> new TransferException("Recipient account not found"));

        if (sender.getBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new TransferException("Insufficient funds");
        }


        sender.setBalance(sender.getBalance().subtract(transferRequest.getAmount()));
        recipient.setBalance(recipient.getBalance().add(transferRequest.getAmount()));
        BigDecimal amount = transferRequest.getAmount();
        accountRepository.save(sender);
        accountRepository.save(recipient);

        TransferTransaction transaction = new TransferTransaction();
        transaction.setSenderAccount(sender);
        transaction.setReceiverAccount(recipient);
        transaction.setAmount(amount);
        transaction.setDiscountAmount(BigDecimal.ZERO);
        transaction.setDescription(transferRequest.getDescription());
        TransferTransaction saved = transferRepository.save(transaction);

        TransferResponse response = new TransferResponse();
        response.setDescription("SUCCESS");
        response.setDescription("Transfer completed successfully");
        response.setTransactionId(saved.getId());
        response.setSenderAccount(sender.getAccountNumber());
        response.setRecipientAccount(recipient.getAccountNumber());
        response.setAmount(amount);
        response.setTransactionDate(saved.getTransactionDate());
        return response;
    }

@Override
    public AirtimeResponse airtime(AirtimeRequest airtimeRequest) {

    if (airtimeRequest.getAccountNumber()== null || airtimeRequest.getAccountNumber().length() != 10) {
        throw new AirtimeException("Account number must be exactly 10 digits");
    }
    if (airtimeRequest.getNetworkName() == null) {
        throw new AirtimeException("Network provider is required");
    }
    if (airtimeRequest.getPhoneNumber() == null || airtimeRequest.getPhoneNumber().length() != 11) {
        throw new AirtimeException("Phone number must be a valid 11-digit Nigerian number");
    }
    if (airtimeRequest.getAirtimeAmount() == null) {
        throw new AirtimeException("Amount is required");
    }

    if (airtimeRequest.getAirtimeAmount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new AirtimeException("Amount must be greater than 0");
    }

    Account account = accountRepository.findByAccountNumber(airtimeRequest.getAccountNumber())
            .orElseThrow(() -> new AirtimeException("Account not found"));
    if (account.getBalance().compareTo(airtimeRequest.getAirtimeAmount()) < 0) {
        throw new AirtimeException("Insufficient funds");
    }


    account.setBalance(account.getBalance().subtract(airtimeRequest.getAirtimeAmount()));
    accountRepository.save(account);

    AirtimeTransaction transaction = new AirtimeTransaction();
    transaction.setAccount(account);
    transaction.setAmount(airtimeRequest.getAirtimeAmount());
    transaction.setNetwork(airtimeRequest.getNetworkName());
    transaction.setPhoneNumber(airtimeRequest.getPhoneNumber());
    transaction.setDescription(airtimeRequest.getDescription());
    transaction.setTransactionDate(LocalDateTime.now());
    AirtimeTransaction saved = airtimeRepository.save(transaction);

    AirtimeResponse response = new AirtimeResponse();
    response.setDescription("Airtime purchase successful");
    response.setTransactionId(saved.getId());
    response.setPhoneNumber(airtimeRequest.getPhoneNumber());
    response.setNetworkName(saved.getNetwork());
    response.setAirtimeAmount(airtimeRequest.getAirtimeAmount());
    response.setTransactionDate(saved.getTransactionDate());
    return response;
}

    @Override
    public List<TransactionItem> transactions(String accountNumber) {
        return List.of();
    }



}
