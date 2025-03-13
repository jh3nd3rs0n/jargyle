# Client API

## Contents

-   [Overview](#overview)
-   [Main Entry Point](#main-entry-point)
-   [Creating the NetObjectFactory from System Properties](#creating-the-netobjectfactory-from-system-properties)
-   [Creating the NetObjectFactory from the SocksClient Object](#creating-the-netobjectfactory-from-the-socksclient-object)
    -   [The Property Object and the Properties Object](#the-property-object-and-the-properties-object)

## Overview

This document discusses how to use the client API.

## Main Entry Point

The main entry point object for the client API is the `NetObjectFactory` 
object. The `NetObjectFactory` object creates networking objects such as 
`Socket`s, `DatagramSocket`s, and `ServerSocket`s. There are three 
approaches to creating a `NetObjectFactory` object:

1.   Creating a `NetObjectFactory` object from the method 
`NetObjectFactory.getDefault()`. This method returns a default 
`NetObjectFactory` object. It creates ordinary networking objects.
2.   Creating a `NetObjectFactory` object from the method 
`NetObjectFactory.newInstance()`. This method returns a 
`NetObjectFactory` object based on the system properties for configuring the 
SOCKS client. Such a `NetObjectFactory` object would create networking 
objects whose traffic would be routed through the SOCKS server. If no system 
properties for configuring the SOCKS client are set, the resultant 
`NetObjectFactory` is a default `NetObjectFactory` described in approach 
number 1.
3.   Creating a `NetObjectFactory` object from a `SocksClient` object.

We will be discussing approaches numbers 2 and 3.

## Creating the NetObjectFactory from System Properties

At minimum, the following system property is needed for the `NetObjectFactory` 
object to create networking objects whose traffic would be routed through the 
SOCKS server:

-   `socksClient.socksServerUri`: The URI of the SOCKS server for the SOCKS 
client to connect. 

Client API example:

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

        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        
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

Please note that the scheme in the URI specifies the SOCKS protocol to be used
to access the SOCKS server (`socks5`), the address or name of the machine of
where the SOCKS server resides (`jargyle.net`), and the port number of the 
SOCKS server (`1234`). In the above example, the SOCKS protocol version 5 is 
used. At this time, the only supported scheme for the URI format is `socks5`.

Other system properties can also be used to configure the SOCKS client.

Client API example:

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

        System.setProperty(
            "socksClient.socksServerUri", "socks5://jargyle.net:1234");
        
        System.setProperty("socksClient.socks5.methods", "USERNAME_PASSWORD");
        System.setProperty("socksClient.socks5.userpassmethod.username", "Aladdin");
        System.setProperty("socksClient.socks5.userpassmethod.password", "opensesame");
        
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

Although in the above examples, the system properties are set within the code, 
the system properties can instead be set outside the program through the `-D` 
option in the `java` utility. This gives the advantage of enabling traffic 
through a SOCKS server or not.

Partial command line example:

```text
java -DsocksClient.socksServerUri=socks5://jargyle.net:1234 \
     -DsocksClient.socks5.methods=USERNAME_PASSWORD \
     -DsocksClient.socks5.userpassmethod.username=Aladdin \
     -DsocksClient.socks5.userpassmethod.password=opensesame \
     ...
```

A complete listing of the properties can be found
[here](../reference/client-properties.md).

## Creating the NetObjectFactory from the SocksClient Object

To create a `SocksClient` object with properties, a `SocksServerUri` 
object must be created. To create a `SocksServerUri` object, a `Scheme` 
enum value must be selected. The `Scheme` enum has at this time the only 
following value:

-   `SOCKS5`: the SOCKS protocol version 5

Client API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        
        SocksServerUri socksServerUri = 
            Scheme.SOCKS5.newSocksServerUri(
                "jargyle.net", 1234);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            Properties.of());
                
        NetObjectFactory netObjectFactory = 
            socksClient.newSocksNetObjectFactory();

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

### The Property Object and the Properties Object

The simplest way to create a `Property` object is to use the method 
`Property.newInstanceWithParsedValue(String, String)`. The first 
`String` parameter would be the name of the property. The second `String` 
parameter would be the value of the property to be parsed.

Client API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        
        Property<Object> socks5Methods = 
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.methods", "USERNAME_PASSWORD");
                
        Property<Object> username =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassmethod.username", 
                "Aladdin");
                
        Property<Object> password =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassmethod.password", 
                "opensesame");
        
        // ...
    }
}
```

Again, a complete listing of the properties can be found 
[here](../reference/client-properties.md).

A `Properties` object can be created by using the method 
`Properties.of(Property...)`. The parameter is a varargs 
parameter of `Property` objects.

Client API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;

import java.io.IOException;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        
        Property<Object> socks5Methods = 
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.methods", "USERNAME_PASSWORD");
                
        Property<Object> username =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassmethod.username", 
                "Aladdin");
                
        Property<Object> password =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassmethod.password", 
                "opensesame");
                
        Properties properties = Properties.of(
            socks5Methods, username, password);
        
        SocksServerUri socksServerUri = 
            Scheme.SOCKS5.newSocksServerUri(
                "jargyle.net", 1234);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            properties);
        
        // ...
    }
}
```