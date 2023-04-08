package com.gymbuddy.auth.security;

import com.gymbuddy.auth.dto.AuthRoles;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.service.UserService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserService userService;
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * checks if the authenticated user have access to the user detail with the id
     * Only a user have access to his own user details or an admin
     */
    public boolean hasUser(final Long userId) {
        String authenticatedUserName = this.getAuthentication().getName();
        final User user = this.userService.getUserEntityById(userId);
        return authenticatedUserName.equals(user.getUsername()) || user.getRole() == AuthRoles.ADMIN;
    }

    // We need this setter method to set the UserService from another class
    // because this one doesn't have access to Application Context.
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void setFilterObject(final Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(final Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }
}
