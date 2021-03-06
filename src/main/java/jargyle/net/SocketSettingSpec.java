package jargyle.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import jargyle.annotation.HelpText;
import jargyle.util.NonnegativeInteger;
import jargyle.util.PositiveInteger;
import jargyle.util.UnsignedByte;

public enum SocketSettingSpec {

	@HelpText(
			doc = "The type-of-service or traffic class field in the IP "
					+ "header for a TCP or UDP socket", 
			usage = "IP_TOS=INTEGER_BETWEEN_0_AND_255"
	)
	IP_TOS(UnsignedByte.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			UnsignedByte b = UnsignedByte.class.cast(value);
			datagramSocket.setTrafficClass(b.intValue());
		}
		
		@Override
		public void apply(
				final Object value,
				final Socket socket) throws SocketException {
			UnsignedByte b = UnsignedByte.class.cast(value);
			socket.setTrafficClass(b.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(UnsignedByte.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Performance preferences for a TCP socket described by "
					+ "three digits whose values indicate the relative "
					+ "importance of short connection time, low latency, and "
					+ "high bandwidth", 
			usage = "PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2"
	)
	PERF_PREF(PerformancePreferences.class) {
		
		@Override
		public void apply(
				final Object value,
				final ServerSocket serverSocket) throws SocketException {
			PerformancePreferences p = PerformancePreferences.class.cast(value);
			p.applyTo(serverSocket);
		}

		@Override
		public void apply(
				final Object value,
				final Socket socket) throws SocketException {
			PerformancePreferences p = PerformancePreferences.class.cast(value);
			p.applyTo(socket);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(PerformancePreferences.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can send broadcast datagrams", 
			usage = "SO_BROADCAST=true|false"
	)
	SO_BROADCAST(Boolean.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			datagramSocket.setBroadcast(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	},
	
	@HelpText(
			doc = "Keeps a TCP socket alive when no data has been exchanged "
					+ "in either direction", 
			usage = "SO_KEEPALIVE=true|false"
	)
	SO_KEEPALIVE(Boolean.class) {
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socket.setKeepAlive(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	},
	
	@HelpText(
			doc = "Linger on closing the TCP socket in seconds", 
			usage = "SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647"
	)
	SO_LINGER(NonnegativeInteger.class) {
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			socket.setSoLinger(true, i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can receive TCP urgent data", 
			usage = "SO_OOBINLINE=true|false"
	)
	SO_OOBINLINE(Boolean.class) {
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socket.setOOBInline(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}

	},
	
	@HelpText(
			doc = "The receive buffer size", 
			usage = "SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SO_RCVBUF(PositiveInteger.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			datagramSocket.setReceiveBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocket serverSocket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			serverSocket.setReceiveBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			socket.setReceiveBufferSize(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can reuse socket address and port", 
			usage = "SO_REUSEADDR=true|false"
	)
	SO_REUSEADDR(Boolean.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			datagramSocket.setReuseAddress(b.booleanValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocket serverSocket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			serverSocket.setReuseAddress(b.booleanValue());
		}

		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socket.setReuseAddress(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}

	},
	
	@HelpText(
			doc = "The send buffer size", 
			usage = "SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SO_SNDBUF(PositiveInteger.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			datagramSocket.setSendBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			socket.setSendBufferSize(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for an idle socket", 
			usage = "SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647"
	)
	SO_TIMEOUT(NonnegativeInteger.class) {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			datagramSocket.setSoTimeout(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocket serverSocket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			serverSocket.setSoTimeout(i.intValue());
		}

		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			socket.setSoTimeout(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Disables Nagle's algorithm", 
			usage = "TCP_NODELAY=true|false"
	)
	TCP_NODELAY(Boolean.class) {
		
		@Override
		public void apply(
				final Object value, 
				final Socket socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socket.setTcpNoDelay(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	};
	
	public static SocketSettingSpec getInstance(final String s) {
		SocketSettingSpec socketSettingSpec = null;
		try {
			socketSettingSpec = SocketSettingSpec.valueOf(s);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"unknown %s: %s", 
					SocketSettingSpec.class.getSimpleName(), s), e);
		}
		return socketSettingSpec;
	}
	
	private final Class<?> valueType;
	
	private SocketSettingSpec(final Class<?> valType) {
		this.valueType = valType;
	}
	
	public void apply(
			final Object value,
			final DatagramSocket datagramSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, DatagramSocket.class.getName()));
	}
	
	public void apply(
			final Object value, 
			final ServerSocket serverSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, ServerSocket.class.getName()));
	}
	
	public void apply(
			final Object value, 
			final Socket socket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, Socket.class.getName()));
	}
	
	public final SocketSetting newSocketSetting(final Object value) {
		return new SocketSetting(this, this.valueType.cast(value));
	}
	
	public abstract SocketSetting newSocketSetting(final String value);
	
}
