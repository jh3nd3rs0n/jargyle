package jargyle.net.socks.client.propertyspec.impl;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public final class PropertySpecFactory {
	
	public PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal) {
		return new BooleanPropertySpec(s, defaultVal);
	}

	public PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaPropertySpec(s, defaultVal);
	}

	public PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordPropertySpec(s, defaultVal);
	}

	public PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal) {
		return new FilePropertySpec(s, defaultVal);
	}

	public PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal) {
		return new HostPropertySpec(s, defaultVal);
	}

	public PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal) {
		return new MethodsPropertySpec(s, defaultVal);
	}

	public PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal) {
		return new OidPropertySpec(s, defaultVal);
	}

	public PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal) {
		return new PortPropertySpec(s, defaultVal);
	}

	public PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerPropertySpec(s, defaultVal);
	}

	public PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsPropertySpec(s, defaultVal);
	}

	public PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsPropertySpec(s, defaultVal);
	}

	public PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal) {
		return new StringPropertySpec(s, defaultVal);
	}

	public PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal) {
		return new StringsPropertySpec(s, defaultVal);
	}

	public PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new UserEncryptedPasswordPropertySpec(s, defaultVal);
	}

	public PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal) {
		return new UsernamePropertySpec(s, defaultVal);
	}
	
}
