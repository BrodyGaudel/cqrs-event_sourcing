package com.brodygaudel.ebank.command.controller;

import com.brodygaudel.ebank.command.dto.AccountRequestDTO;
import com.brodygaudel.ebank.command.model.CreateAccountCommand;
import com.brodygaudel.ebank.command.model.DeleteAccountCommand;
import com.brodygaudel.ebank.command.model.UpdateAccountCommand;
import com.brodygaudel.ebank.command.util.IdGenerator;
import com.brodygaudel.ebank.common.enums.AccountStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandRestController {

    private final CommandGateway commandGateway;

    public AccountCommandRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody @NotNull AccountRequestDTO dto){
        return commandGateway.send(
            new CreateAccountCommand(
                  IdGenerator.generateId(), AccountStatus.CREATED, dto.currency(),
                  BigDecimal.valueOf(0), dto.customerId()
            )
        );
    }

    @PutMapping("/update/{id}")
    public CompletableFuture<String> updateAccount(@PathVariable String id, @RequestBody @NotNull AccountRequestDTO dto){
        return commandGateway.send(
              new UpdateAccountCommand(id, dto.status())
        );
    }

    @DeleteMapping("/delete/{id}")
    public CompletableFuture<String> deleteAccount(@PathVariable String id){
        return commandGateway.send(
                new DeleteAccountCommand(id)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception){
        return new ResponseEntity<>(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
