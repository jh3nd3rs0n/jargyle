package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

import java.io.File;

/**
 * An implementation of {@code PropertySpec} of type {@code File}
 * that accepts an existing file.
 */
public final class FilePropertySpec extends PropertySpec<File> {

    /**
     * Constructs a {@code FilePropertySpec} with the provided name and the
     * provided default value.
     *
     * @param n          the provided name
     * @param defaultVal the provided default value (can be {@code null})
     */
    public FilePropertySpec(final String n, final File defaultVal) {
        super(n, File.class, defaultVal);
    }

    @Override
    protected File parse(final String value) {
        return new File(value);
    }

    @Override
    protected File validate(final File value) {
        if (!value.exists()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' does not exist",
                    value));
        }
        if (!value.isFile()) {
            throw new IllegalArgumentException(String.format(
                    "`%s' must be a file",
                    value));
        }
        return value;
    }

}
