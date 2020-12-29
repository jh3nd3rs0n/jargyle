package jargyle.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DirectSocketInterface extends SocketInterface {

	private final Socket socket;
	
	public DirectSocketInterface(final Socket sock) {
		this.socket = sock;
	}

	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		this.socket.bind(bindpoint);
	}
	
	@Override
	public void close() throws IOException {
		this.socket.close();
	}
	
	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		this.socket.connect(endpoint);
	}
	
	@Override
	public void connect(
			SocketAddress endpoint, int timeout) throws IOException {
		this.socket.connect(endpoint, timeout);
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
	public OutputStream getOutputStream() throws IOException {
		return this.socket.getOutputStream();
	}
	
	@Override
	public int getPort() {
		return this.socket.getPort();
	}
	
	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.socket.getReceiveBufferSize();
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
	public int getSendBufferSize() throws SocketException {
		return this.socket.getSendBufferSize();
	}
	
	@Override
	public int getSoLinger() throws SocketException {
		return this.socket.getSoLinger();
	}
	
	@Override
	public int getSoTimeout() throws SocketException {
		return this.socket.getSoTimeout();
	}
	
	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return this.socket.getTcpNoDelay();
	}
	
	@Override
	public int getTrafficClass() throws SocketException {
		return this.socket.getTrafficClass();
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
	public void sendUrgentData(int data) throws IOException {
		this.socket.sendUrgentData(data);
	}
	
	@Override
	public void setKeepAlive(boolean on) throws SocketException {
		this.socket.setKeepAlive(on);
	}
	
	@Override
	public void setOOBInline(boolean on) throws SocketException {
		this.socket.setOOBInline(on);
	}
	
	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socket.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}
	
	@Override
	public void setReceiveBufferSize(
			int size) throws SocketException {
		this.socket.setReceiveBufferSize(size);
	}
	
	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socket.setReuseAddress(on);
	}
	
	@Override
	public void setSendBufferSize(
			int size) throws SocketException {
		this.socket.setSendBufferSize(size);
	}
	
	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException {
		this.socket.setSoLinger(on, linger);
	}
	
	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.socket.setSoTimeout(timeout);
	}
	
	@Override
	public void setTcpNoDelay(boolean on) throws SocketException {
		this.socket.setTcpNoDelay(on);
	}
	
	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socket.setTrafficClass(tc);
	}
	
	@Override
	public void shutdownInput() throws IOException {
		this.socket.shutdownInput();
	}
	
	@Override
	public void shutdownOutput() throws IOException {
		this.socket.shutdownOutput();
	}
	
}
