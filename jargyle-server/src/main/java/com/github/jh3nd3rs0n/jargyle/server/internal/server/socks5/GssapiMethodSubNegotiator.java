package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.*;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import org.ietf.jgss.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class GssapiMethodSubNegotiator
        extends MethodSubNegotiator {

    public GssapiMethodSubNegotiator() {
        super(Method.GSSAPI);
    }

    private void establishContext(
            final Socket socket, final GSSContext context)
            throws IOException, GSSException {
        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        byte[] token;
        while (!context.isEstablished()) {
            AuthenticationMessage message =
                    AuthenticationMessage.newInstanceFromClient(inStream);
            token = message.getToken().getBytes();
            try {
                token = context.acceptSecContext(token, 0, token.length);
            } catch (GSSException e) {
                outStream.write(AbortMessage.INSTANCE.toByteArray());
                outStream.flush();
                throw e;
            }
            if (token == null) {
                outStream.write(AuthenticationMessage.newInstance(
                        Token.newInstance(new byte[]{})).toByteArray());
                outStream.flush();
            } else {
                outStream.write(AuthenticationMessage.newInstance(
                        Token.newInstance(token)).toByteArray());
                outStream.flush();
            }
        }
    }

    private ProtectionLevel negotiateProtectionLevel(
            final Socket socket,
            final GSSContext context,
            final Configuration configuration)
            throws IOException, GSSException {
        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        ProtectionLevelNegotiationMessage message =
                ProtectionLevelNegotiationMessage.newInstanceFrom(
                        inStream);
        boolean necReferenceImpl = configuration.getSettings().getLastValue(
                Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL).booleanValue();
        byte[] token = message.getToken().getBytes();
        if (!necReferenceImpl) {
            token = context.unwrap(token, 0, token.length,
                    new MessageProp(0, true));
        }
        ProtectionLevel protectionLevel;
        try {
            protectionLevel = ProtectionLevel.valueOfByte(token[0]);
        } catch (IllegalArgumentException e) {
            throw new MethodSubNegotiationException(this.getMethod(), e);
        }
        ProtectionLevels protectionLevels =
                configuration.getSettings().getLastValue(
                        Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS);
        ProtectionLevel protectionLevelChoice = protectionLevel;
        if (!protectionLevels.contains(protectionLevelChoice)) {
            protectionLevelChoice = protectionLevels.getFirst();
        }
        token = new byte[]{protectionLevelChoice.byteValue()};
        if (!necReferenceImpl) {
            token = context.wrap(token, 0, token.length,
                    new MessageProp(0, true));
        }
        outStream.write(ProtectionLevelNegotiationMessage.newInstance(
                Token.newInstance(token)).toByteArray());
        outStream.flush();
        if (socket.isClosed()) {
            throw new MethodSubNegotiationException(
                    this.getMethod(),
                    String.format(
                            "client %s closed due to client finding "
                                    + "choice of protection level unacceptable",
                            socket));
        }
        return protectionLevelChoice;
    }

    private GSSContext newContext() throws GSSException {
        GSSManager manager = GSSManager.getInstance();
        return manager.createContext((GSSCredential) null);
    }

    @Override
    public MethodSubNegotiationResults subNegotiate(
            final Socket socket,
            final Configuration configuration) throws IOException {
        GSSContext context = null;
        ProtectionLevel protectionLevelChoice = null;
        String user = null;
        try {
            context = this.newContext();
            this.establishContext(socket, context);
            protectionLevelChoice = this.negotiateProtectionLevel(
                    socket, context, configuration);
            user = context.getSrcName().toString();
        } catch (IOException e) {
            if (context != null) {
                try {
                    context.dispose();
                } catch (GSSException ex) {
                    throw new MethodSubNegotiationException(
                            this.getMethod(), ex);
                }
            }
            throw e;
        } catch (GSSException e) {
            if (context != null) {
                try {
                    context.dispose();
                } catch (GSSException ex) {
                    throw new MethodSubNegotiationException(
                            this.getMethod(), ex);
                }
            }
            throw new MethodSubNegotiationException(this.getMethod(), e);
        }
        MessageProp msgProp = protectionLevelChoice.newMessageProp(
                configuration.getSettings().getLastValue(
                        Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG),
                configuration.getSettings().getLastValue(
                        Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF));
        MethodEncapsulation methodEncapsulation =
                new GssapiMethodEncapsulation(socket, context, msgProp);
        return new MethodSubNegotiationResults(
                this.getMethod(), methodEncapsulation, user);
    }

}
