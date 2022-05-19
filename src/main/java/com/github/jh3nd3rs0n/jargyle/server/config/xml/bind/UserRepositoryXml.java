package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "userRepository", propOrder = { }) 
class UserRepositoryXml extends ValueXml {

	@XmlElement(name = "className", required = true)
	protected String className;
	@XmlElement(name = "initializationValue", required = true)
	protected String initializationValue;
	
	public UserRepositoryXml() {
		this.className = null;
		this.initializationValue = null;
	}
	
	public UserRepositoryXml(final UserRepository userRepository) {
		this.className = userRepository.getClass().getName();
		this.initializationValue = userRepository.getInitializationValue();
	}

	public UserRepository toUserRepository() {
		return UserRepository.newInstance(
				this.className, this.initializationValue);
	}
	
	@Override
	public Object toValue() {
		return this.toUserRepository();
	}

}
