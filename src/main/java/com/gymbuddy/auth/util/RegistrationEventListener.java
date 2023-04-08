package com.gymbuddy.auth.util;

import com.gymbuddy.auth.dto.TokenType;
import com.gymbuddy.auth.event.RegistrationEvent;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.service.AuthenticationService;
import com.gymbuddy.auth.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private MailService mailService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(final RegistrationEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        authenticationService.saveToken(token, user, TokenType.VERIFY);

        // send verification email
        mailService.sendEmail(
                event.getApplicationUrl(),
                token,
                user.getEmail(),
                user.getUsername(), EmailType.REGISTRATION_EMAIL);
    }
}
