package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientIOException;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddress;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.DomainName;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.Set;

public final class Socks5DatagramSocket extends DatagramSocket {

	private static final class Socks5DatagramSocketImpl {
		
		private static final int HALF_SECOND = 500;
		
		private volatile boolean associated;
		private volatile boolean connected;
		private volatile DatagramSocket datagramSocket;
		private volatile InetAddress remoteInetAddress;
		private volatile int remotePort;
		private volatile SocketAddress remoteSocketAddress;
		private volatile Socket socket;
		private final Socks5ClientAgent socks5ClientAgent;
		private volatile InetAddress udpRelayServerInetAddress;
		private volatile int udpRelayServerPort;
		
		public Socks5DatagramSocketImpl(
				final Socks5ClientAgent clientAgent) throws SocketException {
			DatagramSocket datagramSock = new DatagramSocket((SocketAddress) null);
			Socket sock = clientAgent.newClientSocketBuilder().newClientSocket();
			this.associated = false;
			this.connected = false;
			this.datagramSocket = datagramSock;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
			this.socket = sock;
			this.socks5ClientAgent = clientAgent;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
		}
		
		public void close() {
			this.associated = false;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
			this.datagramSocket.close();
			try {
				this.socket.close();
			} catch (SocksClientIOException e) {
				throw new UncheckedIOException(e);
			} catch (IOException e) {
				throw new UncheckedIOException(
						this.socks5ClientAgent.toSocksClientIOException(e));
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
			/*
			 * allow for internal datagram socket to connect before invoking
			 * receive() 
			 */
			this.waitForCompleteAssociation();
			this.datagramSocket.receive(p);
			UdpRequest udpRequest;
			try {
				udpRequest = UdpRequest.newInstanceFrom(
						Arrays.copyOfRange(
								p.getData(), p.getOffset(), p.getLength()));
			} catch (IllegalArgumentException e) {
				throw new Socks5Exception(
						"error in parsing UDP request", e);
			}
			byte[] userData = udpRequest.getUserData();
			InetAddress inetAddress = InetAddress.getByName(
					udpRequest.getDesiredDestinationAddress().toString());
			int inetPort = udpRequest.getDesiredDestinationPort().intValue();
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
			if (!this.associated) {
				this.socks5UdpAssociate();
			}
			String address = p.getAddress().getHostAddress();
			int port = p.getPort();
			byte[] udpRequestBytes = UdpRequest.newInstance(
					UnsignedByte.valueOf(0),
					Address.newInstanceFrom(address),
					Port.valueOf(port),
					Arrays.copyOfRange(
							p.getData(),
							p.getOffset(),
							p.getLength())).toByteArray();
			p.setData(udpRequestBytes, 0, udpRequestBytes.length);
			p.setLength(udpRequestBytes.length);
			p.setAddress(this.udpRelayServerInetAddress);
			p.setPort(this.udpRelayServerPort);
			this.datagramSocket.send(p);
		}
		
		public void socks5UdpAssociate() throws IOException {
			Socket sock = this.socks5ClientAgent.newClientSocketBuilder()
					.proceedToConfigure(this.socket)
					.configure()
					.proceedToConnect()
					.setToBind(true)
					.getConnectedClientSocket();
			Method method = this.socks5ClientAgent.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation = 
					this.socks5ClientAgent.doMethodSubNegotiation(method, sock);
			Socket sck = methodEncapsulation.getSocket();
			DatagramSocket datagramSock = this.datagramSocket;
			String address = datagramSock.getLocalAddress().getHostAddress();
			int port = datagramSock.getLocalPort();
			Properties properties = 
					this.socks5ClientAgent.getProperties();
			if (properties.getValue(
					Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE).booleanValue()) {
				address = HostIpv4Address.ALL_ZEROS_IPV4_ADDRESS;
				port = 0;
			}
			Request req = Request.newInstance(
					Command.UDP_ASSOCIATE, 
					Address.newInstanceFrom(address),
					Port.valueOf(port));
			this.socks5ClientAgent.sendRequest(req, sck);
			Reply rep = this.socks5ClientAgent.receiveReply(sck);
			String serverBoundAddress = 
					rep.getServerBoundAddress().toString();
			int serverBoundPort = rep.getServerBoundPort().intValue();
			if (rep.getServerBoundAddress() instanceof DomainName) {
				throw new Socks5ClientIOException(
						this.socks5ClientAgent.getSocks5Client(),
						String.format(
								"server bound address is not an IP address. "
								+ "actual server bound address is %s", 
								serverBoundAddress));
			}
			if (HostAddress.isAllZerosHostAddress(serverBoundAddress)) {
				serverBoundAddress = this.socks5ClientAgent
						.getSocksServerUri()
						.getHost()
						.toString();
			}
			InetAddress serverBoundInetAddress = InetAddress.getByName(
					serverBoundAddress);
			datagramSock = this.socks5ClientAgent.newClientDatagramSocketBuilder()
					.getConnectedClientDatagramSocket(
							datagramSock,
							serverBoundInetAddress,
							serverBoundPort);
			DatagramSocket datagramSck = methodEncapsulation.getDatagramSocket(
					datagramSock);
			this.associated = true;
			this.datagramSocket = datagramSck;
			this.udpRelayServerInetAddress = serverBoundInetAddress;
			this.udpRelayServerPort = serverBoundPort;
			this.socket = sck;
		}
		
		private void waitForCompleteAssociation() throws IOException {
			int soTimeout = this.datagramSocket.getSoTimeout();
			long waitStartTime = System.currentTimeMillis();
			while (!this.associated) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				if (soTimeout == 0) { continue; }
				long timeSinceWaitStartTime = 
						System.currentTimeMillis() - waitStartTime;
				if (timeSinceWaitStartTime >= soTimeout) {
					throw new SocketTimeoutException(
							"timeout for waiting for complete UDP association "
							+ "has been reached");
				}
			}			
		}
		
	}
	
