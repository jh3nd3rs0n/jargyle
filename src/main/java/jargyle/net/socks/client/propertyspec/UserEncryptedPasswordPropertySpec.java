package jargyle.net.socks.client.propertyspec;

import java.util.Arrays;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.security.EncryptedPassword;

public final class UserEncryptedPasswordPropertySpec extends PropertySpec {

	private static EncryptedPassword getValidatedEncryptedPassword(
			final EncryptedPassword encryptedPassword) {
		char[] password = encryptedPassword.getPassword();
		UsernamePassword.validatePassword(password);
		Arrays.fill(password, '\0');
		return encryptedPassword;
	}
	
	public UserEncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, getValidatedEncryptedPassword(defaultVal));
	}

	@Override
	public Property newProperty(final Object value) {
		EncryptedPassword val = EncryptedPassword.class.cast(value);
		return Property.newInstance(this, getValidatedEncryptedPassword(val));
	}

	@Override
	public Property newProperty(final String value) {
		return Property.newInstance(this, getValidatedEncryptedPassword(
				EncryptedPassword.newInstance(value.toCharArray())));
	}

}
