package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class VersionTest {

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(
                Version.V5,
                Version.valueOfByte((byte) 0x05));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        Version.valueOfByte((byte) 0x04);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        Version.valueOfByte((byte) 0x06);
    }

}