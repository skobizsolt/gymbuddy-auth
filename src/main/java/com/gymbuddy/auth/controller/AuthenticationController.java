package com.gymbuddy.auth.controller;

import com.gymbuddy.auth.dto.PasswordDto;
import com.gymbuddy.auth.dto.TokenDto;
import com.gymbuddy.auth.dto.user.CreateUserDto;
import com.gymbuddy.auth.dto.user.LoginDto;
import com.gymbuddy.auth.service.AuthenticationService;
import com.gymbuddy.auth.util.ServerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    /**
     * Creates a new user.
     *
     * @param createUserDto      provides all essential user data
     * @param httpServletRequest HTTP servlet httpServletRequest
     * @return with MinimalUserDto, containing the created entity's data.
     */
    @PostMapping(value = "/register")
    @Operation(summary = "Create new User")
    public ResponseEntity<String> registerUser(@Valid @RequestBody final CreateUserDto createUserDto,
                                               final HttpServletRequest httpServletRequest) {
        log.info("Endpoint invoked. createUserDto = {}", createUserDto);
        return authenticationService.registerUser(createUserDto, ServerUtils.applicationUrl(httpServletRequest));
    }

    /**
     * Endpoint for verifying a registration.
     *
     * @param token that refers to the user
     * @return response message.
     */
    @GetMapping(value = "/verify-registration")
    @Operation(summary = "Verifying the newly created user")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") final String token) {
        log.info("Endpoint invoked. token = {}", token);
        return authenticationService.verifyRegistration(token);
    }

    /**
     * Endpoint for resending a valid, existing token.
     *
     * @param oldToken           that refers to the user's existing token
     * @param httpServletRequest HTTP servlet request
     * @return response message.
     */
    @GetMapping(value = "/resend-verification")
    @Operation(summary = "Generates a new, active verification token")
    public ResponseEntity<String> sendNewVerificationToken(@RequestParam("token") final String oldToken,
                                                           final HttpServletRequest httpServletRequest) {
        log.info("Endpoint invoked. oldToken = {}", oldToken);
        return authenticationService.sendNewVerificationToken(oldToken, httpServletRequest);

    }

    /**
     * Endpoint for resetting an old user password
     *
     * @param passwordDto        dto that contains an email
     * @param httpServletRequest HTTP servlet request
     * @return response message.
     */
    @PostMapping(value = "/reset-password")
    @Operation(summary = "Reset old password")
    public ResponseEntity<String> resetPassword(@RequestBody final PasswordDto passwordDto,
                                                final HttpServletRequest httpServletRequest) {
        log.info("Endpoint invoked. passwordDto = {}", passwordDto);
        return authenticationService.resetPassword(passwordDto, httpServletRequest);
    }

    /**
     * Method for changing the users pwd via link
     *
     * @param token       that refers to the user
     * @param passwordDto dto that contains the new pwd twice
     * @param request     HTTP servlet request
     * @return return response message.
     */
    @PostMapping(value = "/save-password")
    @Operation(summary = "Save new password")
    public ResponseEntity<String> saveNewPassword(@RequestParam("token") final String token,
                                                  @RequestBody final PasswordDto passwordDto,
                                                  final HttpServletRequest request) {
        log.info("Endpoint invoked. token: {}, passwordDto = {}", token, passwordDto);
        return authenticationService.saveNewPassword(token, passwordDto);
    }

    /**
     * Method for changing existing password.
     *
     * @param passwordDto dto that contains email, the old pwd and the new pwd twice.
     * @return response message.
     */
    @PostMapping(value = "/change-password")
    @Operation(summary = "Change old password to a new one", security = {@SecurityRequirement(name = "token")})
    public ResponseEntity<String> changeExistingPassword(@RequestBody final PasswordDto passwordDto) {
        log.info("Endpoint invoked. passwordDto = {}", passwordDto);
        return authenticationService.changeExistingPassword(passwordDto);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create a login token")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid final LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
