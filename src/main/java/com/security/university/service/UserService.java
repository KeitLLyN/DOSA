package com.security.university.service;

import com.security.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository USER_REPOSITORY;

    @Autowired
    public UserService(UserRepository USER_REPOSITORY){
        this.USER_REPOSITORY = USER_REPOSITORY;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return USER_REPOSITORY.findByUsername(username);
    }
}
