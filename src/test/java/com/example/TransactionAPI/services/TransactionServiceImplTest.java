package com.example.TransactionAPI.services;

import com.example.TransactionAPI.data.models.Account;
import com.example.TransactionAPI.data.models.TransferTransaction;
import com.example.TransactionAPI.data.repositories.AccountRepository;
import com.example.TransactionAPI.data.repositories.TransferRepository;
import com.example.TransactionAPI.dtos.requests.TransferRequest;
import com.example.TransactionAPI.dtos.responses.TransferResponse;
import com.example.TransactionAPI.exceptions.TransferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private static final String SENDER_NO = "0123456789";
    private static final String RECIPIENT_NO = "9876543210";

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sender;
    private Account recipient;

    @BeforeEach
    void setUp() {
        sender = account("1", SENDER_NO, "1000.00");
        recipient = account("2", RECIPIENT_NO, "500.00");
    }

    // ---------- helpers ----------

    private Account account(String id, String number, String balance) {
        Account a = new Account();
        a.setId("id");
        a.setAccountNumber(number);
        a.setBalance(new BigDecimal(balance));
        return a;
    }

    private TransferRequest request(String from, String to, BigDecimal amount) {
        TransferRequest r = new TransferRequest();
        r.setSenderAccount(from);
        r.setRecipientAccount(to);
        r.setAmount(amount);
        r.setDescription("Rent");
        return r;
    }

    private void accountsExist() {
        when(accountRepository.findByAccountNumber(SENDER_NO)).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber(RECIPIENT_NO)).thenReturn(Optional.of(recipient));
    }

    private void saveAssignsIdAndDate() {
        when(transferRepository.save(any(TransferTransaction.class))).thenAnswer(inv -> {
            TransferTransaction t = inv.getArgument(0);
            t.setId("99L");
            t.setTransactionDate(LocalDateTime.of(2026, 10, 19, 10, 30));
            return t;
        });
    }

    private void assertNothingWasSaved() {
        verify(accountRepository, never()).save(any(Account.class));
        verify(transferRepository, never()).save(any(TransferTransaction.class));
    }

    // ---------- success ----------

    @Test
    @DisplayName("successful transfer moves the money, records the transaction and returns a response")
    void transfer_success() {
        accountsExist();
        saveAssignsIdAndDate();

        TransferResponse response =
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("250.00")));

        assertThat(sender.getBalance()).isEqualByComparingTo("750.00");
        assertThat(recipient.getBalance()).isEqualByComparingTo("750.00");
        verify(accountRepository).save(sender);
        verify(accountRepository).save(recipient);

        ArgumentCaptor<TransferTransaction> captor = ArgumentCaptor.forClass(TransferTransaction.class);
        verify(transferRepository).save(captor.capture());
        TransferTransaction saved = captor.getValue();
        assertThat(saved.getSenderAccount()).isSameAs(sender);
        assertThat(saved.getReceiverAccount()).isSameAs(recipient);
        assertThat(saved.getAmount()).isEqualByComparingTo("250.00");
        assertThat(saved.getDiscountAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(saved.getDescription()).isEqualTo("Rent");

        assertThat(response.getTransactionId()).isEqualTo(99L);
        assertThat(response.getSenderAccount()).isEqualTo(SENDER_NO);
        assertThat(response.getRecipientAccount()).isEqualTo(RECIPIENT_NO);
        assertThat(response.getAmount()).isEqualByComparingTo("250.00");
        assertThat(response.getTransactionDate()).isEqualTo(LocalDateTime.of(2026, 10, 19, 10, 30));
    }

    @Test
    @DisplayName("sending the exact balance is allowed and leaves the sender at zero")
    void transfer_exactBalance_succeeds() {
        accountsExist();
        saveAssignsIdAndDate();

        transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("1000.00")));

        assertThat(sender.getBalance()).isEqualByComparingTo("0.00");
        assertThat(recipient.getBalance()).isEqualByComparingTo("1500.00");
    }

    @Test
    @DisplayName("smallest valid amount (0.01) works")
    void transfer_smallestAmount_succeeds() {
        accountsExist();
        saveAssignsIdAndDate();

        transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("0.01")));

        assertThat(sender.getBalance()).isEqualByComparingTo("999.99");
        assertThat(recipient.getBalance()).isEqualByComparingTo("500.01");
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901"})   // 9 and 11 characters
    @DisplayName("sender account must be exactly 10 characters")
    void transfer_invalidSenderAccount_throws(String badNumber) {
        assertThatThrownBy(() ->
                transactionService.transfer(request(badNumber, RECIPIENT_NO, new BigDecimal("100"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Sender account must be exactly 10 digits");

        verifyNoInteractions(accountRepository, transferRepository);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456789", "12345678901"})
    @DisplayName("recipient account must be exactly 10 characters")
    void transfer_invalidRecipientAccount_throws(String badNumber) {
        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, badNumber, new BigDecimal("100"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Recipient account must be exactly 10 digits");

        verifyNoInteractions(accountRepository, transferRepository);
    }

    @Test
    @DisplayName("sender and recipient cannot be the same account")
    void transfer_sameAccount_throws() {
        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, SENDER_NO, new BigDecimal("100"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Sender and recipient accounts must be different");

        verifyNoInteractions(accountRepository, transferRepository);
    }

    @Test
    @DisplayName("null amount is rejected")
    void transfer_nullAmount_throws() {
        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, null)))
                .isInstanceOf(TransferException.class)
                .hasMessage("Amount must be greater than 0");

        verifyNoInteractions(accountRepository, transferRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.00", "-1", "-0.01", "-1000"})
    @DisplayName("zero and negative amounts are rejected")
    void transfer_nonPositiveAmount_throws(String amount) {
        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal(amount))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Amount must be greater than 0");

        verifyNoInteractions(accountRepository, transferRepository);
    }


    @Test
    @DisplayName("unknown sender account is rejected")
    void transfer_senderNotFound_throws() {
        when(accountRepository.findByAccountNumber(SENDER_NO)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("100"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Sender account not found");

        assertNothingWasSaved();
    }

    @Test
    @DisplayName("unknown recipient account is rejected and the sender's balance is untouched")
    void transfer_recipientNotFound_throws() {
        when(accountRepository.findByAccountNumber(SENDER_NO)).thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber(RECIPIENT_NO)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("100"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Recipient account not found");

        assertThat(sender.getBalance()).isEqualByComparingTo("1000.00");
        assertNothingWasSaved();
    }

    @Test
    @DisplayName("insufficient funds: one kobo too much is rejected and nothing changes")
    void transfer_insufficientFunds_throws() {
        accountsExist();

        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("1000.01"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Insufficient funds");

        assertThat(sender.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(recipient.getBalance()).isEqualByComparingTo("500.00");
        assertNothingWasSaved();
    }

    @Test
    @DisplayName("sender with a zero balance cannot send anything")
    void transfer_zeroBalance_throws() {
        sender.setBalance(BigDecimal.ZERO);
        accountsExist();

        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("0.01"))))
                .isInstanceOf(TransferException.class)
                .hasMessage("Insufficient funds");

        assertNothingWasSaved();
    }


    @Test
    @DisplayName("a failure while saving the transaction propagates to the caller")
    void transfer_saveFails_propagates() {
        accountsExist();
        when(transferRepository.save(any(TransferTransaction.class)))
                .thenThrow(new RuntimeException("database down"));

        assertThatThrownBy(() ->
                transactionService.transfer(request(SENDER_NO, RECIPIENT_NO, new BigDecimal("100"))))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("database down");
    }
}