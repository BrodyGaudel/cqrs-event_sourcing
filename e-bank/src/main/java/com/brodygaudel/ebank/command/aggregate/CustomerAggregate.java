package com.brodygaudel.ebank.command.aggregate;

import com.brodygaudel.ebank.command.exception.InvalidObjectIdException;
import com.brodygaudel.ebank.command.model.CreateCustomerCommand;
import com.brodygaudel.ebank.command.model.DeleteCustomerCommand;
import com.brodygaudel.ebank.command.model.UpdateCustomerCommand;
import com.brodygaudel.ebank.common.event.CustomerCreatedEvent;
import com.brodygaudel.ebank.common.event.CustomerDeletedEvent;
import com.brodygaudel.ebank.common.event.CustomerUpdatedEvent;
import com.brodygaudel.ebank.common.enums.Sex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
@Slf4j
@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;
    private String nic;
    private String firstname;
    private String name;
    private String placeOfBirth;
    private Date dateOfBirth;
    private String nationality;
    private Sex sex;


    public CustomerAggregate() {
        super();
    }

    @CommandHandler
    public CustomerAggregate(@NotNull CreateCustomerCommand command) {
        log.info("### Enter CreateCustomerCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
                new CustomerCreatedEvent(
                        command.getCommandId(), command.getNic(), command.getFirstname(),
                        command.getName(), command.getPlaceOfBirth(), command.getDateOfBirth(),
                        command.getNationality(), command.getSex()
                )
        );
    }

    @EventSourcingHandler
    public void on(@NotNull CustomerCreatedEvent event){
        log.info("### Enter CustomerCreatedEvent");
        this.customerId = event.getId();
        this.nic = event.getNic();
        this.firstname = event.getFirstname();
        this.name = event.getName();
        this.placeOfBirth = event.getPlaceOfBirth();
        this.dateOfBirth = event.getDateOfBirth();
        this.nationality = event.getNationality();
        this.sex = event.getSex();
    }

    @CommandHandler
    public void handle(@NotNull UpdateCustomerCommand command){
        log.info("### Enter UpdateCustomerCommand");
        if(command.getCommandId() == null || command.getCommandId().isBlank()){
            throw new InvalidObjectIdException("Id can not be null or blank.");
        }
        AggregateLifecycle.apply(
                new CustomerCreatedEvent(
                        command.getCommandId(), command.getNic(), command.getFirstname(),
                        command.getName(), command.getPlaceOfBirth(), command.getDateOfBirth(),
                        command.getNationality(), command.getSex()
                )
        );
    }

    @EventSourcingHandler
    public void on(@NotNull CustomerUpdatedEvent event){
        log.info("### Enter CustomerUpdatedEvent");
        this.customerId = event.getId();
        this.nic = event.getNic();
        this.firstname = event.getFirstname();
        this.name = event.getName();
        this.dateOfBirth = event.getDateOfBirth();
        this.placeOfBirth = event.getPlaceOfBirth();
        this.nationality = event.getNationality();
        this.sex = event.getSex();
    }

    @CommandHandler
    public void handle(@NotNull DeleteCustomerCommand command){
        log.info("### Enter DeleteCustomerCommand");
        AggregateLifecycle.apply(
                new CustomerDeletedEvent(command.getCommandId())
        );
    }

    @EventSourcingHandler
    public void on(@NotNull CustomerDeletedEvent event){
        log.info("### Enter CustomerDeletedEvent");
        this.customerId = event.getId();
    }
}
