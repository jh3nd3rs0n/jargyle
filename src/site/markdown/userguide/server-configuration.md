# Server Configuration

## Contents

-   [Overview](#overview)
-   [Configuring Endpoints](#configuring-endpoints)
    -   [Binding Endpoints to a Host Name or Address](#binding-endpoints-to-a-host-name-or-address)
    -   [Binding Endpoints to a Host Address from a Network Interface](#binding-endpoints-to-a-host-address-from-a-network-interface)
    -   [Binding Endpoints to a Port Number Range or Port Number Ranges](#binding-endpoints-to-a-port-number-range-or-port-number-ranges)
    -   [Applying Socket Settings to Endpoints](#applying-socket-settings-to-endpoints)
    -   [TCP Socket Connection Types](#tcp-socket-connection-types)
        -   [Extemporaneous TCP Socket Connection Type](#extemporaneous-tcp-socket-connection-type)
        -   [Prepared TCP Socket Connection Type](#prepared-tcp-socket-connection-type)
    -   [Preparing the Remote TCP Socket Before Connecting](#preparing-the-remote-tcp-socket-before-connecting)
-   [Enabling SSL/TLS-layered TCP Traffic Between the Server and Its Clients](#enabling-ssltls-layered-tcp-traffic-between-the-server-and-its-clients)
-   [Enabling DTLS-layered UDP Traffic Between the Server and Its Clients](#enabling-dtls-layered-udp-traffic-between-the-server-and-its-clients)
-   [Using SOCKS5 Authentication](#using-socks5-authentication)
    -   [Using No Authentication](#using-no-authentication)
    -   [Using Username Password Authentication](#using-username-password-authentication)
    -   [Using GSS-API Authentication](#using-gss-api-authentication)
-   [Chaining to Another SOCKS Server](#chaining-to-another-socks-server)
    -   [Configuring the Other SOCKS Server TCP Socket](#configuring-the-other-socks-server-tcp-socket) 
        -   [Binding the Other SOCKS Server TCP Socket to a Host Name or Address](#binding-the-other-socks-server-tcp-socket-to-a-host-name-or-address)
        -   [Binding the Other SOCKS Server TCP Socket to a Host Address from a Network Interface](#binding-the-other-socks-server-tcp-socket-to-a-host-address-from-a-network-interface)
        -   [Binding the Other SOCKS Server TCP Socket to a Port Number Range or Port Number Ranges](#binding-the-other-socks-server-tcp-socket-to-a-port-number-range-or-port-number-ranges)
        -   [Applying Socket Settings to the Other SOCKS Server TCP Socket](#applying-socket-settings-to-the-other-socks-server-tcp-socket)
        -   [Preparing the Other SOCKS Server TCP Socket Before Connecting](#preparing-the-other-socks-server-tcp-socket-before-connecting)
        -   [Summary for Configuring the Other SOCKS Server TCP Socket](#summary-for-configuring-the-other-socks-server-tcp-socket)
    -   [Enabling SSL/TLS-layered TCP Traffic Between the Server and the Other SOCKS Server](#enabling-ssltls-layered-tcp-traffic-between-the-server-and-the-other-socks-server)
    -   [Enabling DTLS-layered UDP Traffic Between the Server and the Other SOCKS Server](#enabling-dtls-layered-udp-traffic-between-the-server-and-the-other-socks-server)
    -   [Chaining to the Other SOCKS Server Using SOCKS5 Authentication](#chaining-to-the-other-socks-server-using-socks5-authentication)
        -   [Chaining to the Other SOCKS Server Using No Authentication](#chaining-to-the-other-socks-server-using-no-authentication)
        -   [Chaining to the Other SOCKS Server Using Username Password Authentication](#chaining-to-the-other-socks-server-using-username-password-authentication)
        -   [Chaining to the Other SOCKS Server Using GSS-API Authentication](#chaining-to-the-other-socks-server-using-gss-api-authentication)
    -   [Resolving Host Names From the Other SOCKS Server](#resolving-host-names-from-the-other-socks-server)
-   [Chaining to a Specified Chain of Other SOCKS Servers](#chaining-to-a-specified-chain-of-other-socks-servers)
-   [Chaining to Specified Chains of Other SOCKS Servers](#chaining-to-specified-chains-of-other-socks-servers)
-   [Configuring Relay Settings](#configuring-relay-settings)
-   [Limiting Relay Bandwidth](#limiting-relay-bandwidth)
-   [Using Rules to Manage Traffic](#using-rules-to-manage-traffic)
    -   [Rule Conditions](#rule-conditions)
    -   [Rule Actions](#rule-actions)
    -   [Allowing or Denying Traffic](#allowing-or-denying-traffic)
    -   [Allowing a Limited Number of Simultaneous Connections](#allowing-a-limited-number-of-simultaneous-connections)
    -   [Routing Traffic Through a Selection of Specified Chains of SOCKS Servers](#routing-traffic-through-a-selection-of-specified-chains-of-socks-servers)
    -   [Redirecting the Desired Destination](#redirecting-the-desired-destination)
    -   [Configuring Endpoints Through Rules](#configuring-endpoints-through-rules)
    -   [Configuring Relay Settings Through Rules](#configuring-relay-settings-through-rules)
    -   [Limiting Relay Bandwidth Through Rules](#limiting-relay-bandwidth-through-rules)

## Overview

This document discusses how to further use the 
[server](cli.md#starting-the-server) and the [server API](server-api.md) by 
means of configuration.

## Configuring Endpoints

The server has endpoints that interact with clients, servers, and peers.

The following are the types of endpoints:

-   `DatagramSocket`: A UDP socket that sends or receives UDP datagram packets 
    to or from other UDP peers 
-   `ServerSocket`: A TCP server socket that waits for and accepts a TCP socket
    that is connected to a client
-   `Socket`: A TCP socket that is connected to a client or will be connected
    to a server.

The endpoints are categorized as follows:

-   **Client endpoints**: Endpoints that face or serve the client
    -   `clientFacingServerSocket`: The client-facing TCP server socket that 
        the server uses to wait for and accept a TCP socket that is connected 
        to the client
    -   `clientSocket`: The accepted TCP socket connected to the client
    -   `socks.onUdpAssociateRequest.clientFacingDatagramSocket`: The
        client-facing UDP socket that sends and receives UDP datagram packets 
        to and from the client
-   **Remote endpoints**: Endpoints that face or connect to targets/peers 
    that are remote from the client
    -   `socks.onBindRequest.remoteFacingServerSocket`: The remote-facing TCP
        server socket that waits for and accepts a TCP socket that is 
        connected to the remote client
    -   `socks.onBindRequest.remoteSocket`: The accepted TCP socket connected 
        to the remote client
    -   `socks.onConnectRequest.remoteSocket`: The TCP socket to be connected 
        to the remote server
    -   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket`: The
        remote-facing UDP socket that sends and receives UDP datagram packets 
        to and from the remote peer

You can configure all endpoints as a whole, you can configure all endpoints of 
one side of the server as a whole, or you can configure individual endpoints.

### Binding Endpoints to a Host Name or Address

You can bind endpoints to a host name or to a host address.

You can bind all endpoints to a host name or to a host address by using the 
setting `bindHost`. The host address can be an IPv4 address or an IPv6 address. 
The default value for the setting is the wildcard address `0.0.0.0`.

Command line examples:

```bash
jargyle start-server \
    --setting=bindHost=jargyle.net
```

```bash
jargyle start-server \
    --setting=bindHost=192.168.1.231
```

```bash
jargyle start-server \
    --setting=bindHost=fe80::34ef:938a:402f:e526%17
```

Server configuration file examples:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>bindHost</name>
            <value>jargyle.net</value>
        </setting>
    </settings>
</configuration>
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>bindHost</name>
            <value>192.168.1.231</value>
        </setting>
    </settings>
</configuration>
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>bindHost</name>
            <value>fe80::34ef:938a:402f:e526%17</value>
        </setting>
    </settings>
</configuration>
```

API examples:

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
                "bindHost", "jargyle.net")
        ))).start();
    }
}
```

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
                "bindHost", "192.168.1.231")
        ))).start();
    }
}
```

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
                "bindHost", "fe80::34ef:938a:402f:e526%17")
        ))).start();
    }
}
```

You can bind all endpoints of one side of the server to a host name or to a 
host address by using any of the following settings:

-   `clientEndpoints.bindHost`: The default binding host name or address for 
    all client endpoints
-   `remoteEndpoints.bindHost`: The default binding host name or address for
    all remote endpoints
-   `socks.onRequest.clientEndpoints.bindHost`: The binding host name or 
    address for all client endpoints
-   `socks.onRequest.remoteEndpoints.bindHost`: The binding host name or 
    address for all remote endpoints

The host address can be an IPv4 address or an IPv6 address. 

If no value is specified for the setting 
`socks.onRequest.clientEndpoints.bindHost`, then the value is obtained from 
the setting `clientEndpoints.bindHost`. If no value is specified for the 
setting `clientEndpoints.bindHost`, then the value is obtained from the 
setting `bindHost`. 

If no value is specified for the setting 
`socks.onRequest.remoteEndpoints.bindHost`, then the value is obtained from 
the setting `remoteEndpoints.bindHost`. If no value is specified for the 
setting `remoteEndpoints.bindHost`, then the value is obtained from the 
setting `bindHost`.

Command line example:

```bash
jargyle start-server \
    --setting=clientEndpoints.bindHost=jargyle.net \
    --setting=remoteEndpoints.bindHost=192.168.1.231
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientEndpoints.bindHost</name>
            <value>jargyle.net</value>
        </setting>
        <setting>
            <name>remoteEndpoints.bindHost</name>
            <value>192.168.1.231</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "clientEndpoints.bindHost", "jargyle.net"),
            Setting.newInstanceWithParsedValue(
                "remoteEndpoints.bindHost", "192.168.1.231")
        ))).start();
    }
}
```

You can bind individual endpoints to a host name or to a host address by using 
any of the following settings:

-   `clientFacingServerSocket.bindHost`: The binding host name or address for 
    the client-facing TCP server socket
-   `socks.onBindRequest.remoteFacingServerSocket.bindHost`: The binding host 
    name or address for the remote-facing TCP server socket
-   `socks.onConnectRequest.remoteSocket.bindHost`: The binding host name or 
    address for the remote TCP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost`: The 
    binding host name or address for the client-facing UDP socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost`: The
    binding host name or address for the remote-facing UDP socket

The host address can be an IPv4 address or an IPv6 address. 

If no value is specified for a setting, depending on the endpoint, the value 
is obtained from either of the settings 
`socks.onRequest.clientEndpoints.bindHost`, 
`socks.onRequest.remoteEndpoints.bindHost`, `clientEndpoints.bindHost`, or 
`remoteEndpoints.bindHost`. 

Command line example:

```bash
jargyle start-server \
    --setting=clientFacingServerSocket.bindHost=jargyle.net \
    --setting=socks.onConnectRequest.remoteSocket.bindHost=192.168.1.231
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientFacingServerSocket.bindHost</name>
            <value>jargyle.net</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHost</name>
            <value>192.168.1.231</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "clientFacingServerSocket.bindHost", "jargyle.net"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindHost", "192.168.1.231")
        ))).start();
    }
}
```

### Binding Endpoints to a Host Address from a Network Interface

Instead of binding endpoints to a specified host address, you can bind 
endpoints to a host address from a specified network interface.

You can bind all endpoints to a host address from a network interface by using 
the setting `netInterface`. The specified network interface must be the name 
of the network interface.

Command line example:

```bash
jargyle start-server \
    --setting=netInterface=eth0
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>netInterface</name>
            <value>eth0</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "netInterface", "eth0")
        ))).start();
    }
}
```

You can also specify which 
[host address types](common-topics.md#host-address-types) to choose from the 
network interface by using the setting `bindHostAddressTypes`. The default 
value for the setting `bindHostAddressTypes` is 
`HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`.

Command line example:

```bash
jargyle start-server \
    --setting=netInterface=eth0 \
    --setting=bindHostAddressTypes=HOST_IPV4_ADDRESS
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "netInterface", "eth0"), 
            Setting.newInstanceWithParsedValue(
                "bindHostAddressTypes", "HOST_IPV4_ADDRESS")
        ))).start();
    }
}
```

You can bind all endpoints of one side of the server to a host address from a 
network interface by using any of the following settings:

-   `clientEndpoints.netInterface`: The default network interface that 
    provides a binding host address for all client endpoints
-   `remoteEndpoints.netInterface`: The default network interface that 
    provides a binding host address for all remote endpoints
-   `socks.onRequest.clientEndpoints.netInterface`: The network interface that 
    provides a binding host address for all client endpoints
-   `socks.onRequest.remoteEndpoints.netInterface`: The network interface that 
    provides a binding host address for all remote endpoints

The specified network interface must be the name of the network interface.

If no value is specified for the setting
`socks.onRequest.clientEndpoints.netInterface`, then the value is obtained 
from the setting `clientEndpoints.netInterface`. If no value is specified for 
the setting `clientEndpoints.netInterface`, then the value is obtained from 
the setting `netInterface`.

If no value is specified for the setting
`socks.onRequest.remoteEndpoints.netInterface`, then the value is obtained 
from the setting `remoteEndpoints.netInterface`. If no value is specified for 
the setting `remoteEndpoints.netInterface`, then the value is obtained from 
the setting `netInterface`.

Command line example:

```bash
jargyle start-server \
    --setting=clientEndpoints.netInterface=eth0 \
    --setting=remoteEndpoints.netInterface=eth1
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientEndpoints.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>remoteEndpoints.netInterface</name>
            <value>eth1</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "clientEndpoints.netInterface", "eth0"),
                Setting.newInstanceWithParsedValue(
                        "remoteEndpoints.netInterface", "eth1")
        ))).start();
    }
}
```

You can also specify which host address types to choose from the network 
interface by using the following settings:

-   `clientEndpoints.bindHostAddressTypes`: The comma separated list of 
    default acceptable binding host address types for all client endpoints
-   `remoteEndpoints.bindHostAddressTypes`: The comma separated list of 
    default acceptable binding host address types for all remote endpoints
-   `socks.onRequest.clientEndpoints.bindHostAddressTypes`: The comma 
    separated list of acceptable binding host address types for all client 
    endpoints
-   `socks.onRequest.remoteEndpoints.bindHostAddressTypes`: The comma 
    separated list of acceptable binding host address types for all remote 
    endpoints

If no value is specified for the setting
`socks.onRequest.clientEndpoints.bindHostAddressTypes`, then the value is 
obtained from the setting `clientEndpoints.bindHostAddressTypes`. If no value 
is specified for the setting `clientEndpoints.bindHostAddressTypes`, then the 
value is obtained from the setting `bindHostAddressTypes`.

If no value is specified for the setting
`socks.onRequest.remoteEndpoints.bindHostAddressTypes`, then the value is 
obtained from the setting `remoteEndpoints.bindHostAddressTypes`. If no value 
is specified for the setting `remoteEndpoints.bindHostAddressTypes`, then the 
value is obtained from the setting `bindHostAddressTypes`.

Command line example:

```bash
jargyle start-server \
    --setting=clientEndpoints.netInterface=eth0 \
    --setting=clientEndpoints.bindHostAddressTypes=HOST_IPV4_ADDRESS \
    --setting=remoteEndpoints.netInterface=eth1 \
    --setting=remoteEndpoints.bindHostAddressTypes=HOST_IPV4_ADDRESS
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientEndpoints.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>clientEndpoints.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
        <setting>
            <name>remoteEndpoints.netInterface</name>
            <value>eth1</value>
        </setting>
        <setting>
            <name>remoteEndpoints.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "clientEndpoints.netInterface", "eth0"),
                Setting.newInstanceWithParsedValue(
                        "clientEndpoints.bindHostAddressTypes", "HOST_IPV4_ADDRESS"),
                Setting.newInstanceWithParsedValue(
                        "remoteEndpoints.netInterface", "eth1"),
                Setting.newInstanceWithParsedValue(
                        "remoteEndpoints.bindHostAddressTypes", "HOST_IPV4_ADDRESS")
        ))).start();
    }
}
```

You can bind individual endpoints to a host address from a network interface 
by using any of the following settings:

-   `clientFacingServerSocket.netInterface`: The network interface that 
    provides a binding host address for the client-facing TCP server socket
-   `socks.onBindRequest.remoteFacingServerSocket.netInterface`: The network 
    interface that provides a binding host address for the remote-facing TCP 
    server socket if the provided host address is all zeros
-   `socks.onConnectRequest.remoteSocket.netInterface`: The network interface 
    that provides a binding host address for the remote TCP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface`: The 
    network interface that provides a binding host address for the 
    client-facing UDP socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface`: The 
    network interface that provides a binding host address for the 
    remote-facing UDP socket

The specified network interface must be the name of the network interface.

If no value is specified for a setting, depending on the endpoint, the value
is obtained from either of the settings
`socks.onRequest.clientEndpoints.netInterface`,
`socks.onRequest.remoteEndpoints.netInterface`, 
`clientEndpoints.netInterface`, or 
`remoteEndpoints.netInterface`.

Command line example:

```bash
jargyle start-server \
    --setting=clientFacingServerSocket.netInterface=eth0 \
    --setting=socks.onConnectRequest.remoteSocket.netInterface=eth1
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientFacingServerSocket.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.netInterface</name>
            <value>eth1</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "clientFacingServerSocket.netInterface", "eth0"),
                Setting.newInstanceWithParsedValue(
                        "socks.onConnectRequest.remoteSocket.netInterface", "eth1")
        ))).start();
    }
}
```

You can also specify which host address types to choose from the network 
interface by using the following settings:

-   `clientFacingServerSocket.bindHostAddressTypes`: The comma separated list 
    of acceptable binding host address types for the client-facing TCP server 
    socket
-   `socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes`: The 
    comma separated list of acceptable binding host address types for the 
    remote-facing TCP server socket if the provided host address is all zeros
-   `socks.onConnectRequest.remoteSocket.bindHostAddressTypes`: The comma 
    separated list of acceptable binding host address types for the remote TCP 
    socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes`: 
    The comma separated list of acceptable binding host address types for the 
    client-facing UDP socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes`: 
    The comma separated list of acceptable binding host address types for the 
    remote-facing UDP socket

If no value is specified for a setting, depending on the endpoint, the value
is obtained from either of the settings
`socks.onRequest.clientEndpoints.bindHostAddressTypes`,
`socks.onRequest.remoteEndpoints.bindHostAddressTypes`,
`clientEndpoints.bindHostAddressTypes`, or
`remoteEndpoints.bindHostAddressTypes`.

Command line example:

```bash
jargyle start-server \
    --setting=clientFacingServerSocket.netInterface=eth0 \
    --setting=clientFacingServerSocket.bindHostAddressTypes=HOST_IPV4_ADDRESS \
    --setting=socks.onConnectRequest.remoteSocket.netInterface=eth1 \
    --setting=socks.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_IPV4_ADDRESS
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientFacingServerSocket.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>clientFacingServerSocket.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.netInterface</name>
            <value>eth1</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "clientFacingServerSocket.netInterface", "eth0"),
                Setting.newInstanceWithParsedValue(
                        "clientFacingServerSocket.bindHostAddressTypes", "HOST_IPV4_ADDRESS"),
                Setting.newInstanceWithParsedValue(
                        "socks.onConnectRequest.remoteSocket.netInterface", "eth1"),
                Setting.newInstanceWithParsedValue(
                        "socks.onConnectRequest.remoteSocket.bindHostAddressTypes", "HOST_IPV4_ADDRESS")
        ))).start();
    }
}
```

### Binding Endpoints to a Port Number Range or Port Number Ranges

You can bind endpoints to a 
[port number range or port number ranges](common-topics.md#port-number-ranges).

You can bind all endpoints to a port number range or port number ranges using 
the following settings:

-   `bindTcpPortRanges`: The comma separated list of default binding port 
    ranges for all TCP endpoints (default value is `0`)
-   `bindUdpPortRanges`: The comma separated list of default binding port 
    ranges for all UDP endpoints (default value is `0`)

Command line example:

```bash
jargyle start-server \
    --setting=bindTcpPortRanges=5000-6000 \
    --setting=bindUdpPortRanges=10000-20000,40000-50000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>bindTcpPortRanges</name>
            <portRanges>
                <portRange>5000-6000</portRange>
            </portRanges>
        </setting>
        <setting>
            <name>bindUdpPortRanges</name>
            <portRanges>
                <portRange>10000-20000</portRange>
                <portRange>40000-50000</portRange>
            </portRanges>            
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "bindTcpPortRanges", "5000-6000"),
                Setting.newInstanceWithParsedValue(
                        "bindUdpPortRanges", "10000-20000,40000-50000")
        ))).start();
    }
}
```

You can bind all endpoints of one side of the server to a port number range or 
port number ranges using the following settings:

-   `clientEndpoints.bindTcpPortRanges`: The comma separated list of default 
    binding port ranges for all client TCP endpoints
-   `clientEndpoints.bindUdpPortRanges`: The comma separated list of default 
    binding port ranges for all client UDP endpoints
-   `remoteEndpoints.bindTcpPortRanges`: The comma separated list of default 
    binding port ranges for all remote TCP endpoints
-   `remoteEndpoints.bindUdpPortRanges`: The comma separated list of default 
    binding port ranges for all remote UDP endpoints
-   `socks.onRequest.clientEndpoints.bindUdpPortRanges`: The comma separated 
    list of binding port ranges for all client UDP endpoints
-   `socks.onRequest.remoteEndpoints.bindTcpPortRanges`: The comma separated 
    list of binding port ranges for all remote TCP endpoints
-   `socks.onRequest.remoteEndpoints.bindUdpPortRanges`: The comma separated 
    list of binding port ranges for all remote UDP endpoints

If no value is specified for the setting
`socks.onRequest.clientEndpoints.bindUdpPortRanges`, then the value is
obtained from the setting `clientEndpoints.bindUdpPortRanges`. If no value
is specified for the setting `clientEndpoints.bindUdpPortRanges`, then the
value is obtained from the setting `bindUdpPortRanges`.

If no value is specified for the setting
`socks.onRequest.remoteEndpoints.bindTcpPortRanges`, then the value is
obtained from the setting `remoteEndpoints.bindTcpPortRanges`. If no value
is specified for the setting `remoteEndpoints.bindTcpPortRanges`, then the
value is obtained from the setting `bindTcpPortRanges`.

If no value is specified for the setting
`socks.onRequest.remoteEndpoints.bindUdpPortRanges`, then the value is
obtained from the setting `remoteEndpoints.bindUdpPortRanges`. If no value
is specified for the setting `remoteEndpoints.bindUdpPortRanges`, then the
value is obtained from the setting `bindUdpPortRanges`.

Command line example:

```bash
jargyle start-server \
    --setting=clientEndpoints.bindTcpPortRanges=5000-5500 \
    --setting=clientEndpoints.bindUdpPortRanges=10000-20000 \
    --setting=remoteEndpoints.bindTcpPortRanges=5600-6000,8000-9000 \
    --setting=remoteEndpoints.bindUdpPortRanges=40000-50000,60000-64000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientEndpoints.bindTcpPortRanges</name>
            <portRanges>
                <portRange>5000-5500</portRange>
            </portRanges>
        </setting>
        <setting>
            <name>clientEndpoints.bindUdpPortRanges</name>
            <portRanges>
                <portRange>10000-20000</portRange>
            </portRanges>            
        </setting>
        <setting>
            <name>remoteEndpoints.bindTcpPortRanges</name>
            <portRanges>
                <portRange>5600-6000</portRange>
                <portRange>8000-9000</portRange>
            </portRanges>
        </setting>
        <setting>
            <name>remoteEndpoints.bindUdpPortRanges</name>
            <portRanges>
                <portRange>40000-50000</portRange>
                <portRange>60000-64000</portRange>
            </portRanges>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "clientEndpoints.bindTcpPortRanges", "5000-5500"),
                Setting.newInstanceWithParsedValue(
                        "clientEndpoints.bindUdpPortRanges", "10000-20000"),
                Setting.newInstanceWithParsedValue(
                        "remoteEndpoints.bindTcpPortRanges", "5600-6000,8000-9000"),
                Setting.newInstanceWithParsedValue(
                        "remoteEndpoints.bindUdpPortRanges", "40000-50000,60000-64000")
        ))).start();
    }
}
```

You can bind individual endpoints to a port number range or port number ranges 
using the following settings:

-   `clientFacingServerSocket.bindPortRanges`: The comma separated list of 
    binding port ranges for the client-facing TCP server socket
-   `socks.onBindRequest.remoteFacingServerSocket.bindPortRanges`: The comma 
    separated list of binding port ranges for the remote-facing TCP server 
    socket if the provided port is zero
-   `socks.onConnectRequest.remoteSocket.bindPortRanges`: The comma separated 
    list of binding port ranges for the remote TCP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges`: 
    The comma separated list of binding port ranges for the client-facing UDP 
    socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges`: 
    The comma separated list of binding port ranges for the remote-facing UDP 
    socket

If no value is specified for a setting, depending on the endpoint, the value
is obtained from either of the settings
`socks.onRequest.clientEndpoints.bindUdpPortRanges`,
`socks.onRequest.remoteEndpoints.bindTcpPortRanges`,
`socks.onRequest.remoteEndpoints.bindUdpPortRanges`,
`clientEndpoints.bindTcpPortRanges`, `clientEndpoints.bindUdpPortRanges`,
`remoteEndpoints.bindTcpPortRanges`, or `remoteEndpoints.bindUdpPortRanges`.

Command line example:

```bash
jargyle start-server \
    --setting=socks.onConnectRequest.remoteSocket.bindPortRanges=5600-6000,8000-9000 \
    --setting=socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges=10000-20000 \
    --setting=socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges=40000-50000,60000-64000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindPortRanges</name>
            <portRanges>
                <portRange>5600-6000</portRange>
                <portRange>8000-9000</portRange>
            </portRanges>
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges</name>
            <portRanges>
                <portRange>10000-20000</portRange>
            </portRanges>            
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges</name>
            <portRanges>
                <portRange>40000-50000</portRange>
                <portRange>60000-64000</portRange>
            </portRanges>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "socks.onConnectRequest.remoteSocket.bindPortRanges", "5600-6000,8000-9000"),
                Setting.newInstanceWithParsedValue(
                        "socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges", "10000-20000"),
                Setting.newInstanceWithParsedValue(
                        "socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges", "40000-50000,60000-64000")
        ))).start();
    }
}
```

### Applying Socket Settings to Endpoints

You can apply [socket settings](common-topics.md#socket-settings) to endpoints.

You can apply socket settings to all endpoints by using the setting 
`socketSettings`.

Command line example:

```bash
jargyle start-server \
    --setting=socketSettings=SO_RCVBUF=256
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socketSettings", "SO_RCVBUF=256")
        ))).start();
    }
}
```

You can apply socket settings to all endpoints of one side of the server by 
using the following settings:

-   `clientEndpoints.socketSettings`: The comma separated list of default 
    socket settings for all client endpoints
-   `remoteEndpoints.socketSettings`: The comma separated list of default 
    socket settings for all remote endpoints
-   `socks.onRequest.clientEndpoints.socketSettings`: The comma separated list 
    of socket settings for all client endpoints
-   `socks.onRequest.remoteEndpoints.socketSettings`: The comma separated list 
    of socket settings for all remote endpoints

If no value is specified for the setting
`socks.onRequest.clientEndpoints.socketSettings`, then the value is
obtained from the setting `clientEndpoints.socketSettings`. If no value
is specified for the setting `clientEndpoints.socketSettings`, then the
value is obtained from the setting `socketSettings`.

If no value is specified for the setting
`socks.onRequest.remoteEndpoints.socketSettings`, then the value is
obtained from the setting `remoteEndpoints.socketSettings`. If no value
is specified for the setting `remoteEndpoints.socketSettings`, then the
value is obtained from the setting `socketSettings`.

Command line example:

```bash
jargyle start-server \
    --setting=clientEndpoints.socketSettings=SO_RCVBUF=256 \
    --setting=remoteEndpoints.socketSettings=SO_RCVBUF=256
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientEndpoints.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>remoteEndpoints.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>        
    </settings>
