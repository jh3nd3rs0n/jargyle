package com.github.jh3nd3rs0n.jargyle.test.echo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodNecReferenceImplIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodNecReferenceImplWithSelectiveIntegOrConfProtectionIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodWithSelectiveIntegOrConfProtectionIT.class
})
public class AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiMethodIT.class);
    }

}
