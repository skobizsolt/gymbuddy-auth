package com.gymbuddy.auth.util;

import com.gymbuddy.auth.exception.ServiceExpection;

import static com.gymbuddy.auth.exception.Errors.UTILITY_CLASS;

public class AuthMessage {

    private AuthMessage() {
        throw new ServiceExpection(UTILITY_CLASS);
    }

    public static final String VERIFICATION_SENT = "Verification email sent to ";
    public static final String SUCCESSFUL_VERIFICATION = "Successful verification!";
    public static final String VERIFICATION_LINK_SENT = "New verification link sent!";
    public static final String PWD_LINK_SENT = "Email sent for password reset!";
    public static final String SEND_NEW_PWD_RESET_REQUEST = "Please send a new password reset request!";
    public static final String PASSWORD_CHANGED = "Password changed successfully!";
    public static final String INVALID_TOKEN = "Invalid token!";
    public static final String EXPIRED_TOKEN = "This token is expired!";
}
