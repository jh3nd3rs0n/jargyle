package com.github.jh3nd3rs0n.echo.performance.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoServer;

public class DatagramEchoServerHelper {
    public static DatagramEchoServer newDatagramEchoServer() {
        return new DatagramEchoServer(0, DatagramEchoServer.INET_ADDRESS);
    }
}
