package jargyle.net.socks.client;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class PropertySpecFactory {
	
	protected abstract PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal);
	
	protected abstract PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal);
	
	protected abstract PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal);
	
	protected abstract PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal);
	
	protected abstract PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal);
	
	protected abstract PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal);
	
	protected abstract PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal);
	
	protected abstract PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal);
	
	protected abstract PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal);
	
	protected abstract PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal);
	
	protected abstract PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal);
	
	protected abstract PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal);
	
	protected abstract PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal);
	
	protected abstract PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal);
	
	protected abstract PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal);
	
}
