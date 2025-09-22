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
package com.github.jh3nd3rs0n.jargyle.test.netty.example.socksproxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SocksServer {

    public static final InetAddress INET_ADDRESS = InetAddress.getLoopbackAddress();

    public static final int PORT = 1080;

    private ExecutorService executor;
    private final InetAddress inetAddress;
    private final int port;
    private State state;

    public SocksServer() {
        this(PORT);
    }

    public SocksServer(final int prt) {
        this(prt, INET_ADDRESS);
    }

    public SocksServer(final int prt, final InetAddress inetAddr) {
        this.executor = null;
        this.inetAddress = inetAddr;
        this.port = prt;
        this.state = State.STOPPED;
    }

    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    public int getPort() {
        return this.port;
    }

    public State getState() {
        return this.state;
    }

    public void start() throws IOException {
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new SocksServerInitializer());
                b.bind(this.inetAddress, this.port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // caused by this.executor.shutdownNow();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
        boolean socksServerActive = false;
        do {
            try (Socket socket = new Socket(INET_ADDRESS, PORT)) {
                socksServerActive = socket.isConnected();
            } catch (IOException ignored) {
            }
        } while (!socksServerActive);
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
