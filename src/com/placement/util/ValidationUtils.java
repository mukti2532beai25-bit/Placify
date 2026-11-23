package com.placement.util;

import com.placement.exception.InvalidInputException;

public class ValidationUtils {

    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be empty.");
        }
        String clean = email.trim().toLowerCase();
        int atIndex = clean.indexOf('@');
        int dotIndex = clean.lastIndexOf('.');
        if (atIndex <= 0 || dotIndex <= atIndex + 1 || dotIndex == clean.length() - 1) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
    }

    public static void validatePhone(String phone) throws InvalidInputException {
        if (phone == null) {
            throw new InvalidInputException("Phone number cannot be null.");
        }
        String digits = phone.trim();
        if (digits.length() != 10) {
            throw new InvalidInputException("Phone number must be exactly 10 digits.");
        }
        for (int i = 0; i < digits.length(); i++) {
            if (!Character.isDigit(digits.charAt(i))) {
                throw new InvalidInputException("Phone number must contain only digits.");
            }
        }
    }

    public static void validateCgpa(double cgpa) throws InvalidInputException {
        if (cgpa < 0.0 || cgpa > 10.0) {
            throw new InvalidInputException("CGPA must be between 0.00 and 10.00.");
        }
    }

    public static void validateBacklogs(int backlogs) throws InvalidInputException {
        if (backlogs < 0) {
            throw new InvalidInputException("Backlogs cannot be negative.");
        }
    }

    public static void validatePassword(String password) throws InvalidInputException {
        if (password == null || password.trim().length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters long.");
        }
    }
}