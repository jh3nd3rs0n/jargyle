package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@code Method}s.
 */
@ValuesValueTypeDoc(
        description = "A comma-separated list of SOCKS5 authentication methods",
        elementValueType = Method.class,
        name = "SOCKS5 Methods",
        syntax = "[SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]",
        syntaxName = "SOCKS5_METHODS"
)
public final class Methods {

    /**
     * The default instance of {@code Methods}.
     */
    private static final Methods DEFAULT_INSTANCE = new Methods(
            List.of(Method.NO_AUTHENTICATION_REQUIRED));

    /**
     * The {@code List} of {@code Method}s.
     */
    private final List<Method> methods;

    /**
     * Constructs a {@code Methods} of the provided {@code List} of
     * {@code Method}s.
     *
     * @param meths the provided {@code List} of {@code Method}s
     */
    private Methods(final List<Method> meths) {
        this.methods = new ArrayList<>(meths);
    }

    /**
     * Returns the default instance of {@code Methods}. The default instance
     * contains only a {@code Method} of {@code NO_AUTHENTICATION_REQUIRED}.
     *
     * @return the default instance of {@code Methods}
     */
    public static Methods getDefault() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a {@code Methods} of the provided {@code List} of
     * {@code Method}s.
     *
     * @param meths the provided {@code List} of {@code Method}s
     * @return a {@code Methods} of the provided {@code List} of
     * {@code Method}s
     */
    public static Methods of(final List<Method> meths) {
        return new Methods(meths);
    }

    /**
     * Returns a {@code Methods} of the provided {@code Method}s.
     *
     * @param meths the provided {@code Method}s
     * @return a {@code Methods} of the provided {@code Method}s
     */
    public static Methods of(final Method... meths) {
        return of(Arrays.asList(meths));
    }

    /**
     * Returns a new instance of {@code Methods} from the provided
     * {@code String}. The provided {@code String} is a comma separated list
     * of {@code String} representations of {@code Method}s. If the provided
     * {@code String} is empty, an empty instance of {@code Methods} is
     * returned. An {@code IllegalArgumentException} is thrown if the
     * provided {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new instance of {@code Methods} from the provided
     * {@code String}
     */
    public static Methods newInstanceFrom(final String s) {
        List<Method> methods = new ArrayList<>();
        if (s.isEmpty()) {
            return new Methods(methods);
        }
        String[] sElements = s.split(",", -1);
        for (String sElement : sElements) {
            methods.add(Method.valueOfString(sElement));
        }
        return new Methods(methods);
    }

    /**
     * Returns the {@code boolean} value to indicate if this
     * {@code Methods} contains the provided {@code Method}.
     *
     * @param meth the provided {@code Method}
     * @return the {@code boolean} value to indicate if this
     * {@code Methods} contains the provided {@code Method}
     */
    public boolean contains(final Method meth) {
        return this.methods.contains(meth);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Methods other = (Methods) obj;
        return this.methods.equals(other.methods);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.methods.hashCode();
        return result;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code Method}s.
     *
     * @return an unmodifiable {@code List} of {@code Method}s
     */
    public List<Method> toList() {
        return Collections.unmodifiableList(this.methods);
    }

    /**
     * Returns the {@code String} representation of this {@code Methods}. The
     * {@code String} representation is a comma separated list of
     * {@code String} representations of {@code Method}s.
     *
     * @return the {@code String} representation of this {@code Methods}
     */
    @Override
    public String toString() {
        return this.methods.stream()
                .map(Method::toString)
                .collect(Collectors.joining(","));
    }

}
