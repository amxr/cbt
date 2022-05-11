package com.fip.cbt.security;

import com.fip.cbt.model.Role;
import com.fip.cbt.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private String URI = "/api/v1/exam";

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .cors().configurationSource(request -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowedOrigins(List.of("https://morning-gorge-09623.herokuapp.com/"));
                    cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    return cors;
                }).and()
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, URI + "/taken").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.GET, URI + "/taken/**").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers("/api/v1/exam/**").hasAuthority(Role.ADMINISTRATOR.toString())
                .antMatchers(HttpMethod.POST, "/api/v1/auth/user").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(userService)
                .passwordEncoder(encoder);
    }
}
