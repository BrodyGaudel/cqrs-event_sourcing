package com.brodygaudel.ebank.common.event;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import lombok.Getter;

@Getter
public class AccountUpdatedEvent extends BaseEvent<String>{

    private final AccountStatus accountStatus;

    public AccountUpdatedEvent(String id, AccountStatus accountStatus) {
        super(id);
        this.accountStatus = accountStatus;
    }
}
