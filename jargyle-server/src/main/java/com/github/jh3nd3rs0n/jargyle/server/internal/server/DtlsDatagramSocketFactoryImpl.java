package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.KeyManagerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {

    private final Configuration configuration;
    private final ReentrantLock lock;
    private SSLContext dtlsContext;

    public DtlsDatagramSocketFactoryImpl(final Configuration config) {
        this.configuration = config;
        this.dtlsContext = null;
        this.lock = new ReentrantLock();
    }

    @Override
    public DatagramSocket getDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException {
        Settings settings = this.configuration.getSettings();
        if (!settings.getLastValue(DtlsSettingSpecConstants.DTLS_ENABLED)) {
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
        dtlsDatagramSocket.setUseClientMode(false);
        CommaSeparatedValues enabledCipherSuites = settings.getLastValue(
                DtlsSettingSpecConstants.DTLS_ENABLED_CIPHER_SUITES);
        if (enabledCipherSuites != null) {
            dtlsDatagramSocket.setEnabledCipherSuites(
                    enabledCipherSuites.toArray());
        }
        CommaSeparatedValues enabledProtocols = settings.getLastValue(
                DtlsSettingSpecConstants.DTLS_ENABLED_PROTOCOLS);
        if (enabledProtocols != null) {
            dtlsDatagramSocket.setEnabledProtocols(enabledProtocols.toArray());
        }
        PositiveInteger wrappedReceiveBufferSize = settings.getLastValue(
                DtlsSettingSpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE);
        if (wrappedReceiveBufferSize != null) {
            dtlsDatagramSocket.setWrappedReceiveBufferSize(
                    wrappedReceiveBufferSize.intValue());
        }
        return dtlsDatagramSocket;
    }

    private SSLContext getDtlsContext() throws IOException {
        Settings settings = this.configuration.getSettings();
        KeyManager[] keyManagers = this.getKeyManagers();
        SSLContext context;
        try {
            context = SslContextHelper.getSslContext(
                    settings.getLastValue(
                            DtlsSettingSpecConstants.DTLS_PROTOCOL),
                    keyManagers,
                    null);
        } catch (KeyManagementException e) {
            throw new IOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return context;
    }

    private KeyManager[] getKeyManagers() throws IOException {
        KeyManager[] keyManagers = null;
        Settings settings = this.configuration.getSettings();
        InputStream keyStoreInputStream = null;
        Bytes keyStoreBytes = settings.getLastValue(
                DtlsSettingSpecConstants.DTLS_KEY_STORE_BYTES);
        if (keyStoreBytes != null) {
            keyStoreInputStream = new ByteArrayInputStream(
                    keyStoreBytes.toArray());
        }
        File keyStoreFile = settings.getLastValue(
                DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE);
        if (keyStoreFile != null) {
            keyStoreInputStream = Files.newInputStream(keyStoreFile.toPath());
        }
        if (keyStoreInputStream != null) {
            char[] keyStorePassword = settings.getLastValue(
                    DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD).getPassword();
            String keyStoreType = settings.getLastValue(
                    DtlsSettingSpecConstants.DTLS_KEY_STORE_TYPE);
            keyManagers = KeyManagerHelper.getKeyManagers(
                    keyStoreInputStream, keyStorePassword, keyStoreType);
            Arrays.fill(keyStorePassword, '\0');
        }
        return keyManagers;
    }

}
