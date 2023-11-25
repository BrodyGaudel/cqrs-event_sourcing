package com.brodygaudel.ebank.command.model;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAccountCommand extends BaseCommand<String>{

    private final AccountStatus status;
    private final Currency currency;
    private final BigDecimal balance;
    private final String customerId;

    public CreateAccountCommand(String commandId, AccountStatus status, Currency currency, BigDecimal balance, String customerId) {
        super(commandId);
        this.status = status;
        this.currency = currency;
        this.balance = balance;
        this.customerId = customerId;
    }
}
