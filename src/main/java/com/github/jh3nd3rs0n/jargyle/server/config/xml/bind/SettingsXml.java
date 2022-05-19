package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "settings", propOrder = { "settingsXml" }) 
class SettingsXml {
	
	@XmlElement(name = "setting")
	protected List<SettingXml> settingsXml;
	
	public SettingsXml() {
		this.settingsXml = new ArrayList<SettingXml>(); 
	}
	
	public SettingsXml(final Settings settings) {
		List<SettingXml> sttngsXml = new ArrayList<SettingXml>();
		for (Setting<Object> setting : settings.toList()) {
			sttngsXml.add(new SettingXml(setting));
		}
		this.settingsXml = sttngsXml;
	}
	
	public Settings toSettings() {
		List<Setting<? extends Object>> settings = 
				new ArrayList<Setting<? extends Object>>();
		for (SettingXml settingXml : this.settingsXml) {
			settings.add(settingXml.toSetting());
		}
		return Settings.newInstance(settings);
	}
	
}