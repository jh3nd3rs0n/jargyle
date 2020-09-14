package jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
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
	
	public <T> T getLastValue(
			final SettingSpec settingSpec, final Class<T> type) {
		List<T> values = this.getValues(settingSpec, type);
		T value = null;
		if (values.size() > 0) {
			value = values.get(values.size() - 1);
		}
		return value;
	}
	
	public <T> List<T> getValues(
			final SettingSpec settingSpec, final Class<T> type) {
		List<T> values = new ArrayList<T>();
		for (Setting setting : this.settings) {
			if (setting.getName().equals(settingSpec.getName())) {
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
	
	public List<Setting> toList() {
		return Collections.unmodifiableList(this.settings);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<Setting> iterator = this.settings.iterator();
				iterator.hasNext();) {
			Setting setting = iterator.next();
			builder.append(setting.toString());
			if (iterator.hasNext()) {
				builder.append(',');
			}
		}
		return builder.toString();
	}
}
