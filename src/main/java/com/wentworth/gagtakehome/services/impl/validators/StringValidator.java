package com.wentworth.gagtakehome.services.impl.validators;

import java.util.UUID;
import java.util.regex.Pattern;

public class StringValidator {

    private static final Pattern ALPHANUMERIC_REGEX = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern ALPHA_REGEX = Pattern.compile("^[a-zA-Z ]+$");

    public static boolean isValidAlphaNumeric(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }

        return ALPHANUMERIC_REGEX.matcher(string).matches();
    }

    public static boolean isValidAlpha(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }

        return ALPHA_REGEX.matcher(string).matches();
    }

    public static boolean isValidUUID(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }

        try {
            UUID.fromString(string);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
