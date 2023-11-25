package com.brodygaudel.ebank.query.service.event;

import com.brodygaudel.ebank.common.enums.Sex;
import com.brodygaudel.ebank.common.event.CustomerCreatedEvent;
import com.brodygaudel.ebank.common.event.CustomerDeletedEvent;
import com.brodygaudel.ebank.common.event.CustomerUpdatedEvent;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerEventHandlerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerEventHandlerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerEventHandlerService(customerRepository);
    }

    @Test
    void onCustomerCreatedEvent() {
        CustomerCreatedEvent event = new CustomerCreatedEvent(
          "id", "nic", "firstname", "name",
                "world", new Date(), "nationality", Sex.M
        );
        Customer customer = Customer.builder().id(event.getId()).nic(event.getNic()).firstname(event.getFirstname()).name(event.getName())
                .placeOfBirth(event.getPlaceOfBirth()).dateOfBirth(event.getDateOfBirth()).nationality(event.getNationality()).sex(event.getSex())
                .build();

        when(customerRepository.checkIfNicExists("nic")).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(
                Customer.builder().id(event.getId()).nic(event.getNic()).firstname(event.getFirstname()).name(event.getName())
                        .placeOfBirth(event.getPlaceOfBirth()).dateOfBirth(event.getDateOfBirth()).nationality(event.getNationality()).sex(event.getSex())
                        .build()
        );
        Customer customerSaved = service.on(event);
        verify(customerRepository, times(1)).save(any());
        assertNotNull(customerSaved);
    }

    @Test
    void onCustomerUpdatedEvent() {
        String id = "id";
        CustomerUpdatedEvent event = new CustomerUpdatedEvent(
                id, "nic", "firstname", "name",
                "world", new Date(), "nationality", Sex.M
        );
        Customer customer = Customer.builder().id(event.getId()).nic(event.getNic()).firstname(event.getFirstname()).name(event.getName())
                .placeOfBirth(event.getPlaceOfBirth()).dateOfBirth(event.getDateOfBirth()).nationality(event.getNationality()).sex(event.getSex())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        when(customerRepository.checkIfNicExists("nic")).thenReturn(false);
        assert customer != null;
        when(customerRepository.save(customer)).thenReturn(
                Customer.builder().id(event.getId()).nic(event.getNic()).firstname(event.getFirstname()).name(event.getName())
                        .placeOfBirth(event.getPlaceOfBirth()).dateOfBirth(event.getDateOfBirth()).nationality(event.getNationality()).sex(event.getSex())
                        .build()
        );
        Customer customerUpdated = service.on(event);
        verify(customerRepository, times(1)).save(any());
        assertNotNull(customerUpdated);
    }

    @Test
    void onCustomerDeletedEvent() {
        String id = "id";
        CustomerDeletedEvent event = new CustomerDeletedEvent(id);
        Customer customer = Customer.builder().id(event.getId())
                .nic("nic").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date())
                .sex(Sex.M).nationality("nationality").build();
        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        service.on(event);
        verify(customerRepository, times(1)).findById(event.getId());
        assert customer != null;
        verify(customerRepository, times(1)).delete(customer);
    }
}