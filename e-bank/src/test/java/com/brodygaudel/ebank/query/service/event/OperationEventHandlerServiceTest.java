package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import com.brodygaudel.ebank.common.event.AccountCreditedEvent;
import com.brodygaudel.ebank.common.event.AccountDebitedEvent;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.entity.Operation;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import com.brodygaudel.ebank.query.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OperationEventHandlerServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private OperationEventHandlerService service;

    @BeforeEach
    void setUp() {
        service = new OperationEventHandlerService(
                operationRepository, accountRepository
        );
    }

    @Test
    void onAccountCreditedEvent() {
        AccountCreditedEvent event = new AccountCreditedEvent(
                "id", "description", BigDecimal.valueOf(5000), "accountId", LocalDateTime.now()
        );
        Account account = Account.builder().id(event.getAccountId()).balance(BigDecimal.valueOf(1000)).currency(Currency.EUR)
                .operations(null).status(AccountStatus.ACTIVATED).customer(new Customer()).build();

        Operation operation = Operation.builder().id(event.getId()).account(account).dateTime(event.getDateTime())
                .amount(event.getAmount()).description(event.getDescription()).build();

        when(accountRepository.findById(event.getAccountId())).thenReturn(Optional.of(account));
        when(operationRepository.save(operation)).thenReturn(operation);
        when(accountRepository.save(account)).thenReturn(
                Account.builder().id(event.getAccountId()).balance(BigDecimal.valueOf(6000))
                        .currency(Currency.EUR).operations(null).status(AccountStatus.ACTIVATED)
                        .customer(new Customer()).build()
        );

        Operation response = service.on(event);
        verify(accountRepository, times(1)).findById(event.getAccountId());
        verify(operationRepository, times(1)).save(operation);
        verify(accountRepository, times(1)).save(account);
        assertNotNull(response);
    }

    @Test
    void onAccountDebitedEvent() {
        AccountDebitedEvent event = new AccountDebitedEvent("id", "description", BigDecimal.valueOf(5000), "accountId", LocalDateTime.now());
        Account account = Account.builder().id(event.getAccountId()).balance(BigDecimal.valueOf(100000)).currency(Currency.EUR)
                .operations(null).status(AccountStatus.ACTIVATED).customer(new Customer()).build();

        Operation operation = Operation.builder().id(event.getId()).account(account).dateTime(event.getDateTime())
                .amount(event.getAmount()).description(event.getDescription()).build();

        when(accountRepository.findById(event.getAccountId())).thenReturn(Optional.of(account));
        when(operationRepository.save(operation)).thenReturn(operation);
        when(accountRepository.save(account)).thenReturn(
                Account.builder().id(event.getAccountId()).balance(BigDecimal.valueOf(95000))
                        .currency(Currency.EUR).operations(null).status(AccountStatus.ACTIVATED)
                        .customer(new Customer()).build()
        );
        Operation response = service.on(event);
        verify(accountRepository, times(1)).findById(event.getAccountId());
        verify(operationRepository, times(1)).save(operation);
        verify(accountRepository, times(1)).save(account);
        assertNotNull(response);
    }
}