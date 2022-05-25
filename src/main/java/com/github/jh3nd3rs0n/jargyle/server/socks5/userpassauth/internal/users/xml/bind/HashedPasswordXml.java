package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "hashedPassword", propOrder = { })
@XmlSeeAlso(value = { Pbkdf2WithHmacSha256HashedPasswordXml.class })
abstract class HashedPasswordXml { 
	
	public static HashedPasswordXml newInstance(
			final HashedPassword hashedPassword) {
		Objects.requireNonNull(hashedPassword);
		if (hashedPassword instanceof Pbkdf2WithHmacSha256HashedPassword) {
			return new Pbkdf2WithHmacSha256HashedPasswordXml(
					(Pbkdf2WithHmacSha256HashedPassword) hashedPassword); 
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				HashedPasswordXml.class.getName(),
				hashedPassword.getClass().getName()));
	}
	
	public abstract HashedPassword toHashedPassword();
	
}