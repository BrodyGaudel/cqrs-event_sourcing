package com.brodygaudel.ebank.common.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountDebitedEvent extends BaseEvent<String>{
    private final String description;
    private final BigDecimal amount;
    private final String accountId;
    private final LocalDateTime dateTime;

    public AccountDebitedEvent(String id, String description, BigDecimal amount, String accountId, LocalDateTime dateTime) {
        super(id);
        this.description = description;
        this.amount = amount;
        this.accountId = accountId;
        this.dateTime = dateTime;
    }
}
