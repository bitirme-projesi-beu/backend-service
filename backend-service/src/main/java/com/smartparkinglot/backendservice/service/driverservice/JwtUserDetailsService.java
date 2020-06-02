package com.smartparkinglot.backendservice.service.driverservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Account acc = driverRepository.findByEmail(email);
      if (acc == null){
          throw new NotFoundException("User not found with following email -> "+email);
      }
      return new User(acc.getEmail(),acc.getPassword(),new ArrayList<>());
    }
}