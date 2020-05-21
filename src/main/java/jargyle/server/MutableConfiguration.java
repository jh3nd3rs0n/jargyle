package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class MutableConfiguration implements Configuration {
	
	private final ConfigurationService configurationService;
	
	public MutableConfiguration(final ConfigurationService service) {
		this.configurationService = service;
	}
	
	@Override
	public Criteria getAllowedClientAddressCriteria() {
		return this.configurationService.getConfiguration().getAllowedClientAddressCriteria();
	}

	@Override
	public Criteria getAllowedIncomingTcpAddressCriteria() {
		return this.configurationService.getConfiguration().getAllowedIncomingTcpAddressCriteria();
	}

	@Override
	public Criteria getAllowedIncomingUdpAddressCriteria() {
		return this.configurationService.getConfiguration().getAllowedIncomingUdpAddressCriteria();
	}

	@Override
	public Socks5RequestCriteria getAllowedSocks5RequestCriteria() {
		return this.configurationService.getConfiguration().getAllowedSocks5RequestCriteria();
	}

	@Override
	public Criteria getBlockedClientAddressCriteria() {
		return this.configurationService.getConfiguration().getBlockedClientAddressCriteria();
	}
	
	@Override
	public Criteria getBlockedIncomingTcpAddressCriteria() {
		return this.configurationService.getConfiguration().getBlockedIncomingTcpAddressCriteria();
	}

	@Override
	public Criteria getBlockedIncomingUdpAddressCriteria() {
		return this.configurationService.getConfiguration().getBlockedIncomingUdpAddressCriteria();
	}

	@Override
	public Socks5RequestCriteria getBlockedSocks5RequestCriteria() {
		return this.configurationService.getConfiguration().getBlockedSocks5RequestCriteria();
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
