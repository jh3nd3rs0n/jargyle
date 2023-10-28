# Jargyle

[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

## Contents

-   [Introduction](#introduction)
-   [License](#license)
-   [Requirements](#requirements)
-   [Releases](#releases)
-   [Automated Testing](#automated-testing)
-   [Building](#building)

## Introduction

Jargyle is a Java SOCKS5 server that has the following additional features:

-   Chain to multiple specified chains of SOCKS servers
-   Use SSL/TLS and DTLS for TCP and UDP traffic from clients and SOCKS servers
-   Resolve host names from an additional SOCKS5 command called RESOLVE

It also has a rule system that allows you to manage traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through multiple selectable routes
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth

## License

Jargyle is licensed under the 
[MIT license](https://github.com/jh3nd3rs0n/jargyle/blob/master/LICENSE). 
Licenses of third party dependencies are listed 
[here](https://github.com/jh3nd3rs0n/jargyle/blob/master/LICENSE_3RD_PARTY).

## Requirements

For automated testing, building, and running Jargyle under the source 
distribution:

-   Apache Maven 3.3.9 or higher 
-   Java 9 or higher

For running Jargyle under the binary distribution:

-   Java 9 or higher

After installation of the requirements, be sure to have the environment 
variable `JAVA_HOME` set to the Java home directory.

## Releases

Releases for the source and binary distributions can be found 
[here](https://github.com/jh3nd3rs0n/jargyle/releases).

## Automated Testing

To run automated testing, run the following commands:

```bash
cd jargyle
mvn clean verify
```

## Building

To build and package Jargyle, run the following command:

```bash
mvn clean package
```

After running the aforementioned command, the built binary distribution can be 
found in the following path:

```text
jargyle-distribution/target/jargyle-distribution-VERSION-bin/
```

Where `VERSION` is the actual version.
