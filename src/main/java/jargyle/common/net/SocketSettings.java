package jargyle.common.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class SocketSettings {

	public static SocketSettings newInstance(
			final List<SocketSetting> socketSttngs) {
		Map<SocketSettingSpec, SocketSetting> socketSettings = 
				new HashMap<SocketSettingSpec, SocketSetting>();
		for (SocketSetting socketSttng : socketSttngs) {
			socketSettings.put(socketSttng.getSocketSettingSpec(), socketSttng);
		}
		return new SocketSettings(socketSettings);
	}
	
	public static SocketSettings newInstance(
			final SocketSetting... socketSettings) {
		return newInstance(Arrays.asList(socketSettings));
	}
	
	public static SocketSettings newInstance(final SocketSettings other) {
		return new SocketSettings(other);
	}
	
	public static SocketSettings newInstance(final String s) {
		Map<SocketSettingSpec, SocketSetting> socketSettings = 
				new HashMap<SocketSettingSpec, SocketSetting>();
		if (s.isEmpty()) {
			return new SocketSettings(socketSettings);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			SocketSetting socketSetting = SocketSetting.newInstance(sElement);
			socketSettings.put(
					socketSetting.getSocketSettingSpec(), socketSetting);
		}
		return new SocketSettings(socketSettings);
	}
	
	private final Map<SocketSettingSpec, SocketSetting> socketSettings;
	
	private SocketSettings(
			final Map<SocketSettingSpec, SocketSetting> socketSttngs) {
		this.socketSettings = new HashMap<SocketSettingSpec, SocketSetting>(
				socketSttngs);
	}
	
	private SocketSettings(final SocketSettings other) {
		this.socketSettings = new HashMap<SocketSettingSpec, SocketSetting>(
				other.socketSettings);
	}
	
	public void applyTo(
			final DatagramSocket datagramSocket) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(datagramSocket);
		}
	}
	
	public void applyTo(
			final ServerSocket serverSocket) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(serverSocket);
		}
	}
	
	public void applyTo(final Socket socket) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(socket);
		}
	}
	
	public <T> T getValue(
			final SocketSettingSpec socketSettingSpec, final Class<T> type) {
		T value = null;
		SocketSetting socketSetting = this.socketSettings.get(
				socketSettingSpec);
		if (socketSetting != null) {
			@SuppressWarnings("unchecked")
			T val = (T) socketSetting.getValue();
			value = val; 
		}
		return value;
	}
	
	public <T> T putValue(
			final SocketSettingSpec socketSettingSpec, final T value) {
		T recentValue = null;
		SocketSetting socketSetting = socketSettingSpec.newSocketSetting(value);
		SocketSetting recentSocketSetting = this.socketSettings.put(
				socketSettingSpec, socketSetting);
		if (recentSocketSetting != null) {
			@SuppressWarnings("unchecked")
			T recentVal = (T) recentSocketSetting.getValue();
			recentValue = recentVal;
		}
		return recentValue;
	}
	
	public Collection<SocketSetting> toCollection() {
		return Collections.unmodifiableCollection(this.socketSettings.values());
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<SocketSetting> iterator = 
				this.socketSettings.values().iterator(); iterator.hasNext();) {
			SocketSetting socketSetting = iterator.next();
			builder.append(socketSetting);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
