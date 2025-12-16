package com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.internal.userrepo.impl.FileSourceUserRepository;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Username Password Authentication Method External Source User Repositories"
)
public final class ExternalSourceUserRepositorySpecConstants {

	private static final UserRepositorySpecs USER_REPOSITORY_SPECS =
			new UserRepositorySpecs();

	@NameValuePairValueSpecDoc(
			description = "User repository that handles the storage of the "
					+ "users from a provided file of a list of URL encoded "
					+ "username and hashed password pairs (If the file does not "
					+ "exist, it will be created and used.)",
			name = "FileSourceUserRepository",
			syntax = "FileSourceUserRepository:FILE",
			valueType = File.class
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
				"expected user repository type name must be one of the "
				+ "following values: %s. actual value is %s",
				str,
				typeName));
	}
	
	public static List<UserRepositorySpec> values() {
		return VALUES;
	}
	
	public static Map<String, UserRepositorySpec> valuesMap() {
		return VALUES_MAP;
	}
	
	private ExternalSourceUserRepositorySpecConstants() { }

}
