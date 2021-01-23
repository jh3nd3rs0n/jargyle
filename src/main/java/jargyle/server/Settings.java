package jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Settings.SettingsXmlAdapter.class)
public final class Settings {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "settings", propOrder = { "settings" })
	static class SettingsXml {
		@XmlElement(name = "setting")
		protected List<Setting> settings = new ArrayList<Setting>();
	}
	
	static final class SettingsXmlAdapter 
		extends XmlAdapter<SettingsXml, Settings> {

		@Override
		public SettingsXml marshal(final Settings v) throws Exception {
			if (v == null) { return null; }
			SettingsXml settingsXml = new SettingsXml();
			settingsXml.settings = new ArrayList<Setting>(v.settings);
			return settingsXml;
		}

		@Override
		public Settings unmarshal(final SettingsXml v) throws Exception {
			if (v == null) { return null; }
			return new Settings(v.settings);
		}
		
	}
	
	public static final Settings EMPTY_INSTANCE = new Settings(
			Collections.emptyList());
	
	public static Settings newInstance(final List<Setting> settings) {
		return new Settings(settings);
	}
	
	public static Settings newInstance(final Setting... settings) {
		return newInstance(Arrays.asList(settings));
	}
	
	private final List<Setting> settings;
	
	private Settings(final List<Setting> sttngs) {
		this.settings = new ArrayList<Setting>(sttngs);
	}
	
	public boolean containsNondefaultValue(final SettingSpec settingSpec) {
		for (Setting setting : this.settings) {
			if (setting.getSettingSpec().equals(settingSpec)) {
				return true;
			}
		}
		return false;
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
		Settings other = (Settings) obj;
		if (this.settings == null) {
			if (other.settings != null) {
				return false;
			}
		} else if (!this.settings.equals(other.settings)) {
			return false;
		}
		return true;
	}
	
	public <T> T getLastValue(
			final SettingSpec settingSpec, final Class<T> type) {
		List<T> values = this.getValues(settingSpec, type);
		T value = null;
		int size = values.size();
		if (size > 0) {
			value = values.get(size - 1);
		}
		return value;
	}
	
	public <T> List<T> getValues(
			final SettingSpec settingSpec, final Class<T> type) {
		List<T> values = new ArrayList<T>();
		for (Setting setting : this.settings) {
			if (setting.getSettingSpec().equals(settingSpec)) {
				@SuppressWarnings("unchecked")
				T val = (T) setting.getValue();
				values.add(val);
			}
		}
		if (values.isEmpty()) {
			Setting defaultSetting = settingSpec.getDefaultSetting();
			@SuppressWarnings("unchecked")
			T val = (T) defaultSetting.getValue();
			values.add(val);
		}
		return Collections.unmodifiableList(values);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.settings == null) ? 0 : this.settings.hashCode());
		return result;
	}

	public List<Setting> toList() {
		return Collections.unmodifiableList(this.settings);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [settings=")
			.append(this.settings)
			.append("]");
		return builder.toString();
	}
	
}
