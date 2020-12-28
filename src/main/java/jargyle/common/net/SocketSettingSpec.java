package jargyle.common.net;

import java.net.SocketException;

import jargyle.common.annotation.HelpText;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;
import jargyle.common.util.UnsignedByte;

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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			UnsignedByte b = UnsignedByte.class.cast(value);
			datagramSocketInterface.setTrafficClass(b.intValue());
		}
		
		@Override
		public void apply(
				final Object value,
				final SocketInterface socketInterface) throws SocketException {
			UnsignedByte b = UnsignedByte.class.cast(value);
			socketInterface.setTrafficClass(b.intValue());
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
				final ServerSocketInterface serverSocketInterface) throws SocketException {
			PerformancePreferences p = PerformancePreferences.class.cast(value);
			p.applyTo(serverSocketInterface);
		}

		@Override
		public void apply(
				final Object value,
				final SocketInterface socketInterface) throws SocketException {
			PerformancePreferences p = PerformancePreferences.class.cast(value);
			p.applyTo(socketInterface);
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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			datagramSocketInterface.setBroadcast(b.booleanValue());
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
				final SocketInterface socketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socketInterface.setKeepAlive(b.booleanValue());
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
				final SocketInterface socketInterface) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			socketInterface.setSoLinger(true, i.intValue());
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
				final SocketInterface socketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socketInterface.setOOBInline(b.booleanValue());
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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			datagramSocketInterface.setReceiveBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocketInterface serverSocketInterface) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			serverSocketInterface.setReceiveBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final SocketInterface socketInterface) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			socketInterface.setReceiveBufferSize(i.intValue());
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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			datagramSocketInterface.setReuseAddress(b.booleanValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocketInterface serverSocketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			serverSocketInterface.setReuseAddress(b.booleanValue());
		}

		@Override
		public void apply(
				final Object value, 
				final SocketInterface socketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socketInterface.setReuseAddress(b.booleanValue());
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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			datagramSocketInterface.setSendBufferSize(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final SocketInterface socketInterface) throws SocketException {
			PositiveInteger i = PositiveInteger.class.cast(value);
			socketInterface.setSendBufferSize(i.intValue());
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
				final DatagramSocketInterface datagramSocketInterface) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			datagramSocketInterface.setSoTimeout(i.intValue());
		}
		
		@Override
		public void apply(
				final Object value, 
				final ServerSocketInterface serverSocketInterface) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			serverSocketInterface.setSoTimeout(i.intValue());
		}

		@Override
		public void apply(
				final Object value, 
				final SocketInterface socketInterface) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.class.cast(value);
			socketInterface.setSoTimeout(i.intValue());
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
				final SocketInterface socketInterface) throws SocketException {
			Boolean b = Boolean.class.cast(value);
			socketInterface.setTcpNoDelay(b.booleanValue());
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
			final DatagramSocketInterface datagramSocketInterface) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, DatagramSocketInterface.class.getName()));
	}
	
	public void apply(
			final Object value, 
			final ServerSocketInterface serverSocketInterface) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, ServerSocketInterface.class.getName()));
	}
	
	public void apply(
			final Object value, 
			final SocketInterface socketInterface) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, SocketInterface.class.getName()));
	}
	
	public abstract SocketSetting newSocketSetting(final Object value);
	
	public abstract SocketSetting newSocketSetting(final String value);
	
}
