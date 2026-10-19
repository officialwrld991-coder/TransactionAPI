package com.example.TransactionAPI.data.repositories;
import com.example.TransactionAPI.data.models.Account; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface AccountRepository extends JpaRepository<Account,Long> { Optional<Account> findByAccountNumber(String accountNumber); }
