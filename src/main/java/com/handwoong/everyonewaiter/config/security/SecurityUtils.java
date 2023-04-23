package com.handwoong.everyonewaiter.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthentication() {
        Authentication authentication = getAuthentication();
        return !(authentication.getPrincipal() instanceof String);
    }
}
