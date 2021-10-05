package com.github.jh3nd3rs0n.jargyle.server;

import java.util.ArrayList;
import java.util.List;

final class ModifiableConfiguration extends Configuration {
	
	public static ModifiableConfiguration newInstance() {
		return new ModifiableConfiguration();
	}
	
	private final List<Setting<Object>> settings;
	
	private ModifiableConfiguration() {
		this.settings = new ArrayList<Setting<Object>>();
	}
	
	public void add(final Configuration configuration) {
		this.addSettings(configuration.getSettings());
	}

	public void addSetting(final Setting<? extends Object> sttng) {
		@SuppressWarnings("unchecked")
		Setting<Object> setting = (Setting<Object>) sttng;
		this.settings.add(setting);
	}
	
	public void addSettings(final Settings sttngs) {
		this.settings.addAll(sttngs.toList());
	}

	@Override
	public Settings getSettings() {
		List<Setting<? extends Object>> sttngs = 
				new ArrayList<Setting<? extends Object>>(this.settings);
		return Settings.newInstance(sttngs);
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