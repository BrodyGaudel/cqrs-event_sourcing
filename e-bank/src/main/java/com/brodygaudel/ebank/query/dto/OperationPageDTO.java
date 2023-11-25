package com.brodygaudel.ebank.query.dto;

import java.util.List;

public record OperationPageDTO(int totalPage, int page, int size, List<OperationResponseDTO> operations) {
}
