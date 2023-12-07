package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.number.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "Standard Socket Settings"
)
public final class StandardSocketSettingSpecConstants {

	private static final SocketSettingSpecs SOCKET_SETTING_SPECS = 
			new SocketSettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The type-of-service or traffic class field in the "
					+ "IP header for a TCP or UDP socket",
			name = "IP_TOS",
			syntax = "IP_TOS=UNSIGNED_BYTE",
			valueType = UnsignedByte.class
	)
	public static final SocketSettingSpec<UnsignedByte> IP_TOS = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<UnsignedByte>(
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
		public SocketSetting<UnsignedByte> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(UnsignedByte.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Performance preferences for a TCP socket described "
					+ "by three digits whose values indicate the relative "
					+ "importance of short connection time, low latency, and "
					+ "high bandwidth",
			name = "PERF_PREFS",
			syntax = "PERF_PREFS=PERFORMANCE_PREFERENCES",
			valueType = PerformancePreferences.class
	)
	public static final SocketSettingSpec<PerformancePreferences> PERF_PREFS = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<PerformancePreferences>(
			"PERF_PREFS", 
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
		public SocketSetting<PerformancePreferences> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(PerformancePreferences.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Can send broadcast datagrams",
			name = "SO_BROADCAST",
			syntax = "SO_BROADCAST=true|false",
			valueType = Boolean.class
	)
	public static final SocketSettingSpec<Boolean> SO_BROADCAST = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<Boolean>(
			"SO_BROADCAST", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value,
				final DatagramSocket datagramSocket) throws SocketException {
			datagramSocket.setBroadcast(value.booleanValue());
		}
	
		@Override
		public SocketSetting<Boolean> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Keeps a TCP socket alive when no data has been "
					+ "exchanged in either direction",
			name = "SO_KEEPALIVE",
			syntax = "SO_KEEPALIVE=true|false",
			valueType = Boolean.class
	)
	public static final SocketSettingSpec<Boolean> SO_KEEPALIVE = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<Boolean>(
			"SO_KEEPALIVE", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setKeepAlive(value.booleanValue());
		}
	
		@Override
		public SocketSetting<Boolean> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Linger on closing the TCP socket in seconds",
			name = "SO_LINGER",
			syntax = "SO_LINGER=NONNEGATIVE_INTEGER",
			valueType = NonnegativeInteger.class
	)
	public static final SocketSettingSpec<NonnegativeInteger> SO_LINGER = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<NonnegativeInteger>(
			"SO_LINGER", 
			NonnegativeInteger.class) {
		
		@Override
		public void apply(
				final NonnegativeInteger value, 
				final Socket socket) throws SocketException {
			socket.setSoLinger(true, value.intValue());
		}
	
		@Override
		public SocketSetting<NonnegativeInteger> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Can receive TCP urgent data",
			name = "SO_OOBINLINE",
			syntax = "SO_OOBINLINE=true|false",
			valueType = Boolean.class
	)
	public static final SocketSettingSpec<Boolean> SO_OOBINLINE = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<Boolean>(
			"SO_OOBINLINE", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setOOBInline(value.booleanValue());
		}
	
		@Override
		public SocketSetting<Boolean> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
	
	});
	
	@NameValuePairValueSpecDoc(
			description = "The receive buffer size",
			name = "SO_RCVBUF",
			syntax = "SO_RCVBUF=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SocketSettingSpec<PositiveInteger> SO_RCVBUF = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<PositiveInteger>(
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
		public SocketSetting<PositiveInteger> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Can reuse socket address and port",
			name = "SO_REUSEADDR",
			syntax = "SO_REUSEADDR=true|false",
			valueType = Boolean.class
	)
	public static final SocketSettingSpec<Boolean> SO_REUSEADDR = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<Boolean>(
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
		public SocketSetting<Boolean> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
	
	});
	
	@NameValuePairValueSpecDoc(
			description = "The send buffer size",
			name = "SO_SNDBUF",
			syntax = "SO_SNDBUF=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SocketSettingSpec<PositiveInteger> SO_SNDBUF = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<PositiveInteger>(
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
		public SocketSetting<PositiveInteger> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "The timeout in milliseconds on waiting for an idle "
					+ "socket",
			name = "SO_TIMEOUT",
			syntax = "SO_TIMEOUT=NONNEGATIVE_INTEGER",
			valueType = NonnegativeInteger.class
	)
	public static final SocketSettingSpec<NonnegativeInteger> SO_TIMEOUT = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<NonnegativeInteger>(
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
		public SocketSetting<NonnegativeInteger> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	});
	
	@NameValuePairValueSpecDoc(
			description = "Disables Nagle's algorithm",
			name = "TCP_NODELAY",
			syntax = "TCP_NODELAY=true|false",
			valueType = Boolean.class
	)
	public static final SocketSettingSpec<Boolean> TCP_NODELAY = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<Boolean>(
			"TCP_NODELAY", 
			Boolean.class) {
		
		@Override
		public void apply(
				final Boolean value, 
				final Socket socket) throws SocketException {
			socket.setTcpNoDelay(value.booleanValue());
		}
	
		@Override
		public SocketSetting<Boolean> newSocketSettingWithParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	});
	
	public static List<SocketSettingSpec<Object>> values() {
		return SOCKET_SETTING_SPECS.toList();
	}
	
	public static Map<String, SocketSettingSpec<Object>> valuesMap() {
		return SOCKET_SETTING_SPECS.toMap();
	}
	
	private StandardSocketSettingSpecConstants() { }
}
