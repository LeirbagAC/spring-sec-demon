package com.gabriel.spring_sec_demon.service;

import com.gabriel.spring_sec_demon.dao.UserPrincipal;
import com.gabriel.spring_sec_demon.dao.UserRepo;
import com.gabriel.spring_sec_demon.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username)  throws UsernameNotFoundException {

        User user = repo.findByUsername(username);

        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User 404");
        }

        return new UserPrincipal(user);
    }

}
