package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Request;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class UsernamePasswordAuthMethodSubNegotiator
        extends MethodSubNegotiator {

    public UsernamePasswordAuthMethodSubNegotiator() {
        super(Method.USERNAME_PASSWORD);
    }

    private UsernamePassword getUsernamePassword(
            final Socks5ClientAgent socks5ClientAgent) {
        UsernamePassword usernamePassword = UsernamePassword.newInstance(
                socks5ClientAgent.getUserpassAuthMethodUsername(),
                socks5ClientAgent.getUserpassAuthMethodPassword().getPassword());
        UserInfo userInfo = socks5ClientAgent.getSocksServerUri().getUserInfo();
        if (userInfo == null) {
            return usernamePassword;
        }
        UsernamePassword userPass = UsernamePassword.tryNewInstanceFrom(
                userInfo.toString());
        if (userPass == null) {
            return usernamePassword;
        }
        return userPass;
    }

    @Override
    public MethodEncapsulation subNegotiate(
            final Socket socket,
            final Socks5ClientAgent socks5ClientAgent) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        UsernamePassword usernamePassword = this.getUsernamePassword(
                socks5ClientAgent);
        Request request = Request.newInstance(
                usernamePassword.getUsername(),
                usernamePassword.getPassword());
        outputStream.write(request.toByteArray());
        outputStream.flush();
        Response response = Response.newInstanceFrom(inputStream);
        UnsignedByte status = response.getStatus();
        if (!status.equals(Response.STATUS_SUCCESS)) {
            throw new MethodSubNegotiationException(
                    this.getMethod(), "invalid username password");
        }
        return MethodEncapsulation.newNullInstance(socket);
    }

}
