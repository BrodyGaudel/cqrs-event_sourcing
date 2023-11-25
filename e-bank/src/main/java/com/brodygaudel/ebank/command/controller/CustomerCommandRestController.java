package com.brodygaudel.ebank.command.controller;

import com.brodygaudel.ebank.command.dto.CustomerRequestDTO;
import com.brodygaudel.ebank.command.model.CreateCustomerCommand;
import com.brodygaudel.ebank.command.model.DeleteCustomerCommand;
import com.brodygaudel.ebank.command.model.UpdateCustomerCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/customers")
public class CustomerCommandRestController {

    private final CommandGateway commandGateway;

    public CustomerCommandRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public CompletableFuture<String> createCustomer(@RequestBody @NotNull CustomerRequestDTO dto){
        return commandGateway.send(
                new CreateCustomerCommand(
                        UUID.randomUUID().toString(), dto.nic(), dto.firstname(), dto.name(), dto.placeOfBirth(), dto.dateOfBirth(), dto.nationality(), dto.sex()
                )
        );
    }

    @PutMapping("/update/{id}")
    public CompletableFuture<String> updateCustomer(@PathVariable(name = "id") String id, @RequestBody @NotNull CustomerRequestDTO dto){
        return commandGateway.send(
                new UpdateCustomerCommand(
                        id, dto.nic(), dto.firstname(), dto.name(), dto.placeOfBirth(), dto.dateOfBirth(), dto.nationality(), dto.sex()
                )
        );
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> deleteOwner(@PathVariable String id){
        return commandGateway.send(new DeleteCustomerCommand(id));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception){
        return new ResponseEntity<>(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
