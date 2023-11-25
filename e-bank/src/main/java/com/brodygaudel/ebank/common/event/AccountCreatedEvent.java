package com.brodygaudel.ebank.common.event;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountCreatedEvent extends BaseEvent<String> {
    private final AccountStatus status;
    private final Currency currency;
    private final BigDecimal balance;
    private final String customerId;

    public AccountCreatedEvent(String id, AccountStatus status, Currency currency, BigDecimal balance, String customerId) {
        super(id);
        this.status = status;
        this.currency = currency;
        this.balance = balance;
        this.customerId = customerId;
    }
}
