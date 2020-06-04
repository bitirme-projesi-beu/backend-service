package com.smartparkinglot.backendservice.service.accountservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Value("${adminusername}")
    private String ADMIN_USERNAME;
    @Value("${adminpassword}")
    private String ADMIN_PASSWORD;
    @Value("${adminrole}")
    private String ADMIN_ROLE;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email.equals(ADMIN_USERNAME)){
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return ADMIN_ROLE;
                }
            });
            String PASSWORD =  new BCryptPasswordEncoder().encode(ADMIN_PASSWORD);
            return new User(ADMIN_USERNAME,PASSWORD, roles );
        }
        Account acc = accountRepository.findByEmail(email);
      if (acc == null){
          throw new NotFoundException("User not found with following email -> "+email);
      }


      return acc;
    }


}