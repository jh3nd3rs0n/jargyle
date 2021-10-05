package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class SettingSpecs {

	private final List<SettingSpec<Object>> settingSpecs;
	
	public SettingSpecs() {
		this.settingSpecs = new ArrayList<SettingSpec<Object>>();
	}
	
	public <T> SettingSpec<T> addThenGet(final SettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SettingSpec<Object> val = (SettingSpec<Object>) value;
		this.settingSpecs.add(val);
		return value;
	}
	
	public List<SettingSpec<Object>> toList() {
		return Collections.unmodifiableList(this.settingSpecs);
	}
	
}
