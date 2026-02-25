package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientAgent;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import org.ietf.jgss.Oid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Socks5ClientAgent extends SocksClientAgent {

    private final Properties properties;
    
    private final Socks5Client socks5Client;

    /**
     * Constructs a {@code Socks5ClientAgent} with the provided
     * {@code Socks5Client}.
     *
     * @param client the provided {@code Socks5Client}
     */
    public Socks5ClientAgent(final Socks5Client client) {
        super(client);
        this.properties = client.getProperties();
        this.socks5Client = client;
    }

    public boolean canSocks5HostResolverResolveFromSocks5Server() {
        Boolean resolveFromSocks5Server = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER);
        if (resolveFromSocks5Server != null) {
            return resolveFromSocks5Server;
        }
        return this.canSocksHostResolverResolveFromSocksServer();
    }

    public MethodEncapsulation doMethodSubNegotiation(
            final Method method,
            final Socket connectedClientSocket) throws IOException {
        MethodSubNegotiator methodSubNegotiator =
                MethodSubNegotiator.getInstance(method);
        return methodSubNegotiator.subNegotiate(
                connectedClientSocket, this);
    }

    @Override
    public Oid getGssapiAuthMethodMechanismOid() {
        Oid mechanismOid = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID);
        if (mechanismOid != null) {
            return mechanismOid;
        }
        return super.getGssapiAuthMethodMechanismOid();
    }

    @Override
    public boolean getGssapiAuthMethodNecReferenceImpl() {
        Boolean necReferenceImpl = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
        if (necReferenceImpl != null) {
            return necReferenceImpl;
        }
        return super.getGssapiAuthMethodNecReferenceImpl();
    }

    @Override
    public CommaSeparatedValues getGssapiAuthMethodProtectionLevels() {
        ProtectionLevels protectionLevels = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
        if (protectionLevels != null) {
            return CommaSeparatedValues.newInstanceFrom(
                    protectionLevels.toString());
        }
        return super.getGssapiAuthMethodProtectionLevels();
    }

    @Override
    public String getGssapiAuthMethodServiceName() {
        String serviceName = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME);
        if (serviceName != null) {
            return serviceName;
        }
        return super.getGssapiAuthMethodServiceName();
    }

    @Override
    public boolean getGssapiAuthMethodSuggestedConf() {
        Boolean suggestedConf = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
        if (suggestedConf != null) {
            return suggestedConf;
        }
        return super.getGssapiAuthMethodSuggestedConf();
    }

    @Override
    public int getGssapiAuthMethodSuggestedInteg() {
        Integer suggestedInteg = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
        if (suggestedInteg != null) {
            return suggestedInteg;
        }
        return super.getGssapiAuthMethodSuggestedInteg();
    }

    @Override
    public CommaSeparatedValues getMethods() {
        CommaSeparatedValues methods = CommaSeparatedValues.newInstanceFrom(
                this.properties.getValue(
                        Socks5PropertySpecConstants.SOCKS5_METHODS).toString());
        if (methods.toArray().length == 0) {
            methods = super.getMethods();
        }
        UserInfo userInfo = this.getSocksServerUri().getUserInfo();
        if (userInfo == null) {
            return methods;
        }
        UsernamePassword usernamePassword =
                UsernamePassword.tryNewInstanceFrom(userInfo.toString());
        if (usernamePassword == null) {
            return methods;
        }
        Methods meths = Methods.newInstanceFrom(methods.toString());
        if (meths.contains(Method.USERNAME_PASSWORD)) {
            return methods;
        }
        if (meths.equals(Methods.of(Method.NO_AUTHENTICATION_REQUIRED))) {
            return CommaSeparatedValues.of(Method.USERNAME_PASSWORD.toString());
        }
        List<String> m = new ArrayList<>();
        m.add(Method.USERNAME_PASSWORD.toString());
        m.addAll(Arrays.asList(methods.toArray()));
        return CommaSeparatedValues.of(m.toArray(new String[0]));
    }

    public Socks5Client getSocks5Client() {
        return this.socks5Client;
    }

    @Override
    public EncryptedPassword getUserpassAuthMethodPassword() {
        EncryptedPassword password = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD);
        if (password != null) {
            return password;
        }
        return super.getUserpassAuthMethodPassword();
    }

    @Override
    public String getUserpassAuthMethodUsername() {
        String username = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME);
        if (username != null) {
            return username;
        }
        return super.getUserpassAuthMethodUsername();
    }

    public boolean isSocks5DatagramSocketClientInfoUnavailable() {
        Boolean clientInfoUnavailable = this.properties.getValue(
                Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE);
        if (clientInfoUnavailable != null) {
            return clientInfoUnavailable;
        }
        return this.isSocksDatagramSocketClientInfoUnavailable();
    }

    public Method negotiateMethod(
            final Socket connectedClientSocket) throws IOException {
        Methods methods = Methods.newInstanceFrom(
                this.getMethods().toString());
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
