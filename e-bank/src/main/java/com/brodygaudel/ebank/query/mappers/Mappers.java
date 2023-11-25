package com.brodygaudel.ebank.query.mappers;

import com.brodygaudel.ebank.query.dto.AccountDTO;
import com.brodygaudel.ebank.query.dto.CustomerResponseDTO;
import com.brodygaudel.ebank.query.dto.OperationResponseDTO;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.entity.Operation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Mappers {

    @Contract("_ -> new")
    public static @NotNull CustomerResponseDTO fromCustomer(@NotNull Customer customer){
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getNic(),
                customer.getFirstname(),
                customer.getName(),
                customer.getPlaceOfBirth(),
                customer.getDateOfBirth(),
                customer.getNationality(),
                customer.getSex()
        );
    }

    @Contract("_ -> new")
    public static @NotNull AccountDTO fromAccount(@NotNull Account account){
        return new AccountDTO(
                account.getId(),
                account.getStatus(),
                account.getCurrency(),
                account.getBalance(),
                account.getCustomer().getId()
        );
    }

    @Contract("_ -> new")
    public static @NotNull OperationResponseDTO fromOperation(@NotNull Operation operation){
        return new OperationResponseDTO(
                operation.getId(),
                operation.getDateTime(),
                operation.getAmount(),
                operation.getDescription(),
                operation.getAccount().getId()
        );
    }

    private Mappers(){
        super();
    }
}
