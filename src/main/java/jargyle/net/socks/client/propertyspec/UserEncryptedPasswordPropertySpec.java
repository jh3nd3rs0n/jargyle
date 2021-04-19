package jargyle.net.socks.client.propertyspec;

import java.util.Arrays;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.security.EncryptedPassword;

public final class UserEncryptedPasswordPropertySpec 
	extends PropertySpec<EncryptedPassword> {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePassword.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}
	
	public UserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(
				s, 
				EncryptedPassword.class, 
				getValidatedEncryptedPassword(defaultVal));
	}

	@Override
	public Property<EncryptedPassword> newProperty(
			final EncryptedPassword value) {
		return super.newProperty(getValidatedEncryptedPassword(value));
	}

	@Override
	public Property<EncryptedPassword> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(getValidatedEncryptedPassword(
				EncryptedPassword.newInstance(value.toCharArray())));
	}

}