</configuration>
```

API example:

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
                "clientEndpoints.socketSettings", "SO_RCVBUF=256"),
            Setting.newInstanceWithParsedValue(
                "remoteEndpoints.socketSettings", "SO_RCVBUF=256")
        ))).start();
    }
}
```

You can apply socket settings to individual endpoints by using the following 
settings:

-   `clientFacingServerSocket.socketSettings`: The comma separated list of 
    socket settings for the client-facing TCP server socket
-   `clientSocket.socketSettings`: The comma separated list of socket settings 
    for the client TCP socket
-   `socks.onBindRequest.remoteFacingServerSocket.socketSettings`: The comma 
    separated list of socket settings for the remote-facing TCP server socket
-   `socks.onBindRequest.remoteSocket.socketSettings`: The comma separated 
    list of socket settings for the remote TCP socket
-   `socks.onConnectRequest.remoteSocket.socketSettings`: The comma separated 
    list of socket settings for the remote TCP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings`: 
    The comma separated list of socket settings for the client-facing UDP 
    socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings`: 
    The comma separated list of socket settings for the remote-facing UDP 
    socket

If no value is specified for a setting, depending on the endpoint, the value
is obtained from either of the settings
`socks.onRequest.clientEndpoints.socketSettings`,
`socks.onRequest.remoteEndpoints.socketSettings`,
`clientEndpoints.socketSettings`, or `remoteEndpoints.socketSettings`.

