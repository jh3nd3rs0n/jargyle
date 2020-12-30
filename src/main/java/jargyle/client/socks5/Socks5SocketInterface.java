package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;

public final class Socks5SocketInterface extends SocketInterface {

	private static final class Socks5SocketInterfaceImpl {
		
		private boolean connected;
		private SocketInterface originalSocketInterface;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private SocketInterface socketInterface;
		private final Socks5Client socks5Client;
		
		public Socks5SocketInterfaceImpl(
				final Socks5Client client,
				final SocketInterface originalSockInterface,
				final SocketInterface sockInterface) {
			this.connected = originalSockInterface.isConnected();
			this.originalSocketInterface = originalSockInterface;
			this.remoteInetAddress = originalSockInterface.getInetAddress();
			this.remotePort = originalSockInterface.getPort();
			this.remoteSocketAddress = originalSockInterface.getRemoteSocketAddress();
			this.socketInterface = (sockInterface == null) ? 
					originalSockInterface : sockInterface;
			this.socks5Client = client;
		}
		
		public void close() throws IOException {
			this.connected = false;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
			this.socketInterface.close();
		}
		
		public void connect(SocketAddress endpoint) throws IOException {
			this.connect(endpoint, 0);
		}
		
		public void connect(
				SocketAddress endpoint,	int timeout) throws IOException {
			if (endpoint == null || !(endpoint instanceof InetSocketAddress)) {
				throw new IllegalArgumentException(
						"endpoint must be an instance of InetSocketAddress");
			}
			InetSocketAddress inetSocketAddress = (InetSocketAddress) endpoint;
			this.socks5Connect(
					inetSocketAddress.getAddress(), 
					inetSocketAddress.getPort(), 
					timeout);
		}

