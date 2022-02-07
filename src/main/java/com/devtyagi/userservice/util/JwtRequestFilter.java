package com.devtyagi.userservice.util;

import com.devtyagi.userservice.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    /**
     * This method processes the API request headers before the actual controller method.
     * Used to Extract and Validate the provided JWT token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        // Get the JWT token from request header 'Authorization'.
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            email = jwtUtil.extractUsername(jwtToken);
        }

        // Use the email extracted from JWT token to check if it is a valid user.
        // If the user's found and is valid, set the authenticated user's info in SecurityContextHolder.
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            val userDetails = userDetailsService.loadUserByUsername(email);
            if(jwtUtil.validateToken(jwtToken, userDetails)) {
                val usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}