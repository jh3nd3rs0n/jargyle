package com.github.jh3nd3rs0n.jargyle.test.help.string;

import java.nio.charset.StandardCharsets;

/**
 * Class of {@code String} constants used for testing.
 */
public final class TestStringConstants {

    /**
     * Test {@code String} constant: {@code Hello, World!} (13 bytes in UTF-8).
     */
    public static final String STRING_01 = "Hello, World!";

    /**
     * Test {@code String} constant:
     * {@code The quick brown fox jumped over the lazy dog.} (45 bytes in 
     * UTF-8).
     */
    public static final String STRING_02 =
            "The quick brown fox jumped over the lazy dog.";

    /**
     * Test {@code String} constant consisting of digits {@code 0} to
     * {@code 9} followed by uppercase letters {@code A} to {@code Z}
     * followed by lowercase letters {@code a} to {@code z} (62 bytes in 
     * UTF-8).
     */
    public static final String STRING_03 =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    /**
     * Test {@code String} constant of {@link TestStringConstants#STRING_03} 
     * repeating 100 times (6200 bytes in UTF-8).
     */
    public static final String STRING_04;

    /**
     * Test {@code String} constant of {@link TestStringConstants#STRING_03}
     * repeating 1000 times (62000 bytes in UTF-8).
     */
    public static final String STRING_05;

    /**
     * Test {@code String} constant of the characters in 
     * {@link TestStringConstants#STRING_03} repeating until the total number 
     * of bytes of 65535 in UTF-8 is reached.
     */    
    public static final String STRING_06;

    /**
     * Test {@code String} constant of the characters in 
     * {@link TestStringConstants#STRING_03} repeating until the total number 
     * of bytes of 65535 + 1 in UTF-8 is reached.
     */    
    public static final String STRING_07;

    /**
     * Test {@code String} constant of the characters in 
     * {@link TestStringConstants#STRING_03} repeating until the total number 
     * of bytes of 65535 * 2 in UTF-8 is reached.
     */    
    public static final String STRING_08;

    /**
     * Test {@code String} constant of the characters in 
     * {@link TestStringConstants#STRING_03} repeating until the total number 
     * of bytes of 65535 * 2 + 1 in UTF-8 is reached.
     */    
    public static final String STRING_09;
    
    static {
        StringBuilder stringBuilder04 = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            stringBuilder04.append(STRING_03);
        }
        STRING_04 = stringBuilder04.toString();
        StringBuilder stringBuilder05 = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            stringBuilder05.append(STRING_03);
        }
        STRING_05 = stringBuilder05.toString();
        StringBuilder stringBuilder06 = new StringBuilder();
        for (int i = 0, j = 0; i < 65535; i++, j++) {
            if (j == STRING_03.length()) {
                j = 0;
            }
            stringBuilder06.append(STRING_03.charAt(j));
        }
        STRING_06 = stringBuilder06.toString();
        StringBuilder stringBuilder07 = new StringBuilder();
        for (int i = 0, j = 0; i < 65535 + 1; i++, j++) {
            if (j == STRING_03.length()) {
                j = 0;
            }
            stringBuilder07.append(STRING_03.charAt(j));
        }
        STRING_07 = stringBuilder07.toString();
        StringBuilder stringBuilder08 = new StringBuilder();
        for (int i = 0, j = 0; i < 65535 * 2; i++, j++) {
            if (j == STRING_03.length()) {
                j = 0;
            }
            stringBuilder08.append(STRING_03.charAt(j));
        }        
        STRING_08 = stringBuilder08.toString();
        StringBuilder stringBuilder09 = new StringBuilder();
        for (int i = 0, j = 0; i < 65535 * 2 + 1; i++, j++) {
            if (j == STRING_03.length()) {
                j = 0;
            }
            stringBuilder09.append(STRING_03.charAt(j));
        }
        STRING_09 = stringBuilder09.toString();        
    }

    /**
     * Prevents the construction of unnecessary instances.
     */
    private TestStringConstants() {
    }

}
