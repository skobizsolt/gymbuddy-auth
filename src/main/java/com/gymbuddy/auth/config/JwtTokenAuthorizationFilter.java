package com.gymbuddy.auth.config;

import com.gymbuddy.auth.exception.Errors;
import com.gymbuddy.auth.exception.ServiceExpection;
import com.gymbuddy.auth.security.JwtAuthentication;
import com.gymbuddy.auth.security.JwtValidationHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseTokenProperties.class)
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> UNFILTERED_URLS = List.of(
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/webjars/swagger-ui/**");

    private final FirebaseTokenProperties tokenProperties;
    private final JwtValidationHelper jwtValidationHelper;

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) {
        final String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderValue == null
                || !StringUtils.startsWithIgnoreCase(authorizationHeaderValue, TOKEN_PREFIX)) {
            throw new ServiceExpection(Errors.MISSING_TOKEN, "No token provided");
        }

        final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipSignatureVerification()
                .setExpectedAudience(tokenProperties.getAudience())
                .setExpectedIssuer(tokenProperties.getIssuer())
                .setRelaxVerificationKeyValidation().build();

        final String substring = authorizationHeaderValue.substring(TOKEN_PREFIX.length());
        final JwtClaims jwtClaims = jwtValidationHelper.getValidTokenClaim(
                jwtConsumer,
                substring,
                tokenProperties.getPublicKeyProvider());

        authorizeUser(jwtClaims);

        filterRequest(request, response, filterChain);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        return UNFILTERED_URLS.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    private GrantedAuthority map(final String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }


    private void authorizeUser(JwtClaims jwtClaims) {
        final String subject;
        try {
            subject = jwtClaims.getSubject();
        } catch (MalformedClaimException e) {
            throw new ServiceExpection(Errors.USER_NOT_IDENTIFIED);
        }

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthentication(subject, Collections.singletonList(map("USER"))));
    }

    private static void filterRequest(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new ServiceExpection(Errors.AUTHORIZATION_ERROR, e);
        }
    }
}
