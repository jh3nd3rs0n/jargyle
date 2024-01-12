package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.Objects;
import java.util.Set;

/**
 * A {@code DatagramSocket} that contains some other {@code DatagramSocket}
 * used as a source of data, possibly transforming the data along the way or
 * providing additional functionality. The class {@code FilterDatagramSocket}
 * itself simply overrides all methods of {@code DatagramSocket} with versions
 * that pass all requests to the contained {@code DatagramSocket}. Subclasses
 * of {@code FilterDatagramSocket} may further override some of these methods
 * and may also provide additional methods and fields.
 */
public class FilterDatagramSocket extends DatagramSocket {

    /**
     * The {@code DatagramSocket} to be filtered.
     */
    protected DatagramSocket datagramSocket;

    /**
     * Constructs an unbound {@code FilterDatagramSocket} with the provided
     * {@code DatagramSocket} to be filtered.
     *
     * @param datagramSock the provided {@code DatagramSocket} to be filtered
     * @throws SocketException if the {@code FilterDatagramSocket} can not be
     *                         opened, or the {@code FilterDatagramSocket}
     *                         could not bind to the specified local port
     *                         (which in either case will not happen since the
     *                         {@code FilterDatagramSocket} is neither opened
     *                         nor bound)
     */
    public FilterDatagramSocket(
            final DatagramSocket datagramSock) throws SocketException {
        super((SocketAddress) null);
        this.datagramSocket = Objects.requireNonNull(datagramSock);
    }

    @Override
    public synchronized void bind(
            final SocketAddress addr) throws SocketException {
        this.datagramSocket.bind(addr);
    }

    @Override
    public void close() {
        this.datagramSocket.close();
    }

    @Override
    public void connect(final InetAddress address, final int port) {
        this.datagramSocket.connect(address, port);
    }

    @Override
    public void connect(final SocketAddress addr) throws SocketException {
        this.datagramSocket.connect(addr);
    }

    @Override
    public void disconnect() {
        this.datagramSocket.disconnect();
    }

    @Override
    public synchronized boolean getBroadcast() throws SocketException {
        return this.datagramSocket.getBroadcast();
    }

    @Override
    public synchronized void setBroadcast(
            final boolean on) throws SocketException {
        this.datagramSocket.setBroadcast(on);
    }

    @Override
    public DatagramChannel getChannel() {
        return this.datagramSocket.getChannel();
    }

    @Override
    public InetAddress getInetAddress() {
        return this.datagramSocket.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return this.datagramSocket.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
        return this.datagramSocket.getLocalPort();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return this.datagramSocket.getLocalSocketAddress();
    }

    @Override
    public <T> T getOption(final SocketOption<T> name) throws IOException {
        return this.datagramSocket.getOption(name);
    }

    @Override
    public int getPort() {
        return this.datagramSocket.getPort();
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        return this.datagramSocket.getReceiveBufferSize();
    }

    @Override
    public synchronized void setReceiveBufferSize(
            final int size) throws SocketException {
        this.datagramSocket.setReceiveBufferSize(size);
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return this.datagramSocket.getRemoteSocketAddress();
    }

    @Override
    public synchronized boolean getReuseAddress() throws SocketException {
        return this.datagramSocket.getReuseAddress();
    }

    @Override
    public synchronized void setReuseAddress(
            final boolean on) throws SocketException {
        this.datagramSocket.setReuseAddress(on);
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        return this.datagramSocket.getSendBufferSize();
    }

    @Override
    public synchronized void setSendBufferSize(
            final int size) throws SocketException {
        this.datagramSocket.setSendBufferSize(size);
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
        return this.datagramSocket.getSoTimeout();
    }

    @Override
    public synchronized void setSoTimeout(
            final int timeout) throws SocketException {
        this.datagramSocket.setSoTimeout(timeout);
    }

    @Override
    public synchronized int getTrafficClass() throws SocketException {
        return this.datagramSocket.getTrafficClass();
    }

    @Override
    public synchronized void setTrafficClass(
            final int tc) throws SocketException {
        this.datagramSocket.setTrafficClass(tc);
    }

    @Override
    public boolean isBound() {
        return this.datagramSocket.isBound();
    }

    @Override
    public boolean isClosed() {
        return this.datagramSocket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return this.datagramSocket.isConnected();
    }

    @Override
    public synchronized void receive(
            final DatagramPacket p) throws IOException {
        this.datagramSocket.receive(p);
    }

    @Override
    public void send(final DatagramPacket p) throws IOException {
        this.datagramSocket.send(p);
    }

    @Override
    public <T> DatagramSocket setOption(
            final SocketOption<T> name, final T value) throws IOException {
        this.datagramSocket.setOption(name, value);
        return this;
    }

    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return this.datagramSocket.supportedOptions();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [datagramSocket=" +
                this.datagramSocket +
                "]";
    }

}
