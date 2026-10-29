package com.example.TransactionAPI;

import com.example.TransactionAPI.data.models.Account;
import com.example.TransactionAPI.data.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class Main implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public  Main(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        if (accountRepository.count() == 0) {

            Account senderAccount = new Account();
            senderAccount.setName("brwnboi");
            senderAccount.setAccountNumber("0123456789");
            senderAccount.setBalance(BigDecimal.valueOf(10000));

            Account receiverAccount = new Account();
            receiverAccount.setName("ghost");
            receiverAccount.setAccountNumber("9876543210");

            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            System.out.println(senderAccount.getName() + " " + senderAccount.getAccountNumber());
            System.out.println( receiverAccount.getName() + " " + receiverAccount.getAccountNumber());


        }
    }
}