Command line example:

```bash
jargyle start-server \
    --setting=clientFacingServerSocket.socketSettings=SO_RCVBUF=256 \
    --setting=socks.onBindRequest.remoteFacingServerSocket.socketSettings=SO_RCVBUF=256 \
    --setting=socks.onConnectRequest.remoteSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>clientFacingServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onBindRequest.remoteFacingServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "clientFacingServerSocket.socketSettings", "SO_RCVBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.remoteFacingServerSocket.socketSettings", "SO_RCVBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256")
        ))).start();
    }
}
```

### TCP Socket Connection Types

There are two types of connections a TCP socket can make: an extemporaneous 
connection and a prepared connection. Each connection type has its advantages 
and disadvantages.

#### Extemporaneous TCP Socket Connection Type

When an extemporaneous connection is about to be made, no preparation is made 
for the TCP socket before connecting. Here are the following 
advantages/disadvantages:

**Advantages**:

-   When having the TCP socket's traffic set through a SOCKS server, if a
    target host name is provided for the TCP socket to connect to, the target
    host name will be resolved by the SOCKS server.

**Disadvantages**:

-   Some socket settings might not be applied after the connection is made.
    Only a prepared outbound connection will allow for such socket settings
    to be applied.
-   No timeout in milliseconds can be specified for how long a wait for the
    TCP socket to connect.

#### Prepared TCP Socket Connection Type

When a prepared outbound connection is about to be made, preparation is made
for the TCP socket before connecting. Such preparation includes applying the
specified socket settings for the TCP socket, resolving the target host name,
and setting the specified timeout in milliseconds on waiting for the TCP
socket to connect. Here are the following advantages/disadvantages:

**Advantages**:

-   Socket settings that can only be applied before a connection is made can
    be applied.
-   A timeout in milliseconds can be specified for how long a wait for the TCP
    socket to connect.

**Disadvantages**:

-   When having the TCP socket's traffic set through a SOCKS server, if a
    target host name is provided for the TCP socket to connect to, the target
    host name is resolved through the local system not the SOCKS server.

### Preparing the Remote TCP Socket Before Connecting

You can [prepare](#prepared-tcp-socket-connection-type) the remote TCP socket 
before connecting by having the setting 
`socks.onConnectRequest.remoteSocket.prepareSocket` set to `true`. 

Command line example:

```bash
jargyle start-server \
    --setting=socks.onConnectRequest.remoteSocket.prepareSocket=true \
    --setting=socks.onConnectRequest.remoteSocket.connectTimeout=120000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.prepareSocket</name>
            <value>true</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.connectTimeout</name>
            <value>120000</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                        "socks.onConnectRequest.remoteSocket.prepareSocket", "true"),
                Setting.newInstanceWithParsedValue(
                        "socks.onConnectRequest.remoteSocket.connectTimeout", "120000")
        ))).start();
    }
}
```

<a id="enabling-ssltls-layered-tcp-traffic-between-the-server-and-its-clients"></a>
## Enabling SSL/TLS-layered TCP Traffic Between the Server and Its Clients

You can enable SSL/TLS-layered TCP traffic between the server and its clients. 
By default, SSL/TLS-layered TCP traffic between the server and its clients is 
disabled. To enable SSL/TLS-layered TCP traffic between the server and its 
clients, you will need to have the setting `ssl.enabled` set to `true`. In 
addition, you will need to have the setting `ssl.keyStoreFile` to specify the 
server's key store file (this file would need to be created by Java's keytool 
utility). Also, you will need to have the setting `ssl.keyStorePassword` to 
specify the password for the server's key store file.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>ssl.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "password")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-ssl-key-store-pass` instead. It will provide an interactive 
prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --enter-ssl-key-store-pass
```

If you want to have the client authenticate using SSL/TLS, you will need to have 
the setting `ssl.needClientAuth` set to `true`. In addition, you will need 
to have the setting `ssl.trustStoreFile` to specify the client's key store 
file to be used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the setting 
`ssl.trustStorePassword` to specify the password for the client's trust 
store file.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=password \
    --setting=ssl.needClientAuth=true \
    --setting=ssl.trustStoreFile=client.jks \
    --setting=ssl.trustStorePassword=drowssap
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>ssl.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>ssl.needClientAuth</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.trustStoreFile</name>
            <value>client.jks</value>
        </setting>
        <setting>
            <name>ssl.trustStorePassword</name>
            <value>drowssap</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "password"),
            Setting.newInstanceWithParsedValue(
                "ssl.needClientAuth", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.trustStoreFile", "client.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.trustStorePassword", "drowssap")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-ssl-trust-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --enter-ssl-key-store-pass \
    --setting=ssl.needClientAuth=true \
    --setting=ssl.trustStoreFile=client.jks \
    --enter-ssl-trust-store-pass
```

<a id="enabling-dtls-layered-udp-traffic-between-the-server-and-its-clients"></a>
## Enabling DTLS-layered UDP Traffic Between the Server and Its Clients

You can enable DTLS-layered UDP traffic between the server and its clients. By 
default, DTLS-layered UDP traffic between the server and its clients is 
disabled. To enable DTLS-layered UDP traffic between the server and its 
clients, you will need to have the setting `dtls.enabled` set to `true`. In 
addition, you will need to have the setting `dtls.keyStoreFile` to specify the 
server's key store file (this file would need to be created by Java's keytool 
utility). Also, you will need to have the setting `dtls.keyStorePassword` to 
specify the password for the server's key store file.

Command line example:

```bash
jargyle start-server \
    --setting=dtls.enabled=true \
    --setting=dtls.keyStoreFile=server.jks \
    --setting=dtls.keyStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>dtls.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>dtls.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>dtls.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStorePassword", "password")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-dtls-key-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=dtls.enabled=true \
    --setting=dtls.keyStoreFile=server.jks \
    --enter-dtls-key-store-pass
```

## Using SOCKS5 Authentication

The server has the following SOCKS5 authentication methods to choose from:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

From the command line and from the API, you can have one or more of the 
aforementioned authentication methods set in the setting `socks5.methods` as 
a comma separated list. 

Command line example:

```bash
jargyle start-server --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI
```

API example:

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
                "socks5.methods", 
                "NO_AUTHENTICATION_REQUIRED,GSSAPI")
        ))).start();
    }
}
```

In the server configuration file, you can have one or more of the aforementioned 
authentication methods set in the setting `socks5.methods` as a 
`<socks5.methods/>` XML element with one or more `<socks5.method/>` XML 
elements each specifying an authentication method.

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

If not set, the default value for the setting `socks5.methods` is set to 
`NO_AUTHENTICATION_REQUIRED`

### Using No Authentication

Because the default value for the setting `socks5.methods` is set to 
`NO_AUTHENTICATION_REQUIRED`, it is not required for
`NO_AUTHENTICATION_REQUIRED` to be included in the setting 
`socks5.methods`.

However, if other authentication methods are to be used in addition to 
`NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be 
included in the setting `socks5.methods`

Command line example:

```bash
jargyle start-server --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socks5.methods", 
                "NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD")
        ))).start();
    }
}
```

### Using Username Password Authentication

To use username password authentication, you will need to have the setting 
`socks5.methods` to have `USERNAME_PASSWORD` included. You will also need 
to have the setting `socks5.userpassauthmethod.userRepository` to specify the 
type name of the user repository along with an initialization string value.

The following are two type names you can use:

-   `FileSourceUserRepository`
-   `StringSourceUserRepository`

`FileSourceUserRepository`: This user repository handles the storage of the 
SOCKS5 users from an initialization string value of a provided file of a list of 
URL encoded username and hashed password pairs. The SOCKS5 users from the file 
are loaded onto memory. Because of this, you will need at least as much memory 
as the size of the file. If the file does not exist, it will be created and 
used. If the file does exist, the existing file will be used. To manage SOCKS5 
users under a user repository, see 
[Managing SOCKS5 Users](cli.md#managing-socks5-users).

Command line example:

```bash
jargyle start-server \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauthmethod.userRepository=FileSourceUserRepository:users
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>socks5.userpassauthmethod.userRepository</name>
            <socks5.userpassauthmethod.userRepository>
                <typeName>FileSourceUserRepository</typeName>
                <initializationString>users</initializationString>
            </socks5.userpassauthmethod.userRepository>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "socks5.userpassauthmethod.userRepository", 
                "FileSourceUserRepository:users")
        ))).start();
    }
}
```

`StringSourceUserRepository`: This user repository handles the storage of 
the SOCKS5 users from an initialization string value of a comma separated list 
of URL encoded username and password pairs.

Each username and password pair in the comma separated list must be of the 
following format:

```text
USERNAME:PASSWORD
```

`USERNAME` is the username and `PASSWORD` is the password.

If the username or the password contains a colon character (`:`), then each 
colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a comma character (`,`), then each 
comma character must be replaced with the URL encoding character `%2C`.

If the username or the password contains a percent sign character (`%`) not 
used for URL encoding, then each percent sign character not used for URL 
encoding must be replaced with the URL encoding character `%25`.

Command line example:

```bash
jargyle start-server \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauthmethod.userRepository=StringSourceUserRepository:Aladdin:opensesame,Jasmine:mission%3Aimpossible
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>socks5.userpassauthmethod.userRepository</name>
            <socks5.userpassauthmethod.userRepository>
                <typeName>StringSourceUserRepository</typeName>
                <initializationString>Aladdin:opensesame,Jasmine:mission%3Aimpossible</initializationString>
            </socks5.userpassauthmethod.userRepository>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "socks5.userpassauthmethod.userRepository",
                "StringSourceUserRepository:Aladdin:opensesame,Jasmine:mission%3Aimpossible")
        ))).start();
    }
}
```

### Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting 
`socks5.methods` to have `GSSAPI` included. You will need to specify Java 
system properties to use a security mechanism that implements the GSS-API (for 
example, Kerberos is a security mechanism that implements the GSS-API).

The following are sufficient examples of using the Kerberos security mechanism:

Command line example:

```bash
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
jargyle start-server --setting=socks5.methods=GSSAPI 
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!--
Before running this file, the following must be executed:
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
-->
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "javax.security.auth.useSubjectCredsOnly", "true");
        System.setProperty(
            "java.security.auth.login.config", "login.conf");
        System.setProperty("java.security.krb5.conf", "krb5.conf");
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", "GSSAPI")
        ))).start();
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
com.sun.security.jgss.accept {
  com.sun.security.auth.module.Krb5LoginModule required
  principal="rcmd/jargyle.net"
  useKeyTab=true
  keyTab="rcmd.keytab"
  storeKey=true;
};
```

In `login.conf`, `rcmd/jargyle.net` is a service principal that is created by 
a Kerberos administrator specifically for a SOCKS5 server with the service name 
`rcmd` residing at `jargyle.net`.

Also in `login.conf`, `rcmd.keytab` is a keytab file also created by a 
Kerberos administrator that contains the aforementioned service principal and 
its respective encrypted key.  

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

## Chaining to Another SOCKS Server

You can have the server chained to another SOCKS server, meaning that its 
traffic can be routed through another SOCKS server. To have the server chained 
to another SOCKS server, you will need to specify the other SOCKS server as a 
URI in the setting `chaining.socksServerUri`

Command line example:

```bash
jargyle start-server --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111")
        ))).start();
    }
}
```

Please note that the scheme in the URI specifies the SOCKS protocol to be used 
to access the other SOCKS server (`socks5`), the address or name of the
machine of where the other SOCKS server resides (`alpha-alpha.net`), and the 
port number of the other SOCKS server (`11111`). In the aforementioned 
examples, the SOCKS protocol version 5 is used. At this time, the only 
supported scheme for the URI format is `socks5`

### Configuring the Other SOCKS Server TCP Socket

The server uses endpoints to interact with the world remote from the client.
When the server is chained to another SOCKS server, those endpoints that the 
server uses become SOCKS-enabled, meaning that their traffic is routed through 
the other SOCKS server. In each of those endpoints is an underlying TCP socket 
to the other SOCKS server. In this documentation, this underlying TCP socket 
is called the other SOCKS server TCP socket.  

Depending on the endpoint, the other SOCKS server TCP socket can be configured 
in only one of the following ways:

-   **Configuring the endpoint**. Configuring certain endpoints configure 
    their other SOCKS server TCP sockets without any special settings 
    required. `socks.onConnectRequest.remoteSocket` is such an endpoint.
-   **Configuring the endpoint and using settings prefixed with 
    `chaining.socksServerSocket`**. Configuring certain endpoints configure 
    some parts of their other SOCKS server TCP sockets but the special 
    settings are required to configure other parts of their other SOCKS server 
    TCP sockets. `socks.onBindRequest.remoteFacingServerSocket` is such an 
    endpoint.
-   **Using settings prefixed with `chaining.socksServerSocket`**. Configuring 
    certain endpoints does nothing to their other SOCKS server TCP sockets.
    `socks.onUdpAssociateRequest.remoteFacingDatagramSocket` is such an 
    endpoint. Special settings are required to configure their other SOCKS 
    server TCP sockets.   

<a id="binding-the-other-socks-server-tcp-socket-to-a-host-name-or-address"></a>
#### Binding the Other SOCKS Server TCP Socket to a Host Name or Address

You can bind the other SOCKS server TCP socket to a host name or to a host 
address.

For the endpoint `socks.onConnectRequest.remoteSocket`, you would use the 
setting `socks.onConnectRequest.remoteSocket.bindHost`.

For all other endpoints, you would use the setting 
`chaining.socksServerSocket.bindHost`.

The host address can be an IPv4 address or an IPv6 address.

Command line examples:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.bindHost=jargyle.net \
    --setting=socks.onConnectRequest.remoteSocket.bindHost=jargyle.net
```

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.bindHost=192.168.1.231 \
    --setting=socks.onConnectRequest.remoteSocket.bindHost=192.168.1.231
```

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.bindHost=fe80::34ef:938a:402f:e526%17 \
    --setting=socks.onConnectRequest.remoteSocket.bindHost=fe80::34ef:938a:402f:e526%17
```

Server configuration file examples:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.bindHost</name>
            <value>jargyle.net</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHost</name>
            <value>jargyle.net</value>
        </setting>
    </settings>
</configuration>
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.bindHost</name>
            <value>192.168.1.231</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHost</name>
            <value>192.168.1.231</value>
        </setting>
    </settings>
</configuration>
```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.bindHost</name>
            <value>fe80::34ef:938a:402f:e526%17</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHost</name>
            <value>fe80::34ef:938a:402f:e526%17</value>
        </setting>
    </settings>
</configuration>
```

API examples:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.bindHost",
                "jargyle.net"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindHost",
                "jargyle.net")                
        ))).start();
    }
}
```

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.bindHost",
                "192.168.1.231"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindHost",
                "192.168.1.231")                
        ))).start();
    }
}
```

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.bindHost",
                "fe80::34ef:938a:402f:e526%17"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindHost",
                "fe80::34ef:938a:402f:e526%17")                
        ))).start();
    }
}
```

