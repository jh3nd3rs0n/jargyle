package com.github.jh3nd3rs0n.jargyle.test.echo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT.class,
        EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplIT.class,
        EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtectionIT.class,
        EchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtectionIT.class
})
public class AllEchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllEchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllEchoObjectsUsingSocks5ClientToSocksServerUsingSocks5GssapiMethodIT.class);
    }

}
