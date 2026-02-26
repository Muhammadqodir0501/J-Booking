package com.example.jbooking.security;

import com.example.jbooking.entity.User;
import com.example.jbooking.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ForbiddenException("Пользователь не авторизован");
        }

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return securityUser.getUser();
    }

    public static UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}