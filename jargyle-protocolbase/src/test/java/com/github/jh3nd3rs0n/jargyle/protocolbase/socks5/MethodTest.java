package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class MethodTest {

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(
                Method.NO_AUTHENTICATION_REQUIRED,
                Method.valueOfByte((byte) 0x00));
    }

    @Test
    public void testValueOfByteByte02() {
        Assert.assertEquals(Method.GSSAPI, Method.valueOfByte((byte) 0x01));
    }

    @Test
    public void testValueOfByteByte03() {
        Assert.assertEquals(
                Method.USERNAME_PASSWORD, Method.valueOfByte((byte) 0x02));
    }

    @Test
    public void testValueOfByteByte04() {
        Assert.assertEquals(
                Method.NO_ACCEPTABLE_METHODS, Method.valueOfByte((byte) 0xff));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        Method.valueOfByte((byte) 0xfe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        Method.valueOfByte((byte) 0x03);
    }

    @Test
    public void testValueOfStringString01() {
        Assert.assertEquals(
                Method.NO_AUTHENTICATION_REQUIRED,
                Method.valueOfString("NO_AUTHENTICATION_REQUIRED"));
    }

    @Test
    public void testValueOfStringString02() {
        Assert.assertEquals(Method.GSSAPI, Method.valueOfString("GSSAPI"));
    }

    @Test
    public void testValueOfStringString03() {
        Assert.assertEquals(
                Method.USERNAME_PASSWORD,
                Method.valueOfString("USERNAME_PASSWORD"));
    }

    @Test
    public void testValueOfStringString04() {
        Assert.assertEquals(
                Method.NO_ACCEPTABLE_METHODS,
                Method.valueOfString("NO_ACCEPTABLE_METHODS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException01() {
        Method.valueOfString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException02() {
        Method.valueOfString(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException03() {
        Method.valueOfString("USERNAME PASSWORD");
    }

}