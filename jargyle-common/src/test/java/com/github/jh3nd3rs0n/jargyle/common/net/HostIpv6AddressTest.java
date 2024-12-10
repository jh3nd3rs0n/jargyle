package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HostIpv6AddressTest {

    @Test
    public void testEqualsObject01() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(hostIpv6Address, hostIpv6Address);
    }

    @Test
    public void testEqualsObject02() {
        HostIpv6Address hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNotEquals(hostIpv6Address, null);
    }

    @Test
    public void testEqualsObject03() {
        HostAddress hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostAddress hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNotEquals(hostIpv6Address, hostIpv4Address);
    }

    @Test
    public void testEqualsObject04() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::");
        Assert.assertNotEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void testEqualsObject05() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(hostIpv6Address1, hostIpv6Address2);
    }

    @Test
    public void testHashCode01() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertEquals(
                hostIpv6Address1.hashCode(), hostIpv6Address2.hashCode());
    }

    @Test
    public void testHashCode02() {
        HostIpv6Address hostIpv6Address1 = HostIpv6Address.newHostIpv6Address(
                "::1");
        HostIpv6Address hostIpv6Address2 = HostIpv6Address.newHostIpv6Address(
                "::");
        Assert.assertNotEquals(
                hostIpv6Address1.hashCode(), hostIpv6Address2.hashCode());
    }

    @Test
    public void testIsAllZerosIpv6AddressString01() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("::"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString02() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0::0"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString03() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address("0000::0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString04() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000::0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString05() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString06() {
        Assert.assertTrue(HostIpv6Address.isAllZerosIpv6Address(
                "0:0:0:0:0:0:0:0"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString07() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000::0000:0000:0000:0000:0000:0000:0000"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString08() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                ":::::::"));
    }

    @Test
    public void testIsAllZerosIpv6AddressString09() {
        Assert.assertFalse(HostIpv6Address.isAllZerosIpv6Address(
                "all zeros"));
    }

    @Test
    public void testNewHostIpv6AddressString01() {
        Assert.assertNotNull(HostIpv6Address.newHostIpv6Address("::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException01() {
        HostIpv6Address.newHostIpv6Address("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException02() {
        HostIpv6Address.newHostIpv6Address("127.0.0.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException06() {
        HostIpv6Address.newHostIpv6Address(
                "0000:0000:0000:0000:0000:0000:0000::0000:0000:0000:0000:0000:0000:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException07() {
        HostIpv6Address.newHostIpv6Address(":::::::");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException08() {
        HostIpv6Address.newHostIpv6Address(":0000:0000:0000:0000:0000:0000:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException09() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0000:0000:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException10() {
        HostIpv6Address.newHostIpv6Address("0.0.0.0:0000:0000:0000:0000:0000:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException11() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0.0.0.0:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException12() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0000:0000:0.0.0.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException13() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0000:999.999.999.999");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException14() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0000:0.0.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException15() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000:0000:0000:0000:0.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException16() {
        HostIpv6Address.newHostIpv6Address("zero:zero:zero:zero:zero:zero:zero:zero");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException17() {
        HostIpv6Address.newHostIpv6Address("::1%10000000000000000000000000000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException18() {
        HostIpv6Address.newHostIpv6Address("::1%bogus");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException19() {
        HostIpv6Address.newHostIpv6Address(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException20() {
        HostIpv6Address.newHostIpv6Address("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException21() {
        HostIpv6Address.newHostIpv6Address("0000:0000:0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException22() {
        HostIpv6Address.newHostIpv6Address("0000: :0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv6AddressStringForIllegalArgumentException23() {
        HostIpv6Address.newHostIpv6Address(":");
    }

    @Test
    public void testToInetAddress01() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::1").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("0:0:0:0:0:0:0:1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress02() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "1080::8:800:200C:417A").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("1080:0:0:0:8:800:200C:417A");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress03() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "1080:8:800:200C::417A").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("1080:8:800:200C:0:0:0:417A");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress04() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::1080:8:800:200C:417A").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("0:0:0:1080:8:800:200C:417A");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress05() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "1080:8:800:200C:417A::").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("1080:8:800:200C:417A:0:0:0");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress06() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::255.255.0.6").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("::FFFF:6");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress07() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::1%0").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("::1%0");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress08() throws UnknownHostException, SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                InetAddress.getByName("::1"));
        String networkInterfaceName = networkInterface.getName();
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "::1%".concat(networkInterfaceName)).toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("::1%".concat(
                networkInterfaceName));
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testToInetAddress09() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv6Address.newHostIpv6Address(
                "0000:0000:0000:0000:0000:0000:0.0.0.0").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName(
                "0000:0000:0000:0000:0000:0000:0.0.0.0");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

}