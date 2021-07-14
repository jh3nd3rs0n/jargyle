package jargyle.net.socks.client.propertyspecfactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.PropertySpecFactory;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public final class PropertySpecFactoryImpl extends PropertySpecFactory {

	private static final Set<Class<?>> PROPERTY_SPEC_IMPL_CLASSES = new HashSet<Class<?>>(Arrays.asList(
			BooleanPropertySpec.class,
			CriteriaPropertySpec.class,
			EncryptedPasswordPropertySpec.class,
			FilePropertySpec.class,
			HostPropertySpec.class,
			MethodsPropertySpec.class,
			OidPropertySpec.class,
			PortPropertySpec.class,
			PositiveIntegerPropertySpec.class,
			ProtectionLevelsPropertySpec.class,
			SocketSettingsPropertySpec.class,
			StringPropertySpec.class,
			StringsPropertySpec.class,
			UserEncryptedPasswordPropertySpec.class,
			UsernamePropertySpec.class));
	
	@Override
	protected boolean canCreateNewInstanceOf(final Class<?> cls) {
		return PROPERTY_SPEC_IMPL_CLASSES.contains(cls);
	}
	
	@Override
	protected PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal) {
		return new BooleanPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal) {
		return new FilePropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal) {
		return new HostPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal) {
		return new MethodsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal) {
		return new OidPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal) {
		return new PortPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal) {
		return new StringPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal) {
		return new StringsPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new UserEncryptedPasswordPropertySpec(s, defaultVal);
	}

	@Override
	protected PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal) {
		return new UsernamePropertySpec(s, defaultVal);
	}

}
