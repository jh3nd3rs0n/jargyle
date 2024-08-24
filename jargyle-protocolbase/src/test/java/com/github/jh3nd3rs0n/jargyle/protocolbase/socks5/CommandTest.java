package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class CommandTest {

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(Command.CONNECT, Command.valueOfByte((byte) 0x01));
    }

    @Test
    public void testValueOfByteByte02() {
        Assert.assertEquals(Command.BIND, Command.valueOfByte((byte) 0x02));
    }

    @Test
    public void testValueOfByteByte03() {
        Assert.assertEquals(Command.UDP_ASSOCIATE, Command.valueOfByte((byte) 0x03));
    }

    @Test
    public void testValueOfByteByte04() {
        Assert.assertEquals(Command.RESOLVE, Command.valueOfByte((byte) 0x04));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        Command.valueOfByte((byte) 0x00);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        Command.valueOfByte((byte) 0xff);
    }

    @Test
    public void testValueOfStringString01() {
        Assert.assertEquals(Command.CONNECT, Command.valueOfString("CONNECT"));
    }

    @Test
    public void testValueOfStringString02() {
        Assert.assertEquals(Command.BIND, Command.valueOfString("BIND"));
    }

    @Test
    public void testValueOfStringString03() {
        Assert.assertEquals(Command.UDP_ASSOCIATE, Command.valueOfString("UDP_ASSOCIATE"));
    }

    @Test
    public void testValueOfStringString04() {
        Assert.assertEquals(Command.RESOLVE, Command.valueOfString("RESOLVE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException01() {
        Command.valueOfString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException02() {
        Command.valueOfString(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException03() {
        Command.valueOfString("UDP ASSOCIATE");
    }

}