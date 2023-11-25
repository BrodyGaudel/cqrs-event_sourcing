package com.brodygaudel.ebank.query.dto;

import com.brodygaudel.ebank.common.enums.Sex;

import java.util.Date;

public record CustomerResponseDTO(String id, String nic, String firstname, String name, String placeOfBirth,
                                  Date dateOfBirth, String nationality, Sex sex) {
}
