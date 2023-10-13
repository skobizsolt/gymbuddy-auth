package com.gymbuddy.auth.config;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties("firebase")
public class FirebaseTokenProperties {
    String audience;
    String issuer;
    String publicKeyProvider;
}