		public void socks5Connect(
				final InetAddress inetAddress,
				final int port,
				final int timeout) throws IOException {
			this.socketInterface = this.originalSocketInterface;
			SocketInterface sockInterface = socks5Client.connectToSocksServerWith(
					this.socketInterface, timeout);
			InputStream inputStream = sockInterface.getInputStream();
			OutputStream outputStream = sockInterface.getOutputStream();
			String address = inetAddress.getHostAddress();
			AddressType addressType = AddressType.get(address);
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.CONNECT, 
					addressType, 
					address, 
					port);
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format("received reply: %s", reply));
			}
			this.connected = true;
			this.remoteInetAddress = InetAddress.getByName(
					socks5Rep.getServerBoundAddress());
			this.remotePort = socks5Rep.getServerBoundPort();
			this.remoteSocketAddress = new InetSocketAddress(
					this.remoteInetAddress,
					this.remotePort);
			this.socketInterface = sockInterface;
		}
		
	}
	
	private final Socks5Client socks5Client;
	private final Socks5SocketInterfaceImpl socks5SocketInterfaceImpl;
	
	public Socks5SocketInterface(final Socks5Client client) {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				new DirectSocketInterface(new Socket()),
				null); 
	}
	
	public Socks5SocketInterface(
			final Socks5Client client, 
			final InetAddress address, 
			final int port) throws IOException {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				new DirectSocketInterface(new Socket()),
				null);
		this.socks5SocketInterfaceImpl.socks5Connect(address, port, 0);
	}
	
	public Socks5SocketInterface(
			final Socks5Client client, 
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				new DirectSocketInterface(new Socket()),
				null);
		this.socks5SocketInterfaceImpl.socketInterface.bind(
				new InetSocketAddress(localAddr, localPort));
		this.socks5SocketInterfaceImpl.socks5Connect(address, port, 0);
	}

	Socks5SocketInterface(
			final Socks5Client client, 
			final SocketInterface originalSockInterface, 
			final SocketInterface sockInterface) {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				originalSockInterface,
				sockInterface);
	}

	public Socks5SocketInterface(
			final Socks5Client client, 
			final String host, 
			final int port) throws UnknownHostException, IOException {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				new DirectSocketInterface(new Socket()),
				null);
		this.socks5SocketInterfaceImpl.socks5Connect(
				InetAddress.getByName(host), port, 0);
	}

	public Socks5SocketInterface(
			final Socks5Client client, 
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		this.socks5Client = client;
		this.socks5SocketInterfaceImpl = new Socks5SocketInterfaceImpl(
				client,
				new DirectSocketInterface(new Socket()),
				null);		
		this.socks5SocketInterfaceImpl.socketInterface.bind(
				new InetSocketAddress(localAddr, localPort));
		this.socks5SocketInterfaceImpl.socks5Connect(
				InetAddress.getByName(host), port, 0);
	}
	
	
	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		this.socks5SocketInterfaceImpl.socketInterface.bind(bindpoint);
	}

	@Override
	public void close() throws IOException {
		this.socks5SocketInterfaceImpl.close();
	}

	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		this.socks5SocketInterfaceImpl.connect(endpoint);
	}

	@Override
	public void connect(SocketAddress endpoint, int timeout) throws IOException {
		this.socks5SocketInterfaceImpl.connect(endpoint, timeout);
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5SocketInterfaceImpl.remoteInetAddress;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.socks5SocketInterfaceImpl.socketInterface.getInputStream();
	}

	@Override
	public boolean getKeepAlive() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getKeepAlive();
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5SocketInterfaceImpl.socketInterface.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5SocketInterfaceImpl.socketInterface.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5SocketInterfaceImpl.socketInterface.getLocalSocketAddress();
	}

	@Override
	public boolean getOOBInline() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getOOBInline();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.socks5SocketInterfaceImpl.socketInterface.getOutputStream();
	}

	@Override
	public int getPort() {
		return this.socks5SocketInterfaceImpl.remotePort;
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5SocketInterfaceImpl.remoteSocketAddress;
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getReuseAddress();
	}

	@Override
	public int getSendBufferSize() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getSendBufferSize();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public int getSoLinger() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getSoLinger();
	}

	@Override
	public int getSoTimeout() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getSoTimeout();
	}

	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getTcpNoDelay();
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return this.socks5SocketInterfaceImpl.socketInterface.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.socks5SocketInterfaceImpl.socketInterface.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5SocketInterfaceImpl.socketInterface.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5SocketInterfaceImpl.connected;
	}

	@Override
	public boolean isInputShutdown() {
		return this.socks5SocketInterfaceImpl.socketInterface.isInputShutdown();
	}

	@Override
	public boolean isOutputShutdown() {
		return this.socks5SocketInterfaceImpl.socketInterface.isOutputShutdown();
	}

	@Override
	public void sendUrgentData(int data) throws IOException {
		this.socks5SocketInterfaceImpl.socketInterface.sendUrgentData(data);
	}

	@Override
	public void setKeepAlive(boolean on) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setKeepAlive(on);

	}

	@Override
	public void setOOBInline(boolean on) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setOOBInline(on);
	}

	@Override
	public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
		this.socks5SocketInterfaceImpl.socketInterface.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setReuseAddress(on);

	}

	@Override
	public void setSendBufferSize(int size) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setSendBufferSize(size);
	}

	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setSoLinger(on, linger);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setSoTimeout(timeout);
	}

	@Override
	public void setTcpNoDelay(boolean on) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setTcpNoDelay(on);
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socks5SocketInterfaceImpl.socketInterface.setTrafficClass(tc);
	}

	@Override
	public void shutdownInput() throws IOException {
		this.socks5SocketInterfaceImpl.socketInterface.shutdownInput();
	}

	@Override
	public void shutdownOutput() throws IOException {
		this.socks5SocketInterfaceImpl.socketInterface.shutdownOutput();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5Client=")
			.append(this.socks5Client)
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", getRemoteSocketAddress()=")
			.append(this.getRemoteSocketAddress())
			.append("]");
		return builder.toString();
	}

}
