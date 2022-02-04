package com.devtyagi.userservice.service;

import com.devtyagi.userservice.repository.UserRepository;
import com.devtyagi.userservice.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        val user = userRepository.findUserByEmailAddress(email);
        if(user == null) {
            throw new UsernameNotFoundException("No user exists with the given email!");
        }
        return new CustomUserDetails(user);
    }
}