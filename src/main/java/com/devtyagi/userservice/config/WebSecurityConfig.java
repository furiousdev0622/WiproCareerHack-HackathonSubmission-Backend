package com.devtyagi.userservice.config;

import com.devtyagi.userservice.constants.Endpoints;
import com.devtyagi.userservice.service.JwtUserDetailsService;
import com.devtyagi.userservice.util.FilterChainExceptionHandler;
import com.devtyagi.userservice.util.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUserDetailsService userDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtRequestFilter jwtRequestFilter;

    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * This method configures the HttpSecurity.
     * Allows specific routes for specific users.
     * /api/v1/auth/** routes for everyone.
     * /api/v1/user/all route for any authenticated users.
     * /api/v1/user/create route for LEVEL2 and LEVEL3 users.
     * /api/v1/delete/{id} route for LEVEL3 users.
     * @param http HttpSecurity object from Spring Security.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().and()
                .authorizeRequests()
                .antMatchers(Endpoints.BASE_URL + "/auth/**").permitAll()
                .antMatchers(Endpoints.BASE_URL + "/user/all").authenticated()
                .antMatchers(Endpoints.BASE_URL + "/user/create").hasAnyRole("LEVEL2", "LEVEL3")
                .antMatchers(Endpoints.BASE_URL + "/user/delete/**").hasRole("LEVEL3");

        /* Add JWT Filter before processing any API request. */
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        /* Add Exception Handler to handle the exceptions that occur during filter chain.
           Handles exception related to JWT */
        http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
