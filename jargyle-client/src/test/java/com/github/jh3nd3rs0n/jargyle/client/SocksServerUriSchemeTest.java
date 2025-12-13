package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

public class SocksServerUriSchemeTest {

    @Test
    public void testNewSocksServerUriUserInfoHostPort01() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                UserInfo.newInstance("Aladdin:opensesame"),
                Host.newInstance("0.0.0.0"),
                Port.valueOf(0)));
    }

    @Test
    public void testNewSocksServerUserInfoUriHostPort02() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                UserInfo.newInstance("Aladdin:opensesame"),
                Host.newInstance("0.0.0.0"),
                null));
    }

    @Test
    public void testNewSocksServerUserInfoUriHostPort03() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                null,
                Host.newInstance("0.0.0.0"),
                Port.valueOf(0)));
    }

    @Test
    public void testNewSocksServerUserInfoUriHostPort04() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                null,
                Host.newInstance("0.0.0.0"),
                null));
    }

    @Test
    public void testNewSocksServerUriString() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "0.0.0.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringForIllegalArgumentException() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("@@@@@@");
    }

    @Test
    public void testNewSocksServerUriStringInt() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "0.0.0.0", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringIntForIllegalArgumentException01() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "@@@@@@", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringIntForIllegalArgumentException02() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "0.0.0.0", -1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringIntForIllegalArgumentException03() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "0.0.0.0", 65536));
    }

    @Test
    public void testNewSocksServerUriStringString() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "Aladdin:opensesame", "0.0.0.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringForIllegalArgumentException01() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("@@@@@@", "0.0.0.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringForIllegalArgumentException02() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("Aladdin:opensesame", "@@@@@@@");
    }

    @Test
    public void testNewSocksServerUriStringStringInt() {
        Assert.assertNotNull(SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "Aladdin:opensesame", "0.0.0.0", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringIntForIllegalArgumentException01() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("@@@@@@", "0.0.0.0", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringIntForIllegalArgumentException02() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("Aladdin:opensesame", "@@@@@@", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringIntForIllegalArgumentException03() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("Aladdin:opensesame", "0.0.0.0", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocksServerUriStringStringIntForIllegalArgumentException04() {
        SocksServerUriScheme.SOCKS5.newSocksServerUri("Aladdin:opensesame", "0.0.0.0", 65536);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("socks5", SocksServerUriScheme.SOCKS5.toString());
    }

    @Test
    public void testValueOfString() {
        Assert.assertNotNull(SocksServerUriScheme.valueOfString("socks5"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfStringForIllegalArgumentException() {
        SocksServerUriScheme.valueOfString("socks7");
    }

}