package com.brodygaudel.ebank.query.entity;

import com.brodygaudel.ebank.common.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Operation {
    @Id
    private String id;

    @Column(nullable = false, updatable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    private Account account;
}
