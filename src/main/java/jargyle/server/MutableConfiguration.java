package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class MutableConfiguration implements Configuration {
	
	private final ConfigurationService configurationService;
	
	public MutableConfiguration(final ConfigurationService service) {
		this.configurationService = service;
	}
	
	@Override
	public Expressions getAllowedClientAddresses() {
		return this.configurationService.getConfiguration().getAllowedClientAddresses();
	}

	@Override
	public Expressions getBlockedClientAddresses() {
		return this.configurationService.getConfiguration().getBlockedClientAddresses();
	}

	@Override
	public UsernamePassword getExternalClientSocks5UsernamePassword() {
		return this.configurationService.getConfiguration().getExternalClientSocks5UsernamePassword();
	}

	@Override
	public Settings getSettings() {
		return this.configurationService.getConfiguration().getSettings();
	}

	@Override
	public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
		return this.configurationService.getConfiguration().getSocks5UsernamePasswordAuthenticator();
	}
	
	@Override
	public String toString() {
		return this.configurationService.getConfiguration().toString();
	}

}
