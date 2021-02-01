package com.security.university.hash;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomHashAlgorithmsTest {
    private final String TEST_TEXT = "some text to test my custom methods";

    @Test
    public void shouldReturnNotEmptyStringWhenHashMD5Called() {
        assertTrue(CustomHashAlgorithms.md5("").length() > 0);
    }

    @Test
    public void shouldReturnLengthOfStringWhenHashMD5Called() {
        int expected = 32;
        int actual = CustomHashAlgorithms.md5("").length();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashed1_WhenHashMD5Called() {
        String expected = DigestUtils.md5Hex("1");
        String actual = CustomHashAlgorithms.md5("1");
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashedSomeText_WhenHashMD5Called() {
        String expected = DigestUtils.md5Hex(TEST_TEXT);
        String actual = CustomHashAlgorithms.md5(TEST_TEXT);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashedSomeText_WhenHashSHA256Called() {
        String expected = DigestUtils.sha256Hex(TEST_TEXT);
        String actual = CustomHashAlgorithms.sha256(TEST_TEXT);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashedSomeText_WhenHashSHA1Called() {
        String expected = DigestUtils.sha1Hex(TEST_TEXT);
        String actual = CustomHashAlgorithms.sha1(TEST_TEXT);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnHashedSomeText_WhenHashSHA384Called() {
        String expected = DigestUtils.sha384Hex(TEST_TEXT);
        String actual = CustomHashAlgorithms.sha384(TEST_TEXT);
        assertEquals(expected, actual);
    }
}