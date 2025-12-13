package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class SocksServerUriTest {

    @Test
    public void testNewInstance01() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1");
        try {
            Assert.assertNotNull(SocksServerUri.newInstance());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstance02() {
        Assert.assertNull(SocksServerUri.newInstance());
    }

    @Test
    public void testNewInstance04() {
        System.setProperty("socksClient.socksServerUri", "socks5://Aladdin:opensesame@127.0.0.1");
        try {
            SocksServerUri socksServerUri = SocksServerUri.newInstance();
            Assert.assertNotNull(socksServerUri);
            Assert.assertEquals(
                    SocksServerUriScheme.SOCKS5,
                    socksServerUri.getSocksServerUriScheme());
            Assert.assertEquals(
                    UserInfo.newInstance("Aladdin:opensesame"),
                    socksServerUri.getUserInfo());
            Assert.assertEquals(
                    Host.newInstance("127.0.0.1"),
                    socksServerUri.getHost());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstance05() {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1:8080");
        try {
            SocksServerUri socksServerUri = SocksServerUri.newInstance();
            Assert.assertNotNull(socksServerUri);
            Assert.assertEquals(
                    SocksServerUriScheme.SOCKS5,
                    socksServerUri.getSocksServerUriScheme());
            Assert.assertEquals(
                    Host.newInstance("127.0.0.1"),
                    socksServerUri.getHost());
            Assert.assertEquals(
                    Port.valueOf(8080),
                    socksServerUri.getPort());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstance06() {
        System.setProperty("socksClient.socksServerUri", "socks5://Aladdin:opensesame@127.0.0.1:8080");
        try {
            SocksServerUri socksServerUri = SocksServerUri.newInstance();
            Assert.assertNotNull(socksServerUri);
            Assert.assertEquals(
                    SocksServerUriScheme.SOCKS5,
                    socksServerUri.getSocksServerUriScheme());
            Assert.assertEquals(
                    UserInfo.newInstance("Aladdin:opensesame"),
                    socksServerUri.getUserInfo());
            Assert.assertEquals(
                    Host.newInstance("127.0.0.1"),
                    socksServerUri.getHost());
            Assert.assertEquals(
                    Port.valueOf(8080),
                    socksServerUri.getPort());
        } finally {
            System.clearProperty("socksClient.socksServerUri");
        }
    }

    @Test
    public void testNewInstanceFromString01() {
        Assert.assertEquals(
                "socks5://127.0.0.1",
                SocksServerUri.newInstanceFrom(
                        "socks5://127.0.0.1").toString());
    }

    @Test
    public void testNewInstanceFromString02() {
        Assert.assertEquals(
                "socks5://[::1]",
                SocksServerUri.newInstanceFrom(
                        "socks5://[::1]").toString());
    }

    @Test
    public void testNewInstanceFromString03() {
        Assert.assertEquals(
                "socks5://127.0.0.1:8080",
                SocksServerUri.newInstanceFrom(
                        "socks5://127.0.0.1:8080").toString());
    }

    @Test
    public void testNewInstanceFromString04() {
        Assert.assertEquals(
                "socks5://Aladdin:opensesame@127.0.0.1",
                SocksServerUri.newInstanceFrom(
                        "socks5://Aladdin:opensesame@127.0.0.1").toString());
    }

    @Test
    public void testNewInstanceFromString05() {
        Assert.assertEquals(
                "socks5://Aladdin:opensesame@127.0.0.1:8080",
                SocksServerUri.newInstanceFrom(
                        "socks5://Aladdin:opensesame@127.0.0.1:8080").toString());
    }

    @Test
    public void testNewInstanceFromString06() {
        Assert.assertEquals(
                "socks5://127.0.0.1",
                SocksServerUri.newInstanceFrom(
                        "socks5://127.0.0.1/methods?method=USERNAME_PASSWORD,username=Aladdin,password=opensesame").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        SocksServerUri.newInstanceFrom(
                "methods?method=USERNAME_PASSWORD,username=Aladdin,password=opensesame");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        SocksServerUri.newInstanceFrom(
                "127.0.0.1/methods?method=USERNAME_PASSWORD,username=Aladdin,password=opensesame");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException04() {
        SocksServerUri.newInstanceFrom("socks5:127.0.0.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        SocksServerUri.newInstanceFrom("      ");
    }

    @Test
    public void testNewInstanceFromURI01() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        -1,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        -1,
                        null,
                        null,
                        null)).toURI());
    }

    @Test
    public void testNewInstanceFromURI02() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        null,
                        "::1",
                        -1,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        null,
                        "::1",
                        -1,
                        null,
                        null,
                        null)).toURI());
    }

    @Test
    public void testNewInstanceFromURI03() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        8080,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        8080,
                        null,
                        null,
                        null)).toURI());
    }

    @Test
    public void testNewInstanceFromURI04() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        "Aladdin:opensesame",
                        "127.0.0.1",
                        -1,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        "Aladdin:opensesame",
                        "127.0.0.1",
                        -1,
                        null,
                        null,
                        null)).toURI());
    }

    @Test
    public void testNewInstanceFromURI05() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        "Aladdin:opensesame",
                        "127.0.0.1",
                        8080,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        "Aladdin:opensesame",
                        "127.0.0.1",
                        8080,
                        null,
                        null,
                        null)).toURI());
    }

    @Test
    public void testNewInstanceFromURI06() throws URISyntaxException {
        Assert.assertEquals(
                new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        -1,
                        null,
                        null,
                        null),
                SocksServerUri.newInstanceFrom(new URI(
                        "socks5",
                        null,
                        "127.0.0.1",
                        -1,
                        "/methods",
                        "method=USERNAME_PASSWORD,username=Aladdin,password=opensesame",
                        null)).toURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromURIForIllegalArgumentException01() throws URISyntaxException {
        SocksServerUri.newInstanceFrom(new URI(
                null,
                null,
                null,
                -1,
                "methods",
                "method=USERNAME_PASSWORD,username=Aladdin,password=opensesame",
                null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromURIForIllegalArgumentException02() throws URISyntaxException {
        SocksServerUri.newInstanceFrom(new URI(
                null,
                null,
                "127.0.0.0",
                -1,
                "/methods",
                "method=USERNAME_PASSWORD,username=Aladdin,password=opensesame",
                null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromURIForIllegalArgumentException03() throws URISyntaxException {
        SocksServerUri.newInstanceFrom(new URI(
                "socks5",
                "127.0.0.1",
                null));
    }

    @Test
    public void testGetPortOrDefault01() {
        Assert.assertEquals(
                Port.valueOf(8080),
                SocksServerUri.newInstanceFrom(
                        "socks5://127.0.0.1:8080").getPortOrDefault());
    }

    @Test
    public void testGetPortOrDefault02() {
        Assert.assertEquals(
                SocksServerUri.DEFAULT_PORT,
                SocksServerUri.newInstanceFrom(
                        "socks5://127.0.0.1").getPortOrDefault());
    }

}