package com.itGirl.ToDo.service;

import com.itGirl.ToDo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomAuthenticationServiceProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        UsernamePasswordAuthenticationToken authenticationToken = null;

        User user = userRepository.findByUserName(username);
        String hashedPwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(16));

        if (user != null) {
            if ((username.equals(user.getUserName()) && BCrypt.checkpw(password, hashedPwd))) {
                Collection<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(user);
                authenticationToken = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
            } else {
                throw new UsernameNotFoundException("User name "+ username + " not found");
            }
        }
        return authenticationToken;
    }

    private Collection<GrantedAuthority> getGrantedAuthorities(User user) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getRole().equals("ADMIN")){
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}

