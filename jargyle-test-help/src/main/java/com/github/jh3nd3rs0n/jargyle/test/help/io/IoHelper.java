package com.github.jh3nd3rs0n.jargyle.test.help.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Helper class for I/O operations for testing
 */
public final class IoHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private IoHelper() {
    }

    /**
     * Reads the contents from the provided {@code File} and returns its
     * contents as a {@code String}.
     *
     * @param file the provided {@code File}
     * @return the provided {@code File}'s contents as a {@code String}
     * @throws IOException if an I/O error occurs
     */
    public static String readStringFrom(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (Reader reader = new InputStreamReader(
                Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            return readStringFrom(reader);
        }
    }

    /**
     * Reads the contents from the provided {@code Reader} and returns its
     * contents as a {@code String}.
     *
     * @param reader the provided {@code Reader}
     * @return the provided {@code Reader}'s contents as a {@code String}
     * @throws IOException if an I/O error occurs
     */
    public static String readStringFrom(
            final Reader reader) throws IOException {
        Objects.requireNonNull(reader);
        try (StringWriter writer = new StringWriter()) {
            int ch;
            while ((ch = reader.read()) != -1) {
                writer.write(ch);
            }
            writer.flush();
            return writer.toString();
        }
    }

    /**
     * Writes the provided {@code String} using the provided {@code Writer}.
     *
     * @param string the provided {@code String}
     * @param writer the provided {@code Writer}
     * @throws IOException if an I/O error occurs
     */
    public static void writeStringThenFlush(
            final String string, final Writer writer) throws IOException {
        Objects.requireNonNull(string);
        Objects.requireNonNull(writer);
        writer.write(string);
        writer.flush();
    }

    /**
     * Writes the provided {@code String} to the provided {@code File}.
     *
     * @param string the provided {@code String}
     * @param file   the provided {@code File}
     * @throws IOException if an I/O error occurs
     */
    public static void writeStringToFile(
            final String string, final File file) throws IOException {
        Objects.requireNonNull(string);
        Objects.requireNonNull(file);
        File tempFile = new File(file.toString().concat(".tmp"));
        try (Writer writer = new OutputStreamWriter(
                Files.newOutputStream(tempFile.toPath()), StandardCharsets.UTF_8)) {
            writeStringThenFlush(string, writer);
        }
        Files.move(
                tempFile.toPath(),
                file.toPath(),
                StandardCopyOption.ATOMIC_MOVE,
                StandardCopyOption.REPLACE_EXISTING);
    }

}
