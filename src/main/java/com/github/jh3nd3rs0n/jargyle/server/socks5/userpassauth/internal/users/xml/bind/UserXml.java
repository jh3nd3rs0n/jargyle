package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.User;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "user", propOrder = { }) 
class UserXml {
	
	@XmlElement(name = "hashedPassword", required = true)
	protected HashedPasswordXml hashedPasswordXml;
	@XmlElement(name = "name", required = true)
	protected String name;
	
	public UserXml() {
		this.hashedPasswordXml = null;
		this.name = null;
	}
	
	public UserXml(final User usr) {
		this.hashedPasswordXml = HashedPasswordXml.newInstance(
				usr.getHashedPassword());
		this.name = usr.getName();
	}
	
	public User toUser() {
		return User.newInstance(
				this.name, this.hashedPasswordXml.toHashedPassword());
	}
	
}