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
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class PropertySpecFactory {

	private static PropertySpecFactory instance;
	
	private static void setInstance(final PropertySpecFactory factory) {
		if (instance != null) {
			throw new AssertionError(String.format(
					"there can only be one instance of %s", 
					PropertySpecFactory.class.getSimpleName()));
		}
		instance = factory;
	}
	
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
	
	public PropertySpecFactory() {
		setInstance(this);
	}
	
	protected boolean canCreateNewInstanceOf(final Class<?> cls) {
		return PROPERTY_SPEC_IMPL_CLASSES.contains(cls);
	}
	
	protected PropertySpec<Boolean> newBooleanPropertySpec(
			final String s, final Boolean defaultVal) {
		return new BooleanPropertySpec(s, defaultVal);
	}

	protected PropertySpec<Criteria> newCriteriaPropertySpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaPropertySpec(s, defaultVal);
	}

	protected PropertySpec<EncryptedPassword> newEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordPropertySpec(s, defaultVal);
	}

	protected PropertySpec<File> newFilePropertySpec(
			final String s, final File defaultVal) {
		return new FilePropertySpec(s, defaultVal);
	}

	protected PropertySpec<Host> newHostPropertySpec(
			final String s, final Host defaultVal) {
		return new HostPropertySpec(s, defaultVal);
	}

	protected PropertySpec<Methods> newMethodsPropertySpec(
			final String s, final Methods defaultVal) {
		return new MethodsPropertySpec(s, defaultVal);
	}

	protected PropertySpec<Oid> newOidPropertySpec(
			final String s, final String defaultVal) {
		return new OidPropertySpec(s, defaultVal);
	}

	protected PropertySpec<Port> newPortPropertySpec(
			final String s, final Port defaultVal) {
		return new PortPropertySpec(s, defaultVal);
	}

	protected PropertySpec<PositiveInteger> newPositiveIntegerPropertySpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerPropertySpec(s, defaultVal);
	}

	protected PropertySpec<ProtectionLevels> newProtectionLevelsPropertySpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsPropertySpec(s, defaultVal);
	}

	protected PropertySpec<SocketSettings> newSocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsPropertySpec(s, defaultVal);
	}

	protected PropertySpec<String> newStringPropertySpec(
			final String s, final String defaultVal) {
		return new StringPropertySpec(s, defaultVal);
	}

	protected PropertySpec<Strings> newStringsPropertySpec(
			final String s, final Strings defaultVal) {
		return new StringsPropertySpec(s, defaultVal);
	}

	protected PropertySpec<EncryptedPassword> newUserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		return new UserEncryptedPasswordPropertySpec(s, defaultVal);
	}

	protected PropertySpec<String> newUsernamePropertySpec(
			final String s, final String defaultVal) {
		return new UsernamePropertySpec(s, defaultVal);
	}
	
}
