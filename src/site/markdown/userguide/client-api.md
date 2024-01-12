# Client API

## Page Contents

-   [Introduction](#introduction)
-   [Creating the NetObjectFactory from System Properties](#creating-the-netobjectfactory-from-system-properties)
-   [Creating the NetObjectFactory from the SocksClient Object](#creating-the-netobjectfactory-from-the-socksclient-object)
    -   [The Property Object and the Properties Object](#the-property-object-and-the-properties-object)

## Introduction

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

At minimum, the following system properties are needed for the 
`NetObjectFactory` object to create networking objects whose traffic would be 
routed through the SOCKS server:

-   `socksServerUri.scheme`: Specifies what SOCKS protocol is used against 
the SOCKS server. At this time, the only supported scheme is `socks5`
-   `socksServerUri.host`: Specifies the host name or address of the SOCKS 
server.
-   `socksServerUri.port`: Specifies the port number of the SOCKS server. 
This system property is optional. The default is 1080.

A complete listing of the properties can be found 
[here](../reference/client-properties.md).

Client API example:

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
        
        System.setProperty("socksServerUri.scheme", "socks5");
        
        System.setProperty("socksServerUri.host", "jargyle.net");
        
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();
        
        Socket socket = netObjectFactory.newSocket();
        ServerSocket serverSocket = netObjectFactory.newServerSocket();
        DatagramSocket datagramSocket = netObjectFactory.newDatagramSocket();
        HostResolver hostResolver = netObjectFactory.newHostResolver();
        
        // ...
    }
}
```

Although in the above example, the system properties are set within the code, 
the system properties can instead be set outside the program through the `-D` 
option in the `java` utility. This gives the advantage of enabling traffic 
through a SOCKS server or not.

```text
java -DsocksServerUri.scheme=socks5 -DsocksServerUri.host=jargyle.net ...
```

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
import java.net.ServerSocket;
import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        
        String socksServerHost = "jargyle.net";
        Integer socksServerPort = Integer.valueOf(1234);
        
        SocksServerUri socksServerUri = 
            Scheme.SOCKS5.newSocksServerUri(
                socksServerHost, socksServerPort);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            Properties.of());
                
        NetObjectFactory netObjectFactory = 
            socksClient.newSocksNetObjectFactory();
        
        Socket socket = netObjectFactory.newSocket();
        ServerSocket serverSocket = netObjectFactory.newServerSocket();
        DatagramSocket datagramSocket = netObjectFactory.newDatagramSocket();
        HostResolver hostResolver = netObjectFactory.newHostResolver();
        
        // ...
    }
}
```

### The Property Object and the Properties Object

The simplest way to create a `Property` object is to use the method 
`Property.newInstanceWithParsedValue(String, String)`. The first 
`String` parameter would be the name of the property. The second `String` 
parameter would be the parsable value of the property.

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
        
        String socksServerHost = "jargyle.net";
        Integer socksServerPort = Integer.valueOf(1234);
        
        SocksServerUri socksServerUri = 
            Scheme.SOCKS5.newSocksServerUri(
                socksServerHost, socksServerPort);
        
        SocksClient socksClient = socksServerUri.newSocksClient(
            properties);
        
        // ...
    }
}
```