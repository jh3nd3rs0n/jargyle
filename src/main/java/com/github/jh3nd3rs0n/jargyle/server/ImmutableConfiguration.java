package com.github.jh3nd3rs0n.jargyle.server;

public final class ImmutableConfiguration extends Configuration {

	public static ImmutableConfiguration newInstance(
			final Configuration config) {
		return newInstance(config.getSettings());
	}
	
	public static ImmutableConfiguration newInstance(final Settings settings) {
		return new ImmutableConfiguration(settings);
	}
	
	private final Settings settings;
	
	private ImmutableConfiguration(final Settings sttngs) {
		this.settings = sttngs;
	}
	
	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.getEmptyInstance();
		}
		return this.settings;
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
