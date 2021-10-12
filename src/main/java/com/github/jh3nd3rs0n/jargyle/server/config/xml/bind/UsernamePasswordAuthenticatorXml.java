package com.github.jh3nd3rs0n.jargyle.server.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "usernamePasswordAuthenticator", propOrder = { }) 
class UsernamePasswordAuthenticatorXml extends ValueXml {
	
	@XmlElement(name = "className", required = true)
	protected String className;
	@XmlElement(name = "value")
	protected String value;
	
	public UsernamePasswordAuthenticatorXml() {
		this.className = null;
		this.value = null;
	}
	
	public UsernamePasswordAuthenticatorXml(
			final UsernamePasswordAuthenticator usernamePasswordAuthenticator) {
		this.className = usernamePasswordAuthenticator.getClass().getName();
		this.value = usernamePasswordAuthenticator.getValue();			
	}
	
	public UsernamePasswordAuthenticator toUsernamePasswordAuthenticator() {
		return UsernamePasswordAuthenticator.newInstance(
				this.className, this.value);
	}

	@Override
	public Object toValue() {
		return this.toUsernamePasswordAuthenticator();
	}
	
}