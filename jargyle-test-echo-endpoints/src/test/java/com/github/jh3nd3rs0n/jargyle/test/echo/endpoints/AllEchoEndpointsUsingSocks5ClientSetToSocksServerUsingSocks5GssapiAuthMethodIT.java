package com.github.jh3nd3rs0n.jargyle.test.echo.endpoints;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodIT.class,
        EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplIT.class,
        EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodNecReferenceImplWithSelectiveIntegOrConfIT.class,
        EchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodWithSelectiveIntegOrConfIT.class
})
public class AllEchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllEchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllEchoEndpointsUsingSocks5ClientSetToSocksServerUsingSocks5GssapiAuthMethodIT.class);
    }

}
