package com.github.jh3nd3rs0n.jargyle.integration.test;

import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramTestServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public final class EchoDatagramTestServerHelper {

    private EchoDatagramTestServerHelper() {
    }

    public static DatagramTestServer newEchoDatagramTestServer(final int port) {
        return newEchoDatagramTestServer(port, DatagramTestServer.INET_ADDRESS);
    }

    public static DatagramTestServer newEchoDatagramTestServer(
            final int port, final InetAddress inetAddress) {
        return new DatagramTestServer(
                new DatagramTestServer.DefaultServerDatagramSocketFactory(),
                port,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrDoubleThreadPoolFactory(),
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory());
    }

    private static final class EchoWorker extends DatagramTestServer.Worker {

        public EchoWorker(final DatagramTestServer.ClientPackets clientPckts) {
            super(clientPckts);
        }

        @Override
        protected void doWork() {
            DatagramPacket packet = this.getClientPackets().removeReceived();
            byte[] bytes = Arrays.copyOfRange(
                    packet.getData(),
                    packet.getOffset(),
                    packet.getLength());
            packet = new DatagramPacket(
                    bytes,
                    bytes.length,
                    packet.getSocketAddress());
            this.getClientPackets().addSendable(packet);
        }

    }

    private static final class EchoWorkerFactory
            extends DatagramTestServer.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public DatagramTestServer.Worker newWorker(
                final DatagramTestServer.ClientPackets clientPckts) {
            return new EchoWorker(clientPckts);
        }

    }

}
