package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.List;
import java.util.stream.Collectors;

public class NetInterfaceTest {

    private final NetworkInterface networkInterface1;
    private final String networkInterfaceName1;
    private final NetworkInterface networkInterface2;
    private final String networkInterfaceName2;

    public NetInterfaceTest() throws SocketException {
        List<NetworkInterface> networkInterfaces =
                NetworkInterface.networkInterfaces()
                        .filter(n -> n.inetAddresses().count() > 1)
                        .collect(Collectors.toList());
        if (networkInterfaces.size() < 2) {
            throw new AssertionError(String.format(
                    "expected at least 2 network interfaces each with at "
                            + "least 2 IP addresses. actual number of "
                            + "network interfaces each with at least 2 IP "
                            + "addresses is %s",
                    networkInterfaces.size()));
        }
        this.networkInterface1 = networkInterfaces.get(0);
        this.networkInterfaceName1 = this.networkInterface1.getName();
        this.networkInterface2 = networkInterfaces.get(1);
        this.networkInterfaceName2 = this.networkInterface2.getName();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException01() {
        NetInterface.newInstanceFrom("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException02() {
        NetInterface.newInstanceFrom(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceFromStringForIllegalArgumentException03() {
        NetInterface.newInstanceFrom("bogus");
    }

    @Test
    public void testEqualsObject01() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertEquals(netInterface, netInterface);
    }

    @Test
    public void testEqualsObject02() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertNotEquals(netInterface, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        NetInterface netInterface1 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        NetInterface netInterface2 = NetInterface.newInstanceFrom(
                this.networkInterfaceName2);
        Assert.assertNotEquals(netInterface1, netInterface2);
    }

    @Test
    public void testEqualsObject05() {
        NetInterface netInterface1 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        NetInterface netInterface2 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertEquals(netInterface1, netInterface2);
    }

    @Test
    public void getHostAddressesHostAddressTypes01() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertTrue(
                netInterface.getHostAddresses(HostAddressTypes.of()).isEmpty());
    }

    @Test
    public void getHostAddressesHostAddressTypes02() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertFalse(
                netInterface.getHostAddresses(HostAddressTypes.of(
                        HostAddressType.IPV4, HostAddressType.IPV6)).isEmpty());
    }

    @Test
    public void testHashCode01() {
        NetInterface netInterface1 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        NetInterface netInterface2 = NetInterface.newInstanceFrom(
                this.networkInterfaceName2);
        Assert.assertNotEquals(netInterface1.hashCode(), netInterface2.hashCode());
    }

    @Test
    public void testHashCode02() {
        NetInterface netInterface1 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        NetInterface netInterface2 = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertEquals(netInterface1.hashCode(), netInterface2.hashCode());
    }

    @Test
    public void testToNetworkInterface01() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertEquals(
                this.networkInterface1, netInterface.toNetworkInterface());
    }

    @Test
    public void testToNetworkInterface02() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName2);
        Assert.assertEquals(
                this.networkInterface2, netInterface.toNetworkInterface());
    }

    @Test
    public void testToString01() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName1);
        Assert.assertEquals(
                this.networkInterfaceName1, netInterface.toString());
    }

    @Test
    public void testToString02() {
        NetInterface netInterface = NetInterface.newInstanceFrom(
                this.networkInterfaceName2);
        Assert.assertEquals(
                this.networkInterfaceName2, netInterface.toString());
    }

}