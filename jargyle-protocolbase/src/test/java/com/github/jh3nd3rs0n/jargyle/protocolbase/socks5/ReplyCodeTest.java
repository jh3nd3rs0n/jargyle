package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class ReplyCodeTest {

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x00),
                ReplyCode.SUCCEEDED);
    }

    @Test
    public void testValueOfByteByte02() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x01),
                ReplyCode.GENERAL_SOCKS_SERVER_FAILURE);
    }

    @Test
    public void testValueOfByteByte03() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x02),
                ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET);
    }

    @Test
    public void testValueOfByteByte04() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x03),
                ReplyCode.NETWORK_UNREACHABLE);
    }

    @Test
    public void testValueOfByteByte05() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x04),
                ReplyCode.HOST_UNREACHABLE);
    }

    @Test
    public void testValueOfByteByte06() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x05),
                ReplyCode.CONNECTION_REFUSED);
    }

    @Test
    public void testValueOfByteByte07() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x06),
                ReplyCode.TTL_EXPIRED);
    }

    @Test
    public void testValueOfByteByte08() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x07),
                ReplyCode.COMMAND_NOT_SUPPORTED);
    }

    @Test
    public void testValueOfByteByte09() {
        Assert.assertEquals(
                ReplyCode.valueOfByte((byte) 0x08),
                ReplyCode.ADDRESS_TYPE_NOT_SUPPORTED);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        ReplyCode.valueOfByte((byte) 0x09);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        ReplyCode.valueOfByte((byte) 0xff);
    }

}