package com.brodygaudel.ebank.command.model;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
public class BaseCommand<T> {

    @TargetAggregateIdentifier
    private T commandId;

    public BaseCommand(T commandId) {
        this.commandId = commandId;
    }
}
