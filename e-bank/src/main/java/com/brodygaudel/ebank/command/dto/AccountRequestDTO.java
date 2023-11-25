package com.brodygaudel.ebank.command.dto;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;

import java.math.BigDecimal;

public record AccountRequestDTO(AccountStatus status,
                                Currency currency,
                                BigDecimal balance,
                                String customerId) {
}