<a id="binding-the-other-socks-server-tcp-socket-to-a-host-address-from-a-network-interface"></a>
#### Binding the Other SOCKS Server TCP Socket to a Host Address from a Network Interface

Instead of binding the other SOCKS server TCP socket to a specified host 
address, you can bind it to a host address from a specified network interface.

For the endpoint `socks.onConnectRequest.remoteSocket`, you would use the 
setting `socks.onConnectRequest.remoteSocket.netInterface`.

For all other endpoints, you would use the setting 
`chaining.socksServerSocket.netInterface`. 

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.netInterface=eth0 \
    --setting=socks.onConnectRequest.remoteSocket.netInterface=eth0
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.netInterface</name>
            <value>eth0</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri",
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.netInterface", "eth0"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.netInterface", "eth0")
        ))).start();
    }
}
```

You can also specify which 
[host address types](common-topics.md#host-address-types) to choose from the 
network interface.

For the endpoint `socks.onConnectRequest.remoteSocket`, you would use the
setting `socks.onConnectRequest.remoteSocket.bindHostAddressTypes`.

For all other endpoints, you would use the setting
`chaining.socksServerSocket.bindHostAddressTypes`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.netInterface=eth0 \
    --setting=chaining.socksServerSocket.bindHostAddressTypes=HOST_IPV4_ADDRESS \
    --setting=socks.onConnectRequest.remoteSocket.netInterface=eth0 \
    --setting=socks.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_IPV4_ADDRESS
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>        
        <setting>
            <name>socks.onConnectRequest.remoteSocket.netInterface</name>
            <value>eth0</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindHostAddressTypes</name>
            <hostAddressTypes>
                <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
            </hostAddressTypes>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri",
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.netInterface", "eth0"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.bindHostAddressTypes", "HOST_IPV4_ADDRESS"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.netInterface", "eth0"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindHostAddressTypes", "HOST_IPV4_ADDRESS")
        ))).start();
    }
}
```

<a id="binding-the-other-socks-server-tcp-socket-to-a-port-number-range-or-port-number-ranges"></a>
#### Binding the Other SOCKS Server TCP Socket to a Port Number Range or Port Number Ranges

You can bind the other SOCKS server TCP socket to a 
[port number range or port number ranges](common-topics.md#port-number-ranges).

For the endpoint `socks.onConnectRequest.remoteSocket`, you would use the
setting `socks.onConnectRequest.remoteSocket.bindPortRanges`.

For all other endpoints, you would use the setting
`chaining.socksServerSocket.bindPortRanges`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.bindPortRanges=5000-6000 \
    --setting=socks.onConnectRequest.remoteSocket.bindPortRanges=5000-6000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.bindPortRanges</name>
            <portRanges>
                <portRange>5000-6000</portRange>
            </portRanges>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.bindPortRanges</name>
            <portRanges>
                <portRange>5000-6000</portRange>
            </portRanges>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri",
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.bindPortRanges", "5000-6000"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.bindPortRanges", "5000-6000")
        ))).start();
    }
}
```

#### Applying Socket Settings to the Other SOCKS Server TCP Socket

You can apply [socket settings](common-topics.md#socket-settings) to the other 
SOCKS server TCP socket.

For the endpoint `socks.onConnectRequest.remoteSocket`, you would use the
setting `socks.onConnectRequest.remoteSocket.socketSettings`.

For the endpoint `socks.onBindRequest.remoteFacingServerSocket`, you would use 
the setting `socks.onBindRequest.remoteFacingServerSocket.socketSettings`.

For the endpoint `socks.onBindRequest.remoteSocket`, you would use the setting 
`socks.onBindRequest.remoteSocket.socketSettings`.

For the endpoint `socks.onUdpAssociateRequest.remoteFacingDatagramSocket`, you 
would use the setting `chaining.socksServerSocket.socketSettings`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256 \
    --setting=socks.onConnectRequest.remoteSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256 \
    --setting=socks.onBindRequest.remoteFacingServerSocket.socketSettings=SO_RCVBUF=256 \
    --setting=socks.onBindRequest.remoteSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onBindRequest.remoteFacingServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onBindRequest.remoteSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri",
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.remoteFacingServerSocket.socketSettings", "SO_RCVBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.remoteSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256")
        ))).start();
    }
}
```

#### Preparing the Other SOCKS Server TCP Socket Before Connecting

You can [prepare](#prepared-tcp-socket-connection-type) the other SOCKS server 
TCP socket before connecting.

For the endpoint `socks.onConnectRequest.remoteSocket`, you would have the
setting `socks.onConnectRequest.remoteSocket.prepareSocket` set to `true`.

For all other endpoints, you would have the setting
`chaining.socksServerSocket.prepareSocket` set to `true`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerSocket.prepareSocket=true \
    --setting=chaining.socksServerSocket.connectTimeout=120000 \
    --setting=socks.onConnectRequest.remoteSocket.prepareSocket=true \
    --setting=socks.onConnectRequest.remoteSocket.connectTimeout=120000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.prepareSocket</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.connectTimeout</name>
            <value>120000</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.prepareSocket</name>
            <value>true</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.connectTimeout</name>
            <value>120000</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri",
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.prepareSocket", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.connectTimeout", "120000"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.prepareSocket", "true"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.connectTimeout", "120000")
        ))).start();
    }
}
```

<a id="summary-for-configuring-the-other-socks-server-tcp-socket"></a>
#### Summary for Configuring the Other SOCKS Server TCP Socket

The following is a table that summarizes which setting to use to configure the 
other SOCKS server TCP socket:

<table>
<tr>
<th colspan="4">Endpoints</th>
</tr>
<tr>
<th></th>
<th><code>socks.onConnectRequest.remoteSocket</code></th>
<th><code>socks.onBindRequest.remoteFacingServerSocket</code></th>
<th><code>socks.onBindRequest.remoteSocket</code></th>
<th><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket</code></th>
</tr>
<tr>
<td>Binding the Other SOCKS Server TCP Socket to a Host Name or Address</td>
<td><code>socks.onConnectRequest.remoteSocket.bindHost</code></td>
<td><code>chaining.socksServerSocket.bindHost</code></td>
<td><i>Not applicable</i></td>
<td><code>chaining.socksServerSocket.bindHost</code></td>
</tr>
<tr>
<td>Binding the Other SOCKS Server TCP Socket to a Host Address from a Network Interface</td>
<td><code>socks.onConnectRequest.remoteSocket.netInterface</code><br/><code>socks.onConnectRequest.remoteSocket.bindHostAddressTypes</code></td>
<td><code>chaining.socksServerSocket.netInterface</code><br/><code>chaining.socksServerSocket.bindHostAddressTypes</code></td>
<td><i>Not applicable</i></td>
<td><code>chaining.socksServerSocket.netInterface</code><br/><code>chaining.socksServerSocket.bindHostAddressTypes</code></td>
</tr>
<tr>
<td>Binding the Other SOCKS Server TCP Socket to a Port Number Range or Port Number Ranges</td>
<td><code>socks.onConnectRequest.remoteSocket.bindPortRanges</code></td>
<td><code>chaining.socksServerSocket.bindPortRanges</code></td>
<td><i>Not applicable</i></td>
<td><code>chaining.socksServerSocket.bindPortRanges</code></td>
</tr>
<tr>
<td>Applying Socket Settings to the Other SOCKS Server TCP Socket</td>
<td><code>socks.onConnectRequest.remoteSocket.socketSettings</code></td>
<td><code>socks.onBindRequest.remoteFacingServerSocket.socketSettings</code></td>
<td><code>socks.onBindRequest.remoteSocket.socketSettings</code></td>
<td><code>chaining.socksServerSocket.socketSettings</code></td>
</tr>
<tr>
<td>Preparing the Other SOCKS Server TCP Socket Before Connecting</td>
<td><code>socks.onConnectRequest.remoteSocket.prepareSocket</code></td>
<td><code>chaining.socksServerSocket.prepareSocket</code></td>
<td><i>Not applicable</i></td>
<td><code>chaining.socksServerSocket.prepareSocket</code></td>
</tr>
</table>

<a id="enabling-ssltls-layered-tcp-traffic-between-the-server-and-the-other-socks-server"></a>
### Enabling SSL/TLS-layered TCP Traffic Between the Server and the Other SOCKS Server

You can enable SSL/TLS-layered TCP traffic between the server and the other 
SOCKS server under the following condition: 

-   The other SOCKS server accepts SSL/TLS connections.

By default, SSL/TLS-layered TCP traffic between the server and the other SOCKS 
server is disabled. To enable SSL/TLS-layered TCP traffic between the server 
and the other SOCKS server, you will need to have the setting 
`chaining.ssl.enabled` set to `true`. In addition, you will need to have the 
setting `chaining.ssl.trustStoreFile` to specify the other SOCKS server's key 
store file used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the setting 
`chaining.ssl.trustStorePassword` to specify the password for the other SOCKS 
server's trust store file.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-ssl-trust-store-pass` instead. It will provide 
an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
```

If the other SOCKS server wants the client (the server) to authenticate using 
SSL/TLS, you will need to have the setting `chaining.ssl.keyStoreFile` to 
specify the client's key store file (this file would need to be created by 
Java's keytool utility). Also, you will need to have the setting 
`chaining.ssl.keyStorePassword` to specify the password for the client's 
key store file.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --setting=chaining.ssl.keyStorePassword=drowssap \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password    
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.ssl.keyStoreFile</name>
            <value>client.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.keyStorePassword</name>
            <value>drowssap</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>chaining.ssl.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.keyStoreFile", "client.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.keyStorePassword", "drowssap"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-ssl-key-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --enter-chaining-ssl-key-store-pass \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
```

<a id="enabling-dtls-layered-udp-traffic-between-the-server-and-the-other-socks-server"></a>
### Enabling DTLS-layered UDP Traffic Between the Server and the Other SOCKS Server

You can enable DTLS-layered UDP traffic between the server and the other SOCKS 
server under the following condition: 

-   The other SOCKS server accepts DTLS connections.

By default, DTLS-layered UDP traffic between the server and the other SOCKS 
server is disabled. To enable DTLS-layered UDP traffic between the server and 
the other SOCKS server, you will need to have the setting 
`chaining.dtls.enabled` set to `true`. In addition, you will need to have the 
setting `chaining.dtls.trustStoreFile` to specify the other SOCKS server's key 
store file used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the setting 
`chaining.dtls.trustStorePassword` to specify the password for the other SOCKS 
server's trust store file.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.dtls.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.dtls.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.dtls.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStorePassword", "password")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-dtls-trust-store-pass` instead. It will provide 
an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
```

### Chaining to the Other SOCKS Server Using SOCKS5 Authentication

The server has the following SOCKS5 authentication methods to choose from when 
chaining to the other SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

From the command line and from the API, you can have one or more of the 
aforementioned authentication methods set in the setting 
`chaining.socks5.methods` as a comma separated list. 

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods",
                "NO_AUTHENTICATION_REQUIRED,GSSAPI")
        ))).start();
    }
}
```

In the configuration file, you can have one or more of the aforementioned 
authentication methods set in the setting `chaining.socks5.methods` as a 
`<socks5.methods/>` XML element with one or more `<socks5.method/>` XML 
elements each specifying an authentication method.

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

