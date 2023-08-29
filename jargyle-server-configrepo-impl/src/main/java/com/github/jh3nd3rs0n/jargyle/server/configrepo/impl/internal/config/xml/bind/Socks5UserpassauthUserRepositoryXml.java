package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "socks5.userpassauth.userRepository", propOrder = { }) 
class Socks5UserpassauthUserRepositoryXml extends ValueXml {

	@XmlElement(name = "className", required = true)
	protected String className;
	@XmlElement(name = "initializationString", required = true)
	protected String initializationString;
	
	public Socks5UserpassauthUserRepositoryXml() {
		this.className = null;
		this.initializationString = null;
	}
	
	public Socks5UserpassauthUserRepositoryXml(
			final UserRepository userRepository) {
		this.className = userRepository.getClass().getName();
		this.initializationString = userRepository.getInitializationString();
	}

	public UserRepository toUserRepository() {
		return UserRepository.newInstance(
				this.className, this.initializationString);
	}
	
	@Override
	public Object toValue() {
		return this.toUserRepository();
	}

}
