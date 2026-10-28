package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContactValidatorTest {
    @Test public void acceptsTenDigitsStartingWithZero() {
        assertTrue(ContactValidator.isPhoneValid("0901234567"));
        assertTrue(ContactValidator.isPhoneValid("0123456789"));
    }

    @Test public void rejectsMissingWrongLengthWrongPrefixAndNonDigits() {
        String[] invalid = {null, "", "090123456", "09012345678", "1901234567",
                "+84901234567", "090 123456", "090123456a", "0901234567\n"};
        for (String phone : invalid) assertFalse("Should reject: " + phone, ContactValidator.isPhoneValid(phone));
    }

    @Test public void nameMustContainNonWhitespaceText() {
        assertFalse(ContactValidator.isNameValid(null));
        assertFalse(ContactValidator.isNameValid(""));
        assertFalse(ContactValidator.isNameValid(" \t\n "));
        assertTrue(ContactValidator.isNameValid(" Nguyễn Văn An "));
    }
}
