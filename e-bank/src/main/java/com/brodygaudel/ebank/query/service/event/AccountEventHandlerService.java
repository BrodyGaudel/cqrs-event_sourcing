package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.event.AccountCreatedEvent;
import com.brodygaudel.ebank.common.event.AccountDeletedEvent;
import com.brodygaudel.ebank.common.event.AccountUpdatedEvent;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.exception.AccountNotFoundException;
import com.brodygaudel.ebank.query.exception.CustomerNotFoundException;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountEventHandlerService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountEventHandlerService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @EventHandler
    public Account on(@NotNull AccountCreatedEvent event) {
        log.info("### AccountCreatedEvent Received");
        Customer customer = customerRepository.findById(event.getCustomerId())
                .orElseThrow( () -> new CustomerNotFoundException("Customer with id '"+event.getCustomerId()+"' not found"));

        Account account = new Account(
                event.getId(), event.getStatus(), event.getCurrency(), event.getBalance(), customer, null
        );
        Account accountSaved = accountRepository.save(account);
        log.info("### Account saved successfully with id :"+accountSaved.getId());
        return accountSaved;
    }

    @EventHandler
    public void on(@NotNull AccountDeletedEvent event) {
        log.info("### AccountDeletedEvent Received");
        accountRepository.deleteById(event.getId());
        log.info("### Account deleted successfully");
    }

    @EventHandler
    public Account on(@NotNull AccountUpdatedEvent event) {
        log.info("### handle AccountUpdatedEvent");
        Account account = accountRepository.findById(event.getId())
                .orElseThrow(() -> new AccountNotFoundException("account not found"));
        account.setStatus(event.getAccountStatus());
        Account accountUpdated = accountRepository.save(account);
        log.info("### account status updated successfully");
        return accountUpdated;
    }
}
