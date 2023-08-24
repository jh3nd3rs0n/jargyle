package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.lang.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;

public final class StandardSocketSettingSpecConstants {

	private static final SocketSettingSpecs SOCKET_SETTING_SPECS = 
			new SocketSettingSpecs();
	
	@HelpText(
			doc = "The type-of-service or traffic class field in the IP "
					+ "header for a TCP or UDP socket", 
			usage = "IP_TOS=INTEGER_BETWEEN_0_AND_255"
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
		public SocketSetting<UnsignedByte> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(UnsignedByte.newInstance(value));
		}
		
	});
	
	@HelpText(
			doc = "Performance preferences for a TCP socket described by "
					+ "three digits whose values indicate the relative "
					+ "importance of short connection time, low latency, and "
					+ "high bandwidth", 
			usage = "PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2"
	)
	public static final SocketSettingSpec<PerformancePreferences> PERF_PREF = SOCKET_SETTING_SPECS.addThenGet(new SocketSettingSpec<PerformancePreferences>(
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
		
	});
	
	@HelpText(
			doc = "Can send broadcast datagrams", 
			usage = "SO_BROADCAST=true|false"
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
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	});
	
	@HelpText(
			doc = "Keeps a TCP socket alive when no data has been exchanged "
					+ "in either direction", 
			usage = "SO_KEEPALIVE=true|false"
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
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
		
	});
	
	@HelpText(
			doc = "Linger on closing the TCP socket in seconds", 
			usage = "SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647"
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
		public SocketSetting<NonnegativeInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	});
	
	@HelpText(
			doc = "Can receive TCP urgent data", 
			usage = "SO_OOBINLINE=true|false"
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
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
	
	});
	
	@HelpText(
			doc = "The receive buffer size", 
			usage = "SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647"
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
		public SocketSetting<PositiveInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	});
	
	@HelpText(
			doc = "Can reuse socket address and port", 
			usage = "SO_REUSEADDR=true|false"
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
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(Boolean.valueOf(value));
		}
	
	});
	
	@HelpText(
			doc = "The send buffer size", 
			usage = "SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647"
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
		public SocketSetting<PositiveInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(PositiveInteger.newInstance(value));
		}
		
	});
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for an idle socket", 
			usage = "SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647"
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
		public SocketSetting<NonnegativeInteger> newSocketSettingOfParsableValue(
				final String value) {
			return super.newSocketSetting(NonnegativeInteger.newInstance(value));
		}
		
	});
	
	@HelpText(
			doc = "Disables Nagle's algorithm", 
			usage = "TCP_NODELAY=true|false"
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
		public SocketSetting<Boolean> newSocketSettingOfParsableValue(
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
