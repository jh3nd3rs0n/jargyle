package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Request;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassauthmethod.Response;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauthmethod.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

final class UsernamePasswordAuthMethodSubNegotiator
        extends MethodSubNegotiator {

    public UsernamePasswordAuthMethodSubNegotiator() {
        super(Method.USERNAME_PASSWORD);
    }

    @Override
    public MethodSubNegotiationResults subNegotiate(
            final Socket socket,
            final Configuration configuration) throws IOException {
        String username = null;
        char[] password = null;
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Request request = Request.newInstanceFrom(inputStream);
            Response response = null;
            username = request.getUsername();
            password = request.getPassword();
            UserRepository userRepository =
                    Socks5ValueDerivationHelper.getSocks5UserpassAuthMethodUserRepositoryFrom(
                            configuration.getSettings());
            User user = userRepository.get(username);
            if (user == null) {
                response = Response.newInstance(UnsignedByte.valueOf(
                        (byte) 0x01));
                outputStream.write(response.toByteArray());
                outputStream.flush();
                throw new MethodSubNegotiationException(
                        this.getMethod(),
                        String.format(
                                "invalid username password from %s",
                                socket));
            }
            HashedPassword hashedPassword = user.getHashedPassword();
            if (!hashedPassword.passwordEquals(password)) {
                response = Response.newInstance(UnsignedByte.valueOf(
                        (byte) 0x01));
                outputStream.write(response.toByteArray());
                outputStream.flush();
                throw new MethodSubNegotiationException(
                        this.getMethod(),
                        String.format(
                                "invalid username password from %s",
                                socket));
            }
            response = Response.newInstance(Response.STATUS_SUCCESS);
            outputStream.write(response.toByteArray());
            outputStream.flush();
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
        MethodEncapsulation methodEncapsulation =
                MethodEncapsulation.newNullInstance(socket);
        return new MethodSubNegotiationResults(
                this.getMethod(), methodEncapsulation, username);
    }

}
