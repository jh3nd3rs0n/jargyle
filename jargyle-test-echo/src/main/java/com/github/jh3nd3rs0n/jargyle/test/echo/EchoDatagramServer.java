package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public final class EchoDatagramServer {

    public static final int RECEIVE_BUFFER_SIZE = DatagramServer.RECEIVE_BUFFER_SIZE;

    private final DatagramServer datagramServer;

    private EchoDatagramServer(final DatagramServer server) {
        this.datagramServer = server;
    }

    public static EchoDatagramServer newInstance(final int port) {
        return newInstance(port, DatagramServer.INET_ADDRESS);
    }

    public static EchoDatagramServer newInstance(
            final int port, final InetAddress inetAddress) {
        return new EchoDatagramServer(new DatagramServer(
                new DatagramServer.DefaultServerDatagramSocketFactory(),
                port,
                inetAddress,
                new VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory(),
                new VirtualThreadPerTaskExecutorOrCachedThreadPoolFactory(),
                new EchoWorkerFactory()));
    }

    public InetAddress getInetAddress() {
        return this.datagramServer.getInetAddress();
    }

    public int getPort() {
        return this.datagramServer.getPort();
    }

    public State getState() {
        State state;
        switch (this.datagramServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
            default:
                throw new AssertionError(String.format(
                        "unhandled state: %s",
                        this.datagramServer.getState()));
        }
        return state;
    }

    public void start() throws IOException {
        this.datagramServer.start();
    }

    public void stop() throws IOException {
        this.datagramServer.stop();
    }

    public enum State {
        STARTED,
        STOPPED
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
