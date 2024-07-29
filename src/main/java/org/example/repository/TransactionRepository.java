package org.example.repository;

import org.example.entity.Transaction;

import java.security.Timestamp;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction,Long>{

    public List<Transaction> findAllByCustomerId(Long customerId);

    public List<Transaction> findAllByCustomerIdAndTransactionDateBetween(Long customerId, Timestamp from, Timestamp to);


}
