package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import org.junit.Assert;
import org.junit.Test;

public class SocksClientAgentTest {

    @Test
    public void testToSocksClientIOExceptionThrowable01() {
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(Properties.of());
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        SocksClientIOException e = new SocksClientIOException(
                socksClient, new Exception());
        Assert.assertEquals(e, socksClientAgent.toSocksClientIOException(e));
    }

    @Test
    public void testToSocksClientIOExceptionThrowable02() {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1", 1080)
                .newSocksClient(Properties.of());
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1", 2080)
                .newSocksClient(Properties.of());
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient2);
        Throwable t = new Exception();
        SocksClientSocketException e1 = new SocksClientSocketException(
                socksClient1, t);
        SocksClientIOException e2 = socksClientAgent.toSocksClientIOException(
                e1);
        Assert.assertEquals(socksClient1, e2.getSocksClient());
        Assert.assertEquals(t, e2.getCause());
    }

    @Test
    public void testToSocksClientIOExceptionThrowable03() {
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(Properties.of());
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientIOException e = socksClientAgent.toSocksClientIOException(
                t);
        Assert.assertEquals(socksClient, e.getSocksClient());
        Assert.assertEquals(t, e.getCause());
    }

    @Test
    public void testToSocksClientSocketExceptionThrowable01() {
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(Properties.of());
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientSocketException e = new SocksClientSocketException(
                socksClient, t);
        Assert.assertEquals(e, socksClientAgent.toSocksClientSocketException(e));
    }

    @Test
    public void testToSocksClientSocketExceptionThrowable02() {
        SocksClient socksClient = SocksServerUriScheme.SOCKS5
                .newSocksServerUri("127.0.0.1")
                .newSocksClient(Properties.of());
        SocksClientAgent socksClientAgent = new SocksClientAgent(socksClient);
        Throwable t = new Exception();
        SocksClientSocketException e =
                socksClientAgent.toSocksClientSocketException(t);
        Assert.assertEquals(socksClient, e.getSocksClient());
        Assert.assertEquals(t, e.getCause());
    }

}