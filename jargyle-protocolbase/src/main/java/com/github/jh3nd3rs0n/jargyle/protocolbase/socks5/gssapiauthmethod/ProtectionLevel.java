package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;
import org.ietf.jgss.MessageProp;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Security context protection level.
 */
@EnumValueTypeDoc(
        description = "Security context protection level",
        name = "SOCKS5 GSS-API Authentication Method Protection Level",
        syntax = "NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF|SELECTIVE_INTEG_OR_CONF",
        syntaxName = "SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL"
)
public enum ProtectionLevel {

    /**
     * No per-message protection. Returns a {@code null} {@code MessageProp}.
     */
    @EnumValueDoc(description = "No per-message protection", value = "NONE")
    NONE((byte) 0x00) {
        @Override
        public MessageProp newMessageProp(
                final int suggestedQOP, final boolean suggestedPrivState) {
            return null;
        }

    },

    /**
     * Required per-message integrity. Returns a new {@code MessageProp} with
     * its QOP value set to 0 and its privacy state set to {@code false}.
     */
    @EnumValueDoc(
            description = "Required per-message integrity",
            value = "REQUIRED_INTEG"
    )
    REQUIRED_INTEG((byte) 0x01) {
        @Override
        public MessageProp newMessageProp(
                final int suggestedQOP, final boolean suggestedPrivState) {
            return new MessageProp(0, false);
        }

    },

    /**
     * Required per-message integrity and confidentiality. Returns a new
     * {@code MessageProp} with its QOP value set to 0 and its privacy state
     * set to {@code true}.
     */
    @EnumValueDoc(
            description = "Required per-message integrity and confidentiality",
            value = "REQUIRED_INTEG_AND_CONF"
    )
    REQUIRED_INTEG_AND_CONF((byte) 0x02) {
        @Override
        public MessageProp newMessageProp(
                final int suggestedQOP, final boolean suggestedPrivState) {
            return new MessageProp(0, true);
        }

    },

    /**
     * Selective per-message integrity or confidentiality based on local
     * client and server configurations. Returns a new {@code MessageProp}
     * with its QOP value set to the provided suggested QOP value and its
     * privacy state set to the provided suggested private state.
     */
    @EnumValueDoc(
            description = "Selective per-message integrity or confidentiality "
                    + "based on local client and server configurations",
            value = "SELECTIVE_INTEG_OR_CONF"
    )
    SELECTIVE_INTEG_OR_CONF((byte) 0x03) {
        @Override
        public MessageProp newMessageProp(
                final int suggestedQOP, final boolean suggestedPrivState) {
            return new MessageProp(suggestedQOP, suggestedPrivState);
        }
    };

    /**
     * The {@code byte} value associated with this {@code ProtectionLevel}.
     */
    private final byte byteValue;

    /**
     * Constructs a {@code ProtectionLevel} with the provided {@code byte}
     * value.
     *
     * @param bValue the provided {@code byte} value
     */
    ProtectionLevel(final byte bValue) {
        this.byteValue = bValue;
    }

    /**
     * Returns the enum constant associated with the provided {@code byte}
     * value. An {@code IllegalArgumentException} is thrown if there is no
     * enum constant associated with the provided {@code byte} value.
     *
     * @param b the provided {@code byte} value
     * @return the enum constant associated with the provided {@code byte}
     * value
     */
    public static ProtectionLevel valueOfByte(final byte b) {
        for (ProtectionLevel protectionLevel : ProtectionLevel.values()) {
            if (protectionLevel.byteValue() == b) {
                return protectionLevel;
            }
        }
        String str = Arrays.stream(ProtectionLevel.values())
                .map(ProtectionLevel::byteValue)
                .map(bv -> UnsignedByte.valueOf(bv).intValue())
                .map(Integer::toHexString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected protection level must be one of the following "
                        + "values: %s. actual value is %s",
                str,
                Integer.toHexString(UnsignedByte.valueOf(b).intValue())));
    }

    /**
     * Returns the enum constant of the provided {@code String}. An
     * {@code IllegalArgumentException} is thrown if there is no enum constant
     * of the provided {@code String}.
     *
     * @param s the provided {@code String}
     * @return the enum constant of the provided {@code String}
     */
    public static ProtectionLevel valueOfString(final String s) {
        ProtectionLevel protectionLevel;
        try {
            protectionLevel = ProtectionLevel.valueOf(s);
        } catch (IllegalArgumentException e) {
            String str = Arrays.stream(ProtectionLevel.values())
                    .map(ProtectionLevel::toString)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(String.format(
                    "expected protection level must be one of the following "
                            + "values: %s. actual value is %s",
                    str,
                    s));
        }
        return protectionLevel;
    }

    /**
     * Returns the {@code byte} value associated with this
     * {@code ProtectionLevel}.
     *
     * @return the {@code byte} value associated with this
     * {@code ProtectionLevel}
     */
    public byte byteValue() {
        return this.byteValue;
    }

    /**
     * Returns a new {@code MessageProp} (or {@code null}) based on this
     * {@code ProtectionLevel}.
     *
     * @param suggestedQOP       the provided suggested quality-of-protection
     *                           (QOP) value
     * @param suggestedPrivState the provided suggested privacy
     *                           (i.e. confidentiality) state
     * @return a new {@code MessageProp} (or {@code null}) based on this
     * {@code ProtectionLevel}
     */
    public abstract MessageProp newMessageProp(
            final int suggestedQOP, final boolean suggestedPrivState);

}