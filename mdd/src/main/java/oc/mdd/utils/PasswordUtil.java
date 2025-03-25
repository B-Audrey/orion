package oc.mdd.utils;

import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    /**
     * Checks if the password is valid.
     * A valid password must have at least one lowercase letter, one uppercase letter, one digit and one special character.
     *
     * @param password the password to check
     * @return true if the password is valid, false otherwise
     */
    public boolean isPasswordValid(String password) {
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[@#$%^&+=!§?*.,;:<>_\\[\\]{}()\\-].*");

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}


