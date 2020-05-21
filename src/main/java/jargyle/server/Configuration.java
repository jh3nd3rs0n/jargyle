package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public interface Configuration {

	Criteria getAllowedClientAddressCriteria();

	Criteria getAllowedIncomingTcpAddressCriteria();
	
	Criteria getAllowedIncomingUdpAddressCriteria();
	
	Socks5RequestCriteria getAllowedSocks5RequestCriteria();
	
	Criteria getBlockedClientAddressCriteria();

	Criteria getBlockedIncomingTcpAddressCriteria();
	
	Criteria getBlockedIncomingUdpAddressCriteria();
	
	Socks5RequestCriteria getBlockedSocks5RequestCriteria();

	UsernamePassword getExternalClientSocks5UsernamePassword();

	Settings getSettings();

	UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator();

}