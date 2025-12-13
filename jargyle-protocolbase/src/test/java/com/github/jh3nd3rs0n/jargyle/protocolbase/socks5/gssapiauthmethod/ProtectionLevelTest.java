package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import org.ietf.jgss.MessageProp;
import org.junit.Assert;
import org.junit.Test;

public class ProtectionLevelTest {

    @Test
    public void testNewMessagePropIntBoolean01() {
        MessageProp messageProp = ProtectionLevel.NONE.newMessageProp(
                0, false);
        Assert.assertNull(messageProp);
    }

    @Test
    public void testNewMessagePropIntBoolean02() {
        MessageProp messageProp = ProtectionLevel.REQUIRED_INTEG.newMessageProp(
                22, true);
        Assert.assertEquals(0, messageProp.getQOP());
        Assert.assertFalse(messageProp.getPrivacy());
    }

    @Test
    public void testNewMessagePropIntBoolean03() {
        MessageProp messageProp = ProtectionLevel.REQUIRED_INTEG_AND_CONF.newMessageProp(
                42, false);
        Assert.assertEquals(0, messageProp.getQOP());
        Assert.assertTrue(messageProp.getPrivacy());
    }

    @Test
    public void testNewMessagePropIntBoolean04() {
        MessageProp messageProp = ProtectionLevel.SELECTIVE_INTEG_OR_CONF.newMessageProp(
                96, false);
        Assert.assertEquals(96, messageProp.getQOP());
        Assert.assertFalse(messageProp.getPrivacy());
    }

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(
                ProtectionLevel.NONE,
                ProtectionLevel.valueOfByte((byte) 0x00));
    }

    @Test
    public void testValueOfByteByte02() {
        Assert.assertEquals(
                ProtectionLevel.REQUIRED_INTEG,
                ProtectionLevel.valueOfByte((byte) 0x01));
    }

    @Test
    public void testValueOfByteByte03() {
        Assert.assertEquals(
                ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                ProtectionLevel.valueOfByte((byte) 0x02));
    }

    @Test
    public void testValueOfByteByte04() {
        Assert.assertEquals(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF,
                ProtectionLevel.valueOfByte((byte) 0x03));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        ProtectionLevel.valueOfByte((byte) 0x04);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        ProtectionLevel.valueOfByte((byte) 0xff);
    }

    @Test
    public void testValueOfStringString01() {
        Assert.assertEquals(
                ProtectionLevel.NONE,
                ProtectionLevel.valueOfString("NONE"));
    }

    @Test
    public void testValueOfStringString02() {
        Assert.assertEquals(
                ProtectionLevel.REQUIRED_INTEG,
                ProtectionLevel.valueOfString("REQUIRED_INTEG"));
    }

    @Test
    public void testValueOfStringString03() {
        Assert.assertEquals(
                ProtectionLevel.REQUIRED_INTEG_AND_CONF,
                ProtectionLevel.valueOfString("REQUIRED_INTEG_AND_CONF"));
    }

    @Test
    public void testValueOfStringString04() {
        Assert.assertEquals(
                ProtectionLevel.SELECTIVE_INTEG_OR_CONF,
                ProtectionLevel.valueOfString("SELECTIVE_INTEG_OR_CONF"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException01() {
        ProtectionLevel.valueOfString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException02() {
        ProtectionLevel.valueOfString(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringStringForIllegalArgumentException03() {
        ProtectionLevel.valueOfString("REQUIRED INTEG");
    }

}