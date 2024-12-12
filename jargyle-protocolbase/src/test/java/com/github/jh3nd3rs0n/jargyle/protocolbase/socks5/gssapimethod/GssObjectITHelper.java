package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import com.github.jh3nd3rs0n.jargyle.protocolbase.GssEnvironment;
import com.github.jh3nd3rs0n.jargyle.protocolbase.VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory;
import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import org.ietf.jgss.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GssObjectITHelper {

    private static GSSContext newEstablishedClientGSSContext(
            final Socket socket) throws IOException, GSSException {
        GSSManager manager = GSSManager.getInstance();
        GSSName serverName = manager.createName(
                GssEnvironment.SERVICE_NAME, null);
        Oid mechOid = new Oid(GssEnvironment.MECHANISM_OID);
        GSSContext context = manager.createContext(
                serverName,
                mechOid,
                null,
                GSSContext.DEFAULT_LIFETIME);
        context.requestMutualAuth(true);
        context.requestConf(true);
        context.requestInteg(true);
        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        byte[] token = new byte[] { };
        while (!context.isEstablished()) {
            if (token == null) {
                token = new byte[] { };
            }
            token = context.initSecContext(token, 0, token.length);
            if (token != null) {
                outStream.write(AuthenticationMessage.newInstance(
                        Token.newInstance(token)).toByteArray());
                outStream.flush();
            }
            if (!context.isEstablished()) {
                AuthenticationMessage message;
                try {
                    message = AuthenticationMessage.newInstanceFromServer(
                            inStream);
                } catch (AbortMessageException e) {
                    throw new IOException(e);
                }
                token = message.getToken().getBytes();
            }
        }
        return context;
    }

    private static GSSContext newEstablishedServerGSSContext(
            final Socket socket) throws IOException, GSSException {
        GSSManager manager = GSSManager.getInstance();
        GSSContext context = manager.createContext((GSSCredential) null);
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
        return context;
    }

    public static void testClientAndServerGssObjects(
            final GssObjectTester clientGssObjectTester,
            final GssObjectTester serverGssObjectTester) throws IOException, GSSException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new GssServerWorkerFactory(serverGssObjectTester));
        Socket socket = null;
        GSSContext gssContext = null;
        try {
            server.start();
            socket = new Socket(
                    server.getInetAddress(), server.getPort());
            gssContext = newEstablishedClientGSSContext(socket);
            clientGssObjectTester.testGssObject(socket, gssContext);
        } finally {
            if (gssContext != null) {
                gssContext.dispose();
            }
            if (socket != null) {
                socket.close();
            }
            if (!server.getState().equals(Server.State.STOPPED)) {
                server.stop();
            }
        }
    }

    @FunctionalInterface
    public interface GssObjectTester {

        void testGssObject(
                final Socket socket,
                final GSSContext gssContext) throws IOException;

    }

    private static final class GssServerWorker extends Server.Worker {

        private GSSContext gssContext;
        private final GssObjectTester serverGssObjectTester;

        public GssServerWorker(
                final Socket clientSock,
                final GssObjectTester serverGssObjTester) {
            super(clientSock);
            this.gssContext = null;
            this.serverGssObjectTester = serverGssObjTester;
        }

        private void doEstablishedGssContextWork() throws IOException, GSSException {
            this.serverGssObjectTester.testGssObject(
                    this.getClientSocket(), this.gssContext);
        }

        @Override
        protected void doWork() throws IOException {
            try {
                this.gssContext = newEstablishedServerGSSContext(
                        this.getClientSocket());
                this.doEstablishedGssContextWork();
            } catch (GSSException e) {
                throw new IOException(e);
            } finally {
                if (this.gssContext != null) {
                    try {
                        this.gssContext.dispose();
                    } catch (GSSException e) {
                        throw new IOException(e);
                    }
                }
            }
        }
    }

    private static final class GssServerWorkerFactory
            extends Server.WorkerFactory {

        private final GssObjectTester serverGssObjectTester;

        public GssServerWorkerFactory(
                final GssObjectTester serverGssObjTester) {
            this.serverGssObjectTester = serverGssObjTester;
        }

        @Override
        public Server.Worker newWorker(final Socket clientSocket) {
            return new GssServerWorker(
                    clientSocket, this.serverGssObjectTester);
        }
    }

}
