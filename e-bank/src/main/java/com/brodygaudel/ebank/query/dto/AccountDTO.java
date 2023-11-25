package com.brodygaudel.ebank.query.dto;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import com.brodygaudel.ebank.common.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDTO {
    private String id;
    private AccountStatus status;
    private Currency currency;
    private BigDecimal balance;
    private String customerId;
}
