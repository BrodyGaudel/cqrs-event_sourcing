package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.OperationType;
import com.brodygaudel.ebank.common.event.AccountCreditedEvent;
import com.brodygaudel.ebank.common.event.AccountDebitedEvent;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Operation;
import com.brodygaudel.ebank.query.exception.AccountNotActivatedException;
import com.brodygaudel.ebank.query.exception.AccountNotFoundException;
import com.brodygaudel.ebank.query.exception.BalanceNotSufficientException;
import com.brodygaudel.ebank.query.repository.AccountRepository;
import com.brodygaudel.ebank.query.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class OperationEventHandlerService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;

    public OperationEventHandlerService(OperationRepository operationRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public Operation on(@NotNull AccountCreditedEvent event) {
        log.info("### AccountCreditedEvent Received");
        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow( () -> new AccountNotFoundException("account not found"));
        if(!account.getStatus().equals(AccountStatus.ACTIVATED)){
            throw new AccountNotActivatedException("this account is '"+account.getStatus()+"' status");
        }
        if(event.getAmount() == null ||event.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new BalanceNotSufficientException("balance must not be null or under 0");
        }
        Operation operation = Operation.builder().id(event.getId()).account(account).dateTime(event.getDateTime())
                .amount(event.getAmount()).description(event.getDescription()).type(OperationType.CREDIT).build();
        Operation operationSaved = operationRepository.save(operation);
        account.setBalance(operationSaved.getAmount().add(account.getBalance()));
        Account accountSaved = accountRepository.save(account);
        log.info("account with id '"+accountSaved.getId()+"' successfully credited with "+operationSaved.getAmount());
        return operationSaved;
    }

    @EventHandler
    public Operation on(@NotNull AccountDebitedEvent event) {
        log.info("### AccountDebitedEvent Received");
        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow( () -> new AccountNotFoundException("account not found"));
        if(!account.getStatus().equals(AccountStatus.ACTIVATED)){
            throw new AccountNotActivatedException("this account is '"+account.getStatus()+"' status");
        }
        if(event.getAmount() == null || account.getBalance().compareTo(event.getAmount())<0){
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        Operation operation = Operation.builder().id(event.getId()).account(account).dateTime(event.getDateTime())
                .amount(event.getAmount()).description(event.getDescription()).type(OperationType.DEBIT)
                .build();
        Operation operationSaved = operationRepository.save(operation);
        account.setBalance(account.getBalance().subtract(operationSaved.getAmount()));
        Account accountSaved = accountRepository.save(account);
        log.info("account with id '"+accountSaved.getId()+"' successfully debited with "+operationSaved.getAmount());
        return operationSaved;
    }
}
