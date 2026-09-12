package com.aurora.parking.infrastructure.persistence.security;

import com.aurora.parking.execption.JwtValidationException;
import com.aurora.parking.role.domain.RoleCode;
import com.aurora.parking.role.domain.RolePermissions;
import com.aurora.parking.security.application.CurrentOperator;
import com.aurora.parking.security.application.JwtTokenPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Extrai o CurrentOperator do JWT (Authorization: Bearer ...).
 * NUNCA loga o token completo (§34, §46).
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenPort jwtTokenPort;

    public JwtAuthenticationFilter(JwtTokenPort jwtTokenPort) {
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var decoded = jwtTokenPort.validateAccessToken(token);
                RoleCode role = RoleCode.valueOf(decoded.role());

                CurrentOperator operator = new CurrentOperator(
                        decoded.userId(), decoded.username(), role, RolePermissions.of(role));

                List<GrantedAuthority> authorities = operator.permissions().stream()
                        .map(p -> (GrantedAuthority) new SimpleGrantedAuthority(p.name()))
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(operator, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtValidationException e) {
                log.debug("Falha na validação do access token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}