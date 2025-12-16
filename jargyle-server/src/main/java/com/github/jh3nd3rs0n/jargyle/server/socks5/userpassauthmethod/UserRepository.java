package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueTypeDoc;

@NameValuePairValueTypeDoc(
		description = "",
		name = "SOCKS5 Username Password Authentication Method User Repository",
		nameValuePairValueSpecs = {
				UserRepositorySpecConstants.class
		},
		syntax = "TYPE_NAME:INITIALIZATION_STRING",
		syntaxName = "SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY"
)
public abstract class UserRepository {

	public static UserRepository newExternalSourceInstance(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user repository must be in the following format: "
					+ "TYPE_NAME:INITIALIZATION_STRING");
		}
		String typeName = sElements[0];
		String initializationString = sElements[1];
		return newExternalSourceInstance(typeName, initializationString);		
	}
	
	public static UserRepository newExternalSourceInstance(
			final String typeName, final String initializationString) {
		UserRepositorySpec userRepositorySpec = 
				ExternalSourceUserRepositorySpecConstants.valueOfTypeName(
						typeName);
		return userRepositorySpec.newUserRepository(initializationString);		
	}
	
	public static UserRepository newInstance() {
		return UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository("");
	}
	
	public static UserRepository newInstanceFrom(final String s) {
		String[] sElements = s.split(":", 2);
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user repository must be in the following format: "
					+ "TYPE_NAME:INITIALIZATION_STRING");
		}
		String typeName = sElements[0];
		String initializationString = sElements[1];
		return newInstance(typeName, initializationString);
	}
	
	public static UserRepository newInstance(
			final String typeName, final String initializationString) {
		UserRepositorySpec userRepositorySpec = 
				UserRepositorySpecConstants.valueOfTypeName(typeName);
		return userRepositorySpec.newUserRepository(initializationString);
	}

	private final String initializationString;
	private final String typeName;
	private final UserRepositorySpec userRepositorySpec;
	
	public UserRepository(
			final UserRepositorySpec spec, final String initializationStr) {
		Objects.requireNonNull(spec, "user repository spec must not be null");
		Objects.requireNonNull(
				initializationStr, "initialization string must not be null");
		this.initializationString = initializationStr;
		this.typeName = spec.getTypeName();
		this.userRepositorySpec = spec;
	}
	
	public abstract User get(final String name);
	
	public abstract Users getAll();
	
	public final String getInitializationString() {
		return this.initializationString;
	}
	
	public final String getTypeName() {
		return this.typeName;
	}
	
	public final UserRepositorySpec getUserRepositorySpec() {
		return this.userRepositorySpec;
	}
	
	public abstract void put(final User user);
	
	public abstract void putAll(final Users users);
	
	public abstract void remove(final String name);

	@Override
	public String toString() {
		return String.format(
				"%s:%s", 
				this.typeName, 
				this.initializationString);
	}
	
}
