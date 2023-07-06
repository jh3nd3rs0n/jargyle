package com.github.jh3nd3rs0n.jargyle.internal.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.DatagramChannel;
import java.util.Set;

public class FilterDatagramSocket extends DatagramSocket {

	protected DatagramSocket datagramSocket;
	
	public FilterDatagramSocket(
			final DatagramSocket datagramSock) throws SocketException {
		super((SocketAddress) null);
		this.datagramSocket = datagramSock;
	}

	@Override
	public synchronized void bind(SocketAddress addr) throws SocketException {
		this.datagramSocket.bind(addr);
	}

	@Override
	public void close() {
		this.datagramSocket.close();
	}

	@Override
	public void connect(InetAddress address, int port) {
		this.datagramSocket.connect(address, port);
	}

	@Override
	public void connect(SocketAddress addr) throws SocketException {
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
	public <T> T getOption(SocketOption<T> name) throws IOException {
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
	public SocketAddress getRemoteSocketAddress() {
		return this.datagramSocket.getRemoteSocketAddress();
	}

	@Override
	public synchronized boolean getReuseAddress() throws SocketException {
		return this.datagramSocket.getReuseAddress();
	}

	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		return this.datagramSocket.getSendBufferSize();
	}

	@Override
	public synchronized int getSoTimeout() throws SocketException {
		return this.datagramSocket.getSoTimeout();
	}

	@Override
	public synchronized int getTrafficClass() throws SocketException {
		return this.datagramSocket.getTrafficClass();
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
	public synchronized void receive(DatagramPacket p) throws IOException {
		this.datagramSocket.receive(p);
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		this.datagramSocket.send(p);
	}

	@Override
	public synchronized void setBroadcast(boolean on) throws SocketException {
		this.datagramSocket.setBroadcast(on);
	}

	@Override
	public <T> DatagramSocket setOption(
			SocketOption<T> name, T value) throws IOException {
		this.datagramSocket.setOption(name, value);
		return this;
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		this.datagramSocket.setReceiveBufferSize(size);
	}

	@Override
	public synchronized void setReuseAddress(boolean on) throws SocketException {
		this.datagramSocket.setReuseAddress(on);
	}

	@Override
	public synchronized void setSendBufferSize(int size) throws SocketException {
		this.datagramSocket.setSendBufferSize(size);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.datagramSocket.setSoTimeout(timeout);
	}

	@Override
	public synchronized void setTrafficClass(int tc) throws SocketException {
		this.datagramSocket.setTrafficClass(tc);
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return this.datagramSocket.supportedOptions();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [datagramSocket=")
			.append(this.datagramSocket)
			.append("]");
		return builder.toString();
	}
	
}
