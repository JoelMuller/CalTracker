package com.jamgm.CalTracker.Interceptors;

import com.jamgm.CalTracker.authentication.JwtUtil;
import com.jamgm.CalTracker.config.RateLimiter;
import com.jamgm.CalTracker.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private final RateLimiter rateLimiter;
    private final JwtUtil jwtUtil;

    public RateLimitFilter(RateLimiter rateLimiter, JwtUtil jwtUtil){
        this.rateLimiter = rateLimiter;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        // Skip rate limiting on public endpoints
        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        try {
            // Extract username from token
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the JWT token
            if (!jwtUtil.validateToken(token, userDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Perform rate limiting check
            if (!rateLimiter.isAllowed(username)) {
                // If rate limit is exceeded, return 429 status
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests - rate limit exceeded");
                return;
            }

            // Set user authentication
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            System.out.println("Malformed JWT token");
        }

        // Continue to the next filter if rate limiting and authentication are valid
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.startsWith("/api/user/login") || uri.startsWith("/api/user/register") || uri.startsWith("/api/swagger-ui") || uri.startsWith("/api/v3/api-docs");
    }
}
