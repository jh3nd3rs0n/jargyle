package jargyle.net;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jargyle.annotation.HelpText;
import jargyle.util.NonnegativeInteger;
import jargyle.util.PositiveInteger;
import jargyle.util.UnsignedByte;

public abstract class SocketSettingSpec<V> {

	@HelpText(
			doc = "The type-of-service or traffic class field in the IP "
					+ "header for a TCP or UDP socket", 
			usage = "IP_TOS=INTEGER_BETWEEN_0_AND_255"
	)
	public static final SocketSettingSpec<UnsignedByte> IP_TOS = new SocketSettingSpec<UnsignedByte>(
			"IP_TOS", 
			UnsignedByte.class) {
		
		@Override
		public void apply(
				final UnsignedByte value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setTrafficClass(value.intValue());
		}
		
		@Override
		public void apply(
				final UnsignedByte value,
				final Socket socket) throws SocketException {
			socket.setTrafficClass(value.intValue());
		}

		@Override
		public SocketSetting<UnsignedByte> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(UnsignedByte.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "Performance preferences for a TCP socket described by "
					+ "three digits whose values indicate the relative "
					+ "importance of short connection time, low latency, and "
					+ "high bandwidth", 
			usage = "PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2"
	)
	public static final SocketSettingSpec<PerformancePreferences> PERF_PREF = new SocketSettingSpec<PerformancePreferences>(
			"PERF_PREF", 
			PerformancePreferences.class) {
		
		@Override
		public void apply(
				final PerformancePreferences value,
				final ServerSocket serverSocket) throws SocketException {
			value.applyTo(serverSocket);
		}

		@Override
		public void apply(
				final PerformancePreferences value,
				final Socket socket) throws SocketException {
			value.applyTo(socket);
		}

		@Override
		public SocketSetting<PerformancePreferences> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(PerformancePreferences.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "Can send broadcast datagrams", 
			usage = "SO_BROADCAST=true|false"
	)
	public static final SocketSettingSpec<Boolean> SO_BROADCAST = new SocketSettingSpec<Boolean>(
			"SO_BROADCAST", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setBroadcast(value.booleanValue());
		}

		@Override
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	};
	
	@HelpText(
			doc = "Keeps a TCP socket alive when no data has been exchanged "
					+ "in either direction", 
			usage = "SO_KEEPALIVE=true|false"
	)
	public static final SocketSettingSpec<Boolean> SO_KEEPALIVE = new SocketSettingSpec<Boolean>(
			"SO_KEEPALIVE", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setKeepAlive(value.booleanValue());
		}

		@Override
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	};
	
	@HelpText(
			doc = "Linger on closing the TCP socket in seconds", 
			usage = "SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SocketSettingSpec<NonnegativeInteger> SO_LINGER = new SocketSettingSpec<NonnegativeInteger>(
			"SO_LINGER", 
			NonnegativeInteger.class) {
		
		@Override
		public void apply(
				final NonnegativeInteger value, 
				final Socket socket) throws SocketException {
			socket.setSoLinger(true, value.intValue());
		}

		@Override
		public SocketSetting<NonnegativeInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "Can receive TCP urgent data", 
			usage = "SO_OOBINLINE=true|false"
	)
	public static final SocketSettingSpec<Boolean> SO_OOBINLINE = new SocketSettingSpec<Boolean>(
			"SO_OOBINLINE", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setOOBInline(value.booleanValue());
		}

