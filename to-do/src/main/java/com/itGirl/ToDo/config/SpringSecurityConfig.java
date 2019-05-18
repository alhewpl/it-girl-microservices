package com.itGirl.ToDo.config;

import com.itGirl.ToDo.service.CustomAuthenticationServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// disable security autoconfig
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomAuthenticationServiceProvider customAuthProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider);
    }


    // Secure the endpoints with HTTP Basic authentication.
    // Normal user can access all requests except for delete.
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "toDo/create_task").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT, "toDo/update_task/{taskId}").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "toDo/fetch_task/{userEmailId}").hasAuthority("USER")
                .antMatchers(HttpMethod.GET, "toDo/all_tasks").hasAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "toDo/delete_task/{userEmailId}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "actuator/**").hasAuthority("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable(); // to enable h2 console
    }

}
