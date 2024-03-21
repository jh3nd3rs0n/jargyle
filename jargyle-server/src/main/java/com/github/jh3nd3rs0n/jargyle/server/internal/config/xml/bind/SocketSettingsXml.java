package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socketSettings", propOrder = { "socketSettingsXml" }) 
class SocketSettingsXml extends ValueXml {
	
	@XmlElement(name = "socketSetting")
	protected List<SocketSettingXml> socketSettingsXml;
	
	public SocketSettingsXml() {
		this.socketSettingsXml = new ArrayList<SocketSettingXml>(); 
	}
	
	public SocketSettingsXml(final SocketSettings socketSettings) {
		this.socketSettingsXml = new ArrayList<SocketSettingXml>();
		for (SocketSetting<Object> value : socketSettings.toMap().values()) {
			this.socketSettingsXml.add(new SocketSettingXml(value));
		}
	}
	
	public SocketSettings toSocketSettings() {
		List<SocketSetting<? extends Object>> socketSettings =
				new ArrayList<SocketSetting<? extends Object>>();
		for (SocketSettingXml socketSettingXml : this.socketSettingsXml) {
			socketSettings.add(socketSettingXml.toSocketSetting());
		}
		return SocketSettings.of(socketSettings);
	}

	@Override
	public Object toValue() {
		return this.toSocketSettings();
	}
	
}