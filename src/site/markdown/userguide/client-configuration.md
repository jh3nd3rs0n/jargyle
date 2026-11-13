# Client Configuration

## Contents

-   [Overview](#overview)
-   [(TCP) Socket Connection Types](#tcp-socket-connection-types)
    -   [Extemporaneous (TCP) Socket Connection Type](#extemporaneous-tcp-socket-connection-type)
    -   [Prepared (TCP) Socket Connection Type](#prepared-tcp-socket-connection-type)
-   [Configuring the SOCKS Server TCP Socket](#configuring-the-socks-server-tcp-socket)
    -   [Binding the SOCKS Server TCP Socket to a Host Name or Address](#binding-the-socks-server-tcp-socket-to-a-host-name-or-address)
    -   [Binding the SOCKS Server TCP Socket to a Host Address from a Network Interface](#binding-the-socks-server-tcp-socket-to-a-host-address-from-a-network-interface)
    -   [Binding the SOCKS Server TCP Socket to a Port Number Range or Port Number Ranges](#binding-the-socks-server-tcp-socket-to-a-port-number-range-or-port-number-ranges)
    -   [Applying Socket Settings to the SOCKS Server TCP Socket](#applying-socket-settings-to-the-socks-server-tcp-socket)
    -   [Preparing the SOCKS Server TCP Socket Before Connecting](#preparing-the-socks-server-tcp-socket-before-connecting)
-   [Enabling SSL/TLS-layered TCP Traffic Between the Client and the SOCKS Server](#enabling-ssltls-layered-tcp-traffic-between-the-client-and-the-socks-server)
-   [Enabling DTLS-layered UDP Traffic Between the Client and the SOCKS Server](#enabling-dtls-layered-udp-traffic-between-the-client-and-the-socks-server)
-   [Accessing the SOCKS Server Using SOCKS5 Authentication](#accessing-the-socks-server-using-socks5-authentication)
    -   [Accessing the SOCKS Server Using No Authentication](#accessing-the-socks-server-using-no-authentication)
    -   [Accessing the SOCKS Server Using Username Password Authentication](#accessing-the-socks-server-using-username-password-authentication)
    -   [Accessing the SOCKS Server Using GSS-API Authentication](#accessing-the-socks-server-using-gss-api-authentication)
-   [Resolving Host Names From the SOCKS Server](#resolving-host-names-from-the-socks-server)
-   [Routing Traffic Through a Specified Chain of SOCKS Servers](#routing-traffic-through-a-specified-chain-of-socks-servers)

## Overview

This document discusses how to further use the [client API](client-api.md) 
by means of configuration.

## (TCP) Socket Connection Types

There are two types of connections a TCP socket (`Socket`) can make: an
extemporaneous connection and a prepared connection. Each connection type
has its advantages and disadvantages.

### Extemporaneous (TCP) Socket Connection Type

An extemporaneous connection occurs when creating a `Socket` from one of the
following methods:

-   `SocketFactory.newSocket(java.lang.String host, int port)`
-   `SocketFactory.newSocket(java.lang.String host, int port, java.net.InetAddress localAddr, int localPort)`
-   `SocketFactory.newSocket(java.net.InetAddress address, int port)`
-   `SocketFactory.newSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddr, int localPort)`

When an extemporaneous connection is about to be made, no preparation is made
for the `Socket` before connecting.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocketFactory socketFactory = SocketFactory.getInstance();
        Socket socket = socketFactory.newSocket("example.com", 443);
        // ...
    }
}
```

Here are the following advantages/disadvantages:

**Advantages:**

-   When having the `Socket`'s traffic set through a SOCKS server, if a target
    host name is provided for the `Socket` to connect to
    (`java.lang.String host`), the target host name will be resolved by the
    SOCKS server

**Disadvantages:**

-  Some `Socket` configuration might not be applied after the connection is
   made. Only a prepared connection can have such `Socket` configuration
   applied earlier.
-  No timeout in milliseconds can be specified for how long a wait for the
   `Socket` to connect.

### Prepared (TCP) Socket Connection Type

For a prepared connection, the following occurs:

1.   An unbound and unconnected `Socket` is created from the method
     `SocketFactory.newSocket()`.
2.   The `Socket` is bound by using the method
     `Socket.bind(java.net.SocketAddress bindpoint)`.
3.   Additional configuration is applied to the `Socket` (optional).
4.   The `Socket` is connected by using either of the following methods:
    -   `Socket.connect(java.net.SocketAddress endpoint)`
    -   `Socket.connect(java.net.SocketAddress endpoint, int timeout)`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocketFactory socketFactory = SocketFactory.getInstance();
        Socket socket = socketFactory.newSocket();
        socket.bind(new InetSocketAddress(null, 0));
        socket.connect(new InetSocketAddress("example.com", 443));
        // ...
    }
}
```

Here are the following advantages/disadvantages:

**Advantages:**

-   `Socket` configuration that can only be applied before a connection is
    made can be applied.
-   A timeout in milliseconds can be specified for how long a wait for the
    `Socket` to connect (`int timeout`).

**Disadvantages:**

-   When having the `Socket`'s traffic set through a SOCKS server, if a target
    host name is provided for the TCP socket to connect to, the target host
    name is resolved through the local system not the SOCKS server.

## Configuring the SOCKS Server TCP Socket

Objects that have their traffic routed through the SOCKS server each has a 
client. Each client has an underlying TCP socket to the SOCKS server. In this 
documentation, this underlying TCP socket is called the SOCKS server TCP 
socket.

Depending on the object, the SOCKS server TCP socket can be configured in only 
one of the following ways:

-   **Configuring the object**. Configuring certain objects configure their 
    SOCKS server TCP sockets without any special properties required. `Socket` 
    would be such an object.
-   **Configuring the object and using properties prefixed with
    `socksClient.socksServerSocket`**. Configuring certain objects configure
    some parts of their SOCKS server TCP sockets but the special properties 
    are required to configure other parts of their SOCKS server TCP sockets. 
    `ServerSocket` would be such an object.
-   **Using properties prefixed with `socksClient.socksServerSocket`**. 
    Configuring certain objects does nothing to their SOCKS server TCP sockets.
    `DatagramSocket` would be such an object. Special properties are required 
    to configure their SOCKS server TCP sockets. 

### Binding the SOCKS Server TCP Socket to a Host Name or Address

You can bind the SOCKS server TCP socket to a host name or to a host address.

For a `Socket`, you can provide the host name or address as a
`java.lang.String` or a `java.net.InetAddress` to the following methods:

-   `SocketFactory.newSocket(java.lang.String host, int port, java.net.InetAddress localAddr, int localPort)`:
    The third method argument `localAddr` is used to bind the SOCKS server TCP 
    socket to a host name or address.
-   `SocketFactory.newSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddr, int localPort)`:
    The third method argument `localAddr` is used to bind the SOCKS server TCP 
    socket to a host address.
-   `Socket.bind(java.net.SocketAddress bindpoint)`: The host name or address 
    of the provided `java.net.SocketAddress` is used to bind the SOCKS server 
    TCP socket to a host name or address.

For all other objects, you would use the property 
`socksClient.socksServerSocket.bindHost`.

The host address can be an IPv4 address or an IPv6 address.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Examples of binding the SOCKS server TCP socket to a host name or 
         * address:
         * 
         * System.setProperty("socksClient.socksServerSocket.bindHost", "a96p08.local");
         * 
         * System.setProperty("socksClient.socksServerSocket.bindHost", "192.168.1.231");
         * 
         * System.setProperty("socksClient.socksServerSocket.bindHost", "fe80::34ef:938a:402f:e526%17");
         * 
         */
        SocketFactory socketFactory = SocketFactory.getInstance();
        /*
         * Examples of binding the SOCKS server TCP socket to a host name or
         * address through a Socket:
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "a96p08.local", 0);
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "192.168.1.231", 0);
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "fe80::34ef:938a:402f:e526%17", 0);
         * 
         * InetAddress address = InetAddress.getByName("example.com");
         * InetAddress localAddr = InetAddress.getByName("a96p08.local");
         * Socket socket = socketFactory.newSocket(address, 443, localAddr, 0);
         * 
         * Socket socket = socketFactory.newSocket();
         * socket.bind(new InetSocketAddress("a96p08.local", 0));
         * 
         */
        // ...
    }
}
```

<a id="binding-the-socks-server-tcp-socket-to-a-host-address-from-a-network-interface"></a>
### Binding the SOCKS Server TCP Socket to a Host Address from a Network Interface

Instead of binding the SOCKS server TCP socket to a specified host address, 
you can bind it to a host address from a specified network interface.

For a `Socket`, you can provide the name of the network interface to 
`NetInterface.newInstanceFrom(java.lang.String s)` and receive a 
`NetInterface` object that can provide you a host address.

For all other objects, you would use the property 
`socksClient.socksServerSocket.netInterface`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Example of binding the SOCKS server TCP socket to a host address 
         * from a network interface:
         * 
         * System.setProperty("socksClient.socksServerSocket.netInterface", "eth0");
         * 
         */
        SocketFactory socketFactory = SocketFactory.getInstance();
        /*
         * Examples of binding the SOCKS server TCP socket to a host address 
         * from a network interface through a Socket:
         * 
         * InetAddress address = InetAddress.getByName("example.com");
         * InetAddress localAddr = NetInterface.newInstanceFrom("eth0")
         *     .getHostAddresses().get(0).toInetAddress();
         * Socket socket = socketFactory.newSocket(address, 443, localAddr, 0);
         * 
         * Socket socket = socketFactory.newSocket();
         * socket.bind(new InetSocketAddress(
         *     NetInterface.newInstanceFrom("eth0")
         *         .getHostAddresses().get(0).toInetAddress(), 
         *     0));
         * 
         */
        // ...
    }
}
```

You can also specify which 
[host address types](common-topics.md#host-address-types) to choose from the 
network interface.

For a `Socket`, you can provide the host address types to 
`NetInterface.getHostAddresses(HostAddressTypes hostAddressTypes)` to the 
`NetInterface` object.

For all other objects, you would use the property
`socksClient.socksServerSocket.bindHostAddressTypes`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressType;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Example of binding the SOCKS server TCP socket to a host address 
         * from a network interface:
         * 
         * System.setProperty("socksClient.socksServerSocket.netInterface", "eth0");
         * System.setProperty("socksClient.socksServerSocket.bindHostAddressTypes", "HOST_IPV4_ADDRESS");
         * 
         */
        SocketFactory socketFactory = SocketFactory.getInstance();
        /*
         * Examples of binding the SOCKS server TCP socket to a host address 
         * from a network interface through a Socket:
         * 
         * InetAddress address = InetAddress.getByName("example.com");
         * InetAddress localAddr = NetInterface.newInstanceFrom("eth0")
         *     .getHostAddresses(HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS))
         *     .get(0)
         *     .toInetAddress();
         * Socket socket = socketFactory.newSocket(address, 443, localAddr, 0);
         * 
         * Socket socket = socketFactory.newSocket();
         * socket.bind(new InetSocketAddress(
         *     NetInterface.newInstanceFrom("eth0")
         *         .getHostAddresses(HostAddressTypes.of(HostAddressType.HOST_IPV4_ADDRESS))
         *         .get(0)
         *         .toInetAddress(), 
         *     0));
         * 
         */
        // ...
    }
}
```

<a id="binding-the-socks-server-tcp-socket-to-a-port-number-range-or-port-number-ranges"></a>
### Binding the SOCKS Server TCP Socket to a Port Number Range or Port Number Ranges

You can bind the SOCKS server TCP socket to a 
[port number range or port number ranges](common-topics.md#port-number-ranges).

For a `Socket`, you can only provide a port number to the following methods:

-   `SocketFactory.newSocket(java.lang.String host, int port, java.net.InetAddress localAddr, int localPort)`:
    The fourth method argument `localPort` is used to bind the SOCKS server 
    TCP socket to a port number.
-   `SocketFactory.newSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddr, int localPort)`:
    The fourth method argument `localPort` is used to bind the SOCKS server 
    TCP socket to a port number.
-   `Socket.bind(java.net.SocketAddress bindpoint)`: The port number of the 
    provided `java.net.SocketAddress` is used to bind the SOCKS server TCP 
    socket to a port number.

For all other objects, you would use the property 
`socksClient.socksServerSocket.bindPortRanges`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Examples of binding the SOCKS server TCP socket to a port number 
         * range or port number ranges:
         * 
         * System.setProperty("socksClient.socksServerSocket.bindPortRanges", "20400-23500");
         * 
         * System.setProperty("socksClient.socksServerSocket.bindPortRanges", "5000,6500-7500");
         * 
         * System.setProperty("socksClient.socksServerSocket.bindPortRanges", "3500-4500,6000,9000-10000");
         * 
         */
        SocketFactory socketFactory = SocketFactory.getInstance();
        /*
         * Examples of binding the SOCKS server TCP socket to a port number 
         * through a Socket:
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "a96p08.local", 0);
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "192.168.1.231", 0);
         * 
         * Socket socket = socketFactory.newSocket("example.com", 443, "fe80::34ef:938a:402f:e526%17", 0);
         * 
         * InetAddress address = InetAddress.getByName("example.com");
         * InetAddress localAddr = InetAddress.getByName("a96p08.local");
         * Socket socket = socketFactory.newSocket(address, 443, localAddr, 0);
         * 
         * Socket socket = socketFactory.newSocket();
         * socket.bind(new InetSocketAddress("a96p08.local", 0));
         * 
         */
        // ...
    }
}
```

### Applying Socket Settings to the SOCKS Server TCP Socket

You can apply [socket settings](common-topics.md#socket-settings) to the SOCKS 
server TCP socket.

For a `Socket`, you would use the following methods:

-   `Socket.setKeepAlive(boolean on)`
-   `Socket.setOption(java.net.SocketOption<T> name, T value)`
-   `Socket.setReceiveBufferSize(int size)`
-   `Socket.setReuseAddress(boolean on)`
-   `Socket.setSendBufferSize(int size)`
-   `Socket.setSoLinger(boolean on, int linger)`
-   `Socket.setSoTimeout(int timeout)`
-   `Socket.setTcpNoDelay(boolean on)`
-   `Socket.setTrafficClass(int tc)`

For a `ServerSocket`, you would use the following methods:

-   `ServerSocket.setOption(java.net.SocketOption<T> name, T value)`
-   `ServerSocket.setReceiveBufferSize(int size)`
-   `ServerSocket.setReuseAddress(boolean on)`
-   `ServerSocket.setSoTimeout(int timeout)`

For all other objects, you would use the property 
`socksClient.socksServerSocket.socketSettings`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Example of applying socket settings to the SOCKS server TCP socket:
         * 
         * System.setProperty("socksClient.socksServerSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256");
         * 
         */
        /*
         * Example of applying socket settings to the SOCKS server TCP socket 
         * through a Socket:
         * 
         * SocketFactory socketFactory = SocketFactory.getInstance();
         * Socket socket = socketFactory.newSocket();
         * socket.setReceiveBufferSize(256);
         * socket.setSendBufferSize(256);
         * 
         */
        /*
         * Example of applying socket settings to the SOCKS server TCP socket
         * through a ServerSocket:
         *
         * ServerSocketFactory serverSocketFactory = ServerSocketFactory.getInstance();
         * ServerSocket serverSocket = serverSocketFactory.newServerSocket();
         * serverSocket.setReceiveBufferSize(256);
         * serverSocket.bind(new InetSocketAddress(null, 5000));
         * Socket socket = serverSocket.accept();
         * socket.setSendBufferSize(256);
         *
         */
        // ...
    }
}
```

### Preparing the SOCKS Server TCP Socket Before Connecting

You can prepare the SOCKS server TCP socket before connecting.

For a `Socket`, making a 
[prepared connection](#prepared-tcp-socket-connection-type) prepares the SOCKS 
server TCP socket before connecting.

For all other objects, you would have the property 
`socksClient.socksServerSocket.prepareSocket` set to `true`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        /*
         * Example of preparing the SOCKS server TCP socket before connecting:
         * 
         * System.setProperty("socksClient.socksServerSocket.prepareSocket", "true");
         * System.setProperty("socksClient.socksServerSocket.connectTimeout", "120000");
         * 
         */
        SocketFactory socketFactory = SocketFactory.getInstance();
        /*
         * Example of preparing the SOCKS server TCP socket before connecting
         * through a Socket:
         * 
         * Socket socket = socketFactory.newSocket();
         * socket.bind(new InetSocketAddress(null, 0));
         * socket.connect(new InetSocketAddress("example.com", 443), 120000);
         * 
         */
        // ...
    }
}
```

<a id="enabling-ssltls-layered-tcp-traffic-between-the-client-and-the-socks-server"></a>
## Enabling SSL/TLS-layered TCP Traffic Between the Client and the SOCKS Server

You can enable SSL/TLS-layered TCP traffic between the client and the SOCKS 
server under the following condition: 

-   The SOCKS server accepts SSL/TLS connections.

By default, SSL/TLS-layered TCP traffic between the client and the SOCKS 
server is disabled. To enable SSL/TLS-layered TCP traffic between the client 
and the SOCKS server, you will need to have the property 
`socksClient.ssl.enabled` set to `true`. In addition, you will need to have 
the property `socksClient.ssl.trustStoreFile` to specify the SOCKS server's 
key store file used as a trust store (this file would need to be created by 
Java's keytool utility). Also, you will need to have the property 
`socksClient.ssl.trustStorePassword` to specify the password for the SOCKS 
server's trust store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        // ...
    }
}
```

If the SOCKS server wants the client to authenticate using SSL/TLS, you will 
need to have the property `socksClient.ssl.keyStoreFile` to specify the 
client's key store file (this file would need to be created by Java's keytool 
utility). Also, you will need to have the property 
`socksClient.ssl.keyStorePassword` to specify the password for the 
client's key store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.keyStoreFile", "client.jks");
        System.setProperty("socksClient.ssl.keyStorePassword", "drowssap");
        System.setProperty("socksClient.ssl.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        // ...
    }
}
```

<a id="enabling-dtls-layered-udp-traffic-between-the-client-and-the-socks-server"></a>
## Enabling DTLS-layered UDP Traffic Between the Client and the SOCKS Server

You can enable DTLS-layered UDP traffic between the client and the SOCKS 
server under the following condition: 

-   The SOCKS server accepts DTLS connections.

By default, DTLS-layered UDP traffic between the client and the SOCKS server 
is disabled. To enable DTLS-layered UDP traffic between the client and the 
SOCKS server, you will need to have the property `socksClient.dtls.enabled` 
set to `true`. In addition, you will need to have the property 
`socksClient.dtls.trustStoreFile` to specify the SOCKS server's key store 
file used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the property 
`socksClient.dtls.trustStorePassword` to specify the password for the SOCKS 
server's trust store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty("socksClient.dtls.enabled", "true");
        System.setProperty("socksClient.dtls.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.dtls.trustStorePassword", "password");
        // ...
    }
}
```

## Accessing the SOCKS Server Using SOCKS5 Authentication

The client has the following SOCKS5 authentication methods to choose from when 
accessing the SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

From the API, you can have one or more of the aforementioned authentication 
methods set in the property `socksClient.socks5.methods` as a comma 
separated list. 

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty(
            "socksClient.socks5.methods", 
            "NO_AUTHENTICATION_REQUIRED,GSSAPI");
        // ...
    }
}
```

If not set, the default value for the property `socksClient.socks5.methods` 
is set to `NO_AUTHENTICATION_REQUIRED`.

### Accessing the SOCKS Server Using No Authentication

Because the default value for the property `socksClient.socks5.methods` is 
set to `NO_AUTHENTICATION_REQUIRED`, it is not required for 
`NO_AUTHENTICATION_REQUIRED` to be included in the property 
`socksClient.socks5.methods`.

However, if other authentication methods are to be used in addition to 
`NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be 
included in the property `socksClient.socks5.methods`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty(
            "socksClient.socks5.methods", 
            "NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD");
        // ...
    }
}
```

<a id="accessing-the-socks-server-using-username-password-authentication"></a>
### Accessing the SOCKS Server Using Username Password Authentication

To access the SOCKS server using username password authentication, you 
will need to have the property `socksClient.socks5.methods` to have 
`USERNAME_PASSWORD` included. You will also need to have the properties 
`socksClient.socks5.userpassauthmethod.username` and 
`socksClient.socks5.userpassauthmethod.password` to specify the username and 
password for the SOCKS5 server.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty(
            "socksClient.socks5.methods", "USERNAME_PASSWORD");
        System.setProperty(
            "socksClient.socks5.userpassauthmethod.username", 
            "Aladdin");
        System.setProperty(
            "socksClient.socks5.userpassauthmethod.password",
            "opensesame");
        // ...
    }
}
```

Instead of using the properties `socksClient.socks5.userpassauthmethod.username` 
and `socksClient.socks5.userpassauthmethod.password` to specify the username and 
password for the SOCKS5 server, you can supply the username and password for 
the SOCKS5 server as a username and password pair in the user information 
component of the SOCKS server URI.

The username and password pair must be in the following format:

```text
USERNAME:PASSWORD
```

`USERNAME` is the username and `PASSWORD` is the password.

If the username or the password contains a colon character (`:`), then each
colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a percent sign character (`%`) not
used for URL encoding, then each percent sign character not used for URL
encoding must be replaced with the URL encoding character `%25`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", 
            "socks5://Jasmine:mission%3Aimpossible@jargyle.net:1234");
        // ...
    }
}
```

There is no need to have the property `socksClient.socks5.methods` to have 
`USERNAME_PASSWORD` included since it will be automatically included during 
runtime.

### Accessing the SOCKS Server Using GSS-API Authentication

To access the SOCKS server using GSS-API authentication, you will need to have 
the property `socksClient.socks5.methods` to have `GSSAPI` included. You 
will also need to specify Java system properties to use a security mechanism 
that implements the GSS-API (for example, Kerberos is a security mechanism that 
implements the GSS-API) and you will also need to specify the GSS-API service 
name for the SOCKS5 server.

The following is a sufficient example of using the Kerberos security mechanism:

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty(
            "java.security.auth.login.config", "login.conf");
        System.setProperty("java.security.krb5.conf", "krb5.conf");
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty("socksClient.socks5.methods", "GSSAPI");
        System.setProperty(
            "socksClient.socks5.gssapiauthmethod.serviceName",
            "rcmd/jargyle.net");
        // ...
    }
}
```

The Java system property `javax.security.auth.useSubjectCredsOnly` with 
the value `false` disables JAAS-based authentication to obtain the credentials 
directly and lets the underlying security mechanism obtain them instead.

The Java system property `java.security.auth.login.config` with the value 
`login.conf` provides a JAAS configuration file for the underlying security 
mechanism.

`login.conf`:

```text
com.sun.security.jgss.initiate {
  com.sun.security.auth.module.Krb5LoginModule required
  principal="alice"
  useKeyTab=true
  keyTab="alice.keytab"
  storeKey=true;
};
```

In `login.conf`, `alice` is a principal that is created by a Kerberos 
administrator. 

Also in `login.conf`, `alice.keytab` is a keytab file also created by a 
Kerberos administrator that contains the aforementioned principal and its 
respective encrypted key.  

The Java system property `java.security.krb5.conf` with the value 
`krb5.conf` provides the Kerberos configuration file that points to the 
Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```text
[libdefaults]
    kdc_realm = JARGYLE.NET
    default_realm = JARGYLE.NET
    udp_preference_limit = 4096
    kdc_tcp_port = 12345
    kdc_udp_port = 12345

[realms]
    JARGYLE.NET = {
        kdc = jargyle.net:12345
    }
```

In `krb5.conf`, a KDC is defined as running at `jargyle.net` on port `12345` 
with its realm as `JARGYLE.NET`.

The property `socksClient.socks5.gssapiauthmethod.serviceName` with the value 
`rcmd/jargyle.net` is the GSS-API service name (or the Kerberos service 
principal) for the SOCKS server residing at `jargyle.net`.

## Resolving Host Names From the SOCKS Server

By default, host name resolution from the SOCKS server occurs only when a 
`Socket` is created from the following methods:

-   `SocketFactory.newSocket(java.lang.String host, int port)`: The first 
    method argument `host` gets resolved from the SOCKS server
-   `SocketFactory.newSocket(java.lang.String host, int port, java.net.InetAddress localAddr, int localPort)`:
    The first method argument `host` gets resolved from the SOCKS server

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        SocketFactory socketFactory = SocketFactory.getInstance();
        Socket socket = socketFactory.newSocket("example.com", 443);
        // ...
    }
}
```

The client API comes with a `HostResolver` object to resolve host names. By 
default, the `HostResolver` object resolves host names through the local 
system. To enable the `HostResolver` object to have the host names resolved 
from the SOCKS server instead, you would need to set the property 
`socksClient.socks.socksHostResolver.resolveFromSocksServer` set to `true`. 
This property can only be used if the SOCKS server supports handling the 
[RESOLVE request](../reference/socks5-resolve-request.md).

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        System.setProperty(
            "socksClient.socks.socksHostResolver.resolveFromSocksServer",
            "true");
        HostResolverFactory hostResolverFactory = 
            HostResolverFactory.getInstance();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        SocketFactory socketFactory = SocketFactory.getInstance();        
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        // ...
    }
}
```

## Routing Traffic Through a Specified Chain of SOCKS Servers

You can have objects route traffic through a specified chain of SOCKS servers. 
To do this, you will need to create a chain of `SocksClient` objects with each 
`SocksClient` object specifying a SOCKS server.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of());
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(), socksClient2);
        SocketFactory socketFactory = 
            socksClient3.getSocksSocketFactory();
        // ...
    }
}
```

Objects that have traffic routed through a specified chain of SOCKS servers 
each has a `SocksClient` object. Each `SocksClient` object has an underlying 
TCP socket to the last SOCKS server in the chain. Within each underlying TCP 
socket to the last SOCKS server in the chain is another `SocksClient` object. 
Within each `SocksClient` object is an underlying TCP socket to the 
second-to-last SOCKS server in the chain and so on. All of these underlying 
TCP sockets are called the SOCKS server TCP sockets.

For configuring any of the SOCKS server TCP sockets, it is recommended to 
configure only the last SOCKS server TCP socket in the chain by setting the 
`Properties` object for the last `SocksClient` object.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of());
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(
                 Property.newInstanceFromParsedValue(
                     "socksClient.socksServerSocket.socketSettings", 
                     "SO_RCVBUF=256,SO_SNDBUF=256")
            ), socksClient2);
        /*
         * Example of configuring the last SOCKS server TCP socket
         * through a Socket:
         *
         * SocketFactory socketFactory = socksClient3.getSocksSocketFactory();
         * Socket socket = socketFactory.newSocket();
         * socket.setReceiveBufferSize(256);
         * socket.setSendBufferSize(256);
         *
         */
        /*
         * Example of configuring the last SOCKS server TCP socket
         * through a ServerSocket:
         *
         * ServerSocketFactory serverSocketFactory = 
         *     socksClient3.getSocksServerSocketFactory();
         * ServerSocket serverSocket = serverSocketFactory.newServerSocket();
         * serverSocket.setReceiveBufferSize(256);
         * serverSocket.bind(new InetSocketAddress(null, 5000));
         * Socket socket = serverSocket.accept();
         * socket.setSendBufferSize(256);
         *
         */
        // ...
    }
}
```

To specify the properties regarding SSL/TLS/DTLS and authentication for a 
SOCKS server in the chain, the `Properties` object for the properties for that 
SOCKS server will need to be set for the `SocksClient` object specifying that 
SOCKS server. 

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of(
                 Property.newInstanceFromParsedValue(
                     "socksClient.socks5.methods", "GSSAPI"),
                 Property.newInstanceFromParsedValue(
                     "socksClient.socks5.gssapiauthmethod.serviceName",
                     "rcmd/alpha-alpha.net")));
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(
                 Property.newInstanceFromParsedValue(
                     "socksClient.ssl.enabled", "true"),
                 Property.newInstanceFromParsedValue(
                     "socksClient.ssl.trustStoreFile", "server.jks"),
                 Property.newInstanceFromParsedValue(
                     "socksClient.ssl.trustStorePassword", "password"),
                 Property.newInstanceFromParsedValue(
                     "socksClient.socks5.methods", "USERNAME_PASSWORD"),
                 Property.newInstanceFromParsedValue(
                     "socksClient.socks5.userpassauthmethod.username",
                     "Aladdin"), 
                 Property.newInstanceFromParsedValue(
                     "socksClient.socks5.userpassauthmethod.password",
                     "opensesame")
            ), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(), socksClient2);
        SocketFactory socketFactory = 
            socksClient3.getSocksSocketFactory();
        // ...
    }
}
```

