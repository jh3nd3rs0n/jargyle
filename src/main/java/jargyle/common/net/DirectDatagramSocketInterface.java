package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DirectDatagramSocketInterface extends DatagramSocketInterface {
	
	private final DatagramSocket datagramSocket;
	
	public DirectDatagramSocketInterface(final DatagramSocket datagramSock) {
		this.datagramSocket = datagramSock;
	}
	
	@Override
	public void bind(SocketAddress addr) throws SocketException {
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
	public boolean getBroadcast() throws SocketException {
		return this.datagramSocket.getBroadcast();
	}
	
	public DatagramSocket getDatagramSocket() {
		return this.datagramSocket;
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
	public int getPort() {
		return this.datagramSocket.getPort();
	}
	
	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.datagramSocket.getReceiveBufferSize();
	}
	
	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.datagramSocket.getRemoteSocketAddress();
	}
	
	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.datagramSocket.getReuseAddress();
	}
	
	@Override
	public int getSendBufferSize() throws SocketException {
		return this.datagramSocket.getSendBufferSize();
	}
	
	@Override
	public int getSoTimeout() throws SocketException {
		return this.datagramSocket.getSoTimeout();
	}
	
	@Override
	public int getTrafficClass() throws SocketException {
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
	public void receive(DatagramPacket p) throws IOException {
		this.datagramSocket.receive(p);
	}
	
	@Override
	public void send(DatagramPacket p) throws IOException {
		this.datagramSocket.send(p);
	}
	
	@Override
	public void setBroadcast(boolean on) throws SocketException {
		this.datagramSocket.setBroadcast(on);
	}
	
	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.datagramSocket.setReceiveBufferSize(size);
	}
	
	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.datagramSocket.setReuseAddress(on);
	}
	
	@Override
	public void setSendBufferSize(int size) throws SocketException {
		this.datagramSocket.setSendBufferSize(size);
	}
	
	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.datagramSocket.setSoTimeout(timeout);
	}
	
	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.datagramSocket.setTrafficClass(tc);
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
