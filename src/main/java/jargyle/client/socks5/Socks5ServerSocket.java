package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;

import jargyle.common.net.FilterSocket;
import jargyle.common.net.PerformancePreferences;
import jargyle.common.net.SocketSettingSpec;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;

public final class Socks5ServerSocket extends ServerSocket {

	private static final class AcceptedSocks5Socket extends FilterSocket {
		
		private InetAddress localInetAddress;
		private int localPort;
		private SocketAddress localSocketAddress;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
				
		public AcceptedSocks5Socket(
				final Socks5Socket socks5Socket, 
				final InetAddress address, 
				final int port, 
				final InetAddress localAddr, 
				final int localPort) {
			super(socks5Socket);
			this.localInetAddress = localAddr;
			this.localPort = localPort;
			this.localSocketAddress = new InetSocketAddress(
					localAddr, localPort);
			this.remoteInetAddress = address;
			this.remotePort = port;
			this.remoteSocketAddress = new InetSocketAddress(address, port);
		}

		@Override
		public void bind(SocketAddress bindpoint) throws IOException {
			super.bind(bindpoint);
			this.localInetAddress = super.getLocalAddress();
			this.localPort = super.getLocalPort();
			this.localSocketAddress = super.getLocalSocketAddress();
		}
		
		@Override
		public synchronized void close() throws IOException {
			super.close();
			InetAddress wildcardAddress = null;
			try {
				wildcardAddress = InetAddress.getByName(
						AddressType.IP_V4_ADDRESS.getWildcardAddress());
			} catch (UnknownHostException e) {
				throw new AssertionError(e.toString(), e);
			}
			this.localInetAddress = wildcardAddress;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
		}
	
		@Override
		public void connect(SocketAddress endpoint) throws IOException {
			super.connect(endpoint);
			this.remoteInetAddress = super.getInetAddress();
			this.remotePort = super.getPort();
			this.remoteSocketAddress = super.getRemoteSocketAddress();
		}
		
		@Override
		public void connect(
				SocketAddress endpoint,	int timeout) throws IOException {
			super.connect(endpoint, timeout);
			this.remoteInetAddress = super.getInetAddress();
			this.remotePort = super.getPort();
			this.remoteSocketAddress = super.getRemoteSocketAddress();
		}

		@Override
		public InetAddress getInetAddress() {
			return this.remoteInetAddress;
		}

		@Override
		public InetAddress getLocalAddress() {
			return this.localInetAddress;
		}

		@Override
		public int getLocalPort() {
			return this.localPort;
		}

		@Override
		public SocketAddress getLocalSocketAddress() {
			return this.localSocketAddress;
		}

		@Override
		public int getPort() {
			return this.remotePort;
		}

		@Override
		public SocketAddress getRemoteSocketAddress() {
			return this.remoteSocketAddress;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [getLocalSocketAddress()=")
				.append(this.getLocalSocketAddress())
				.append(", getRemoteSocketAddress()=")
				.append(this.getRemoteSocketAddress())
				.append("]");
			return builder.toString();
		}

	}
	
	public static final int DEFAULT_SO_TIMEOUT = 0;
	
	private static void bind(
			final Socks5ServerSocket socks5ServerSocket,
			final int port,
			final InetAddress bindAddr) throws IOException {
		socks5ServerSocket.socks5Client.bind(socks5ServerSocket.socket);
		socks5ServerSocket.socks5Client.connectToSocksServerWith(
				socks5ServerSocket.socket);
		Socket sock = socks5ServerSocket.socks5Client.authenticate(
				socks5ServerSocket.socket);
		InputStream inStream = sock.getInputStream();
		OutputStream outStream = sock.getOutputStream();
		InetAddress bAddr = bindAddr;
		if (bAddr == null) {
			bAddr = InetAddress.getByName(
					AddressType.IP_V4_ADDRESS.getWildcardAddress());
		}
		String address = bAddr.getHostAddress();
		AddressType addressType = AddressType.get(address);
		Socks5Request socks5Req = Socks5Request.newInstance(
				Command.BIND, 
				addressType, 
				address, 
				port);
		outStream.write(socks5Req.toByteArray());
		outStream.flush();
		Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inStream);
		Reply reply = socks5Rep.getReply();
		if (!reply.equals(Reply.SUCCEEDED)) {
			throw new IOException(String.format("received reply: %s", reply));
		}
		socks5ServerSocket.bind = false;		
		socks5ServerSocket.bound = true;
		socks5ServerSocket.localInetAddress = InetAddress.getByName(
				socks5Rep.getServerBoundAddress());
		socks5ServerSocket.localPort = socks5Rep.getServerBoundPort();
		socks5ServerSocket.localSocketAddress = new InetSocketAddress(
				socks5ServerSocket.localInetAddress,
				socks5ServerSocket.localPort);
		socks5ServerSocket.socket =	sock;
	}

