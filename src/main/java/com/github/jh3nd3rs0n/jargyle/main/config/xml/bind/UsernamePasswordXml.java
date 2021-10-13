package com.github.jh3nd3rs0n.jargyle.main.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "usernamePassword", propOrder = { }) 
class UsernamePasswordXml extends ValueXml {
	
	@XmlElement(name = "encryptedPassword", required = true)
	protected EncryptedPasswordXml encryptedPasswordXml;
	@XmlElement(name = "username", required = true)
	protected String username;
	
	public UsernamePasswordXml() {
		this.encryptedPasswordXml = null;
		this.username = null;
	}
	
	public UsernamePasswordXml(final UsernamePassword usernamePassword) {
		this.encryptedPasswordXml = EncryptedPasswordXml.newInstance(
				usernamePassword.getEncryptedPassword());
		this.username = usernamePassword.getUsername();			
	}
	
	public UsernamePassword toUsernamePassword() {
		return UsernamePassword.newInstance(
				this.username, 
				this.encryptedPasswordXml.toEncryptedPassword().getPassword());
	}

	@Override
	public Object toValue() {
		return this.toUsernamePassword();
	}
	
}