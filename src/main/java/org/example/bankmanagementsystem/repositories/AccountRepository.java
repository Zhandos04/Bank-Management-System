package org.example.bankmanagementsystem.repositories;

import org.example.bankmanagementsystem.Entities.Account;
import org.example.bankmanagementsystem.Entities.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findAllByCustomerId(Long customerId);
    Optional<Account> findByCustomerIdAndType(Long customerId, AccountType accountType);
}
