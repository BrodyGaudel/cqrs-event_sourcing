package com.brodygaudel.ebank.query.repository;

import com.brodygaudel.ebank.query.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("select a from Account a where a.customer.id=?1")
    List<Account> findByCustomerId(String customerId);
}
