package jargyle.server;

public final class MutableConfiguration extends Configuration {
	
	private final ConfigurationService configurationService;
	
	public MutableConfiguration(final ConfigurationService service) {
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
