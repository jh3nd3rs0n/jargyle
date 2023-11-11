package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "encryptedPassword", propOrder = { })
class EncryptedPasswordXml extends ValueXml { 

	@XmlElement(name = "typeName", required = true)
	protected String typeName;
	@XmlElement(name = "argumentsString", required = true)
	protected String argumentsString;
	
	public EncryptedPasswordXml() {
		this.typeName = null;
		this.argumentsString = null;
	}
	
	public EncryptedPasswordXml(final EncryptedPassword encryptedPassword) {
		this.typeName = encryptedPassword.getTypeName();
		this.argumentsString = encryptedPassword.getArgumentsString();
	}
	
	public EncryptedPassword toEncryptedPassword() {
		return EncryptedPassword.newInstance(
				this.typeName, this.argumentsString);
	}
	
	@Override
	public Object toValue() {
		return this.toEncryptedPassword();
	}
	
}