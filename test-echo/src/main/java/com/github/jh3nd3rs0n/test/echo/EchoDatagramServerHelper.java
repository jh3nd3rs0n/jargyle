package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.test.help.net.DatagramServer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public final class EchoDatagramServerHelper {

    private EchoDatagramServerHelper() {
    }

    public static DatagramServer newEchoDatagramServer(final int port) {
        return newEchoDatagramServer(port, DatagramServer.INET_ADDRESS);
    }

    public static DatagramServer newEchoDatagramServer(
            final int port, final InetAddress inetAddress) {
        return new DatagramServer(
                new DatagramServer.DefaultServerDatagramSocketFactory(),
                port,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory(),
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory());
    }

    private static final class EchoWorker extends DatagramServer.Worker {

        public EchoWorker(final DatagramServer.ClientPackets clientPckts) {
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
            extends DatagramServer.WorkerFactory {

        public EchoWorkerFactory() {
        }

        @Override
        public DatagramServer.Worker newWorker(
                final DatagramServer.ClientPackets clientPckts) {
            return new EchoWorker(clientPckts);
        }

    }

}
