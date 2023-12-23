package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Set;

import com.github.jh3nd3rs0n.jargyle.client.SocksClient.ClientSocketConnectParams;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientIOExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientSocketExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.AddressType;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Request;

public final class Socks5Socket extends Socket {

	private static final class Socks5SocketImpl {
		
		private boolean connected;
		private InputStream inputStream;
		private OutputStream outputStream;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private Socket socket;
		private final Socks5Client socks5Client;
		
		public Socks5SocketImpl(final Socks5Client client, final Socket sock) {
			this.connected = sock.isConnected();
			this.inputStream = null;
			this.outputStream = null;
			this.remoteInetAddress = sock.getInetAddress();
			this.remotePort = sock.getPort();
			this.remoteSocketAddress = sock.getRemoteSocketAddress();
			this.socket = sock;
			this.socks5Client = client;
		}
		
		public void close() throws IOException {
			this.connected = false;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
			this.socket.close();
		}
		
		public void connect(SocketAddress endpoint) throws IOException {
			this.connect(endpoint, 0);
		}
		
		public void connect(
				SocketAddress endpoint,	int timeout) throws IOException {
			if (this.connected) {
				throw new SocketException("socket is already connected");
			}
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

		public InputStream getInputStream() throws IOException {
			if (this.inputStream == null) {
				this.inputStream = new Socks5SocketInputStream(
						this.socks5Client, this.socket.getInputStream());
			}
			return this.inputStream;
		}
		
		public OutputStream getOutputStream() throws IOException {
			if (this.outputStream == null) {
				this.outputStream = new Socks5SocketOutputStream(
						this.socks5Client, this.socket.getOutputStream());
			}
			return this.outputStream;
		}
		
		public void socks5Connect(
				final InetAddress inetAddress,
				final int port,
				final int timeout) throws IOException {
			ClientSocketConnectParams params = new ClientSocketConnectParams();
			params.setMustBindBeforeConnect(false);
			params.setConnectTimeout(Integer.valueOf(timeout));
			Socket sock = this.socks5Client.getConnectedClientSocket(
					this.socket, params);
			String address = inetAddress.getHostAddress();
			this.socks5Connect(sock, address, port);
		}
		
		public void socks5Connect(
				final Socket connectedSocket,
				final String address,
				final int port) throws IOException {
			Method method = this.socks5Client.negotiateMethod(connectedSocket); 
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.doMethodSubnegotiation(
							method, connectedSocket);
			Socket connectedSock = methodEncapsulation.getSocket();
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.CONNECT, 
					Address.newInstance(address), 
					Port.newInstance(port));
			this.socks5Client.sendSocks5Request(socks5Req, connectedSock);
			Socks5Reply socks5Rep = null;
			try {
				socks5Rep = this.socks5Client.receiveSocks5Reply(connectedSock);
			} catch (FailureSocks5ReplyException e) {
				Reply reply = e.getFailureSocks5Reply().getReply();
				if (reply.equals(Reply.HOST_UNREACHABLE)) {
					throw new UnknownHostException(address);
				} else {
					throw e;
				}
			}
			String serverBoundAddress = 
					socks5Rep.getServerBoundAddress().toString();
			int serverBoundPort = socks5Rep.getServerBoundPort().intValue();
			AddressType addressType =
					socks5Rep.getServerBoundAddress().getAddressType();
			if (addressType.equals(AddressType.DOMAINNAME)) {
				throw new Socks5ClientIOException(
						this.socks5Client, 
						String.format(
								"server bound address is not an IP address. "
								+ "actual server bound address is %s", 
								serverBoundAddress));
			}
			this.connected = true;
			this.remoteInetAddress = InetAddress.getByName(serverBoundAddress);
			this.remotePort = serverBoundPort;
			this.remoteSocketAddress = new InetSocketAddress(
					this.remoteInetAddress,
					this.remotePort);
			this.socket = connectedSock;
			this.inputStream = new Socks5SocketInputStream(
					this.socks5Client, this.socket.getInputStream());
			this.outputStream = new Socks5SocketOutputStream(
					this.socks5Client, this.socket.getOutputStream());
		}
		
	}
	
	private static final class Socks5SocketInputStream extends FilterInputStream {
		
		private final Socks5Client socks5Client;
		
		public Socks5SocketInputStream(
				final Socks5Client client, final InputStream inStream) {
			super(inStream);
			this.socks5Client = client;			
		}

		@Override
		public int available() throws IOException {
			int available = 0;
			try {
				available = this.in.available();
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
			return available;
		}
		
		@Override
		public void close() throws IOException {
			try {
				this.in.close();
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}		
		}
		
		@Override
		public int read() throws IOException {
			int b = -1;
			try {
				b = this.in.read();
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
			return b;
		}
		
		@Override
		public int read(byte[] b) throws IOException {
			int bytesRead = -1;
			try {
				bytesRead = this.in.read(b);
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
			return bytesRead;
		}
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int bytesRead = -1;
			try {
				bytesRead = this.in.read(b, off, len);
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
			return bytesRead;
		}
	}
	
	private static final class Socks5SocketOutputStream extends FilterOutputStream {
		
		private final Socks5Client socks5Client;
		
		public Socks5SocketOutputStream(
				final Socks5Client client, final OutputStream outStream) {
			super(outStream);
			this.socks5Client = client;			
		}

		@Override
		public synchronized void close() throws IOException {
			try {
				this.out.close();
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
		}
		
		@Override
		public void flush() throws IOException {
			try {
				this.out.flush();
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			try {
				this.out.write(b);
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			try {
				this.out.write(b, off, len);
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}			
		}
		
		@Override
		public void write(final int b) throws IOException {
			try {
				this.out.write(b);
			} catch (IOException e) {
				SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
						e, this.socks5Client);
			}
		}
	}
	
	private final Socks5Client socks5Client;
	private final Socks5SocketImpl socks5SocketImpl;
	
	Socks5Socket(final Socks5Client client) {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, client.newClientSocket()); 
	}
	
	Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port) throws IOException {
		Socks5SocketImpl impl = new Socks5SocketImpl(
				client, client.newClientSocket());
		try {
			impl.socks5Connect(address, port, 0);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, client);
		}
		this.socks5Client = client;
		this.socks5SocketImpl = impl;
	}
	
	Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		Socks5SocketImpl impl = new Socks5SocketImpl(
				client, client.newClientSocket());
		try {
			impl.socket.bind(new InetSocketAddress(localAddr, localPort));
			impl.socks5Connect(address, port, 0);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, client);
		}
		this.socks5Client = client;
		this.socks5SocketImpl = impl;
	}

