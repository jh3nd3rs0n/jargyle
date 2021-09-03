package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Method;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;

public final class SocksClientHelper {

	public static SocksClient newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance());
	}
	
	public static SocksClient newSocks5Client(
			final String host, 
			final Integer port, 
			final UsernamePassword usernamePassword) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
						usernamePassword.getUsername()),
				Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
						usernamePassword.getEncryptedPassword()));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private SocksClientHelper() { }
	
}
