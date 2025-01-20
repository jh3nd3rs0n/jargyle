# About Jargyle 

Jargyle is a Java SOCKS5 API and server with TCP traffic that can be layered 
with SSL/TLS, UDP traffic that can be layered with DTLS, and both types of 
traffic that can be routed through multiple SOCKS5 servers. It is inspired by 
[JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

**Warning:** Jargyle is not production ready. The user guide is complete, but
the reference documentation and the Javadocs are incomplete. Breaking changes
may occur, but any existing documentation will be updated to reflect the
changes.

## Page Contents

-   [Features](#features)
-   [Uses](#uses)
-   [Examples](#examples)
    -   [SOCKS Client API Example](#socks-client-api-example)
    -   [SOCKS Server API Example](#socks-server-api-example)
    -   [Command Line Example](#command-line-example)

## Features

Jargyle consists of a SOCKS client API, a SOCKS server API, and a SOCKS server.

The SOCKS client API has the following features:

-   SSL/TLS-layered TCP traffic between itself and SOCKS5 servers
-   DTLS-layered UDP traffic between itself and SOCKS5 servers
-   Resolve host names from SOCKS5 servers that handle the
    [SOCKS5 RESOLVE request](reference/socks5-resolve-request.md)
-   Route traffic through a specified chain of SOCKS5 servers

The SOCKS server API and the SOCKS server have the following features:

-   SSL/TLS-layered TCP traffic between itself and clients
-   DTLS-layered UDP traffic between itself and clients
-   Route traffic through (or, in other words, chain to) specified chains of 
    SOCKS5 servers
-   SSL/TLS-layered TCP traffic between itself and SOCKS5 servers
-   DTLS-layered UDP traffic between itself and SOCKS5 servers
-   Resolve host names for clients from the local system or from SOCKS5
    servers that handle the
    [SOCKS5 RESOLVE request](reference/socks5-resolve-request.md)

The SOCKS server API and the SOCKS server also have a rule system that allows 
you to manage traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through a selection of specified chains of SOCKS5 servers
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth

## Uses

-   Both the SOCKS client API and the SOCKS server API can be used for testing.
-   The SOCKS client API can be used for enabling SOCKS and additional
    features for clients.
-   A chain of at least two SOCKS server instances can be used to provide 
    secure traffic: a local SOCKS server instance and one or more remote SOCKS 
    server instances. Local clients can access the local SOCKS server instance 
    while the routed TCP and UDP traffic between the local SOCKS server 
    instance and the remote SOCKS server instances are layered with SSL/TLS 
    and DTLS respectively.

## Examples

The following are brief examples: one of the SOCKS client API, one of the 
SOCKS server API, and one of the command line interface for the SOCKS server. 

### SOCKS Client API Example

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
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
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:8080");
        /*
         * Enable SSL/TLS-layered TCP traffic between the SOCKS client 
         * and the SOCKS server.
         */
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        /*
         * Enable DTLS-layered UDP traffic between the SOCKS client and 
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
         * Have the HostResolver resolve host names from the SOCKS5 server 
         * instead of having the HostResolver resolve host names from the 
         * local system.
         */
        System.setProperty(
            "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server", 
            "true");
        
        /*
         * Create networking objects whose traffic would be routed 
         * through the SOCKS server based on the system properties 
         * above. If no system properties for configuring the SOCKS 
         * client were provided, the created networking objects 
         * would be ordinary networking objects.
         */
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();

        /*
         * Example of creating a HostResolver and a Socket
         */
        /*        
        HostResolver hostResolver = netObjectFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("google.com");        
        Socket socket = netObjectFactory.newSocket(inetAddress, 443);
        */

        /*
         * Example of creating a ServerSocket
         */
        /*
        ServerSocket serverSocket = netObjectFactory.newServerSocket(443);
        */

        /*
         * Example of creating a DatagramSocket
         */        
        /*
        DatagramSocket datagramSocket = netObjectFactory.newDatagramSocket(4444);
        */
        
        // ...
    }
}
```

### SOCKS Server API Example

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "port", "8080"),
            /*
             * Enable SSL/TLS-layered TCP traffic between the SOCKS 
             * server and the clients.
             */
            Setting.newInstanceWithParsedValue(
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "drowssap"),
            /*
             * Enable DTLS-layered UDP traffic between the SOCKS server 
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

### Command Line Example

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
