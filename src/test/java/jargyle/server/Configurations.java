package jargyle.server;

import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.server.UnmodifiableConfiguration;
import jargyle.server.SettingSpec;
import jargyle.server.Settings;
import jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator;

public final class Configurations {

	public static Configuration newConfiguration() {
		UnmodifiableConfiguration.Builder builder = new UnmodifiableConfiguration.Builder();
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuth() {
		UnmodifiableConfiguration.Builder builder = new UnmodifiableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI))));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5GssapiAuthNecReferenceImpl() {
		UnmodifiableConfiguration.Builder builder = new UnmodifiableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.GSSAPI)),
				SettingSpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
		return builder.build();
	}
	
	public static Configuration newConfigurationUsingSocks5UsernamePasswordAuth() {
		UnmodifiableConfiguration.Builder builder = new UnmodifiableConfiguration.Builder();
		builder.settings(Settings.newInstance(
				SettingSpec.SOCKS5_AUTH_METHODS.newSetting(
						AuthMethods.newInstance(AuthMethod.USERNAME_PASSWORD))));
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame,");
		sb.append("Jasmine:mission%3Aimpossible,");
		sb.append("Abu:safeDriversSave40%25");
		builder.socks5UsernamePasswordAuthenticator(
				new StringSourceUsernamePasswordAuthenticator(sb.toString()));
		return builder.build();
	}
	
	private Configurations() { }
}
