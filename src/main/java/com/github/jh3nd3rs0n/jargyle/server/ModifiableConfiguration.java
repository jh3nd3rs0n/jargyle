package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.List;

public final class ModifiableConfiguration extends Configuration {
	
	public static ModifiableConfiguration newInstance() {
		return new ModifiableConfiguration();
	}
	
	private final List<Setting<Object>> settings;
	
	private ModifiableConfiguration() {
		this.settings = new ArrayList<Setting<Object>>();
	}
	
	public void addSetting(
			final int index, final Setting<? extends Object> sttng) {
		@SuppressWarnings("unchecked")
		Setting<Object> setting = (Setting<Object>) sttng;
		this.settings.add(index, setting);
	}
	
	public void addSetting(final Setting<? extends Object> sttng) {
		@SuppressWarnings("unchecked")
		Setting<Object> setting = (Setting<Object>) sttng;
		this.settings.add(setting);
	}
	
	public void addSettings(final int index, final Settings sttngs) {
		this.settings.addAll(index, sttngs.toList());
	}
	
	public void addSettings(final Settings sttngs) {
		this.settings.addAll(sttngs.toList());
	}
	
	public int getSettingCount() {
		return this.settings.size();
	}
	
	@Override
	public Settings getSettings() {
		List<Setting<? extends Object>> sttngs = 
				new ArrayList<Setting<? extends Object>>(this.settings);
		return Settings.newInstance(sttngs);
	}

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