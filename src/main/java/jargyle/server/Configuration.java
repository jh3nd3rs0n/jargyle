package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestRules;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public interface Configuration {

	Expressions getAllowedClientAddressExpressions();

	Socks5RequestRules getAllowedSocks5RequestRules();
	
	Expressions getBlockedClientAddressExpressions();
	
	Socks5RequestRules getBlockedSocks5RequestRules();

	UsernamePassword getExternalClientSocks5UsernamePassword();

	Settings getSettings();

	UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator();

}