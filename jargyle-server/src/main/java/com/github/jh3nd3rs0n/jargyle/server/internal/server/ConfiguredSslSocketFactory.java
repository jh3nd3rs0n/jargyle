package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

final class ConfiguredSslSocketFactory extends SslSocketFactory {

    private final Configuration configuration;
    private final ReentrantLock lock;
    private SSLContext sslContext;

    private ConfiguredSslSocketFactory(final Configuration config) {
        this.configuration = config;
        this.lock = new ReentrantLock();
        this.sslContext = null;
    }

    public static ConfiguredSslSocketFactory newInstance(
            final Configuration configuration) {
        return new ConfiguredSslSocketFactory(Objects.requireNonNull(
                configuration));
    }

    private KeyManager[] getKeyManagers() throws IOException {
        KeyManager[] keyManagers = null;
        Settings settings = this.configuration.getSettings();
        InputStream keyStoreInputStream = null;
        Bytes keyStoreBytes = settings.getLastValue(
                SslSettingSpecConstants.SSL_KEY_STORE_BYTES);
        if (keyStoreBytes != null) {
            keyStoreInputStream = new ByteArrayInputStream(
                    keyStoreBytes.toArray());
        }
        File keyStoreFile = settings.getLastValue(
                SslSettingSpecConstants.SSL_KEY_STORE_FILE);
        if (keyStoreFile != null) {
            keyStoreInputStream = Files.newInputStream(keyStoreFile.toPath());
        }
        if (keyStoreInputStream != null) {
            char[] keyStorePassword = settings.getLastValue(
                    SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD).getPassword();
            String keyStoreType = settings.getLastValue(
                    SslSettingSpecConstants.SSL_KEY_STORE_TYPE);
            keyManagers = KeyManagerHelper.getKeyManagers(
                    keyStoreInputStream, keyStorePassword, keyStoreType);
            Arrays.fill(keyStorePassword, '\0');
        }
        return keyManagers;
    }

    @Override
    public Socket getSocket(
            final Socket socket,
            final InputStream consumed,
            final boolean autoClose) throws IOException {
        Settings settings = this.configuration.getSettings();
        if (!settings.getLastValue(SslSettingSpecConstants.SSL_ENABLED)) {
            return socket;
        }
        this.lock.lock();
        try {
            if (this.sslContext == null) {
                this.sslContext = this.getSslContext();
            }
        } finally {
            this.lock.unlock();
        }
        SslSocketFactory factory = SslSocketFactory.newInstance(
                this.sslContext);
        SSLSocket sslSocket = (SSLSocket) factory.getSocket(
                socket, consumed, autoClose);
        CommaSeparatedValues enabledCipherSuites = settings.getLastValue(
                SslSettingSpecConstants.SSL_ENABLED_CIPHER_SUITES);
        if (enabledCipherSuites != null) {
            sslSocket.setEnabledCipherSuites(enabledCipherSuites.toArray());
        }
        CommaSeparatedValues enabledProtocols = settings.getLastValue(
                SslSettingSpecConstants.SSL_ENABLED_PROTOCOLS);
        if (enabledProtocols != null) {
            sslSocket.setEnabledProtocols(enabledProtocols.toArray());
        }
        if (settings.getLastValue(
                SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH).booleanValue()) {
            sslSocket.setNeedClientAuth(true);
        }
        if (settings.getLastValue(
                SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH).booleanValue()) {
            sslSocket.setWantClientAuth(true);
        }
        return sslSocket;
    }

    @Override
    public Socket getSocket(
            final Socket socket,
            final String host,
            final int port,
            final boolean autoClose) throws IOException {
        throw new UnsupportedOperationException();
    }

    private SSLContext getSslContext() throws IOException {
        Settings settings = this.configuration.getSettings();
        KeyManager[] keyManagers = this.getKeyManagers();
        TrustManager[] trustManagers = this.getTrustManagers();
        SSLContext context;
        try {
            context = SslContextHelper.getSslContext(
                    settings.getLastValue(SslSettingSpecConstants.SSL_PROTOCOL),
                    keyManagers,
                    trustManagers);
        } catch (KeyManagementException e) {
            throw new IOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return context;
    }

    private TrustManager[] getTrustManagers() throws IOException {
        TrustManager[] trustManagers = null;
        Settings settings = this.configuration.getSettings();
        InputStream trustStoreInputStream = null;
        Bytes trustStoreBytes = settings.getLastValue(
                SslSettingSpecConstants.SSL_TRUST_STORE_BYTES);
        if (trustStoreBytes != null) {
            trustStoreInputStream = new ByteArrayInputStream(
                    trustStoreBytes.toArray());
        }
        File trustStoreFile = settings.getLastValue(
                SslSettingSpecConstants.SSL_TRUST_STORE_FILE);
        if (trustStoreFile != null) {
            trustStoreInputStream = Files.newInputStream(
                    trustStoreFile.toPath());
        }
        if (trustStoreInputStream != null) {
            char[] trustStorePassword = settings.getLastValue(
                    SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD).getPassword();
            String trustStoreType = settings.getLastValue(
                    SslSettingSpecConstants.SSL_TRUST_STORE_TYPE);
            trustManagers = TrustManagerHelper.getTrustManagers(
                    trustStoreInputStream, trustStorePassword, trustStoreType);
            Arrays.fill(trustStorePassword, '\0');
        }
        return trustManagers;
    }

}