If not set, the default value for the setting `chaining.socks5.methods` is 
set to `NO_AUTHENTICATION_REQUIRED`.

#### Chaining to the Other SOCKS Server Using No Authentication

Because the default value for the setting `chaining.socks5.methods` is set 
to `NO_AUTHENTICATION_REQUIRED`, it is not required for 
`NO_AUTHENTICATION_REQUIRED` to be included in the setting 
`chaining.socks5.methods`.

However, if other authentication methods are to be used in addition to 
`NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be 
included in the setting `chaining.socks5.methods`

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods",
                "NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD")
        ))).start();
    }
}
```

<a id="chaining-to-the-other-socks-server-using-username-password-authentication"></a>
#### Chaining to the Other SOCKS Server Using Username Password Authentication

To chain to the other SOCKS server using username password authentication, you 
will need to have the setting `chaining.socks5.methods` to have 
`USERNAME_PASSWORD` included. You will also need to have the settings 
`chaining.socks5.userpassauthmethod.username` and 
`chaining.socks5.userpassauthmethod.password` to specify the username and password 
for the other SOCKS5 server.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauthmethod.username=Aladdin \
    --setting=chaining.socks5.userpassauthmethod.password=opensesame
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.userpassauthmethod.username</name>
            <value>Aladdin</value>
        </setting>
        <setting>
            <name>chaining.socks5.userpassauthmethod.password</name>
            <value>opensesame</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassauthmethod.username",
                "Aladdin"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassauthmethod.password",
                "opensesame")
        ))).start();
    }
}
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-socks5-userpassauthmethod-pass` instead. It will 
provide an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauthmethod.username=Aladdin \
    --enter-chaining-socks5-userpassauthmethod-pass
```

Instead of using the settings `chaining.socks5.userpassauthmethod.username` and 
`chaining.socks5.userpassauthmethod.password` to specify the username and password 
for the other SOCKS5 server, you can supply the username and password for the 
other SOCKS5 server as a username and password pair in the user information 
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

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://Jasmine:mission%3Aimpossible@alpha-alpha.net:11111
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://Jasmine:mission%3Aimpossible@alpha-alpha.net:11111</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://Jasmine:mission%3Aimpossible@alpha-alpha.net:11111")
        ))).start();
    }
}
```

There is no need to have the setting `chaining.socks5.methods` to have
`USERNAME_PASSWORD` included since it will be automatically included during 
runtime.

**Warning**: This alternative leaves the password exposed in plaintext. It is 
recommended to use the approaches mentioned earlier since they hide the 
password either by encrypting it in the server configuration file created by 
`new-server-config-file` or by obtaining the password from the interactive 
prompt from the command line option 
`--enter-chaining-socks5-userpassauthmethod-pass`. 

#### Chaining to the Other SOCKS Server Using GSS-API Authentication

To chain to the other SOCKS server using GSS-API authentication, you will need 
to have the setting `chaining.socks5.methods` to have `GSSAPI` included. 
You will also need to specify Java system properties to use a security 
mechanism that implements the GSS-API (for example, Kerberos is a security 
mechanism that implements the GSS-API) and you will also need to specify the 
GSS-API service name for the other SOCKS5 server.

The following are sufficient examples of using the Kerberos security mechanism:

Command line example:

```bash
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapiauthmethod.serviceName=rcmd/alpha-alpha.net 
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
<!--
Before running this file, the following must be executed:
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
-->
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.gssapiauthmethod.serviceName</name>
            <value>rcmd/alpha-alpha.net</value>
        </setting>
    </settings>
</configuration>
```

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty(
            "java.security.auth.login.config", "login.conf");
        System.setProperty("java.security.krb5.conf", "krb5.conf");
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "GSSAPI"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.gssapiauthmethod.serviceName",
                "rcmd/alpha-alpha.net")
        ))).start();
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
    kdc_realm = ALPHA-ALPHA.NET
    default_realm = ALPHA-ALPHA.NET
    udp_preference_limit = 4096
    kdc_tcp_port = 12345
    kdc_udp_port = 12345

[realms]
    ALPHA-ALPHA.NET = {
        kdc = alpha-alpha.net:12345
    }
```

In `krb5.conf`, a KDC is defined as running at `alpha-alpha.net` on port 
`12345` with its realm as `ALPHA-ALPHA.NET`.

The setting `chaining.socks5.gssapiauthmethod.serviceName` with the value 
`rcmd/alpha-alpha.net` is the GSS-API service name (or the Kerberos service 
principal) for the other SOCKS server residing at `alpha-alpha.net`.

### Resolving Host Names From the Other SOCKS Server

Before discussing host name resolution from the other SOCKS server, a brief 
explanation of the server's internals:

The server uses endpoints to interact with the world remote from the client.

-   Under the CONNECT request, it uses a TCP socket to be connected to the 
remote server. In this documentation, this socket is called the remote TCP 
socket.
-   Under the BIND request, it uses a TCP server socket that waits for and 
accepts a remote TCP socket. In this documentation, this socket is called 
the remote-facing TCP server socket.
-   Under the UDP ASSOCIATE request, it uses a UDP socket that sends and 
receives UDP datagram packets to and from remote UDP peers. In this 
documentation, this UDP socket is called the remote-facing UDP socket.

The server also uses a host resolver to resolve host names for the 
aforementioned endpoints and for the 
[RESOLVE request](../reference/socks5-resolve-request.md).

When the server is chained to another SOCKS server, the aforementioned 
endpoints that the server uses become SOCKS-enabled, meaning that their 
traffic is routed through the other SOCKS server.

It is similar for the host resolver. When the server is chained to another 
SOCKS server, the host resolver that the server uses becomes SOCKS-enabled, 
meaning that it can use the other SOCKS server to resolve host names provided 
that the other SOCKS server supports handling the RESOLVE request. However, 
this functionality for the host resolver is disabled by default making the 
host resolver resolve host names through the local system.

Therefore, default host name resolution from the other SOCKS server is 
performed but has the following limitations:

Default host name resolution from the other SOCKS server OCCURS ONLY...

-   ...under the CONNECT request when the remote TCP socket makes an 
[extemporaneous connection](#extemporaneous-tcp-socket-connection-type). The 
host resolver is not used in resolving the target host name. When the remote 
TCP socket is SOCKS-enabled, the target host name is resolved by the other 
SOCKS server and not through the local system.

Default host name resolution from the other SOCKS server DOES NOT OCCUR...

-   ...under the CONNECT request when the remote TCP socket makes a 
[prepared connection](#prepared-tcp-socket-connection-type). The host resolver 
is used in resolving the target host name. Because of its default 
functionality, the host resolver resolves the target host name through the 
local system.
-   ...under the BIND request when resolving the binding host name for the 
remote-facing TCP server socket. The host resolver is used in resolving the 
binding host name for the remote-facing TCP server socket. Because of its 
default functionality, the host resolver resolves the binding host name for 
the remote-facing TCP server socket through the local system.
-   ...under the UDP ASSOCIATE request when resolving the host name for an 
outbound UDP datagram packet. The host resolver is used in resolving the host 
name for an outbound UDP datagram packet. Because of its default 
functionality, the host resolver resolves the host name for an outbound UDP 
datagram packet through the local system.
-   ...under the RESOLVE request when resolving the provided host name. The host 
resolver is used in resolving the provided host name. Because of its default 
functionality, the host resolver resolves the provided host name through the 
local system.

If you prefer to have host name resolution from the other SOCKS server without 
the aforementioned limitations, you would need to set the setting 
`chaining.socks.socksHostResolver.resolveFromSocksServer` to `true`. This 
setting enables the host resolver to resolve host names from the other SOCKS 
server. This setting can only be used if the other SOCKS server supports 
handling the RESOLVE request.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks.socksHostResolver.resolveFromSocksServer=true
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks.socksHostResolver.resolveFromSocksServer</name>
            <value>true</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks.socksHostResolver.resolveFromSocksServer", 
                "true")
        ))).start();
    }
}
```

## Chaining to a Specified Chain of Other SOCKS Servers

You can have the server chained to a specified chain of other SOCKS servers, 
meaning that its traffic can be routed through the specified chain of the other 
SOCKS servers. To have the server chained to a specified chain of other SOCKS 
servers, you will need to have the setting `chaining.socksServerUri` 
specified multiple times with each setting specifying a SOCKS server as a URI.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331")
        ))).start();
    }
}
```

The server uses endpoints to interact with the world remote from the client.
When the server is chained to a specified chain of other SOCKS servers, those
endpoints that the server uses become SOCKS-enabled, meaning that their 
traffic is routed through the specified chain of other SOCKS servers. In each 
of those endpoints is an underlying TCP socket to the last other SOCKS server 
in the chain. Within each underlying TCP socket to the last other SOCKS server
in the chain is another underlying TCP socket to the second-to-last other 
SOCKS server in the chain and so on. All of these underlying TCP sockets are 
called the other SOCKS server TCP sockets.

For configuring any of the other SOCKS server TCP sockets, it is recommended 
to configure only the last other SOCKS server TCP socket in the chain.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256 \
    --setting=socks.onConnectRequest.remoteSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256 \
    --setting=socks.onBindRequest.remoteFacingServerSocket.socketSettings=SO_RCVBUF=256 \
    --setting=socks.onBindRequest.remoteSocket.socketSettings=SO_RCVBUF=256,SO_SNDBUF=256
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onBindRequest.remoteFacingServerSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>socks.onBindRequest.remoteSocket.socketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_RCVBUF</name>
                    <value>256</value>
                </socketSetting>
                <socketSetting>
                    <name>SO_SNDBUF</name>
                    <value>256</value>
                </socketSetting>
            </socketSettings>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.remoteFacingServerSocket.socketSettings", "SO_RCVBUF=256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.remoteSocket.socketSettings", "SO_RCVBUF=256,SO_SNDBUF=256")
        ))).start();
    }
}
```

To specify the settings regarding SSL/TLS/DTLS and authentication for a SOCKS 
server in the chain, the settings will need to be placed after that specified 
SOCKS server.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapiauthmethod.serviceName=rcmd/alpha-alpha.net \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauthmethod.username=Aladdin \
    --setting=chaining.socks5.userpassauthmethod.password=opensesame \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.gssapiauthmethod.serviceName</name>
            <value>rcmd/alpha-alpha.net</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.userpassauthmethod.username</name>
            <value>Aladdin</value>
        </setting>
        <setting>
            <name>chaining.socks5.userpassauthmethod.password</name>
            <value>opensesame</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "GSSAPI"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.gssapiauthmethod.serviceName",
                "rcmd/alpha-alpha.net"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassauthmethod.username",
                "Aladdin"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassauthmethod.password",
                "opensesame"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331")
        ))).start();
    }
}
```

How the host names of the other SOCKS servers in a chain of other SOCKS 
servers are resolved depends on the 
[type of connection](#tcp-socket-connection-types) 
the other SOCKS server TCP sockets make. If they make an 
[extemporaneous connection](#extemporaneous-tcp-socket-connection-type), the 
specified host name for each SOCKS server except the first is resolved by the 
previous SOCKS server (the specified host name of the first is resolved by the 
local system). If they make a 
[prepared connection](#prepared-tcp-socket-connection-type) (`prepareSocket` 
set to `true`), the specified host name for each SOCKS server is resolved by 
the local system.

If you would like to have the other SOCKS server TCP sockets make a prepared 
connection without the aforementioned limitations, you will need to have the 
setting `chaining.socks.socksHostResolver.resolveFromSocksServer` set to 
`true` for each server. Again, this setting can only be used if each SOCKS 
server supports handling the 
[RESOLVE request](../reference/socks5-resolve-request.md).

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socks.socksHostResolver.resolveFromSocksServer=true \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socks.socksHostResolver.resolveFromSocksServer=true \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socks.socksHostResolver.resolveFromSocksServer=true \
    --setting=chaining.socksServerSocket.prepareSocket=true \
    --setting=chaining.socksServerSocket.connectTimeout=120000 \
    --setting=socks.onConnectRequest.remoteSocket.prepareSocket=true \
    --setting=socks.onConnectRequest.remoteSocket.connectTimeout=120000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socks.socksHostResolver.resolveFromSocksServer</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socks.socksHostResolver.resolveFromSocksServer</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socks.socksHostResolver.resolveFromSocksServer</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.prepareSocket</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.socksServerSocket.connectTimeout</name>
            <value>120000</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.prepareSocket</name>
            <value>true</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.remoteSocket.connectTimeout</name>
            <value>120000</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks.socksHostResolver.resolveFromSocksServer",
                "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks.socksHostResolver.resolveFromSocksServer",
                "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks.socksHostResolver.resolveFromSocksServer",
                "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.prepareSocket", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerSocket.connectTimeout", "120000"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.prepareSocket", "true"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.remoteSocket.connectTimeout", "120000")
        ))).start();
    }
}
```

The known limitations of chaining to a specified chain of other SOCKS servers
include the following:

-   Only TCP traffic can be routed through the chain.

## Chaining to Specified Chains of Other SOCKS Servers

You can have the server chained to specified chains of other SOCKS servers, 
meaning that its traffic can be routed through one of the specified chains of 
other SOCKS servers at a time. To have the server chained to multiple 
specified chains of other SOCKS servers, you will need to have a route ID 
assigned at the end of each 
[chain](#chaining-to-a-specified-chain-of-other-socks-servers) by using the 
setting `chaining.routeId`

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333 \
    --setting=chaining.routeId=gamma
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>gamma</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "gamma")
        ))).start();
    }
}
```

From the aforementioned examples: 

-   The chain consisting of `socks5://alpha-alpha.net:11111`, 
`socks5://alpha-beta.net:11112`, and `socks5://alpha-gamma.net:11113` is assigned 
the route ID of `alpha`
-   The chain consisting of `socks5://beta-alpha.net:22221`, 
`socks5://beta-beta.net:22222`, and `socks5://beta-gamma.net:22223` is assigned 
the route ID of `beta`
-   The chain consisting of `socks5://gamma-alpha.net:33331`, 
`socks5://gamma-beta.net:33332`, and `socks5://gamma-gamma.net:33333` is assigned 
the route ID of `gamma`

