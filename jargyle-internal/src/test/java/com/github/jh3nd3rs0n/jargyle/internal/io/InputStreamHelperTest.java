package com.github.jh3nd3rs0n.jargyle.internal.io;

import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class InputStreamHelperTest {

    @Test
    public void testContinuouslyReadFromInputStreamByteArray() throws IOException {
        byte[] expectedBytes = new byte[0];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray00() throws IOException {
        byte[] expectedBytes = new byte[] {
                (byte) 0x01, (byte) 0x02, (byte) 0x03 };
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length + 1];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray01() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_01.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray02() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_02.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray03() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_03.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray04() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_04.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray05() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_05.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray06() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_06.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray07() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_07.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray08() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_08.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testContinuouslyReadFromInputStreamByteArray09() throws IOException {
        byte[] expectedBytes = TestStringConstants.STRING_09.getBytes(
                StandardCharsets.UTF_8);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                expectedBytes);
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = InputStreamHelper.continuouslyReadFrom(
                byteArrayInputStream, actualBytes);
        actualBytes = Arrays.copyOf(actualBytes, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

}