package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMethodSelectionMessageTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceMethodsForIllegalArgumentException01() {
        List<Method> methodList = new ArrayList<>();
        List<Method> values = List.of(Method.values());
        for (int i = 0; i < 255; i++) {
            methodList.addAll(values);
        }
        Methods methods = Methods.of(methodList);
        ClientMethodSelectionMessage.newInstance(methods);
    }

    @Test
    public void testEqualsObject01() {
        ClientMethodSelectionMessage cmsm =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm, cmsm);
    }

    @Test
    public void testEqualsObject02() {
        ClientMethodSelectionMessage cmsm =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertNotEquals(cmsm, null);
    }

    @Test
    public void testEqualsObject03() {
        Object obj1 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Object obj2 = new Object();
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test
    public void testEqualsObject04() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.NO_ACCEPTABLE_METHODS));
        Assert.assertNotEquals(cmsm1, cmsm2);
    }

    @Test
    public void testEqualsObject05() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testHashCode01() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.NO_ACCEPTABLE_METHODS));
        Assert.assertNotEquals(cmsm1.hashCode(), cmsm2.hashCode());
    }

    @Test
    public void testHashCode02() {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstance(Methods.of(
                        Method.GSSAPI,
                        Method.USERNAME_PASSWORD,
                        Method.NO_AUTHENTICATION_REQUIRED));
        Assert.assertEquals(cmsm1.hashCode(), cmsm2.hashCode());
    }

    @Test
    public void testToByteArray01() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray02() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray03() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.GSSAPI,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray04() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(Methods.of());
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray05() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.GSSAPI,
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray06() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.GSSAPI,
                                Method.GSSAPI,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray07() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.USERNAME_PASSWORD,
                                Method.USERNAME_PASSWORD));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

    @Test
    public void testToByteArray08() throws IOException {
        ClientMethodSelectionMessage cmsm1 =
                ClientMethodSelectionMessage.newInstance(
                        Methods.of(
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.NO_AUTHENTICATION_REQUIRED,
                                Method.NO_AUTHENTICATION_REQUIRED));
        ClientMethodSelectionMessage cmsm2 =
                ClientMethodSelectionMessage.newInstanceFrom(new ByteArrayInputStream(
                        cmsm1.toByteArray()));
        Assert.assertEquals(cmsm1, cmsm2);
    }

}