		@Override
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}

	};
	
	@HelpText(
			doc = "The receive buffer size", 
			usage = "SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SocketSettingSpec<PositiveInteger> SO_RCVBUF = new SocketSettingSpec<PositiveInteger>(
			"SO_RCVBUF", 
			PositiveInteger.class) {
		
		@Override
		public void apply(
				final PositiveInteger value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setReceiveBufferSize(value.intValue());
		}
		
		@Override
		public void apply(
				final PositiveInteger value, 
				final ServerSocket serverSocket) throws SocketException {
			serverSocket.setReceiveBufferSize(value.intValue());
		}
		
		@Override
		public void apply(
				final PositiveInteger value, 
				final Socket socket) throws SocketException {
			socket.setReceiveBufferSize(value.intValue());
		}

		@Override
		public SocketSetting<PositiveInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "Can reuse socket address and port", 
			usage = "SO_REUSEADDR=true|false"
	)
	public static final SocketSettingSpec<Boolean> SO_REUSEADDR = new SocketSettingSpec<Boolean>(
			"SO_REUSEADDR", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setReuseAddress(value.booleanValue());
		}
		
		@Override
		public void apply(
				final Boolean value, 
				final ServerSocket serverSocket) throws SocketException {
			serverSocket.setReuseAddress(value.booleanValue());
		}

		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setReuseAddress(value.booleanValue());
		}

		@Override
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}

	};
	
	@HelpText(
			doc = "The send buffer size", 
			usage = "SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SocketSettingSpec<PositiveInteger> SO_SNDBUF = new SocketSettingSpec<PositiveInteger>(
			"SO_SNDBUF", 
			PositiveInteger.class) {
		
		@Override
		public void apply(
				final PositiveInteger value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setSendBufferSize(value.intValue());
		}
		
		@Override
		public void apply(
				final PositiveInteger value, 
				final Socket socket) throws SocketException {
			socket.setSendBufferSize(value.intValue());
		}

		@Override
		public SocketSetting<PositiveInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for an idle socket", 
			usage = "SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SocketSettingSpec<NonnegativeInteger> SO_TIMEOUT = new SocketSettingSpec<NonnegativeInteger>(
			"SO_TIMEOUT", 
			NonnegativeInteger.class) {
		
		@Override
		public void apply(
				final NonnegativeInteger value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setSoTimeout(value.intValue());
		}
		
		@Override
		public void apply(
				final NonnegativeInteger value, 
				final ServerSocket serverSocket) throws SocketException {
			serverSocket.setSoTimeout(value.intValue());
		}

		@Override
		public void apply(
				final NonnegativeInteger value, 
				final Socket socket) throws SocketException {
			socket.setSoTimeout(value.intValue());
		}

		@Override
		public SocketSetting<NonnegativeInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	};
	
	@HelpText(
			doc = "Disables Nagle's algorithm", 
			usage = "TCP_NODELAY=true|false"
	)
	public static final SocketSettingSpec<Boolean> TCP_NODELAY = new SocketSettingSpec<Boolean>(
			"TCP_NODELAY", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setTcpNoDelay(value.booleanValue());
		}

		@Override
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	};
	
	private static final List<SocketSettingSpec<Object>> VALUES = 
			new ArrayList<SocketSettingSpec<Object>>();
	
	private static final Map<String, SocketSettingSpec<Object>> VALUES_MAP =
			new HashMap<String, SocketSettingSpec<Object>>();
	
	private static void fillValuesAndValuesMap() {
		Field[] fields = SocketSettingSpec.class.getFields();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			Class<?> type = field.getType();
			if (!Modifier.isStatic(modifiers)
					|| !Modifier.isFinal(modifiers)
					|| !SocketSettingSpec.class.isAssignableFrom(type)) {
				continue;
			}
			Object value = null;
			try {
				value = field.get(null);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			}
			@SuppressWarnings("unchecked")
			SocketSettingSpec<Object> val = (SocketSettingSpec<Object>) value;
			VALUES.add(val);
			VALUES_MAP.put(val.toString(), val);
		}
	}
	
	public static SocketSettingSpec<Object> valueOfString(final String s) {
		Map<String, SocketSettingSpec<Object>> valuesMap =
				SocketSettingSpec.valuesMap();
		if (valuesMap.containsKey(s)) {
			return valuesMap.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown socket setting: %s", s));
	}
	
	public static SocketSettingSpec<Object>[] values() {
		if (VALUES.isEmpty()) {
			fillValuesAndValuesMap();
		}
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object>[] vals = 
				(SocketSettingSpec<Object>[]) VALUES.toArray(
						new SocketSettingSpec<?>[VALUES.size()]);
		return vals;
	}
	
	private static Map<String, SocketSettingSpec<Object>> valuesMap() {
		if (VALUES_MAP.isEmpty()) {
			fillValuesAndValuesMap();
		}
		return Collections.unmodifiableMap(VALUES_MAP);		
	}
	
	private final String string;
	private final Class<V> valueType;
		
	private SocketSettingSpec(final String s, final Class<V> valType) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		this.string = s;
		this.valueType = valType;
	}
	
	public void apply(
			final V value,
			final DatagramSocket datagramSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, DatagramSocket.class.getName()));
	}
	
	public void apply(
			final V value, 
			final ServerSocket serverSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, ServerSocket.class.getName()));
	}
	
	public void apply(
			final V value, 
			final Socket socket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s is not supported under %s", 
				this, Socket.class.getName()));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		SocketSettingSpec<?> other = (SocketSettingSpec<?>) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	public final Class<V> getValueType() {
		return this.valueType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}

	public final SocketSetting<V> newSocketSetting(final V value) {
		return new SocketSetting<V>(this, this.valueType.cast(value));
	}
	
	public abstract SocketSetting<V> newSocketSettingOfParsableValue(
			final String value);

	@Override
	public final String toString() {
		return this.string;
	}
	
}
