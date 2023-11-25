package com.brodygaudel.ebank.command.model;

import com.brodygaudel.ebank.common.enums.Sex;
import lombok.Getter;

import java.util.Date;

@Getter
public class UpdateCustomerCommand extends BaseCommand<String> {

    private final String nic;
    private final String firstname;
    private final String name;
    private final String placeOfBirth;
    private final Date dateOfBirth;
    private final String nationality;
    private final Sex sex;

    public UpdateCustomerCommand(String commandId, String nic, String firstname, String name, String placeOfBirth, Date dateOfBirth, String nationality, Sex sex) {
        super(commandId);
        this.nic = nic;
        this.firstname = firstname;
        this.name = name;
        this.placeOfBirth = placeOfBirth;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.sex = sex;
    }
}
