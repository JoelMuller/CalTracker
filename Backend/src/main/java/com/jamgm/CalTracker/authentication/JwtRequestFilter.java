package com.jamgm.CalTracker.authentication;

import com.jamgm.CalTracker.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("filter request, check if before request gets to controller " + request.getRequestURI());
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));

        // if token is not validated enter if
        if(!jwtUtil.validateToken(token, userDetails)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SecurityContext context = SecurityContextHolder.getContext();
        System.out.println("SecurityContext authentication: " + context.getAuthentication());
        chain.doFilter(request, response);
    }
}
