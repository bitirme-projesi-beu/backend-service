package com.smartparkinglot.backendservice.service.accountservice;

import com.smartparkinglot.backendservice.domain.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();
    Account getById(Long id);
    Account getAuthenticatedProfile();
    Account addOrSave(Account account);
    void deleteAccount(Account account);
    void setDeactive(Account account);
    String Login(Account account);
    Boolean isExistsById(Long id);
}
