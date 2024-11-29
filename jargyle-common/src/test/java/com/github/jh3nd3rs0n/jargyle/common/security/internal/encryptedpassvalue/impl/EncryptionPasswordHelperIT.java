package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpassvalue.impl;

import org.junit.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EncryptionPasswordHelperIT {

    private static final String PARTIAL_PASSWORD_1 = "This is partial password 1. ";
    private static final String PARTIAL_PASSWORD_2 = "This is partial password 2. ";

    private static Path baseDir = null;
    private static Path partialPasswordFile = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
        partialPasswordFile = baseDir.resolve("partialPasswordFile");
        Files.write(partialPasswordFile, PARTIAL_PASSWORD_1.getBytes(StandardCharsets.UTF_8));
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (partialPasswordFile != null) {
            Files.deleteIfExists(partialPasswordFile);
            partialPasswordFile = null;
        }
        if (baseDir != null) {
            Files.deleteIfExists(baseDir);
            baseDir = null;
        }
    }

    @Test
    public void testGetEncryptionPassword01() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile",
                    partialPasswordFile.toAbsolutePath().toString());
            Assert.assertArrayEquals(
                    PARTIAL_PASSWORD_1.toCharArray(),
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile");
        }
    }

    @Test
    public void testGetEncryptionPassword02() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                    PARTIAL_PASSWORD_2);
            Assert.assertArrayEquals(
                    PARTIAL_PASSWORD_2.toCharArray(),
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testGetEncryptionPassword03() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile",
                    partialPasswordFile.toAbsolutePath().toString());
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword",
                    PARTIAL_PASSWORD_2);
            Assert.assertArrayEquals(
                    PARTIAL_PASSWORD_1.concat(PARTIAL_PASSWORD_2).toCharArray(),
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile");
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPassword");
        }
    }

    @Test
    public void testGetEncryptionPassword04() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile",
                    baseDir.toAbsolutePath().toString());
            Assert.assertArrayEquals(
                    new char[] {},
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile");
        }
    }

    @Test
    public void testGetEncryptionPassword05() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile",
                    "");
            Assert.assertArrayEquals(
                    new char[] {},
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile");
        }
    }

    @Test
    public void testGetEncryptionPassword06() {
        try {
            System.setProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile",
                    " ");
            Assert.assertArrayEquals(
                    new char[] {},
                    EncryptionPasswordHelper.getEncryptionPassword());
        } finally {
            System.clearProperty(
                    "com.github.jh3nd3rs0n.jargyle.common.security.partialEncryptionPasswordFile");
        }
    }

}