	private final Socks5ClientAgent socks5ClientAgent;
	private final Socks5DatagramSocketImpl socks5DatagramSocketImpl;
	
	Socks5DatagramSocket(
			final Socks5ClientAgent clientAgent) throws SocketException {
		super((SocketAddress) null);
		Socks5DatagramSocketImpl impl;
		try {
			impl =	new Socks5DatagramSocketImpl(clientAgent);
			impl.datagramSocket.bind(new InetSocketAddress(
					(InetAddress) null, 0));
		} catch (SocketException e) {
			throw clientAgent.toSocksClientSocketException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5DatagramSocketImpl =	impl;
	}

	Socks5DatagramSocket(
			final Socks5ClientAgent clientAgent,
			final int port) throws SocketException {
		super((SocketAddress) null);
		Socks5DatagramSocketImpl impl;
		try {
			impl = new Socks5DatagramSocketImpl(clientAgent);
			impl.datagramSocket.bind(new InetSocketAddress(
					(InetAddress) null, port));
		} catch (SocketException e) {
			throw clientAgent.toSocksClientSocketException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5DatagramSocketImpl = impl;
	}

	Socks5DatagramSocket(
			final Socks5ClientAgent clientAgent,
			final int port, 
			final InetAddress laddr) throws SocketException {
		super((SocketAddress) null);
		Socks5DatagramSocketImpl impl;
		try {
			impl = new Socks5DatagramSocketImpl(clientAgent);
			impl.datagramSocket.bind(new InetSocketAddress(laddr, port));
		} catch (SocketException e) {
			throw clientAgent.toSocksClientSocketException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5DatagramSocketImpl = impl;		
	}

	Socks5DatagramSocket(
			final Socks5ClientAgent clientAgent,
			final SocketAddress bindaddr) throws SocketException {
		super((SocketAddress) null);
		Socks5DatagramSocketImpl impl;
		try {
			impl = new Socks5DatagramSocketImpl(clientAgent);
			if (bindaddr != null) {
				impl.datagramSocket.bind(bindaddr);
			}
		} catch (SocketException e) {
			throw clientAgent.toSocksClientSocketException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5DatagramSocketImpl = impl;
	}
	
	@Override
	public synchronized void bind(SocketAddress addr) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.bind(addr);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
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
		try {
			this.socks5DatagramSocketImpl.connect(addr);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public void disconnect() {
		this.socks5DatagramSocketImpl.disconnect();
	}

	@Override
	public synchronized boolean getBroadcast() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getBroadcast();			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
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
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getOption(name);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
    }

	@Override
	public int getPort() {
		return this.socks5DatagramSocketImpl.remotePort;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getReceiveBufferSize();
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5DatagramSocketImpl.remoteSocketAddress;
	}

	@Override
	public synchronized boolean getReuseAddress() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getReuseAddress();
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }

	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getSendBufferSize();
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }
	
	public Socks5Client getSocks5Client() {
		return this.socks5ClientAgent.getSocks5Client();
	}

	@Override
	public synchronized int getSoTimeout() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getSoTimeout();
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }

	@Override
	public synchronized int getTrafficClass() throws SocketException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.getTrafficClass();
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
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
		try {
			this.socks5DatagramSocketImpl.receive(p);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		try {
			this.socks5DatagramSocketImpl.send(p);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
	}

	@Override
	public synchronized void setBroadcast(boolean on) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setBroadcast(on);
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public <T> DatagramSocket setOption(
			SocketOption<T> name, T value) throws IOException {
		try {
			return this.socks5DatagramSocketImpl.datagramSocket.setOption(
					name, value);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
    }

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setReceiveBufferSize(size);
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public synchronized void setReuseAddress(boolean on) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setReuseAddress(on);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public synchronized void setSendBufferSize(int size) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setSendBufferSize(size);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setSoTimeout(timeout);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public synchronized void setTrafficClass(int tc) throws SocketException {
		try {
			this.socks5DatagramSocketImpl.datagramSocket.setTrafficClass(tc);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return this.socks5DatagramSocketImpl.datagramSocket.supportedOptions();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getSocks5Client()=")
			.append(this.getSocks5Client())
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", getRemoteSocketAddress()=")
			.append(this.getRemoteSocketAddress())
			.append("]");
		return builder.toString();
	}

}
