package jargyle.server.socks5;

import jargyle.client.socks5.Socks5Client;
import jargyle.client.socks5.Socks5ServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;

public final class Socks5Clients {

	public static Socks5Client newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClientBuilder().build();
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port, 
			final UsernamePassword usernamePassword) {
		return new Socks5ServerUri(host, port).newSocksClientBuilder()
				.authMethods(AuthMethods.newInstance(
						AuthMethod.USERNAME_PASSWORD))
				.usernamePassword(usernamePassword)
				.build();
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port,
			final String gssapiServiceName,
			final GssapiProtectionLevels gssapiProtectionLevels, 
			final boolean gssapiNecReferenceImpl) {
		return new Socks5ServerUri(host, port).newSocksClientBuilder()
				.authMethods(AuthMethods.newInstance(AuthMethod.GSSAPI))
				.gssapiServiceName(gssapiServiceName)
				.gssapiProtectionLevels(gssapiProtectionLevels)
				.gssapiNecReferenceImpl(gssapiNecReferenceImpl)
				.build();
	}
	
	private Socks5Clients() { }
	
}
