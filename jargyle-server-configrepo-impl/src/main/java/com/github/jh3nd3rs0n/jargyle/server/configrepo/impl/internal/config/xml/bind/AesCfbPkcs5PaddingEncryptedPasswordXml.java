package com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.internal.config.xml.bind;

import com.github.jh3nd3rs0n.jargyle.common.security.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "aesCfbPkcs5PaddingEncryptedPassword", propOrder = { }) 
class AesCfbPkcs5PaddingEncryptedPasswordXml extends EncryptedPasswordXml {
	
	@XmlElement(name = "encodedKey", required = true)
	protected byte[] encodedKey;
	@XmlElement(name = "encrypted", required = true)
	protected byte[] encrypted;
	@XmlElement(name = "initializationVector", required = true)
	protected byte[] initializationVector;

	public AesCfbPkcs5PaddingEncryptedPasswordXml() { 
		this.encodedKey = null;
		this.encrypted = null;
		this.initializationVector = null;
	}
	
	public AesCfbPkcs5PaddingEncryptedPasswordXml(
			final AesCfbPkcs5PaddingEncryptedPassword encryptedPassword) {
		this.encodedKey = encryptedPassword.getEncodedKey();
		this.encrypted = encryptedPassword.getEncrypted();
		this.initializationVector = 
				encryptedPassword.getInitializationVector();			
	}
	
	@Override
	public AesCfbPkcs5PaddingEncryptedPassword toEncryptedPassword() {
		return AesCfbPkcs5PaddingEncryptedPassword.newInstance(
				this.encodedKey, 
				this.encrypted, 
				this.initializationVector);
	}

	@Override
	public Object toValue() {
		return this.toEncryptedPassword();
	}
	
}