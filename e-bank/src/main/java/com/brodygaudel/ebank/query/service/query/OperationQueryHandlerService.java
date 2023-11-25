package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.query.entity.Operation;
import com.brodygaudel.ebank.query.exception.OperationNotFoundException;
import com.brodygaudel.ebank.query.model.GetOperationByIdQuery;
import com.brodygaudel.ebank.query.model.GetOperationsByAccountIdQuery;
import com.brodygaudel.ebank.query.model.OperationPage;
import com.brodygaudel.ebank.query.repository.OperationRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OperationQueryHandlerService {

    private final OperationRepository operationRepository;

    public OperationQueryHandlerService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public Operation handle(@NotNull GetOperationByIdQuery query){
        log.info("### handle GetOperationByIdQuery");
        Operation operation = operationRepository.findById(query.id())
                .orElseThrow( () -> new OperationNotFoundException("operation not found"));
        log.info("operation found");
        return operation;
    }

    @QueryHandler
    public OperationPage handle(@NotNull GetOperationsByAccountIdQuery query){
        log.info("### handle GetOperationsByAccountIdQuery");
        Page<Operation> operations = operationRepository.findByAccountIdOrderByDateDesc(
                query.accountId(), PageRequest.of(query.page(), query.size())
        );
        log.info("operation(s) found");
        return new OperationPage(operations.getTotalPages(), operations.getContent());
    }
}
