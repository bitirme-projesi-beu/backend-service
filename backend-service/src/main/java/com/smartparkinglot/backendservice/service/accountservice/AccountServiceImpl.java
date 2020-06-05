package com.smartparkinglot.backendservice.service.accountservice;

import com.smartparkinglot.backendservice.config.JwtTokenUtil;
import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.exceptions.WrongCredentialsException;
import com.smartparkinglot.backendservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        if (accountList == null){
            throw new NullPointerException();
        }
        return accountList;
    }

    @Override
    public Account getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found with id: "+id));
        return account;
    }

    @Override
    public Account addOrSave(Account account) {
        Long accountId = account.getId();
        String accountMail = account.getEmail();

        if (accountId == null && accountRepository.isExistsByEmail(accountMail) == true){
            Account found_account = accountRepository.findByEmail(accountMail);
            if (found_account.getIsDeleted()){
                /*
                found_account.setIsDeleted(false);
                accountRepository.save(found_account);
                 */
                throw new AccountDeactivatedException("Account is deleted. Please reach the admin for reactivating the account.");
            }
            throw new AlreadyExistsException("User already exists with given credentials");
        }else{
            account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
            account.setIsDeleted(false);
            return accountRepository.save(account);
        }
    }

    @Override
    public void deleteAccount(Account account) {
        try{
            accountRepository.deleteById(account.getId());
        }catch (Exception ex){
            throw new NotFoundException("There is no id attached to account");
        }
    }

    @Override
    public void setDeactive(Account account) {
        Account searchResultAccount = accountRepository.findById(account.getId())
                .orElseThrow( () -> new NotFoundException("There is no account with this id:"+account.getId()));
        searchResultAccount.setIsDeleted(true);
        try {
            accountRepository.save(searchResultAccount);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

    @Override
    public String Login(Account account) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(),account.getPassword()));
        }catch (DisabledException e){
            throw new AccountDeactivatedException("This account deactivated earlier. Contact with admin to reaccess the account");
        }catch (BadCredentialsException e) {
            throw new WrongCredentialsException("Wrong credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(account.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public Boolean isExistsById(Long id) {
        try{
            return accountRepository.existsById(id);
        }catch (Exception e){
            throw new NotFoundException("There must be a id attached to body");
        }
    }

}
