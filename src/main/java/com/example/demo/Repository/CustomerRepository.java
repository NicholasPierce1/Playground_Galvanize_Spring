package com.example.demo.Repository;

import com.example.demo.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<com.example.demo.domain.Customer,Long> {
    List<Customer> findByLastName(String lastName);
}