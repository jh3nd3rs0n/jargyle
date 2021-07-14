package jargyle.net.socks.server;

import java.io.File;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
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

	protected abstract SettingSpec<Boolean> newBooleanSettingSpec(
			final String s, final Boolean defaultVal);
	
	protected abstract SettingSpec<Criteria> newCriteriaSettingSpec(
			final String s, final Criteria defaultVal);
	
	protected abstract SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal);
	
	protected abstract SettingSpec<File> newFileSettingSpec(
			final String s, final File defaultVal);
	
	protected abstract SettingSpec<Host> newHostSettingSpec(
			final String s, final Host defaultVal);
	
	protected abstract SettingSpec<Methods> newMethodsSettingSpec(
			final String s, final Methods defaultVal);
	
	protected abstract SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal);
	
	protected abstract SettingSpec<Oid> newOidSettingSpec(
			final String s, final Oid defaultVal);
	
	protected abstract SettingSpec<Port> newPortSettingSpec(
			final String s, final Port defaultVal);
	
	protected abstract SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal);
	
	protected abstract SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal);
	
	protected abstract SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal);
	
	protected abstract SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal);
	
	protected abstract SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal);
	
	protected abstract SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal);
	
	protected abstract SettingSpec<String> newStringSettingSpec(
			final String s, final String defaultVal);
	
	protected abstract SettingSpec<Strings> newStringsSettingSpec(
			final String s, final Strings defaultVal);
	
	protected abstract SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal);
	
	protected abstract SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal);
}
