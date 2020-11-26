package jargyle.server.socks5;

import java.util.ArrayList;
import java.util.List;

import jargyle.client.Properties;
import jargyle.client.Property;
import jargyle.client.PropertySpec;
import jargyle.client.socks5.Socks5Client;
import jargyle.client.socks5.Socks5ServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;

public final class Socks5ClientFactory {

	public static Socks5Client newSocks5Client(
			final String host, final Integer port) {
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance());
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port, 
			final UsernamePassword usernamePassword) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
				AuthMethods.newInstance(AuthMethod.USERNAME_PASSWORD)));
		properties.add(PropertySpec.SOCKS5_USERNAME.newProperty(
				usernamePassword.getUsername()));
		properties.add(PropertySpec.SOCKS5_PASSWORD.newProperty(
				usernamePassword.getEncryptedPassword().getPassword()));
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance(properties));
	}
	
	public static Socks5Client newSocks5Client(
			final String host, 
			final Integer port,
			final String gssapiServiceName,
			final GssapiProtectionLevels gssapiProtectionLevels, 
			final boolean gssapiNecReferenceImpl) {
		List<Property> properties = new ArrayList<Property>();
		properties.add(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
				AuthMethods.newInstance(AuthMethod.GSSAPI)));
		properties.add(PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
				gssapiServiceName));
		properties.add(PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
				gssapiProtectionLevels));
		properties.add(PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
				Boolean.valueOf(gssapiNecReferenceImpl)));
		return new Socks5ServerUri(host, port).newSocksClient(
				Properties.newInstance(properties));
	}
	
	private Socks5ClientFactory() { }
	
}
