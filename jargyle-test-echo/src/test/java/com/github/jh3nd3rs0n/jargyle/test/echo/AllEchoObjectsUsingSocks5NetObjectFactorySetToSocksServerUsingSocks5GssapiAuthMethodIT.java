package com.github.jh3nd3rs0n.jargyle.test.echo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithSelectiveIntegOrConfProtectionIT.class,
        EchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfProtectionIT.class
})
public class AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllEchoObjectsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

}
