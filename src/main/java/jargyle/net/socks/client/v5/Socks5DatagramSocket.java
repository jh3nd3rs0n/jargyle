package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.DatagramChannel;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.AddressType;
import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.net.socks.transport.v5.Socks5Request;
import jargyle.net.socks.transport.v5.UdpRequestHeader;
import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;

public final class Socks5DatagramSocket extends DatagramSocket {

	private static final class Socks5DatagramSocketImpl {
		
		private boolean connected;
		private DatagramSocket datagramSocket;
		private DatagramSocket originalDatagramSocket;
		private Socket originalSocket;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private Socket socket;
		private final Socks5Client socks5Client;
		private InetAddress udpRelayServerInetAddress;
		private int udpRelayServerPort;
		
		public Socks5DatagramSocketImpl(
				final Socks5Client client) throws SocketException {
			DatagramSocket originalDatagramSock = new DatagramSocket(null);
			Socket originalSock = new Socket();
			SocketSettings socketSettings = client.getProperties().getValue(
					PropertySpec.SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(originalSock);
			this.connected = false;
			this.datagramSocket = originalDatagramSock;
			this.originalDatagramSocket = originalDatagramSock;
			this.originalSocket = originalSock;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
			this.socket = originalSock;
			this.socks5Client = client;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
		}
		
		public void bind(SocketAddress addr) throws SocketException {
			if (this.datagramSocket.isBound()) {
				throw new SocketException("socket is already bound");
			}
			InetAddress wildcardInetAddress = 
					Host.getIpv4WildcardInstance().toInetAddress();
			int port = 0;
			InetAddress inetAddress = wildcardInetAddress;
			if (addr != null) {
				if (!(addr instanceof InetSocketAddress)) {
					throw new IllegalArgumentException(
							"bind address must be an instance of InetSocketAddress");
				}
				InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
				port = inetSocketAddress.getPort();
				inetAddress = inetSocketAddress.getAddress();
			}
			try {
				this.socks5UdpAssociate(port, inetAddress);
			} catch (IOException e) {
				StringBuilder sb = new StringBuilder(e.toString());
				sb.append(String.format("%n"));
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				pw.flush();
				sb.append(sw.toString());
				throw new SocketException(sb.toString());
			}
		}
		
		public void close() {
			this.datagramSocket.close();
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
			try {
				this.socket.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		
		public void connect(InetAddress address, int port) {
			if (address == null) {
				throw new IllegalArgumentException("inetAddress must not be null");
			}
			if (port < 1 || port > Port.MAX_INT_VALUE) {
				throw new IllegalArgumentException("port is out of range");
			}
			this.connected = true;
			this.remoteInetAddress = address;
			this.remotePort = port;
			this.remoteSocketAddress = new InetSocketAddress(address, port);
		}
		
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
		
		public void disconnect() {
			this.connected = false;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
		}
		
		public void receive(final DatagramPacket p) throws IOException {
			if (this.datagramSocket.isClosed()) {
				throw new SocketException("socket is closed");
			}
			if (!this.datagramSocket.isBound()) {
				throw new SocketException("socket is not bound");
			}
			this.datagramSocket.receive(p);
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
		
		public void send(final DatagramPacket p) throws IOException {
			if (this.datagramSocket.isClosed()) {
				throw new SocketException("socket is closed");
			}
			SocketAddress socketAddress = p.getSocketAddress();
			if (socketAddress != null 
					&& this.connected 
					&& !this.remoteSocketAddress.equals(socketAddress)) {
				throw new IllegalArgumentException(
						"packet address and connected socket address must be the same");
			}
			if (!this.datagramSocket.isBound()) {
				throw new SocketException("socket is not bound");
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
			this.datagramSocket.send(p);
		}
		
		public void socks5UdpAssociate(
				final int port, 
				final InetAddress inetAddress) throws IOException {
			if (!this.socket.equals(this.originalSocket)) {
				this.socket = this.originalSocket;
			}
			Socket sock = this.socks5Client.getConnectedSocket(
					this.socket, true);
			if (!this.datagramSocket.equals(this.originalDatagramSocket)) {
				this.datagramSocket = this.originalDatagramSocket;
			}
			this.datagramSocket.bind(new InetSocketAddress(inetAddress, port));
			String address = 
					this.datagramSocket.getLocalAddress().getHostAddress();
			int prt = this.datagramSocket.getLocalPort();
			InputStream inputStream = sock.getInputStream();
			OutputStream outputStream = sock.getOutputStream();
			AddressType addressType = AddressType.get(address);
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.UDP_ASSOCIATE, 
					addressType, 
					address, 
					prt);
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format(
						"received reply: %s", reply));
			}
			DatagramSocket datagramSock = 
					this.socks5Client.getConnectedDatagramSocket(
							this.datagramSocket, 
							socks5Rep.getServerBoundAddress(),
							socks5Rep.getServerBoundPort());
			if (sock instanceof GssSocket) {
				GssSocket gssSocket = (GssSocket) sock;
				datagramSock = new GssDatagramSocket(
						datagramSock,
						gssSocket.getGSSContext(),
						gssSocket.getMessageProp());
			}
			this.datagramSocket = datagramSock;
			this.udpRelayServerInetAddress = 
					InetAddress.getByName(socks5Rep.getServerBoundAddress());
			this.udpRelayServerPort = socks5Rep.getServerBoundPort();
			this.socket = sock;
		}
		
	}
	
	private final Socks5Client socks5Client;
	private final Socks5DatagramSocketImpl socks5DatagramSocketImpl;
	
	public Socks5DatagramSocket(
			final Socks5Client client) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;
		this.socks5DatagramSocketImpl =	new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.bind(new InetSocketAddress(
				(InetAddress) null, 0));
	}

