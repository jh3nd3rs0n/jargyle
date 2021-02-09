package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

public final class DatagramSocketInterfaceDatagramSocket 
    extends DatagramSocket {

	private final DatagramSocketInterface datagramSocketInterface;
	
	public DatagramSocketInterfaceDatagramSocket(
			final DatagramSocketInterface datagramSockInterface) 
			throws SocketException {
		super((SocketAddress) null);
		this.datagramSocketInterface = datagramSockInterface;
	}
	
	@Override
	public void bind(SocketAddress addr) throws SocketException {
		this.datagramSocketInterface.bind(addr);
	}

	@Override
	public void close() {
		this.datagramSocketInterface.close();
	}

	@Override
	public void connect(InetAddress address, int port) {
		this.datagramSocketInterface.connect(address, port);
	}

	@Override
	public void connect(SocketAddress addr) throws SocketException {
		this.datagramSocketInterface.connect(addr);
	}

	@Override
	public void disconnect() {
		this.datagramSocketInterface.disconnect();
	}

	@Override
	public boolean getBroadcast() throws SocketException {
		return this.datagramSocketInterface.getBroadcast();
	}

	@Override
	public DatagramChannel getChannel() {
		return null;
	}

	@Override
	public InetAddress getInetAddress() {
		return this.datagramSocketInterface.getInetAddress();
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.datagramSocketInterface.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.datagramSocketInterface.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.datagramSocketInterface.getLocalSocketAddress();
	}

	@Override
	public int getPort() {
		return this.datagramSocketInterface.getPort();
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.datagramSocketInterface.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.datagramSocketInterface.getRemoteSocketAddress();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.datagramSocketInterface.getReuseAddress();
	}

	@Override
	public int getSendBufferSize() throws SocketException {
		return this.datagramSocketInterface.getSendBufferSize();
	}

	@Override
	public int getSoTimeout() throws SocketException {
		return this.datagramSocketInterface.getSoTimeout();
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return this.datagramSocketInterface.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.datagramSocketInterface.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.datagramSocketInterface.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.datagramSocketInterface.isConnected();
	}

	@Override
	public void receive(DatagramPacket p) throws IOException {
		this.datagramSocketInterface.receive(p);
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		this.datagramSocketInterface.send(p);
	}

	@Override
	public void setBroadcast(boolean on) throws SocketException {
		this.datagramSocketInterface.setBroadcast(on);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.datagramSocketInterface.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.datagramSocketInterface.setReuseAddress(on);
	}

	@Override
	public void setSendBufferSize(int size) throws SocketException {
		this.datagramSocketInterface.setSendBufferSize(size);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.datagramSocketInterface.setSoTimeout(timeout);
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.datagramSocketInterface.setTrafficClass(tc);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [datagramSocketInterface=")
			.append(this.datagramSocketInterface)
			.append("]");
		return builder.toString();
	}
	
}
