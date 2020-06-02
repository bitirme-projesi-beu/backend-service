package com.smartparkinglot.backendservice.service.driverservice;

import com.smartparkinglot.backendservice.domain.Account;

import java.util.List;

public interface DriverService {
    List<Account> getAllDrivers();
    Account getById(Long id);
    Account addOrSave(Account account);
    void deleteDriver(Account account);
    void setDeactive(Account account);
    String Login(Account account);
    Boolean isExistsById(Long id);
}
