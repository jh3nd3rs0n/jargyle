package jargyle.net.socks.server.settingspec.impl;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.Socks5RequestCriteria;
import jargyle.net.socks.server.v5.Socks5RequestWorkerFactory;
import jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.NonnegativeInteger;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public final class SettingSpecFactory {
	
	public SettingSpec<Boolean> newBooleanSettingSpec(
			final String s, final Boolean defaultVal) {
		return new BooleanSettingSpec(s, defaultVal);
	}

	public SettingSpec<Criteria> newCriteriaSettingSpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaSettingSpec(s, defaultVal);
	}

	public SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordSettingSpec(s, defaultVal);
	}

	public SettingSpec<File> newFileSettingSpec(
			final String s, final File defaultVal) {
		return new FileSettingSpec(s, defaultVal);
	}

	public SettingSpec<Host> newHostSettingSpec(
			final String s, final Host defaultVal) {
		return new HostSettingSpec(s, defaultVal);
	}

	public SettingSpec<Methods> newMethodsSettingSpec(
			final String s, final Methods defaultVal) {
		return new MethodsSettingSpec(s, defaultVal);
	}

	public SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		return new NonnegativeIntegerSettingSpec(s, defaultVal);
	}

	public SettingSpec<Oid> newOidSettingSpec(
			final String s, final Oid defaultVal) {
		return new OidSettingSpec(s, defaultVal);
	}

	public SettingSpec<Port> newPortSettingSpec(
			final String s, final Port defaultVal) {
		return new PortSettingSpec(s, defaultVal);
	}

	public SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerSettingSpec(s, defaultVal);
	}

	public SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsSettingSpec(s, defaultVal);
	}

	public SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsSettingSpec(s, defaultVal);
	}

	public SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		return new Socks5RequestCriteriaSettingSpec(s, defaultVal);
	}

	public SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal) {
		return new Socks5RequestWorkerFactorySettingSpec(s, defaultVal);
	}

	public SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		return new SocksServerUriSettingSpec(s, defaultVal);
	}

	public SettingSpec<String> newStringSettingSpec(
			final String s, final String defaultVal) {
		return new StringSettingSpec(s, defaultVal);
	}

	public SettingSpec<Strings> newStringsSettingSpec(
			final String s, final Strings defaultVal) {
		return new StringsSettingSpec(s, defaultVal);
	}

	public SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		return new UsernamePasswordAuthenticatorSettingSpec(s, defaultVal);
	}

	public SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		return new UsernamePasswordSettingSpec(s, defaultVal);
	}

}
