package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "pbkdf2WithHmacSha256HashedPassword", propOrder = { }) 
class Pbkdf2WithHmacSha256HashedPasswordXml extends HashedPasswordXml {
	
	@XmlElement(name = "hash", required = true)
	protected byte[] hash;
	@XmlElement(name = "salt", required = true)
	protected byte[] salt;
	
	public Pbkdf2WithHmacSha256HashedPasswordXml() {
		this.hash = null;
		this.salt = null;
	}
	
	public Pbkdf2WithHmacSha256HashedPasswordXml(
			final Pbkdf2WithHmacSha256HashedPassword hashedPassword) {
		this.hash = hashedPassword.getHash();
		this.salt = hashedPassword.getSalt();
	}

	@Override
	public Pbkdf2WithHmacSha256HashedPassword toHashedPassword() {
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(
				this.hash, this.salt);
	}
	
}