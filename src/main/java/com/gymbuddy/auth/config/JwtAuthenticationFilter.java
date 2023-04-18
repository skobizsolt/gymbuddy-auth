package com.gymbuddy.auth.config;

import com.gymbuddy.auth.exception.Errors;
import com.gymbuddy.auth.exception.ServiceExpection;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.persistence.query.UserEntityMapper;
import com.gymbuddy.auth.security.JwtAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserEntityMapper userEntityMapper;

    private static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "GymBuĐĐYTrAin3RAppAuthent1cat1oN2023!";
    public static final String AUDIENCE = "GymBuddy Auth";
    public static final String ISSUER = "GymBuddy";
    public static final int MAX_TIME_TO_LIVE_WEEKS = 2;
    private static final HmacKey TOKEN_SECRET_KEY = new HmacKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    @SneakyThrows({
            InvalidJwtException.class,
            MalformedClaimException.class,
            ChangeSetPersister.NotFoundException.class,
            ServletException.class,
            IOException.class})
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) {
        final String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeaderValue == null
                || !StringUtils.startsWithIgnoreCase(authorizationHeaderValue, TOKEN_PREFIX)) {
            throw new ServiceExpection(Errors.MISSING_TOKEN);
        }

        final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setVerificationKey(TOKEN_SECRET_KEY)
                .setExpectedAudience(AUDIENCE)
                .setExpectedIssuer(ISSUER)
                .setRelaxVerificationKeyValidation().build();

        final String substring = authorizationHeaderValue.substring(TOKEN_PREFIX.length());
        final JwtClaims jwtClaims = jwtConsumer.processToClaims(substring);

        final String subject = jwtClaims.getSubject();
        final Optional<User> maybeUser = userEntityMapper.getUsersByUsername(subject);
        if (maybeUser.isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }

        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthentication(subject, Collections.singletonList(map(maybeUser.get().getRole().name()))));

        filterChain.doFilter(request, response);
    }

    private GrantedAuthority map(final String role) {
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}