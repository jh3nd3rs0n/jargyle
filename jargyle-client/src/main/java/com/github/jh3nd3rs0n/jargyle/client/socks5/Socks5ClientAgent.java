package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientAgent;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

final class Socks5ClientAgent extends SocksClientAgent {

    private final Socks5Client socks5Client;

    /**
     * Constructs a {@code Socks5ClientAgent} with the provided
     * {@code Socks5Client}.
     *
     * @param client the provided {@code Socks5Client}
     */
    public Socks5ClientAgent(final Socks5Client client) {
        super(client);
        this.socks5Client = client;
    }

    public MethodEncapsulation doMethodSubNegotiation(
            final Method method,
            final Socket connectedClientSocket) throws IOException {
        MethodSubNegotiator methodSubNegotiator =
                MethodSubNegotiator.getInstance(method);
        return methodSubNegotiator.subNegotiate(
                connectedClientSocket, this.socks5Client);
    }

    private Methods getMethods() {
        Methods methods = this.getProperties().getValue(
                Socks5PropertySpecConstants.SOCKS5_METHODS);
        UserInfo userInfo = this.getSocksServerUri().getUserInfo();
        if (userInfo == null) {
            return methods;
        }
        UsernamePassword usernamePassword =
                UsernamePassword.tryNewInstanceFrom(userInfo.toString());
        if (usernamePassword == null) {
            return methods;
        }
        if (methods.contains(Method.USERNAME_PASSWORD)) {
            return methods;
        }
        if (methods.equals(Methods.getDefault())) {
            return Methods.of(Method.USERNAME_PASSWORD);
        }
        List<Method> meths = new ArrayList<>();
        meths.add(Method.USERNAME_PASSWORD);
        meths.addAll(methods.toList());
        return Methods.of(meths);
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    public Method negotiateMethod(
            final Socket connectedClientSocket) throws IOException {
        Methods methods = this.getMethods();
        ClientMethodSelectionMessage cmsm =
                ClientMethodSelectionMessage.newInstance(methods);
        InputStream inputStream = connectedClientSocket.getInputStream();
        OutputStream outputStream = connectedClientSocket.getOutputStream();
        outputStream.write(cmsm.toByteArray());
        outputStream.flush();
        ServerMethodSelectionMessage smsm =
                ServerMethodSelectionMessage.newInstanceFrom(inputStream);
        return smsm.getMethod();
    }

    public Reply receiveReply(
            final Socket connectedClientSocket) throws IOException {
        InputStream inputStream = connectedClientSocket.getInputStream();
        Reply rep = Reply.newInstanceFrom(inputStream);
        ReplyCode replyCode = rep.getReplyCode();
        if (!replyCode.equals(ReplyCode.SUCCEEDED)) {
            throw new FailureReplyException(this.socks5Client, rep);
        }
        return rep;
    }

    public void sendRequest(
            final Request req,
            final Socket connectedClientSocket) throws IOException {
        OutputStream outputStream = connectedClientSocket.getOutputStream();
        outputStream.write(req.toByteArray());
        outputStream.flush();
    }

}
