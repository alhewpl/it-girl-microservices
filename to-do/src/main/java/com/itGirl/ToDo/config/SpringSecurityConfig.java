package com.itGirl.ToDo.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// disable security autoconfig
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // Create 1 normal user & 1 admin
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER", "ADMIN");

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
                .antMatchers(HttpMethod.POST, "toDo/create_task").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "toDo/update_task/{taskId}").hasRole("USER")
                .antMatchers(HttpMethod.GET, "toDo/fetch_task/{userEmailId}").hasRole("USER")
                .antMatchers(HttpMethod.GET, "toDo/all_tasks").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "toDo/delete_task/{userEmailId}").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }

}
