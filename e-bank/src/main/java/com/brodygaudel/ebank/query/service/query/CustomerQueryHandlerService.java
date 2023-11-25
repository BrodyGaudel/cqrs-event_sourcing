package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.exception.CustomerNotFoundException;
import com.brodygaudel.ebank.query.model.CustomerPage;
import com.brodygaudel.ebank.query.model.GetAllCustomersQuery;
import com.brodygaudel.ebank.query.model.GetCustomerByIdQuery;
import com.brodygaudel.ebank.query.model.SearchCustomersQuery;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerQueryHandlerService {

    private final CustomerRepository customerRepository;

    public CustomerQueryHandlerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @QueryHandler
    public List<Customer> handle(GetAllCustomersQuery query){
        log.info("### handle GetAllCustomersQuery");
        List<Customer> customers = customerRepository.findAll();
        log.info("### return '"+customers.size()+"' customers");
        return customers;
    }

    @QueryHandler
    public Customer handle(@NotNull GetCustomerByIdQuery query){
        log.info("### handle GetCustomerByIdQuery");
        Customer customer = customerRepository.findById(query.id())
                .orElseThrow( () -> new CustomerNotFoundException("customer not found"));
        log.info("### customer found");
        return customer;
    }

    @QueryHandler
    public CustomerPage handle(@NotNull SearchCustomersQuery query){
        log.info("### handle SearchCustomersQuery");
        Page<Customer> customers = customerRepository.search(
                "%"+query.keyword()+"%",
                PageRequest.of(query.page(), query.size())
        );
        log.info("### customer(s) found");
        return new CustomerPage(customers.getTotalPages(), customers.getContent().stream().toList());
    }


}
