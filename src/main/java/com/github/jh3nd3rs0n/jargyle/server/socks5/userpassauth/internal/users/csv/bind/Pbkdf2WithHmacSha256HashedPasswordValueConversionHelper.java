package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.users.csv.bind;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.hashedpass.impl.Pbkdf2WithHmacSha256HashedPassword;

final class Pbkdf2WithHmacSha256HashedPasswordValueConversionHelper {

	public static Pbkdf2WithHmacSha256HashedPassword toHashedPassword(
			final String value) {
		String message = String.format(
				"value must be in the following format: "
				+ "%s:HASH_BASE_64_STRING;SALT_BASE_64_STRING " 
				+ "actual value is %s",
				Pbkdf2WithHmacSha256HashedPassword.class.getName(),
				value);
		String[] valueElements = value.split(":", 2);
		if (valueElements.length != 2) {
			throw new IllegalArgumentException(message);
		}
		String className = valueElements[0];
		String parameters = valueElements[1];
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
		return Pbkdf2WithHmacSha256HashedPassword.newInstance(hash, salt);		
	}
	
	public static String toValue(
			final Pbkdf2WithHmacSha256HashedPassword hashedPassword) {
		Encoder encoder = Base64.getEncoder();
		return String.format(
				"%s:%s;%s", 
				hashedPassword.getClass().getName(),
				encoder.encodeToString(hashedPassword.getHash()),
				encoder.encodeToString(hashedPassword.getSalt()));
	}
	
	private Pbkdf2WithHmacSha256HashedPasswordValueConversionHelper() { }
	
}
