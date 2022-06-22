package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

final class Pbkdf2WithHmacSha256HashedPasswordValue extends HashedPasswordValue {

	private final Pbkdf2WithHmacSha256HashedPassword hashedPassword;
	private final String value;
	
	public Pbkdf2WithHmacSha256HashedPasswordValue(
			final Pbkdf2WithHmacSha256HashedPassword hashedPsswrd) {
		Encoder encoder = Base64.getEncoder();
		String val = String.format(
				"%s:%s;%s", 
				hashedPsswrd.getClass().getName(),
				encoder.encodeToString(hashedPsswrd.getHash()),
				encoder.encodeToString(hashedPsswrd.getSalt()));
		this.hashedPassword = hashedPsswrd;
		this.value = val;
	}
	
	public Pbkdf2WithHmacSha256HashedPasswordValue(final String val) {
		String message = String.format(
				"value must be in the following format: "
				+ "%s:HASH_BASE_64_STRING;SALT_BASE_64_STRING " 
				+ "actual value is %s",
				Pbkdf2WithHmacSha256HashedPassword.class.getName(),
				val);
		String[] valElements = val.split(":", 2);
		if (valElements.length != 2) {
			throw new IllegalArgumentException(message);
		}
		String className = valElements[0];
		String parameters = valElements[1];
		if (!className.equals(
				Pbkdf2WithHmacSha256HashedPassword.class.getName())) {
			throw new IllegalArgumentException(message);
		}
		String[] parametersElements = parameters.split(";", 2);
		if (parametersElements.length != 2) {
			throw new IllegalArgumentException(message);
		}
		String hashBase64String = parametersElements[0];
		String saltBase64String = parametersElements[1];
		Decoder decoder = Base64.getDecoder();
		byte[] hash = decoder.decode(hashBase64String);
		byte[] salt = decoder.decode(saltBase64String);
		Pbkdf2WithHmacSha256HashedPassword hashedPsswrd = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(hash, salt); 
		this.hashedPassword = hashedPsswrd;
		this.value = val;
	}
	
	@Override
	public HashedPassword toHashedPassword() {
		return this.hashedPassword;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