There is another route that is assigned a route ID. That route is the direct 
route. The direct route uses no chain to route the traffic through. It is 
assigned by default a route ID of `lastRoute`.

To omit the direct route from being included, have the last chain not assigned a 
route ID from the setting `chaining.routeId`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333")
        ))).start();
    }
}
```

From the aforementioned examples: 

-   The chain consisting of `socks5://alpha-alpha.net:11111`, 
`socks5://alpha-beta.net:11112`, and `socks5://alpha-gamma.net:11113` is assigned 
the route ID of `alpha`
-   The chain consisting of `socks5://beta-alpha.net:22221`, 
`socks5://beta-beta.net:22222`, and `socks5://beta-gamma.net:22223` is assigned 
the route ID of `beta`
-   The chain consisting of `socks5://gamma-alpha.net:33331`, 
`socks5://gamma-beta.net:33332`, and `socks5://gamma-gamma.net:33333` is assigned 
by default the route ID of `lastRoute`

To change the route ID assigned to the last route, you can set the setting 
`lastRouteId` to the route ID you want assigned to the last route.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333 \
    --setting=lastRouteId=omega
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
        <setting>
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega")
        ))).start();
    }
}
```

You can also set the setting `routeSelectionStrategy` to specify the 
selection strategy for the next route. The default is `CYCLICAL`.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333 \
    --setting=lastRouteId=omega \
    --setting=routeSelectionStrategy=RANDOM
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
        <setting>
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>routeSelectionStrategy</name>
            <value>RANDOM</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionStrategy", "RANDOM")
        ))).start();
    }
}
```

You can also set the setting `routeSelectionLogAction` to specify the 
logging action to take if a route is selected.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333 \
    --setting=lastRouteId=omega \
    --setting=routeSelectionStrategy=RANDOM \
    --setting=routeSelectionLogAction=LOG_AS_INFO
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
        <setting>
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>routeSelectionStrategy</name>
            <value>RANDOM</value>
        </setting>
        <setting>
            <name>routeSelectionLogAction</name>
            <value>LOG_AS_INFO</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionStrategy", "RANDOM"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionLogAction", "LOG_AS_INFO")
        ))).start();
    }
}
```

## Configuring Relay Settings

You can configure settings for the relaying of data between clients and their 
remote targets/peers. You can configure the relay settings for all SOCKS 
requests or for each SOCKS request.

You can configure the relay settings for all SOCKS requests using the 
following settings:

-   `socks.onRequest.relayBufferSize`: The buffer size in bytes for relaying 
    the data (Value must be an integer between 1 (inclusive) and 2147483647 
    (inclusive)) (Default value is `1024`)
-   `socks.onRequest.relayIdleTimeout`: The timeout in milliseconds on 
    relaying no data (Value must be an integer between 1 (inclusive) and 
    2147483647 (inclusive)) (Default value is `60000`)

Command line example:

```bash
jargyle start-server \
    --setting=socks.onRequest.relayBufferSize=256 \
    --setting=socks.onRequest.relayIdleTimeout=500
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onRequest.relayBufferSize</name>
            <value>256</value>
        </setting>
        <setting>
            <name>socks.onRequest.relayIdleTimeout</name>
            <value>500</value>
        </setting>        
    </settings>
</configuration>
```

API example:

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
                "socks.onRequest.relayBufferSize", "256"),
            Setting.newInstanceWithParsedValue(
                "socks.onRequest.relayIdleTimeout", "500")
        ))).start();
    }
}
```

You can configure the relay settings for each SOCKS request using the 
following settings:

-   `socks.onBindRequest.relayBufferSize`: The buffer size in bytes for 
    relaying the data (Value must be an integer between 1 (inclusive) and 
    2147483647 (inclusive))
-   `socks.onBindRequest.relayIdleTimeout`: The timeout in milliseconds on
    relaying no data (Value must be an integer between 1 (inclusive) and
    2147483647 (inclusive))
-   `socks.onConnectRequest.relayBufferSize`: The buffer size in bytes for 
    relaying the data (Value must be an integer between 1 (inclusive) and 
    2147483647 (inclusive))
-   `socks.onConnectRequest.relayIdleTimeout`: The timeout in milliseconds on
    relaying no data (Value must be an integer between 1 (inclusive) and
    2147483647 (inclusive))
-   `socks.onUdpAssociateRequest.relayBufferSize`: The buffer size in bytes 
    for relaying the data (Value must be an integer between 1 (inclusive) and 
    2147483647 (inclusive))
-   `socks.onUdpAssociateRequest.relayIdleTimeout`: The timeout in 
    milliseconds on relaying no data (Value must be an integer between 1 
    (inclusive) and 2147483647 (inclusive))

If no value is specified for a setting, depending on the setting, the value
is obtained from either of the settings
`socks.onRequest.relayBufferSize` or `socks.onRequest.relayIdleTimeout`.

Command line example:

```bash
jargyle start-server \
    --setting=socks.onBindRequest.relayBufferSize=256 \
    --setting=socks.onBindRequest.relayIdleTimeout=500 \
    --setting=socks.onConnectRequest.relayBufferSize=256 \
    --setting=socks.onConnectRequest.relayIdleTimeout=500 \
    --setting=socks.onUdpAssociateRequest.relayBufferSize=512 \
    --setting=socks.onUdpAssociateRequest.relayIdleTimeout=1000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onBindRequest.relayBufferSize</name>
            <value>256</value>
        </setting>
        <setting>
            <name>socks.onBindRequest.relayIdleTimeout</name>
            <value>500</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.relayBufferSize</name>
            <value>256</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.relayIdleTimeout</name>
            <value>500</value>
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.relayBufferSize</name>
            <value>512</value>
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.relayIdleTimeout</name>
            <value>1000</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socks.onBindRequest.relayBufferSize", "256"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.relayIdleTimeout", "500"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.relayBufferSize", "256"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.relayIdleTimeout", "500"),
            Setting.newInstanceWithParsedValue(
                "socks.onUdpAssociateRequest.relayBufferSize", "512"),
            Setting.newInstanceWithParsedValue(
                "socks.onUdpAssociateRequest.relayIdleTimeout", "1000")
        ))).start();
    }
}
```

## Limiting Relay Bandwidth

You can limit the bandwidth of the relaying of data between clients and their 
remote targets/peers. You can limit the relay bandwidth for all SOCKS requests 
or for each SOCKS request.

You can limit the relay bandwidth for all SOCKS requests using the following 
settings:

-   `socks.onRequest.relayInboundBandwidthLimit`: Specifies the upper limit on 
    bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks.onRequest.relayOutboundBandwidthLimit`: Specifies the upper limit 
    on bandwidth in bytes per second of receiving outbound data to be relayed

The value given to any of the settings must be an integer between 1
(inclusive) and 2147483647 (inclusive)

Command line example:

```bash
jargyle start-server \
    --setting=socks.onRequest.relayInboundBandwidthLimit=1024000 \
    --setting=socks.onRequest.relayOutboundBandwidthLimit=1024000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onRequest.relayInboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>
        <setting>
            <name>socks.onRequest.relayOutboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>        
    </settings>
</configuration>
```

API example:

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
                "socks.onRequest.relayInboundBandwidthLimit", "1024000"),
            Setting.newInstanceWithParsedValue(
                "socks.onRequest.relayOutboundBandwidthLimit", "1024000")
        ))).start();
    }
}
```

You can limit the relay bandwidth for each SOCKS request using the following
settings:

-   `socks.onBindRequest.relayInboundBandwidthLimit`: Specifies the upper
    limit on bandwidth in bytes per second of receiving inbound data to be 
    relayed
-   `socks.onBindRequest.relayOutboundBandwidthLimit`: Specifies the upper
    limit on bandwidth in bytes per second of receiving outbound data to be 
    relayed
-   `socks.onConnectRequest.relayInboundBandwidthLimit`: Specifies the upper
    limit on bandwidth in bytes per second of receiving inbound data to be 
    relayed
-   `socks.onConnectRequest.relayOutboundBandwidthLimit`: Specifies the upper
    limit on bandwidth in bytes per second of receiving outbound data to be 
    relayed
-   `socks.onUdpAssociateRequest.relayInboundBandwidthLimit`: Specifies the
    upper limit on bandwidth in bytes per second of receiving inbound data to 
    be relayed
-   `socks.onUdpAssociateRequest.relayOutboundBandwidthLimit`: Specifies the
    upper limit on bandwidth in bytes per second of receiving outbound data to 
    be relayed

Again, the value given to any of the settings must be an integer between 1
(inclusive) and 2147483647 (inclusive)

If no value is specified for a setting, depending on the setting, the value
is obtained from either of the settings
`socks.onRequest.relayInboundBandwidthLimit` or 
`socks.onRequest.relayOutboundBandwidthLimit`.

Command line example:

```bash
jargyle start-server \
    --setting=socks.onBindRequest.relayInboundBandwidthLimit=1024000 \
    --setting=socks.onBindRequest.relayOutboundBandwidthLimit=1024000 \
    --setting=socks.onConnectRequest.relayInboundBandwidthLimit=1024000 \
    --setting=socks.onConnectRequest.relayOutboundBandwidthLimit=1024000 \
    --setting=socks.onUdpAssociateRequest.relayInboundBandwidthLimit=2048000 \
    --setting=socks.onUdpAssociateRequest.relayOutboundBandwidthLimit=2048000
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks.onBindRequest.relayInboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>
        <setting>
            <name>socks.onBindRequest.relayOutboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.relayInboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>
        <setting>
            <name>socks.onConnectRequest.relayOutboundBandwidthLimit</name>
            <value>1024000</value>
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.relayInboundBandwidthLimit</name>
            <value>2048000</value>
        </setting>
        <setting>
            <name>socks.onUdpAssociateRequest.relayOutboundBandwidthLimit</name>
            <value>2048000</value>
        </setting>
    </settings>
</configuration>
```

API example:

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
                "socks.onBindRequest.relayInboundBandwidthLimit", "1024000"),
            Setting.newInstanceWithParsedValue(
                "socks.onBindRequest.relayOutboundBandwidthLimit", "1024000"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.relayInboundBandwidthLimit", "1024000"),
            Setting.newInstanceWithParsedValue(
                "socks.onConnectRequest.relayOutboundBandwidthLimit", "1024000"),
            Setting.newInstanceWithParsedValue(
                "socks.onUdpAssociateRequest.relayInboundBandwidthLimit", "2048000"),
            Setting.newInstanceWithParsedValue(
                "socks.onUdpAssociateRequest.relayOutboundBandwidthLimit", "2048000")
        ))).start();
    }
}
```

## Using Rules to Manage Traffic

A rule consists of the following:

-   Rule conditions: fields that altogether evaluates as true if they match a 
specific instance of traffic
-   Rule actions: fields that are applied if the aforementioned rule conditions 
evaluate as true for matching a specific instance of traffic

From the command line and from the API, a rule consists of a comma separated 
list of both rule conditions and rule actions. 

In the server configuration file, a rule is expressed as a `<rule/>` XML 
element with a `<ruleConditions/>` XML element and a `<ruleActions/>` XML 
element. 

See [Rule Conditions](#rule-conditions) and [Rule Actions](#rule-actions) for 
more information.

To specify a rule, you would need to have the setting `rule` specify the rule. 

Command line example:

```bash
# Allow all forms of traffic.
jargyle start-server --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow all forms of traffic.</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow all forms of traffic.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

To specify multiple rules, you would need to have the setting `rule` specified 
multiple times with each setting specifying another rule.

Command line example:

```bash
# Allow the CONNECT request for any server on port 80 or 443.
# Deny the CONNECT request for any server on any other port.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.command=CONNECT,socks.request.desiredDestinationPort=80,socks.request.desiredDestinationPort=443,firewallAction=ALLOW \
    --setting=rule=socks.request.command=CONNECT,firewallAction=DENY \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks.request.desiredDestinationPort</name>
                        <value>80</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks.request.desiredDestinationPort</name>
                        <value>443</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow the CONNECT request for any server on port 80 or 443.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>DENY</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Deny the CONNECT request for any server on any other port.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow the CONNECT request for any server on port 80 
             * or 443.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.command=CONNECT,"
                + "socks.request.desiredDestinationPort=80,"
                + "socks.request.desiredDestinationPort=443,"
                + "firewallAction=ALLOW"),
            /*
             * Deny the CONNECT request for any server on any 
             * other port.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.command=CONNECT,firewallAction=DENY"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

When a specific instance of traffic is matched by the first rule, that rule is 
applied and the rest of the rules are ignored. Therefore, it is best to have 
more specific rules specified first and have less specific rules specified 
last.

### Rule Conditions

From the command line and from the API, rule conditions consist of a comma 
separated list of rule conditions. Each rule condition consists of the syntax 
of `NAME=VALUE` where `NAME` is expressed as the name of the rule condition 
and `VALUE` is expressed as the value assigned to the rule condition. 

Partial command line and API example:

```text
clientAddress=127.0.0.1,clientAddress=0:0:0:0:0:0:0:1
```

In the server configuration file, rule conditions are expressed in a 
`<ruleConditions/>` XML element with zero to many `<ruleCondition/>` XML 
elements. Each `<ruleCondition/>` XML element contains a `<name/>` XML 
element for the name of the rule condition and the `<value/>` XML element for 
the value assigned to the rule condition.

Partial server configuration file example:

```xml
<ruleConditions>
    <ruleCondition>
        <name>clientAddress</name>
        <value>127.0.0.1</value>
    </ruleCondition>
    <ruleCondition>
        <name>clientAddress</name>
        <value>0:0:0:0:0:0:0:1</value>
    </ruleCondition>
