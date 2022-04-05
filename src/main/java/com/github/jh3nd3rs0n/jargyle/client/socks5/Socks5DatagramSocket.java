package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.DatagramChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Exception;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

public final class Socks5DatagramSocket extends DatagramSocket {

	private static final class Socks5DatagramSocketImpl {
		
		private static enum AssociationStatus {
			UNASSOCIATED,
			ASSOCIATING,
			ASSOCIATED
		}
		
		private static final class AssociationStatusHelper {
			
			private static final Map<UUID, AssociationStatus> MAP =
					Collections.synchronizedMap(new HashMap<UUID, AssociationStatus>());
			
			public static boolean has(
					final UUID uuid, final AssociationStatus associationStatus) {
				if (!MAP.containsKey(uuid)) {
					return false;
				}
				AssociationStatus value = MAP.get(uuid);
				return value.equals(associationStatus);
			}
			
			public static void put(
					final UUID uuid, final AssociationStatus associationStatus) {
				MAP.put(uuid, associationStatus);
			}
			
			public static void remove(final UUID uuid) {
				MAP.remove(uuid);
			}
			
			private AssociationStatusHelper() { }
			
		}
		
		private static final int HALF_SECOND = 500;
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				Socks5DatagramSocketImpl.class);
		
		private UUID associationStatusUuid;
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
			client.configureInternalSocket(originalSock);
			UUID statusUuid = UUID.randomUUID();
			AssociationStatusHelper.put(statusUuid, AssociationStatus.UNASSOCIATED);
			this.associationStatusUuid = statusUuid;
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
		
