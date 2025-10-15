package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class UsernamePasswordMethodSubNegotiator
        extends MethodSubNegotiator {

    public UsernamePasswordMethodSubNegotiator() {
        super(Method.USERNAME_PASSWORD);
    }

    private UsernamePassword getUsernamePassword(
            final Socks5Client socks5Client) {
        UsernamePassword usernamePassword = UsernamePassword.newInstance(
                socks5Client.getProperties().getValue(
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME),
                socks5Client.getProperties().getValue(
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD).getPassword());
        UserInfo userInfo = socks5Client.getSocksServerUri().getUserInfo();
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
            final Socks5Client socks5Client) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        UsernamePassword usernamePassword = this.getUsernamePassword(
                socks5Client);
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
