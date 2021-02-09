package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;

public final class ServerSocketInterfaceServerSocket extends ServerSocket {

	private final ServerSocketInterface serverSocketInterface;
	
	public ServerSocketInterfaceServerSocket(
			final ServerSocketInterface serverSockInterface) 
			throws IOException {
		this.serverSocketInterface = serverSockInterface;
	}
	
	@Override
	public Socket accept() throws IOException {
		return new SocketInterfaceSocket(this.serverSocketInterface.accept());
	}

	@Override
	public void bind(SocketAddress endpoint) throws IOException {
		this.serverSocketInterface.bind(endpoint);
	}

	@Override
	public void bind(SocketAddress endpoint, int backlog) throws IOException {
		this.serverSocketInterface.bind(endpoint, backlog);
	}

	@Override
	public void close() throws IOException {
		this.serverSocketInterface.close();
	}

	@Override
	public ServerSocketChannel getChannel() {
		return null;
	}

	@Override
	public InetAddress getInetAddress() {
		return this.serverSocketInterface.getInetAddress();
	}

	@Override
	public int getLocalPort() {
		return this.serverSocketInterface.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.serverSocketInterface.getLocalSocketAddress();
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.serverSocketInterface.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.serverSocketInterface.getReuseAddress();
	}

	@Override
	public synchronized int getSoTimeout() throws IOException {
		return this.serverSocketInterface.getSoTimeout();
	}

	@Override
	public boolean isBound() {
		return this.serverSocketInterface.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.serverSocketInterface.isClosed();
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.serverSocketInterface.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		this.serverSocketInterface.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.serverSocketInterface.setReuseAddress(on);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.serverSocketInterface.setSoTimeout(timeout);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [serverSocketInterface=")
			.append(this.serverSocketInterface)
			.append("]");
		return builder.toString();
	}
	
}
