package com.smartparkinglot.backendservice.serviceTest;

import com.smartparkinglot.backendservice.config.JwtTokenUtil;
import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.AccountDeactivatedException;
import com.smartparkinglot.backendservice.exceptions.AlreadyExistsException;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.exceptions.WrongCredentialsException;
import com.smartparkinglot.backendservice.repository.AccountRepository;
import com.smartparkinglot.backendservice.service.accountservice.AccountServiceImpl;
import com.smartparkinglot.backendservice.service.accountservice.JwtUserDetailsService;
import mockit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
public class AccountServiceTest {

    @Injectable
    private AccountRepository accountRepository;
    @Injectable
    private AuthenticationManager authenticationManager;
    @Injectable
    private JwtTokenUtil jwtTokenUtil;
    @Injectable
    private JwtUserDetailsService userDetailsService;

    @Tested
    private AccountServiceImpl accountService;

    LocalDateTime now;
    private List<Account> accounts;

    @BeforeEach
    public void setup(){
        now = LocalDateTime.now();
        accounts = new ArrayList<>();
    }


    @Test
    public void shouldGetAllDrivers(){
        // given
        Account acc1 = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Account acc2 = new Account(2L,"password2","email2","name2","surname2",now,"ROLE_DRIVER",false);
        accounts.add(acc1);
        accounts.add(acc2);

        //when
        new Expectations(){{
           accountRepository.findAll();
           result = accounts;
           times = 1;
        }};

        List<Account> testedAccounts = accountService.getAllAccounts();
        assertEquals(0L,testedAccounts.get(0).getId());
        assertEquals(2L,testedAccounts.get(1).getId());
        assertEquals(accounts.size(),testedAccounts.size());
    }
    @Test
    public void shouldNotGetAllDriversBecauseOfNull(){
        Throwable t = null;
        accounts = null;
        new Expectations(){{
           accountRepository.findAll();
           result = accounts;
           times = 1;
        }};
        try{
            List<Account> testedAccounts = accountService.getAllAccounts();
        }catch (NullPointerException n){
            t = n;
        }
        assertTrue(t instanceof NullPointerException);
    }
    @Test
    public void shouldGetById(){
        Account acc1 = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        new Expectations(){{
           accountRepository.findById(0L);
           result = acc1;
           times = 1;
        }};

        Account testedAccount = accountService.getById(0L);

        assertEquals(0L,testedAccount.getId());
        assertEquals(acc1,testedAccount);

    }
    @Test
    public void shouldNotGetByIdBecauseOfNotFound(){
        Throwable t = null;
        new Expectations(){{
            accountRepository.findById(1L);
            result = Optional.empty();
            times = 1;
        }};
        try{
            Account testedAcc1 = accountService.getById(1L);
        }catch (NotFoundException n ){
            t = n;
        }
        assertTrue(t instanceof  NotFoundException);
    }
    @Test
    public void shouldAddOrSave(){
        Account acc1 = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        new Expectations(){{
            accountRepository.save(acc1);
            result = acc1;
            times = 1;
        }};

        Account testedAcc1 = accountService.addOrSave(acc1);
        assertEquals(acc1,testedAcc1);
    }
    @Test
    public void shouldNotAddOrSaveBecauseAccountDeactivated() throws AccountDeactivatedException{
        Account acc1 = new Account(null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Throwable t = null;
        new Expectations(){{
           accountRepository.isExistsByEmail(anyString);
           result = true;
           times = 1;

           accountRepository.findByEmail(anyString).getIsDeleted();
           result = true;
           times = 1;
        }};

        try {
            Account testedAcc1 = accountService.addOrSave(acc1);
        }catch (AccountDeactivatedException n){
            t = n;
        }
        assertTrue(t instanceof AccountDeactivatedException);
    }
    @Test
    public void shouldNotAddOrSaveBecauseAlreadyExists() throws AlreadyExistsException{
        Throwable t = null;
        Account acc1 = new Account(null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        new Expectations(){{
            accountRepository.isExistsByEmail(anyString);
            result = true;
            times = 1;
        }};
        try{
            Account testedAcc1 = accountService.addOrSave(acc1);
        }catch (AlreadyExistsException n){
            t = n;
        }
        assertTrue(t instanceof AlreadyExistsException);
    }
    @Test
    public void shouldDeleteAccount(){
        Account acc1 = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        new Expectations(){{
           accountRepository.deleteById(anyLong);
        }};
        accountService.deleteAccount(acc1);

        new Verifications(){{
            accountRepository.deleteById(acc1.getId());
            times = 1;
        }};
    }
    @Test
    public void shouldNotDeleteAccountBecauseNotFound() throws NotFoundException{
        Account acc1 = new Account(null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Throwable t = null;
        new Expectations(){{
            accountRepository.deleteById(anyLong);
            result = new IllegalArgumentException();
        }};
        try{
            accountService.deleteAccount(acc1);
        }catch (NotFoundException n){
            t = n;
        }
        assertTrue(t instanceof NotFoundException);
    }
    @Test
    public void shouldSetDeactive(){
        Account activeAcc = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Account deactivatedAcc = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",true);
        new Expectations(){{
            accountRepository.findById(anyLong);
            result = activeAcc;
            times=1;

            accountRepository.save(withInstanceOf(Account.class));
            result = deactivatedAcc;
            times = 1;
        }};

        accountService.setDeactive(activeAcc);

        new Verifications(){{
            accountRepository.findById(0L);
            times = 1;

            accountRepository.save(deactivatedAcc);
            times = 1;
        }};


    }
    @Test
    public void shouldNotSetDeactiveBecauseNotFound()throws NotFoundException{
        Account activeAcc = new Account( null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Throwable t = null;
        new Expectations(){{
            accountRepository.findById(activeAcc.getId());
            result = Optional.empty();
            times = 1;
        }};
        try{
            accountService.setDeactive(activeAcc);
        }catch (NotFoundException n){
            t = n;
        }
        assertTrue(t instanceof NotFoundException);
    }

    @Test
    public void shouldNotSetSetDeactiveBecauseOfIllegalArgument() throws  IllegalArgumentException{
        Account activeAcc = new Account( null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Account deactivatedAcc = new Account(0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",true);
        Throwable t = null;
        new Expectations(){{
            accountRepository.findById(anyLong);
            result = activeAcc;

            accountRepository.save(withInstanceOf(Account.class));
            result = new IllegalArgumentException();
        }};
        try{
            accountService.setDeactive(activeAcc);
        }catch (IllegalArgumentException n){
            t = n;
        }
        assertTrue(t instanceof IllegalArgumentException);
    }

    @Test
    public void shouldLogin(){
        Account activeAcc = new Account( 0L,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(activeAcc.getEmail(),activeAcc.getPassword());
        new Expectations(){{
            authenticationManager.authenticate(authToken);

            userDetailsService.loadUserByUsername(activeAcc.getEmail());
            result = activeAcc;

            jwtTokenUtil.generateToken(activeAcc);
            result = "randomtoken";
        }};

        String token = accountService.Login(activeAcc);
        new Verifications(){{
            authenticationManager.authenticate(authToken);
            times = 1;
        }};

        assertEquals("randomtoken",token);

    }

    @Test
    public void shouldNotLoginBecauseDisabledException() throws AccountDeactivatedException {
        Account activeAcc = new Account(0L, "password1", "email1", "name1", "surname1", now, "ROLE_ADMIN", false);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(activeAcc.getEmail(), activeAcc.getPassword());
        Throwable t = null;
        new Expectations() {{
            authenticationManager.authenticate(authToken);
            result = new AccountDeactivatedException("");
        }};
        try {
            String token = accountService.Login(activeAcc);
        } catch (AccountDeactivatedException n) {
            t = n;
        }
        assertTrue(t instanceof AccountDeactivatedException);
    }


    @Test
    public void shouldNotLoginBecauseWrongCredentialsException() throws WrongCredentialsException {
        Account activeAcc = new Account(0L, "password1", "email1", "name1", "surname1", now, "ROLE_ADMIN", false);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(activeAcc.getEmail(), activeAcc.getPassword());
        Throwable t = null;
        new Expectations() {{
            authenticationManager.authenticate(authToken);
            result = new WrongCredentialsException("");
        }};
        try {
            String token = accountService.Login(activeAcc);
        } catch (WrongCredentialsException n) {
            t = n;
        }
        assertTrue(t instanceof WrongCredentialsException);
    }


    @Test
    public void shouldNotLoginBecauseAuthFails() throws AuthenticationException {
        Account activeAcc = new Account(0L, "password1", "email1", "name1", "surname1", now, "ROLE_ADMIN", false);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(activeAcc.getEmail(), activeAcc.getPassword());
        Throwable t = null;
        new Expectations() {{
            authenticationManager.authenticate(authToken);
            result = new AuthenticationException("") {
                @Override
                public String toString() {
                    return super.toString();
                }
            } ;
        }};
        try {
            String token = accountService.Login(activeAcc);
        } catch (AuthenticationException n) {
            t = n;
        }
        assertTrue(t instanceof AuthenticationException);
    }

    @Test
    public void shouldIsExistsById(){
        new Expectations(){{
            accountRepository.existsById(0L);
            result = true;
            times = 1;

            accountRepository.existsById(1L);
            result = false;
            times = 1;
        }};

        Boolean mustTrue = accountService.isExistsById(0L);
        Boolean mustFalse = accountService.isExistsById(1L);

        assertTrue(mustTrue);
        assertFalse(mustFalse);


    }
    @Test
    public void shouldNotIsExistsByIdBecauseNull() throws NotFoundException{
        Account activeAcc = new Account( null,"password1","email1","name1","surname1",now,"ROLE_ADMIN",false);
        Throwable t = null;
        new Expectations(){{
            accountRepository.existsById(activeAcc.getId());
            result = new NotFoundException("Id cant be null!");
        }};

        try {
            accountService.isExistsById(activeAcc.getId());
        }catch (NotFoundException n){
            t = n;
        }
        assertTrue(t instanceof NotFoundException);

    }
}
