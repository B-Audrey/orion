package oc.mdd.utils;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    public boolean isPasswordValid(String password) {
        // Vérifie la présence d'au moins une minuscule, une majuscule, un chiffre et un caractère spécial
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[@#$%^&+=!§?*.,;:<>_\\[\\]{}()\\-].*");

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}


