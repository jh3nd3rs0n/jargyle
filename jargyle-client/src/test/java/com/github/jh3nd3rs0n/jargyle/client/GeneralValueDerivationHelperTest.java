package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import org.junit.Assert;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GeneralValueDerivationHelperTest {

    @Test
    public void testGetClientBindHostFromProperties01() {
        Host host = HostIpv4Address.getAllZerosInstance();
        Properties properties = Properties.of();
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientBindHostFrom(properties));
    }

    @Test
    public void testGetClientBindHostFromProperties02() {
        Host host = Host.newInstance("127.0.0.1");
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST.newProperty(host));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientBindHostFrom(properties));
    }

    @Test
    public void testGetClientBindHostFromProperties03() throws SocketException, UnknownHostException {
        Host host = Host.newInstance("127.0.0.1");
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(
                host.toInetAddress());
        NetInterface netInterface = NetInterface.newInstance(networkInterface);
        Properties properties = Properties.of(
                GeneralPropertySpecConstants.CLIENT_NET_INTERFACE.newProperty(netInterface),
                GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES.newProperty(
                        HostAddressTypes.of(HostAddressType.IPV4)));
        Assert.assertEquals(
                host,
                GeneralValueDerivationHelper.getClientBindHostFrom(properties));
    }

}