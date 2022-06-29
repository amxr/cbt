package com.fip.cbt.security;

import com.fip.cbt.model.Role;
import com.fip.cbt.security.jwt.JWTFilter;
import com.fip.cbt.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTFilter filter;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        String URI = "/api/v1/exam";
        String AUTH_URI = "/api/v1/auth";

        http
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .cors().and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, URI).hasAnyAuthority(Role.TESTOWNER.toString(), Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.POST, URI).hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.PUT, URI + "/update/{examNumber}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.PUT, URI + "/{examNumber}/add-questions").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.GET, URI +"/{examNumber}").hasAnyAuthority(Role.TESTOWNER.toString(), Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.PATCH, URI +"/{examNumber}/candidates/approve").hasAnyAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.PATCH, URI +"/{examNumber}/register").hasAnyAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.PATCH, URI +"/{examNumber}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.POST, URI +"/taken").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers(HttpMethod.GET, URI +"/taken").hasAnyAuthority(Role.CANDIDATE.toString(), Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.GET, URI +"/taken/{examId}").hasAnyAuthority(Role.CANDIDATE.toString(), Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.DELETE, URI +"/taken/{examId}").hasAuthority(Role.TESTOWNER.toString())
                .antMatchers(HttpMethod.POST, AUTH_URI +"/register", AUTH_URI).permitAll()
                .antMatchers("/actuator/**").permitAll()
                .and()
                .userDetailsService(userService);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
