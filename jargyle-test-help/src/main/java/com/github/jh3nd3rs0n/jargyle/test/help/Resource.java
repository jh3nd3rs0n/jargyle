package com.github.jh3nd3rs0n.jargyle.test.help;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * An {@code Object} representation of a Java resource.
 */
public final class Resource {

    /**
     * The name of the Java resource.
     */
    private final String name;

    /**
     * Constructs a {@code Resource} with the provided name.
     *
     * @param nm the provided name
     */
    public Resource(final String nm) {
        this.name = Objects.requireNonNull(nm);
    }

    /**
     * Returns the content from the Java resource as a {@code String}.
     *
     * @return the content from the Java resource as a {@code String}
     */
    public String getContentAsString() {
        ClassLoader classLoader = Resource.class.getClassLoader();
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
     * Returns the Java resource as a {@code File}.
     *
     * @return the Java resource as a {@code File}
     */
    public File getFile() {
        ClassLoader classLoader = Resource.class.getClassLoader();
        URL url = classLoader.getResource(this.name);
        try {
            assert url != null;
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
