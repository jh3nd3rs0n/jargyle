# Client Configuration

## Contents

-   [Overview](#overview)
-   [Enabling SSL/TLS-layered TCP Traffic Between the Client and the SOCKS Server](#enabling-ssltls-layered-tcp-traffic-between-the-client-and-the-socks-server)
-   [Enabling DTLS-layered UDP Traffic Between the Client and the SOCKS Server](#enabling-dtls-layered-udp-traffic-between-the-client-and-the-socks-server)
-   [Accessing the SOCKS Server Using SOCKS5 Authentication](#accessing-the-socks-server-using-socks5-authentication)
    -   [Accessing the SOCKS Server Using No Authentication](#accessing-the-socks-server-using-no-authentication)
    -   [Accessing the SOCKS Server Using Username Password Authentication](#accessing-the-socks-server-using-username-password-authentication)
    -   [Accessing the SOCKS Server Using GSS-API Authentication](#accessing-the-socks-server-using-gss-api-authentication)
-   [Resolving Host Names From the SOCKS5 Server](#resolving-host-names-from-the-socks5-server)
-   [Routing Traffic Through a Specified Chain of SOCKS Servers](#routing-traffic-through-a-specified-chain-of-socks-servers)

## Overview

This document discusses how to further use the [client API](client-api.md) 
by means of configuration.

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

## Resolving Host Names From the SOCKS5 Server

By default, host name resolution from the SOCKS5 server occurs only when a 
`Socket` is created with a host name and port number.

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
from the SOCKS5 server instead, you would need to set the property 
`socksClient.socks5.socks5HostResolver.resolveFromSocks5Server` set to 
`true`. This property can only be used if the SOCKS5 server supports handling 
the SOCKS5 RESOLVE request.

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
            "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
            "true");
        HostResolverFactory hostResolverFactory = 
            HostResolverFactory.getInstance();
        SocketFactory socketFactory = SocketFactory.getInstance();
        HostResolver hostResolver = hostResolverFactory.newHostResolver();
        InetAddress inetAddress = hostResolver.resolve("example.com");
        Socket socket = socketFactory.newSocket(inetAddress, 443);
        // ...
    }
}
```

## Routing Traffic Through a Specified Chain of SOCKS Servers

You can have the client route traffic through a specified chain of SOCKS 
servers. To do this, you will need to create a chain of `SocksClient` objects 
with each `SocksClient` object specifying a SOCKS server.

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

The known limitations of routing traffic through a specified chain of SOCKS 
servers include the following:

-   Only TCP traffic can be routed through the chain.
