package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

public class NetInterfacePropertySpecTest {

    @Test
    public void testParseString01() throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                InetAddress.getLoopbackAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Assert.assertEquals(
                netInterface,
                new NetInterfacePropertySpec("netInterfaceProperty", null)
                        .parse(netInterface.toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseStringForIllegalArgumentException01() {
        new NetInterfacePropertySpec("netInterfaceProperty", null)
                .parse("bogus");
    }

}