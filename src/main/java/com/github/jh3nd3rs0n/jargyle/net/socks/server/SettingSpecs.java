package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class SettingSpecs {

	private final Map<String, SettingSpec<Object>> settingSpecMap;
	
	public SettingSpecs() {
		this.settingSpecMap = new HashMap<String, SettingSpec<Object>>();
	}
	
	public <T> SettingSpec<T> putThenGet(final SettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SettingSpec<Object> val = (SettingSpec<Object>) value;
		this.settingSpecMap.put(val.toString(), val);
		return value;
	}
	
	public Map<String, SettingSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.settingSpecMap);
	}
	
}
