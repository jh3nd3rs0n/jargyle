package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Exception;

/**
 * Thrown when the client receives an {@code AbortMessage} from the server.
 */
public final class AbortMessageException extends Socks5Exception {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an {@code AbortMessageException}.
     */
    public AbortMessageException() {
    }

}
