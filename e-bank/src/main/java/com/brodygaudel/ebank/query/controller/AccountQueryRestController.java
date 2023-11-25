package com.brodygaudel.ebank.query.controller;

import com.brodygaudel.ebank.query.dto.AccountDTO;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.mappers.Mappers;
import com.brodygaudel.ebank.query.model.GetAccountByIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountByCustomerIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/queries/accounts")
public class AccountQueryRestController {

    private final QueryGateway queryGateway;

    public AccountQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get/{id}")
    public AccountDTO getAccountById(@PathVariable String id){
        Account account = queryGateway.query(
                new GetAccountByIdQuery(id),
                ResponseTypes.instanceOf(Account.class)
        ).join();
        return Mappers.fromAccount(account);
    }

    @GetMapping("/list/{customerId}")
    public List<AccountDTO> getAllAccountsByCustomerId(@PathVariable String customerId){
        List<Account> accounts = queryGateway.query(
                new GetAllAccountByCustomerIdQuery(customerId),
                ResponseTypes.multipleInstancesOf(Account.class)
        ).join();
        return accounts.stream().map(Mappers::fromAccount).toList();
    }

    @GetMapping("/all")
    public List<AccountDTO> getAllAccounts(){
        List<Account> accounts = queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class)
        ).join();
        return accounts.stream().map(Mappers::fromAccount).toList();
    }
}
