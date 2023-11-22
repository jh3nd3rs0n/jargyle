package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public abstract class SocketSettingSpec<V> {
	
	private final String name;
	private final Class<V> valueType;
		
	SocketSettingSpec(final String n, final Class<V> valType) {
		Objects.requireNonNull(n);
		Objects.requireNonNull(valType);
		this.name = n;
		this.valueType = valType;
	}
	
	public void apply(
			final V value,
			final DatagramSocket datagramSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s does not support application to a %s", 
				this.name, DatagramSocket.class.getName()));
	}
	
	public void apply(
			final V value, 
			final ServerSocket serverSocket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s does not support application to a %s", 
				this.name, ServerSocket.class.getName()));
	}
	
	public void apply(
			final V value, 
			final Socket socket) throws SocketException {
		throw new UnsupportedOperationException(String.format(
				"socket setting spec %s does not support application to a %s", 
				this.name, Socket.class.getName()));
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
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final Class<V> getValueType() {
		return this.valueType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		return result;
	}
	
	public final SocketSetting<V> newSocketSetting(final V value) {
		return new SocketSetting<V>(this, this.valueType.cast(value));
	}
	
	public final SocketSetting<V> newSocketSetting(
			final V value, final String doc) {
		SocketSetting<V> socketSetting = this.newSocketSetting(value);
		return new SocketSetting<V>(
				socketSetting.getSocketSettingSpec(),
				socketSetting.getValue(),
				doc);
	}

	public abstract SocketSetting<V> newSocketSettingWithParsableValue(
			final String value);

	public final SocketSetting<V> newSocketSettingWithParsableValue(
			final String value, final String doc) {
		SocketSetting<V> socketSetting = this.newSocketSettingWithParsableValue(
				value);
		return new SocketSetting<V>(
				socketSetting.getSocketSettingSpec(),
				socketSetting.getValue(),
				doc);
	}
	
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [name=")
			.append(this.name)
			.append("]");
		return builder.toString();
	}
	
}
