package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import jargyle.common.net.FilterSocket;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;

public final class Socks5Socket extends FilterSocket {

	private static void connect(
			final Socks5Socket socks5Socket,
			final InetAddress inetAddress,
			final int port,
			final int timeout) throws IOException {
		Socket sock = socks5Socket.socks5Client.connectToSocksServerWith(
				socks5Socket.socket, timeout);
		InputStream inputStream = sock.getInputStream();
		OutputStream outputStream = sock.getOutputStream();
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
		socks5Socket.connected = true;
		socks5Socket.remoteInetAddress = InetAddress.getByName(
				socks5Rep.getServerBoundAddress());
		socks5Socket.remotePort = socks5Rep.getServerBoundPort();
		socks5Socket.remoteSocketAddress = new InetSocketAddress(
				socks5Socket.remoteInetAddress,
				socks5Socket.remotePort);
		socks5Socket.socket = sock;
	}

	private boolean connected;
	private Socket originalSocket;
	private InetAddress remoteInetAddress;
	private int remotePort;
	private SocketAddress remoteSocketAddress;
	private final Socks5Client socks5Client;
	
	public Socks5Socket(final Socks5Client client) {
		super();
		this.connected = false;
		this.originalSocket = this.socket;
		this.remoteInetAddress = null;
		this.remotePort = 0;
		this.remoteSocketAddress = null;
		this.socks5Client = client;
	}
	
	public Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port) throws IOException {
		this(client);
		connect(this, address, port, 0);
	}
	
	public Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		this(client);
		this.socket.bind(new InetSocketAddress(localAddr, localPort));
		connect(this, address, port, 0);
	}

	Socks5Socket(
			final Socks5Client client, 
			final Socket originalSock, 
			final Socket sock) {
		super(sock);
		this.connected = sock.isConnected();
		this.originalSocket = originalSock;
		this.remoteInetAddress = sock.getInetAddress();
		this.remotePort = sock.getPort();
		this.remoteSocketAddress = sock.getRemoteSocketAddress();
		this.socks5Client = client;
	}

	public Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port) throws UnknownHostException, IOException {
		this(client);
		connect(this, InetAddress.getByName(host), port, 0);
	}

	public Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		this(client);
		this.socket.bind(new InetSocketAddress(localAddr, localPort));
		connect(this, InetAddress.getByName(host), port, 0);
	}

	@Override
	public synchronized void close() throws IOException {
		super.close();
		this.connected = false;
		this.remoteInetAddress = null;
		this.remotePort = 0;
		this.remoteSocketAddress = null;
	}
	
	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		this.connect(endpoint, 0);
	}

	@Override
	public void connect(
			SocketAddress endpoint,	int timeout) throws IOException {
		if (endpoint == null || !(endpoint instanceof InetSocketAddress)) {
			throw new IllegalArgumentException(
					"endpoint must be an instance of InetSocketAddress");
		}
		if (this.connected) {
			this.socket.close();
			this.socket = this.originalSocket;
		}
		InetSocketAddress inetSocketAddress = (InetSocketAddress) endpoint;
		connect(
				this, 
				inetSocketAddress.getAddress(), 
				inetSocketAddress.getPort(), 
				timeout);
	}

	@Override
	public InetAddress getInetAddress() {
		return this.remoteInetAddress;
	}

	@Override
	public int getPort() {
		return this.remotePort;
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.remoteSocketAddress;
	}
	
	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", getRemoteSocketAddress()=")
			.append(this.getRemoteSocketAddress())
			.append(", socks5Client=")
			.append(this.socks5Client)
			.append("]");
		return builder.toString();
	}
	
}
