package com.github.jh3nd3rs0n.jargyle.server.internal.config.xml.bind;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl.AesCfbPkcs5PaddingEncryptedPassword;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "encryptedPassword", propOrder = { })
@XmlSeeAlso(value = { AesCfbPkcs5PaddingEncryptedPasswordXml.class })
abstract class EncryptedPasswordXml extends ValueXml { 
	
	public static EncryptedPasswordXml newInstance(
			final EncryptedPassword encryptedPassword) {
		Objects.requireNonNull(encryptedPassword);
		if (encryptedPassword instanceof AesCfbPkcs5PaddingEncryptedPassword) {
			return new AesCfbPkcs5PaddingEncryptedPasswordXml(
					(AesCfbPkcs5PaddingEncryptedPassword) encryptedPassword);
		}
		throw new IllegalArgumentException(String.format(
				"no %s for %s", 
				EncryptedPasswordXml.class.getName(),
				encryptedPassword.getClass().getName()));
	}
	
	public abstract EncryptedPassword toEncryptedPassword();
	
}