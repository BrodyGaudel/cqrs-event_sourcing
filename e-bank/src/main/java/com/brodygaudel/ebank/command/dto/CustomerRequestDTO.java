package com.brodygaudel.ebank.command.dto;

import com.brodygaudel.ebank.common.enums.Sex;

import java.util.Date;

public record CustomerRequestDTO(String nic, String firstname, String name, String placeOfBirth,
                                 Date dateOfBirth, String nationality, Sex sex) {
}
