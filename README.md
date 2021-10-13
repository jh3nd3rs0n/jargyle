# Jargyle 

[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

Jargyle is a Java SOCKS5 server. It has the following features:

-   100% implementation of the SOCKS5 protocol specification which includes [username password authentication](#5-11-2-using-username-password-authentication) and [GSS-API authentication](#5-11-3-using-gss-api-authentication)
-   [SSL/TLS for TCP traffic between Jargyle and its clients](#5-7-using-ssl-tls-for-tcp-traffic-between-jargyle-and-its-clients)
-   [DTLS for UDP traffic between Jargyle and its clients](#5-8-using-dtls-for-udp-traffic-between-jargyle-and-its-clients)
-   [Host name resolution](#5-9-using-host-name-resolution)
-   [SOCKS server chaining](#5-12-chaining-to-another-socks-server)
-   [SSL/TLS for TCP traffic through SOCKS server chaining](#5-12-1-using-ssl-tls-for-tcp-traffic-through-socks-server-chaining)
-   [DTLS for UDP traffic through SOCKS server chaining](#5-12-2-using-dtls-for-udp-traffic-through-socks-server-chaining)
-   [Host name resolution through SOCKS5 server chaining](#5-12-3-using-host-name-resolution-through-socks5-server-chaining)
-   [SOCKS server chaining to a specified chain of other SOCKS servers](#5-13-chaining-to-a-specified-chain-of-other-socks-servers)
-   [Allow or block addresses](#5-14-allowing-or-blocking-addresses)
-   [Allow or block SOCKS5 requests](#5-15-allowing-or-blocking-socks5-requests)

Although Jargyle can act as a standalone SOCKS5 server, it can act as a bridge between the following:

-   Operating systems and applications that access SOCKS5 servers using plaintext connections and no SOCKS5 authentication
-   SOCKS5 servers requiring SSL/TLS/DTLS connections and/or SOCKS5 authentication

**DISCLAIMER**: Jargyle is not production-ready but it aims to be. It is also subject to breaking changes.

## Contents

-   [1. Requirements](#1-requirements)
-   [2. Releases](#2-releases)
-   [3. Automated Testing](#3-automated-testing)
-   [4. Building](#4-building)
-   [5. Running Jargyle](#5-running-jargyle)
-   [5. 1. Usage](#5-1-usage)
-   [5. 2. Creating a Configuration File](#5-2-creating-a-configuration-file)
-   [5. 3. Supplementing a Configuration File With Command Line Options](#5-3-supplementing-a-configuration-file-with-command-line-options)
-   [5. 4. Combining Configuration Files](#5-4-combining-configuration-files)
-   [5. 5. Running With a Configuration File](#5-5-running-with-a-configuration-file)
-   [5. 6. Running With a Monitored Configuration File](#5-6-running-with-a-monitored-configuration-file)
-   [5. 7. Using SSL/TLS for TCP Traffic Between Jargyle and Its Clients](#5-7-using-ssl-tls-for-tcp-traffic-between-jargyle-and-its-clients)
-   [5. 8. Using DTLS for UDP Traffic Between Jargyle and Its Clients](#5-8-using-dtls-for-udp-traffic-between-jargyle-and-its-clients)
-   [5. 9. Using Host Name Resolution](#5-9-using-host-name-resolution)
-   [5. 10. Managing SOCKS5 Users (for Username Password Authentication)](#5-10-managing-socks5-users-for-username-password-authentication)
-   [5. 10. 1. Creating a Users File](#5-10-1-creating-a-users-file)
-   [5. 10. 2. Adding Users to an Existing Users File](#5-10-2-adding-users-to-an-existing-users-file)
-   [5. 10. 3. Removing a User From an Existing Users File](#5-10-3-removing-a-user-from-an-existing-users-file)
-   [5. 11. Using SOCKS5 Authentication](#5-11-using-socks5-authentication)
-   [5. 11. 1. Using No Authentication](#5-11-1-using-no-authentication)
-   [5. 11. 2. Using Username Password Authentication](#5-11-2-using-username-password-authentication)
-   [5. 11. 3. Using GSS-API Authentication](#5-11-3-using-gss-api-authentication)
-   [5. 12. Chaining to Another SOCKS Server](#5-12-chaining-to-another-socks-server)
-   [5. 12. 1. Using SSL/TLS for TCP Traffic Through SOCKS Server Chaining](#5-12-1-using-ssl-tls-for-tcp-traffic-through-socks-server-chaining)
-   [5. 12. 2. Using DTLS for UDP Traffic Through SOCKS Server Chaining](#5-12-2-using-dtls-for-udp-traffic-through-socks-server-chaining)
-   [5. 12. 3. Using Host Name Resolution Through SOCKS5 Server Chaining](#5-12-3-using-host-name-resolution-through-socks5-server-chaining)
-   [5. 12. 4. Using SOCKS5 Authentication](#5-12-4-using-socks5-authentication)
-   [5. 12. 4. 1. Using No Authentication](#5-12-4-1-using-no-authentication)
-   [5. 12. 4. 2. Using Username Password Authentication](#5-12-4-2-using-username-password-authentication)
-   [5. 12. 4. 3. Using GSS-API Authentication](#5-12-4-3-using-gss-api-authentication)
-   [5. 13. Chaining to a Specified Chain of Other SOCKS Servers](#5-13-chaining-to-a-specified-chain-of-other-socks-servers)
-   [5. 14. Allowing or Blocking Addresses](#5-14-allowing-or-blocking-addresses)
-   [5. 15. Allowing or Blocking SOCKS5 Requests](#5-15-allowing-or-blocking-socks5-requests)
-   [5. 16. Logging](#5-16-logging)
-   [6. Miscellaneous Notes](#6-miscellaneous-notes)
-   [6. 1. The Doc XML Attribute and the Doc XML Element](#6-1-the-doc-xml-attribute-and-the-doc-xml-element)
-   [6. 2. Multiple Settings of the Same Name](#6-2-multiple-settings-of-the-same-name)
-   [6. 3. The SOCKS5 RESOLVE Command](#6-3-the-socks5-resolve-command)
-   [7. Contact](#7-contact)

## 1. Requirements

For automated testing, building, and running Jargyle under the source distribution:

-   Apache Maven&#8482; 3.3.9 or higher 
-   Java&#8482; SDK 1.9 or higher

For running Jargyle under the binary distribution:

-   Java&#8482; Runtime Environment 1.9 or higher

After installation of the requirements, be sure to have the environment variable `JAVA_HOME` set to the location of the installed Java&#8482; SDK or the installed Java&#8482; Runtime Environment.

## 2. Releases

Releases for the source and binary distributions can be found [here](https://github.com/jh3nd3rs0n/jargyle/releases).

## 3. Automated Testing

To run automated testing, run the following commands:

```bash
    
    cd directory-containing-pom.xml
    mvn clean verify
    
```

Where `directory-containing-pom.xml` would be the actual directory that contains the file `pom.xml`. This file is used by the Maven command `mvn`.

## 4. Building

To build and package Jargyle, run the following command:

```bash
    
    mvn clean package
    
```

After running the aforementioned command, the built JAR file and its dependent JAR files can be found in the following path:

```text
    
    target/
    
```

## 5. Running Jargyle 

To run Jargyle without any command line arguments, you can run the following command:

```bash
    
    ./bin/jargyle
    
```

The aforementioned command will run Jargyle on port 1080 at address 0.0.0.0 using no authentication.

### 5. 1. Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

```text
    
    Usage: jargyle [OPTIONS]
           jargyle --config-file-xsd
           jargyle --help
           jargyle --monitored-config-file=FILE
           jargyle [OPTIONS] --new-config-file=FILE
           jargyle --settings-help
           jargyle --socks5-userpassauth-users ARGS
    
    OPTIONS:
      --config-file=FILE, -f FILE
          The configuration file
      --config-file-xsd, -x
          Print the configuration file XSD and exit
      --enter-chaining-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the other SOCKS server
      --enter-chaining-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
      --enter-chaining-socks5-userpassauth-user-pass
          Enter through an interactive prompt the username password to be used to access the other SOCKS5 server
      --enter-chaining-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
      --enter-chaining-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
      --enter-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
      --enter-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the SOCKS server
      --enter-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
      --enter-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
      --help, -h
          Print this help and exit
      --monitored-config-file=FILE, -m FILE
          The configuration file to be monitored for any changes to be applied to the running configuration
      --new-config-file=FILE, -n FILE
          Create a new configuration file based on the preceding options and exit
      --setting=NAME=VALUE, -s NAME=VALUE
          A setting for the SOCKS server
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
      --socks5-userpassauth-users
          Mode for managing SOCKS5 users (add --help for more information)
      --version, -v
          Print version information and exit
    
```

The following is a list of available settings for the SOCKS server (displayed when using the command line option `--settings-help`):

```text
    
    GENERAL SETTINGS:
    
      allowedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed client address criteria (default is matches:.*)
    
      backlog=INTEGER_BETWEEN_0_AND_2147483647
          The maximum length of the queue of incoming connections (default is 50)
    
      blockedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked client address criteria
    
      clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client-facing socket
    
      host=HOST
          The host name or address for the SOCKS server (default is 0.0.0.0)
    
      port=INTEGER_BETWEEN_0_AND_65535
          The port for the SOCKS server (default is 1080)
    
      socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the SOCKS server
    
    CHAINING GENERAL SETTINGS:
    
      chaining.internalBindHost=HOST
          The binding host name or address for the internal socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0.0.0.0)
    
      chaining.internalConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting for the internal socket to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 60000)
    
      chaining.internalSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the internal socket that is used to connect to the other SOCKS server (used for the SOCKS5 command RESOLVE and UDP ASSOCIATE)
    
      chaining.socksServerUri=SCHEME://HOST[:PORT]
          The URI of the other SOCKS server
    
    CHAINING DTLS SETTINGS:
    
      chaining.dtls.enabled=true|false
          The boolean value to indicate if DTLS connections to the other SOCKS server are enabled (default is false)
    
      chaining.dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[...]]]
          The space separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server
    
      chaining.dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[...]]]
          The space separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server
    
      chaining.dtls.keyStoreFile=FILE
          The key store file for the DTLS connections to the other SOCKS server
    
      chaining.dtls.keyStorePassword=PASSWORD
          The password for the key store for the DTLS connections to the other SOCKS server
    
      chaining.dtls.keyStoreType=TYPE
          The type of key store file for the DTLS connections to the other SOCKS server (default is PKCS12)
    
      chaining.dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647
          The maximum packet size for the DTLS connections to the other SOCKS server (default is 32768)
    
      chaining.dtls.protocol=PROTOCOL
          The protocol version for the DTLS connections to the other SOCKS server (default is DTLSv1.2)
    
      chaining.dtls.trustStoreFile=FILE
          The trust store file for the DTLS connections to the other SOCKS server
    
      chaining.dtls.trustStorePassword=PASSWORD
          The password for the trust store for the DTLS connections to the other SOCKS server
    
      chaining.dtls.trustStoreType=TYPE
          The type of trust store file for the DTLS connections to the other SOCKS server (default is PKCS12)
    
    CHAINING SOCKS5 SETTINGS:
    
      chaining.socks5.gssapiauth.mechanismOid=SOCKS5_GSSAPIAUTH_MECHANISM_OID
          The object ID for the GSS-API authentication mechanism to the other SOCKS5 server (default is 1.2.840.113554.1.2.2)
    
      chaining.socks5.gssapiauth.necReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation (default is false)
    
      chaining.socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      chaining.socks5.gssapiauth.serviceName=SOCKS5_GSSAPIAUTH_SERVICE_NAME
          The GSS-API service name for the other SOCKS5 server
    
      chaining.socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[...]]]
          The space separated list of acceptable authentication methods to the other SOCKS5 server (default is NO_AUTHENTICATION_REQUIRED)
    
      chaining.socks5.resolve.useResolveCommand=true|false
          The boolean value to indicate that the RESOLVE command is to be used on the other SOCKS5 server for resolving host names (default is false)
    
      chaining.socks5.userpassauth.usernamePassword=USERNAME:PASSWORD
          The username password to be used to access the other SOCKS5 server
    
    CHAINING SSL SETTINGS:
    
      chaining.ssl.enabled=true|false
          The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled (default is false)
    
      chaining.ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]
          The space separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]
          The space separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.keyStoreFile=FILE
          The key store file for the SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.keyStorePassword=PASSWORD
          The password for the key store for the SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.keyStoreType=TYPE
          The type of key store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)
    
      chaining.ssl.protocol=PROTOCOL
          The protocol version for the SSL/TLS connections to the other SOCKS server (default is TLSv1.2)
    
      chaining.ssl.trustStoreFile=FILE
          The trust store file for the SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.trustStorePassword=PASSWORD
          The password for the trust store for the SSL/TLS connections to the other SOCKS server
    
      chaining.ssl.trustStoreType=TYPE
          The type of trust store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)
    
    DTLS SETTINGS:
    
      dtls.enabled=true|false
          The boolean value to indicate if DTLS connections to the SOCKS server are enabled (default is false)
    
      dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[...]]]
          The space separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server
    
      dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[...]]]
          The space separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server
    
      dtls.keyStoreFile=FILE
          The key store file for the DTLS connections to the SOCKS server
    
      dtls.keyStorePassword=PASSWORD
          The password for the key store for the DTLS connections to the SOCKS server
    
      dtls.keyStoreType=TYPE
          The type of key store file for the DTLS connections to the SOCKS server (default is PKCS12)
    
      dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647
          The maximum packet size for the DTLS connections to the SOCKS server (default is 32768)
    
      dtls.needClientAuth=true|false
          The boolean value to indicate that client authentication is required for DTLS connections to the SOCKS server (default is false)
    
      dtls.protocol=PROTOCOL
          The protocol version for the DTLS connections to the SOCKS server (default is DTLSv1.2)
    
      dtls.trustStoreFile=FILE
          The trust store file for the DTLS connections to the SOCKS server
    
      dtls.trustStorePassword=PASSWORD
          The password for the trust store for the DTLS connections to the SOCKS server
    
      dtls.trustStoreType=TYPE
          The type of trust store file for the DTLS connections to the SOCKS server (default is PKCS12)
    
      dtls.wantClientAuth=true|false
          The boolean value to indicate that client authentication is requested for DTLS connections to the SOCKS server (default is false)
    
    SOCKS5 SETTINGS:
    
      socks5.gssapiauth.necReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default is false)
    
      socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[...]]]
          The space separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)
    
      socks5.onBind.allowedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed inbound address criteria (default is matches:.*)
    
      socks5.onBind.blockedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked inbound address criteria
    
      socks5.onBind.inboundSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the inbound socket
    
      socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the listen socket
    
      socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.prepareServerFacingSocket=true|false
          The boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default is false)
    
      socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.serverFacingBindHost=HOST
          The binding host name or address for the server-facing socket (default is 0.0.0.0)
    
      socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting for the server-facing socket to connect (default is 60000)
    
      socks5.onConnect.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing socket
    
      socks5.onUdpAssociate.allowedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed inbound address criteria (default is matches:.*)
    
      socks5.onUdpAssociate.allowedOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed outbound address criteria (default is matches:.*)
    
      socks5.onUdpAssociate.blockedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked inbound address criteria
    
      socks5.onUdpAssociate.blockedOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked outbound address criteria
    
      socks5.onUdpAssociate.clientFacingBindHost=HOST
          The binding host name or address for the client-facing UDP socket (default is 0.0.0.0)
    
      socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client-facing UDP socket
    
      socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 32768)
    
      socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onUdpAssociate.serverFacingBindHost=HOST
          The binding host name or address for the server-facing UDP socket (default is 0.0.0.0)
    
      socks5.onUdpAssociate.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing UDP socket
    
      socks5.socks5RequestWorkerFactory=CLASSNAME[:VALUE]
          The SOCKS5 request worker factory for the SOCKS5 server
    
      socks5.userpassauth.usernamePasswordAuthenticator=CLASSNAME[:VALUE]
          The username password authenticator for the SOCKS5 server
    
    SSL SETTINGS:
    
      ssl.enabled=true|false
          The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled (default is false)
    
      ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]
          The space separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server
    
      ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]
          The space separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server
    
      ssl.keyStoreFile=FILE
          The key store file for the SSL/TLS connections to the SOCKS server
    
      ssl.keyStorePassword=PASSWORD
          The password for the key store for the SSL/TLS connections to the SOCKS server
    
      ssl.keyStoreType=TYPE
          The type of key store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)
    
      ssl.needClientAuth=true|false
          The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server (default is false)
    
      ssl.protocol=PROTOCOL
          The protocol version for the SSL/TLS connections to the SOCKS server (default is TLSv1.2)
    
      ssl.trustStoreFile=FILE
          The trust store file for the SSL/TLS connections to the SOCKS server
    
      ssl.trustStorePassword=PASSWORD
          The password for the trust store for the SSL/TLS connections to the SOCKS server
    
      ssl.trustStoreType=TYPE
          The type of trust store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)
    
      ssl.wantClientAuth=true|false
          The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server (default is false)
    
    SCHEMES:
    
      socks5
          SOCKS protocol version 5
    
    SOCKET_SETTINGS:
    
      IP_TOS=INTEGER_BETWEEN_0_AND_255
          The type-of-service or traffic class field in the IP header for a TCP or UDP socket
    
      PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2
          Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth
    
      SO_BROADCAST=true|false
          Can send broadcast datagrams
    
      SO_KEEPALIVE=true|false
          Keeps a TCP socket alive when no data has been exchanged in either direction
    
      SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647
          Linger on closing the TCP socket in seconds
    
      SO_OOBINLINE=true|false
          Can receive TCP urgent data
    
      SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647
          The receive buffer size
    
      SO_REUSEADDR=true|false
          Can reuse socket address and port
    
      SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647
          The send buffer size
    
      SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647
          The timeout in milliseconds on waiting for an idle socket
    
      TCP_NODELAY=true|false
          Disables Nagle's algorithm
    
    SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS:
    
      NONE
          No protection
    
      REQUIRED_INTEG
          Required per-message integrity
    
      REQUIRED_INTEG_AND_CONF
          Required per-message integrity and confidentiality
    
    SOCKS5_METHODS:
    
      NO_AUTHENTICATION_REQUIRED
          No authentication required
    
      GSSAPI
          GSS-API authentication
    
      USERNAME_PASSWORD
          Username password authentication
    
                
```

The following is the command line help for managing SOCKS5 users for username password authentication (displayed when using the command line options `--socks5-userpassauth-users --help`):

```text
    
    Usage: jargyle --socks5-userpassauth-users COMMAND
           jargyle --socks5-userpassauth-users --help
           jargyle --socks5-userpassauth-users --xsd
    
    COMMANDS:
      add-users-to-file FILE
          Add users to an existing file through an interactive prompt
      create-new-file FILE
          Create a new file of zero or more users through an interactive prompt
      remove-user NAME FILE
          Remove user by name from an existing file
    
    OPTIONS:
      --help, -h
          Print this help and exit
      --xsd, -x
          Print the XSD and exit
    
```

### 5. 2. Creating a Configuration File

You can create a configuration file by using the command line option `--new-config-file`

The following command creates an empty configuration file:

```bash
    
    ./bin/jargyle --new-config-file=empty_configuration.xml
    
```

`empty_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings/>
    </configuration>
    
```

Any preceding command line options that do not terminate before the command line option `--new-config-file` will be set in the new configuration file.

The following command creates a configuration file with the port number, the number of allowed backlogged connections, and no authentication required:

```bash
    
    ./bin/jargyle \
        --setting=port=1234 \
        --setting=backlog=100 \
        --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
        --new-config-file=configuration.xml
    
```

`configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
        </settings>
    </configuration>
    
```
  
### 5. 3. Supplementing a Configuration File With Command Line Options

You can supplement an existing configuration file with command line options.

The following command adds one command line option after the existing configuration file:

```bash
    
    ./bin/jargyle \
        --config-file=configuration.xml \
        --setting=socketSettings=SO_TIMEOUT=0 \
        --new-config-file=supplemented_configuration.xml
    
```

`supplemented_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>socketSettings</name>
                <socketSettings>
                    <socketSetting>
                        <name>SO_TIMEOUT</name>
                        <value>0</value>
                    </socketSetting>
                </socketSettings>
            </setting>
        </settings>
    </configuration>
    
```

### 5. 4. Combining Configuration Files

You can combine multiple configuration files into one configuration file.

The following command combines the two earlier configuration files into one:

```bash
    
    ./bin/jargyle \
        --config-file=configuration.xml \
        --config-file=supplemented_configuration.xml \
        --new-config-file=combined_configuration.xml
    
```

`combined_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>socketSettings</name>
                <socketSettings>
                    <socketSetting>
                        <name>SO_TIMEOUT</name>
                        <value>0</value>
                    </socketSetting>
                </socketSettings>
            </setting>
        </settings>
    </configuration>
    
```

Although the redundant settings in the combined configuration file are unnecessary, the result configuration file is for demonstration purposes only. (See [Multiple Settings of the Same Name](#6-2-multiple-settings-of-the-same-name) for more information.)

### 5. 5. Running With a Configuration File

To run Jargyle with a configuration file, you can use the command line option `--config-file`

```bash
    
    ./bin/jargyle --config-file=configuration.xml
    
```

Also the configuration file can be supplemented with command line options and/or can be combined with multiple configuration files.

### 5. 6. Running With a Monitored Configuration File

You can run Jargyle with a configuration file to be monitored for any changes to be applied to the running configuration.

To run Jargyle with a monitored configuration file, you can use the command line option `--monitored-config-file`

```bash
    
    ./bin/jargyle --monitored-config-file=configuration.xml
    
```

Unlike the command line option `--config-file`, the monitored configuration file cannot be supplemented with command line options and cannot be combined with multiple configuration files.

The following are the settings in the monitored configuration file that will have no effect if changed during the running configuration:

-   `backlog`
-   `host`
-   `port`
-   `socketSettings`

A restart of Jargyle would be required if you want any of the changed aforementioned settings to be applied to the running configuration.

### 5. 7. Using SSL/TLS for TCP Traffic Between Jargyle and Its Clients

You can use SSL/TLS for TCP traffic between Jargyle and its clients. By default SSL/TLS for TCP traffic between Jargyle and its clients is disabled. To enable SSL/TLS for TCP traffic between Jargyle and its clients, you will need to have the setting `ssl.enabled` set to `true`. In addition, you will need to have the setting `ssl.keyStoreFile` to specify Jargyle's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `ssl.keyStorePassword` to specify the password for Jargyle's key store file.

```text
    
    ./bin/jargyle \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --enter-ssl-key-store-pass
    
```

If you want to have the client authenticate using SSL/TLS, you will need to have the setting `ssl.needClientAuth` set to `true`. In addition, you will need to have the setting `ssl.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `ssl.trustStorePassword` to specify the password for the client's trust store file.

```text
    
    ./bin/jargyle \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password \
        --setting=ssl.needClientAuth=true \
        --setting=ssl.trustStoreFile=client.jks \
        --setting=ssl.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --enter-ssl-key-store-pass \
        --setting=ssl.needClientAuth=true \
        --setting=ssl.trustStoreFile=client.jks \
        --enter-ssl-trust-store-pass
    
```

### 5. 8. Using DTLS for UDP Traffic Between Jargyle and Its Clients

You can use DTLS for UDP traffic between Jargyle and its clients. By default DTLS for UDP traffic between Jargyle and its clients is disabled. To enable DTLS for UDP traffic between Jargyle and its clients, you will need to have the setting `dtls.enabled` set to `true`. In addition, you will need to have the setting `dtls.keyStoreFile` to specify Jargyle's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `dtls.keyStorePassword` to specify the password for Jargyle's key store file.

```text
    
    ./bin/jargyle \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass
    
```

If you want to have the client authenticate using DTLS, you will need to have the setting `dtls.needClientAuth` set to `true`. In addition, you will need to have the setting `dtls.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `dtls.trustStorePassword` to specify the password for the client's trust store file.

```text
    
    ./bin/jargyle \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --setting=dtls.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --enter-dtls-trust-store-pass
    
```

**NOTE**: For a DTLS connection to be established, the client must send a SOCKS5 UDP ASSOCIATE command with a port number of a value greater than zero that the client expects to send and receive datagram packets. If the client sends a SOCKS5 UDP ASSOCIATE command with a port number of zero, a DTLS connection will not be established.

### 5. 9. Using Host Name Resolution

Jargyle can be used to resolve host names. For a client to request Jargyle to resolve a host name, it must use [the SOCKS5 RESOLVE command](#6-3-the-socks5-resolve-command).

### 5. 10. Managing SOCKS5 Users (for Username Password Authentication)

You can manage SOCKS5 users stored in an XML file called a users file. A users file can be used for [username password authentication](#5-11-2-using-username-password-authentication).

#### 5. 10. 1. Creating a Users File

To create a users file, you would run the following command:

```bash
    
    ./bin/jargyle --socks5-userpassauth-users create-new-file FILE
    
```

Where `FILE` would be the name for the new users file.

Once you have run the command, an interactive prompt will ask you if you want to enter a user.

```text
    
    ./bin/jargyle --socks5-userpassauth-users create-new-file users.xml
    Would you like to enter a user? ('Y' for yes): 
    
```

If you do not want to enter a user, a new empty users file will be created. 

```text
    
    Would you like to enter a user? ('Y' for yes): n
    Writing to 'users.xml'...
    
```

`users.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users/>
    
```

If you want to enter a user, the prompt will ask you for the user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you do not want to enter any more users, the new users file will be created.

```text
    
    Would you like to enter a user? ('Y' for yes): Y
    User
    Name: Aladdin
    Password: 
    Re-type password:
    User 'Aladdin' added.
    Would you like to enter another user? ('Y' for yes): Y
    User
    Name: Jasmine
    Password: 
    Re-type password:
    User 'Jasmine' added.
    Would you like to enter another user? ('Y' for yes): Y
    User
    Name: Abu
    Password: 
    Re-type password:
    User 'Abu' added.
    Would you like to enter another user? ('Y' for yes): n
    Writing to 'users.xml'...
    
```

`users.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
    </users>
    
```

#### 5. 10. 2. Adding Users to an Existing Users File

To add users to an existing users file, you would run the following command:

```bash
    
    ./bin/jargyle --socks5-userpassauth-users add-users-to-file FILE
    
```

Where `FILE` would be the name for the existing users file.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you do not want to enter any more users, the updated users file will be saved.

```text
    
    ./bin/jargyle --socks5-userpassauth-users add-users-to-file users.xml
    User
    Name: Jafar
    Password: 
    Re-type password:
    User 'Jafar' added.
    Would you like to enter another user? ('Y' for yes): n
    Writing to 'users.xml'...
    
```

`users.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=</hash>
                <salt>jIBPXJxqlMk=</salt>
            </hashedPassword>
            <name>Jafar</name>
        </user>        
    </users>
    
```

#### 5. 10. 3. Removing a User From an Existing Users File

To remove a user from an existing users file, you would run the following command:

```bash
    
    ./bin/jargyle --socks5-userpassauth-users remove-user NAME FILE
    
```

Where `NAME` would be the name of the user and `FILE` would be the name for the existing users file.

Once you have run the command, the user of the specified name will be removed from the existing users file.

```text
    
    ./bin/jargyle --socks5-userpassauth-users remove-user Jafar users.xml
    User 'Jafar' removed
    Writing to 'users.xml'...
    
```

`users.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
    </users>
    
```

### 5. 11. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `socks5.methods` as a space separated list.

Partial command line example:

```text
    
    "--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>GSSAPI USERNAME_PASSWORD</value>
    </setting>
    
```

If not set, the default value for the setting `socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`

#### 5. 11. 1. Using No Authentication

Because the default value for the setting `socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `socks5.methods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `socks5.methods`

Partial command line example:

```text
    
    "--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD</value>
    </setting>
    
```

#### 5. 11. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `socks5.methods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```text
    
    --setting=socks5.methods=USERNAME_PASSWORD
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>USERNAME_PASSWORD</value>
    </setting>
    
```

Also, you will need to have the setting `socks5.userpassauth.usernamePasswordAuthenticator` to specify the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator` along with a string value

The following are two provided classes you can use:

-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUsernamePasswordAuthenticator`
-   `com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator`

`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the string value of a space separated list of USERNAME:PASSWORD pairs

Partial command line example:

```text
    
    "--setting=socks5.methods=USERNAME_PASSWORD" \
    "--setting=socks5.userpassauth.usernamePasswordAuthenticator=com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUsernamePasswordAuthenticator:Aladdin:opensesame Jasmine:mission%3Aimpossible"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>USERNAME_PASSWORD</value>
    </setting>
    <setting>
        <name>socks5.userpassauth.usernamePasswordAuthenticator</name>
        <usernamePasswordAuthenticator>
            <className>com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUsernamePasswordAuthenticator</className>
            <value>Aladdin:opensesame Jasmine:mission%3Aimpossible</value>
        </usernamePasswordAuthenticator>
    </setting>
    
```

If any of the usernames or any of the passwords contain a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If any of the usernames or any of the passwords contain a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If any of the usernames or any of the passwords contain a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If any of the usernames or any of the passwords contain a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

`com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the [XML file of users](#5-10-managing-socks5-users-for-username-password-authentication) whose file name is provided as a string value

Partial command line example:

```text
    
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauth.usernamePasswordAuthenticator=com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator:users.xml
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>USERNAME_PASSWORD</value>
    </setting>    
    <setting>
        <name>socks5.userpassauth.usernamePasswordAuthenticator</name>
        <usernamePasswordAuthenticator>
            <className>com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator</className>
            <value>users.xml</value>
        </usernamePasswordAuthenticator>
    </setting>
    
```

#### 5. 11. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `socks5.methods` to have `GSSAPI` included.

Partial command line example:

```text
    
    --setting=socks5.methods=GSSAPI
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <value>GSSAPI</value>
    </setting>
    
```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API).

The following is a sufficient example of using the Kerberos security mechanism:

```bash
    
    export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
    ./bin/jargyle --setting=socks5.methods=GSSAPI 
    
```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file for the underlying security mechanism.

`login.conf`:

```text
    
    com.sun.security.jgss.accept {
      com.sun.security.auth.module.Krb5LoginModule required
      principal="rcmd/127.0.0.1"
      useKeyTab=true
      keyTab="rcmd.keytab"
      storeKey=true;
    };
    
```

In `login.conf`, `rcmd/127.0.0.1` is a service principal that is created by a Kerberos administrator specifically for a SOCKS5 server with the service name `rcmd` residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the name of the machine of where the SOCKS5 server resides.) 

Also in `login.conf`, `rcmd.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned service principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```text
    
    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        udp_preference_limit = 4096
        kdc_tcp_port = 12345
        kdc_udp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)  

### 5. 12. Chaining to Another SOCKS Server

You can have Jargyle chained to another SOCKS server, meaning that its traffic can be routed through another SOCKS server. To have Jargyle chained to another SOCKS server, you will need to specify the other SOCKS server as a URI in the setting `chaining.socksServerUri`

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    
```

Please note that the scheme in the URI specifies the SOCKS protocol to be used when accessing the other SOCKS server (`socks5`), the address or name of the machine of where the other SOCKS server resides (`127.0.0.1`), and the port number of the other SOCKS server (`23456`). In the aforementioned examples, the SOCKS protocol version 5 is used. At this time, the only supported scheme for the URI format is `socks5`

#### 5. 12. 1. Using SSL/TLS for TCP Traffic Through SOCKS Server Chaining

You can use SSL/TLS for TCP traffic through SOCKS server chaining under the following condition: 

-   The other SOCKS server accepts SSL/TLS connections.

By default SSL/TLS for TCP traffic through SOCKS server chaining is disabled. To enable SSL/TLS for TCP traffic through SOCKS server chaining, you will need to have the setting `chaining.ssl.enabled` set to `true`. In addition, you will need to have the setting `chaining.ssl.trustStoreFile` to specify the server's key store file used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.ssl.trustStorePassword` to specify the password for the server's trust store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-ssl-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
    
```

If the other SOCKS server wants the client (Jargyle) to authenticate using SSL/TLS, you will need to have the setting `chaining.ssl.keyStoreFile` to specify the client's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.ssl.keyStorePassword` to specify the password for the client's key store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --setting=chaining.ssl.keyStorePassword=drowssap \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password    
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-ssl-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --enter-chaining-ssl-key-store-pass \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
    
```

#### 5. 12. 2. Using DTLS for UDP Traffic Through SOCKS Server Chaining

You can use DTLS for UDP traffic through SOCKS server chaining under the following condition: 

-   The other SOCKS server accepts DTLS connections.

By default DTLS for UDP traffic through SOCKS server chaining is disabled. To enable DTLS for UDP traffic through SOCKS server chaining, you will need to have the setting `chaining.dtls.enabled` set to `true`. In addition, you will need to have the setting `chaining.dtls.trustStoreFile` to specify the server's key store file used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.dtls.trustStorePassword` to specify the password for the server's trust store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-dtls-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
    
```

If the other SOCKS server wants the client (Jargyle) to authenticate using DTLS, you will need to have the setting `chaining.dtls.keyStoreFile` to specify the client's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.dtls.keyStorePassword` to specify the password for the client's key store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.keyStoreFile=client.jks \
    --setting=chaining.dtls.keyStorePassword=drowssap \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password    
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-dtls-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.keyStoreFile=client.jks \
    --enter-chaining-dtls-key-store-pass \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
    
```

#### 5. 12. 3. Using Host Name Resolution Through SOCKS5 Server Chaining

Before discussing host name resolution through SOCKS5 server chaining, a brief explanation of Jargyle's internals:

Jargyle uses sockets to interact with the external world.

-   Under the CONNECT command, it uses a socket that connects to the desired target server. In this documentation, this socket is called the server-facing socket.
-   Under the BIND command, it uses a socket that listens for an inbound socket. In this documentation, this socket is called the listen socket.
-   Under the UDP ASSOCIATE command, it uses a UDP socket that sends and receives datagram packets to and from server UDP sockets. In this documentation, this UDP socket is called the server-facing UDP socket.

Jargyle also uses a host resolver to resolve host names for the aforementioned sockets and for [the RESOLVE command](#6-3-the-socks5-resolve-command).

When Jargyle is chained to another SOCKS5 server, the aforementioned sockets that Jargyle uses become SOCKS5-enabled, meaning that their traffic is routed through the other SOCKS5 server.

It is similar for the host resolver. When Jargyle is chained to another SOCKS5 server, the host resolver that Jargyle uses becomes SOCKS5-enabled, meaning that it can use the other SOCKS5 server to resolve host names provided that the other SOCKS5 server supports the SOCKS5 RESOLVE command. However, this functionality for the host resolver is disabled by default making the host resolver resolve host names through the local system.

Therefore, default host name resolution through SOCKS5 server chaining is performed but has the following limitations:

Default host name resolution through SOCKS5 server chaining OCCURS ONLY...

-   ...under the CONNECT command when the server-facing socket makes an extemporaneous outbound connection. Preparation is omitted for the server-facing socket. Such preparation includes applying the specified socket settings for the server-facing socket, resolving the target host name before connecting, and setting the specified timeout in milliseconds on waiting for the server-facing socket to connect. The host resolver is not used in resolving the target host name. When the server-facing socket is SOCKS5-enabled, the target host name is resolved by the other SOCKS5 server and not through the local system.

Default host name resolution through SOCKS5 server chaining DOES NOT OCCUR...

-   ...under the CONNECT command when the server-facing socket makes a prepared outbound connection. Preparation for the server-facing socket includes resolving the target host name before connecting. The host resolver is used in resolving the target host name. Because of its default functionality, the host resolver resolves the target host name through the local system.
-   ...under the BIND command when resolving the binding host name for the listen socket. The host resolver is used in resolving the binding host name for the listen socket. Because of its default functionality, the host resolver resolves the binding host name for the listen socket through the local system.
-   ...under the UDP ASSOCIATE command when resolving the host name for an outbound datagram packet. The host resolver is used in resolving the host name for an outbound datagram packet. Because of its default functionality, the host resolver resolves the host name for an outbound datagram packet through the local system.
-   ...under the RESOLVE command when resolving the provided host name. The host resolver is used in resolving the provided host name. Because of its default functionality, the host resolver resolves the provided host name through the local system.

If you prefer to have host name resolution through SOCKS5 server chaining without the aforementioned limitations, you would need to set the setting `chaining.socks5.resolve.useResolveCommand` to `true`. This setting enables the host resolver to use the SOCKS5 RESOLVE command on the other SOCKS5 server to resolve host names. This setting can only be used if the other SOCKS5 server supports the SOCKS5 RESOLVE command.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.resolve.useResolveCommand=true
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socks5.resolve.useResolveCommand</name>
        <value>true</value>
    </setting>
    
```

In addition to using this setting, you can set the setting `socks5.onConnect.prepareServerFacingSocket` to `true` to allow preparation to be performed for the server-facing socket.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.resolve.useResolveCommand=true \
    --setting=socks5.onConnect.prepareServerFacingSocket=true \
    --setting=socks5.onConnect.serverFacingSocketSettings=SO_TIMEOUT=500 \
    --setting=socks5.onConnect.serverFacingConnectTimeout=10000
        
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socks5.resolve.useResolveCommand</name>
        <value>true</value>
    </setting>
    <setting>
        <name>socks5.onConnect.prepareServerFacingSocket</name>
        <value>true</value>
    </setting>
    <setting>
        <name>socks5.onConnect.serverFacingSocketSettings</name>
        <socketSettings>
            <socketSetting>
                <name>SO_TIMEOUT</name>
                <value>500</value>
            </socketSetting>
        </socketSettings>
    </setting>
    <setting>
        <name>socks5.onConnect.serverFacingConnectTimeout</name>
        <value>10000</value>
    </setting>
        
```

#### 5. 12. 4. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from for accessing the other SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `chaining.socks5.methods` as a space separated list.

Partial command line example:

```text
    
    "--setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <value>GSSAPI USERNAME_PASSWORD</value>
    </setting>
    
```

If not set, the default value for the setting `chaining.socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`

##### 5. 12. 4. 1. Using No Authentication

Because the default value for the setting `chaining.socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `chaining.socks5.methods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `chaining.socks5.methods`

Partial command line example:

```text
    
    "--setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <value>NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD</value>
    </setting>
    
```

##### 5. 12. 4. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `chaining.socks5.methods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=USERNAME_PASSWORD
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <value>USERNAME_PASSWORD</value>
    </setting>
    
```

To provide a username and password for the other SOCKS5 server, you can use either of the following command line options:

-   `--setting=chaining.socks5.userpassauth.usernamePassword=USERNAME:PASSWORD`
-   `--enter-chaining-socks5-userpassauth-user-pass`

The command line option `--setting=chaining.socks5.userpassauth.usernamePassword=USERNAME:PASSWORD` requires an actual username followed by a colon character (`:`) followed by an actual password.

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauth.usernamePassword=Aladdin:opensesame
    
```

If the username or the password contains a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If the username or the password contains a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If the username or the password contains a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

The command line option `--enter-chaining-socks5-userpassauth-user-pass` provides an interactive prompt for you to enter the username and password. This command line option is used for when you do not want to have the username and password appear in any script or in any part of the command line history for security reasons.

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --enter-chaining-socks5-userpassauth-user-pass
    
```

##### 5. 12. 4. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `chaining.socks5.methods` to have `GSSAPI` included.

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=GSSAPI
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <value>GSSAPI</value>
    </setting>
    
```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API), and you will also need to specify the GSS-API service name for the other SOCKS5 server.

The following is a sufficient example of using the Kerberos security mechanism:

```bash
    
    export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
    ./bin/jargyle \
        --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
        --setting=chaining.socks5.methods=GSSAPI \
        --setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1 
    
```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file for the underlying security mechanism.

`login.conf`:

```text
    
    com.sun.security.jgss.initiate  {
      com.sun.security.auth.module.Krb5LoginModule required
      principal="alice"
      useKeyTab=true
      keyTab="alice.keytab"
      storeKey=true;
    };
    
```

In `login.conf`, `alice` is a principal that is created by a Kerberos administrator. 

Also in `login.conf`, `alice.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```text
    
    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        udp_preference_limit = 4096
        kdc_tcp_port = 12345
        kdc_udp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)

The command line option `--setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1` is the GSS-API service name (or the Kerberos service principal) for the other SOCKS5 server residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the name of the machine of where the other SOCKS5 server resides.)

### 5. 13. Chaining to a Specified Chain of Other SOCKS Servers

You can have Jargyle chained to a specified chain of other SOCKS servers, meaning that its traffic can be routed through the specified chain of the other SOCKS servers. To have Jargyle chained to a specified chain of other SOCKS servers, you will need to specify each SOCKS server as a URI in its own separate setting of `chaining.socksServerUri`

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:65432</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:34567</value>
    </setting>        
    
```

To specify the settings regarding each SOCKS server in the chain, the settings regarding each SOCKS server will need to be placed after each specified SOCKS server.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567 \
    --setting=chaining.socks5.resolve.useResolveCommand=true
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:65432</value>
    </setting>
    <setting>
        <name>chaining.socks5.methods</name>
        <value>GSSAPI</value>
    </setting>
    <setting>
        <name>chaining.socks5.gssapiauth.serviceName</name>
        <value>rcmd/127.0.0.1</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:34567</value>
    </setting>        
    <setting>
        <name>chaining.socks5.resolve.useResolveCommand</name>
        <value>true</value>
    </setting>    
    
```

The known limitations of Jargyle chained to a specified chain of other SOCKS servers include the following:

-   Only TCP traffic can be routed through the chain. Jargyle will attempt to route any UDP traffic through the last SOCKS server of the chain.

### 5. 14. Allowing or Blocking Addresses

You can allow or block the following addresses:

-   Client addresses (IPv4 and IPv6)
-   Inbound addresses following the SOCKS5 BIND command (IPv4 and IPv6)
-   Inbound addresses following the SOCKS5 UDP ASSOCIATE command (IPv4 and IPv6)
-   Outbound addresses following the SOCKS5 UDP ASSOCIATE command (IPv4, IPv6, and domain name)

To allow or block an address or addresses, you will need to specify the address or addresses in any of the following settings:

-   `allowedClientAddressCriteria`
-   `blockedClientAddressCriteria`
-   `socks5.onBind.allowedInboundAddressCriteria`
-   `socks5.onBind.blockedInboundAddressCriteria`
-   `socks5.onUdpAssociate.allowedInboundAddressCriteria`
-   `socks5.onUdpAssociate.allowedOutboundAddressCriteria`
-   `socks5.onUdpAssociate.blockedInboundAddressCriteria`
-   `socks5.onUdpAssociate.blockedOutboundAddressCriteria`

You can specify an address or addresses in any of the aforementioned settings as a space separated list of each address or addresses as either a literal expression preceded by the prefix `equals:` or a regular expression preceded by the prefix `matches:`.

Partial command line example:

```text
    
    "--setting=allowedClientAddressCriteria=equals:127.0.0.1 equals:0:0:0:0:0:0:0:1" \
    "--setting=blockedClientAddressCriteria=matches:(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1)).*"
    
```

You can specify an address or addresses in any of the aforementioned settings as a sequence of `<criterion/>` XML elements in the configuration file.

Partial configuration file example:

```xml
    
    <setting>
        <name>allowedClientAddressCriteria</name>
        <criteria>
            <criterion method="equals" value="127.0.0.1"/>
            <criterion method="equals" value="0:0:0:0:0:0:0:1"/>
        </criteria>
    </setting>
    <setting>
        <name>blockedClientAddressCriteria</name>
        <criteria>
            <criterion method="matches" value="(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1)).*"/>
        </criteria>
    </setting>        
    
```

### 5. 15. Allowing or Blocking SOCKS5 Requests

You can allow or block SOCKS5 requests. To allow or block SOCKS5 requests, you will need to specify the SOCKS5 request or requests in any of the following settings in the configuration file:

-   `socks5.allowedSocks5RequestCriteria`
-   `socks5.blockedSocks5RequestCriteria`
 
You can specify a SOCKS5 request or requests in any of the aforementioned settings as a sequence of `<socks5RequestCriterion/>` XML elements in the configuration file.

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.allowedSocks5RequestCriteria</name>
        <socks5RequestCriteria>
            <socks5RequestCriterion>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="CONNECT"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="80" maxPort="80"/>
                    <portRange minPort="443" maxPort="443"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>
        </socks5RequestCriteria>
    </setting>
    <setting>
        <name>socks5.blockedSocks5RequestCriteria</name>
        <socks5RequestCriteria>
            <socks5RequestCriterion>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="BIND"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="0" maxPort="65535"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>
            <socks5RequestCriterion>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="UDP_ASSOCIATE"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="0" maxPort="65535"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>                    
        </socks5RequestCriteria>
    </setting>        
    
```

### 5. 16. Logging

Jargyle uses Simple Logging Facade for Java (SLF4J) for logging and uses java.util.logging as its underlying framework. 

In java.util.logging, there are seven logging levels in the order of highest priority to the lowest priority:

-   `SEVERE`
-   `WARNING`
-   `INFO`
-   `CONFIG`
-   `FINE`
-   `FINER`
-   `FINEST`

By default, the current level is set at `INFO` and up. This means that only logging messages of levels `INFO` and up will appear in the output.

The following are the classes that use logging:

-   `com.github.jh3nd3rs0n.jargyle.common.net.RelayServer$DataWorker`
-   `com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocket`
-   `com.github.jh3nd3rs0n.jargyle.main.JargyleCLI`
-   `com.github.jh3nd3rs0n.jargyle.main.XmlFileSourceConfigurationProvider$ConfigurationUpdater`
-   `com.github.jh3nd3rs0n.jargyle.main.socks5.userpassauth.XmlFileSourceUsersProvider$UsersUpdater`
-   `com.github.jh3nd3rs0n.jargyle.server.Listener`
-   `com.github.jh3nd3rs0n.jargyle.server.Worker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.BindCommandWorker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.ConnectCommandWorker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.ResolveCommandWorker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5Worker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.UdpAssociateCommandWorker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.UdpRelayServer$InboundPacketsWorker`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.UdpRelayServer$OutboundPacketsWorker`

To configure logging for any of the aforementioned classes, you can use a configuration file to specify the logging properties for any of the classes.

The following is a configuration file example of setting the logging level of some of the classes to `FINE` and up:

`logging.properties`:

```text
    
    com.github.jh3nd3rs0n.jargyle.main.JargyleCLI.handlers = java.util.logging.ConsoleHandler
    com.github.jh3nd3rs0n.jargyle.main.JargyleCLI.level = FINE    
    com.github.jh3nd3rs0n.jargyle.server.Listener.handlers = java.util.logging.ConsoleHandler
    com.github.jh3nd3rs0n.jargyle.server.Listener.level = FINE    
    com.github.jh3nd3rs0n.jargyle.server.Worker.handlers = java.util.logging.ConsoleHandler
    com.github.jh3nd3rs0n.jargyle.server.Worker.level = FINE
    com.github.jh3nd3rs0n.jargyle.server.socks5.ConnectCommandWorker.handlers = java.util.logging.ConsoleHandler
    com.github.jh3nd3rs0n.jargyle.server.socks5.ConnectCommandWorker.level = FINE
    com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5Worker.handlers = java.util.logging.ConsoleHandler
    com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5Worker.level = FINE
    java.util.logging.ConsoleHandler.level = FINE
    
```

To use the configuration file, you can use the Java system property `java.util.logging.config.file`.

Example:

```bash
    
    export JARGYLE_OPTS="-Djava.util.logging.config.file=logging.properties"
    ./bin/jargyle
    
```

## 6. Miscellaneous Notes

The following are miscellaneous notes regarding Jargyle.

### 6. 1. The Doc XML Attribute and the Doc XML Element

When using an existing configuration file to create a new configuration file, any XML comments from the existing configuration file cannot be transferred to the new configuration file. To preserve comments from one configuration file to the next configuration file, the `doc` XML attribute and the `<doc/>` XML element can be used in certain XML elements. 

You can use the `doc` XML attribute in the following XML elements:

-   `<clientAddressCriterion/>`
-   `<commandCriterion/>`
-   `<criterion/>`
-   `<desiredDestinationAddressCriterion/>`
-   `<portRange/>`

You can use the `<doc/>` XML element in the following XML elements:

-   `<setting/>`
-   `<socketSetting/>`
-   `<socks5RequestCriterion/>`

Partial configuration file example:

```xml
    
    <setting>
        <name>allowedClientAddressCriteria</name>
        <criteria>
            <criterion method="equals" value="127.0.0.1" doc="IPv4 loopback address"/>
            <criterion method="equals" value="0:0:0:0:0:0:0:1" doc="IPv6 loopback address"/>
        </criteria>
    </setting>
    <setting>
        <name>blockedClientAddressCriteria</name>
        <criteria>
            <criterion method="matches" value="(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1)).*" doc="Block any address that is not a loopback address"/>
        </criteria>
    </setting>
    <setting>
        <name>socks5.allowedSocks5RequestCriteria</name>
        <socks5RequestCriteria>
            <socks5RequestCriterion>
                <doc>Allow any client to connect to any address on port 80 or port 443</doc>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="CONNECT"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="80" maxPort="80" doc="HTTP port"/>
                    <portRange minPort="443" maxPort="443" doc="HTTPS port"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>
        </socks5RequestCriteria>
    </setting>
    <setting>
        <name>socks5.blockedSocks5RequestCriteria</name>
        <socks5RequestCriteria>
            <socks5RequestCriterion>
                <doc>Block any BIND requests</doc>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="BIND"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="0" maxPort="65535"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>
            <socks5RequestCriterion>
                <doc>Block any UDP ASSOCIATE requests</doc>
                <clientAddressCriterion method="matches" value=".*"/>
                <commandCriterion method="equals" value="UDP_ASSOCIATE"/>
                <desiredDestinationAddressCriterion method="matches" value=".*"/>
                <desiredDestinationPortRanges>
                    <portRange minPort="0" maxPort="65535"/>
                </desiredDestinationPortRanges>
            </socks5RequestCriterion>                    
        </socks5RequestCriteria>
    </setting>
    
```

### 6. 2. Multiple Settings of the Same Name

Unless otherwise stated, if a setting of the same name appears more than once on the command line or in the configuration file, then only the last setting of the same name is recognized. 

### 6. 3. The SOCKS5 RESOLVE Command

The SOCKS5 RESOLVE command specifies the type of SOCKS5 request sent by the client for the server to perform: to resolve the provided host name and reply with the resolved IPv4 or IPv6 address. At the time of this writing, the SOCKS5 RESOLVE command is an additional SOCKS5 command made for Jargyle. It is not a part of the SOCKS5 protocol specification. 

The following is the specification for defining a SOCKS5 request with the RESOLVE command and the reply to that SOCKS5 request. It is described in expressions, names, and terms that are based off of the SOCKS5 protocol specification described in RFC [1928](https://datatracker.ietf.org/doc/html/rfc1928).

In a SOCKS request, the RESOLVE command is represented as `X'04'` in the `CMD` field.  In the SOCKS request, the `ATYP` field SHOULD be `X'03'` (DOMAINNAME) and the `DST.ADDR` field SHOULD be a fully-qualified domain name with the first octet containing the number of octets of the name that follows. The `DST.PORT` field in the SOCKS request can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive).

In reply to a SOCKS request with the RESOLVE command, the `ATYP` field in the reply MUST be of any value other than `X'03'` (DOMAINNAME) and the `BND.ADDR` field in the reply MUST be the resolved address of the `DST.ADDR` field of the SOCKS request. The `BND.PORT` field in the reply can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive). If the `ATYP` field and the `DST.ADDR` field of the SOCKS request is not a fully-qualified domain name, the `ATYP` field and the `BND.ADDR` field in the reply MUST be the same as the `ATYP` field and the `DST.ADDR` field of the SOCKS request. After the reply is sent, the connection between the client and the server is then closed.

## 7. Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
