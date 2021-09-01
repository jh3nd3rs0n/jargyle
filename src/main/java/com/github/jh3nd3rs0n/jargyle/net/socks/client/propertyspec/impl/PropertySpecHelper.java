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
			final Object permissionObj, 
			final String s, 
			final Boolean defaultVal) {
		return new BooleanPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Criteria> newCriteriaPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Criteria defaultVal) {
		return new CriteriaPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final Object permissionObj, 
			final String s, 
			final EncryptedPassword defaultVal) {
		return new EncryptedPasswordPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<File> newFilePropertySpec(
			final Object permissionObj, 
			final String s, 
			final File defaultVal) {
		return new FilePropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Host> newHostPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Host defaultVal) {
		return new HostPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Methods> newMethodsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Methods defaultVal) {
		return new MethodsPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Oid> newOidPropertySpec(
			final Object permissionObj, 
			final String s, 
			final String defaultVal) {
		return new OidPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Port> newPortPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Port defaultVal) {
		return new PortPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final Object permissionObj, 
			final String s, 
			final PositiveInteger defaultVal) {
		return new PositiveIntegerPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final ProtectionLevels defaultVal) {
		return new ProtectionLevelsPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final SocketSettings defaultVal) {
		return new SocketSettingsPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<String> newStringPropertySpec(
			final Object permissionObj, 
			final String s, 
			final String defaultVal) {
		return new StringPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<Strings> newStringsPropertySpec(
			final Object permissionObj, 
			final String s, 
			final Strings defaultVal) {
		return new StringsPropertySpec(permissionObj, s, defaultVal);
	}

	public static PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final Object permissionObj, 
			final String s, 
			final EncryptedPassword defaultVal) {
		return new UserEncryptedPasswordPropertySpec(
				permissionObj, s, defaultVal);
	}

	public static PropertySpec<String> newUsernamePropertySpec(
			final Object permissionObj, 
			final String s, 
			final String defaultVal) {
		return new UsernamePropertySpec(permissionObj, s, defaultVal);
	}
	
	private PropertySpecHelper() { }
	
}
