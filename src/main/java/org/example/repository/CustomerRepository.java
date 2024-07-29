package org.example.repository;

import org.example.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long>{
 public Customer findByCustomer(Long customerId);

}
