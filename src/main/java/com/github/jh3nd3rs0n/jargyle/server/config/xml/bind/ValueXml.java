package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Criteria;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriteria;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "value")
abstract class ValueXml {
	
	public static ValueXml newInstance(final Object val) {
		Objects.requireNonNull(val);
		if (val instanceof Criteria) {
			return new CriteriaXml((Criteria) val);
		} else if (val instanceof EncryptedPassword) {
			return EncryptedPasswordXml.newInstance((EncryptedPassword) val);
		} else if (val instanceof SocketSettings) {
			return new SocketSettingsXml((SocketSettings) val);
		} else if (val instanceof Socks5RequestCriteria) {
			return new Socks5RequestCriteriaXml(
					(Socks5RequestCriteria) val);
		} else if (val instanceof Socks5RequestWorkerFactory) {
			return new Socks5RequestWorkerFactoryXml(
					(Socks5RequestWorkerFactory) val);
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