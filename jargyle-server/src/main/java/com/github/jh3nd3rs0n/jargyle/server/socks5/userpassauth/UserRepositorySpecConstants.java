package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.userrepo.impl.FileSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.internal.userrepo.impl.StringSourceUserRepository;

public final class UserRepositorySpecConstants {

	private static final UserRepositorySpecs USER_REPOSITORY_SPECS =
			new UserRepositorySpecs();

	@HelpText(
			doc = "User repository that handles the storage of the users from "
					+ "a provided file of a list of URL encoded username and "
					+ "hashed password pairs (If the file does not exist, it "
					+ "will be created and used.)", 
			usage = "FileSourceUserRepository:FILE"
	)
	public static final UserRepositorySpec FILE_SOURCE_USER_REPOSITORY = USER_REPOSITORY_SPECS.addThenGet(new UserRepositorySpec(
			"FileSourceUserRepository") {

				@Override
				public UserRepository newUserRepository(
						final String initializationString) {
					return FileSourceUserRepository.newInstance(
							this, initializationString);
				}
		
	});
	
	@HelpText(
			doc = "User repository that handles the storage of the users from "
					+ "a provided string of a comma separated list of URL "
					+ "encoded username and password pairs", 
			usage = "StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]"
	)
	public static final UserRepositorySpec STRING_SOURCE_USER_REPOSITORY = USER_REPOSITORY_SPECS.addThenGet(new UserRepositorySpec(
			"StringSourceUserRepository") {

				@Override
				public UserRepository newUserRepository(
						final String initializationString) {
					return new StringSourceUserRepository(
							this, initializationString);
				}
		
	});
	
	private static final List<UserRepositorySpec> VALUES =
			USER_REPOSITORY_SPECS.toList();
	
	private static final Map<String, UserRepositorySpec> VALUES_MAP =
			USER_REPOSITORY_SPECS.toMap();
	
	public static UserRepositorySpec valueOfTypeName(final String typeName) {
		if (VALUES_MAP.containsKey(typeName)) {
			return VALUES_MAP.get(typeName);
		}
		String str = VALUES.stream()
				.map(UserRepositorySpec::getTypeName)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected user repository spec must be one of the following "
				+ "values: %s. actual value is %s",
				str,
				typeName));
	}
	
	public static List<UserRepositorySpec> values() {
		return VALUES;
	}
	
	public static Map<String, UserRepositorySpec> valuesMap() {
		return VALUES_MAP;
	}
	
	private UserRepositorySpecConstants() { }

}