How the host names of the SOCKS servers in a chain of SOCKS servers are 
resolved depends on the [type of connection](#tcp-socket-connection-types) a 
`Socket` makes. If it makes an 
[extemporaneous connection](#extemporaneous-tcp-socket-connection-type), the 
specified host name for each SOCKS server except the first is resolved by the 
previous SOCKS server (the specified host name of the first is resolved by the 
local system). If it makes a 
[prepared connection](#prepared-tcp-socket-connection-type), the specified 
host name for each SOCKS server is resolved by the local system.

API example of a `Socket` making an extemporaneous connection:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of());
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(), socksClient2);
        SocketFactory socketFactory = 
            socksClient3.getSocksSocketFactory();
        Socket socket = socketFactory.newSocket("example.com", 443);
        /*
         * The host name 'alpha-alpha.net' is resolved by the local system.
         * 
         * The host name 'beta-alpha.net' is resolved by the previous SOCKS 
         * server with the host name 'alpha-alpha.net'.
         * 
         * The host name 'gamma-alpha.net' is resolved by the previous SOCKS 
         * server with the host name 'beta-alpha.net'.
         * 
         * The host name 'example.com' is resolved by the last SOCKS server 
         * with the host name 'gamma-alpha.net'.
         */
        // ...
    }
}
```

API example of a `Socket` making a prepared connection:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of());
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(), socksClient2);
        HostResolverFactory hostResolverFactory =
            socksClient3.getSocksHostResolverFactory();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        SocketFactory socketFactory =
            socksClient3.getSocksSocketFactory();
        Socket socket = socketFactory.newSocket();
        socket.bind(new InetSocketAddress(null, 0));
        socket.connect(new InetSocketAddress(inetAddress, 443));
        /*
         * The host name 'alpha-alpha.net' is resolved by the local system.
         * 
         * The host name 'beta-alpha.net' is resolved by the local system.
         * 
         * The host name 'gamma-alpha.net' is resolved by the local system.
         * 
         * The host name 'example.com' is resolved by the local system.
         */
        // ...
    }
}
```

