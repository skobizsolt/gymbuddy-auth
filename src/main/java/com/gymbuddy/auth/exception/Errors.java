package com.gymbuddy.auth.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class Errors {

    //region Entity errors
    public static final String SHOP_NOT_FOUND = "Shop not found!";
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";
    public static final String LOCATION_NOT_FOUND = "Location not found!";
    public static final String CATEGORY_NOT_FOUND = "Category not found!";
    public static final String ADDRESS_NOT_FOUND = "Address not found!";
    //endregion

    //region Util Errors
    public static final String INCORRECT_DATE_ORDER = "Incorrect date order!";
    public static final String WRONG_EMAIL_TYPE = "Email not sent! Wrong email type!";
    public static final String PWD_RESET_ERROR = "Password reset not initiated due to an error.";
    public static final String VERIFICATION_ERROR = "Errors during verification! Please send a new registration!";
    public static final String INVALID_PASSWORD = "Password is incorrect!";
    public static final String PWD_NOT_MATCHING = "Passwords are not matching!";
    public static final String MISSING_TOKEN = "Authorization token is missing!";
    public static final String UTILITY_CLASS = "This class is a utility class!";
    public static final String FORBIDDEN_BEHAVIOUR = "Access to this page is denied!";
    //region

    private final String cause;
    private final ZonedDateTime timeStamp = ZonedDateTime.now();

    public Errors(String cause) {
        this.cause = cause;
    }
}
