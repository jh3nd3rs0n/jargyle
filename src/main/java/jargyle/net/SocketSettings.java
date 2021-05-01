package jargyle.net;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(SocketSettings.SocketSettingsXmlAdapter.class)
public final class SocketSettings {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "socketSettings", propOrder = { "socketSettings" })
	static class SocketSettingsXml {
		@XmlElement(name = "socketSetting")
		protected List<SocketSetting<? extends Object>> socketSettings = new ArrayList<SocketSetting<? extends Object>>();
	}
	
	static final class SocketSettingsXmlAdapter 
		extends XmlAdapter<SocketSettingsXml, SocketSettings> {

		@Override
		public SocketSettingsXml marshal(
				final SocketSettings v) throws Exception {
			if (v == null) { return null; }
			SocketSettingsXml socketSettingsXml = new SocketSettingsXml();
			socketSettingsXml.socketSettings = 
					new ArrayList<SocketSetting<? extends Object>>(
							v.socketSettings.values());
			return socketSettingsXml;
		}

		@Override
		public SocketSettings unmarshal(
				final SocketSettingsXml v) throws Exception {
			if (v == null) { return null; }
			return newInstance(v.socketSettings);
		}
		
	}
	
	public static SocketSettings newInstance(
			final List<SocketSetting<? extends Object>> socketSttngs) {
		Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSettings = 
				new LinkedHashMap<SocketSettingSpec<Object>, SocketSetting<Object>>();
		for (SocketSetting<? extends Object> socketSttng : socketSttngs) {
			@SuppressWarnings("unchecked")
			SocketSetting<Object> sockSttng = (SocketSetting<Object>) socketSttng;
			SocketSettingSpec<Object> sockSttngSpec = sockSttng.getSocketSettingSpec(); 
			socketSettings.put(sockSttngSpec, sockSttng);
		}
		return new SocketSettings(socketSettings);
	}
	
	@SafeVarargs
	public static SocketSettings newInstance(
			final SocketSetting<? extends Object>... socketSettings) {
		return newInstance(Arrays.asList(socketSettings));
	}
	
	public static SocketSettings newInstance(final SocketSettings other) {
		return new SocketSettings(other);
	}
	
	public static SocketSettings newInstance(final String s) {
		Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSettings = 
				new LinkedHashMap<SocketSettingSpec<Object>, SocketSetting<Object>>();
		if (s.isEmpty()) {
			return new SocketSettings(socketSettings);
		}
		String[] sElements = s.split(" ");
		for (String sElement : sElements) {
			SocketSetting<Object> socketSetting = SocketSetting.newInstance(
					sElement);
			SocketSettingSpec<Object> socketSettingSpec = 
					socketSetting.getSocketSettingSpec();
			socketSettings.put(socketSettingSpec, socketSetting);
		}
		return new SocketSettings(socketSettings);
	}
	
	private final Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSettings;
	
	private SocketSettings(
			final Map<SocketSettingSpec<Object>, SocketSetting<Object>> socketSttngs) {
		this.socketSettings = 
				new LinkedHashMap<SocketSettingSpec<Object>, SocketSetting<Object>>(
						socketSttngs);
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
		V recentValue = null;
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> socketSttngSpec = 
				(SocketSettingSpec<Object>) socketSettingSpec;
		SocketSetting<Object> socketSttng = socketSttngSpec.newSocketSetting(
				socketSettingSpec.getValueType().cast(value));
		SocketSetting<Object> recentSocketSetting = 
				this.socketSettings.put(socketSttngSpec, socketSttng);
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
		StringBuilder builder = new StringBuilder();
		for (Iterator<SocketSetting<Object>> iterator = 
				this.socketSettings.values().iterator(); iterator.hasNext();) {
			SocketSetting<Object> socketSetting = iterator.next();
			builder.append(socketSetting);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}
