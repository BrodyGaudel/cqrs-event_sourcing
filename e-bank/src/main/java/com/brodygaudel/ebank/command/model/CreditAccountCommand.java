package com.brodygaudel.ebank.command.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class CreditAccountCommand extends BaseCommand<String> {
    private final String description;
    private final BigDecimal amount;
    private final String accountId;
    private final LocalDateTime dateTime;

    public CreditAccountCommand(String commandId, String description, BigDecimal amount, String accountId, LocalDateTime dateTime) {
        super(commandId);
        this.description = description;
        this.amount = amount;
        this.accountId = accountId;
        this.dateTime = dateTime;
    }
}
