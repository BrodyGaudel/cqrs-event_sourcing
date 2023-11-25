package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import com.brodygaudel.ebank.common.enums.Sex;
import com.brodygaudel.ebank.common.event.AccountCreatedEvent;
import com.brodygaudel.ebank.common.event.AccountDeletedEvent;
import com.brodygaudel.ebank.common.event.AccountUpdatedEvent;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountEventHandlerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountEventHandlerService service;

    @BeforeEach
    void setUp() {
        service = new AccountEventHandlerService(
                accountRepository,
                customerRepository
        );
    }

    @Test
    void onAccountCreatedEvent() {
        AccountCreatedEvent event = new AccountCreatedEvent(
                "id",
                AccountStatus.CREATED,
                Currency.EUR,
                BigDecimal.valueOf(5986),
                "customerId"
        );
        Customer customer = Customer.builder().id(event.getCustomerId()).nic("nic").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();

        Account account = Account.builder().id(event.getId()).balance(event.getBalance()).currency(event.getCurrency())
                .operations(null).status(event.getStatus()).customer(customer).build();

        when(customerRepository.findById(event.getCustomerId())).thenReturn(Optional.of(customer));
        when(accountRepository.save(account)).thenReturn(account);

        Account response = service.on(event);
        verify(customerRepository, times(1)).findById(event.getCustomerId());
        verify(accountRepository, times(1)).save(account);
        assertNotNull(response);
    }


    @Test
    void onAccountDeletedEvent(){
        AccountDeletedEvent event = new AccountDeletedEvent("id");
        service.on(event);
        verify(accountRepository, times(1)).deleteById(event.getId());
    }

    @Test
    void onAccountUpdatedEvent(){
        String id = "1";
        AccountUpdatedEvent event = new AccountUpdatedEvent(id, AccountStatus.ACTIVATED);

        Customer customer = Customer.builder().id("1").nic("nic").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();
        Account account = new Account();
        account.setStatus(AccountStatus.CREATED);
        account.setId(id);
        account.setCurrency(Currency.EUR);
        account.setBalance(BigDecimal.valueOf(5689));
        account.setCustomer(customer);

        Account acc = new Account();
        acc.setStatus(event.getAccountStatus());
        acc.setId("1");
        acc.setCurrency(Currency.EUR);
        acc.setBalance(BigDecimal.valueOf(5689));
        acc.setCustomer(customer);

        when(accountRepository.findById(event.getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(acc);
        Account response = service.on(event);
        verify(accountRepository, times(1)).findById(event.getId());
        verify(accountRepository, times(1)).save(account);
        assertNotNull(response);
    }
}