	public Socks5DatagramSocket(
			final Socks5Client client, final int port) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.bind(new InetSocketAddress(
				(InetAddress) null, port));
	}

	public Socks5DatagramSocket(
			final Socks5Client client, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.bind(new InetSocketAddress(
				laddr, port));		
	}

	public Socks5DatagramSocket(
			final Socks5Client client, 
			final SocketAddress bindaddr) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		if (bindaddr != null) {
			this.socks5DatagramSocketImpl.bind(bindaddr);
		}
	}
	
	@Override
	public synchronized void bind(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketImpl.bind(addr);
	}

	@Override
	public void close() {
		this.socks5DatagramSocketImpl.close();
	}

	@Override
	public void connect(InetAddress address, int port) {
		this.socks5DatagramSocketImpl.connect(address, port);
	}

	@Override
	public void connect(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketImpl.connect(addr);
	}

	@Override
	public void disconnect() {
		this.socks5DatagramSocketImpl.disconnect();
	}

	@Override
	public synchronized boolean getBroadcast() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getBroadcast();
	}

	@Override
	public DatagramChannel getChannel() {
		return null;
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5DatagramSocketImpl.remoteInetAddress;
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5DatagramSocketImpl.datagramSocket.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5DatagramSocketImpl.datagramSocket.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5DatagramSocketImpl.datagramSocket.getLocalSocketAddress();
	}

	@Override
	public int getPort() {
		return this.socks5DatagramSocketImpl.remotePort;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5DatagramSocketImpl.remoteSocketAddress;
	}

	@Override
	public synchronized boolean getReuseAddress() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getReuseAddress();
	}

	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getSendBufferSize();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public synchronized int getSoTimeout() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getSoTimeout();
	}

	@Override
	public synchronized int getTrafficClass() throws SocketException {
		return this.socks5DatagramSocketImpl.datagramSocket.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.socks5DatagramSocketImpl.datagramSocket.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5DatagramSocketImpl.datagramSocket.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5DatagramSocketImpl.connected;
	}

	@Override
	public synchronized void receive(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketImpl.receive(p);
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketImpl.send(p);
	}

	@Override
	public synchronized void setBroadcast(boolean on) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setBroadcast(on);
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setReceiveBufferSize(size);
	}

	@Override
	public synchronized void setReuseAddress(boolean on) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setReuseAddress(on);
	}

	@Override
	public synchronized void setSendBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setSendBufferSize(size);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setSoTimeout(timeout);
	}

	@Override
	public synchronized void setTrafficClass(int tc) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.setTrafficClass(tc);
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
