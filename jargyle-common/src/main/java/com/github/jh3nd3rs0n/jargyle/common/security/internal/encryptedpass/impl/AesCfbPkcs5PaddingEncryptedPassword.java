package com.github.jh3nd3rs0n.jargyle.common.security.internal.encryptedpass.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;

public final class AesCfbPkcs5PaddingEncryptedPassword extends EncryptedPassword {

	private static final String CIPHER_ALGORITHM = "AES/CFB/PKCS5Padding";
	private static final int KEY_LENGTH = 256;
	private static final String SECRET_KEY_SPEC_ALGORITHM = "AES";

	private static AesCfbPkcs5PaddingEncryptedPassword newInstance(
			final byte[] secret) {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(SECRET_KEY_SPEC_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
		keyGenerator.init(KEY_LENGTH);
		SecretKey secretKey = keyGenerator.generateKey();
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		} catch (NoSuchPaddingException e) {
			throw new AssertionError(e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (InvalidKeyException e) {
			throw new AssertionError(e);
		}
		AlgorithmParameters params = cipher.getParameters();
		IvParameterSpec ivParameterSpec = null;
		try {
			ivParameterSpec = params.getParameterSpec(IvParameterSpec.class);
		} catch (InvalidParameterSpecException e) {
			throw new AssertionError(e);
		}
		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(secret);
		} catch (IllegalBlockSizeException e) {
			throw new AssertionError(e);
		} catch (BadPaddingException e) {
			throw new AssertionError(e);
		}
		return new AesCfbPkcs5PaddingEncryptedPassword(
				secretKey.getEncoded(), 
				encrypted, 
				ivParameterSpec.getIV());
	}
	
	public static AesCfbPkcs5PaddingEncryptedPassword newInstance(
			final byte[] encodedKey,
			final byte[] encrypted,
			final byte[] initializationVector) {
		return new AesCfbPkcs5PaddingEncryptedPassword(
				encodedKey,
				encrypted,
				initializationVector);
	}

	public static AesCfbPkcs5PaddingEncryptedPassword newInstance(
			final char[] password) {
		ByteArrayOutputStream byteArrayOutputStream = 
				new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(byteArrayOutputStream);
		for (char ch : password) {
			try {
				writer.write(ch);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		try {
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] secret = byteArrayOutputStream.toByteArray();
		return newInstance(secret);
	}
	
	private final byte[] encodedKey;
	private final byte[] encrypted;
	private final byte[] initializationVector;

	private AesCfbPkcs5PaddingEncryptedPassword(
			final byte[] key, 
			final byte[] enc, 
			final byte[] iv) {
		this.encodedKey = Arrays.copyOf(key, key.length);
		this.encrypted = Arrays.copyOf(enc, enc.length);
		this.initializationVector = Arrays.copyOf(iv, iv.length);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		AesCfbPkcs5PaddingEncryptedPassword other = 
				(AesCfbPkcs5PaddingEncryptedPassword) obj;
		if (!Arrays.equals(this.encodedKey, other.encodedKey)) {
			return false;
		}
		if (!Arrays.equals(this.encrypted, other.encrypted)) {
			return false;
		}
		if (!Arrays.equals(
				this.initializationVector, other.initializationVector)) {
			return false;
		}
		return true;
	}
	
	public byte[] getEncodedKey() {
		return Arrays.copyOf(this.encodedKey, this.encodedKey.length);
	}
	
	public byte[] getEncrypted() {
		return Arrays.copyOf(this.encrypted, this.encrypted.length);
	}
	
	public byte[] getInitializationVector() {
		return Arrays.copyOf(
				this.initializationVector, this.initializationVector.length);
	}

	@Override
	public char[] getPassword() {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		} catch (NoSuchPaddingException e) {
			throw new AssertionError(e);
		}
		try {
			cipher.init(
					Cipher.DECRYPT_MODE, 
					new SecretKeySpec(
							this.encodedKey, SECRET_KEY_SPEC_ALGORITHM),
					new IvParameterSpec(this.initializationVector));
		} catch (InvalidKeyException e) {
			throw new AssertionError(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new AssertionError(e);
		}
		byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(this.encrypted);
		} catch (IllegalBlockSizeException e) {
			throw new AssertionError(e);
		} catch (BadPaddingException e) {
			throw new AssertionError(e);
		}
		Reader reader = new InputStreamReader(new ByteArrayInputStream(
				decrypted));
		int passwordLength = 0;
		char[] password = new char[passwordLength];
		int ch = -1;
		do {
			try {
				ch = reader.read();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			if (ch != -1) {
				password = Arrays.copyOf(password, ++passwordLength);
				password[passwordLength - 1] = (char) ch;
			}
		} while (ch != -1);
		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.encodedKey);
		result = prime * result + Arrays.hashCode(this.encrypted);
		result = prime * result + Arrays.hashCode(this.initializationVector);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [encodedKey=")
			.append(Arrays.toString(this.encodedKey))
			.append(", encrypted=")
			.append(Arrays.toString(this.encrypted))
			.append(", initializationVector=")
			.append(Arrays.toString(this.initializationVector))
			.append("]");
		return builder.toString();
	}

}
