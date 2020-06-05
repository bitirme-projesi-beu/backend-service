package com.smartparkinglot.backendservice.controller;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.service.accountservice.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/users")
public class AuthenticationController {
    @Autowired
    private AccountService accountService;

    @PostMapping("driver-sign-up") @Operation(summary = "It registers a driver to application")
    public ResponseEntity registerDriver(@RequestBody Account account){
        account.setRole("ROLE_DRIVER");
        accountService.addOrSave(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("admin-sign-up") @Operation(summary = "It registers a admin to application")
    public ResponseEntity registerAdmin(@RequestBody Account account){
        account.setRole("ROLE_ADMIN");
        accountService.addOrSave(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") @Operation(summary = "It registers a P.L owner to application")
    @PostMapping("parking-lot-owner-sign-up")
    public ResponseEntity registerPLOwner(@RequestBody Account account){
        account.setRole("ROLE_PL.OWNER");
        accountService.addOrSave(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("authenticate") @Operation(summary = "Authenticate user with email and pw and get access token")
    public ResponseEntity<String> login(@RequestBody Account account){
        return new ResponseEntity<String>(accountService.Login(account),HttpStatus.OK);
    }

}
