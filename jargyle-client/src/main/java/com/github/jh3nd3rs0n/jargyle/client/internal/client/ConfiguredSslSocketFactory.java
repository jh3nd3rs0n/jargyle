package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

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

/**
 * An implementation of {@code SslSocketFactory} that is configured by a
 * {@code Properties} object. If the property {@code socksClient.ssl.enabled}
 * is set to {@code true}, this implementation will return a new
 * {@code Socket} layered over the existing {@code Socket}. Otherwise, the
 * existing {@code Socket} is returned. This implementation does not support
 * the method {@link #getSocket(Socket, InputStream, boolean)}.
 */
final class ConfiguredSslSocketFactory extends SslSocketFactory {

    /**
     * The {@code ReentrantLock} that is used for controlling thread access to
     * the {@code SSLContext}.
     */
    private final ReentrantLock lock;

    /**
     * The {@code Properties} used to configure this
     * {@code ConfiguredSslSocketFactory}.
     */
    private final Properties properties;

    /**
     * The {@code SSLContext}.
     */
    private SSLContext sslContext;

    /**
     * Constructs a {@code ConfiguredSslSocketFactory} with the provided
     * {@code Properties}.
     *
     * @param props the provided {@code Properties}
     */
    private ConfiguredSslSocketFactory(final Properties props) {
        this.lock = new ReentrantLock();
        this.properties = props;
        this.sslContext = null;
    }

    /**
     * Returns a new {@code ConfiguredSslSocketFactory} with the provided
     * {@code Properties}.
     *
     * @param properties the provided {@code Properties}
     * @return a new {@code ConfiguredSslSocketFactory} with the provided
     * {@code Properties}
     */
    public static ConfiguredSslSocketFactory newInstance(
            final Properties properties) {
        return new ConfiguredSslSocketFactory(Objects.requireNonNull(
                properties));
    }

    /**
     * Returns an array of {@code KeyManager}s from the {@code Properties}
     * used to configure this {@code ConfiguredSslSocketFactory} or {@code null}
     * if both of the properties {@code socksClient.ssl.keyStoreBytes} and
     * {@code socksClient.ssl.keyStoreFile} are not set.
     *
     * @return an array of {@code KeyManager}s from the {@code Properties}
     * used to configure this {@code ConfiguredSslSocketFactory} or {@code null}
     * if both of the properties {@code socksClient.ssl.keyStoreBytes} and
     * {@code socksClient.ssl.keyStoreFile} are not set
     * @throws IOException if an I/O error occurs in getting the array of
     *                     {@code KeyManager}s
     */
    private KeyManager[] getKeyManagers() throws IOException {
        KeyManager[] keyManagers = null;
        InputStream keyStoreInputStream = null;
        Bytes keyStoreBytes = this.properties.getValue(
                SslPropertySpecConstants.SSL_KEY_STORE_BYTES);
        if (keyStoreBytes != null) {
            keyStoreInputStream = new ByteArrayInputStream(
                    keyStoreBytes.toArray());
        }
        File keyStoreFile = this.properties.getValue(
                SslPropertySpecConstants.SSL_KEY_STORE_FILE);
        if (keyStoreFile != null) {
            keyStoreInputStream = Files.newInputStream(keyStoreFile.toPath());
        }
        if (keyStoreInputStream != null) {
            char[] keyStorePassword = this.properties.getValue(
                    SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD).getPassword();
            String keyStoreType = this.properties.getValue(
                    SslPropertySpecConstants.SSL_KEY_STORE_TYPE);
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
            final boolean autoClose) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Socket getSocket(
            final Socket socket,
            final String host,
            final int port,
            final boolean autoClose) throws IOException {
        if (!this.properties.getValue(SslPropertySpecConstants.SSL_ENABLED)) {
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
                socket, host, port, autoClose);
        CommaSeparatedValues enabledCipherSuites = this.properties.getValue(
                SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES);
        if (enabledCipherSuites != null) {
            sslSocket.setEnabledCipherSuites(enabledCipherSuites.toArray());
        }
        CommaSeparatedValues enabledProtocols = this.properties.getValue(
                SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS);
        if (enabledProtocols != null) {
            sslSocket.setEnabledProtocols(enabledProtocols.toArray());
        }
        return sslSocket;
    }

    /**
     * Returns the {@code SSLContext} from the {@code Properties} used to
     * configure this {@code ConfiguredSslSocketFactory}.
     *
     * @return the {@code SSLContext}
     * @throws IOException if an I/O error occurs in getting the
     *                     {@code SSLContext}
     */
    private SSLContext getSslContext() throws IOException {
        KeyManager[] keyManagers = this.getKeyManagers();
        TrustManager[] trustManagers = this.getTrustManagers();
        SSLContext context;
        try {
            context = SslContextHelper.getSslContext(
                    this.properties.getValue(
                            SslPropertySpecConstants.SSL_PROTOCOL),
                    keyManagers,
                    trustManagers);
        } catch (KeyManagementException e) {
            throw new IOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return context;
    }

    /**
     * Returns an array of {@code TrustManager}s from the {@code Properties}
     * used to configure this {@code ConfiguredSslSocketFactory} or {@code null}
     * if both of the properties {@code socksClient.ssl.trustStoreBytes} and
     * {@code socksClient.ssl.trustStoreFile} are not set.
     *
     * @return an array of {@code TrustManager}s from the {@code Properties}
     * used to configure this {@code ConfiguredSslSocketFactory} or {@code null}
     * if both of the properties {@code socksClient.ssl.trustStoreBytes} and
     * {@code socksClient.ssl.trustStoreFile} are not set
     * @throws IOException if an I/O error occurs in getting the array of
     *                     {@code TrustManager}s
     */
    private TrustManager[] getTrustManagers() throws IOException {
        TrustManager[] trustManagers = null;
        InputStream trustStoreInputStream = null;
        Bytes trustStoreBytes = this.properties.getValue(
                SslPropertySpecConstants.SSL_TRUST_STORE_BYTES);
        if (trustStoreBytes != null) {
            trustStoreInputStream = new ByteArrayInputStream(
                    trustStoreBytes.toArray());
        }
        File trustStoreFile = this.properties.getValue(
                SslPropertySpecConstants.SSL_TRUST_STORE_FILE);
        if (trustStoreFile != null) {
            trustStoreInputStream = Files.newInputStream(
                    trustStoreFile.toPath());
        }
        if (trustStoreInputStream != null) {
            char[] trustStorePassword = this.properties.getValue(
                    SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD).getPassword();
            String trustStoreType = this.properties.getValue(
                    SslPropertySpecConstants.SSL_TRUST_STORE_TYPE);
            trustManagers = TrustManagerHelper.getTrustManagers(
                    trustStoreInputStream, trustStorePassword, trustStoreType);
            Arrays.fill(trustStorePassword, '\0');
        }
        return trustManagers;
    }

}
