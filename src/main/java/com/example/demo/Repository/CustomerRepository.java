package com.example.demo.Repository;

import com.example.demo.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<com.example.demo.domain.Customer,Long> {
    List<Customer> findByLastName(String lastName);
}