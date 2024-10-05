package com.github.jh3nd3rs0n.jargyle.protocolbase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssapiMethodEncapsulationTest.class,
        com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssDatagramSocketTest.class,
        com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssSocketTest.class,
})
public class AllGssObjectTests {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        TestGssEnvironment.setUpBeforeClass(AllGssObjectTests.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        TestGssEnvironment.tearDownAfterClass(AllGssObjectTests.class);
    }

}
