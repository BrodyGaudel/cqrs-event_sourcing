package com.brodygaudel.ebank.command.controller;

import com.brodygaudel.ebank.command.dto.OperationRequestDTO;
import com.brodygaudel.ebank.command.model.CreditAccountCommand;
import com.brodygaudel.ebank.command.model.DebitAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/operations")
public class OperationCommandRestController {

    private final CommandGateway commandGateway;

    public OperationCommandRestController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody @NotNull OperationRequestDTO dto){
        return commandGateway.send(
                new CreditAccountCommand(
                        UUID.randomUUID().toString(), dto.description(),
                        dto.amount(), dto.accountId(), LocalDateTime.now()
                )
        );
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody @NotNull OperationRequestDTO dto){
        return commandGateway.send(
                new DebitAccountCommand(
                        UUID.randomUUID().toString(), dto.description(),
                        dto.amount(), dto.accountId(), LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception){
        return new ResponseEntity<>(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
