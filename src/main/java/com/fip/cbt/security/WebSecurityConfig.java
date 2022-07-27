package com.fip.cbt.security;

import com.fip.cbt.model.Role;
import com.fip.cbt.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final String URI = "/api/v1/exam";
    private final String AUTHURI = "/api/v1/auth";

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, URI).hasAnyAuthority(Role.TESTOWNER.toString(), Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.POST, URI).hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.PUT, URI + "/update/{examNumber}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.GET, URI + "/scheduled").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.PUT, URI + "/{examNumber}/add-questions").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.GET, URI+"/{examNumber}").hasAnyAuthority(Role.TESTOWNER.toString(), Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.PATCH, URI+"/{examNumber}/candidates/approve").hasAnyAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.PATCH, URI+"/{examNumber}/register").hasAnyAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.PATCH, URI+"/{examNumber}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.POST, URI+"/taken").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.GET, URI+"/taken").hasAnyAuthority(Role.CANDIDATE.toString(), Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.GET, URI+"/taken/{examId}").hasAnyAuthority(Role.CANDIDATE.toString(), Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.DELETE, URI+"/taken/{examId}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.POST, AUTHURI+"/register", AUTHURI).permitAll()
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
