# About Jargyle

## Introduction

Jargyle is a Java SOCKS5 server and API designed to be both flexible and 
powerful. It allows you to create SOCKS5 proxies, integrate SOCKS5 
functionality into your Java applications, and manage network traffic with 
fine-grained control. Inspired by projects like JSocks, SocksLib, Esocks, and 
Dante, Jargyle aims to provide a modern and robust solution for SOCKS5 in Java.

## Project Status

Jargyle is still a **work in progress** and is **not yet production-ready**. 
Here’s why:

-   **Testing**: Comprehensive unit and integration tests are still under 
    development. This means that while the project is functional, it is not 
    yet fully tested for all use cases and edge cases.

-   **Documentation**: The Javadocs and reference documentation are 
    incomplete. The user guide provides basic usage examples, but some 
    advanced features may lack detailed explanations.

**Warning**: Breaking changes may occur as the project evolves. However, any 
existing documentation will be updated to reflect these changes.

## Key Concepts

Before diving into the features and examples, it's important to understand the 
core concepts behind Jargyle:

-   **SOCKS5**: A network protocol that allows clients to connect to servers 
    through a proxy server. SOCKS5 provides advantages like firewall traversal 
    and anonymity.

-   **SOCKS Server**: Accepts connections from clients, forwards their 
    requests to the destination server, and relays the responses back.

-   **SOCKS Client API**: Allows Java applications to act as SOCKS clients, 
    routing their network traffic through a SOCKS server.

-   **Proxy Chaining**: Routes traffic through multiple SOCKS servers, 
    creating a chain of proxies.

-   **SSL/TLS and DTLS**: Encrypts traffic between clients and the SOCKS 
    server (SSL/TLS for TCP, DTLS for UDP) for enhanced security.

-   **Authentication Methods**: SOCKS5 supports different authentication 
    methods, including no authentication, username/password, and GSS-API.

## Features

### Core Functionality

-   **SOCKS5 Server and API**: Create and manage SOCKS5 servers and integrate 
    SOCKS5 client functionality into your Java applications.

-   **CONNECT, BIND, and UDP ASSOCIATE**: Supports all standard SOCKS5 
    requests.

-   **Authentication**: Supports No Authentication, Username/Password 
    Authentication, and GSS-API Authentication.

### Advanced Features

-   **SSL/TLS and DTLS Support**: Encrypt TCP and UDP traffic for enhanced 
    security.

-   **Proxy Chaining**: Route traffic through multiple SOCKS5 servers.

-   **Host Resolution**: Resolve hostnames locally or through a SOCKS5 server.

-   **Rule-Based Traffic Management**:
    
    -   **Firewall Rules**: Allow or deny traffic based on various criteria.
    
    -   **Rate Limiting**: Limit the number of simultaneous connections.
    
    -   **Route Selection**: Route traffic through specific proxy chains based 
        on rules.
    
    -   **Traffic Redirection**: Redirect traffic to different destinations.
    
    -   **Socket Configuration**: Configure socket settings (e.g., buffer 
        sizes).
    
    -   **Bandwidth Limiting**: Control the bandwidth used for data relay.

### API Highlights

-   **Configuration-Based Design**: Tailor the SOCKS server and client 
    behavior through a flexible configuration system.

-   **Synchronous I/O**: Utilizes a straightforward and predictable 
    synchronous I/O model, emphasizing simplicity and reliability.

## Uses

-   **Enhancing SOCKS Support**: Use Jargyle to provide advanced SOCKS 
    features (e.g., authentication, proxy chaining) to applications that only 
    have basic SOCKS support.

-   **Secure Tunneling**: Encrypt your network traffic using SSL/TLS or DTLS.

-   **Circumventing Restrictions**: Bypass firewalls and geo-restrictions.

-   **Testing and Development**: Simulate different network conditions and 
    test SOCKS5 client/server implementations.

-   **Adding SOCKS5 Support to Applications**: Integrate the SOCKS client API 
    into your Java applications to route traffic through a SOCKS server.

## Examples

The following examples demonstrate how to use Jargyle's command-line interface 
and APIs.

### Command Line Examples

```bash
# Start a SOCKS server on port 8080 with username/password authentication
jargyle start-server \
    --setting=port=8080 \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassmethod.userRepository=StringSourceUserRepository:Aladdin:opensesame
```

```bash
# Start a SOCKS server that chains to another SOCKS server
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://upstream-proxy.example.com:1080
```

### SOCKS Server API Examples

```java

// Example: Start a basic SOCKS server programmatically

package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        SocksServer server = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                Setting.newInstanceWithParsedValue("port", "1080")
        ))).start();
    }
}
```

### SOCKS Client API Examples

```java

// Example: Configure a SOCKS client to connect to a SOCKS server

package com.example;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;

import java.io.IOException;

import java.net.Socket;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.setProperty("socksClient.socksServerUri", "socks5://127.0.0.1:1080");
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();
        Socket socket = netObjectFactory.newSocket("google.com", 80);
        // ...
    }
}
```

## Get Started

1.   **Download**: Download the latest release of Jargyle from 
     [Releases](releases.md).

2.   **Explore**:
     
     -   **User Guide**: Learn how to use Jargyle with the 
         [user guide](userguide/index.md).
     
     -   **API Reference (Incomplete)**: Explore the API documentation in the 
         [reference documentation](reference/index.md). *(Note: This 
         documentation is currently incomplete.)*
     
     -   **JavaDocs (Incomplete)**: Browse the JavaDocs 
         [here](https://jh3nd3rs0n.github.io/jargyle/apidocs/index.html). 
         *(Note: These JavaDocs are currently incomplete.)*
     
3.   **Feedback Welcome!**: If you encounter any issues or have suggestions, 
     please open an [issue](https://github.com/jh3nd3rs0n/jargyle/issues) on 
     GitHub. Your feedback will help improve Jargyle!

## License

Jargyle is licensed under the 
[MIT license](https://opensource.org/licenses/MIT). You are free to use, 
modify, and distribute Jargyle in accordance with the terms of this license. 
See the full license text 
[here](https://jh3nd3rs0n.github.io/jargyle/licenses.html).