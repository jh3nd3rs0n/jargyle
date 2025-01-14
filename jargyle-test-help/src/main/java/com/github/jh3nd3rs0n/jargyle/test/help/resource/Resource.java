package com.github.jh3nd3rs0n.jargyle.test.help.resource;

import java.io.*;
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
     * Returns the content from the Java resource as a {@code byte} array.
     *
     * @return the content from the Java resource as a {@code byte} array
     */
    public byte[] getContentAsBytes() {
        ClassLoader classLoader = this.cls.getClassLoader();
        try (InputStream in = classLoader.getResourceAsStream(this.name)) {
            assert in != null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns the content from the Java resource as a {@code String}.
     *
     * @return the content from the Java resource as a {@code String}
     */
    public String getContentAsString() {
        ClassLoader classLoader = this.cls.getClassLoader();
        try (InputStream in = classLoader.getResourceAsStream(this.name)) {
            assert in != null;
            StringWriter writer = new StringWriter();
            try (Reader reader = new InputStreamReader(
                    in, StandardCharsets.UTF_8)) {
                int ch;
                while ((ch = reader.read()) != -1) {
                    writer.write(ch);
                }
                writer.flush();
            }
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
