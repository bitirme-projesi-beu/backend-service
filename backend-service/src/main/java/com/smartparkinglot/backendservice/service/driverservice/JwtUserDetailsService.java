package com.smartparkinglot.backendservice.service.driverservice;

import com.smartparkinglot.backendservice.domain.Account;
import com.smartparkinglot.backendservice.exceptions.NotFoundException;
import com.smartparkinglot.backendservice.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
      List<String> roles = new ArrayList<>();
      roles.add(acc.getRole());
      List<GrantedAuthority> grantedAuthorities = roles.stream().map(r -> {
          return new SimpleGrantedAuthority(r);
      }).collect(Collectors.toList());
      return new User(acc.getEmail(),acc.getPassword(),grantedAuthorities);
    }
}