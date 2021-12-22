package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.ClientRoutingRules;
import com.github.jh3nd3rs0n.jargyle.server.ClientFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRoutingRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpFirewallRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "value")
abstract class ValueXml {
	
	public static ValueXml newInstance(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof ClientFirewallRules) {
			return new ClientFirewallRulesXml((ClientFirewallRules) val);
		} else if (val instanceof ClientRoutingRules) {
			return new ClientRoutingRulesXml((ClientRoutingRules) val);
		} else if (val instanceof EncryptedPassword) {
			return EncryptedPasswordXml.newInstance((EncryptedPassword) val);
		} else if (val instanceof SocketSettings) {
			return new SocketSettingsXml((SocketSettings) val);
		} else if (val instanceof Socks5ReplyFirewallRules) {
			return new Socks5ReplyFirewallRulesXml(
					(Socks5ReplyFirewallRules) val);
		} else if (val instanceof Socks5RequestFirewallRules) {
			return new Socks5RequestFirewallRulesXml(
					(Socks5RequestFirewallRules) val);
		} else if (val instanceof Socks5RequestRoutingRules) {
			return new Socks5RequestRoutingRulesXml(
					(Socks5RequestRoutingRules) val);
		} else if (val instanceof Socks5RequestWorkerFactory) {
			return new Socks5RequestWorkerFactoryXml(
					(Socks5RequestWorkerFactory) val);
		} else if (val instanceof Socks5UdpFirewallRules) {
			return new Socks5UdpFirewallRulesXml((Socks5UdpFirewallRules) val);
		} else if (val instanceof UsernamePasswordAuthenticator) {
			return new UsernamePasswordAuthenticatorXml(
					(UsernamePasswordAuthenticator) val);
		} else if (val instanceof UsernamePassword) {
			return new UsernamePasswordXml((UsernamePassword) val);
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				ValueXml.class.getName(),
				val.getClass().getName()));
	}
	
	public abstract Object toValue();
	
}