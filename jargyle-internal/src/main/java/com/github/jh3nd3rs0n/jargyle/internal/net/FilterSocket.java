package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.Set;

/**
 * A {@code Socket} that contains some other {@code Socket} used as a source
 * of data, possibly transforming the data along the way or providing
 * additional functionality. The class {@code FilterSocket} itself simply
 * overrides all methods of {@code Socket} with versions that pass all requests
 * to the contained {@code Socket}. Subclasses of {@code FilterSocket} may
 * further override some of these methods and may also provide additional
 * methods and fields.
 */
public class FilterSocket extends Socket {

    /**
     * The {@code Socket} to be filtered.
     */
    protected Socket socket;

    /**
     * Constructs an unbound {@code FilterSocket} with the provided
     * {@code Socket} to be filtered.
     *
     * @param sock the provided {@code Socket} to be filtered
     */
    public FilterSocket(final Socket sock) {
        this.socket = Objects.requireNonNull(sock);
    }

    @Override
    public void bind(final SocketAddress bindpoint) throws IOException {
        this.socket.bind(bindpoint);
    }

    @Override
    public synchronized void close() throws IOException {
        this.socket.close();
    }

    @Override
    public void connect(final SocketAddress endpoint) throws IOException {
        this.socket.connect(endpoint);
    }

    @Override
    public void connect(
            final SocketAddress endpoint,
            final int timeout) throws IOException {
        this.socket.connect(endpoint, timeout);
    }

    @Override
    public SocketChannel getChannel() {
        return this.socket.getChannel();
    }

    @Override
    public InetAddress getInetAddress() {
        return this.socket.getInetAddress();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
        return this.socket.getKeepAlive();
    }

    @Override
    public void setKeepAlive(final boolean on) throws SocketException {
        this.socket.setKeepAlive(on);
    }

    @Override
    public InetAddress getLocalAddress() {
        return this.socket.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        return this.socket.getLocalPort();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return this.socket.getLocalSocketAddress();
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return this.socket.getOOBInline();
    }

    @Override
    public void setOOBInline(final boolean on) throws SocketException {
        this.socket.setOOBInline(on);
    }

    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        return this.socket.getOption(name);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    @Override
    public int getPort() {
        return this.socket.getPort();
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        return this.socket.getReceiveBufferSize();
    }

    @Override
    public synchronized void setReceiveBufferSize(
            final int size) throws SocketException {
        this.socket.setReceiveBufferSize(size);
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return this.socket.getRemoteSocketAddress();
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        return this.socket.getReuseAddress();
    }

    @Override
    public void setReuseAddress(final boolean on) throws SocketException {
        this.socket.setReuseAddress(on);
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        return this.socket.getSendBufferSize();
    }

    @Override
    public synchronized void setSendBufferSize(
            final int size) throws SocketException {
        this.socket.setSendBufferSize(size);
    }

    @Override
    public int getSoLinger() throws SocketException {
        return this.socket.getSoLinger();
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
        return this.socket.getSoTimeout();
    }

    @Override
    public synchronized void setSoTimeout(
            final int timeout) throws SocketException {
        this.socket.setSoTimeout(timeout);
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return this.socket.getTcpNoDelay();
    }

    @Override
    public void setTcpNoDelay(final boolean on) throws SocketException {
        this.socket.setTcpNoDelay(on);
    }

    @Override
    public int getTrafficClass() throws SocketException {
        return this.socket.getTrafficClass();
    }

    @Override
    public void setTrafficClass(final int tc) throws SocketException {
        this.socket.setTrafficClass(tc);
    }

    @Override
    public boolean isBound() {
        return this.socket.isBound();
    }

    @Override
    public boolean isClosed() {
        return this.socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    @Override
    public boolean isInputShutdown() {
        return this.socket.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
        return this.socket.isOutputShutdown();
    }

    @Override
    public void sendUrgentData(final int data) throws IOException {
        this.socket.sendUrgentData(data);
    }

    @Override
    public <T> Socket setOption(
            final SocketOption<T> name, final T value) throws IOException {
        this.socket.setOption(name, value);
        return this;
    }

    @Override
    public void setPerformancePreferences(
            final int connectionTime,
            final int latency,
            final int bandwidth) {
        this.socket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    @Override
    public void setSoLinger(
            final boolean on, final int linger) throws SocketException {
        this.socket.setSoLinger(on, linger);
    }

    @Override
    public void shutdownInput() throws IOException {
        this.socket.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
        this.socket.shutdownOutput();
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return this.socket.supportedOptions();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [socket=" +
                this.socket +
                "]";
    }

}
