package com.gymbuddy.auth.service;

import com.gymbuddy.auth.dto.PasswordDto;
import com.gymbuddy.auth.dto.TokenDto;
import com.gymbuddy.auth.dto.TokenType;
import com.gymbuddy.auth.dto.user.CreateUserDto;
import com.gymbuddy.auth.dto.user.LoginDto;
import com.gymbuddy.auth.event.RegistrationEvent;
import com.gymbuddy.auth.exception.Errors;
import com.gymbuddy.auth.exception.ServiceExpection;
import com.gymbuddy.auth.mapper.UsersMapper;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.persistence.domain.auth.AuthenticationToken;
import com.gymbuddy.auth.persistence.query.TokenEntityMapper;
import com.gymbuddy.auth.persistence.query.UserEntityMapper;
import com.gymbuddy.auth.persistence.repository.TokenRepository;
import com.gymbuddy.auth.persistence.repository.UserRepository;
import com.gymbuddy.auth.util.AuthMessage;
import com.gymbuddy.auth.util.EmailType;
import com.gymbuddy.auth.util.ServerUtils;
import com.gymbuddy.auth.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gymbuddy.auth.util.TokenUtils.*;

/**
 * Default implementation of the Authentication service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class DefaultAuthenticationService implements AuthenticationService {
    @Autowired
    private final TokenRepository tokenRepository;
    @Autowired
    private final TokenEntityMapper tokenEntityMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UsersMapper usersMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private final MailService mailService;
    @Autowired
    private final ApplicationEventPublisher publisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> registerUser(final CreateUserDto createUserDto,
                                               final String requestUrl) {
        final User user = usersMapper.toUser(createUserDto);
        checkMatchingPasswords(createUserDto.getPassword(), createUserDto.getMatchingPassword());
        // password encryption
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        userRepository.save(user);
        publisher.publishEvent(
                new RegistrationEvent(
                        user,
                        requestUrl));
        return ResponseEntity
                .ok()
                .body(AuthMessage.VERIFICATION_SENT + user.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> verifyRegistration(final String token) {
        final String responseMessage = checkTokenCondition(token, TokenType.VERIFY);
        if (!STATUS_VALID.equals(responseMessage)) {
            throw new ServiceExpection(Errors.VERIFICATION_ERROR);
        }

        return ResponseEntity.ok().body(AuthMessage.SUCCESSFUL_VERIFICATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> sendNewVerificationToken(final String oldToken,
                                                           final HttpServletRequest httpServletRequest) {
        final AuthenticationToken verificationToken =
                getToken(oldToken, TokenType.VERIFY);
        if (verificationToken == null) {
            throw new ServiceExpection(getBadRequestResponseMessage(STATUS_INVALID));
        }
        verificationToken.setToken(UUID.randomUUID().toString());
        tokenRepository.save(verificationToken);
        mailService.sendEmail(
                ServerUtils.applicationUrl(httpServletRequest),
                verificationToken.getToken(),
                verificationToken.getUser().getEmail(),
                verificationToken.getUser().getUsername(),
                EmailType.REGISTRATION_EMAIL);
        return ResponseEntity
                .ok()
                .body(AuthMessage.VERIFICATION_LINK_SENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> resetPassword(final PasswordDto passwordDto,
                                                final HttpServletRequest httpServletRequest) {
        final User user = userEntityMapper.getUsersByEmail(passwordDto.getEmail())
                .orElseThrow(() -> new ServiceExpection(Errors.USER_NOT_FOUND));
        final AuthenticationToken existingPasswordToken =
                tokenEntityMapper.getUserRelatedToken(
                        user.getUserId(), TokenType.PASSWORD.name()).orElse(null);
        //Token not exist
        if (existingPasswordToken == null) {
            final String newPasswordToken = UUID.randomUUID().toString();
            saveToken(newPasswordToken, user, TokenType.PASSWORD);
            mailService.sendEmail(
                    ServerUtils.applicationUrl(httpServletRequest),
                    newPasswordToken,
                    passwordDto.getEmail(),
                    user.getUsername(),
                    EmailType.RESET_PASSWORD_EMAIL);
            return ResponseEntity
                    .ok()
                    .body(AuthMessage.PWD_LINK_SENT);
        }
        mailService.sendEmail(
                ServerUtils.applicationUrl(httpServletRequest),
                existingPasswordToken.getToken(),
                passwordDto.getEmail(),
                user.getUsername(),
                EmailType.RESET_PASSWORD_EMAIL);
        return ResponseEntity
                .ok()
                .body(AuthMessage.PWD_LINK_SENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> saveNewPassword(final String token, final PasswordDto passwordDto) {
        // checking if passwords are matching
        checkMatchingPasswords(passwordDto.getNewPassword(), passwordDto.getMatchingPassword());
        // checking and getting the token
        final String result = checkTokenCondition(token, TokenType.PASSWORD);
        final AuthenticationToken passwordToken = getToken(token, TokenType.PASSWORD);
        // return if token not valid
        if (!STATUS_VALID.equals(result)) {
            throw new ServiceExpection(getBadRequestResponseMessage(result)
                    + " " + AuthMessage.SEND_NEW_PWD_RESET_REQUEST);
        }
        if (passwordToken != null) {
            final User user = passwordToken.getUser();
            // checking whether the token is associated with a user or not
            if (user != null) {
                // changing password
                return changePassword(user, passwordDto.getNewPassword());
            }
        }
        // if somehow the token is not attached to any user
        throw new ServiceExpection(Errors.PWD_RESET_ERROR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> changeExistingPassword(final PasswordDto passwordDto) {
        final User user = userEntityMapper.getUsersByEmail(passwordDto.getEmail()).orElse(null);
        if (user == null) {
            throw new ServiceExpection(Errors.USER_NOT_FOUND);
        }
        checkIfGivenPassword(passwordDto.getOldPassword(), user.getPassword());
        checkMatchingPasswords(passwordDto.getNewPassword(), passwordDto.getMatchingPassword());
        return changePassword(user, passwordDto.getNewPassword());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveToken(final String token, final User user, final TokenType tokenType) {
        final AuthenticationToken verificationToken = new AuthenticationToken(token, user);
        verificationToken.setTokenType(tokenType);
        tokenRepository.save(verificationToken);
    }

    @Override
    public ResponseEntity<TokenDto> login(final LoginDto loginDto) {
        final User user = userEntityMapper.getUsersByUsername(loginDto.getUsername())
                .orElseThrow(() -> new ServiceExpection(Errors.USER_NOT_FOUND));
        checkIfGivenPassword(loginDto.getPassword(), user.getPassword());
        final String jwtToken = TokenUtils.createJwtToken(user);

        final TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(jwtToken);
        return ResponseEntity.ok().body(tokenDto);
    }

    //region UTILITY METHODS
    private String checkTokenCondition(final String tokenUuid, final TokenType tokenType) {
        final AuthenticationToken token = getToken(tokenUuid, tokenType);
        if (token == null) {
            return STATUS_INVALID;
        }
        if (TokenUtils.timeDifference(token) <= 0) {
            tokenRepository.delete(token);
            // if the verification time is expired, the registration should be deleted
            if (tokenType == TokenType.VERIFY) {
                userRepository.delete(token.getUser());
            }
            return STATUS_EXPIRED;
        }
        if (tokenType == TokenType.VERIFY) {
            final User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            tokenRepository.delete(token);
        }
        return STATUS_VALID;
    }

    private AuthenticationToken getToken(final String token, final TokenType tokenType) {
        return tokenEntityMapper.getToken(token, tokenType.name()).orElse(null);
    }

    private ResponseEntity<String> changePassword(final User user, final String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity
                .ok()
                .body(AuthMessage.PASSWORD_CHANGED);
    }

    private void checkMatchingPasswords(final String password, final String passwordAgain) {
        if (!password.equals(passwordAgain)) {
            throw new ServiceExpection(Errors.PWD_NOT_MATCHING);
        }
    }

    private String getBadRequestResponseMessage(final String response) {
        // checking if the token is exists
        if (response.equals(STATUS_INVALID)) {
            return AuthMessage.INVALID_TOKEN;
        }
        // checking if token is expired
        if (response.equals(STATUS_EXPIRED)) {
            return AuthMessage.EXPIRED_TOKEN;
        }
        return STATUS_VALID;
    }

    private void checkIfGivenPassword(final String givenPassword, final String existingPassword) {
        if (!passwordEncoder.matches(givenPassword, existingPassword)) {
            throw new ServiceExpection(Errors.INVALID_PASSWORD);
        }
    }
    //endregion
}
