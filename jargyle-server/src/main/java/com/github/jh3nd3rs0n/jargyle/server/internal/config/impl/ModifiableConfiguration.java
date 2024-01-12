package com.github.jh3nd3rs0n.jargyle.server.internal.config.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

public final class ModifiableConfiguration extends Configuration {
	
	public static ModifiableConfiguration newInstance() {
		return newInstance(Settings.getEmptyInstance());
	}
	
	public static ModifiableConfiguration newInstance(
			final Configuration config) {
		return newInstance(config.getSettings());
	}
	
	public static ModifiableConfiguration newInstance(
			final Settings settings) {
		return new ModifiableConfiguration(settings);
	}
	
	private final List<Setting<Object>> settings;
	
	private ModifiableConfiguration(final Settings sttngs) {
		this.settings = new ArrayList<Setting<Object>>(sttngs.toList());
	}
	
	@Override
	public void addSetting(
			final int index, final Setting<? extends Object> sttng) {
		@SuppressWarnings("unchecked")
		Setting<Object> setting = (Setting<Object>) sttng;
		this.settings.add(index, setting);
	}
	
	@Override
	public void addSetting(final Setting<? extends Object> sttng) {
		@SuppressWarnings("unchecked")
		Setting<Object> setting = (Setting<Object>) sttng;
		this.settings.add(setting);
	}
	
	@Override
	public void addSettings(final int index, final Settings sttngs) {
		this.settings.addAll(index, sttngs.toList());
	}
	
	@Override
	public void addSettings(final Settings sttngs) {
		this.settings.addAll(sttngs.toList());
	}
	
	@Override
	public int getSettingCount() {
		return this.settings.size();
	}
	
	@Override
	public Settings getSettings() {
		List<Setting<? extends Object>> sttngs = 
				new ArrayList<Setting<? extends Object>>(this.settings);
		return Settings.of(sttngs);
	}

	@Override
	public Setting<Object> removeSetting(final int index) {
		return this.settings.remove(index);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [settings=")
			.append(this.getSettings())
			.append("]");
		return builder.toString();
	}
	
}