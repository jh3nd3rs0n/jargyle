package jargyle.net.socks.server.settingspecfactory;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

public abstract class SettingSpecFactory {

	private static SettingSpecFactory instance;
	
	private static final Set<Class<?>> SETTING_SPEC_IMPL_CLASSES = new HashSet<Class<?>>(Arrays.asList(
			BooleanSettingSpec.class,
			CriteriaSettingSpec.class,
			EncryptedPasswordSettingSpec.class,
			FileSettingSpec.class,
			HostSettingSpec.class,
			MethodsSettingSpec.class,
			NonnegativeIntegerSettingSpec.class,
			OidSettingSpec.class,
			PortSettingSpec.class,
			PositiveIntegerSettingSpec.class,
			ProtectionLevelsSettingSpec.class,
			SocketSettingsSettingSpec.class,
			Socks5RequestCriteriaSettingSpec.class,
			Socks5RequestWorkerFactorySettingSpec.class,
			SocksServerUriSettingSpec.class,
			StringSettingSpec.class,
			StringsSettingSpec.class,
			UsernamePasswordAuthenticatorSettingSpec.class,
			UsernamePasswordSettingSpec.class));
	
	public SettingSpecFactory() {
		if (instance != null) {
			throw new AssertionError(String.format(
					"there can only be one instance of %s", 
					SettingSpecFactory.class.getSimpleName()));
		}
		instance = this;
	}
	
	protected boolean canCreateNewInstanceOf(final Class<?> cls) {
		return SETTING_SPEC_IMPL_CLASSES.contains(cls);
	}
	
	protected SettingSpec<Boolean> newBooleanSettingSpec(
			final String s, final Boolean defaultVal) {
		return new BooleanSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Criteria> newCriteriaSettingSpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaSettingSpec(s, defaultVal);
	}

	protected SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordSettingSpec(s, defaultVal);
	}

	protected SettingSpec<File> newFileSettingSpec(
			final String s, final File defaultVal) {
		return new FileSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Host> newHostSettingSpec(
			final String s, final Host defaultVal) {
		return new HostSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Methods> newMethodsSettingSpec(
			final String s, final Methods defaultVal) {
		return new MethodsSettingSpec(s, defaultVal);
	}

	protected SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		return new NonnegativeIntegerSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Oid> newOidSettingSpec(
			final String s, final Oid defaultVal) {
		return new OidSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Port> newPortSettingSpec(
			final String s, final Port defaultVal) {
		return new PortSettingSpec(s, defaultVal);
	}

	protected SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerSettingSpec(s, defaultVal);
	}

	protected SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsSettingSpec(s, defaultVal);
	}

	protected SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		return new Socks5RequestCriteriaSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal) {
		return new Socks5RequestWorkerFactorySettingSpec(s, defaultVal);
	}

	protected SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		return new SocksServerUriSettingSpec(s, defaultVal);
	}

	protected SettingSpec<String> newStringSettingSpec(
			final String s, final String defaultVal) {
		return new StringSettingSpec(s, defaultVal);
	}

	protected SettingSpec<Strings> newStringsSettingSpec(
			final String s, final Strings defaultVal) {
		return new StringsSettingSpec(s, defaultVal);
	}

	protected SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		return new UsernamePasswordAuthenticatorSettingSpec(s, defaultVal);
	}

	protected SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		return new UsernamePasswordSettingSpec(s, defaultVal);
	}

}
