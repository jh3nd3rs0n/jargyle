package jargyle.net.socks.client;

import jargyle.net.socks.client.v5.Socks5Client;
import jargyle.net.socks.client.v5.Socks5ServerUri;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;

public final class SocksClientHelper {

	public static Socks5Client newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance());
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port, 
			final UsernamePassword usernamePassword) {
		Properties properties = Properties.newInstance(
				PropertySpec.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
						usernamePassword.getUsername()),
				PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
						usernamePassword.getEncryptedPassword()));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private SocksClientHelper() { }
	
}