		public void close() {
			AssociationStatusHelper.remove(this.associationStatusUuid);
			this.datagramSocket.close();
			try {
				this.socket.close();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
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
			// this.socks5UdpAssociateIfNotAssociated();
			if (this.socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_UDP_ASSOCIATE_USE_CLIENT_MODE).booleanValue()) {
				LOGGER.info("{}: Waiting for completed association", this.datagramSocket.getLocalSocketAddress());
				this.waitForCompleteAssociation();
				LOGGER.info("{}: Finished waiting for completed association", this.datagramSocket.getLocalSocketAddress());
			} else {
				if (AssociationStatusHelper.has(
						this.associationStatusUuid, AssociationStatus.ASSOCIATING)) {
					LOGGER.info("{}: Waiting for completed association", this.datagramSocket.getLocalSocketAddress());
					this.waitForCompleteAssociation();
					LOGGER.info("{}: Finished waiting for completed association", this.datagramSocket.getLocalSocketAddress());
				} 
				else if (AssociationStatusHelper.has(
						this.associationStatusUuid, AssociationStatus.UNASSOCIATED)) {
					LOGGER.info("{}: Associating...", this.datagramSocket.getLocalSocketAddress());
					this.socks5UdpAssociate();
					LOGGER.info("{}: ...Associated", this.datagramSocket.getLocalSocketAddress());
				}
			}
			this.datagramSocket.receive(p);
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(p.getData());
			} catch (IllegalArgumentException e) {
				throw new Socks5Exception(
						"error in parsing UDP header request", e);
			}
			byte[] userData = header.getUserData();
			InetAddress inetAddress = InetAddress.getByName(
					header.getDesiredDestinationAddress());
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
			if (!this.datagramSocket.isBound()) {
				throw new SocketException("socket is not bound");
			}
			SocketAddress socketAddress = p.getSocketAddress();
			if (this.connected 
					&& this.remoteSocketAddress != null 
					&& !this.remoteSocketAddress.equals(socketAddress)) {
				throw new IllegalArgumentException(
						"packet address and connected socket address must be the same");
			}
			// this.socks5UdpAssociateIfNotAssociated();
			if (this.socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_UDP_ASSOCIATE_USE_CLIENT_MODE).booleanValue()) {
				if (AssociationStatusHelper.has(
						this.associationStatusUuid, AssociationStatus.ASSOCIATING)) {
					LOGGER.info("{}: Waiting for completed association", this.datagramSocket.getLocalSocketAddress());
					this.waitForCompleteAssociation();
					LOGGER.info("{}: Finished waiting for completed association", this.datagramSocket.getLocalSocketAddress());
				} else if (AssociationStatusHelper.has(
						this.associationStatusUuid, AssociationStatus.UNASSOCIATED)) {
					LOGGER.info("{}: Associating...", this.datagramSocket.getLocalSocketAddress());
					this.socks5UdpAssociate();
					LOGGER.info("{}: ...Associated", this.datagramSocket.getLocalSocketAddress());
				}
			} else {
				LOGGER.info("{}: Waiting for completed association", this.datagramSocket.getLocalSocketAddress());
				this.waitForCompleteAssociation();
				LOGGER.info("{}: Finished waiting for completed association", this.datagramSocket.getLocalSocketAddress());				
			}
			String address = p.getAddress().getHostAddress();
			int port = p.getPort();
			byte[] headerBytes = UdpRequestHeader.newInstance(
					0,
					address,
					port,
					p.getData()).toByteArray();
			p.setData(headerBytes, 0, headerBytes.length);
			p.setLength(headerBytes.length);
			p.setAddress(this.udpRelayServerInetAddress);
			p.setPort(this.udpRelayServerPort);
			this.datagramSocket.send(p);
		}
		
		public void socks5UdpAssociate() throws IOException {
			AssociationStatusHelper.put(
					this.associationStatusUuid, AssociationStatus.ASSOCIATING);
			if (!this.socket.equals(this.originalSocket)) {
				this.socket = this.originalSocket;
			}
			Socket sock = this.socks5Client.getConnectedInternalSocket(
					this.socket, true);
			Method method = this.socks5Client.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.doMethodSubnegotiation(method, sock);
			Socket sck = methodEncapsulation.getSocket();
			if (!this.datagramSocket.equals(this.originalDatagramSocket)) {
				this.datagramSocket = this.originalDatagramSocket;
			}
			DatagramSocket datagramSock = this.datagramSocket;
			String address = datagramSock.getLocalAddress().getHostAddress();
			int prt = datagramSock.getLocalPort();
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.UDP_ASSOCIATE, 
					address, 
					prt);
			this.socks5Client.sendSocks5Request(socks5Req, sck);
			Socks5Reply socks5Rep = this.socks5Client.receiveSocks5Reply(sck); 
			datagramSock = this.socks5Client.getConnectedInternalDatagramSocket(
					datagramSock,
					this.socks5Client.getSocksServerUri().getHost(),
					socks5Rep.getServerBoundPort());
			DatagramSocket datagramSck = methodEncapsulation.getDatagramSocket(
					datagramSock);
			AssociationStatusHelper.put(
					this.associationStatusUuid, AssociationStatus.ASSOCIATED);
			this.datagramSocket = datagramSck;
			this.udpRelayServerInetAddress = InetAddress.getByName(
					socks5Rep.getServerBoundAddress());
			this.udpRelayServerPort = socks5Rep.getServerBoundPort();
			this.socket = sck;
		}
		
		private void socks5UdpAssociateIfNotAssociated() throws IOException {
			if (AssociationStatusHelper.has(
					this.associationStatusUuid, AssociationStatus.ASSOCIATING)) {
				this.waitForCompleteAssociation();
			} else if (AssociationStatusHelper.has(
					this.associationStatusUuid, AssociationStatus.UNASSOCIATED)) {
				this.socks5UdpAssociate();
			}
		}
		
		private void waitForCompleteAssociation() throws IOException {
			int soTimeout = this.socket.getSoTimeout();
			long waitStartTime = System.currentTimeMillis();
			while (!AssociationStatusHelper.has(
					this.associationStatusUuid, AssociationStatus.ASSOCIATED)) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				if (soTimeout == 0) { continue; }
				long timeSinceWaitStartTime = 
						System.currentTimeMillis() - waitStartTime;
				if (timeSinceWaitStartTime >= soTimeout) {
					throw new IOException(
							"timeout for waiting for complete UDP association "
							+ "has been reached");
				}
			}
		}
	}
	
	private final Socks5Client socks5Client;
	private final Socks5DatagramSocketImpl socks5DatagramSocketImpl;
	
	Socks5DatagramSocket(final Socks5Client client) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;
		this.socks5DatagramSocketImpl =	new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.datagramSocket.bind(new InetSocketAddress(
				(InetAddress) null, 0));
	}

	Socks5DatagramSocket(
			final Socks5Client client, final int port) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.datagramSocket.bind(new InetSocketAddress(
				(InetAddress) null, port));
	}

	Socks5DatagramSocket(
			final Socks5Client client, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		this.socks5DatagramSocketImpl.datagramSocket.bind(new InetSocketAddress(
				laddr, port));		
	}

	Socks5DatagramSocket(
			final Socks5Client client, 
			final SocketAddress bindaddr) throws SocketException {
		super((SocketAddress) null);
		this.socks5Client = client;		
		this.socks5DatagramSocketImpl = new Socks5DatagramSocketImpl(client);
		if (bindaddr != null) {
			this.socks5DatagramSocketImpl.datagramSocket.bind(bindaddr);
		}
	}
	
	@Override
	public synchronized void bind(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketImpl.datagramSocket.bind(addr);
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
		throw new UnsupportedOperationException();
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
	public <T> T getOption(SocketOption<T> name) throws IOException {
		return this.socks5DatagramSocketImpl.datagramSocket.getOption(name);
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
	public <T> DatagramSocket setOption(
			SocketOption<T> name, T value) throws IOException {
		this.socks5DatagramSocketImpl.datagramSocket.setOption(name, value);
		return this;
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
	public Set<SocketOption<?>> supportedOptions() {
		return this.socks5DatagramSocketImpl.datagramSocket.supportedOptions();
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
