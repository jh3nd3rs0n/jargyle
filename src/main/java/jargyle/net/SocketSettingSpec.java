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
	IP_TOS {
		
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
				final Socket Socket) throws SocketException {
			UnsignedByte b = UnsignedByte.class.cast(value);
			Socket.setTrafficClass(b.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			UnsignedByte val = UnsignedByte.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, UnsignedByte.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Performance preferences for a TCP socket described by "
					+ "three digits whose values indicate the relative "
					+ "importance of short connection time, low latency, and "
					+ "high bandwidth", 
			usage = "PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2"
	)
	PERF_PREF {
		
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
				final Socket Socket) throws SocketException {
			PerformancePreferences p = PerformancePreferences.class.cast(value);
			p.applyTo(Socket);
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			PerformancePreferences val = PerformancePreferences.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, PerformancePreferences.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can send broadcast datagrams", 
			usage = "SO_BROADCAST=true|false"
	)
	SO_BROADCAST {
		
		@Override
		public void apply(
				final Object value,
				final DatagramSocket datagramSocket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			datagramSocket.setBroadcast(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, Boolean.valueOf(value));
		}
		
	},
	
	@HelpText(
			doc = "Keeps a TCP socket alive when no data has been exchanged "
					+ "in either direction", 
			usage = "SO_KEEPALIVE=true|false"
	)
	SO_KEEPALIVE {
		
		@Override
		public void apply(
				final Object value, 
				final Socket Socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			Socket.setKeepAlive(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, Boolean.valueOf(value));
		}
		
	},
	
	@HelpText(
			doc = "Linger on closing the TCP socket in seconds", 
			usage = "SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647"
	)
	SO_LINGER {
		
		@Override
		public void apply(
				final Object value, 
				final Socket Socket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			Socket.setSoLinger(true, i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			NonnegativeInteger val = NonnegativeInteger.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, NonnegativeInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can receive TCP urgent data", 
			usage = "SO_OOBINLINE=true|false"
	)
	SO_OOBINLINE {
		
		@Override
		public void apply(
				final Object value, 
				final Socket Socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			Socket.setOOBInline(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, Boolean.valueOf(value));
		}

	},
	
	@HelpText(
			doc = "The receive buffer size", 
			usage = "SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SO_RCVBUF {
		
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
				final Socket Socket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			Socket.setReceiveBufferSize(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, PositiveInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Can reuse socket address and port", 
			usage = "SO_REUSEADDR=true|false"
	)
	SO_REUSEADDR {
		
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
				final Socket Socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			Socket.setReuseAddress(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, Boolean.valueOf(value));
		}

	},
	
	@HelpText(
			doc = "The send buffer size", 
			usage = "SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SO_SNDBUF {
		
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
				final Socket Socket) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			Socket.setSendBufferSize(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, PositiveInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for an idle socket", 
			usage = "SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647"
	)
	SO_TIMEOUT {
		
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
				final Socket Socket) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			Socket.setSoTimeout(i.intValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			NonnegativeInteger val = NonnegativeInteger.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, NonnegativeInteger.newInstance(value));
		}
		
	},
	
	@HelpText(
			doc = "Disables Nagle's algorithm", 
			usage = "TCP_NODELAY=true|false"
	)
	TCP_NODELAY {
		
		@Override
		public void apply(
				final Object value, 
				final Socket Socket) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			Socket.setTcpNoDelay(b.booleanValue());
		}

		@Override
		public SocketSetting newSocketSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new SocketSetting(this, val);
		}

		@Override
		public SocketSetting newSocketSetting(final String value) {
			return new SocketSetting(this, Boolean.valueOf(value));
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
			final Socket Socket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, Socket.class.getName()));
	}
	
	public abstract SocketSetting newSocketSetting(final Object value);
	
	public abstract SocketSetting newSocketSetting(final String value);
	
}
