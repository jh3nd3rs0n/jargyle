package jargyle.net.socks.server;

public final class MutableConfiguration extends Configuration {
	
	public static MutableConfiguration newInstance(
			final ConfigurationService service) {
		return new MutableConfiguration(service);
	}
	
	private final ConfigurationService configurationService;
	
	private MutableConfiguration(final ConfigurationService service) {
		this.configurationService = service;
	}
	
	@Override
	public Settings getSettings() {
		return this.configurationService.getConfiguration().getSettings();
	}

	@Override
	public String toString() {
		return this.configurationService.getConfiguration().toString();
	}

}
