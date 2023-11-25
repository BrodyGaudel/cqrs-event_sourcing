package com.brodygaudel.ebank.command.aggregate;

import com.brodygaudel.ebank.command.exception.InvalidObjectIdException;
import com.brodygaudel.ebank.command.model.CreateAccountCommand;
import com.brodygaudel.ebank.command.model.DeleteAccountCommand;
import com.brodygaudel.ebank.command.model.UpdateAccountCommand;
import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import com.brodygaudel.ebank.common.event.AccountCreatedEvent;
import com.brodygaudel.ebank.common.event.AccountDeletedEvent;
import com.brodygaudel.ebank.common.event.AccountUpdatedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@Slf4j
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private AccountStatus status;
    private Currency currency;
    private BigDecimal balance;
    private String customerId;

    public AccountAggregate() {
        super();
    }

    @CommandHandler
    public AccountAggregate(@NotNull CreateAccountCommand command) {
        log.info("### Enter CreateAccountCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
                new AccountCreatedEvent(
                        command.getCommandId(), command.getStatus(),
                        command.getCurrency(), command.getBalance(),
                        command.getCustomerId()
                )
        );
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreatedEvent event){
        log.info("### Enter AccountCreatedEvent");
        this.accountId = event.getId();
        this.status = event.getStatus();
        this.currency = event.getCurrency();
        this.balance = event.getBalance();
        this.customerId = event.getCustomerId();
    }

    @CommandHandler
    public void handle(@NotNull UpdateAccountCommand command){
        log.info("### Enter UpdateAccountCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
                new AccountUpdatedEvent(command.getCommandId(), command.getAccountStatus())
        );
    }

    @EventSourcingHandler
    public void on(@NotNull AccountUpdatedEvent event){
        this.accountId = event.getId();
        this.status = event.getAccountStatus();
    }

    @CommandHandler
    public void handle(@NotNull DeleteAccountCommand command){
        log.info("### Enter DeleteAccountCommand");
        AggregateLifecycle.apply(
                new AccountDeletedEvent(command.getCommandId())
        );
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDeletedEvent event){
        log.info("### Enter AccountDeletedEvent");
        this.accountId = event.getId();
    }
}
