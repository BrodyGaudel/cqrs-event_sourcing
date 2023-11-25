package com.brodygaudel.ebank.query.service.query;

import com.brodygaudel.ebank.common.enums.Sex;
import com.brodygaudel.ebank.query.entity.Customer;
import com.brodygaudel.ebank.query.model.CustomerPage;
import com.brodygaudel.ebank.query.model.GetAllCustomersQuery;
import com.brodygaudel.ebank.query.model.GetCustomerByIdQuery;
import com.brodygaudel.ebank.query.model.SearchCustomersQuery;
import com.brodygaudel.ebank.query.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerQueryHandlerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerQueryHandlerService service;

    @BeforeEach
    void setUp() {
        service = new CustomerQueryHandlerService(customerRepository);
    }

    @Test
    void handleGetAllCustomersQuery() {
        Customer customer1 = Customer.builder().id("1").nic("nic1").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();
        Customer customer2 = Customer.builder().id("2").nic("nic2").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();
        List<Customer> customerList = List.of(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customerList);
        List<Customer> customers = service.handle( new GetAllCustomersQuery());
        verify(customerRepository, times(1)).findAll();
        assertNotNull(customers);
        assertFalse(customers.isEmpty());
    }

    @Test
    void handleGetCustomerByIdQuery() {
        String id = "id";
        Customer customer = Customer.builder().id(id).nic("nic2").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();
        when(customerRepository.findById(id)).thenReturn(Optional.ofNullable(customer));
        Customer customerFound = service.handle(new GetCustomerByIdQuery(id));
        verify(customerRepository, times(1)).findById(id);
        assertNotNull(customerFound);
    }

    @Test
    void handleSearchCustomersQuery() {
        Customer customer1 = Customer.builder().id("1").nic("nic1").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();
        Customer customer2 = Customer.builder().id("2").nic("nic2").firstname("firstname").name("name")
                .placeOfBirth("town").dateOfBirth(new Date()).sex(Sex.M).nationality("nationality").build();

        SearchCustomersQuery query = new SearchCustomersQuery("keyword", 1, 10);
        Page<Customer> mockPage = new PageImpl<>(Arrays.asList(customer1, customer2));
        when(customerRepository.search(anyString(), any(PageRequest.class))).thenReturn(mockPage);

        CustomerPage result = service.handle(query);
        verify(customerRepository, times(1)).search("%keyword%", PageRequest.of(1, 10));
        assertEquals(1, result.getTotalPage());
        assertEquals(Arrays.asList(customer1, customer2), result.getCustomers());
    }
}