package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilePropertySpecIT {

    private static Path baseDir = null;
    private static Path actualFile = null;
    private static Path bogusFile = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
        actualFile = Files.createFile(baseDir.resolve("actualFile"));
        bogusFile = baseDir.resolve("bogusFile");
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (bogusFile != null) {
            bogusFile = null;
        }
        if (actualFile != null) {
            Files.deleteIfExists(actualFile);
            actualFile = null;
        }
        if (baseDir != null) {
            Files.deleteIfExists(baseDir);
            baseDir = null;
        }
    }

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                actualFile.toFile(),
                new FilePropertySpec("fileProperty", null).parse(
                        actualFile.toFile().toString()));
    }

    @Test
    public void testValidateFile01() {
        Assert.assertEquals(
                actualFile.toFile(),
                new FilePropertySpec("fileProperty", null).validate(
                        actualFile.toFile()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateFileForIllegalArgumentException01() {
        new FilePropertySpec("fileProperty", null).validate(
                baseDir.toFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateFileForIllegalArgumentException02() {
        new FilePropertySpec("fileProperty", null).validate(
                bogusFile.toFile());
    }

}