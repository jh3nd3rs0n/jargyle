package com.github.jh3nd3rs0n.jargyle.test.help.gss.kerberos;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.admin.kadmin.local.LocalKadmin;
import org.apache.kerby.kerberos.kerb.admin.kadmin.local.LocalKadminImpl;
import org.apache.kerby.kerberos.kerb.server.KdcServer;
import org.apache.kerby.kerberos.kerb.server.impl.DefaultInternalKdcServerImpl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A testing environment set up for running Kerberos within a test class. It
 * can only be used once in a module.
 */
public final class TestKerberosEnvironment {

    /**
     * The system property name for {@code java.security.auth.login.config}:
     * the JAAS login configuration file.
     */
    private static final String JAVA_SECURITY_LOGIN_CONFIG_PROPERTY_NAME =
            "java.security.auth.login.config";

    /**
     * The system property name for {@code java.security.krb5.conf}: the
     * Kerberos configuration file.
     */
    private static final String JAVA_SECURITY_KRB5_CONF_PROPERTY_NAME =
            "java.security.krb5.conf";

    /**
     * The system property name for
     * {@code javax.security.auth.useSubjectCredsOnly}: enables JAAS-based
     * authentication to obtain the credentials directly instead of letting
     * the underlying security mechanism (Kerberos) obtain them (we want this
     * system property to be set to {@code false}).
     */
    private static final String JAVAX_SECURITY_AUTH_USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME =
            "javax.security.auth.useSubjectCredsOnly";

    /**
     * The filename of the key tab file for the exported identity keys.
     */
    private static final String KEY_TAB_FILENAME = "keytab";

    /**
     * The filename of the Kerberos configuration file.
     */
    private static final String KRB5_CONF_FILENAME = "krb5.conf";

    /**
     * The filename of the JAAS login configuration file.
     */
    private static final String LOGIN_CONF_FILENAME = "login.conf";

    /**
     * The acceptor {@code Principal}.
     */
    private final Principal acceptorPrincipal;

    /**
     * The initiator {@code Principal}.
     */
    private final Principal initiatorPrincipal;

    /**
     * The host of the KDC.
     */
    private final String kdcHost;

    /**
     * The port of the KDC.
     */
    private final int kdcPort;

    /**
     * The realm the KDC is in.
     */
    private final String kdcRealm;

    /**
     * The key tab file for the exported identity keys.
     */
    private final Path keyTabFile;

    /**
     * The Kerberos configuration file.
     */
    private final Path krb5ConfFile;

    /**
     * The JAAS login configuration file.
     */
    private final Path loginConfFile;

    /**
     * The working directory of the KDC.
     */
    private final Path workingDirectory;

    /**
     * The KDC.
     */
    private KdcServer kdcServer;

    /**
     * Constructs a {@code TestKerberosEnvironment} with the provided
     * {@code Builder}.
     *
     * @param builder the provided {@code Builder}
     */
    private TestKerberosEnvironment(final Builder builder) {
        this.acceptorPrincipal = builder.acceptorPrincipal;
        this.initiatorPrincipal = builder.initiatorPrincipal;
        this.kdcHost = builder.kdcHost;
        this.kdcPort = builder.kdcPort;
        this.kdcRealm = builder.kdcRealm;
        this.kdcServer = null;
        this.keyTabFile = builder.workingDirectory.resolve(KEY_TAB_FILENAME);
        this.krb5ConfFile = builder.workingDirectory.resolve(KRB5_CONF_FILENAME);
        this.loginConfFile = builder.workingDirectory.resolve(LOGIN_CONF_FILENAME);
        this.workingDirectory = builder.workingDirectory;
    }

