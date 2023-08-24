package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SocketSettings {

	public static SocketSettings newInstance(
			final List<SocketSetting<? extends Object>> socketSttngs) {
		return new SocketSettings(socketSttngs);
	}
	
	@SafeVarargs
	public static SocketSettings newInstance(
			final SocketSetting<? extends Object>... socketSttngs) {
		return newInstance(Arrays.asList(socketSttngs));
	}
	
	public static SocketSettings newInstance(final SocketSettings other) {
		return new SocketSettings(other);
	}
	
	public static SocketSettings newInstance(final String s) {
		List<SocketSetting<? extends Object>> socketSettings = 
				new ArrayList<SocketSetting<? extends Object>>();
		if (s.isEmpty()) {
			return newInstance(socketSettings);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			SocketSetting<Object> socketSetting = SocketSetting.newInstance(
					sElement);
			socketSettings.add(socketSetting);
		}
		return newInstance(socketSettings);
	}
	
	private final Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSettings;

	private SocketSettings(
			final List<SocketSetting<? extends Object>> socketSttngs) {
		Map<SocketSettingSpec<Object>, SocketSetting<Object>> map =
				new LinkedHashMap<SocketSettingSpec<Object>, SocketSetting<Object>>();
		for (SocketSetting<? extends Object> socketSttng : socketSttngs) {
			@SuppressWarnings("unchecked")
			SocketSettingSpec<Object> socketSttngSpec = 
					(SocketSettingSpec<Object>) socketSttng.getSocketSettingSpec();
			if (map.containsKey(socketSttngSpec)) {
				map.remove(socketSttngSpec);
			}
			@SuppressWarnings("unchecked")
			SocketSetting<Object> sockSttng = (SocketSetting<Object>) socketSttng;
			map.put(socketSttngSpec, sockSttng);
		}
		this.socketSettings = map;
	}
	
	private SocketSettings(final SocketSettings other) {
		this.socketSettings = 
				new LinkedHashMap<SocketSettingSpec<Object>, SocketSetting<Object>>(
						other.socketSettings);
	}
	
	public void applyTo(
			final DatagramSocket datagramSocket) throws SocketException {
		for (SocketSetting<Object> socketSetting 
				: this.socketSettings.values()) {
			socketSetting.applyTo(datagramSocket);
		}
	}
	
	public void applyTo(
			final ServerSocket serverSocket) throws SocketException {
		for (SocketSetting<Object> socketSetting 
				: this.socketSettings.values()) {
			socketSetting.applyTo(serverSocket);
		}
	}
	
	public void applyTo(final Socket socket) throws SocketException {
		for (SocketSetting<Object> socketSetting 
				: this.socketSettings.values()) {
			socketSetting.applyTo(socket);
		}
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
		SocketSettings other = (SocketSettings) obj;
		if (this.socketSettings == null) {
			if (other.socketSettings != null) {
				return false;
			}
		} else if (!this.socketSettings.equals(other.socketSettings)) {
			return false;
		}
		return true;
	}
	
	public <V> V getValue(final SocketSettingSpec<V> socketSettingSpec) {
		V value = null;
		SocketSetting<Object> socketSetting = this.socketSettings.get(
				socketSettingSpec);
		if (socketSetting != null) {
			value = socketSettingSpec.getValueType().cast(
					socketSetting.getValue()); 
		}
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socketSettings == null) ? 
				0 : this.socketSettings.hashCode());
		return result;
	}
	
	public <V> V putValue(
			final SocketSettingSpec<V> socketSettingSpec, final V value) {
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> socketSttngSpec = 
				(SocketSettingSpec<Object>) socketSettingSpec;
		SocketSetting<Object> socketSttng = socketSttngSpec.newSocketSetting(
				socketSettingSpec.getValueType().cast(value));
		V recentValue = this.remove(socketSettingSpec);
		this.socketSettings.put(socketSttngSpec, socketSttng);
		return recentValue;
	}
	
	public <V> V remove(final SocketSettingSpec<V> socketSettingSpec) {
		V recentValue = null;
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> socketSttngSpec = 
				(SocketSettingSpec<Object>) socketSettingSpec;
		SocketSetting<Object> recentSocketSetting = this.socketSettings.remove(
				socketSttngSpec);
		if (recentSocketSetting != null) {
			recentValue = socketSettingSpec.getValueType().cast(
					recentSocketSetting.getValue());
		}
		return recentValue;
	}

	public Map<SocketSettingSpec<Object>, SocketSetting<Object>> toMap() {
		return Collections.unmodifiableMap(this.socketSettings);
	}

	@Override
	public String toString() {
		return this.socketSettings.values().stream()
				.map(SocketSetting::toString)
				.collect(Collectors.joining(" "));
	}
}
