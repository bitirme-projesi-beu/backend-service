package com.smartparkinglot.backendservice.repositories;

import com.smartparkinglot.backendservice.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Long> {
    Driver findByEmail(String email);
}
