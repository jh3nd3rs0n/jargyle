package jargyle.server;

import jargyle.ResourceHelper;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator;

public final class ConfigurationFactory {

	public static Configuration newConfiguration() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuth() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI))));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuthNecReferenceImpl() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI)),
				SettingSpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5UsernamePasswordAuth() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.USERNAME_PASSWORD)),
				SettingSpec.SOCKS5_USERNAME_PASSWORD_AUTHENTICATOR.newSetting(
						new StringSourceUsernamePasswordAuthenticator(sb.toString()))));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSsl() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getServerKeyStoreFile()),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSetting(
						ResourceHelper.getServerKeyStorePassword())));
		return builder.build();
	}
	
	private ConfigurationFactory() { }
}
