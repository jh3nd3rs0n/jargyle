# About Jargyle

## What is Jargyle?

Jargyle is a Java SOCKS5 server and a Java SOCKS5 client/server API. It is 
inspired by 
[JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

## What is the Status of Jargyle?

Jargyle is still a **work in progress** and is **not yet production-ready**. 
Here’s why:

-   **Testing**: Comprehensive unit and integration tests are still under 
    development. This means that while Jargyle is functional, it is not yet 
    fully tested for all use cases and edge cases.

-   **Documentation**: The Javadocs and reference documentation are 
    incomplete. The user guide provides basic usage examples, but some 
    advanced features may lack detailed explanations.

**Warning**: Breaking changes may occur as Jargyle evolves. However, any 
existing documentation will be updated to reflect these changes.

## What Makes Jargyle Different from Other Java SOCKS Servers and APIs?

Jargyle **completely implements the SOCKS5 protocol** described in RFC 
[1928](https://datatracker.ietf.org/doc/html/rfc1928). In addition to 
supporting all SOCKS5 requests (**CONNECT, BIND, and UDP ASSOCIATE**), it 
supports **no authentication**, **username/password authentication**, and also 
**GSS-API authentication**.

It can use **SSL/TLS and DTLS** to encrypt TCP and UDP traffic between client 
and server for enhanced security.

It can perform **(SOCKS server) chaining**, routing traffic through multiple 
SOCKS servers.

It can perform **host name resolution** through the local system or through a 
SOCKS server. 

For the SOCKS server and the SOCKS server API, you can **manage traffic using 
rules**. With rules, you can do the following:

-   **Allow or deny traffic** based on various criteria

-   **Limit** the number of **simultaneous connections**

-   **Route traffic** through specific SOCKS server chains based on rules

-   **Redirect traffic** to different destinations

-   **Configure** endpoints

-   **Limit bandwidth** of relaying data

## How Can Applications That Have Basic SOCKS Support Work with Jargyle's Features?

Through chaining, a running instance of Jargyle using none to little 
authentication could route traffic through another running instance of Jargyle 
using SSL/TLS/DTLS and authentication. Applications that have basic SOCKS 
support can use the former to take advantage of the features of the latter.

## What Are Some Examples of Using Jargyle?

Here are a few command line examples:

```bash
# Start a SOCKS server on port 8080 with SSL/TLS and username/password authentication
jargyle start-server \
    --setting=port=8080 \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=password \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauthmethod.userRepository=StringSourceUserRepository:Aladdin:opensesame
```

```bash
# Start a SOCKS server on port 1080 that chains to the previous SOCKS server
jargyle start-server \
    --setting=port=1080 \
    --setting=chaining.socksServerUri=socks5://jargyle.net:8080 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauthmethod.username=Aladdin \
    --setting=chaining.socks5.userpassauthmethod.password=opensesame
```

Here is a SOCKS server API example of the first command line example done 
programmatically:

```java

// Example: Start a SOCKS server programmatically on port 8080 with SSL/TLS and username/password authentication

package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue("port", "8080"),
            Setting.newInstanceWithParsedValue("ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue("ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue("ssl.keyStorePassword", "password"),
            Setting.newInstanceWithParsedValue("socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue("socks5.userpassauthmethod.userRepository", "StringSourceUserRepository:Aladdin:opensesame")
        ))).start();
    }
}
```

Here is a SOCKS client API example connecting to the previous SOCKS server:

```java

// Example: Configure a SOCKS client to connect to the previous SOCKS server

package com.example;

import com.github.jh3nd3rs0n.jargyle.client.SocketFactory;

import java.io.IOException;

import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty("socksClient.socksServerUri", "socks5://jargyle.net:8080");
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.trustStoreFile", "server.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        System.setProperty("socksClient.socks5.methods", "USERNAME_PASSWORD");
        System.setProperty("socksClient.socks5.userpassauthmethod.username", "Aladdin");
        System.setProperty("socksClient.socks5.userpassauthmethod.password", "opensesame");
        SocketFactory socketFactory = SocketFactory.getInstance();
        Socket socket = socketFactory.newSocket("example.com", 80);
        // ...
    }
}
```

## How Can I Get Started with Jargyle?

You can download the latest release of Jargyle [here](./downloads.html).

You can also explore the following:

-   **User Guide**: Learn how to use Jargyle with the
    [user guide](userguide/index.md).

-   **Reference Documentation**: Explore Jargyle's comprehensive usage with 
    the [reference documentation](reference/index.md). 
    *(Note: This documentation is currently incomplete.)*

-   **Javadocs**: Browse the 
    [SOCKS client/server API Javadocs](https://jh3nd3rs0n.github.io/jargyle/apidocs/index.html).
    *(Note: These Javadocs are currently incomplete.)*

Feedback is welcome. If you encounter any issues or have suggestions, please 
open an issue on GitHub 
[here](https://github.com/jh3nd3rs0n/jargyle/issues). Your feedback will help 
improve Jargyle!

## What is the License for Jargyle?

Jargyle is licensed under the 
[MIT license](https://opensource.org/licenses/MIT). You are free to use, 
modify, and distribute Jargyle in accordance with the terms of this license. 
See the full license text 
[here](https://jh3nd3rs0n.github.io/jargyle/licenses.html).