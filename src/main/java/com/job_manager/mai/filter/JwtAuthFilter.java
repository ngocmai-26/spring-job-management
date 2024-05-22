package com.job_manager.mai.filter;

import com.job_manager.mai.provider.JwtProvider;
import com.job_manager.mai.util.SecurityHelper;
import com.job_manager.mai.util.UrlHandlerChecker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    private final UrlHandlerChecker urlHandlerChecker;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
//            if (!urlHandlerChecker.checkUrl(request.getRequestURI())) {
//                throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders());
//            }
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = getTokenFromAuthHeader(authHeader);
            final String username = jwtProvider.extractUserName(jwt);
            if (username != null && !SecurityHelper.isLogged()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtProvider.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityHelper.setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    public String getTokenFromAuthHeader(String token) {
        return token.substring(7);
    }
}
