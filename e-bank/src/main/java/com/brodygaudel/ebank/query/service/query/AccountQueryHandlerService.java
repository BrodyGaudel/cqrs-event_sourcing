package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.exception.AccountNotFoundException;
import com.brodygaudel.ebank.query.model.GetAccountByIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountByCustomerIdQuery;
import com.brodygaudel.ebank.query.model.GetAllAccountsQuery;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AccountQueryHandlerService {

    private final AccountRepository accountRepository;

    public AccountQueryHandlerService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @QueryHandler
    public List<Account> handle(GetAllAccountsQuery query){
        log.info("### handle GetAllAccountsQuery");
        List<Account> accounts = accountRepository.findAll();
        log.info("### return '"+accounts.size()+"' accounts");
        return accounts;
    }

    @QueryHandler
    public Account handle(@NotNull GetAccountByIdQuery query){
        log.info("### handle GetAccountByIdQuery");
        Account accounts = accountRepository.findById(query.id())
                .orElseThrow( () -> new AccountNotFoundException("account not found"));
        log.info("account found");
        return accounts;
    }

    @QueryHandler
    public List<Account> handle(@NotNull GetAllAccountByCustomerIdQuery query){
        log.info("### handle GetAllAccountByCustomerIdQuery");
        List<Account> accounts = accountRepository.findByCustomerId(query.customerId());
        log.info("### return '"+accounts.size()+"' accounts");
        return accounts;
    }


}
