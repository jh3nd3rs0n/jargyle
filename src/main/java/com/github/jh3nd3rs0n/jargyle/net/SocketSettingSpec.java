package com.github.jh3nd3rs0n.jargyle.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public abstract class SocketSettingSpec<V> {
	
	private final String string;
	private final Class<V> valueType;
		
	SocketSettingSpec(final String s, final Class<V> valType) {
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
