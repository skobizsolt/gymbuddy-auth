package com.gymbuddy.auth.util;

import com.gymbuddy.auth.config.JwtAuthenticationFilter;
import com.gymbuddy.auth.exception.ServiceExpection;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.persistence.domain.auth.AuthenticationToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import static com.gymbuddy.auth.exception.Errors.UTILITY_CLASS;

public class TokenUtils {

    public static final String STATUS_VALID = "valid";
    public static final String STATUS_EXPIRED = "expired";
    public static final String STATUS_INVALID = "invalid";

    private TokenUtils() {
        throw new ServiceExpection(UTILITY_CLASS);
    }

    public static Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public static long timeDifference(final AuthenticationToken token) {
        final Calendar calendar = Calendar.getInstance();
        return token.getExpiryDate().getTime() - calendar.getTime().getTime();
    }

    public static String createJwtToken(final User user) {
        return Jwts.builder()
                .claim("name", user.getUsername())
                .setSubject(user.getUsername())
                .setId(user.getUserId().toString())
                .setIssuedAt(new Date())
                .setAudience(JwtAuthenticationFilter.AUDIENCE)
                .setIssuer(JwtAuthenticationFilter.ISSUER)
                .setExpiration(Date.from(
                        LocalDateTime.now()
                                .plusWeeks(JwtAuthenticationFilter.MAX_TIME_TO_LIVE_WEEKS)
                                .toInstant(ZoneOffset.UTC)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getSigningKey() {
        byte[] keyBytes = JwtAuthenticationFilter.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}