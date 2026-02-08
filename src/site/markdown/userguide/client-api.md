# Client API

## Contents

-   [Overview](#overview)
-   [Main Entry Points](#main-entry-points)
-   [The Default Factory Objects](#the-default-factory-objects)
-   [The Factory Objects from the SocksClient Object Created from System Properties](#the-factory-objects-from-the-socksclient-object-created-from-system-properties)
-   [The Factory Objects from the SocksClient Object](#the-factory-objects-from-the-socksclient-object)
    -   [The Property Object and the Properties Object](#the-property-object-and-the-properties-object)

## Overview

This document discusses how to use the client API.

## Main Entry Points

The main entry point classes for the client API are the following classes:

-   `DatagramSocketFactory`: a factory that creates `DatagramSocket`s
-   `HostResolverFactory`: a factory that creates `HostResolver`s. (A 
    `HostResolver` resolves a provided host name or address by returning a 
    `InetAddress` of the provided host name or address.)
-   `ServerSocketFactory`: a factory that creates `ServerSocket`s
-   `SocketFactory`: a factory that creates `Socket`s

There are three approaches to obtaining a factory object of the aforementioned 
classes:

1.   Obtain a factory object from the factory class's static method 
     `getDefault()`. This method returns a default factory object. The default 
     factory object creates plain objects.
     
2.   Obtain a factory object from the factory class's static method 
     `getInstance()`. This method returns a factory object from the 
     `SocksClient` object created from the system properties. Such a factory 
     object creates objects whose traffic would be routed through the SOCKS 
     server. If no system properties are set for creating the `SocksClient` 
     object, the resultant factory object is a default factory object 
     described in approach number 1.
     
3.   Obtain a factory object from a `SocksClient` object.

## The Default Factory Objects

To obtain a default factory object, you would need to call the factory class's 
static method `getDefault()`. This method returns a default factory object of 
the class. The default factory object creates plain objects.

Client API example:

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
        
        /*
         * Example of obtaining a default HostResolverFactory and a default 
         * SocketFactory and creating a plain HostResolver and a plain Socket
         */
        /*
        HostResolverFactory hostResolverFactory = 
            HostResolverFactory.getDefault();
        SocketFactory socketFactory = SocketFactory.getDefault();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        */

        /*
         * Example of obtaining a default ServerSocketFactory and creating a 
         * plain ServerSocket
         */
        /*
        ServerSocketFactory serverSocketFactory = 
            ServerSocketFactory.getDefault();
        ServerSocket serverSocket = serverSocketFactory.newServerSocket(443);
        */

        /*
         * Example of obtaining a default DatagramSocketFactory and creating a 
         * plain DatagramSocket
         */        
        /*
        DatagramSocketFactory datagramSocketFactory = 
            DatagramSocketFactory.getDefault();
        DatagramSocket datagramSocket = 
            datagramSocketFactory.newDatagramSocket(4444);
        */
        
        // ...
    }
}
```

<a id="the-factory-objects-from-the-socksclient-object-created-from-system-properties"></a>
## The Factory Objects from the SocksClient Object Created from System Properties

To obtain a factory object from the `SocksClient` object created from system 
properties, the following system property is needed:

-   `socksClient.socksServerUri`: The URI of the SOCKS server for the SOCKS 
    client to connect. 

Once the aforementioned system property is set, you would need to call the 
factory class's static method `getInstance()`. This method returns a factory 
object of the class. Such a factory object creates objects whose traffic would 
be routed through the SOCKS server. If the aforementioned system property is 
not set, the resultant factory object is a default factory object that creates 
plain objects.

Client API example:

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

        /*
         * Example of obtaining a HostResolverFactory and a SocketFactory and 
         * creating a HostResolver and a Socket
         */
        /*
        HostResolverFactory hostResolverFactory = 
            HostResolverFactory.getInstance();
        SocketFactory socketFactory = SocketFactory.getInstance();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        */

        /*
         * Example of obtaining a ServerSocketFactory and creating a 
         * ServerSocket
         */
        /*
        ServerSocketFactory serverSocketFactory = 
            ServerSocketFactory.getInstance();
        ServerSocket serverSocket = serverSocketFactory.newServerSocket(443);
        */

        /*
         * Example of obtaining a DatagramSocketFactory and creating a 
         * DatagramSocket
         */        
        /*
        DatagramSocketFactory datagramSocketFactory = 
            DatagramSocketFactory.getInstance();
        DatagramSocket datagramSocket = 
            datagramSocketFactory.newDatagramSocket(4444);
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

Other system properties can also be used to configure the `SocksClient` object.

Client API example:

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
        
        System.setProperty("socksClient.socks5.methods", "USERNAME_PASSWORD");
        System.setProperty("socksClient.socks5.userpassauthmethod.username", "Aladdin");
        System.setProperty("socksClient.socks5.userpassauthmethod.password", "opensesame");

        /*
         * Example of obtaining a HostResolverFactory and a SocketFactory and 
         * creating a HostResolver and a Socket 
         */
        /*
        HostResolverFactory hostResolverFactory = 
            HostResolverFactory.getInstance();        
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        SocketFactory socketFactory = SocketFactory.getInstance();   
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        */

        /*
         * Example of obtaining a ServerSocketFactory and creating a
         * ServerSocket
         */
        /*
        ServerSocketFactory serverSocketFactory = 
            ServerSocketFactory.getInstance();
        ServerSocket serverSocket = serverSocketFactory.newServerSocket(443);
        */

        /*
         * Example of obtaining a DatagramSocketFactory and creating a 
         * DatagramSocket
         */        
        /*
        DatagramSocketFactory datagramSocketFactory = 
            DatagramSocketFactory.getInstance();
        DatagramSocket datagramSocket = 
            datagramSocketFactory.newDatagramSocket(4444);
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
     -DsocksClient.socks5.userpassauthmethod.username=Aladdin \
     -DsocksClient.socks5.userpassauthmethod.password=opensesame \
     ...
```

A complete listing of the properties can be found
[here](../reference/client-properties.md).

## The Factory Objects from the SocksClient Object

To obtain a factory object from a `SocksClient` object directly, a 
`SocksClient` object must be created. To create a `SocksClient` object, a 
`SocksServerUri` object must be created. To create a `SocksServerUri` object, 
a `SocksServerUriScheme` enum value must be selected. The 
`SocksServerUriScheme` enum has at this time the only following value:

-   `SOCKS5`: the SOCKS protocol version 5

Client API example:

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
        
        SocksServerUri socksServerUri = 
            SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "jargyle.net", 1234);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            Properties.of());

        /*
         * Example of obtaining a HostResolverFactory and a SocketFactory and 
         * creating a HostResolver and a Socket 
         */
        /*
        HostResolverFactory hostResolverFactory = 
            socksClient.getSocksHostResolverFactory();
        SocketFactory socketFactory = socksClient.getSocksSocketFactory();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        */

        /*
         * Example of obtaining a ServerSocketFactory and creating a 
         * ServerSocket 
         */
        /*
        ServerSocketFactory serverSocketFactory = 
            socksClient.getSocksServerSocketFactory();
        ServerSocket serverSocket = serverSocketFactory.newServerSocket(443);
        */

        /*
         * Example of obtaining a DatagramSocketFactory and creating a 
         * DatagramSocket 
         */        
        /*
        DatagramSocketFactory datagramSocketFactory = 
            socksClient.getSocksDatagramSocketFactory();
        DatagramSocket datagramSocket = 
            datagramSocketFactory.newDatagramSocket(4444);
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
        
        Property<Object> socks5Methods = 
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.methods", "USERNAME_PASSWORD");
                
        Property<Object> username =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassauthmethod.username", 
                "Aladdin");
                
        Property<Object> password =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassauthmethod.password", 
                "opensesame");
        
        // ...
    }
}
```

Again, a complete listing of the properties can be found 
[here](../reference/client-properties.md).

A `Properties` object can be created by using the method 
`Properties.of(Property...)`. The parameter is a varargs parameter of 
`Property` objects.

Client API example:

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
        
        Property<Object> socks5Methods = 
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.methods", "USERNAME_PASSWORD");
                
        Property<Object> username =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassauthmethod.username", 
                "Aladdin");
                
        Property<Object> password =
            Property.newInstanceWithParsedValue(
                "socksClient.socks5.userpassauthmethod.password", 
                "opensesame");
                
        Properties properties = Properties.of(
            socks5Methods, username, password);
        
        SocksServerUri socksServerUri = 
            SocksServerUriScheme.SOCKS5.newSocksServerUri(
                "jargyle.net", 1234);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            properties);
        
        // ...
    }
}
```