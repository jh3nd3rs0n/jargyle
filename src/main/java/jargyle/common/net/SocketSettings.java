package jargyle.common.net;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
		protected List<SocketSetting> socketSettings = new ArrayList<SocketSetting>();
	}
	
	static final class SocketSettingsXmlAdapter 
		extends XmlAdapter<SocketSettingsXml, SocketSettings> {

		@Override
		public SocketSettingsXml marshal(final SocketSettings v) throws Exception {
			if (v == null) { return null; }
			SocketSettingsXml socketSettingsXml = new SocketSettingsXml();
			socketSettingsXml.socketSettings = new ArrayList<SocketSetting>(
					v.socketSettings.values());
			return socketSettingsXml;
		}

		@Override
		public SocketSettings unmarshal(final SocketSettingsXml v) throws Exception {
			if (v == null) { return null; }
			return newInstance(v.socketSettings);
		}
		
	}
	
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
			final DatagramSocketInterface datagramSocketInterface) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(datagramSocketInterface);
		}
	}
	
	public void applyTo(
			final ServerSocketInterface serverSocketInterface) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(serverSocketInterface);
		}
	}
	
	public void applyTo(final SocketInterface socketInterface) throws SocketException {
		for (SocketSetting socketSetting : this.socketSettings.values()) {
			socketSetting.applyTo(socketInterface);
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.socketSettings == null) ? 0 : this.socketSettings.hashCode());
		return result;
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

	public Map<SocketSettingSpec, SocketSetting> toMap() {
		return Collections.unmodifiableMap(this.socketSettings);
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