If you would like to have the `Socket` make a prepared connection without the 
aforementioned limitations, you will need to have the setting 
`socksClient.socks.socksHostResolver.resolveFromSocksServer` set to `true` for 
each server. Again, this setting can only be used if each SOCKS server 
supports handling the 
[RESOLVE request](../reference/socks5-resolve-request.md).

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.HostResolverFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUriScheme;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        SocksClient socksClient1 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("alpha-alpha.net", 11111)
            .newSocksClient(Properties.of(Property.newInstanceFromParsedValue(
                    "socksClient.socks.socksHostResolver.resolveFromSocksServer",
                    "true"
            )));
        SocksClient socksClient2 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("beta-alpha.net", 22221)
            .newSocksClient(Properties.of(Property.newInstanceFromParsedValue(
                    "socksClient.socks.socksHostResolver.resolveFromSocksServer",
                    "true"
            )), socksClient1);
        SocksClient socksClient3 = SocksServerUriScheme.SOCKS5
            .newSocksServerUri("gamma-alpha.net", 33331)
            .newSocksClient(Properties.of(Property.newInstanceFromParsedValue(
                    "socksClient.socks.socksHostResolver.resolveFromSocksServer",
                    "true"
            )), socksClient2);
        HostResolverFactory hostResolverFactory =
            socksClient3.getSocksHostResolverFactory();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        SocketFactory socketFactory =
            socksClient3.getSocksSocketFactory();
        Socket socket = socketFactory.newSocket();
        socket.bind(new InetSocketAddress(null, 0));
        socket.connect(new InetSocketAddress(inetAddress, 443));
        /*
         * The host name 'alpha-alpha.net' is resolved by the local system.
         *
         * The host name 'beta-alpha.net' is resolved by the previous SOCKS
         * server with the host name 'alpha-alpha.net'.
         *
         * The host name 'gamma-alpha.net' is resolved by the previous SOCKS
         * server with the host name 'beta-alpha.net'.
         *
         * The host name 'example.com' is resolved by the last SOCKS server
         * with the host name 'gamma-alpha.net'.
         */
        // ...
    }
}
```

The known limitations of routing traffic through a specified chain of SOCKS
servers include the following:

-   Only TCP traffic can be routed through the chain.

