package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;

import jargyle.client.PropertySpec;
import jargyle.common.net.DatagramPacketFilter;
import jargyle.common.net.DatagramPacketFilterFactory;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.common.net.socks5.UdpRequestHeader;

public final class Socks5DatagramSocket extends DatagramSocket {
	
	private static void connectInit(
			final Socks5DatagramSocket socks5DatagramSocket) {
		socks5DatagramSocket.connected = false;
		socks5DatagramSocket.remoteInetAddress = null;
		socks5DatagramSocket.remotePort = -1;
		socks5DatagramSocket.remoteSocketAddress = null;
	}
	
	private static void init(
			final Socks5DatagramSocket socks5DatagramSocket) throws SocketException {
		socks5DatagramSocket.associated = false;
		socks5DatagramSocket.socket = new Socket();
		socks5DatagramSocket.datagramPacketFilter = 
				DatagramPacketFilterFactory.newDatagramPacketFilter(
						socks5DatagramSocket.socket);
		SocketSettings socketSettings = 
				socks5DatagramSocket.socks5Client.getProperties().getValue(
						PropertySpec.SOCKET_SETTINGS, SocketSettings.class);
		socketSettings.applyTo(socks5DatagramSocket.socket);
		socks5DatagramSocket.udpRelayServerInetAddress = null;
		socks5DatagramSocket.udpRelayServerPort = -1;
	}
	
	private boolean associated;
	private boolean connected;
	private DatagramPacketFilter datagramPacketFilter;
	private InetAddress remoteInetAddress;
	private int remotePort;
	private SocketAddress remoteSocketAddress;
	private Socket socket;
	private final Socks5Client socks5Client;
	private InetAddress udpRelayServerInetAddress;
	private int udpRelayServerPort;
	
	public Socks5DatagramSocket(
			final Socks5Client client) throws SocketException {
		this.socks5Client = client;
		init(this);
		connectInit(this);
	}

	public Socks5DatagramSocket(
			final Socks5Client client, final int port) throws SocketException {
		super(port);
		this.socks5Client = client;
		init(this);
		connectInit(this);
	}

	public Socks5DatagramSocket(
			final Socks5Client client, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		super(port, laddr);
		this.socks5Client = client;		
		init(this);
		connectInit(this);
	}

	public Socks5DatagramSocket(
			final Socks5Client client, 
			final SocketAddress bindaddr) throws SocketException {
		super(bindaddr);
		this.socks5Client = client;
		init(this);
		connectInit(this);
	}

	private void associate() throws IOException {
		String address = null;
		int port = -1;
		if (!this.connected) {
			address = AddressType.IP_V4_ADDRESS.getWildcardAddress();
			port = 0;
		} else {
			address = this.remoteInetAddress.getHostAddress();
			port = this.remotePort;
		}
		this.socks5Client.bind(this.socket);
		this.socks5Client.connectToSocksServerWith(this.socket);
		Socket sock = this.socks5Client.authenticate(this.socket);
		InputStream inputStream = sock.getInputStream();
		OutputStream outputStream = sock.getOutputStream();
		AddressType addressType = AddressType.get(address);
		Socks5Request socks5Req = Socks5Request.newInstance(
				Command.UDP_ASSOCIATE, 
				addressType, 
				address, 
				port);
		outputStream.write(socks5Req.toByteArray());
		outputStream.flush();
		Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
		Reply reply = socks5Rep.getReply();
		if (!reply.equals(Reply.SUCCEEDED)) {
			throw new IOException(String.format(
					"received reply: %s", reply));
		}
		this.datagramPacketFilter = 
				DatagramPacketFilterFactory.newDatagramPacketFilter(sock);
		this.udpRelayServerInetAddress = InetAddress.getByName(
				socks5Rep.getServerBoundAddress());
		this.udpRelayServerPort = socks5Rep.getServerBoundPort();
		this.socket = sock;
		this.associated = true;
	}
	
	@Override
	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		super.close();
		try {
			init(this);
		} catch (SocketException e) {
			throw new AssertionError(e);
		}
	}
	
	@Override
	public void connect(InetAddress address, int port) {
		if (address == null) {
			throw new IllegalArgumentException("inetAddress must not be null");
		}
		if (port < 1 || port > 0xffff) {
			throw new IllegalArgumentException("port is out of range");
		}
		this.connected = true;
		this.remoteInetAddress = address;
		this.remotePort = port;
		this.remoteSocketAddress = new InetSocketAddress(address, port);
	}
	
	@Override
	public void connect(SocketAddress addr) throws SocketException {
		if (addr == null || !(addr instanceof InetSocketAddress)) {
			throw new IllegalArgumentException(
					"address must be an instance of InetSocketAddress");
		}
		InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
		this.connect(
				inetSocketAddress.getAddress(), 
				inetSocketAddress.getPort());
	}
	
	@Override
	public void disconnect() {
		connectInit(this);
	}
	
	@Override
	public DatagramChannel getChannel() {
		return null;
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
	public synchronized void receive(
			final DatagramPacket p) throws IOException {
		super.receive(p);
		this.datagramPacketFilter.filterAfterReceive(p);
		UdpRequestHeader header = null; 
		try {
			header = UdpRequestHeader.newInstance(p.getData());
		} catch (IllegalArgumentException e) {
			throw new IOException(
					"error in parsing UDP header request", e);
		}
		byte[] userData = header.getUserData();
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(
					header.getDesiredDestinationAddress());
		} catch (UnknownHostException e) {
			throw new IOException("error in determining address", e);
		}
		int inetPort = header.getDesiredDestinationPort();
		p.setData(userData, 0, userData.length);
		p.setLength(userData.length);
		p.setAddress(inetAddress);
		p.setPort(inetPort);		
	}

	@Override
	public void send(final DatagramPacket p) throws IOException {
		if (this.isClosed()) {
			throw new SocketException("socket is closed");
		}
		SocketAddress socketAddress = p.getSocketAddress();
		if (socketAddress != null 
				&& this.connected 
				&& !this.remoteSocketAddress.equals(socketAddress)) {
			throw new IllegalArgumentException(
					"packet address and connected socket address must be the same");
		}
		if (!this.associated) {
			this.associate();
		}
		String address = p.getAddress().getHostAddress();
		int port = p.getPort();
		AddressType addressType = AddressType.get(address);
		byte[] headerBytes = UdpRequestHeader.newInstance(
				0,
				addressType,
				address,
				port,
				p.getData()).toByteArray();
		p.setData(headerBytes, 0, headerBytes.length);
		p.setLength(headerBytes.length);
		p.setAddress(this.udpRelayServerInetAddress);
		p.setPort(this.udpRelayServerPort);
		this.datagramPacketFilter.filterBeforeSend(p);
		super.send(p);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", socks5Client=")
			.append(this.socks5Client)
			.append("]");
		return builder.toString();
	}
	
}
