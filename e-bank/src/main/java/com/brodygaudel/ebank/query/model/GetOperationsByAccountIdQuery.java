package com.brodygaudel.ebank.query.model;

public record GetOperationsByAccountIdQuery(String accountId, int page, int size) {
}
