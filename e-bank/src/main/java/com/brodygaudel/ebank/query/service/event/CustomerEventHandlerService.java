package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.event.CustomerCreatedEvent;
import com.brodygaudel.ebank.common.event.CustomerDeletedEvent;
import com.brodygaudel.ebank.common.event.CustomerUpdatedEvent;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.exception.CustomerNotFoundException;
import com.brodygaudel.ebank.query.exception.NicAlreadyExistException;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventHandlerService {

    private final CustomerRepository customerRepository;

    public CustomerEventHandlerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @EventHandler
    public Customer on(@NotNull CustomerCreatedEvent event){
        log.info("### CustomerCreatedEvent Received");
        if(nicExists(event.getNic())){
            throw new NicAlreadyExistException("this National Identity Card already exist");
        }
        Customer customer = Customer.builder().id(event.getId())
                .nic(event.getNic()).firstname(event.getFirstname())
                .name(event.getName()).placeOfBirth(event.getPlaceOfBirth())
                .dateOfBirth(event.getDateOfBirth()).nationality(event.getNationality())
                .sex(event.getSex())
                .build();
        Customer customerSaved = customerRepository.save(customer);
        log.info("### customer saved with id '"+customerSaved.getId()+"'.");
        return customerSaved;
    }

    @EventHandler
    public Customer on(@NotNull CustomerUpdatedEvent event){
        log.info("### CustomerUpdatedEvent Received");
        Customer customer = customerRepository.findById(event.getId())
                .orElseThrow(()-> new CustomerNotFoundException("Customer with id '"+event.getId()+" 'not found"));
        if (!customer.getNic().equals(event.getNic()) && nicExists(event.getNic())){
            throw new NicAlreadyExistException("this National Identity Card already exist");
        }
        customer.setName(event.getName());
        customer.setFirstname(event.getFirstname());
        customer.setNationality(event.getNationality());
        customer.setPlaceOfBirth(event.getPlaceOfBirth());
        customer.setDateOfBirth(event.getDateOfBirth());
        customer.setSex(event.getSex());
        customer.setNic(event.getNic());
        Customer customerUpdated = customerRepository.save(customer);

        log.info("### customer updated successfully");
        return customerUpdated;
    }

    @EventHandler
    public void on(@NotNull CustomerDeletedEvent event){
        log.info("### CustomerDeletedEvent Received");
        Customer customer = customerRepository.findById(event.getId())
                .orElseThrow(()-> new CustomerNotFoundException("Customer you want deleted do not exist"));
        customerRepository.delete(customer);
        log.info("### customer deleted successfully");
    }

    private boolean nicExists(String nic){
        return customerRepository.checkIfNicExists(nic);
    }
}
