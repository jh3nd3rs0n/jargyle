package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestRules;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class MutableConfiguration implements Configuration {
	
	private final ConfigurationService configurationService;
	
	public MutableConfiguration(final ConfigurationService service) {
		this.configurationService = service;
	}
	
	@Override
	public Expressions getAllowedClientAddressExpressions() {
		return this.configurationService.getConfiguration().getAllowedClientAddressExpressions();
	}

	@Override
	public Socks5RequestRules getAllowedSocks5RequestRules() {
		return this.configurationService.getConfiguration().getAllowedSocks5RequestRules();
	}

	@Override
	public Expressions getBlockedClientAddressExpressions() {
		return this.configurationService.getConfiguration().getBlockedClientAddressExpressions();
	}

	@Override
	public Socks5RequestRules getBlockedSocks5RequestRules() {
		return this.configurationService.getConfiguration().getBlockedSocks5RequestRules();
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
