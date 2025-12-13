package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.internal.annotation.ValuesValueTypeDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A collection of {@code ProtectionLevel}s.
 */
@ValuesValueTypeDoc(
        description = "A comma separated list of security context protection levels",
        elementValueType = ProtectionLevel.class,
        name = "SOCKS5 GSS-API Method Protection Levels",
        syntax = "SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL2[...]]",
        syntaxName = "SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS"
)
public final class ProtectionLevels {

    /**
     * The {@code List} of {@code ProtectionLevel}s.
     */
    private final List<ProtectionLevel> protectionLevels;

    /**
     * Constructs a {@code ProtectionLevels} of the provided {@code List} of
     * {@code ProtectionLevel}s.
     *
     * @param protectionLvls the provided {@code List} of
     *                       {@code ProtectionLevel}s
     */
    private ProtectionLevels(final List<ProtectionLevel> protectionLvls) {
        this.protectionLevels = new ArrayList<>(protectionLvls);
    }

    /**
     * Returns a {@code ProtectionLevels} of the first provided
     * {@code ProtectionLevel} and the provided {@code List} of the
     * additional {@code ProtectionLevel}s.
     *
     * @param protectionLvl  the first provided {@code ProtectionLevel}
     * @param protectionLvls the provided {@code List} of additional
     *                       {@code ProtectionLevel}s
     * @return a {@code ProtectionLevels} of the first provided
     * {@code ProtectionLevel} and the provided {@code List} of the
     * additional {@code ProtectionLevel}s
     */
    public static ProtectionLevels of(
            final ProtectionLevel protectionLvl,
            final List<ProtectionLevel> protectionLvls) {
        List<ProtectionLevel> list = new ArrayList<>();
        list.add(protectionLvl);
        list.addAll(protectionLvls);
        return new ProtectionLevels(list);
    }

    /**
     * Returns a {@code ProtectionLevels} of the first provided
     * {@code ProtectionLevel} and the provided additional
     * {@code ProtectionLevel}s.
     *
     * @param protectionLvl  the first provided {@code ProtectionLevel}
     * @param protectionLvls the provided additional {@code ProtectionLevel}s
     * @return a {@code ProtectionLevels} of the first provided
     * {@code ProtectionLevel} and the provided additional
     * {@code ProtectionLevel}s
     */
    public static ProtectionLevels of(
            final ProtectionLevel protectionLvl,
            final ProtectionLevel... protectionLvls) {
        return of(protectionLvl, Arrays.asList(protectionLvls));
    }

    /**
     * Returns a new instance of {@code ProtectionLevels} from the provided
     * {@code String}. The provided {@code String} is a comma separated list
     * of {@code String} representations of {@code ProtectionLevel}s. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} is invalid.
     *
     * @param s the provided {@code String}
     * @return a new instance of {@code ProtectionLevels} from the provided
     * {@code String}
     */
    public static ProtectionLevels newInstanceFrom(final String s) {
        List<ProtectionLevel> protectionLevels = new ArrayList<>();
        String[] sElements = s.split(",", -1);
        for (String sElement : sElements) {
            protectionLevels.add(ProtectionLevel.valueOfString(sElement));
        }
        return new ProtectionLevels(protectionLevels);
    }

    /**
     * Returns the {@code boolean} value to indicate if this
     * {@code ProtectionLevels} contains the provided {@code ProtectionLevel}.
     *
     * @param protectionLvl the provided {@code ProtectionLevel}
     * @return the {@code boolean} value to indicate if this
     * {@code ProtectionLevels} contains the provided {@code ProtectionLevel}
     */
    public boolean contains(final ProtectionLevel protectionLvl) {
        return this.protectionLevels.contains(protectionLvl);
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
        ProtectionLevels other = (ProtectionLevels) obj;
        return this.protectionLevels.equals(other.protectionLevels);
    }

    /**
     * Returns the first {@code ProtectionLevel} of this
     * {@code ProtectionLevels}.
     *
     * @return the first {@code ProtectionLevel} of this
     * {@code ProtectionLevels}
     */
    public ProtectionLevel getFirst() {
        return this.protectionLevels.get(0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.protectionLevels.hashCode();
        return result;
    }

    /**
     * Returns an unmodifiable {@code List} of {@code ProtectionLevel}s.
     *
     * @return an unmodifiable {@code List} of {@code ProtectionLevel}s
     */
    public List<ProtectionLevel> toList() {
        return Collections.unmodifiableList(this.protectionLevels);
    }

    /**
     * Returns the {@code String} representation of this
     * {@code ProtectionLevels}. The {@code String} representation is a comma
     * separated list of {@code String} representations of
     * {@code ProtectionLevel}s.
     *
     * @return the {@code String} representation of this
     * {@code ProtectionLevels}
     */
    @Override
    public String toString() {
        return this.protectionLevels.stream()
                .map(ProtectionLevel::toString)
                .collect(Collectors.joining(","));
    }

}
