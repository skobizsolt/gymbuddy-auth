package com.gymbuddy.auth.security;

import com.gymbuddy.auth.exception.Errors;
import com.gymbuddy.auth.exception.ServiceExpection;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class JwtValidationHelper {

    public JwtClaims getValidTokenClaim(JwtConsumer jwtConsumer, String substring, final String providerUrl) {
        final JwtContext jwtClaims = getJwtClaims(jwtConsumer, substring);
        boolean validToken = isTokenHasValidKeyId(jwtClaims, providerUrl);
        if (!validToken) {
            throw new ServiceExpection(Errors.INVALID_TOKEN);
        }
        return jwtClaims.getJwtClaims();
    }

    private static JwtContext getJwtClaims(final JwtConsumer jwtConsumer, final String substring) {
        try {
            return jwtConsumer.process(substring);
        } catch (InvalidJwtException e) {
            throw new ServiceExpection(Errors.INVALID_TOKEN, e);
        }
    }

    private List<String> getFirebaseKeyIds(final String providerUrl) {
        final RestTemplate restTemplate = new RestTemplate();
        final var response = (Map<String, String>) restTemplate
                .getForEntity(
                        providerUrl,
                        Map.class)
                .getBody();
        if (response == null) {
            return Collections.emptyList();
        }
        return response.keySet().stream().toList();
    }

    private boolean isTokenHasValidKeyId(final JwtContext context, String providerUrl) {
        final String kid = context.getJoseObjects().get(0).getKeyIdHeaderValue();
        final List<String> validKeys = getFirebaseKeyIds(providerUrl);
        return validKeys.contains(kid);
    }
}
