package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class SocketSetting<V> {

	public static SocketSetting<Object> newInstance(final String s) {
		String[] sElements = s.split("=", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"socket setting must be in the following format: "
					+ "NAME=VALUE");
		}
		String socketSettingSpecString = sElements[0];
		String value = sElements[1];
		return newInstanceOfParsableValue(socketSettingSpecString, value);
	}
	
	public static <V> SocketSetting<V> newInstance(
			final String name, final V value) {
		@SuppressWarnings("unchecked")
		SocketSetting<V> socketSetting = 
				(SocketSetting<V>) SocketSettingSpecConstants.valueOfName(
						name).newSocketSetting(value);
		return socketSetting;
	}
	
	public static <V> SocketSetting<V> newInstance(
			final String name, final V value, final String doc) {
		SocketSetting<V> socketSetting = newInstance(name, value);
		return new SocketSetting<V>(
				socketSetting.getSocketSettingSpec(),
				socketSetting.getValue(),
				doc);
	}
	
	public static SocketSetting<Object> newInstanceOfParsableValue(
			final String name, final String value) {
		return SocketSettingSpecConstants.valueOfName(
				name).newSocketSettingOfParsableValue(value);
	}
	
	public static SocketSetting<Object> newInstanceOfParsableValue(
			final String name, final String value, final String doc) {
		SocketSetting<Object> socketSetting = newInstanceOfParsableValue(
				name, value);
		return new SocketSetting<Object>(
				socketSetting.getSocketSettingSpec(), 
				socketSetting.getValue(), 
				doc);
	}
	
	private final String name;
	private final SocketSettingSpec<V> socketSettingSpec;
	private final V value;
	private final String doc;
	
	SocketSetting(final SocketSettingSpec<V> spec, final V val) {
		this(spec, val, null);
	}
	
	private SocketSetting(
			final SocketSettingSpec<V> spec, final V val, final String d) {
		V v = spec.getValueType().cast(val);
		this.name = spec.getName();
		this.socketSettingSpec = spec;
		this.value = v;
		this.doc = d;
	}
	
	public void applyTo(
			final DatagramSocket datagramSocket) throws SocketException {
		this.socketSettingSpec.apply(this.value, datagramSocket);
	}
	
	public void applyTo(
			final ServerSocket serverSocket) throws SocketException {
		this.socketSettingSpec.apply(this.value, serverSocket);
	}
	
	public void applyTo(final Socket socket) throws SocketException {
		this.socketSettingSpec.apply(this.value, socket);
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
		SocketSetting<?> other = (SocketSetting<?>) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!this.value.equals(other.value)) {
			return false;
		}
		return true;
	}

	public String getDoc() {
		return this.doc;
	}
	
	public String getName() {
		return this.name;
	}
	
	public SocketSettingSpec<V> getSocketSettingSpec() {
		return this.socketSettingSpec;
	}

	public V getValue() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		result = prime * result + ((this.value == null) ? 
				0 : this.value.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("%s=%s", this.name, this.value);
	}
	
}
