package com.brodygaudel.ebank.query.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationResponseDTO(
        String id, LocalDateTime dateTime, BigDecimal amount, String description, String accountId) {
}
