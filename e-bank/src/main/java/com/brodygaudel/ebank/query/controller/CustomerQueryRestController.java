package com.brodygaudel.ebank.query.controller;

import com.brodygaudel.ebank.query.dto.CustomerPageDTO;
import com.brodygaudel.ebank.query.dto.CustomerResponseDTO;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.mappers.Mappers;
import com.brodygaudel.ebank.query.model.CustomerPage;
import com.brodygaudel.ebank.query.model.GetAllCustomersQuery;
import com.brodygaudel.ebank.query.model.GetCustomerByIdQuery;
import com.brodygaudel.ebank.query.model.SearchCustomersQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/queries/customers")
public class CustomerQueryRestController {

    private final QueryGateway queryGateway;

    public CustomerQueryRestController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/get/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable String id){
        Customer customer =  queryGateway.query(
                new GetCustomerByIdQuery(id),
                ResponseTypes.instanceOf(Customer.class)
        ).join();
        if(customer == null){
            return null;
        }
        return Mappers.fromCustomer(customer);
    }

    @GetMapping("/all")
    public List<CustomerResponseDTO> getAllCustomers(){
        List<Customer> customers = queryGateway.query(
                new GetAllCustomersQuery(),
                ResponseTypes.multipleInstancesOf(Customer.class)
        ).join();
        if(customers == null || customers.isEmpty()){
            return Collections.emptyList();
        }
        return customers.stream().map(Mappers::fromCustomer).toList();
    }

    @GetMapping("/search")
    public CustomerPageDTO searchCustomers(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size){
        CustomerPage customerPage = queryGateway.query(
                new SearchCustomersQuery(keyword, page, size),
                ResponseTypes.instanceOf(CustomerPage.class)
        ).join();
        if(customerPage.getCustomers() == null || customerPage.getCustomers().isEmpty()){
            return new CustomerPageDTO(customerPage.getTotalPage(), page, size, Collections.emptyList());
        }
        List<CustomerResponseDTO> customers = customerPage.getCustomers().stream().map(Mappers::fromCustomer).toList();
        return new CustomerPageDTO(customerPage.getTotalPage(), page, size, customers);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
