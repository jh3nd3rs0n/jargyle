package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.test.help.gss.kerberos.KerberosEnvironment;
import com.github.jh3nd3rs0n.test.help.gss.kerberos.KerberosEnvironmentException;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;

public class GssEnvironment {

    private static final String BASE_DIR_PREFIX =
            "com.github.jh3nd3rs0n.jargyle.integration.test-";

    private static final String KDC_REALM = "EXAMPLE.COM";
    private static final String KDC_HOST =
            InetAddress.getLoopbackAddress().getHostAddress();
    private static final int KDC_PORT = 9000;

    private static final String PRINCIPAL = "alice";
    private static final String PRINCIPAL_PASSWORD = "87654321";

    private static final String RCMD_SERVICE_PRINCIPAL = String.format(
            "rcmd/%s", InetAddress.getLoopbackAddress().getHostAddress());
    private static final String RCMD_SERVICE_PRINCIPAL_PASSWORD = "12345678";

    public static final String MECHANISM_OID = "1.2.840.113554.1.2.2";
    public static final String SERVICE_NAME = RCMD_SERVICE_PRINCIPAL;

    private static Path baseDir = null;
    private static Class<?> testClass = null;
    private static KerberosEnvironment kerberosEnvironment;

    public static void setUpBeforeClass(
            final Class<?> testCls) throws IOException {
        if (testClass == null) {
            testClass = testCls;
        } else {
            return;
        }
        baseDir = Files.createTempDirectory(BASE_DIR_PREFIX);
        kerberosEnvironment = new KerberosEnvironment.Builder(
                KDC_REALM, KDC_HOST, KDC_PORT, baseDir)
                .setAcceptorPrincipal(
                        RCMD_SERVICE_PRINCIPAL,
                        RCMD_SERVICE_PRINCIPAL_PASSWORD)
                .setInitiatorPrincipal(
                        PRINCIPAL,
                        PRINCIPAL_PASSWORD)
                .build();
        try {
            kerberosEnvironment.setUp();
        } catch (KerberosEnvironmentException e) {
            throw new IOException(e);
        }
    }

    public static void tearDownAfterClass(
            final Class<?> testCls) throws IOException {
        if (testClass != null && !testClass.equals(testCls)) {
            return;
        }
        if (kerberosEnvironment != null) {
            try {
                kerberosEnvironment.tearDown();
            } catch (KerberosEnvironmentException e) {
                throw new IOException(e);
            }
        }
        if (baseDir != null) {
            Files.deleteIfExists(baseDir);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

}
