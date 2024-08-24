package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import org.junit.Assert;
import org.junit.Test;

import java.net.Socket;

public class MethodEncapsulationTest {

    @Test
    public void testNewNullInstanceSocket() {
        Assert.assertNotNull(MethodEncapsulation.newNullInstance(new Socket()));
    }

}