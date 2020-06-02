package com.smartparkinglot.backendservice.service.driverservice;

import com.smartparkinglot.backendservice.config.JwtTokenUtil;
import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.exceptions.WrongCredentialsException;
import com.smartparkinglot.backendservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DriverServiceImpl implements DriverService {
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    public List<Account> getAllDrivers() {
        List<Account> accountList = driverRepository.findAll();
        if (accountList == null){
            throw new NullPointerException();
        }
        return accountList;
    }

    @Override
    public Account getById(Long id) {
        Account account = driverRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Account not found with id: "+id));


        return account;

    }

    @Override
    public Account addOrSave(Account account) {
        Long driver_id = account.getId();
        String driver_email = account.getEmail();

        if (driver_id == null && driverRepository.findByEmail(driver_email) != null ){
            Account found_account = driverRepository.findByEmail(driver_email);
            if (found_account.getIsDeleted()){
                /*
                found_account.setIsDeleted(false);
                driverRepository.save(found_account);
                 */
                throw new AccountDeactivatedException("Account is deleted. Please reach the admin for reactivating the account.");
            }
            throw new AlreadyExistsException("User already exists with given credentials");
        }else{
            account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
            account.setIsDeleted(false);
            return driverRepository.save(account);
        }
    }

    @Override
    public void deleteDriver(Account account) {
        if (account.getId() == null)
            throw new NotFoundException("There is no id attached to account");
        try{
            driverRepository.deleteById(account.getId());
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }

    @Override
    public void setDeactive(Account account) {
        Account driverb = driverRepository.findById(account.getId()).get();
        if (driverb == null) throw new NotFoundException("Account not found with id:"+ account.getId());
        driverb.setIsDeleted(true);
        driverRepository.save(driverb);
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

        final UserDetails userDetails =userDetailsService.loadUserByUsername(account.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;

    }

    @Override
    public Boolean isExistsById(Long id) {
        return driverRepository.existsById(id);
    }

}
