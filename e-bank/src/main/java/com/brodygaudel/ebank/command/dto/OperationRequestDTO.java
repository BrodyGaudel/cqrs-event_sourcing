package com.brodygaudel.ebank.command.dto;

import java.math.BigDecimal;

public record OperationRequestDTO(String description, BigDecimal amount, String accountId) {
}