    /**
     * Creates the Kerberos configuration file.
     *
     * @throws IOException if an error occurs in creating the Kerberos
     *                     configuration file
     */
    private void createKrb5ConfFile() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(
                this.krb5ConfFile, StandardCharsets.UTF_8)) {
            w.write("[libdefaults]");
            w.newLine();
            w.write(String.format("    kdc_realm = %s", this.kdcRealm));
            w.newLine();
            w.write(String.format("    default_realm = %s", this.kdcRealm));
            w.newLine();
            w.write("    udp_preference_limit = 4096");
            w.newLine();
            w.write(String.format("    kdc_tcp_port = %s", this.kdcPort));
            w.newLine();
            w.write(String.format("    kdc_udp_port = %s", this.kdcPort));
            w.newLine();
            w.newLine();
            w.write("[realms]");
            w.newLine();
            w.write(String.format("    %s = {", this.kdcRealm));
            w.newLine();
            w.write(String.format("        kdc = %s:%s", this.kdcHost, this.kdcPort));
            w.newLine();
            w.write("    }");
            w.newLine();
        }
    }

    /**
     * Creates the JAAS login configuration file.
     *
     * @throws IOException if an error occurs in creating the JAAS login
     *                     configuration file
     */
    private void createLoginConfFile() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(
                this.loginConfFile, StandardCharsets.UTF_8)) {
            if (this.initiatorPrincipal != null) {
                w.write("com.sun.security.jgss.initiate {");
                w.newLine();
                w.write("  com.sun.security.auth.module.Krb5LoginModule required");
                w.newLine();
                w.write(String.format(
                        "  principal=\"%s\"",
                        this.initiatorPrincipal.getPrincipal()));
                w.newLine();
                w.write("  useKeyTab=true");
                w.newLine();
                w.write(String.format(
                        "  keyTab=\"%s\"",
                        this.keyTabFile.toUri()));
                w.newLine();
                w.write("  storeKey=true;");
                w.newLine();
                w.write("};");
                w.newLine();
            }
            if (this.acceptorPrincipal != null) {
                w.write("com.sun.security.jgss.accept {");
                w.newLine();
                w.write("  com.sun.security.auth.module.Krb5LoginModule required");
                w.newLine();
                w.write(String.format(
                        "  principal=\"%s\"",
                        this.acceptorPrincipal.getPrincipal()));
                w.newLine();
                w.write("  useKeyTab=true");
                w.newLine();
                w.write(String.format(
                        "  keyTab=\"%s\"",
                        this.keyTabFile.toUri()));
                w.newLine();
                w.write("  storeKey=true;");
                w.newLine();
                w.write("};");
                w.newLine();
            }
        }
    }

    /**
     * Sets up this {@code TestKerberosEnvironment}.
     *
     * @throws TestKerberosEnvironmentException if an error occurs in setting
     *                                          up this
     *                                          {@code TestKerberosEnvironment}
     */
    public void setUp() throws TestKerberosEnvironmentException {
        try {
            this.setUpKdcServer();
        } catch (KrbException e) {
            this.tearDown();
            throw new TestKerberosEnvironmentException(e);
        }
        try {
            this.createKrb5ConfFile();
            this.createLoginConfFile();
        } catch (IOException e) {
            this.tearDown();
            throw new TestKerberosEnvironmentException(e);
        }
        System.setProperty(
                JAVA_SECURITY_KRB5_CONF_PROPERTY_NAME,
                this.krb5ConfFile.toAbsolutePath().toString());
        System.setProperty(
                JAVA_SECURITY_LOGIN_CONFIG_PROPERTY_NAME,
                this.loginConfFile.toAbsolutePath().toString());
        System.setProperty(
                JAVAX_SECURITY_AUTH_USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME,
                "false");
    }

    /**
     * Sets up the KDC.
     *
     * @throws KrbException if an error occurs in setting up the KDC
     */
    private void setUpKdcServer() throws KrbException {
        this.kdcServer = new KdcServer(this.workingDirectory.toFile());
        this.kdcServer.setKdcRealm(this.kdcRealm);
        this.kdcServer.setKdcHost(this.kdcHost);
        this.kdcServer.setKdcPort(this.kdcPort);
        this.kdcServer.setWorkDir(this.workingDirectory.toFile());
        this.kdcServer.setInnerKdcImpl(new DefaultInternalKdcServerImpl(
                this.kdcServer.getKdcSetting()));
        this.kdcServer.init();
        LocalKadmin kadmin = new LocalKadminImpl(
                this.kdcServer.getKdcSetting(),
                this.kdcServer.getIdentityService());
        kadmin.createBuiltinPrincipals();
        kadmin.addPrincipal(
                this.acceptorPrincipal.getPrincipal(),
                this.acceptorPrincipal.getPassword());
        kadmin.addPrincipal(
                this.initiatorPrincipal.getPrincipal(),
                this.initiatorPrincipal.getPassword());
        kadmin.exportKeytab(this.keyTabFile.toFile());
        this.kdcServer.start();
    }

    /**
     * Tears down this {@code TestKerberosEnvironment}.
     *
     * @throws TestKerberosEnvironmentException if an error occurs in
     *                                          tearing down this
     *                                          {@code TestKerberosEnvironment}
     */
    public void tearDown() throws TestKerberosEnvironmentException {
        if (this.kdcServer != null) {
            try {
                this.kdcServer.stop();
            } catch (KrbException e) {
                throw new TestKerberosEnvironmentException(e);
            }
        }
        try {
            Files.deleteIfExists(this.keyTabFile);
            Files.deleteIfExists(this.krb5ConfFile);
            Files.deleteIfExists(this.loginConfFile);
        } catch (IOException e) {
            throw new TestKerberosEnvironmentException(e);
        }
        System.clearProperty(JAVA_SECURITY_KRB5_CONF_PROPERTY_NAME);
        System.clearProperty(JAVA_SECURITY_LOGIN_CONFIG_PROPERTY_NAME);
        System.clearProperty(
                JAVAX_SECURITY_AUTH_USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME);
    }

    /**
     * The builder for the {@code TestKerberosEnvironment}.
     */
    public static final class Builder {

        /**
         * The realm the KDC is in.
         */
        private final String kdcRealm;

        /**
         * The host of the KDC.
         */
        private final String kdcHost;

        /**
         * The port of the KDC.
         */
        private final int kdcPort;

        /**
         * The working directory of the KDC.
         */
        private final Path workingDirectory;

        /**
         * The acceptor {@code Principal}.
         */
        private Principal acceptorPrincipal;

        /**
         * The initiator {@code Principal}.
         */
        private Principal initiatorPrincipal;

        /**
         * Constructs a {@code Builder} with the provided realm of the KDC,
         * the provided host of the KDC, the provided port of the KDC, and the
         * provided working directory of the KDC.
         *
         * @param realm      the provided realm of the KDC
         * @param host       the provided host of the KDC
         * @param port       the provided port of the KDC
         * @param workingDir the provided working directory of the KDC
         */
        public Builder(
                final String realm,
                final String host,
                final int port,
                final Path workingDir) {
            this.kdcRealm = realm;
            this.kdcHost = host;
            this.kdcPort = port;
            this.workingDirectory = workingDir;
            this.acceptorPrincipal = null;
            this.initiatorPrincipal = null;
        }

        /**
         * Builds and returns a new {@code TestKerberosEnvironment}.
         *
         * @return a new {@code TestKerberosEnvironment}
         */
        public TestKerberosEnvironment build() {
            return new TestKerberosEnvironment(this);
        }

        /**
         * Sets the acceptor principal with the provided principal and the
         * provided password.
         *
         * @param principal the provided principal
         * @param password  the provided password
         * @return this {@code Builder}
         */
        public Builder setAcceptorPrincipal(
                final String principal, final String password) {
            this.acceptorPrincipal = new Principal(principal, password);
            return this;
        }

        /**
         * Sets the initiator principal with the provided principal and the
         * provided password.
         *
         * @param principal the provided principal
         * @param password  the provided password
         * @return this {@code Builder}
         */
        public Builder setInitiatorPrincipal(
                final String principal, final String password) {
            this.initiatorPrincipal = new Principal(principal, password);
            return this;
        }

    }

    /**
     * A principal for the KDC.
     */
    private static final class Principal {

        /**
         * The principal of this {@code Principal}.
         */
        private final String principal;

        /**
         * The password of this {@code Principal}.
         */
        private final String password;

        /**
         * Constructs a {@code Principal} with the provided principal and the
         * provided password.
         *
         * @param princip the provided principal
         * @param pass    the provided password
         */
        public Principal(final String princip, final String pass) {
            this.principal = princip;
            this.password = pass;
        }

        /**
         * Returns the password of this {@code Principal}.
         *
         * @return the password of this {@code Principal}
         */
        public String getPassword() {
            return this.password;
        }

        /**
         * Returns the principal of this {@code Principal}.
         *
         * @return the principal of this {@code Principal}
         */
        public String getPrincipal() {
            return this.principal;
        }

    }
}
