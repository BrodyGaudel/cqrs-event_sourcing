package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.common.enums.OperationType;
import com.brodygaudel.ebank.query.entity.Account;
import com.brodygaudel.ebank.query.entity.Operation;
import com.brodygaudel.ebank.query.model.GetOperationByIdQuery;
import com.brodygaudel.ebank.query.model.GetOperationsByAccountIdQuery;
import com.brodygaudel.ebank.query.model.OperationPage;
import com.brodygaudel.ebank.query.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OperationQueryHandlerServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private OperationQueryHandlerService service;

    @BeforeEach
    void setUp() {
        service = new OperationQueryHandlerService(
                operationRepository
        );
    }

    @Test
    void handleGetOperationByIdQuery() {
        GetOperationByIdQuery query = new GetOperationByIdQuery("id");
        Operation operation = Operation.builder().id(query.id()).dateTime(LocalDateTime.now()).description("desc")
                .amount(BigDecimal.valueOf(6985)).account(new Account()).type(OperationType.CREDIT).build();
        when(operationRepository.findById(query.id())).thenReturn(Optional.of(operation));
        Operation response = service.handle(query);
        verify(operationRepository, times(1)).findById(query.id());
        assertNotNull(response);
    }

    @Test
    void handleGetOperationsByAccountIdQuery() {
        GetOperationsByAccountIdQuery query = new GetOperationsByAccountIdQuery(
               "accountId", 0, 10
        );
        Account account = new Account();
        account.setId(query.accountId());
        Operation operation1 = Operation.builder().id("1").dateTime(LocalDateTime.now()).description("desc")
                .amount(BigDecimal.valueOf(6985)).account(account).type(OperationType.CREDIT).build();

        Operation operation2 = Operation.builder().id("2").dateTime(LocalDateTime.now()).description("desc")
                .amount(BigDecimal.valueOf(8956)).account(account).type(OperationType.CREDIT).build();

        Page<Operation> operations = new PageImpl<>(Arrays.asList(operation1, operation2));
        when(operationRepository.findByAccountIdOrderByDateDesc(
                query.accountId(), PageRequest.of(query.page(), query.size()))
        ).thenReturn(operations);

        OperationPage operationPage = service.handle(query);
        verify(operationRepository, times(1)).findByAccountIdOrderByDateDesc(
                query.accountId(), PageRequest.of(query.page(), query.size())
        );
        assertNotNull(operationPage);
        assertFalse(operationPage.getOperations().isEmpty());
    }
}