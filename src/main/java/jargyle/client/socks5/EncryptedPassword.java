package jargyle.client.socks5;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.client.socks5.AesCbcPkcs5PaddingEncryptedPassword.AesCbcPkcs5PaddingEncryptedPasswordXml;

@XmlJavaTypeAdapter(EncryptedPassword.EncryptedPasswordXmlAdapter.class)
public abstract class EncryptedPassword {
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "encryptedPassword", propOrder = { })
	@XmlSeeAlso(value = { AesCbcPkcs5PaddingEncryptedPasswordXml.class })
	static abstract class EncryptedPasswordXml { }
	
	static final class EncryptedPasswordXmlAdapter 
		extends XmlAdapter<EncryptedPasswordXml, EncryptedPassword> {

		@Override
		public EncryptedPasswordXml marshal(
				final EncryptedPassword arg) throws Exception {
			EncryptedPasswordXml encryptedPasswordXml = null;
			if (arg instanceof AesCbcPkcs5PaddingEncryptedPassword) {
				AesCbcPkcs5PaddingEncryptedPassword encryptedPassword =
						(AesCbcPkcs5PaddingEncryptedPassword) arg;
				encryptedPasswordXml = 
						encryptedPassword.toAesCbcPkcs5PaddingEncryptedPasswordXml();
			}
			if (encryptedPasswordXml == null) {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						EncryptedPassword.class.getSimpleName(),
						arg.getClass().getSimpleName()));
			}
			return encryptedPasswordXml;
		}

		@Override
		public EncryptedPassword unmarshal(
				final EncryptedPasswordXml arg) throws Exception {
			EncryptedPassword encryptedPassword = null;
			if (arg instanceof AesCbcPkcs5PaddingEncryptedPasswordXml) {
				AesCbcPkcs5PaddingEncryptedPasswordXml encryptedPasswordXml =
						(AesCbcPkcs5PaddingEncryptedPasswordXml) arg;
				encryptedPassword = 
						AesCbcPkcs5PaddingEncryptedPassword.newInstance(
								encryptedPasswordXml);
			}
			if (encryptedPassword == null) {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						EncryptedPasswordXml.class.getSimpleName(),
						arg.getClass().getSimpleName()));
			}
			return encryptedPassword;
		}
		
	}
	
	public static EncryptedPassword newInstance(final char[] password) {
		return AesCbcPkcs5PaddingEncryptedPassword.newInstance(password);
	}

	public abstract char[] getPassword();
	
}
