package com.wentworth.gagtakehome.services.impl.validators;

public class NumericValidator {
    public static boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
