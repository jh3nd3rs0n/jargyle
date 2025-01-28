package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of {@code DtlsDatagramSocketFactory} that is configured
 * by a {@code Properties} object. If the property
 * {@code socksClient.dtls.enabled} is set to {@code true}, this
 * implementation will return a new {@code DatagramSocket} layered over the
 * existing {@code DatagramSocket}. Otherwise, the existing
 * {@code DatagramSocket} is returned.
 */
final class ConfiguredDtlsDatagramSocketFactory extends DtlsDatagramSocketFactory {

    /**
     * The {@code ReentrantLock} that is used for controlling thread access to
     * the DTLS context.
     */
    private final ReentrantLock lock;

    /**
     * The {@code Properties} used to configure this
     * {@code ConfiguredDtlsDatagramSocketFactory}.
     */
    private final Properties properties;

    /**
     * The {@code SSLContext} as DTLS context.
     */
    private SSLContext dtlsContext;

    /**
     * Constructs a {@code ConfiguredDtlsDatagramSocketFactory} with the provided
     * {@code Properties}.
     *
     * @param props the provided {@code Properties}
     */
    private ConfiguredDtlsDatagramSocketFactory(final Properties props) {
        this.dtlsContext = null;
        this.lock = new ReentrantLock();
        this.properties = props;
    }

    /**
     * Returns a new {@code ConfiguredDtlsDatagramSocketFactory} with the
     * provided {@code Properties}.
     *
     * @param properties the provided {@code Properties}
     * @return a new {@code ConfiguredDtlsDatagramSocketFactory} with the
     * provided {@code Properties}
     */
    public static ConfiguredDtlsDatagramSocketFactory newInstance(
            final Properties properties) {
        return new ConfiguredDtlsDatagramSocketFactory(Objects.requireNonNull(
                properties));
    }

    @Override
    public DatagramSocket getDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException {
        if (!this.properties.getValue(DtlsPropertySpecConstants.DTLS_ENABLED)) {
            return datagramSocket;
        }
        this.lock.lock();
        try {
            if (this.dtlsContext == null) {
                this.dtlsContext = this.getDtlsContext();
            }
        } finally {
            this.lock.unlock();
        }
        DtlsDatagramSocketFactory factory =
                DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
        DtlsDatagramSocket dtlsDatagramSocket =
                (DtlsDatagramSocket) factory.getDatagramSocket(datagramSocket);
        dtlsDatagramSocket.setUseClientMode(true);
        CommaSeparatedValues enabledCipherSuites = this.properties.getValue(
                DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES);
        if (enabledCipherSuites != null) {
            dtlsDatagramSocket.setEnabledCipherSuites(
                    enabledCipherSuites.toArray());
        }
        CommaSeparatedValues enabledProtocols = this.properties.getValue(
                DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS);
        if (enabledProtocols != null) {
            dtlsDatagramSocket.setEnabledProtocols(
                    enabledProtocols.toArray());
        }
        PositiveInteger wrappedReceiveBufferSize = this.properties.getValue(
                DtlsPropertySpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE);
        if (wrappedReceiveBufferSize != null) {
            dtlsDatagramSocket.setWrappedReceiveBufferSize(
                    wrappedReceiveBufferSize.intValue());
        }
        return dtlsDatagramSocket;
    }

    /**
     * Returns the {@code SSLContext} as DTLS context from the
     * {@code Properties} used to configure this
     * {@code ConfiguredDtlsDatagramSocketFactory}.
     *
     * @return the {@code SSLContext} as DTLS context
     * @throws IOException if an I/O error occurs in getting the
     *                     {@code SSLContext} as DTLS context
     */
    private SSLContext getDtlsContext() throws IOException {
        TrustManager[] trustManagers = this.getTrustManagers();
        SSLContext context;
        try {
            context = SslContextHelper.getSslContext(
                    this.properties.getValue(
                            DtlsPropertySpecConstants.DTLS_PROTOCOL),
                    null,
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
     * used to configure this {@code ConfiguredDtlsDatagramSocketFactory} or
     * {@code null} if both of the properties
     * {@code socksClient.dtls.trustStoreBytes} and
     * {@code socksClient.dtls.trustStoreFile} are not set.
     *
     * @return an array of {@code TrustManager}s from the {@code Properties}
     * used to configure this {@code ConfiguredDtlsDatagramSocketFactory} or
     * {@code null} if both of the properties
     * {@code socksClient.dtls.trustStoreBytes} and
     * {@code socksClient.dtls.trustStoreFile} are not set
     * @throws IOException if an I/O error occurs in getting the array of
     *                     {@code TrustManager}s
     */
    private TrustManager[] getTrustManagers() throws IOException {
        TrustManager[] trustManagers = null;
        InputStream trustStoreInputStream = null;
        Bytes trustStoreBytes = this.properties.getValue(
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES);
        if (trustStoreBytes != null) {
            trustStoreInputStream = new ByteArrayInputStream(
                    trustStoreBytes.toArray());
        }
        File trustStoreFile = this.properties.getValue(
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE);
        if (trustStoreFile != null) {
            trustStoreInputStream = Files.newInputStream(
                    trustStoreFile.toPath());
        }
        if (trustStoreInputStream != null) {
            char[] trustStorePassword = this.properties.getValue(
                    DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD).getPassword();
            String trustStoreType = this.properties.getValue(
                    DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE);
            trustManagers = TrustManagerHelper.getTrustManagers(
                    trustStoreInputStream, trustStorePassword, trustStoreType);
            Arrays.fill(trustStorePassword, '\0');
        }
        return trustManagers;
    }

}
