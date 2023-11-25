package com.brodygaudel.ebank.command.aggregate;

import com.brodygaudel.ebank.command.exception.InvalidObjectIdException;
import com.brodygaudel.ebank.command.model.CreditAccountCommand;
import com.brodygaudel.ebank.command.model.DebitAccountCommand;
import com.brodygaudel.ebank.common.event.AccountCreditedEvent;
import com.brodygaudel.ebank.common.event.AccountDebitedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Slf4j
@Aggregate
public class OperationAggregate {

    @AggregateIdentifier
    private String operationId;
    private String description;
    private BigDecimal amount;
    private String accountId;
    private LocalDateTime dateTime;

    public OperationAggregate() {
        super();
    }

    @CommandHandler
    public OperationAggregate(@NotNull CreditAccountCommand command) {
        log.info("### Enter CreditAccountCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
               new AccountCreditedEvent(
                       command.getCommandId(),
                       command.getDescription(),
                       command.getAmount(),
                       command.getAccountId(),
                       command.getDateTime()
               )
        );
    }

    @EventSourcingHandler
    public void on(@NotNull AccountCreditedEvent event){
        log.info("### Enter AccountCreditedEvent");
        this.operationId = event.getId();
        this.description = event.getDescription();
        this.amount = event.getAmount();
        this.accountId = event.getAccountId();
        this.dateTime = event.getDateTime();
    }

    @CommandHandler
    public OperationAggregate(@NotNull DebitAccountCommand command) {
        log.info("### Enter DebitAccountCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
                new AccountDebitedEvent(
                        command.getCommandId(),
                        command.getDescription(),
                        command.getAmount(),
                        command.getAccountId(),
                        command.getDateTime()
                )
        );
    }

    @EventSourcingHandler
    public void on(@NotNull AccountDebitedEvent event){
        log.info("### Enter AccountDebitedEvent");
        this.operationId = event.getId();
        this.description = event.getDescription();
        this.amount = event.getAmount();
        this.accountId = event.getAccountId();
        this.dateTime = event.getDateTime();
    }
}
