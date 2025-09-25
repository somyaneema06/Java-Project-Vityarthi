package edu.ccrm.util;

public final class Validators {
    private Validators() {}

    public static boolean isEmailValid(String e) {
        return e != null && e.contains("@") && e.contains(".");
    }
}