</ruleConditions>
```

All rule conditions together are evaluated as true if each group of one or 
more rule conditions with the same name is evaluated as true. Within a group 
of one or more rule conditions with the same name, all rule conditions 
together are evaluated as true if at least one of the rule conditions within 
that group is evaluated as true. Zero rule conditions is evaluated as true.

For instance, the following examples are evaluated as true if the client address 
is the IPv4 loopback address or the IPv6 loopback address:

Partial command line and API example:

```text
clientAddress=127.0.0.1,clientAddress=0:0:0:0:0:0:0:1
```

Partial server configuration file example:

```xml
<ruleConditions>
    <ruleCondition>
        <name>clientAddress</name>
        <value>127.0.0.1</value>
    </ruleCondition>
    <ruleCondition>
        <name>clientAddress</name>
        <value>0:0:0:0:0:0:0:1</value>
    </ruleCondition>
</ruleConditions>
```

The next following examples are evaluated as true if username password 
authentication was used and the user is either 'guest' or 'specialuser':

Partial command line and API example:

```text
socks.method=USERNAME_PASSWORD,socks.user=guest,socks.user=specialuser
```

Partial server configuration file example:

```xml
<ruleConditions>
    <ruleCondition>
        <name>socks.method</name>
        <value>USERNAME_PASSWORD</value>
    </ruleCondition>
    <ruleCondition>
        <name>socks.user</name>
        <value>guest</value>
    </ruleCondition>
    <ruleCondition>
        <name>socks.user</name>
        <value>specialuser</value>
    </ruleCondition>    
</ruleConditions>
```

The next following examples are evaluated as true because there are no rule 
conditions given:

Partial command line and API example:

```text

```

Partial server configuration file example:

```xml
<ruleConditions/>
```

A complete listing of rule conditions can be found 
[here](../reference/rule-conditions.md).

### Rule Actions

From the command line and from the API, rule actions consist of a comma 
separated list of rule actions. Each rule action consists of the syntax of 
`NAME=VALUE` where `NAME` is expressed as the name of the rule action and 
`VALUE` is expressed as the value assigned to the rule action. 

Partial command line and API example:

```text
firewallAction=ALLOW,firewallActionLogAction=LOG_AS_INFO
```

In the server configuration file, rule actions are expressed in a 
`<ruleActions/>` XML element with zero to many `<ruleAction/>` XML elements. 
Each `<ruleAction/>` XML element contains a `<name/>` XML element for the name 
of the rule action and the `<value/>` XML element for the value assigned to 
the rule action.

Partial server configuration file example:

```xml
<ruleActions>
    <ruleAction>
        <name>firewallAction</name>
        <value>ALLOW</value>
    </ruleAction>
    <ruleAction>
        <name>firewallActionLogAction</name>
        <value>LOG_AS_INFO</value>
    </ruleAction>
