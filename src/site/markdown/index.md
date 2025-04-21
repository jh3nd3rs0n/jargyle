# About Jargyle 

## Contents

-   [Overview](#overview)
-   [Features](#features)
-   [Uses](#uses)
-   [Examples](#examples)
    -   [Command Line Examples](#command-line-examples) 
    -   [SOCKS Server API Examples](#socks-server-api-examples)
    -   [SOCKS Client API Examples](#socks-client-api-examples)
-   [Get Started](#get-started)

## Overview

Jargyle is a Java SOCKS5 server and API. It is inspired by
[JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

Jargyle is licensed under the
[MIT license](https://opensource.org/licenses/MIT).
Its license can be found 
[here](https://jh3nd3rs0n.github.io/jargyle/licenses.html).

## Features

Jargyle consists of a SOCKS server, a SOCKS server API, and a SOCKS client API.

The SOCKS server, the SOCKS server API, and the SOCKS client API support the 
following features:

-   SOCKS5 authentication methods:
    -   No authentication
    -   Username password authentication
    -   GSS-API authentication
-   SOCKS5 requests:
    -   CONNECT
    -   BIND
    -   UDP ASSOCIATE

The SOCKS server and the SOCKS server API have the following additional 
features:

-   SSL/TLS-layered TCP traffic between itself and clients
-   DTLS-layered UDP traffic between itself and clients
-   Route traffic through (or, in other words, chain to) specified chains of 
    SOCKS5 servers
-   SSL/TLS-layered TCP traffic between itself and SOCKS5 servers
-   DTLS-layered UDP traffic between itself and SOCKS5 servers
-   Resolve host names for clients from the local system or from SOCKS5
    servers that handle the
    [SOCKS5 RESOLVE request](reference/socks5-resolve-request.md)

The SOCKS server and the SOCKS server API also have a rule system that allows 
you to manage traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through a selection of specified chains of SOCKS5 servers
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth

The SOCKS client API has the following additional features:

-   SSL/TLS-layered TCP traffic between itself and SOCKS5 servers
-   DTLS-layered UDP traffic between itself and SOCKS5 servers
-   Resolve host names from SOCKS5 servers that handle the
    [SOCKS5 RESOLVE request](reference/socks5-resolve-request.md)
-   Route traffic through a specified chain of SOCKS5 servers

## Uses

-   A local SOCKS server instance can be used as a proxy between local SOCKS 
    clients that do not use SSL/TLS/DTLS connections and/or authentication and 
    remote SOCKS server instances that require SSL/TLS/DTLS connections and/or 
    authentication. (See [examples](#examples))
-   Both the SOCKS client API and the SOCKS server API can be used for testing.
-   The SOCKS client API can be used for enabling SOCKS and additional
    features for clients.

## Examples

The following are examples of the following types: examples of the command 
line interface for the SOCKS server, examples of the SOCKS server API, and 
examples of the SOCKS client API. 

### Command Line Examples

```bash
# This example starts a SOCKS server instance that requires SSL/TLS/DTLS 
# connections and username password authentication.
#
# Each of the following comments explains each part of the following command:
# 
# - Start the SOCKS server (at address 0.0.0.0) on port 8080.
# - Enable SSL/TLS-layered TCP traffic between the SOCKS server and the 
#   clients.
# - Enable DTLS-layered UDP traffic between the SOCKS server and the clients.
# - Use only the SOCKS5 username password authentication method as the SOCKS5 
#   authentication method of choice.
#
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

```bash
# This example starts a local SOCKS server instance that acts a proxy between 
# local SOCKS clients that do not use SSL/TLS/DTLS connections and 
# authentication and the SOCKS server instance mentioned earlier that requires 
# SSL/TLS/DTLS connections and username password authentication.
# 
# Each of the following comments explains each part of the following command:
#
# - Start the SOCKS server at address 127.0.0.1 (on port 1080).
# - Route traffic through a SOCKS5 server at jargyle.net on port 8080.
# - Enable SSL/TLS-layered TCP traffic between the SOCKS server and the SOCKS5 
#   server at jargyle.net on port 8080.
# - Enable DTLS-layered UDP traffic between the SOCKS server and the SOCKS5 
#   server at jargyle.net on port 8080.
# - Use only the SOCKS5 username password authentication method as the SOCKS5 
#   authentication method of choice for the SOCKS5 server at jargyle.net on 
#   port 8080.
# - Resolve host names from the SOCKS5 server at jargyle.net on port 8080.
# - Mark the previous SOCKS server as the last in a chain of SOCKS servers and 
#   name the route of the chain as 'jargyle.net'.
# - Name the last and unassigned route (in this case the direct route) as 
#   'direct'.
# - Allow the CONNECT request to 127.0.0.1 and select route 'direct' for the 
#   traffic to be routed through.
# - Allow anything else and select route 'jargyle.net' for the traffic to be 
#   routed through.
#  
jargyle start-server \
    --setting=internalFacingBindHost=127.0.0.1 \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=jargyle.jks \
    --setting=chaining.ssl.trustStorePassword=password \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=jargyle.jks \
    --setting=chaining.dtls.trustStorePassword=password \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame \
    --setting=chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true \
    --setting=chaining.routeId=jargyle.net \
    --setting=lastRouteId=direct \
    --setting=rule=socks5.request.command=CONNECT,socks5.request.desiredDestinationAddress=127.0.0.1,firewallAction=ALLOW,selectableRouteId=direct \
    --setting=rule=firewallAction=ALLOW,selectableRouteId=jargyle.net
```

### SOCKS Server API Examples

```java
/*
 * This example starts a SOCKS server instance that requires SSL/TLS/DTLS 
 * connections and username password authentication.
 */

package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        /*
         * Create a SocksServer and start it (at address 0.0.0.0) on port 8080.
         */
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

```java
/*
 * This example starts a local SOCKS server instance that acts a proxy between 
 * local SOCKS clients that do not use SSL/TLS/DTLS connections and 
 * authentication and the SOCKS server instance mentioned earlier that 
 * requires SSL/TLS/DTLS connections and username password authentication.
 */

package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        /*
         * Create a SocksServer and start it at address 127.0.0.1 (on port 
         * 1080).
         */
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "internalFacingBindHost", "127.0.0.1"),
            /*
             * Route traffic through a SOCKS5 server at jargyle.net on port 
             * 8080.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", "socks5://jargyle.net:8080"),            
            /*
             * Enable SSL/TLS-layered TCP traffic between the SOCKS 
             * server and the SOCKS5 server at jargyle.net on port 8080.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "jargyle.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password"),
            /*
             * Enable DTLS-layered UDP traffic between the SOCKS server 
             * and the SOCKS5 server at jargyle.net on port 8080.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStoreFile", "jargyle.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStorePassword", "password"),
            /*
             * Use only the SOCKS5 username password authentication 
             * method as the SOCKS5 authentication method of choice for the 
             * SOCKS5 server at jargyle.net on port 8080.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.username", "Aladdin"),            
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.password", "opensesame"),
            /*
             * Resolve host names from the SOCKS5 server at jargyle.net on 
             * port 8080.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.socks5HostResolver.resolveFromSocks5Server", 
                "true"),
            /*
             * Mark the previous SOCKS server as the last in a chain of SOCKS 
             * servers and name the route of the chain as 'jargyle.net'.
             */
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "jargyle.net"),
            /*
             * Name the last and unassigned route (in this case the direct 
             * route) as 'direct'.
             */            
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "direct"),
            /*
             * Allow the CONNECT request to 127.0.0.1 and select route 
             * 'direct' for the traffic to be routed through.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT," 
                + "socks5.request.desiredDestinationAddress=127.0.0.1," 
                + "firewallAction=ALLOW," 
                + "selectableRouteId=direct"),
            /*
             * Allow anything else and select route 'jargyle.net' for the 
             * traffic to be routed through.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "firewallAction=ALLOW," 
                + "selectableRouteId=jargyle.net")
        ))).start();
    }
}
```

### SOCKS Client API Examples

```java
/*
 * This example shows the minimum required to connect to a local SOCKS server 
 * instance.
 */

package com.example;

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
            "socksClient.socksServerUri", "socks5://127.0.0.1");
        
        /*
         * Create networking objects whose traffic would be routed 
         * through the SOCKS server based on the system properties 
         * above. If no system properties for configuring the SOCKS 
         * client were provided, the created networking objects 
         * would be ordinary networking objects.
         */
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();

        /*
         * Example of creating a Socket
         */
        /*        
        Socket socket = netObjectFactory.newSocket("google.com", 443);
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

```java
/*
 * This example shows connecting a SOCKS server instance that requires 
 * SSL/TLS/DTLS connections and username password authentication. 
 */

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
         * Use only the SOCKS5 username password authentication method as the 
         * SOCKS5 authentication method of choice for the SOCKS server.
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

## Get Started

To download a release of Jargyle, please see [Releases](releases.md).

To know how to use Jargyle, please see the [user guide](userguide/index.md). 
For further information, please see the 
[reference documentation](reference/index.md).

To view the Javadocs for Jargyle, they can be found
[here](https://jh3nd3rs0n.github.io/jargyle/apidocs/index.html).

**Warning:** Jargyle is not production ready. The user guide is complete, but
the reference documentation and the Javadocs are incomplete. Breaking changes
may occur, but any existing documentation will be updated to reflect the
changes.

Still have questions? Maybe the [FAQ](faq.md) might answer some of them.
