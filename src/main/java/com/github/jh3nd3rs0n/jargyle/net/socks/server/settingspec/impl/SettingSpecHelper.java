package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import java.io.File;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5RequestCriteria;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class SettingSpecHelper {
	
	public static SettingSpec<Boolean> newBooleanSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Boolean defaultVal) {
		return new BooleanSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Criteria> newCriteriaSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Criteria defaultVal) {
		return new CriteriaSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final Object permissionObj, 
			final String s, 
			final EncryptedPassword defaultVal) {
		return new EncryptedPasswordSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<File> newFileSettingSpec(
			final Object permissionObj, 
			final String s, 
			final File defaultVal) {
		return new FileSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Host> newHostSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Host defaultVal) {
		return new HostSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Methods> newMethodsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Methods defaultVal) {
		return new MethodsSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final Object permissionObj, 
			final String s, 
			final NonnegativeInteger defaultVal) {
		return new NonnegativeIntegerSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Oid> newOidSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Oid defaultVal) {
		return new OidSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Port> newPortSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Port defaultVal) {
		return new PortSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final Object permissionObj, 
			final String s, 
			final PositiveInteger defaultVal) {
		return new PositiveIntegerSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final ProtectionLevels defaultVal) {
		return new ProtectionLevelsSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final SocketSettings defaultVal) {
		return new SocketSettingsSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Socks5RequestCriteria defaultVal) {
		return new Socks5RequestCriteriaSettingSpec(
				permissionObj, s, defaultVal);
	}

	public static SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final Object permissionObj, 
			final String s, 
			final Socks5RequestWorkerFactory defaultVal) {
		return new Socks5RequestWorkerFactorySettingSpec(
				permissionObj, s, defaultVal);
	}

	public static SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final Object permissionObj, 
			final String s, 
			final SocksServerUri defaultVal) {
		return new SocksServerUriSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<String> newStringSettingSpec(
			final Object permissionObj, 
			final String s, 
			final String defaultVal) {
		return new StringSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<Strings> newStringsSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Strings defaultVal) {
		return new StringsSettingSpec(permissionObj, s, defaultVal);
	}

	public static SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final Object permissionObj, 
			final String s, 
			final UsernamePasswordAuthenticator defaultVal) {
		return new UsernamePasswordAuthenticatorSettingSpec(
				permissionObj, s, defaultVal);
	}

	public static SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final Object permissionObj, 
			final String s, 
			final UsernamePassword defaultVal) {
		return new UsernamePasswordSettingSpec(permissionObj, s, defaultVal);
	}
	
	private SettingSpecHelper() { }

}
