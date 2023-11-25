package com.brodygaudel.ebank.query.model;

import com.brodygaudel.ebank.query.entity.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OperationPage {
    private int totalPage;
    private List<Operation> operations;
}
