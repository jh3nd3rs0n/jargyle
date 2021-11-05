package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.List;

public final class ClientRules extends Rules<ClientRule> {

	private static final ClientRules DEFAULT_INSTANCE = new ClientRules(
			Arrays.asList(ClientRule.getDefault()));
	
	public static ClientRules getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static ClientRules newInstance(final ClientRule... clientRls) {
		return newInstance(Arrays.asList(clientRls));
	}
	
	public static ClientRules newInstance(final List<ClientRule> clientRls) {
		return new ClientRules(clientRls);
	}
	
	public static ClientRules newInstance(final String s) {
		return newInstance(ClientRule.newInstances(s));
	}
	
	private ClientRules(final List<ClientRule> rls) {
		super(rls);
	}
	
	public ClientRule anyAppliesTo(
			final String clientAddress, final String socksServerAddress) {
		for (ClientRule clientRule : this.toList()) {
			if (clientRule.appliesTo(clientAddress, socksServerAddress)) {
				return clientRule;
			}
		}
		throw new IllegalArgumentException(String.format(
				"client address %s to SOCKS server address %s does not apply "
				+ "to any rule",
				clientAddress,
				socksServerAddress));
	}
	
}
