package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "encryptedPassword", propOrder = { })
class EncryptedPasswordXml extends ValueXml { 

	@XmlElement(name = "className", required = true)
	protected String className;
	@XmlElement(name = "argumentsValue", required = true)
	protected String argumentsValue;
	
	public EncryptedPasswordXml() {
		this.className = null;
		this.argumentsValue = null;
	}
	
	public EncryptedPasswordXml(final EncryptedPassword encryptedPassword) {
		this.className = encryptedPassword.getClass().getName();
		this.argumentsValue = encryptedPassword.getArgumentsValue();
	}
	
	public EncryptedPassword toEncryptedPassword() {
		return EncryptedPassword.newInstance(
				this.className, this.argumentsValue);
	}
	
	@Override
	public Object toValue() {
		return this.toEncryptedPassword();
	}
	
}