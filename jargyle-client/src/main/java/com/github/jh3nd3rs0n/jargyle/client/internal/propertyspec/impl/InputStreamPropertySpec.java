package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class InputStreamPropertySpec extends PropertySpec<InputStream> {

    public InputStreamPropertySpec(
            final String n, final InputStream defaultVal) {
        super(n, InputStream.class, defaultVal);
    }

    @Override
    protected InputStream parse(final String value) {
        File file = new File(value);
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' does not exist",
                    value));
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' must be a file",
                    value));
        }
        try {
            return Files.newInputStream(file.toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    String.format("unable to open file `%s'", value),
                    e);
        }
    }

}
