package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import org.junit.Assert;
import org.junit.Test;

public class SocketSettingsPropertySpecTest {

    @Test
    public void testParseString01() {
        Assert.assertEquals(
                SocketSettings.of(),
                new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                        .parse(""));
    }

    @Test
    public void testParseString02() {
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_SNDBUF.newSocketSetting(
                                PositiveInteger.valueOf(7240))),
                new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                        .parse("SO_SNDBUF=7240"));
    }

    @Test
    public void testParseString03() {
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_SNDBUF.newSocketSetting(
                                PositiveInteger.valueOf(7240)),
                        StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSetting(
                                PositiveInteger.valueOf(4000))),
                new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                        .parse("SO_SNDBUF=7240,SO_RCVBUF=4000"));
    }

    @Test
    public void testParseString04() {
        Assert.assertEquals(
                SocketSettings.of(
                        StandardSocketSettingSpecConstants.SO_SNDBUF.newSocketSetting(
                                PositiveInteger.valueOf(7240)),
                        StandardSocketSettingSpecConstants.SO_RCVBUF.newSocketSetting(
                                PositiveInteger.valueOf(4000)),
                        StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSetting(
                                NonNegativeInteger.valueOf(1000))),
                new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                        .parse("SO_SNDBUF=7240,SO_RCVBUF=4000,SO_TIMEOUT=1000"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                .parse(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException02() {
        new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                .parse("bogus");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException03() {
        new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                .parse("bogus=true");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException04() {
        new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                .parse("SO_SNDBUF=-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException05() {
        new SocketSettingsPropertySpec("socketSetttingsProperty", null)
                .parse("SO_SNDBUF=7240,");
    }

}