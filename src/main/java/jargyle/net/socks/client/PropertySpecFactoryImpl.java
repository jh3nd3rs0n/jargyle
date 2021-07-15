package jargyle.net.socks.client;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.propertyspecfactory.PropertySpecFactory;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

final class PropertySpecFactoryImpl extends PropertySpecFactory {

	private static final PropertySpecFactoryImpl INSTANCE = 
			new PropertySpecFactoryImpl();
	
	public static PropertySpecFactoryImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	protected boolean canCreateNewInstanceOf(final Class<?> cls) {
		return super.canCreateNewInstanceOf(cls);
	}

	@Override
	protected PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal) {
		return super.newBooleanPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal) {
		return super.newCriteriaPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return super.newEncryptedPasswordPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal) {
		return super.newFilePropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal) {
		return super.newHostPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal) {
		return super.newMethodsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal) {
		return super.newOidPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal) {
		return super.newPortPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		return super.newPositiveIntegerPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		return super.newProtectionLevelsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		return super.newSocketSettingsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal) {
		return super.newStringPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal) {
		return super.newStringsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return super.newUserEncryptedPasswordPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal) {
		return super.newUsernamePropertySpec(s, defaultVal);
	}

}
