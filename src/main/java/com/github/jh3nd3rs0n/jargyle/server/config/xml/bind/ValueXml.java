package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.ClientRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5ReplyRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5UdpRules;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "value")
abstract class ValueXml {
	
	public static ValueXml newInstance(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof ClientRules) {
			return new ClientRulesXml((ClientRules) val);
		} else if (val instanceof EncryptedPassword) {
			return EncryptedPasswordXml.newInstance((EncryptedPassword) val);
		} else if (val instanceof SocketSettings) {
			return new SocketSettingsXml((SocketSettings) val);
		} else if (val instanceof Socks5ReplyRules) {
			return new Socks5ReplyRulesXml((Socks5ReplyRules) val);
		} else if (val instanceof Socks5RequestRules) {
			return new Socks5RequestRulesXml((Socks5RequestRules) val);
		} else if (val instanceof Socks5RequestWorkerFactory) {
			return new Socks5RequestWorkerFactoryXml(
					(Socks5RequestWorkerFactory) val);
		} else if (val instanceof Socks5UdpRules) {
			return new Socks5UdpRulesXml((Socks5UdpRules) val);
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