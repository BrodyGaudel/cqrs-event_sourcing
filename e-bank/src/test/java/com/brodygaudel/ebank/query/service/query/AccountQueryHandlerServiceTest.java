package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.model.GetAccountByIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountByCustomerIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountsQuery;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest
class AccountQueryHandlerServiceTest {

    @Mock
    private AccountRepository  accountRepository;

    @InjectMocks
    private AccountQueryHandlerService service;

    @BeforeEach
    void setUp() {
        service = new AccountQueryHandlerService(accountRepository);
    }

    @Test
    void handleGetAllAccountsQuery() {
        GetAllAccountsQuery query = new GetAllAccountsQuery();
        Account account1 = Account.builder().id("1").customer(new Customer()).balance(BigDecimal.valueOf(897))
                .status(AccountStatus.ACTIVATED).currency(Currency.EUR).operations(null).build();
        Account account2 = Account.builder().id("2").customer(new Customer()).balance(BigDecimal.valueOf(897))
                .status(AccountStatus.ACTIVATED).currency(Currency.EUR).operations(null).build();
        List<Account> accountList = List.of(account1, account2);

        when(accountRepository.findAll()).thenReturn(accountList);

        List<Account> accounts = service.handle(query);
        verify(accountRepository, times(1)).findAll();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }

    @Test
    void handleGetAccountByIdQuery() {
        GetAccountByIdQuery query = new GetAccountByIdQuery("id");
        Account account = Account.builder().id("2").customer(new Customer()).balance(BigDecimal.valueOf(897))
                .status(AccountStatus.ACTIVATED).currency(Currency.EUR).operations(null).build();
        when(accountRepository.findById(query.id())).thenReturn(Optional.of(account));

        Account response = service.handle(query);
        verify(accountRepository, times(1)).findById(query.id());
        assertNotNull(response);
    }

    @Test
    void handleGetAllAccountByCustomerIdQuery() {
        GetAllAccountByCustomerIdQuery query = new GetAllAccountByCustomerIdQuery("customerId");
        Customer customer = new Customer();
        customer.setId(query.customerId());

        Account account1 = Account.builder().id("1").customer(customer).balance(BigDecimal.valueOf(897))
                .status(AccountStatus.ACTIVATED).currency(Currency.EUR).operations(null).build();
        Account account2 = Account.builder().id("2").customer(customer).balance(BigDecimal.valueOf(897))
                .status(AccountStatus.ACTIVATED).currency(Currency.EUR).operations(null).build();
        List<Account> accountList = List.of(account1, account2);

        when(accountRepository.findByCustomerId(query.customerId())).thenReturn(accountList);
        List<Account> accounts = service.handle(query);
        verify(accountRepository, times(1)).findByCustomerId(query.customerId());
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
    }
}