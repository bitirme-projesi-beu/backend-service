package com.smartparkinglot.backendservice.controller;


import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.service.accountservice.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasRole('ROLE_ADMIN')") @Operation(summary = "It retrieves all accounts on the application for ADMIN",security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping
    public ResponseEntity<List<Account>> getAllDrivers(){
        return new ResponseEntity<List<Account>>(accountService.getAllAccounts(),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{id}") @Operation(summary = "It retrieves account via given id on the application for ADMIN", security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity<Account> getById(@PathVariable Long id){
        return new ResponseEntity<Account>(accountService.getById(id),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DRIVER') || hasRole('ROLE_ADMIN')")
    @DeleteMapping("delete")
    @Operation(summary = "deactivates user for end-user ",security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity deactiveDriver(@RequestBody Account account){
        accountService.setDeactive(account);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin-delete")
    @Operation(summary = "deletes user for admin",security = { @SecurityRequirement(name = "bearer-key") })
    public ResponseEntity deleteDriver(@RequestBody Account account){
        accountService.deleteAccount(account);
        return new ResponseEntity(HttpStatus.OK);
    }
}






