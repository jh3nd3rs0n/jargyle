package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public interface Configuration {

	Expressions getAllowedClientAddresses();

	Expressions getBlockedClientAddresses();

	UsernamePassword getExternalClientSocks5UsernamePassword();

	Settings getSettings();

	UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator();

}