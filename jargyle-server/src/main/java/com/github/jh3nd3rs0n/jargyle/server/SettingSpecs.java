package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class SettingSpecs {

	private final List<SettingSpec<Object>> settingSpecs;
	private final Map<String, SettingSpec<Object>> settingSpecsMap;
	
	public SettingSpecs() {
		this.settingSpecs = new ArrayList<SettingSpec<Object>>();
		this.settingSpecsMap = new HashMap<String, SettingSpec<Object>>();
	}
	
	public <T> SettingSpec<T> addThenGet(final SettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SettingSpec<Object> val = (SettingSpec<Object>) value;
		this.settingSpecs.add(val);
		this.settingSpecsMap.put(val.getName(), val);
		return value;
	}
	
	public List<SettingSpec<Object>> toList() {
		return Collections.unmodifiableList(this.settingSpecs);
	}
	
	public Map<String, SettingSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.settingSpecsMap);
	}
	
}
