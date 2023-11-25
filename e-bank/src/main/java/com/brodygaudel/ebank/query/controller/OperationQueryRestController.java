package com.brodygaudel.ebank.query.controller;

import com.brodygaudel.ebank.query.dto.OperationPageDTO;
import com.brodygaudel.ebank.query.dto.OperationResponseDTO;
import com.brodygaudel.ebank.query.entity.Operation;
import com.brodygaudel.ebank.query.mappers.Mappers;
import com.brodygaudel.ebank.query.model.GetOperationByIdQuery;
import com.brodygaudel.ebank.query.model.GetOperationsByAccountIdQuery;
import com.brodygaudel.ebank.query.model.OperationPage;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/queries/operations")
public class OperationQueryRestController {

    private final QueryGateway queryGateway;

    public OperationQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get/{id}")
    public OperationResponseDTO getOperationById(@PathVariable String id){
        Operation operation = queryGateway.query(
                new GetOperationByIdQuery(id),
                ResponseTypes.instanceOf(Operation.class)
        ).join();
        if(operation == null){
            return null;
        }
        return Mappers.fromOperation(operation);
    }

    @GetMapping("/list/{accountId}/{page}/{size}")
    public OperationPageDTO getAllOperationsByAccountId(@PathVariable String accountId,
                                                        @PathVariable(name="page") int page,
                                                        @PathVariable(name="size") int size){

        OperationPage operationPage = queryGateway.query(
                new GetOperationsByAccountIdQuery(accountId, page, size),
                ResponseTypes.instanceOf(OperationPage.class)
        ).join();

        if(operationPage == null){
            return null;
        } else if (operationPage.getOperations() == null || operationPage.getOperations().isEmpty()) {
            return new OperationPageDTO(0,page, size, Collections.emptyList());
        }else{
            return new OperationPageDTO(
                    operationPage.getTotalPage(), page, size,
                    operationPage.getOperations().stream().map(Mappers::fromOperation).toList()
            );
        }
    }
}
