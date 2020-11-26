package jargyle.server;

import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import jargyle.client.Properties;
import jargyle.client.Property;
import jargyle.client.PropertySpec;
import jargyle.client.SocksClient;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.Socks5ServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.Host;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.util.PositiveInteger;

public final class SocksClientFactory {
	
	private static List<Property> getSocks5ClientProperties(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		List<Property> properties = new ArrayList<Property>();
		AuthMethods authMethods = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_AUTH_METHODS, 
				AuthMethods.class);
		properties.add(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(authMethods));
		Oid gssapiMechanismOid = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_MECHANISM_OID, 
				Oid.class);
		properties.add(PropertySpec.SOCKS5_GSSAPI_MECHANISM_OID.newProperty(
				gssapiMechanismOid));
		Boolean gssapiNecReferenceImpl = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL,
				Boolean.class);
		properties.add(
				PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
						gssapiNecReferenceImpl));
		GssapiProtectionLevels gssapiProtectionLevels = 
				settings.getLastValue(
						SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_PROTECTION_LEVELS, 
						GssapiProtectionLevels.class);
		properties.add(
				PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
						gssapiProtectionLevels));
		String gssapiServiceName = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_GSSAPI_SERVICE_NAME, 
				String.class);
		properties.add(PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
				gssapiServiceName));
		UsernamePassword usernamePassword = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKS5_USERNAME_PASSWORD, 
				UsernamePassword.class);
		if (usernamePassword != null) {
			properties.add(PropertySpec.SOCKS5_USERNAME.newProperty(
					usernamePassword.getUsername()));
			properties.add(PropertySpec.SOCKS5_PASSWORD.newProperty(
					usernamePassword.getEncryptedPassword().getPassword()));			
		}
		return properties;
	}
	
	private static List<Property> getSocksClientProperties(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		List<Property> properties = new ArrayList<Property>();
		Host bindHost = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_BIND_HOST, Host.class);
		properties.add(PropertySpec.BIND_HOST.newProperty(bindHost));
		PositiveInteger connectTimeout = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_CONNECT_TIMEOUT, 
				PositiveInteger.class);
		properties.add(PropertySpec.CONNECT_TIMEOUT.newProperty(connectTimeout));
		SocketSettings socketSettings = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_SOCKET_SETTINGS, 
				SocketSettings.class);
		properties.add(PropertySpec.SOCKET_SETTINGS.newProperty(socketSettings));
		return properties;
	}
	
	public static SocksClient newSocksClient(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		SocksServerUri socksServerUri = settings.getLastValue(
				SettingSpec.EXTERNAL_CLIENT_EXTERNAL_SERVER_URI, 
				SocksServerUri.class);
		if (socksServerUri == null) {
			return null;
		}
		List<Property> properties = new ArrayList<Property>();
		properties.addAll(getSocksClientProperties(configuration));
		SocksClient socksClient = null;
		if (socksServerUri instanceof Socks5ServerUri) {
			properties.addAll(getSocks5ClientProperties(configuration));
			Socks5ServerUri socks5ServerUri = (Socks5ServerUri) socksServerUri;
			socksClient = socks5ServerUri.newSocksClient(Properties.newInstance(
					properties));
		} else {
			throw new AssertionError(String.format(
					"unhandled %s: %s", 
					SocksServerUri.class.getSimpleName(), 
					socksServerUri.getClass().getSimpleName()));
		}
		return socksClient;
	}
	
	private SocksClientFactory() { }
	
}
