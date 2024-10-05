package com.github.jh3nd3rs0n.jargyle.test.help.resource;

import com.github.jh3nd3rs0n.jargyle.test.help.io.IoHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * An {@code Object} representation of a Java resource.
 */
public final class Resource {

    /**
     * The class whose {@code ClassLoader} is used to obtain the Java resource.
     */
    private final Class<?> cls;

    /**
     * The name of the Java resource.
     */
    private final String name;

    /**
     * Constructs a {@code Resource} with the provided class whose
     * {@code ClassLoader} is used to obtain the Java resource and the
     * provided name of the Java resource.
     *
     * @param c  the provided class whose {@code ClassLoader} is used to obtain
     *           the Java resource
     * @param nm the provided name of the Java resource
     */
    public Resource(Class<?> c, final String nm) {
        this.cls = Objects.requireNonNull(c);
        this.name = Objects.requireNonNull(nm);
    }

    /**
     * Returns the content from the Java resource as a {@code String}.
     *
     * @return the content from the Java resource as a {@code String}
     */
    public String getContentAsString() {
        ClassLoader classLoader = this.cls.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(this.name);
        try {
            assert in != null;
            try (Reader reader =
                         new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return IoHelper.readStringFrom(reader);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns the Java resource as an {@code InputStream}.
     *
     * @return the Java resource as an {@code InputStream}
     */
    public InputStream getInputStream() {
        ClassLoader classLoader = this.cls.getClassLoader();
        return classLoader.getResourceAsStream(this.name);
    }

}
