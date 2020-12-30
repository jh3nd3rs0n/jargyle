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
import jargyle.common.net.FilterSocketInterface;
import jargyle.common.net.PerformancePreferences;
import jargyle.common.net.ServerSocketInterface;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketSettingSpec;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;

public final class Socks5ServerSocketInterface extends ServerSocketInterface {

	private static final class AcceptedSocks5SocketInterface 
		extends FilterSocketInterface {
		
		private InetAddress localInetAddress;
		private int localPort;
		private SocketAddress localSocketAddress;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private final Socks5Client socks5Client;
		
		public AcceptedSocks5SocketInterface(
				final Socks5SocketInterface socks5SocketInterface, 
				final InetAddress address, 
				final int port, 
				final InetAddress localAddr, 
				final int localPort) {
			super(socks5SocketInterface);
			this.localInetAddress = localAddr;
			this.localPort = localPort;
			this.localSocketAddress = new InetSocketAddress(
					localAddr, localPort);
			this.remoteInetAddress = address;
			this.remotePort = port;
			this.remoteSocketAddress = new InetSocketAddress(address, port);
			this.socks5Client = socks5SocketInterface.getSocks5Client();
		}
		

		@Override
		public void bind(SocketAddress bindpoint) throws IOException {
			super.bind(bindpoint);
			this.localInetAddress = super.getLocalAddress();
			this.localPort = super.getLocalPort();
			this.localSocketAddress = super.getLocalSocketAddress();
		}
		
		@Override
		public void close() throws IOException {
			super.close();
			InetAddress wildcardAddress = null;
			try {
				wildcardAddress = InetAddress.getByName(
						AddressType.IP_V4_ADDRESS.getWildcardAddress());
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
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

	private static final class Socks5ServerSocketInterfaceImpl {
		
		private static final int DEFAULT_BACKLOG = 50;
		
		private boolean bound;
		private boolean closed;
		private InetAddress localInetAddress;
		private int localPort;
		private SocketAddress localSocketAddress;
		private SocketInterface originalSocketInterface;
		private SocketInterface socketInterface;
		private SocketSettings socketSettings;
		private boolean socks5Bound;
		private final Socks5Client socks5Client;
		
		public Socks5ServerSocketInterfaceImpl(
				final Socks5Client client) throws IOException {
			SocketInterface sockInterface = 
					new DirectSocketInterface(new Socket());
			this.bound = false;
			this.closed = false;
			this.localInetAddress = null;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.originalSocketInterface = sockInterface;
			this.socketInterface = sockInterface;
			this.socketSettings = SocketSettings.newInstance();
			this.socks5Client = client;
			this.socks5Bound = false;
		}
		
		public SocketInterface accept() throws IOException {
			if (this.closed) {
				throw new SocketException("socket is closed");
			}
			if (!this.socks5Bound) {
				this.socks5Bind(this.localPort, this.localInetAddress);
			}
			SocketInterface acceptedSocks5SocketInterface = null;
			try {
				InputStream inputStream = this.socketInterface.getInputStream();
				Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
				Reply reply = socks5Rep.getReply();
				if (!reply.equals(Reply.SUCCEEDED)) {
					throw new IOException(String.format(
							"received reply: %s", reply));
				}
				acceptedSocks5SocketInterface = new AcceptedSocks5SocketInterface(
						new Socks5SocketInterface(
								this.socks5Client, 
								this.originalSocketInterface, 
								this.socketInterface),
						InetAddress.getByName(socks5Rep.getServerBoundAddress()),
						socks5Rep.getServerBoundPort(),
						this.localInetAddress,
						this.localPort);
				SocketInterface newSocketInterface = new DirectSocketInterface(
						new Socket());
				this.socketSettings.applyTo(newSocketInterface);
				this.originalSocketInterface = newSocketInterface;
				this.socketInterface = newSocketInterface;
			} finally {
				this.socks5Bound = false;
			}
			return acceptedSocks5SocketInterface;
		}

		public void bind(final SocketAddress endpoint) throws IOException {
			this.bind(endpoint, DEFAULT_BACKLOG);
		}

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
			this.socks5Bind(inetSocketAddress.getPort(), inetSocketAddress.getAddress());
		}
		
		public void close() throws IOException {
			this.bound = false;
			this.closed = true;
			this.localInetAddress = null;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.socks5Bound = false;
			this.socketInterface.close();
		}
		
		public int getReceiveBufferSize() throws SocketException {
			return this.socketInterface.getReceiveBufferSize();
		}

		public boolean getReuseAddress() throws SocketException {
			return this.socketInterface.getReuseAddress();
		}

		public int getSoTimeout() throws IOException {
			return this.socketInterface.getSoTimeout();
		}
		
		public void setPerformancePreferences(
				int connectionTime, int latency, int bandwidth) {
			if (!this.bound) {
				PerformancePreferences pp = PerformancePreferences.newInstance(
						connectionTime, latency, bandwidth);
				this.socketInterface.setPerformancePreferences(
						connectionTime, latency, bandwidth);
				this.socketSettings.putValue(SocketSettingSpec.PERF_PREF, pp);
			}
		}
		
		public void setReceiveBufferSize(int size) throws SocketException {
			PositiveInteger i = PositiveInteger.newInstance(size);
			this.socketInterface.setReceiveBufferSize(size);
			this.socketSettings.putValue(SocketSettingSpec.SO_RCVBUF, i);
		}
		
		public void setReuseAddress(boolean on) throws SocketException {
			Boolean b = Boolean.valueOf(on);
			this.socketInterface.setReuseAddress(on);
			this.socketSettings.putValue(SocketSettingSpec.SO_REUSEADDR, b);
		}

		public void setSoTimeout(int timeout) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.newInstance(timeout);
			this.socketInterface.setSoTimeout(timeout);
			this.socketSettings.putValue(SocketSettingSpec.SO_TIMEOUT, i);
		}

		public void socks5Bind(
				final int port, final InetAddress bindAddr) throws IOException {
			this.socketInterface = this.originalSocketInterface;
			SocketInterface sockInterface = this.socks5Client.connectToSocksServerWith(
					this.socketInterface, true);
			InputStream inStream = sockInterface.getInputStream();
			OutputStream outStream = sockInterface.getOutputStream();
			int prt = port;
			if (prt == -1) {
				prt = 0;
			}
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
					prt);
			outStream.write(socks5Req.toByteArray());
			outStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format("received reply: %s", reply));
			}
			this.bound = true;
			this.localInetAddress = InetAddress.getByName(
					socks5Rep.getServerBoundAddress());
			this.localPort = socks5Rep.getServerBoundPort();
			this.localSocketAddress = new InetSocketAddress(
					this.localInetAddress,
					this.localPort);
			this.socketInterface = sockInterface;
			this.socks5Bound = true;
		}

	}
	
	private final Socks5Client socks5Client;
	private final Socks5ServerSocketInterfaceImpl socks5ServerSocketInterfaceImpl;
	
	public Socks5ServerSocketInterface(final Socks5Client client) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketInterfaceImpl = new Socks5ServerSocketInterfaceImpl(
				client);
	}

	public Socks5ServerSocketInterface(
			final Socks5Client client, final int port) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketInterfaceImpl = new Socks5ServerSocketInterfaceImpl(
				client);
		this.socks5ServerSocketInterfaceImpl.socks5Bind(port, null);
	}

	public Socks5ServerSocketInterface(
			final Socks5Client client, 
			final int port, 
			final int backlog) throws IOException {
		this(client, port);
	}

	public Socks5ServerSocketInterface(
			final Socks5Client client, 
			final int port, 
			final int backlog,
			final InetAddress bindAddr) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketInterfaceImpl = new Socks5ServerSocketInterfaceImpl(
				client);
		this.socks5ServerSocketInterfaceImpl.socks5Bind(port, bindAddr);
	}
	
	@Override
	public SocketInterface accept() throws IOException {
		return this.socks5ServerSocketInterfaceImpl.accept();
	}

	@Override
	public void bind(SocketAddress endpoint) throws IOException {
		this.socks5ServerSocketInterfaceImpl.bind(endpoint);
	}

	@Override
	public void bind(SocketAddress endpoint, int backlog) throws IOException {
		this.socks5ServerSocketInterfaceImpl.bind(endpoint, backlog);
	}

	@Override
	public void close() throws IOException {
		this.socks5ServerSocketInterfaceImpl.close();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5ServerSocketInterfaceImpl.localInetAddress;
	}

	@Override
	public int getLocalPort() {
		return this.socks5ServerSocketInterfaceImpl.localPort;
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5ServerSocketInterfaceImpl.localSocketAddress;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.socks5ServerSocketInterfaceImpl.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5ServerSocketInterfaceImpl.getReuseAddress();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public synchronized int getSoTimeout() throws IOException {
		return this.socks5ServerSocketInterfaceImpl.getSoTimeout();
	}

	@Override
	public boolean isBound() {
		return this.socks5ServerSocketInterfaceImpl.bound;
	}

	@Override
	public boolean isClosed() {
		return this.socks5ServerSocketInterfaceImpl.closed;
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socks5ServerSocketInterfaceImpl.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		this.socks5ServerSocketInterfaceImpl.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5ServerSocketInterfaceImpl.setReuseAddress(on);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.socks5ServerSocketInterfaceImpl.setSoTimeout(timeout);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5Client=")
			.append(this.socks5Client)
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append("]");
		return builder.toString();
	}

}
