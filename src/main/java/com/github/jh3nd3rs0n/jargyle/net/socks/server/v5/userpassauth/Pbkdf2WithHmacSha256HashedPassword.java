package com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.userpassauth;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(Pbkdf2WithHmacSha256HashedPassword.Pbkdf2WithHmacSha256HashedPasswordXmlAdapter.class)
final class Pbkdf2WithHmacSha256HashedPassword extends HashedPassword {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "pbkdf2WithHmacSha256HashedPassword", propOrder = { })
	static class Pbkdf2WithHmacSha256HashedPasswordXml extends HashedPasswordXml {
		@XmlElement(name = "hash", required = true)
		protected byte[] hash;
		@XmlElement(name = "salt", required = true)
		protected byte[] salt;
	}
	
	static final class Pbkdf2WithHmacSha256HashedPasswordXmlAdapter 
		extends XmlAdapter<Pbkdf2WithHmacSha256HashedPasswordXml, Pbkdf2WithHmacSha256HashedPassword> {

		@Override
		public Pbkdf2WithHmacSha256HashedPasswordXml marshal(
				final Pbkdf2WithHmacSha256HashedPassword arg) throws Exception {
			Pbkdf2WithHmacSha256HashedPasswordXml hashedPasswordXml = 
					new Pbkdf2WithHmacSha256HashedPasswordXml();
			hashedPasswordXml.hash = Arrays.copyOf(arg.hash, arg.hash.length);
			hashedPasswordXml.salt = Arrays.copyOf(arg.salt, arg.salt.length);
			return hashedPasswordXml;
		}

		@Override
		public Pbkdf2WithHmacSha256HashedPassword unmarshal(
				final Pbkdf2WithHmacSha256HashedPasswordXml arg) throws Exception {
			return new Pbkdf2WithHmacSha256HashedPassword(arg.hash, arg.salt);
		}
		
	}

	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_LENGTH = 256;
	private static final int SALT_LENGTH = 8;
	private static final String SECRET_KEY_FACTORY_ALGORITHM = 
			"PBKDF2WithHmacSHA256";
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	public static Pbkdf2WithHmacSha256HashedPassword newInstance(
			final char[] password) {
		return newInstance(password, newSalt());
	}
	
	public static Pbkdf2WithHmacSha256HashedPassword newInstance(
			final char[] password, final byte[] salt) {
		char[] psswrd = Arrays.copyOf(password, password.length);
		byte[] slt = Arrays.copyOf(salt, salt.length);
		KeySpec keySpec = new PBEKeySpec(
				psswrd, slt, ITERATION_COUNT, KEY_LENGTH);
		SecretKeyFactory factory = null;
		try {
			factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		byte[] hsh = null;
		try {
			hsh = factory.generateSecret(keySpec).getEncoded();
		} catch (InvalidKeySpecException e) {
			throw new AssertionError(e);
		}
		return new Pbkdf2WithHmacSha256HashedPassword(hsh, slt);
	}
	
	public static Pbkdf2WithHmacSha256HashedPassword newInstance(
			final Pbkdf2WithHmacSha256HashedPasswordXml hashedPasswordXml) {
		return new Pbkdf2WithHmacSha256HashedPassword(
				hashedPasswordXml.hash, hashedPasswordXml.salt);
	}
	
	private static byte[] newSalt() {
		return nextBytes(SALT_LENGTH);
	}
	
	private static byte[] nextBytes(final int length) {
		byte[] bytes = new byte[length];
		SECURE_RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	private final byte[] hash;
	private final byte[] salt;
	
	private Pbkdf2WithHmacSha256HashedPassword(
			final byte[] hsh, final byte[] slt) {
		this.hash = Arrays.copyOf(hsh, hsh.length);
		this.salt = Arrays.copyOf(slt, slt.length);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pbkdf2WithHmacSha256HashedPassword)) {
			return false;
		}
		Pbkdf2WithHmacSha256HashedPassword other = 
				(Pbkdf2WithHmacSha256HashedPassword) obj;
		if (!Arrays.equals(this.hash, other.hash)) {
			return false;
		}
		if (!Arrays.equals(this.salt, other.salt)) {
			return false;
		}
		return true;
	}
	
	public byte[] getHash() {
		return Arrays.copyOf(this.hash, this.hash.length);
	}

	public byte[] getSalt() {
		return Arrays.copyOf(this.salt, this.salt.length);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.hash);
		result = prime * result + Arrays.hashCode(this.salt);
		return result;
	}

	public Pbkdf2WithHmacSha256HashedPasswordXml toPbkdf2WithHmacSha256HashedPasswordXml() {
		Pbkdf2WithHmacSha256HashedPasswordXml hashedPasswordXml = 
				new Pbkdf2WithHmacSha256HashedPasswordXml();
		hashedPasswordXml.hash = Arrays.copyOf(this.hash, this.hash.length);
		hashedPasswordXml.salt = Arrays.copyOf(this.salt, this.salt.length);
		return hashedPasswordXml;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [hash=")
			.append(Arrays.toString(this.hash))
			.append(", salt=")
			.append(Arrays.toString(this.salt))
			.append("]");
		return builder.toString();
	}
	
}
