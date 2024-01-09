# About Jargyle 

Jargyle is a Java SOCKS5 API and server.

**Note:** Although the user guide is complete, the reference and Javadocs are 
incomplete at this time, but they will be complete in the future.

-   [Client API Example](#client-api-example)
-   [Server API Example](#server-api-example)
-   [Command Line Example](#command-line-example)
-   [Features](#features)

## Client API Example

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        /*
         * Configure the SOCKS client through system properties.
         */
        /*
         * Set the URI of the SOCKS server for the SOCKS client to 
         * connect.
         */
        System.setProperty("socksServerUri.scheme", "socks5");
        System.setProperty("socksServerUri.host", "jargyle.net");
        System.setProperty("socksServerUri.port", "8080");
        /*
         * Enable SSL/TLS for TCP traffic between the SOCKS client 
         * and the SOCKS server.
         */
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        /*
         * Enable DTLS for UDP traffic between the SOCKS client and 
         * the SOCKS server.
         */
        System.setProperty("socksClient.dtls.enabled", "true");
        System.setProperty("socksClient.dtls.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.dtls.trustStorePassword", "password");
        /*
         * Use only the SOCKS5 username password authentication 
         * method as the SOCKS5 authentication method of choice.
         */
        System.setProperty("socksClient.socks5.methods", "USERNAME_PASSWORD");
        System.setProperty("socksClient.socks5.userpassmethod.username", "Aladdin");
        System.setProperty("socksClient.socks5.userpassmethod.password", "opensesame");
        /*
         * Have the HostResolver to send the RESOLVE command to the 
         * SOCKS server to resolve host names instead of having the 
         * HostResolver resolve host names from the local system.
         */
        System.setProperty("socksClient.socks5.useResolveCommand", "true");
        
        /*
         * Create networking objects whose traffic would be routed 
         * through the SOCKS server based on the system properties 
         * above. If no system properties for configuring the SOCKS 
         * client were provided, the created networking objects 
         * would be ordinary networking objects.
         */
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();
        Socket socket = netObjectFactory.newSocket();
        ServerSocket serverSocket = netObjectFactory.newServerSocket();
        DatagramSocket datagramSocket = netObjectFactory.newDatagramSocket();
        HostResolver hostResolver = netObjectFactory.newHostResolver();
        
        /*
         * Use the created networking objects as if they were 
         * ordinary networking objects.
         */
        // ...
    }
}
```

## Server API Example

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.newInstance(
            Setting.newInstanceWithParsedValue(
                "port", "8080"),
            /*
             * Enable SSL/TLS for TCP traffic between the SOCKS 
             * server and the clients.
             */
            Setting.newInstanceWithParsedValue(
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "drowssap"),
            /*
             * Enable DTLS for UDP traffic between the SOCKS server 
             * and the clients.
             */
            Setting.newInstanceWithParsedValue(
                "dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStorePassword", "drowssap"),
            /*
             * Use only the SOCKS5 username password authentication 
             * method as the SOCKS5 authentication method of choice.
             */
            Setting.newInstanceWithParsedValue(
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "socks5.userpassmethod.userRepository",
                "StringSourceUserRepository:Aladdin:opensesame")
        ))).start();
    }
}
```

## Command Line Example

```bash
jargyle start-server \
    --setting=port=8080 \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=drowssap \
    --setting=dtls.enabled=true \
    --setting=dtls.keyStoreFile=server.jks \
    --setting=dtls.keyStorePassword=drowssap \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassmethod.userRepository=StringSourceUserRepository:Aladdin:opensesame
```

## Features

The client API has the following features:

-   Route traffic through a specified chain of SOCKS servers
-   Use SSL/TLS for TCP traffic to and from SOCKS servers
-   Use DTLS for UDP traffic to and from SOCKS servers
-   Resolve host names from SOCKS5 servers

The server API and the server have the following features:

-   Route traffic through multiple specified chains of SOCKS servers
-   Use SSL/TLS for TCP traffic to and from clients
-   Use DTLS for UDP traffic to and from clients
-   Use SSL/TLS for TCP traffic to and from SOCKS servers
-   Use DTLS for UDP traffic to and from SOCKS servers
-   Resolve host names for clients from the local system or from SOCKS5 servers

The server API and the server also have a rule system that allows you to manage 
traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through a selection of multiple specified chains of SOCKS servers
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth