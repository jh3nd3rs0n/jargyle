package jargyle.net.socks.server;

import jargyle.net.socks.server.v5.userpassauth.StringSourceUsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;

public final class ConfigurationHelper {

	public static Configuration newConfiguration() {
		return ImmutableConfiguration.newInstance((Settings) null);
	}
	
	public static Configuration newConfigurationUsingSocks5UserpassAuth() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				SettingSpec.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
						new StringSourceUsernamePasswordAuthenticator(sb.toString()))));
	}
	
	private ConfigurationHelper() { }
}