</ruleActions>
```

Unless otherwise stated, if a rule action of the same name appears more than 
once in the comma separated list or in the `<ruleActions/>` XML element, then 
only the last rule action of the same name is recognized.

Some rule actions have higher precedence over other rule actions. The more 
specific the rule action the more precedence it has over less specific rule 
actions. For example, the rule action `clientEnpoints.bindUdpPortRange` has 
higher precedence over the rule action `bindUdpPortRange`. The rule action 
`clientEnpoints.bindUdpPortRange` is more specific about a range of port 
numbers to choose from for client UDP endpoints. The rule action 
`bindUdpPortRange` is comparatively less specific since it specifies a range 
of port numbers to choose from for all UDP endpoints.

A complete listing of rule actions can be found 
[here](../reference/rule-actions.md).

### Allowing or Denying Traffic

To allow or deny a specific instance of traffic, you will need the following 
rule action:

-   `firewallAction`

The value given to the rule action must be either of the following values:

-   `ALLOW`
-   `DENY`

This rule action can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method`
-   `socks.reply.serverBoundAddress`
-   `socks.reply.serverBoundPort`
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.secondReply.serverBoundAddress`
-   `socks.secondReply.serverBoundPort`
-   `socks.udp.inbound.desiredDestinationAddress`
-   `socks.udp.inbound.desiredDestinationPort`
-   `socks.udp.inbound.sourceAddress`
-   `socks.udp.inbound.sourcePort`
-   `socks.udp.outbound.desiredDestinationAddress`
-   `socks.udp.outbound.desiredDestinationPort`
-   `socks.udp.outbound.sourceAddress`
-   `socks.udp.outbound.sourcePort`
-   `socks.user`
-   `socksServerAddress`

**Note**: for any other rule actions to be applied, the rule action 
`firewallAction` with the value of `ALLOW` must be present.

You can also specify the logging action to take if the rule action 
`firewallAction` is applied by adding the following rule action:

-   `firewallActionLogAction`

The rule action `firewallActionLogAction` is optional.

Command line example:

```bash
# Deny any BIND or UDP ASSOCIATE requests and log as a warning message each 
# denial.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.command=BIND,socks.request.command=UDP_ASSOCIATE,firewallAction=DENY,firewallActionLogAction=LOG_AS_WARNING \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>BIND</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>UDP_ASSOCIATE</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>DENY</value>
                    </ruleAction>
                    <ruleAction>
                        <name>firewallActionLogAction</name>
                        <value>LOG_AS_WARNING</value>
                    </ruleAction>                
                </ruleActions>
            </rule>
            <doc>Deny any BIND or UDP ASSOCIATE requests and log as a warning message each denial.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
            /*
             * Deny any BIND or UDP ASSOCIATE requests and log as a warning 
             * message each denial.
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                 "socks.request.command=BIND,"
                 + "socks.request.command=UDP_ASSOCIATE,"
                 + "firewallAction=DENY,"
                 + "firewallActionLogAction=LOG_AS_WARNING"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

### Allowing a Limited Number of Simultaneous Connections

To allow a limited number of simultaneous connections, you will need the 
following rule action:

-   `firewallActionAllowLimit`

The value given to the rule action must be an integer between 0 (inclusive) 
and 2147483647 (inclusive)

This rule action can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method`
-   `socks.reply.serverBoundAddress`
-   `socks.reply.serverBoundPort`
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.secondReply.serverBoundAddress`
-   `socks.secondReply.serverBoundPort`
-   `socks.user`
-   `socksServerAddress`

You can also specify the logging action to take if the limit on the number of 
simultaneous connections has been reached by adding the following rule action:

-   `firewallActionAllowLimitReachedLogAction`

The rule action `firewallActionAllowLimitReachedLogAction` is optional.

Command line example:

```bash
# Allow the user 'guest' from username password authentication 50 simultaneous 
# connections and log as an informational message when the limit has been 
# reached.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.method=USERNAME_PASSWORD,socks.user=guest,firewallAction=ALLOW,firewallActionAllowLimit=50,firewallActionAllowLimitReachedLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.method</name>
                        <value>USERNAME_PASSWORD</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks.user</name>
                        <value>guest</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>firewallActionAllowLimit</name>
                        <value>50</value>
                    </ruleAction>                
                    <ruleAction>
                        <name>firewallActionAllowLimitReachedLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow the user 'guest' from username password authentication 50 simultaneous connections and log as an informational message when the limit has been reached.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow the user 'guest' from username password 
             * authentication 50 simultaneous connections and log 
             * as an informational message when the limit has been 
             * reached.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.method=USERNAME_PASSWORD,"
                + "socks.user=guest,"
                + "firewallAction=ALLOW,"
                + "firewallActionAllowLimit=50,"
                + "firewallActionAllowLimitReachedLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

<a id="routing-traffic-through-a-selection-of-specified-chains-of-socks-servers"></a>
### Routing Traffic Through a Selection of Specified Chains of SOCKS Servers

To route traffic through a selection of specified chains of SOCKS servers, you 
can use the following rule actions:

-   `routeSelectionStrategy`: Specifies the selection strategy for the next 
route (This rule action is optional. If this rule action is not specified, the 
setting `routeSelectionStrategy` is used.)
-   `selectableRouteId`: Specifies the ID for a selectable 
[route](#chaining-to-specified-chains-of-other-socks-servers) (This rule 
action is optional. This rule action can be specified multiple times with each 
rule action specifying another ID for a selectable route. If this rule action 
is not specified, all the routes defined by the settings `chaining.routeId` 
and `lastRouteId` are selectable.)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method` 
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.user`
-   `socksServerAddress`

You can also specify the logging action to take if a route is selected by adding 
the following rule action:

-   `routeSelectionLogAction`

The rule action `routeSelectionLogAction` is optional. If the rule action is 
not specified, the setting `routeSelectionLogAction` is used.

Command line example:

```bash
# Allow the CONNECT request, randomly select either route 'alpha' or 'beta' 
# for the traffic to be routed through, and log as an informational message 
# the route that has been selected.
# Allow anything else and select route 'omega' for the traffic to be routed 
# through.
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://alpha-alpha.net:11111 \
    --setting=chaining.socksServerUri=socks5://alpha-beta.net:11112 \
    --setting=chaining.socksServerUri=socks5://alpha-gamma.net:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://beta-alpha.net:22221 \
    --setting=chaining.socksServerUri=socks5://beta-beta.net:22222 \
    --setting=chaining.socksServerUri=socks5://beta-gamma.net:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://gamma-alpha.net:33331 \
    --setting=chaining.socksServerUri=socks5://gamma-beta.net:33332 \
    --setting=chaining.socksServerUri=socks5://gamma-gamma.net:33333 \
    --setting=lastRouteId=omega \
    --setting=rule=socks.request.command=CONNECT,firewallAction=ALLOW,routeSelectionStrategy=RANDOM,selectableRouteId=alpha,selectableRouteId=beta,routeSelectionLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW,selectableRouteId=omega
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-alpha.net:11111</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-beta.net:11112</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://alpha-gamma.net:11113</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>alpha</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-alpha.net:22221</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-beta.net:22222</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://beta-gamma.net:22223</value>
        </setting>
        <setting>
            <name>chaining.routeId</name>
            <value>beta</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-alpha.net:33331</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-beta.net:33332</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://gamma-gamma.net:33333</value>
        </setting>
        <setting>
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>routeSelectionStrategy</name>
                        <value>RANDOM</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>alpha</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>beta</value>
                    </ruleAction>
                    <ruleAction>
                        <name>routeSelectionLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow the CONNECT request, randomly select either route 'alpha' or 'beta' for the traffic to be routed through, and log as an informational message the route that has been selected.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>omega</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else and select route 'omega' for the traffic to be routed through.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
                "chaining.socksServerUri", 
                "socks5://alpha-alpha.net:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-beta.net:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://alpha-gamma.net:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-alpha.net:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-beta.net:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://beta-gamma.net:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-alpha.net:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-beta.net:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://gamma-gamma.net:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            /*
             * Allow the CONNECT request, randomly select either route 'alpha' 
             * or 'beta' for the traffic to be routed through, and log as an 
             * informational message the route that has been selected.
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                "socks.request.command=CONNECT,"
                + "firewallAction=ALLOW,"
                + "routeSelectionStrategy=RANDOM,"
                + "selectableRouteId=alpha,"
                + "selectableRouteId=beta,"
                + "routeSelectionLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else and select route 'omega' for the traffic 
             * to be routed through.
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                "firewallAction=ALLOW,"
                + "selectableRouteId=omega")
        ))).start();
    }
}
```

### Redirecting the Desired Destination

To redirect the desired destination, you will need either or both of the 
following rule actions:

-   `socks.request.desiredDestinationAddressRedirect`: Specifies the desired 
destination address redirect
-   `socks.request.desiredDestinationPortRedirect`: Specifies the desired 
destination port redirect

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method` 
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.user`
-   `socksServerAddress`

You can also specify the logging action to take if the desired destination is 
redirected by adding the following rule action:

-   `socks.request.desiredDestinationRedirectLogAction`

The rule action `socks.request.desiredDestinationRedirectLogAction` is optional.

Command line example:

```bash
# Redirect desired destination 'discontinued-server.net' to 'new-server.net' 
# and log as an informational message the redirection.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.desiredDestinationAddress=discontinued-server.net,firewallAction=ALLOW,socks.request.desiredDestinationAddressRedirect=new-server.net,socks.request.desiredDestinationRedirectLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.desiredDestinationAddress</name>
                        <value>discontinued-server.net</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks.request.desiredDestinationAddressRedirect</name>
                        <value>new-server.net</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks.request.desiredDestinationRedirectLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Redirect desired destination 'discontinued-server.net' to 'new-server.net' and log as an informational message the redirection.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
            /*
             * Redirect desired destination 'discontinued-server.net' 
             * to 'new-server.net' and log as an informational 
             * message the redirection.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.desiredDestinationAddress=discontinued-server.net,"
                + "firewallAction=ALLOW,"
                + "socks.request.desiredDestinationAddressRedirect=new-server.net,"
                + "socks.request.desiredDestinationRedirectLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

### Configuring Endpoints Through Rules

To configure the endpoints through rules, you will need any of the following 
rule actions:

-   `bindHost`: Specifies the binding host name or address for all endpoints
-   `bindHostAddressType`: Specifies an acceptable binding host address type
    for all endpoints (This rule action can be specified multiple times with 
    each rule action specifying another host address type)
-   `bindTcpPortRange`: Specifies a binding port range for all TCP endpoints
    (This rule action can be specified multiple times with each rule action
    specifying another port range)
-   `bindUdpPortRange`: Specifies a binding port range for all UDP endpoints
    (This rule action can be specified multiple times with each rule action
    specifying another port range)
-   `clientEndpoints.bindHost`: Specifies the binding host name or address for
    all client endpoints
-   `clientEndpoints.bindHostAddressType`: Specifies an acceptable binding 
    host address type for all client endpoints (This rule action can be
    specified multiple times with each rule action specifying another host 
    address type)
-   `clientEndpoints.bindUdpPortRange`: Specifies a binding port range for all 
    client UDP endpoints (This rule action can be specified multiple times 
    with each rule action specifying another port range)
-   `clientEndpoints.netInterface`: Specifies the network interface that
    provides a binding host address for all client endpoints
-   `clientEndpoints.socketSetting`: Specifies a socket setting for all
    client endpoints (This rule action can be specified multiple times with
    each rule action specifying another socket setting)
-   `clientSocket.socketSetting`: Specifies a socket setting for the client 
    TCP socket (This rule action can be specified multiple times with each 
    rule action specifying another socket setting)
-   `netInterface`: Specifies the network interface that provides a binding
    host address for all endpoints
-   `remoteEndpoints.bindHost`: Specifies the binding host name or address
    for all remote endpoints
-   `remoteEndpoints.bindHostAddressType`: Specifies an acceptable binding
    host address type for all remote endpoints (This rule action can be
    specified multiple times with each rule action specifying another host 
    address type)
-   `remoteEndpoints.bindTcpPortRange`: Specifies a binding port range for all 
    remote TCP endpoints (This rule action can be specified multiple times 
    with each rule action specifying another port range)
-   `remoteEndpoints.bindUdpPortRange`: Specifies a binding port range for all 
    remote UDP endpoints (This rule action can be specified multiple times 
    with each rule action specifying another port range)
-   `remoteEndpoints.netInterface`: Specifies the network interface that
    provides a binding host address for all remote endpoints
-   `remoteEndpoints.socketSetting`: Specifies a socket setting for all
    remote endpoints (This rule action can be specified multiple times with
    each rule action specifying another socket setting)
-   `socketSetting`: Specifies a socket setting for all endpoints (This rule
    action can be specified multiple times with each rule action specifying 
    another socket setting)
-   `socks.onBindRequest.remoteFacingServerSocket.bindHost`: Specifies the 
    binding host name or address for the remote-facing TCP server socket if 
    the provided host address is all zeros
-   `socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType`: 
    Specifies an acceptable binding host address type for the remote-facing 
    TCP server socket (This rule action can be specified multiple times with 
    each rule action specifying another host address type)
-   `socks.onBindRequest.remoteFacingServerSocket.bindPortRange`: Specifies a 
    binding port range for the remote-facing TCP server socket if the provided 
    port is zero (This rule action can be specified multiple times with each 
    rule action specifying another port range)
-   `socks.onBindRequest.remoteFacingServerSocket.netInterface`: Specifies the 
    network interface that provides a binding host address for the 
    remote-facing TCP server socket
-   `socks.onBindRequest.remoteFacingServerSocket.socketSetting`: Specifies a 
    socket setting for the remote-facing TCP server socket (This rule action 
    can be specified multiple times with each rule action specifying another 
    socket setting)
-   `socks.onBindRequest.remoteSocket.socketSetting`: Specifies a socket 
    setting for the remote TCP socket (This rule action can be specified 
    multiple times with each rule action specifying another socket setting)
-   `socks.onConnectRequest.remoteSocket.bindHost`: Specifies the binding host
    name or address for the remote TCP socket
-   `socks.onConnectRequest.remoteSocket.bindHostAddressType`: Specifies an
    acceptable binding host address type for the remote TCP socket (This rule 
    action can be specified multiple times with each rule action specifying 
    another host address type)
-   `socks.onConnectRequest.remoteSocket.bindPortRange`: Specifies a binding
    port range for the remote TCP socket (This rule action can be specified 
    multiple times with each rule action specifying another port range)
-   `socks.onConnectRequest.remoteSocket.connectTimeout`: Specifies the timeout
    in milliseconds on waiting for the remote TCP socket to connect (Value must 
    be an integer between 1 (inclusive) and 2147483647 (inclusive))
-   `socks.onConnectRequest.remoteSocket.netInterface`: Specifies the network
    interface that provides a binding host address for the remote TCP socket
-   `socks.onConnectRequest.remoteSocket.prepareSocket`: Specifies the boolean
    value to indicate if the remote TCP socket is to be prepared before 
    connecting (involves applying the specified socket settings, resolving the 
    target host name, and setting the specified timeout on waiting to connect)
-   `socks.onConnectRequest.remoteSocket.socketSetting`: Specifies a socket
    setting for the remote TCP socket (This rule action can be specified 
    multiple times with each rule action specifying another socket setting)
-   `socks.onRequest.clientEndpoints.bindHost`: Specifies the binding host 
    name or address for all client endpoints
-   `socks.onRequest.clientEndpoints.bindHostAddressType`: Specifies an
    acceptable binding host address type for all client endpoints (This
    rule action can be specified multiple times with each rule action 
    specifying another host address type)
-   `socks.onRequest.clientEndpoints.bindUdpPortRange`: Specifies a
    binding port range for all client UDP endpoints (This rule action can be
    specified multiple times with each rule action specifying another port 
    range)
-   `socks.onRequest.clientEndpoints.netInterface`: Specifies the network
    interface that provides a binding host address for all client endpoints
-   `socks.onRequest.clientEndpoints.socketSetting`: Specifies a socket
    setting for all client endpoints (This rule action can be specified
    multiple times with each rule action specifying another socket setting)
-   `socks.onRequest.remoteEndpoints.bindHost`: Specifies the binding host
    name or address for all remote endpoints
-   `socks.onRequest.remoteEndpoints.bindHostAddressType`: Specifies an
    acceptable binding host address type for all remote endpoints (This
    rule action can be specified multiple times with each rule action 
    specifying another host address type)
-   `socks.onRequest.remoteEndpoints.bindTcpPortRange`: Specifies a
    binding port range for all remote-facing TCP endpoints (This rule action 
    can be specified multiple times with each rule action specifying another 
    port range)
-   `socks.onRequest.remoteEndpoints.bindUdpPortRange`: Specifies a
    binding port range for all remote UDP endpoints (This rule action can be
    specified multiple times with each rule action specifying another port 
    range)
-   `socks.onRequest.remoteEndpoints.netInterface`: Specifies the network
    interface that provides a binding host address for all remote endpoints
-   `socks.onRequest.remoteEndpoints.socketSetting`: Specifies a socket
    setting for all remote endpoints (This rule action can be specified
    multiple times with each rule action specifying another socket setting)
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost`: 
    Specifies the binding host name or address for the client-facing UDP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType`: 
    Specifies an acceptable binding host address type for the client-facing 
    UDP socket (This rule action can be specified multiple times with each 
    rule action specifying another host address type)
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange`: 
    Specifies a binding port range for the client-facing UDP socket (This rule 
    action can be specified multiple times with each rule action specifying 
    another port range)
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface`: 
    Specifies a network interface that provides a binding host address for the 
    client-facing UDP socket
-   `socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting`: 
    Specifies a socket setting for the client-facing UDP socket (This rule 
    action can be specified multiple times with each rule action specifying 
    another socket setting)
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost`: 
    Specifies the binding host name or address for the remote-facing UDP socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAdddressType`: 
    Specifies an acceptable binding host address type for the remote-facing 
    UDP socket (This rule action can be specified multiple times with each 
    rule action specifying another host address type)
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange`: 
    Specifies a binding port range for the remote-facing UDP socket (This rule 
    action can be specified multiple times with each rule action specifying 
    another port range)
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface`: 
    Specifies the network interface that provides a binding host address for 
    the remote-facing UDP socket
-   `socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting`: 
    Specifies a socket setting for the remote-facing UDP socket (This rule 
    action can be specified multiple times with each rule action specifying 
    another socket setting)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method` 
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.user`
-   `socksServerAddress`

The rule action `socks.onBindRequest.remoteSocket.socketSetting` can also be 
used with the following rule conditions:

-   `socks.reply.serverBoundAddress`
-   `socks.reply.serverBoundPort`

Command line example:

```bash
# Allow the CONNECT request to connect to 'special-server.net' and configure 
# the TCP socket to be used to access the remote server.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.command=CONNECT,socks.request.desiredDestinationAddress=special-server.net,firewallAction=ALLOW,socks.onConnectRequest.remoteSocket.socketSetting=SO_RCVBUF=256,socks.onConnectRequest.remoteSocket.socketSetting=SO_SNDBUF=256 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks.request.desiredDestinationAddress</name>
                        <value>special-server.net</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks.onConnectRequest.remoteSocket.socketSetting</name>
                        <socketSetting>
                            <name>SO_RCVBUF</name>
                            <value>256</value>
                        </socketSetting>
                    </ruleAction>
                    <ruleAction>
                        <name>socks.onConnectRequest.remoteSocket.socketSetting</name>
                        <socketSetting>
                            <name>SO_SNDBUF</name>
                            <value>256</value>
                        </socketSetting>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow the CONNECT request to connect to 'special-server.net' and configure the TCP socket to be used to access the remote server.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow the CONNECT request to connect to 'special-server.net' 
             * and configure the TCP socket to be used to access the remote 
             * server.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.command=CONNECT,"
                + "socks.request.desiredDestinationAddress=special-server.net,"
                + "firewallAction=ALLOW,"
                + "socks.onConnectRequest.remoteSocket.socketSetting=SO_RCVBUF=256,"
                + "socks.onConnectRequest.remoteSocket.socketSetting=SO_SNDBUF=256"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

### Configuring Relay Settings Through Rules

To configure the relay settings through rules, you will need any of the 
following rule actions:

-   `socks.onBindRequest.relayBufferSize`: Specifies the buffer size in bytes 
for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks.onBindRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))
-   `socks.onConnectRequest.relayBufferSize`: Specifies the buffer size in bytes 
for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks.onConnectRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))
-   `socks.onRequest.relayBufferSize`: Specifies the buffer size in bytes
for relaying the data (Value must be an integer between 1 (inclusive) and
2147483647 (inclusive))
-   `socks.onRequest.relayIdleTimeout`: Specifies the timeout in
milliseconds on relaying no data (Value must be an integer between 1 (inclusive)
and 2147483647 (inclusive))
-   `socks.onUdpAssociateRequest.relayBufferSize`: Specifies the buffer size in 
bytes for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks.onUdpAssociateRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method`
-   `socks.reply.serverBoundAddress`
-   `socks.reply.serverBoundPort`
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.secondReply.serverBoundAddress`
-   `socks.secondReply.serverBoundPort`
-   `socks.user`
-   `socksServerAddress`

Command line example:

```bash
# Allow the CONNECT request to connect to 'intermittent-idling-server.net' 
# with a relay idle timeout of 1024000 milliseconds (1024 seconds).
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.command=CONNECT,socks.request.desiredDestinationAddress=intermittent-idling-server.net,firewallAction=ALLOW,socks.onConnectRequest.relayIdleTimeout=1024000 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks.request.desiredDestinationAddress</name>
                        <value>intermittent-idling-server.net</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks.onConnectRequest.relayIdleTimeout</name>
                        <value>1024000</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow the CONNECT request to connect to 'intermittent-idling-server.net' with a relay idle timeout of 1024000 milliseconds (1024 seconds).</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow the CONNECT request to connect to 
             * 'intermittent-idling-server.net' with a relay idle 
             * timeout of 1024000 milliseconds (1024 seconds).
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.command=CONNECT,"
                + "socks.request.desiredDestinationAddress=intermittent-idling-server.net,"
                + "firewallAction=ALLOW,"
                + "socks.onConnectRequest.relayIdleTimeout=1024000"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

### Limiting Relay Bandwidth Through Rules

To limit the relay bandwidth through rules, you will need any of the following 
rule actions:

-   `socks.onBindRequest.relayInboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks.onBindRequest.relayOutboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks.onConnectRequest.relayInboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks.onConnectRequest.relayOutboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks.onRequest.relayInboundBandwidthLimit`: Specifies the upper
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks.onRequest.relayOutboundBandwidthLimit`: Specifies the upper
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks.onUdpAssociateRequest.relayInboundBandwidthLimit`: Specifies the 
upper limit on bandwidth in bytes per second of receiving inbound data to be 
relayed
-   `socks.onUdpAssociateRequest.relayOutboundBandwidthLimit`: Specifies the 
upper limit on bandwidth in bytes per second of receiving outbound data to be 
relayed

The value given to any of the rule actions must be an integer between 1 
(inclusive) and 2147483647 (inclusive)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks.method` 
-   `socks.reply.serverBoundAddress`
-   `socks.reply.serverBoundPort`
-   `socks.request.command`
-   `socks.request.desiredDestinationAddress`
-   `socks.request.desiredDestinationPort`
-   `socks.secondReply.serverBoundAddress`
-   `socks.secondReply.serverBoundPort`
-   `socks.user`
-   `socksServerAddress`

Command line example:

```bash
# Allow the CONNECT request to connect to 'streaming-server.net' with an upper 
# limit on the relay inbound and outbound bandwidth of 1024000 bytes per 
# second.
# Allow anything else.
jargyle start-server \
    --setting=rule=socks.request.command=CONNECT,socks.request.desiredDestinationAddress=streaming-server.net,firewallAction=ALLOW,socks.onConnectRequest.relayInboundBandwidthLimit=1024000,socks.onConnectRequest.relayOutboundBandwidthLimit=1024000 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks.request.desiredDestinationAddress</name>
                        <value>streaming-server.net</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                         <name>socks.onConnectRequest.relayInboundBandwidthLimit</name>
                        <value>1024000</value>
                    </ruleAction>
                    <ruleAction>
                         <name>socks.onConnectRequest.relayOutboundBandwidthLimit</name>
                        <value>1024000</value>
                    </ruleAction>                
                </ruleActions>
            </rule>
            <doc>Allow the CONNECT request to connect to 'streaming-server.net' with an upper limit on the relay inbound and outbound bandwidth of 1024000 bytes per second.</doc>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
            <doc>Allow anything else.</doc>
        </setting>    
    </settings>
</configuration>
```

API example:

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
            /*
             * Allow the CONNECT request to connect to 
             * 'streaming-server.net' with an upper limit on the 
             * relay inbound and outbound bandwidth of 1024000 
             * bytes per second.
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks.request.command=CONNECT,"
                + "socks.request.desiredDestinationAddress=streaming-server.net,"
                + "firewallAction=ALLOW,"
                + "socks.onConnectRequest.relayInboundBandwidthLimit=1024000,"
                + "socks.onConnectRequest.relayOutboundBandwidthLimit=1024000"),
            /*
             * Allow anything else.
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```
