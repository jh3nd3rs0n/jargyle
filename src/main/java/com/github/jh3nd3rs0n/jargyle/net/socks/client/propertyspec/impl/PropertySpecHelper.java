package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import java.io.File;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class PropertySpecHelper {
	
	public static PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal) {
		return new BooleanPropertySpec(s, defaultVal);
	}

	public static PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaPropertySpec(s, defaultVal);
	}

	public static PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordPropertySpec(s, defaultVal);
	}

	public static PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal) {
		return new FilePropertySpec(s, defaultVal);
	}

	public static PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal) {
		return new HostPropertySpec(s, defaultVal);
	}

	public static PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal) {
		return new MethodsPropertySpec(s, defaultVal);
	}

	public static PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal) {
		return new OidPropertySpec(s, defaultVal);
	}

	public static PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal) {
		return new PortPropertySpec(s, defaultVal);
	}

	public static PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerPropertySpec(s, defaultVal);
	}

	public static PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsPropertySpec(s, defaultVal);
	}

	public static PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsPropertySpec(s, defaultVal);
	}

	public static PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal) {
		return new StringPropertySpec(s, defaultVal);
	}

	public static PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal) {
		return new StringsPropertySpec(s, defaultVal);
	}

	public static PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new UserEncryptedPasswordPropertySpec(s, defaultVal);
	}

	public static PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal) {
		return new UsernamePropertySpec(s, defaultVal);
	}
	
	private PropertySpecHelper() { }
	
}
