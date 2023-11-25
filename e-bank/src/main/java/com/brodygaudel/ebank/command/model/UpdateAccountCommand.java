package com.brodygaudel.ebank.command.model;

import com.brodygaudel.ebank.common.enums.AccountStatus;
import lombok.Getter;

@Getter
public class UpdateAccountCommand extends BaseCommand<String>{
    private final AccountStatus accountStatus;

    public UpdateAccountCommand(String commandId, AccountStatus accountStatus) {
        super(commandId);
        this.accountStatus = accountStatus;
    }
}
