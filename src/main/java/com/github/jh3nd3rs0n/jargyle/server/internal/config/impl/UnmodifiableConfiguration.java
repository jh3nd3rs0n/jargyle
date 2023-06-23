package com.github.jh3nd3rs0n.jargyle.server.internal.config.impl;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

public final class UnmodifiableConfiguration extends Configuration {

	public static UnmodifiableConfiguration newInstance(
			final Configuration config) {
		return newInstance(config.getSettings());
	}
	
	public static UnmodifiableConfiguration newInstance(
			final Settings settings) {
		return new UnmodifiableConfiguration(settings);
	}
	
	private final Settings settings;
	
	private UnmodifiableConfiguration(final Settings sttngs) {
		this.settings = sttngs;
	}
	
	@Override
	public void addSetting(
			final int index, final Setting<? extends Object> sttng) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addSetting(final Setting<? extends Object> sttng) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addSettings(final int index, final Settings sttngs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addSettings(final Settings sttngs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSettingCount() {
		return this.getSettings().toList().size();
	}

	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.getEmptyInstance();
		}
		return this.settings;
	}

	@Override
	public Setting<Object> removeSetting(final int index) {
		throw new UnsupportedOperationException();
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
