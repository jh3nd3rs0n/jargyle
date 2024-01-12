package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Settings {

	private static final Settings EMPTY_INSTANCE = new Settings(
			Collections.emptyList());
	
	public static Settings getEmptyInstance() {
		return EMPTY_INSTANCE;
	}
	
	public static Settings of(
			final List<Setting<? extends Object>> settings) {
		List<Setting<Object>> sttngs = new ArrayList<Setting<Object>>();
		for (Setting<? extends Object> setting : settings) {
			@SuppressWarnings("unchecked")
			Setting<Object> sttng = (Setting<Object>) setting;
			sttngs.add(sttng);
		}
		return new Settings(sttngs);
	}
	
	@SafeVarargs
	public static Settings of(
			final Setting<? extends Object>... settings) {
		return of(Arrays.asList(settings));
	}
	
	private final Map<SettingSpec<Object>, List<Setting<Object>>> settingListMap;
	private final List<Setting<Object>> settings;
	
	private Settings(final List<Setting<Object>> sttngs) {
		Map<SettingSpec<Object>, List<Setting<Object>>> sttngListMap = 
				new HashMap<SettingSpec<Object>, List<Setting<Object>>>();
		for (Setting<Object> sttng : sttngs) {
			SettingSpec<Object> sttngSpec = sttng.getSettingSpec();
			if (sttngListMap.containsKey(sttngSpec)) {
				List<Setting<Object>> sttngList = sttngListMap.get(sttngSpec);
				sttngList.add(sttng);
			} else {
				List<Setting<Object>> sttngList = 
						new ArrayList<Setting<Object>>();
				sttngList.add(sttng);
				sttngListMap.put(sttngSpec, sttngList);
			}
		}
		this.settingListMap = sttngListMap;
		this.settings = new ArrayList<Setting<Object>>(sttngs);
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
	
	public <V> V getLastValue(final SettingSpec<V> settingSpec) {
		List<Setting<Object>> settingList = this.settingListMap.get(
				settingSpec);
		V value = null;
		if (settingList != null && settingList.size() > 0) {
			Setting<Object> setting = settingList.get(settingList.size() - 1);
			value = settingSpec.getValueType().cast(setting.getValue());
		}
		if (value == null) {
			Setting<V> defaultSetting = settingSpec.getDefaultSetting();
			value = defaultSetting.getValue();
		}
		return value;		
	}
	
	public <V> List<V> getValues(final SettingSpec<V> settingSpec) {
		List<Setting<Object>> settingList = this.settingListMap.get(
				settingSpec);
		List<V> values = new ArrayList<V>();
		if (settingList != null) {
			for (Setting<Object> setting : settingList) {
				V value = settingSpec.getValueType().cast(setting.getValue());
				values.add(value);
			}
		}
		if (values.isEmpty()) {
			Setting<V> defaultSetting = settingSpec.getDefaultSetting();
			V value = defaultSetting.getValue();
			if (value != null) {
				values.add(value);
			}
		}
		return Collections.unmodifiableList(values);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.settings == null) ? 
				0 : this.settings.hashCode());
		return result;
	}

	public List<Setting<Object>> toList() {
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
