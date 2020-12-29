package jargyle.common.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class FilterSocketInterface extends SocketInterface {

	protected SocketInterface socketInterface;
	
	public FilterSocketInterface(final SocketInterface sockInterface) {
		this.socketInterface = sockInterface;
	}
	
	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		this.socketInterface.bind(bindpoint);
	}
	
	@Override
	public void close() throws IOException {
		this.socketInterface.close();
	}
	
	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		this.socketInterface.connect(endpoint);
	}
	
	@Override
	public void connect(
			SocketAddress endpoint, int timeout) throws IOException {
		this.socketInterface.connect(endpoint, timeout);
	}
	
	@Override
	public InetAddress getInetAddress() {
		return this.socketInterface.getInetAddress();
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return this.socketInterface.getInputStream();
	}
	
	@Override
	public boolean getKeepAlive() throws SocketException {
		return this.socketInterface.getKeepAlive();
	}
	
	@Override
	public InetAddress getLocalAddress() {
		return this.socketInterface.getLocalAddress();
	}
	
	@Override
	public int getLocalPort() {
		return this.socketInterface.getLocalPort();
	}
	
	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socketInterface.getLocalSocketAddress();
	}
	
	@Override
	public boolean getOOBInline() throws SocketException {
		return this.socketInterface.getOOBInline();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.socketInterface.getOutputStream();
	}
	
	@Override
	public int getPort() {
		return this.socketInterface.getPort();
	}
	
	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.socketInterface.getReceiveBufferSize();
	}
	
	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socketInterface.getRemoteSocketAddress();
	}
	
	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socketInterface.getReuseAddress();
	}
	
	@Override
	public int getSendBufferSize() throws SocketException {
		return this.socketInterface.getSendBufferSize();
	}
	
	public final SocketInterface getSocketInterface() {
		return this.socketInterface;
	}
	
	@Override
	public int getSoLinger() throws SocketException {
		return this.socketInterface.getSoLinger();
	}
	
	@Override
	public int getSoTimeout() throws SocketException {
		return this.socketInterface.getSoTimeout();
	}
	
	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return this.socketInterface.getTcpNoDelay();
	}
	
	@Override
	public int getTrafficClass() throws SocketException {
		return this.socketInterface.getTrafficClass();
	}
	
	@Override
	public boolean isBound() {
		return this.socketInterface.isBound();
	}
	
	@Override
	public boolean isClosed() {
		return this.socketInterface.isClosed();
	}
	
	@Override
	public boolean isConnected() {
		return this.socketInterface.isConnected();
	}
	
	@Override
	public boolean isInputShutdown() {
		return this.socketInterface.isInputShutdown();
	}
	
	@Override
	public boolean isOutputShutdown() {
		return this.socketInterface.isOutputShutdown();
	}
	
	@Override
	public void sendUrgentData(int data) throws IOException {
		this.socketInterface.sendUrgentData(data);
	}
	
	@Override
	public void setKeepAlive(boolean on) throws SocketException {
		this.socketInterface.setKeepAlive(on);
	}
	
	@Override
	public void setOOBInline(boolean on) throws SocketException {
		this.socketInterface.setOOBInline(on);
	}
	
	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socketInterface.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}
	
	@Override
	public void setReceiveBufferSize(
			int size) throws SocketException {
		this.socketInterface.setReceiveBufferSize(size);
	}
	
	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socketInterface.setReuseAddress(on);
	}
	
	@Override
	public void setSendBufferSize(
			int size) throws SocketException {
		this.socketInterface.setSendBufferSize(size);
	}
	
	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException {
		this.socketInterface.setSoLinger(on, linger);
	}
	
	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.socketInterface.setSoTimeout(timeout);
	}
	
	@Override
	public void setTcpNoDelay(boolean on) throws SocketException {
		this.socketInterface.setTcpNoDelay(on);
	}
	
	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socketInterface.setTrafficClass(tc);
	}
	
	@Override
	public void shutdownInput() throws IOException {
		this.socketInterface.shutdownInput();
	}
	
	@Override
	public void shutdownOutput() throws IOException {
		this.socketInterface.shutdownOutput();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socketInterface=")
			.append(this.socketInterface)
			.append("]");
		return builder.toString();
	}

}
