/*
 * Copyright (c) 2024 Jonathan K. Henderson
 *
 * This file is licensed under the MIT Licence. See LICENSE for details.
 *
 * This file is a derivative work. Below is the original license.
 */
/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.github.jh3nd3rs0n.test.netty.example.socksproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public final class SocksServer {

    static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

    static final int PORT = Integer.parseInt(System.getProperty("port", "1080"));

    private ExecutorService executor;
    private State state;

    public SocksServer() {
        this.executor = null;
        this.state = State.STOPPED;
    }

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             // Disable logging to get actual performance under performance testing
             // .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new SocksServerInitializer());
            b.bind(INET_ADDRESS, PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public String getHostAddress() {
        return INET_ADDRESS.getHostAddress();
    }

    public int getPort() {
        return PORT;
    }

    public State getState() {
        return this.state;
    }

    public void start() throws IOException {
        AtomicReference<Exception> exceptionAtomicReference =
                new AtomicReference<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(() -> {
            try {
                SocksServer.main(new String[]{});
            } catch (InterruptedException e) {
                // caused by this.executor.shutdownNow();
            } catch (Exception e) {
                exceptionAtomicReference.set(e);
            }
        });
        boolean socksServerActive = false;
        Exception e = null;
        do {
            try (Socket socket = new Socket(INET_ADDRESS, PORT)) {
                socksServerActive = socket.isConnected();
            } catch (IOException ignored) {
            }
        } while (!socksServerActive
                && (e = exceptionAtomicReference.get()) == null);
        if (e != null) {
            throw new IOException(e);
        }
        this.state = State.STARTED;
    }

    public void stop() {
        this.executor.shutdownNow();
        this.executor = null;
        this.state = State.STOPPED;
    }

    public enum State {

        STARTED,

        STOPPED

    }

}
