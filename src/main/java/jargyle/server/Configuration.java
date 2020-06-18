package jargyle.server;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public abstract class Configuration {

	public Configuration() { }
	
	public abstract Criteria getAllowedClientAddressCriteria();

	public abstract Criteria getAllowedSocks5IncomingTcpAddressCriteria();
	
	public abstract Criteria getAllowedSocks5IncomingUdpAddressCriteria();
	
	public abstract Socks5RequestCriteria getAllowedSocks5RequestCriteria();
	
	public abstract Criteria getBlockedClientAddressCriteria();

	public abstract Criteria getBlockedSocks5IncomingTcpAddressCriteria();
	
	public abstract Criteria getBlockedSocks5IncomingUdpAddressCriteria();
	
	public abstract Socks5RequestCriteria getBlockedSocks5RequestCriteria();

	public abstract UsernamePassword getExternalClientSocks5UsernamePassword();

	public abstract Settings getSettings();

	public abstract UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator();

}