package jargyle.net.socks.server;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.settingspecfactory.SettingSpecFactory;
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

final class SettingSpecFactoryImpl extends SettingSpecFactory {

	public static final SettingSpecFactoryImpl INSTANCE = 
			new SettingSpecFactoryImpl();
	
	@Override
	protected boolean canCreateNewInstanceOf(final Class<?> cls) {
		return super.canCreateNewInstanceOf(cls);
	}

	@Override
	protected SettingSpec<Boolean> newBooleanSettingSpec(
			final String s, final Boolean defaultVal) {
		return super.newBooleanSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Criteria> newCriteriaSettingSpec(
			final String s, final Criteria defaultVal) {
		return super.newCriteriaSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		return super.newEncryptedPasswordSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<File> newFileSettingSpec(
			final String s, final File defaultVal) {
		return super.newFileSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Host> newHostSettingSpec(
			final String s, final Host defaultVal) {
		return super.newHostSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Methods> newMethodsSettingSpec(
			final String s, final Methods defaultVal) {
		return super.newMethodsSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		return super.newNonnegativeIntegerSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Oid> newOidSettingSpec(
			final String s, final Oid defaultVal) {
		return super.newOidSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Port> newPortSettingSpec(
			final String s, final Port defaultVal) {
		return super.newPortSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		return super.newPositiveIntegerSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal) {
		return super.newProtectionLevelsSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		return super.newSocketSettingsSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		return super.newSocks5RequestCriteriaSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal) {
		return super.newSocks5RequestWorkerFactorySettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		return super.newSocksServerUriSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<String> newStringSettingSpec(
			final String s, final String defaultVal) {
		return super.newStringSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<Strings> newStringsSettingSpec(
			final String s, final Strings defaultVal) {
		return super.newStringsSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		return super.newUsernamePasswordAuthenticatorSettingSpec(s, defaultVal);
	}

	@Override
	protected SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		return super.newUsernamePasswordSettingSpec(s, defaultVal);
	}

}
