package com.github.jh3nd3rs0n.jargyle.server.internal.config.impl;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationRepository;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

public final class UpdatedConfiguration extends Configuration {
	
	public static UpdatedConfiguration newInstance(
			final ConfigurationRepository repository) {
		return new UpdatedConfiguration(repository);
	}
	
	private final ConfigurationRepository configurationRepository;
	
	private UpdatedConfiguration(final ConfigurationRepository repository) {
		this.configurationRepository = repository;
	}
	
	@Override
	public void addSetting(
			final int index, final Setting<? extends Object> sttng) {
		Configuration configuration = Configuration.newModifiableInstance(
				this.configurationRepository.get());
		configuration.addSetting(index, sttng);
		this.configurationRepository.set(configuration);
	}

	@Override
	public void addSetting(final Setting<? extends Object> sttng) {
		Configuration configuration = Configuration.newModifiableInstance(
				this.configurationRepository.get());
		configuration.addSetting(sttng);
		this.configurationRepository.set(configuration);
	}

	@Override
	public void addSettings(final int index, final Settings sttngs) {
		Configuration configuration = Configuration.newModifiableInstance(
				this.configurationRepository.get());
		configuration.addSettings(index, sttngs);
		this.configurationRepository.set(configuration);
	}

	@Override
	public void addSettings(final Settings sttngs) {
		Configuration configuration = Configuration.newModifiableInstance(
				this.configurationRepository.get());
		configuration.addSettings(sttngs);
		this.configurationRepository.set(configuration);
	}

	@Override
	public int getSettingCount() {
		return this.configurationRepository.get().getSettingCount();
	}

	@Override
	public Settings getSettings() {
		return this.configurationRepository.get().getSettings();
	}

	@Override
	public Setting<Object> removeSetting(final int index) {
		Configuration configuration = Configuration.newModifiableInstance(
				this.configurationRepository.get());
		Setting<Object> setting = configuration.removeSetting(index);
		this.configurationRepository.set(configuration);
		return setting;
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
