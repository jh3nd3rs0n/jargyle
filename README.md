# Jargyle

[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

## Contents

-   [Introduction](#introduction)
-   [License](#license)
-   [Releases](#releases)
-   [Automated Testing](#automated-testing)
-   [Building](#building)

## Introduction

Jargyle is a Java SOCKS5 API and server.

**Note:** Although the user guide is complete, the reference and Javadocs are 
incomplete at this time, but they will be complete in the future.

-   [Client API Example](#client-api-example)
-   [Server API Example](#server-api-example)
-   [Command Line Example](#command-line-example)
-   [Features](#features)

### Client API Example:

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
        /*
         * Configure the SOCKS client through system properties.
         */
        /*
         * Set the URI of the SOCKS server for the SOCKS client to 
         * connect.
         */
        System.setProperty("socksServerUri.scheme", "socks5");
        System.setProperty("socksServerUri.host", "jargyle.net");
        System.setProperty("socksServerUri.port", "8080");
        /*
         * Enable SSL/TLS for TCP traffic between the SOCKS client 
         * and the SOCKS server.
         */
        System.setProperty("socksClient.ssl.enabled", "true");
        System.setProperty("socksClient.ssl.trustStoreFile", "jargyle.jks");
        System.setProperty("socksClient.ssl.trustStorePassword", "password");
        /*
         * Enable DTLS for UDP traffic between the SOCKS client and 
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
         * Have the HostResolver to send the RESOLVE command to the 
         * SOCKS server to resolve host names instead of having the 
         * HostResolver resolve host names from the local system.
         */
        System.setProperty("socksClient.socks5.useResolveCommand", "true");
        
        /*
         * Create networking objects whose traffic would be routed 
         * through the SOCKS server based on the system properties 
         * above. If no system properties for configuring the SOCKS 
         * client were provided, the created networking objects 
         * would be ordinary networking objects.
         */
        NetObjectFactory netObjectFactory = NetObjectFactory.newInstance();
        Socket socket = netObjectFactory.newSocket();
        ServerSocket serverSocket = netObjectFactory.newServerSocket();
        DatagramSocket datagramSocket = netObjectFactory.newDatagramSocket();
        HostResolver hostResolver = netObjectFactory.newHostResolver();
        
        /*
         * Use the created networking objects as if they were 
         * ordinary networking objects.
         */
        // ...
    }
}
```

### Server API Example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.newInstance(
            Setting.newInstanceWithParsableValue(
                "port", "8080"),
            /*
             * Enable SSL/TLS for TCP traffic between the SOCKS 
             * server and the clients.
             */
            Setting.newInstanceWithParsableValue(
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsableValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsableValue(
                "ssl.keyStorePassword", "drowssap"),
            /*
             * Enable DTLS for UDP traffic between the SOCKS server 
             * and the clients.
             */
            Setting.newInstanceWithParsableValue(
                "dtls.enabled", "true"),
            Setting.newInstanceWithParsableValue(
                "dtls.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsableValue(
                "dtls.keyStorePassword", "drowssap"),
            /*
             * Use only the SOCKS5 username password authentication 
             * method as the SOCKS5 authentication method of choice.
             */
            Setting.newInstanceWithParsableValue(
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsableValue(
                "socks5.userpassmethod.userRepository",
                "StringSourceUserRepository:Aladdin:opensesame")
        ))).start();
    }
}
```

### Command Line Example:

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

### Features

The client API has the following features:

-   Route traffic through a specified chain of SOCKS servers
-   Use SSL/TLS for TCP traffic to and from SOCKS servers
-   Use DTLS for UDP traffic to and from SOCKS servers
-   Resolve host names from SOCKS5 servers

The server API and the server have the following features:

-   Route traffic through multiple specified chains of SOCKS servers
-   Use SSL/TLS for TCP traffic to and from clients
-   Use DTLS for UDP traffic to and from clients
-   Use SSL/TLS for TCP traffic to and from SOCKS servers
-   Use DTLS for UDP traffic to and from SOCKS servers
-   Resolve host names for clients from the local system or from SOCKS5 servers

The server API and the server also have a rule system that allows you to manage 
traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through a selection of multiple specified chains of SOCKS servers
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth

## License

Jargyle is licensed under the 
[MIT license](https://github.com/jh3nd3rs0n/jargyle/blob/master/LICENSE).

## Releases

The following are the types of releases:

-   [Build System Dependency](#build-system-dependency)
-   [Binary distribution](#binary-distribution)
-   [Source distribution](#source-distribution)

### Build System Dependency

The build system dependency provides both the client API and the server API.

Requirements:

-   Java 9 or higher

To declare the dependency in your build system, the definition to declare the 
dependency can be found 
[here](https://jh3nd3rs0n.github.io/jargyle/dependency-info.html). 

To declare the dependency in your build system for only the client API, the 
definition to declare the dependency can be found 
[here](https://jh3nd3rs0n.github.io/jargyle/jargyle-client/dependency-info.html).

To declare the dependency in your build system for only the server API, the 
definition to declare the dependency can be found 
[here](https://jh3nd3rs0n.github.io/jargyle/jargyle-server/dependency-info.html). 

### Binary Distribution

The binary distribution contains the files to run Jargyle from the command 
line. The JAR files included in the distribution can also be used as an API for
your project.

Requirements:

-   Java 9 or higher

Once you have installed the requirements for the binary distribution, be sure 
to have the environment variable `JAVA_HOME` set to the Java home directory.

Releases for the binary distribution can be found
[here](https://github.com/jh3nd3rs0n/jargyle/releases).

### Source Distribution

The source distribution contains the files to run automated testing and to 
build the binary distribution.

Requirements:

-   Java 9 or higher
-   Apache Maven 3.3.9 or higher

Once you have installed the requirements for the source distribution, be sure
to have the environment variable `JAVA_HOME` set to the Java home directory.

Releases for the source distribution can be found
[here](https://github.com/jh3nd3rs0n/jargyle/releases).

## Automated Testing

To run automated testing, run the following commands:

```bash
cd jargyle
mvn clean verify
```

## Building

To build and package the binary distribution, run the following command:

```bash
mvn clean package
```

After running the aforementioned command, the built binary distribution can be 
found as a directory and in multiple archive formats in the following path:

```text
jargyle-distribution/target/
```
