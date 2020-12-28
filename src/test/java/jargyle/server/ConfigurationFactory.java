package jargyle.server;

import jargyle.ResourceHelper;
import jargyle.ResourceNameConstants;
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
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSetting(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.SERVER_KEY_STORE_PASSWORD_FILE))));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSslAndRequestedClientAuth() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSetting(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSetting(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.CLIENT_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_WANT_CLIENT_AUTH.newSetting(Boolean.TRUE)));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSslAndRequiredClientAuth() {
		ImmutableConfiguration.Builder builder = new ImmutableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SSL_ENABLED.newSetting(Boolean.TRUE),
				SettingSpec.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.SERVER_KEY_STORE_FILE)),
				SettingSpec.SSL_KEY_STORE_PASSWORD.newSetting(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.SERVER_KEY_STORE_PASSWORD_FILE)),
				SettingSpec.SSL_NEED_CLIENT_AUTH.newSetting(Boolean.TRUE),
				SettingSpec.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.CLIENT_KEY_STORE_FILE)),
				SettingSpec.SSL_TRUST_STORE_PASSWORD.newSetting(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.CLIENT_KEY_STORE_PASSWORD_FILE))));
		return builder.build();
	}
	
	private ConfigurationFactory() { }
}
