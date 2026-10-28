package com.example.myapplication;

public final class ContactValidator {
    private ContactValidator() { }

    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isPhoneValid(String phone) {
        return phone != null && phone.matches("0[0-9]{9}");
    }
}
