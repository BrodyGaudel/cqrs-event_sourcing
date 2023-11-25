package com.brodygaudel.ebank.query.dto;

import java.util.List;

public record CustomerPageDTO(int totalPage, int page, int size, List<CustomerResponseDTO> customers) {
}
