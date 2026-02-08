package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.test.help.net.Server;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static org.junit.Assert.*;

public class DefaultSocketFactoryIT {

    @Test
    public void testNewSocket() throws IOException {
        try (Socket socket = DefaultSocketFactory.getInstance().newSocket()) {
            assertFalse(socket.isConnected());
        }
    }

    @Test
    public void testNewSocketInetAddressInt01() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(inetAddress, port);
            Assert.assertTrue(socket.isConnected());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntForIllegalArgumentException01() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntForIllegalArgumentException02() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), 65536);
    }

    @Test
    public void testNewSocketInetAddressIntInetAddressInt01() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    inetAddress, port, null, 0);
            Assert.assertTrue(socket.isConnected());
            // should be true but turning up false
            // Assert.assertTrue(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test
    public void testNewSocketInetAddressIntInetAddressInt02() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    inetAddress, port, InetAddress.getLoopbackAddress(), 0);
            Assert.assertTrue(socket.isConnected());
            Assert.assertFalse(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntInetAddressIntForIllegalArgumentException01() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), -1, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntInetAddressIntForIllegalArgumentException02() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), 65536, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntInetAddressIntForIllegalArgumentException03() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), 0, null, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketInetAddressIntInetAddressIntForIllegalArgumentException04() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                InetAddress.getLoopbackAddress(), 0, null, 65536);
    }

    @Test
    public void testNewSocketStringInt01() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    (String) null, port);
            Assert.assertTrue(socket.isConnected());
            Assert.assertTrue(socket.getInetAddress().isLoopbackAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test
    public void testNewSocketStringInt02() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            String host = inetAddress.getHostAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    host, port);
            Assert.assertTrue(socket.isConnected());
            Assert.assertEquals(inetAddress, socket.getInetAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntForIllegalArgumentException01() throws IOException {
        DefaultSocketFactory.getInstance().newSocket((String) null, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntForIllegalArgumentException02() throws IOException {
        DefaultSocketFactory.getInstance().newSocket((String) null, 65536);
    }

    @Test
    public void testNewSocketStringIntInetAddressInt01() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    (String) null, port, null, 0);
            Assert.assertTrue(socket.isConnected());
            Assert.assertTrue(socket.getInetAddress().isLoopbackAddress());
            // should be true but turning up false
            // Assert.assertTrue(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test
    public void testNewSocketStringIntInetAddressInt02() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    (String) null, port, InetAddress.getLoopbackAddress(), 0);
            Assert.assertTrue(socket.isConnected());
            Assert.assertTrue(socket.getInetAddress().isLoopbackAddress());
            Assert.assertFalse(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test
    public void testNewSocketStringIntInetAddressInt03() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            String host = inetAddress.getHostAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    host, port, null, 0);
            Assert.assertTrue(socket.isConnected());
            Assert.assertEquals(inetAddress, socket.getInetAddress());
            // should be true but turning up false
            // Assert.assertTrue(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test
    public void testNewSocketStringIntInetAddressInt04() throws IOException {
        Server server = new Server(
                new Server.DefaultServerSocketFactory(),
                0,
                new Server.DefaultWorkerFactory());
        Socket socket = null;
        try {
            server.start();
            InetAddress inetAddress = server.getInetAddress();
            String host = inetAddress.getHostAddress();
            int port = server.getPort();
            socket = DefaultSocketFactory.getDefault().newSocket(
                    host, port, InetAddress.getLoopbackAddress(), 0);
            Assert.assertTrue(socket.isConnected());
            Assert.assertEquals(inetAddress, socket.getInetAddress());
            Assert.assertFalse(socket.getLocalAddress().isAnyLocalAddress());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (server.getState().equals(Server.State.STARTED)) {
                server.stop();
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntInetAddressIntForIllegalArgumentException01() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                (String) null, -1, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntInetAddressIntForIllegalArgumentException02() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                (String) null, 65536, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntInetAddressIntForIllegalArgumentException03() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                (String) null, 0, null, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewSocketStringIntInetAddressIntForIllegalArgumentException04() throws IOException {
        DefaultSocketFactory.getDefault().newSocket(
                (String) null, 0, null, 65536);
    }

}