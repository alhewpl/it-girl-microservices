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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    //@Autowired
    //CustomAuthenticationServiceProvider customAuthProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // auth.authenticationProvider(customAuthProvider);
    	
    	auth.inMemoryAuthentication()
        .withUser("user").password("{noop}password").roles("USER")
        .and()
        .withUser("admin").password("{noop}password").roles("USER", "ADMIN");
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
                .antMatchers(HttpMethod.GET, "/h2-console/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/actuator/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);; // to enable h2 console
    }

}
