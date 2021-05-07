package jargyle.net.socks.server;

public final class MutableConfiguration extends Configuration {
	
	public static MutableConfiguration newInstance(
			final ConfigurationProvider provider) {
		return new MutableConfiguration(provider);
	}
	
	private final ConfigurationProvider configurationProvider;
	
	private MutableConfiguration(final ConfigurationProvider provider) {
		this.configurationProvider = provider;
	}
	
	@Override
	public Settings getSettings() {
		return this.configurationProvider.getConfiguration().getSettings();
	}

	@Override
	public String toString() {
		return this.configurationProvider.getConfiguration().toString();
	}

}
