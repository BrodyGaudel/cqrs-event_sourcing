package com.brodygaudel.ebank.query.repository;

import com.brodygaudel.ebank.query.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("select case when count(c)>0 then true else false END from Customer c where c.nic=?1")
    Boolean checkIfNicExists(String nic);

    @Query("select c from Customer c where c.firstname like :keyword or c.name like :keyword or c.nic like :keyword order by c.firstname DESC")
    Page<Customer> search(@Param("keyword") String keyword, Pageable pageable);
}
