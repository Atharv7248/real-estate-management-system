package com.reindeer.trail.util;

public class CurrentUser {
    private static String email;
    private static String role;

    public static void set(String emailVal, String roleVal) {
        email = emailVal;
        role = roleVal;
    }

    public static String getEmail() {
        return email;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        email = null;
        role = null;
    }
}
