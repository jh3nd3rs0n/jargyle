package jargyle.net.socks5;

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

import jargyle.net.DatagramSocketInterface;
import jargyle.net.DirectDatagramSocketInterface;
import jargyle.net.DirectSocketInterface;
import jargyle.net.SocketInterface;
import jargyle.net.SocketSettings;
import jargyle.net.socks.PropertySpec;
import jargyle.net.socks5.gssapiauth.GssDatagramSocketInterface;
import jargyle.net.socks5.gssapiauth.GssSocketInterface;

public final class Socks5DatagramSocketInterface 
	extends DatagramSocketInterface {

	private static final class Socks5DatagramSocketInterfaceImpl {
		
		private boolean connected;
		private DatagramSocketInterface datagramSocketInterface;
		private DatagramSocketInterface directDatagramSocketInterface;
		private SocketInterface directSocketInterface;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private SocketInterface socketInterface;
		private final Socks5Client socks5Client;
		private InetAddress udpRelayServerInetAddress;
		private int udpRelayServerPort;
		
		public Socks5DatagramSocketInterfaceImpl(
				final Socks5Client client) throws SocketException {
			DatagramSocketInterface directDatagramSockInterface =
					new DirectDatagramSocketInterface(new DatagramSocket(null));
			SocketInterface directSockInterface = new DirectSocketInterface(
					new Socket());
			SocketSettings socketSettings = client.getProperties().getValue(
					PropertySpec.SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(directSockInterface);
			this.connected = false;
			this.datagramSocketInterface = directDatagramSockInterface;
			this.directDatagramSocketInterface = directDatagramSockInterface;
			this.directSocketInterface = directSockInterface;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
			this.socketInterface = directSockInterface;
			this.socks5Client = client;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
		}
		
		public void bind(SocketAddress addr) throws SocketException {
			if (this.datagramSocketInterface.isBound()) {
				throw new SocketException("socket is already bound");
			}
			InetAddress wildcardInetAddress = null;
			try {
				wildcardInetAddress = InetAddress.getByName(
						AddressType.IP_V4_ADDRESS.getWildcardAddress());
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
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
				throw new SocketException(e.toString());
			}
		}
		
		public void close() {
			this.datagramSocketInterface.close();
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
			try {
				this.socketInterface.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		
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
			if (this.datagramSocketInterface.isClosed()) {
				throw new SocketException("socket is closed");
			}
			if (!this.datagramSocketInterface.isBound()) {
				throw new SocketException("socket is not bound");
			}
			this.datagramSocketInterface.receive(p);
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
			if (this.datagramSocketInterface.isClosed()) {
				throw new SocketException("socket is closed");
			}
			SocketAddress socketAddress = p.getSocketAddress();
			if (socketAddress != null 
					&& this.connected 
					&& !this.remoteSocketAddress.equals(socketAddress)) {
				throw new IllegalArgumentException(
						"packet address and connected socket address must be the same");
			}
			if (!this.datagramSocketInterface.isBound()) {
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
			this.datagramSocketInterface.send(p);
		}
		
		public void socks5UdpAssociate(
				final int port, 
				final InetAddress inetAddress) throws IOException {
			if (!this.socketInterface.equals(this.directSocketInterface)) {
				this.socketInterface = this.directSocketInterface;
			}
			SocketInterface sockInterface = 
					this.socks5Client.connectToSocksServerWith(
							this.socketInterface, true);
			if (!this.datagramSocketInterface.equals(
					this.directDatagramSocketInterface)) {
				this.datagramSocketInterface = 
						this.directDatagramSocketInterface;
			}
			DatagramSocketInterface datagramSockInterface = 
					this.socks5Client.getClientSideSslWrapper().wrapIfSslEnabled(
							this.datagramSocketInterface);
			if (sockInterface instanceof GssSocketInterface) {
				GssSocketInterface gssSocketInterface = 
						(GssSocketInterface) sockInterface;
				datagramSockInterface = new GssDatagramSocketInterface(
						datagramSockInterface,
						gssSocketInterface.getGSSContext(),
						gssSocketInterface.getMessageProp());
			}
			datagramSockInterface.bind(new InetSocketAddress(inetAddress, port));
			String address = 
					datagramSockInterface.getLocalAddress().getHostAddress();
			int prt = datagramSockInterface.getLocalPort();
			InputStream inputStream = sockInterface.getInputStream();
			OutputStream outputStream = sockInterface.getOutputStream();
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
			this.datagramSocketInterface = datagramSockInterface;
			this.udpRelayServerInetAddress = 
					InetAddress.getByName(socks5Rep.getServerBoundAddress());
			this.udpRelayServerPort = socks5Rep.getServerBoundPort();
			this.socketInterface = sockInterface;
		}
		
	}
	
	private final Socks5Client socks5Client;
	private final Socks5DatagramSocketInterfaceImpl socks5DatagramSocketInterfaceImpl;
	
	public Socks5DatagramSocketInterface(
			final Socks5Client client) throws SocketException {
		this.socks5Client = client;
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(client);
		this.socks5DatagramSocketInterfaceImpl.bind(new InetSocketAddress(
				(InetAddress) null, 0));
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, final int port) throws SocketException {
		this.socks5Client = client;		
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(client);
		this.socks5DatagramSocketInterfaceImpl.bind(new InetSocketAddress(
				(InetAddress) null, port));
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		this.socks5Client = client;		
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(client);
		this.socks5DatagramSocketInterfaceImpl.bind(new InetSocketAddress(
				laddr, port));		
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, 
			final SocketAddress bindaddr) throws SocketException {
		this.socks5Client = client;		
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(client);
		if (bindaddr != null) {
			this.socks5DatagramSocketInterfaceImpl.bind(bindaddr);
		}
	}
	
	@Override
	public void bind(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.bind(addr);
	}

	@Override
	public void close() {
		this.socks5DatagramSocketInterfaceImpl.close();
	}

	@Override
	public void connect(InetAddress address, int port) {
		this.socks5DatagramSocketInterfaceImpl.connect(address, port);
	}

	@Override
	public void connect(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.connect(addr);
	}

	@Override
	public void disconnect() {
		this.socks5DatagramSocketInterfaceImpl.disconnect();
	}

	@Override
	public boolean getBroadcast() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getBroadcast();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5DatagramSocketInterfaceImpl.remoteInetAddress;
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalSocketAddress();
	}

	@Override
	public int getPort() {
		return this.socks5DatagramSocketInterfaceImpl.remotePort;
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5DatagramSocketInterfaceImpl.remoteSocketAddress;
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getReuseAddress();
	}

	@Override
	public int getSendBufferSize() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getSendBufferSize();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public int getSoTimeout() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getSoTimeout();
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5DatagramSocketInterfaceImpl.connected;
	}

	@Override
	public void receive(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketInterfaceImpl.receive(p);
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketInterfaceImpl.send(p);
	}

	@Override
	public void setBroadcast(boolean on) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setBroadcast(on);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setReuseAddress(on);
	}

	@Override
	public void setSendBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setSendBufferSize(size);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setSoTimeout(timeout);
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setTrafficClass(tc);
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
