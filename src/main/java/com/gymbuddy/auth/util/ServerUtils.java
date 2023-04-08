package com.gymbuddy.auth.util;

import com.gymbuddy.auth.exception.ServiceExpection;
import jakarta.servlet.http.HttpServletRequest;

import static com.gymbuddy.auth.exception.Errors.UTILITY_CLASS;

public class ServerUtils {

    private ServerUtils() {
        throw new ServiceExpection(UTILITY_CLASS);
    }

    public static String applicationUrl(final HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