	Socks5Socket(final Socks5Client client, final Socket sock) {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(client, sock);
	}

	Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port) throws UnknownHostException, IOException {
		Socks5SocketImpl impl = null;
		try {
			Socket connectedInternalSocket = client.newConnectedClientSocket();
			impl = new Socks5SocketImpl(client, connectedInternalSocket);
			impl.socks5Connect(connectedInternalSocket, host, port);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, client);
		}
		this.socks5Client = client;
		this.socks5SocketImpl = impl;
	}

	Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		Socks5SocketImpl impl = null;
		try {
			Socket connectedInternalSocket = client.newConnectedClientSocket(
					localAddr, localPort);
			impl = new Socks5SocketImpl(client, connectedInternalSocket);
			impl.socks5Connect(connectedInternalSocket, host, port);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, client);
		}
		this.socks5Client = client;
		this.socks5SocketImpl = impl;
	}
	
	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		try {
			this.socks5SocketImpl.socket.bind(bindpoint);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
	}

	@Override
	public synchronized void close() throws IOException {
		try {
			this.socks5SocketImpl.close();
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);			
		}
	}

	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		try {
			this.socks5SocketImpl.connect(endpoint);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
	}

	@Override
	public void connect(SocketAddress endpoint, int timeout) throws IOException {
		try {
			this.socks5SocketImpl.connect(endpoint, timeout);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);			
		}
	}

	@Override
	public SocketChannel getChannel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5SocketImpl.remoteInetAddress;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		try {
			return this.socks5SocketImpl.getInputStream();
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
		return null;
	}

	@Override
	public boolean getKeepAlive() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getKeepAlive();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return false;
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5SocketImpl.socket.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5SocketImpl.socket.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5SocketImpl.socket.getLocalSocketAddress();
	}

	@Override
	public boolean getOOBInline() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getOOBInline();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return false;
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		try {
			return this.socks5SocketImpl.socket.getOption(name);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
		return null;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		try {
			return this.socks5SocketImpl.getOutputStream();
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
		return null;
	}

	@Override
	public int getPort() {
		return this.socks5SocketImpl.remotePort;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getReceiveBufferSize();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return 0;
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5SocketImpl.remoteSocketAddress;
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getReuseAddress();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return false;
	}
	
	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getSendBufferSize();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return 0;
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}

	@Override
	public int getSoLinger() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getSoLinger();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return 0;
	}

	@Override
	public synchronized int getSoTimeout() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getSoTimeout();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return 0;
	}

	@Override
	public boolean getTcpNoDelay() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getTcpNoDelay();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return false;
	}

	@Override
	public int getTrafficClass() throws SocketException {
		try {
			return this.socks5SocketImpl.socket.getTrafficClass();
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
		return 0;
	}

	@Override
	public boolean isBound() {
		return this.socks5SocketImpl.socket.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5SocketImpl.socket.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5SocketImpl.connected;
	}

	@Override
	public boolean isInputShutdown() {
		return this.socks5SocketImpl.socket.isInputShutdown();
	}

	@Override
	public boolean isOutputShutdown() {
		return this.socks5SocketImpl.socket.isOutputShutdown();
	}

	@Override
	public void sendUrgentData(int data) throws IOException {
		try {
			this.socks5SocketImpl.socket.sendUrgentData(data);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setKeepAlive(boolean on) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setKeepAlive(on);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setOOBInline(boolean on) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setOOBInline(on);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public <T> Socket setOption(
			SocketOption<T> name, T value) throws IOException {
		try {
			return this.socks5SocketImpl.socket.setOption(name, value);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
		return null;
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socks5SocketImpl.socket.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setReceiveBufferSize(size);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setReuseAddress(on);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public synchronized void setSendBufferSize(int size) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setSendBufferSize(size);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setSoLinger(on, linger);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setSoTimeout(timeout);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setTcpNoDelay(boolean on) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setTcpNoDelay(on);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		try {
			this.socks5SocketImpl.socket.setTrafficClass(tc);			
		} catch (SocketException e) {
			SocksClientSocketExceptionThrowingHelper.throwAsSocksClientSocketException(
					e, this.socks5Client);
		}
	}

	@Override
	public void shutdownInput() throws IOException {
		try {
			this.socks5SocketImpl.socket.shutdownInput();
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
	}

	@Override
	public void shutdownOutput() throws IOException {
		try {
			this.socks5SocketImpl.socket.shutdownOutput();			
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return this.socks5SocketImpl.socket.supportedOptions();
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
