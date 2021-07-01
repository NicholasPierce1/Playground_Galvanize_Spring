package com.example.demo.controllers;

import com.example.demo.domain.Customer;
import com.example.demo.Repository.CustomerRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/Customer")
    public Customer create(@RequestBody Customer customer){
        System.out.println(customer.getId());
        return this.customerRepository.save(customer);
    }

    @GetMapping("/Customer")
    public Iterable<Customer> getAll(){
        return this.customerRepository.findAll();
    }

    @GetMapping("/Customer/{id}")
    public Customer getCustomerById(@PathVariable Long id){
        return this.customerRepository.findById(id).get();
    }

    @DeleteMapping("Customer/{id}")
    public String deleteCustomerById(@PathVariable Long id){
        this.customerRepository.deleteById(id);
        return "That customer has been deleted " + id;
    }

    @PutMapping("Customer/{id}")
    public Customer updateCustomerById(@PathVariable Long id,@RequestBody Customer customer){
        Customer newCustomer = this.customerRepository.findById(id).get();
        newCustomer.setFirstName(customer.getFirstName());
        newCustomer.setLastName(customer.getLastName());
        return this.customerRepository.save(newCustomer);
    }

    @GetMapping("/CustomerByName/{lastName}")
    public List<Customer> getCustomerById(@PathVariable String lastName){
        return this.customerRepository.findByLastName(lastName);
    }

}
