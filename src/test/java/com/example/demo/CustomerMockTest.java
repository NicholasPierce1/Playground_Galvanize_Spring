package com.example.demo;

import com.example.demo.Repository.CustomerRepository;
import com.example.demo.controllers.CustomerController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
//@ContextConfiguration({CustomerRepository.class, CustomerController.class})
public class CustomerMockTest {


    @Autowired
    private CustomerController customerController;

//    @Mock
//    CloudService mockCloudService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testMocks(){  // needs spring boot annotation
        assertNotNull(this.customerController);
        assertNotNull(this.customerRepository);
    }

/*
    //LONG SERVICE
    @Test
    public void testPerform() throws Exception {
        //Setup
        CloudServiceImpl cloudService = new CloudServiceImpl();
        Calculator calculator = new Calculator(cloudService);

        // Exercise and Assert
        assertEquals(10, calculator.perform(2, 3));
    }


    //MOCKED SERVICE
    @Test
    public void testPerform2() throws Exception {
        //Setup
        when(mockCloudService.add(2,3)).thenReturn(5);
        //when(mockCloudService.add(3,3)).thenReturn(6);
        Calculator calculator = new Calculator(mockCloudService);

        // Exercise and Assert
        //assertEquals(18, calculator.perform(3, 3));
        assertEquals(10, calculator.perform(2, 3));
        verify(mockCloudService).add(2,3); // name of method
    }

    //CREATE
    @Test
    public void testCreateCustomer() {

        //MockitoAnnotations.initMocks(this);

        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer("jean-marc","julien"));

        customerController = new CustomerController(customerRepository);
        Customer actual = customerController.create(new Customer("jj","julien"));

        assertThat(actual.getFirstName()).isEqualTo("jean-marc");
    }

    //READ
    @Test
    public void testGetCustomerById() {

        //MockitoAnnotations.initMocks(this);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer("jean-marc", "julien")));

        customerController = new CustomerController(customerRepository);
        Optional<Customer> actual = customerController.getCustomerById(1L);

        assertThat(actual.get().getFirstName()).isEqualTo("jean-marc");
    }


    //UPDATE
    @Test
    public void testUpdateCustomer() {

        //MockitoAnnotations.initMocks(this);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer("jean-marc", "julien")));
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer("jean-marc","julien"));

        customerController = new CustomerController(customerRepository);
        Optional<Customer> actual = customerController.getCustomerById(1L);
        Customer actual2 = customerController.create(actual.get());

        assertThat(actual2.getFirstName()).isEqualTo("jean-marc");
    }

    //DELETE
    @Test
    public void testDeleteCustomer() {

        MockitoAnnotations.initMocks(this);

        Customer user = new Customer();
        user.setId(1L);
        user.setFirstName("jean-marc");
        user.setLastName("julien");

        when(customerRepository.findById(user.getId())).thenReturn(Optional.of(user));
        customerController = new CustomerController(customerRepository);

        Optional<Customer> actual = customerController.deleteCustomerById(user.getId());

        assertThat(actual.get().getFirstName()).isEqualTo("jean-marc");

    }

    //LIST
    @Test
    public void testListCustomers() {

        ArrayList<Customer> customers = new ArrayList<Customer>();
        customers.add(new Customer("jean-marc","julien"));
        customers.add(new Customer("hunt","applegate"));
        when(customerRepository.findAll()).thenReturn(customers);

        customerController = new CustomerController(customerRepository);
        Iterable<Customer> actual = customerController.getAll();

        assertThat(actual.spliterator().getExactSizeIfKnown()).isEqualTo(2);
    }
*/
}
