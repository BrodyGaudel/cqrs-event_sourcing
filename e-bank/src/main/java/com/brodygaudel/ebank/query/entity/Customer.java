package com.brodygaudel.ebank.query.entity;

import com.brodygaudel.ebank.common.enums.Sex;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Customer {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String nic;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String placeOfBirth;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
    private String nationality;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    List<Account> accounts;
}
