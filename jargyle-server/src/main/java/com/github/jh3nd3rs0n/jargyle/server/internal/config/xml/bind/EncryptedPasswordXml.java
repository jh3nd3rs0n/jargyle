package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

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
	@XmlElement(name = "encryptedPasswordValue", required = true)
	protected String encryptedPasswordValue;
	
	public EncryptedPasswordXml() {
		this.typeName = null;
		this.encryptedPasswordValue = null;
	}
	
	public EncryptedPasswordXml(final EncryptedPassword encryptedPassword) {
		this.typeName = encryptedPassword.getTypeName();
		this.encryptedPasswordValue =
				encryptedPassword.getEncryptedPasswordValue().toString();
	}
	
	public EncryptedPassword toEncryptedPassword() {
		return EncryptedPassword.newInstance(
				this.typeName, this.encryptedPasswordValue);
	}
	
	@Override
	public Object toValue() {
		return this.toEncryptedPassword();
	}
	
}