	private boolean bind;
	private boolean bound;
	private boolean closed;
	private InetAddress localInetAddress;
	private int localPort;
	private SocketAddress localSocketAddress;
	private Socket originalSocket;
	private Socket socket;
	private SocketSettings socketSettings;
	private final Socks5Client socks5Client;
	
	public Socks5ServerSocket(final Socks5Client client) throws IOException {
		Socket sock = new Socket();
		this.bind = true;
		this.bound = false;
		this.closed = false;
		this.localInetAddress = null;
		this.localPort = -1;
		this.localSocketAddress = null;
		this.originalSocket = sock;
		this.socket = sock;
		this.socketSettings = SocketSettings.newInstance();
		this.socks5Client = client;
	}

	public Socks5ServerSocket(
			final Socks5Client client, final int port) throws IOException {
		this(client);
		bind(this, port, null);
	}

	public Socks5ServerSocket(
			final Socks5Client client, 
			final int port, 
			final int backlog) throws IOException {
		this(client, port);
	}

	public Socks5ServerSocket(
			final Socks5Client client, 
			final int port, 
			final int backlog,
			final InetAddress bindAddr) throws IOException {
		this(client);
		bind(this, port, bindAddr);
	}

	@Override
	public Socket accept() throws IOException {
		if (this.closed) {
			throw new SocketException("socket is closed");
		}
		if (this.bind) {
			bind(this, this.localPort, this.localInetAddress);
		}
		Socket acceptedSocks5Socket = null;
		try {
			InputStream inputStream = this.socket.getInputStream();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format(
						"received reply: %s", reply));
			}
			Socket newSocket = new Socket();
			this.socketSettings.applyTo(newSocket);
			acceptedSocks5Socket = new AcceptedSocks5Socket(
					new Socks5Socket(
							this.socks5Client, 
							this.originalSocket, 
							this.socket),
					InetAddress.getByName(socks5Rep.getServerBoundAddress()),
					socks5Rep.getServerBoundPort(),
					this.localInetAddress,
					this.localPort);
			this.originalSocket = newSocket;
			this.socket = newSocket;
		} finally {
			this.bind = true;
		}
		return acceptedSocks5Socket;
	}

	@Override
	public void bind(final SocketAddress endpoint) throws IOException {
		this.bind(endpoint, 50);
	}

	@Override
	public void bind(
			final SocketAddress endpoint, 
			final int backlog) throws IOException {
		if (this.bound) {
			throw new IOException("socket is already bound");
		}
		SocketAddress end = endpoint;
		if (end == null) {
			end = new InetSocketAddress((InetAddress) null, 0);
		} else {
			if (!(end instanceof InetSocketAddress)) {
				throw new IllegalArgumentException(
						"endpoint must be an instance of InetSocketAddress");
			}
		}
		InetSocketAddress inetSocketAddress = (InetSocketAddress) end;
		bind(this, inetSocketAddress.getPort(), inetSocketAddress.getAddress());
	}

	@Override
	public void close() throws IOException {
		this.socket.close();
		this.bind = true;
		this.bound = false;
		this.closed = true;
		this.localInetAddress = null;
		this.localPort = -1;
		this.localSocketAddress = null;
	}

	@Override
	public ServerSocketChannel getChannel() {
		return null;
	}

	@Override
	public InetAddress getInetAddress() {
		return this.localInetAddress;
	}

	@Override
	public int getLocalPort() {
		return this.localPort;
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.localSocketAddress;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.socket.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socket.getReuseAddress();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public synchronized int getSoTimeout() throws IOException {
		return this.socket.getSoTimeout();
	}

	@Override
	public boolean isBound() {
		return this.bound;
	}

	@Override
	public boolean isClosed() {
		return this.closed;
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		if (!this.bound) {
			PerformancePreferences pp = PerformancePreferences.newInstance(
					connectionTime, latency, bandwidth);
			this.socket.setPerformancePreferences(
					connectionTime, latency, bandwidth);
			this.socketSettings.putValue(SocketSettingSpec.PERF_PREF, pp);
		}
	}

	@Override
	public synchronized void setReceiveBufferSize(
			int size) throws SocketException {
		PositiveInteger i = PositiveInteger.newInstance(size);
		this.socket.setReceiveBufferSize(size);
		this.socketSettings.putValue(SocketSettingSpec.SO_RCVBUF, i);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		Boolean b = Boolean.valueOf(on);
		this.socket.setReuseAddress(on);
		this.socketSettings.putValue(SocketSettingSpec.SO_REUSEADDR, b);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		NonnegativeInteger i = NonnegativeInteger.newInstance(timeout);
		this.socket.setSoTimeout(timeout);
		this.socketSettings.putValue(SocketSettingSpec.SO_TIMEOUT, i);
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
