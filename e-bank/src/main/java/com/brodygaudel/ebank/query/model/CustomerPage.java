package com.brodygaudel.ebank.query.model;

import com.brodygaudel.ebank.query.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerPage {
    private int totalPage;
    private List<Customer> customers;
}
