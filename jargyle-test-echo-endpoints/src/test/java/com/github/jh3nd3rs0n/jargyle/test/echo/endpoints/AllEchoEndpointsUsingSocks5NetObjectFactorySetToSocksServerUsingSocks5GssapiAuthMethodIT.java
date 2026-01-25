package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class,
        EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT.class,
        EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithSelectiveIntegOrConfIT.class,
        EchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfIT.class
})
public class AllEchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllEchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllEchoEndpointsUsingSocks5NetObjectFactorySetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

}
