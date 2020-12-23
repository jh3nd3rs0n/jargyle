package jargyle.common.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.common.security.Pbkdf2WithHmacSha256HashedPassword.Pbkdf2WithHmacSha256HashedPasswordXml;

@XmlJavaTypeAdapter(HashedPassword.HashedPasswordXmlAdapter.class)
public abstract class HashedPassword {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "hashedPassword", propOrder = { })
	@XmlSeeAlso(value = { Pbkdf2WithHmacSha256HashedPasswordXml.class })
	static abstract class HashedPasswordXml { }
	
	static final class HashedPasswordXmlAdapter 
		extends XmlAdapter<HashedPasswordXml, HashedPassword> {

		@Override
		public HashedPasswordXml marshal(
				final HashedPassword arg) throws Exception {
			HashedPasswordXml hashedPasswordXml = null;
			if (arg instanceof Pbkdf2WithHmacSha256HashedPassword) {
				Pbkdf2WithHmacSha256HashedPassword hashedPassword =
						(Pbkdf2WithHmacSha256HashedPassword) arg;
				hashedPasswordXml = 
						hashedPassword.toPbkdf2WithHmacSha256HashedPasswordXml();
			}
			if (hashedPasswordXml == null) {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						HashedPassword.class.getSimpleName(),
						arg.getClass().getSimpleName()));
			}
			return hashedPasswordXml;
		}

		@Override
		public HashedPassword unmarshal(
				final HashedPasswordXml arg) throws Exception {
			HashedPassword hashedPassword = null;
			if (arg instanceof Pbkdf2WithHmacSha256HashedPasswordXml) {
				Pbkdf2WithHmacSha256HashedPasswordXml hashedPasswordXml =
						(Pbkdf2WithHmacSha256HashedPasswordXml) arg;
				hashedPassword = 
						Pbkdf2WithHmacSha256HashedPassword.newInstance(
								hashedPasswordXml);
			}
			if (hashedPassword == null) {
				throw new AssertionError(String.format(
						"unhandled %s: %s", 
						HashedPasswordXml.class.getSimpleName(),
						arg.getClass().getSimpleName()));
			}
			return hashedPassword;
		}
		
	}
	
	public static HashedPassword newInstance(final char[] password) {
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
	}
	
	public static HashedPassword newInstance(
			final char[] password, final HashedPassword other) {
		HashedPassword hashedPassword = null;
		if (other instanceof Pbkdf2WithHmacSha256HashedPassword) {
			Pbkdf2WithHmacSha256HashedPassword otherHashedPassword =
					(Pbkdf2WithHmacSha256HashedPassword) other;
			hashedPassword = Pbkdf2WithHmacSha256HashedPassword.newInstance(
					password, otherHashedPassword.getSalt());
		}
		if (hashedPassword == null) {
			throw new AssertionError(String.format(
					"unhandled %s: %s", 
					HashedPassword.class.getSimpleName(),
					other.getClass().getSimpleName()));
		}
		return hashedPassword;
	}
	
}
