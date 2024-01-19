package com.github.jh3nd3rs0n.jargyle.common.security;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class TestEncryptedPasswordValue extends EncryptedPasswordValue {

    private final byte[] password;

    private final String string;

    public TestEncryptedPasswordValue(final char[] psswrd) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(out)) {
            writer.write(psswrd);
            writer.flush();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        byte[] pss = out.toByteArray();
        this.password = pss;
        Base64.Encoder encoder = Base64.getEncoder();
        this.string = encoder.encodeToString(pss);
    }

    public TestEncryptedPasswordValue(final String s) {
        Base64.Decoder decoder = Base64.getDecoder();
        this.password = decoder.decode(s);
        this.string = s;
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
        TestEncryptedPasswordValue other =
                (TestEncryptedPasswordValue) obj;
        return this.string.equals(other.string);
    }

    @Override
    public char[] getPassword() {
        List<Character> characters = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(
                this.password))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                characters.add((char) ch);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        int charactersCount = characters.size();
        char[] psswrd = new char[charactersCount];
        for (int i = 0; i < charactersCount; i++) {
            psswrd[i] = characters.get(i);
        }
        return psswrd;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.string.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
