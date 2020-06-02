package com.smartparkinglot.backendservice.repository;

import com.smartparkinglot.backendservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Account,Long> {
    Account findByEmail(String email);
}
