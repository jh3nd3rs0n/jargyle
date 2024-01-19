package com.github.jh3nd3rs0n.jargyle.common.security;

public class TestEncryptedPasswordSpec extends EncryptedPasswordSpec {

    TestEncryptedPasswordSpec(final String typeName) {
        super(typeName);
    }

    @Override
    public EncryptedPassword newEncryptedPassword(final char[] password) {
        return new EncryptedPassword(
                this, new TestEncryptedPasswordValue(password));
    }

    @Override
    public EncryptedPassword newEncryptedPassword(
            final String encryptedPasswordValue) {
        return new EncryptedPassword(
                this,
                new TestEncryptedPasswordValue(encryptedPasswordValue));
    }

}
