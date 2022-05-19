package com.github.jh3nd3rs0n.jargyle.server;

public final class MutableConfiguration extends Configuration {
	
	public static MutableConfiguration newInstance(
			final ConfigurationRepository repository) {
		return new MutableConfiguration(repository);
	}
	
	private final ConfigurationRepository configurationRepository;
	
	private MutableConfiguration(final ConfigurationRepository repository) {
		this.configurationRepository = repository;
	}
	
	@Override
	public Settings getSettings() {
		return this.configurationRepository.get().getSettings();
	}

	@Override
	public String toString() {
		return this.configurationRepository.get().toString();
	}

}
