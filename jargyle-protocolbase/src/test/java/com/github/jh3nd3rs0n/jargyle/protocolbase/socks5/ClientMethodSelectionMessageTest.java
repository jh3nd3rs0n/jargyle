package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

public class ClientMethodSelectionMessageTest {

    @Test
    public void testNewInstanceFromByteArray01() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstanceFrom(new byte[] {
                        (byte) 0x05,
                        (byte) 0x01,
                        (byte) 0x00 });
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testNewInstanceFromByteArray02() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstanceFrom(new byte[] {
                        (byte) 0x05,
                        (byte) 0x02,
                        (byte) 0x01,
                        (byte) 0x00 });
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testNewInstanceFromByteArray03() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstanceFrom(new byte[] {
                        (byte) 0x05,
                        (byte) 0x03,
                        (byte) 0x01,
                        (byte) 0x00,
                        (byte) 0x02, });
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.NO_AUTHENTICATION_REQUIRED,
                        Method.USERNAME_PASSWORD));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testNewInstanceFromByteArray04() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstanceFrom(new byte[] {
                        (byte) 0x05,
                        (byte) 0x06,
                        (byte) 0x01,
                        (byte) 0x00,
                        (byte) 0x02,
                        (byte) 0x01,
                        (byte) 0x00,
                        (byte) 0x02 });
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.NO_AUTHENTICATION_REQUIRED,
                        Method.USERNAME_PASSWORD,
                        Method.GSSAPI,
                        Method.NO_AUTHENTICATION_REQUIRED,
                        Method.USERNAME_PASSWORD));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testNewInstanceFromByteArray05() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstanceFrom(new byte[] {
                        (byte) 0x05,
                        (byte) 0x06,
                        (byte) 0x01,
                        (byte) 0xaa,
                        (byte) 0x02,
                        (byte) 0xbb,
                        (byte) 0x00,
                        (byte) 0xcc });
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray01() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray02() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray03() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.GSSAPI,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray04() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of());
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray05() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.GSSAPI,
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray06() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.GSSAPI,
                                Method.GSSAPI,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray07() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.USERNAME_PASSWORD,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray08() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(
                        cmsm1.toByteArray());
        Assert.assertEquals(cmsm1, cmsm2);
    }

}