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
-   [SOCKS server chaining to multiple specified chains of other SOCKS servers](#5-14-chaining-to-multiple-specified-chains-of-other-socks-servers)
-   [Firewall rules](#5-15-using-firewall-rules) for [clients](#5-15-1-using-firewall-rules-for-clients), [SOCKS5 requests](#5-15-2-using-firewall-rules-for-socks5-requests), [SOCKS5 replies](#5-15-3-using-firewall-rules-for-socks5-replies), and [SOCKS5 UDP traffic](#5-15-4-using-firewall-rules-for-socks5-udp-traffic)

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
-   [5. 14. Chaining to Multiple Specified Chains of Other SOCKS Servers](#5-14-chaining-to-multiple-specified-chains-of-other-socks-servers)
-   [5. 15. Using Firewall Rules](#5-15-using-firewall-rules)
-   [5. 15. 1. Using Firewall Rules for Clients](#5-15-1-using-firewall-rules-for-clients)
-   [5. 15. 2. Using Firewall Rules for SOCKS5 Requests](#5-15-2-using-firewall-rules-for-socks5-requests)
-   [5. 15. 3. Using Firewall Rules for SOCKS5 Replies](#5-15-3-using-firewall-rules-for-socks5-replies)
-   [5. 15. 4. Using Firewall Rules for SOCKS5 UDP Traffic](#5-15-4-using-firewall-rules-for-socks5-udp-traffic)
-   [5. 16. Using Routing Rules](#5-16-using-routing-rules)
-   [5. 16. 1. Using Routing Rules for Clients](#5-16-1-using-routing-rules-for-clients)
-   [5. 16. 2. Using Routing Rules for SOCKS5 Requests](#5-16-2-using-routing-rules-for-socks5-requests)
-   [5. 17. Value Syntax Reference](#5-17-value-syntax-reference)
-   [5. 17. 1. Address Range](#5-17-1-address-range)
-   [5. 17. 2. Firewall Rule Action](#5-17-2-firewall-rule-action)
-   [5. 17. 3. Log Action](#5-17-3-log-action)
-   [5. 17. 4. Port Range](#5-17-4-port-range)
-   [5. 17. 5. Selection Strategy](#5-17-5-selection-strategy)
-   [5. 17. 6. SOCKS5 Command](#5-17-6-socks5-command)
-   [5. 17. 7. SOCKS5 Method](#5-17-7-socks5-method)
-   [5. 18. Miscellaneous Notes](#5-18-miscellaneous-notes)
-   [5. 18. 1. Multiple Settings of the Same Name](#5-18-1-multiple-settings-of-the-same-name)
-   [5. 18. 2. The SOCKS5 RESOLVE Command](#5-18-2-the-socks5-resolve-command)
-   [5. 18. 3. The Doc XML Element](#5-18-3-the-doc-xml-element)
-   [6. Contact](#6-contact)

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
    
    ./bin/jargyle start-server
    
```

The aforementioned command will run Jargyle on port 1080 at address 0.0.0.0 using no authentication.

### 5. 1. Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

```text
    
    Usage: jargyle COMMAND
           jargyle --help
           jargyle --version
    
    COMMANDS:
      manage-socks5-users COMMAND
          Manage SOCKS5 users
      new-server-config-file [OPTIONS] FILE
          Create a new server configuration file based on the provided options
      start-server [OPTIONS] [MONITORED_CONFIG_FILE]
          Start the SOCKS server
    
    OPTIONS:
      --help, -h
          Print this help and exit
      --version, -V
          Print version information and exit
    
    
    
```

The following is the command line help for the command `manage-socks5-users` (displayed when using the command `manage-socks5-users --help`):

```text
    
    Usage: jargyle manage-socks5-users COMMAND
           jargyle manage-socks5-users --help
    
    COMMANDS:
      add-users-to-file FILE
          Add users to an existing file through an interactive prompt
      new-file FILE
          Create a new file of zero or more users through an interactive prompt
      print-xsd
          Print the XSD
      remove-user NAME FILE
          Remove user by name from an existing file
    
    OPTIONS:
      --help, -h
          Print this help and exit
    
    
    
```

The following is the command line help for the command `new-server-config-file` (displayed when using the command `new-server-config-file --help`):

```
    
    Usage: jargyle new-server-config-file [OPTIONS] FILE
           jargyle new-server-config-file --config-file-xsd
           jargyle new-server-config-file --help
           jargyle new-server-config-file --settings-help
    
    OPTIONS:
      --config-file=FILE, -f FILE
          A configuration file
      --config-file-xsd, -x
          Print the configuration file XSD and exit
      --enter-chaining-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the other SOCKS server
      --enter-chaining-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
      --enter-chaining-socks5-userpassauth-pass
          Enter through an interactive prompt the password to be used to access the other SOCKS5 server
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
      --setting=NAME=VALUE, -s NAME=VALUE
          A setting for the SOCKS server
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
    
    
    
```

The following is the command line help for the command `start-server` (displayed when using the command `start-server --help`):

```
    
    Usage: jargyle start-server [OPTIONS] [MONITORED_CONFIG_FILE]
           jargyle start-server --config-file-xsd
           jargyle start-server --help
           jargyle start-server --settings-help
    
    OPTIONS:
      --config-file=FILE, -f FILE
          A configuration file
      --config-file-xsd, -x
          Print the configuration file XSD and exit
      --enter-chaining-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the other SOCKS server
      --enter-chaining-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
      --enter-chaining-socks5-userpassauth-pass
          Enter through an interactive prompt the password to be used to access the other SOCKS5 server
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
      --setting=NAME=VALUE, -s NAME=VALUE
          A setting for the SOCKS server
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
    
    
    
```

The following is a list of available settings for the SOCKS server (displayed when using either the commands `new-server-config-file --settings-help` or `start-server --settings-help`):

```text
    
    SETTINGS:
    
      GENERAL SETTINGS:
    
        backlog=INTEGER_BETWEEN_0_AND_2147483647
            The maximum length of the queue of incoming connections (default is 50)
    
        clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the client-facing socket
    
        clientFirewallRules=[CLIENT_FIREWALL_RULE_FIELD1[ CLIENT_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for TCP traffic from a client to the SOCKS server (default is firewallRuleAction=ALLOW)
    
        clientRoutingRules=[CLIENT_ROUTING_RULE_FIELD1[ CLIENT_ROUTING_RULE_FIELD2[...]]]
            The space separated list of routing rules for a client
    
        host=HOST
            The host name or address for the SOCKS server (default is 0.0.0.0)
    
        lastRouteId=ROUTE_ID
            The ID for the last and unassigned route (default is lastRoute)
    
        port=INTEGER_BETWEEN_0_AND_65535
            The port for the SOCKS server (default is 1080)
    
        routeSelectionLogAction=LOG_ACTION
            The logging action to take if a route is selected from the list of all of the route IDs
    
        routeSelectionStrategy=SELECTION_STRATEGY
            The selection strategy for the next route to use from the list of all of the route IDs (default is CYCLICAL)
    
        socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the SOCKS server
    
      CHAINING GENERAL SETTINGS:
    
        chaining.internalBindHost=HOST
            The binding host name or address for the internal socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0.0.0.0)
    
        chaining.internalConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on waiting for the internal socket to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 60000)
    
        chaining.internalSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the internal socket that is used to connect to the other SOCKS server (used for the SOCKS5 command RESOLVE and UDP ASSOCIATE)
    
        chaining.routeId=ROUTE_ID
            The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers
    
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
    
        chaining.socks5.userpassauth.password=PASSWORD
            The password to be used to access the other SOCKS5 server
    
        chaining.socks5.userpassauth.username=USERNAME
            The username to be used to access the other SOCKS5 server
    
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
    
        socks5.onBind.inboundSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the inbound socket
    
        socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the listen socket
    
        socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data (default is 1024)
    
        socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data (default is 60000)
    
        socks5.onBind.secondSocks5ReplyFirewallRules=[SOCKS5_REPLY_FIREWALL_RULE_FIELD1[ SOCKS5_REPLY_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for a second SOCKS5 reply to a client (default is firewallRuleAction=ALLOW)
    
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
    
        socks5.onUdpAssociate.clientFacingBindHost=HOST
            The binding host name or address for the client-facing UDP socket (default is 0.0.0.0)
    
        socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the client-facing UDP socket
    
        socks5.onUdpAssociate.inboundSocks5UdpFirewallRules=[SOCKS5_UDP_FIREWALL_RULE_FIELD1[ SOCKS5_UDP_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for UDP traffic from a UDP peer to a UDP client (default is firewallRuleAction=ALLOW)
    
        socks5.onUdpAssociate.outboundSocks5UdpFirewallRules=[SOCKS5_UDP_FIREWALL_RULE_FIELD1[ SOCKS5_UDP_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for UDP traffic from a UDP client to a UDP peer (default is firewallRuleAction=ALLOW)
    
        socks5.onUdpAssociate.peerFacingBindHost=HOST
            The binding host name or address for the peer-facing UDP socket (default is 0.0.0.0)
    
        socks5.onUdpAssociate.peerFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
            The space separated list of socket settings for the peer-facing UDP socket
    
        socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data (default is 32768)
    
        socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data (default is 60000)
    
        socks5.socks5ReplyFirewallRules=[SOCKS5_REPLY_FIREWALL_RULE_FIELD1[ SOCKS5_REPLY_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for a SOCKS5 reply to a client (default is firewallRuleAction=ALLOW)
    
        socks5.socks5RequestFirewallRules=[SOCKS5_REQUEST_FIREWALL_RULE_FIELD1[ SOCKS5_REQUEST_FIREWALL_RULE_FIELD2[...]]]
            The space separated list of firewall rules for a SOCKS5 request from a client (default is firewallRuleAction=ALLOW)
    
        socks5.socks5RequestRoutingRules=[SOCKS5_REQUEST_ROUTING_RULE_FIELD1[ SOCKS5_REQUEST_ROUTING_RULE_FIELD2[...]]]
            The space separated list of routing rules for a SOCKS5 request
    
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
    
    SETTING VALUE SYNTAXES:
    
      CLIENT_FIREWALL_RULE_FIELDS:
    
        firewallRuleAction=FIREWALL_RULE_ACTION
            Specifies the action to take. This field starts a new client firewall rule.
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the SOCKS server address
    
        logAction=LOG_ACTION
            Specifies the logging action to take if the firewall rule is applied
    
      CLIENT_ROUTING_RULE_FIELDS:
    
        routingRule=
            This field starts a new client routing rule
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the SOCKS server address
    
        routeId=ROUTE_ID
            Specifies the ID for a route. This field can be specified multiple times for each ID for a route.
    
        routeIdSelectionStrategy=SELECTION_STRATEGY
            Specifies the selection strategy for the next route ID to use from the list of route IDs
    
        logAction=LOG_ACTION
            Specifies the logging action to take if a route is selected from the list of route IDs
    
      FIREWALL_RULE_ACTIONS:
    
        ALLOW
    
        DENY
    
      LOG_ACTIONS:
    
        LOG_AS_WARNING
            Log at the WARNING level
    
        LOG_AS_INFO
            Log at the INFO level
    
      SCHEMES:
    
        socks5
            SOCKS protocol version 5
    
      SELECTION_STRATEGIES:
    
        CYCLICAL
            Select the next in the cycle
    
        RANDOM
            Select the next at random
    
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
    
      SOCKS5_COMMANDS:
    
        CONNECT
            A request to the SOCKS server to connect to another server
    
        BIND
            A request to the SOCKS server to bind to another address and port in order to receive an inbound connection
    
        UDP_ASSOCIATE
            A request to the SOCKS server to associate a UDP socket for sending and receiving datagrams
    
        RESOLVE
            A request to the SOCKS server to resolve a host name
    
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
    
      SOCKS5_REPLY_FIREWALL_RULE_FIELDS:
    
        firewallRuleAction=FIREWALL_RULE_ACTION
            Specifies the action to take. This field starts a new SOCKS5 reply firewall rule.
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the SOCKS server address
    
        method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method
    
        user=USER
            Specifies the user if any after the negotiated SOCKS5 method
    
        command=SOCKS5_COMMAND
            Specifies the SOCKS5 command of the SOCKS5 request
    
        desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the desired destination address of the SOCKS5 request
    
        desiredDestinationPortRange=PORT|PORT1-PORT2
            Specifies the port range for the desired destination port of the SOCKS5 request
    
        serverBoundAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the server bound address of the SOCKS5 reply
    
        serverBoundPortRange=PORT|PORT1-PORT2
            Specifies the port range for the server bound port of the SOCKS5 reply
    
        logAction=LOG_ACTION
            Specifies the logging action to take if the firewall rule is applied
    
      SOCKS5_REQUEST_FIREWALL_RULE_FIELDS:
    
        firewallRuleAction=FIREWALL_RULE_ACTION
            Specifies the action to take. This field starts a new SOCKS5 request firewall rule.
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the SOCKS server address
    
        method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method
    
        user=USER
            Specifies the user if any after the negotiated SOCKS5 method
    
        command=SOCKS5_COMMAND
            Specifies the SOCKS5 command of the SOCKS5 request
    
        desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the desired destination address of the SOCKS5 request
    
        desiredDestinationPortRange=PORT|PORT1-PORT2
            Specifies the port range for the desired destination port of the SOCKS5 request
    
        logAction=LOG_ACTION
            Specifies the logging action to take if the firewall rule is applied
    
      SOCKS5_REQUEST_ROUTING_RULE_FIELDS:
    
        routingRule=
            This field starts a new SOCKS5 request routing rule
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the SOCKS server address
    
        method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method
    
        user=USER
            Specifies the user if any after the negotiated SOCKS5 method
    
        command=SOCKS5_COMMAND
            Specifies the SOCKS5 command of the SOCKS5 request
    
        desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the desired destination address of the SOCKS5 request
    
        desiredDestinationPortRange=PORT|PORT1-PORT2
            Specifies the port range for the desired destination port of the SOCKS5 request
    
        routeId=ROUTE_ID
            Specifies the ID for a route. This field can be specified multiple times for each ID for a route.
    
        routeIdSelectionStrategy=SELECTION_STRATEGY
            Specifies the selection strategy for the next route ID to use from the list of route IDs
    
        logAction=LOG_ACTION
            Specifies the logging action to take if a route is selected from the list of route IDs
    
      SOCKS5_UDP_FIREWALL_RULE_FIELDS:
    
        firewallRuleAction=FIREWALL_RULE_ACTION
            Specifies the action to take. This field starts a new SOCKS5 UDP firewall rule.
    
        clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the client address
    
        method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method
    
        user=USER
            Specifies the user if any after the negotiated SOCKS5 method
    
        peerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the address range for the peer address
    
        logAction=LOG_ACTION
            Specifies the logging action to take if the firewall rule is applied
    
    
    
    
```

### 5. 2. Creating a Configuration File

You can create a configuration file by using the command `new-server-config-file`

The following command creates an empty configuration file:

```bash
    
    ./bin/jargyle new-server-config-file empty_configuration.xml
    
```

`empty_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings/>
    </configuration>
    
```

The following command creates a configuration file with the port number, the number of allowed backlogged connections, and no authentication required:

```bash
    
    ./bin/jargyle new-server-config-file \
        --setting=port=1234 \
        --setting=backlog=100 \
        --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
        configuration.xml
    
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
    
    ./bin/jargyle new-server-config-file \
        --config-file=configuration.xml \
        --setting=socketSettings=SO_TIMEOUT=0 \
        supplemented_configuration.xml
    
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
    
    ./bin/jargyle new-server-config-file \
        --config-file=configuration.xml \
        --config-file=supplemented_configuration.xml \
        combined_configuration.xml
    
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

Although the redundant settings in the combined configuration file are unnecessary, the result configuration file is for demonstration purposes only. (See [Multiple Settings of the Same Name](#5-18-1-multiple-settings-of-the-same-name) for more information.)

### 5. 5. Running With a Configuration File

To run Jargyle with a configuration file, you can use the following command:

```bash
    
    ./bin/jargyle start-server --config-file=configuration.xml
    
```

Also the configuration file can be supplemented with command line options and/or can be combined with multiple configuration files.

### 5. 6. Running With a Monitored Configuration File

You can run Jargyle with a configuration file to be monitored for any changes to be applied to the running configuration.

To run Jargyle with a monitored configuration file, you can use the following command:

```bash
    
    ./bin/jargyle start-server configuration.xml
    
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
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --enter-ssl-key-store-pass
    
```

If you want to have the client authenticate using SSL/TLS, you will need to have the setting `ssl.needClientAuth` set to `true`. In addition, you will need to have the setting `ssl.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `ssl.trustStorePassword` to specify the password for the client's trust store file.

```text
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password \
        --setting=ssl.needClientAuth=true \
        --setting=ssl.trustStoreFile=client.jks \
        --setting=ssl.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle start-server \
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
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass
    
```

If you want to have the client authenticate using DTLS, you will need to have the setting `dtls.needClientAuth` set to `true`. In addition, you will need to have the setting `dtls.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `dtls.trustStorePassword` to specify the password for the client's trust store file.

```text
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --setting=dtls.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```text
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --enter-dtls-trust-store-pass
    
```

**NOTE**: For a DTLS connection to be established, the client must send a SOCKS5 UDP ASSOCIATE command with a port number of a value greater than zero that the client expects to send and receive datagram packets. If the client sends a SOCKS5 UDP ASSOCIATE command with a port number of zero, a DTLS connection will not be established.

### 5. 9. Using Host Name Resolution

Jargyle can be used to resolve host names. For a client to request Jargyle to resolve a host name, it must use [the SOCKS5 RESOLVE command](#5-18-2-the-socks5-resolve-command).

### 5. 10. Managing SOCKS5 Users (for Username Password Authentication)

You can manage SOCKS5 users stored in an XML file called a users file. A users file can be used for [username password authentication](#5-11-2-using-username-password-authentication).

#### 5. 10. 1. Creating a Users File

To create a users file, you would run the following command:

```bash
    
    ./bin/jargyle manage-socks5-users create-new-file FILE
    
```

Where `FILE` would be the name for the new users file.

Once you have run the command, an interactive prompt will ask you if you want to enter a user.

```text
    
    ./bin/jargyle manage-socks5-users create-new-file users.xml
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
    
    ./bin/jargyle manage-socks5-users add-users-to-file FILE
    
```

Where `FILE` would be the name for the existing users file.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you do not want to enter any more users, the updated users file will be saved.

```text
    
    ./bin/jargyle manage-socks5-users add-users-to-file users.xml
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
    
    ./bin/jargyle manage-socks5-users remove-user NAME FILE
    
```

Where `NAME` would be the name of the user and `FILE` would be the name for the existing users file.

Once you have run the command, the user of the specified name will be removed from the existing users file.

```text
    
    ./bin/jargyle manage-socks5-users remove-user Jafar users.xml
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
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator`

`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the string value of a space separated list of username password values.

Each username password value in the space separated list must be of the following format:

```text
    
    USERNAME:PASSWORD
    
```

Where `USERNAME` is the username and `PASSWORD` is the password.

If the username or the password contains a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If the username or the password contains a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If the username or the password contains a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

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

`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the [XML file of users](#5-10-managing-socks5-users-for-username-password-authentication) whose file name is provided as a string value.

Partial command line example:

```text
    
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauth.usernamePasswordAuthenticator=com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator:users.xml
    
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
            <className>com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.XmlFileSourceUsernamePasswordAuthenticator</className>
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
    ./bin/jargyle start-server --setting=socks5.methods=GSSAPI 
    
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
-   Under the UDP ASSOCIATE command, it uses a UDP socket that sends and receives datagram packets to and from peer UDP sockets. In this documentation, this UDP socket is called the peer-facing UDP socket.

Jargyle also uses a host resolver to resolve host names for the aforementioned sockets and for [the RESOLVE command](#5-18-2-the-socks5-resolve-command).

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

Also, you will need to have the settings `chaining.socks5.userpassauth.username` and `chaining.socks5.userpassauth.password` respectively specify the username and password for the other SOCKS5 server.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauth.username=Aladdin \
    --setting=chaining.socks5.userpassauth.password=opensesame
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-socks5-userpassauth-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauth.username=Aladdin \
    --enter-chaining-socks5-userpassauth-pass
    
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
    ./bin/jargyle start-server \
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

### 5. 14. Chaining to Multiple Specified Chains of Other SOCKS Servers

You can have Jargyle chained to multiple specified chains of other SOCKS servers, meaning that its traffic can be routed through one of the specified chains of other SOCKS servers. To have Jargyle chained to multiple specified chains of other SOCKS servers, you will need to have a route ID assigned at the end of each [chain](#5-13-chaining-to-a-specified-chain-of-other-socks-servers) by using the setting `chaining.routeId`

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11111 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11112 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22221 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22222 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33331 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33332 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33333 \
    --setting=chaining.routeId=delta
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11111</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11112</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11113</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>alpha</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22221</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22222</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22223</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>beta</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33331</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33332</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33333</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>delta</value>
    </setting>
    
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, `socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, `socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, `socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned the route ID of `delta`

There is another route that is assigned a route ID. That route is the direct route. The direct route uses no chain to route the traffic through. It is assigned by default a route ID of `lastRoute`.

To omit the direct route from being included, have the last chain not assigned a route ID from the setting `chaining.routeId`.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11111 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11112 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22221 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22222 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33331 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33332 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33333
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11111</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11112</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11113</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>alpha</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22221</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22222</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22223</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>beta</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33331</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33332</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33333</value>
    </setting>
    
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, `socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, `socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, `socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned by default the route ID of `lastRoute`

To change the route ID assigned to the last route, you can set the setting `lastRouteId` to the route ID you want assigned to the last route.

Partial command line example:

```text
    
    --setting=lastRouteId=omega
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>lastRouteId</name>
        <value>omega</value>
    </setting>
    
```

You can also set the setting `routeSelectionStrategy` to specify the [selection strategy](#5-17-5-selection-strategy) for the next route to use from the list of all of the route IDs. The default is `CYCLICAL`.

Partial command line example:

```text
    
    --setting=routeSelectionStrategy=RANDOM
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>routeSelectionStrategy</name>
        <value>RANDOM</value>
    </setting>
    
```

You can also set the setting `routeSelectionLogAction` to specify the [logging action](#5-17-3-log-action) to take if a route is selected from the list of all of the route IDs.

Partial command line example:

```text
    
    --setting=routeSelectionLogAction=LOG_AS_INFO
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>routeSelectionLogAction</name>
        <value>LOG_AS_INFO</value>
    </setting>
    
```

### 5. 15. Using Firewall Rules

You can specify firewall rules for traffic of the following types:

-   TCP traffic from a client to the SOCKS server
-   A SOCKS5 request from a client
-   A SOCKS5 reply to a client
-   SOCKS5 UDP traffic between a UDP client and a UDP peer

On the command line, firewall rules for traffic of a particular type consist of a space separated list of fields. Each field consists of the syntax of `NAME=VALUE` where `NAME` is expressed as the name of the field and `VALUE` is expressed as the value assigned to the field.

The following are common firewall rule fields:

-   `firewallRuleAction` : Specifies the [action](#5-17-2-firewall-rule-action) to take. This field is required. This field also starts a new firewall rule.
-   `logAction` : Specifies the [logging action](#5-17-3-log-action) to take if the firewall rule is applied. This field is optional.

By default, firewall rules for traffic of a particular type have the default firewall rule:

```text
    
    firewallRuleAction=ALLOW
    
```

The aforementioned default firewall rule allows all traffic of a particular type.

When a particular instance of traffic of a particular type is matched by the first firewall rule in the space separated list, that firewall rule is applied and the rest of the firewall rules in the space separated list are ignored. Therefore it is best to have more specific firewall rules first in the space separated list and have less specific firewall rules last in the space separated list.

#### 5. 15. 1. Using Firewall Rules for Clients

You can specify firewall rules for TCP traffic from a client to the SOCKS server in the following setting:

-   `clientFirewallRules`

A client firewall rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `socksServerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the SOCKS server address. This field is optional.

Partial command line example:

```text
    
    "--setting=clientFirewallRules=firewallRuleAction=ALLOW clientAddressRange=127.0.0.1 firewallRuleAction=ALLOW clientAddressRange=::1 firewallRuleAction=DENY"
    
```

You can also specify the firewall rules in the aforementioned setting as a `<clientFirewallRules/>` XML element containing a sequence of `<clientFirewallRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>clientFirewallRules</name>
        <clientFirewallRules>
            <clientFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <clientAddressRange>127.0.0.1</clientAddressRange>
                <!-- Allows IPv4 loopback address -->
            </clientFirewallRule>
            <clientFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <clientAddressRange>::1</clientAddressRange>
                <!-- Allows IPv6 loopback address -->
            </clientFirewallRule>
            <clientFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <!-- Denies any other client address -->
            </clientFirewallRule>
        </clientFirewallRules>
    </setting>
    
```

#### 5. 15. 2. Using Firewall Rules for SOCKS5 Requests

You can specify firewall rules for a SOCKS5 request from a client in the following setting:

-   `socks5.socks5RequestFirewallRules`

A SOCKS5 request firewall rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `socksServerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the SOCKS server address. This field is optional.
-   `method` : Specifies the negotiated [SOCKS5 method](#5-17-7-socks5-method). This field is optional.
-   `user` : Specifies the user if any after the negotiated SOCKS5 method. This field is optional.
-   `command` : Specifies the [SOCKS5 command](#5-17-6-socks5-command) of the SOCKS5 request. This field is optional.
-   `desiredDestinationAddressRange` : Specifies the [address range](#5-17-1-address-range) for the desired destination address of the SOCKS5 request. This field is optional.
-   `desiredDestinationPortRange` : Specifies the [port range](#5-17-4-port-range) for the desired destination port of the SOCKS5 request. This field is optional.

Partial command line example:

```text
    
    "--setting=socks5.socks5RequestFirewallRules=firewallRuleAction=ALLOW command=CONNECT desiredDestinationPortRange=80 firewallRuleAction=ALLOW command=CONNECT desiredDestinationPortRange=443 firewallRuleAction=DENY command=CONNECT firewallRuleAction=ALLOW"
     
```
 
You can also specify the firewall rules in the aforementioned setting as a `<socks5RequestFirewallRules/>` XML element containing a sequence of `<socks5RequestFirewallRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.socks5RequestFirewallRules</name>
        <socks5RequestFirewallRules>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <command>CONNECT</command>
                <desiredDestinationPortRange>80</desiredDestinationPortRange>
                <!-- Allow any client to connect to any address on port 80 -->
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <command>CONNECT</command>
                <desiredDestinationPortRange>443</desiredDestinationPortRange>
                <!-- Allow any client to connect to any address on port 443 -->
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <!-- Deny any other CONNECT requests -->                
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <!-- Allow any other requests -->
            </socks5RequestFirewallRule>
        </socks5RequestFirewallRules>
    </setting>
    
```

#### 5. 15. 3. Using Firewall Rules for SOCKS5 Replies

You can specify firewall rules for a SOCKS5 reply to a client in either of the following settings:

-   `socks5.onBind.secondSocks5ReplyFirewallRules`
-   `socks5.socks5ReplyFirewallRules`

A SOCKS5 reply firewall rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `socksServerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the SOCKS server address. This field is optional.
-   `method` : Specifies the negotiated [SOCKS5 method](#5-17-7-socks5-method). This field is optional.
-   `user` : Specifies the user if any after the negotiated SOCKS5 method. This field is optional.
-   `command` : Specifies the [SOCKS5 command](#5-17-6-socks5-command) of the SOCKS5 request. This field is optional.
-   `desiredDestinationAddressRange` : Specifies the [address range](#5-17-1-address-range) for the desired destination address of the SOCKS5 request. This field is optional.
-   `desiredDestinationPortRange` : Specifies the [port range](#5-17-4-port-range) for the desired destination port of the SOCKS5 request. This field is optional.
-   `serverBoundAddressRange` : Specifies the [address range](#5-17-1-address-range) for the server bound address of the SOCKS5 reply. This field is optional.
-   `serverBoundPortRange` : Specifies the [port range](#5-17-4-port-range) for the server bound port of the SOCKS5 reply. This field is optional.

Partial command line example:

```text
    
    "--setting=socks5.socks5ReplyFirewallRules=firewallRuleAction=DENY command=CONNECT serverBoundAddressRange=127.0.0.1 firewallRuleAction=DENY command=CONNECT serverBoundAddressRange=::1 firewallRuleAction=ALLOW"
     
```
 
You can also specify the firewall rules in any of the aforementioned settings as a `<socks5ReplyFirewallRules/>` XML element containing a sequence of `<socks5ReplyFirewallRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.socks5ReplyFirewallRules</name>
        <socks5ReplyFirewallRules>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <serverBoundAddressRange>127.0.0.1</serverBoundAddressRange>
                <!-- Deny replies to connecting to the IPv4 loopback address -->
            </socks5ReplyFirewallRule>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <serverBoundAddressRange>::1</serverBoundAddressRange>
                <!-- Deny replies to connecting to the IPv6 loopback address -->                
            </socks5ReplyFirewallRule>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <!-- Allow any other reply -->
            </socks5ReplyFirewallRule>
        </socks5ReplyFirewallRules>
    </setting>
    
```

#### 5. 15. 4. Using Firewall Rules for SOCKS5 UDP Traffic

You can specify firewall rules for UDP traffic between a UDP client and a UDP peer in either of the following settings:

-   `socks5.onUdpAssociate.inboundSocks5UdpFirewallRules`
-   `socks5.onUdpAssociate.outboundSocks5UdpFirewallRules`

A SOCKS5 UDP firewall rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `method` : Specifies the negotiated [SOCKS5 method](#5-17-7-socks5-method). This field is optional.
-   `user` : Specifies the user if any after the negotiated SOCKS5 method. This field is optional.
-   `peerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the peer address. This field is optional.

Partial command line example:

```text
    
    "--setting=socks5.onUdpAssociate.outboundSocks5UdpFirewallRules=firewallRuleAction=DENY peerAddressRange=badpeer.com firewallRuleAction=ALLOW"
     
```
 
You can also specify the firewall rules in any of the aforementioned settings as a `<socks5UdpFirewallRules/>` XML element containing a sequence of `<socks5UdpFirewallRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.onUdpAssociate.outboundSocks5UdpFirewallRules</name>
        <socks5UdpFirewallRules>
            <socks5UdpFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <peerAddressRange>badpeer.com</peerAddressRange>
                <!-- Deny any UDP packets that go to badpeer.com -->
            </socks5UdpFirewallRule>
            <socks5UdpFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <!-- Allow any other UDP packets -->
            </socks5UdpFirewallRule>
        </socks5UdpFirewallRules>
    </setting>
    
```

### 5. 16. Using Routing Rules

You can specify routing rules for the following:

-   Clients
-   SOCKS5 requests

On the command line, routing rules consist of a space separated list of fields. Each field consists of the syntax of `NAME=VALUE` where `NAME` is expressed as the name of the field and `VALUE` is expressed as the value assigned to the field.

The following are common routing rule fields:

-   `routingRule` : This field starts a new routing rule. No value needs to specified (the value can be empty). This field is required.
-   `routeId` : Specifies the [ID for a route](#5-14-chaining-to-multiple-specified-chains-of-other-socks-servers). This field can be specified multiple times for each ID for a route. This field is optional.
-   `routeIdSelectionStrategy` : Specifies the [selection strategy](#5-17-5-selection-strategy) for the next route ID to use from the list of route IDs. This field is optional.
-   `logAction` : Specifies the [logging action](#5-17-3-log-action) to take if the route ID is selected from the list of route IDs. This field is optional.

When a particular instance of traffic of a particular type is matched by the first routing rule in the space separated list, that routing rule will select a route and the rest of the routing rules in the space separated list are ignored. Therefore it is best to have more specific routing rules first in the space separated list and have less specific routing rules last in the space separated list.

If no particular instance of traffic of a particular type is matched by a routing rule, a selected route outside the space separated list will be used.

#### 5. 16. 1. Using Routing Rules for Clients

You can specify routing rules for clients in the following setting:

-   `clientRoutingRules`

A client routing rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `socksServerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the SOCKS server address. This field is optional.

Partial command line example:

```text
    
    "--setting=clientRoutingRules=routingRule= clientAddressRange=127.0.0.1 routeId=alpha routeId=beta routeIdSelectionStrategy=RANDOM routingRule= clientAddressRange=::1 routeId=alpha routeId=beta routeIdSelectionStrategy=RANDOM"
    
```

You can also specify the routing rules in the aforementioned setting as a `<clientRoutingRules/>` XML element containing a sequence of `<clientRoutingRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>clientRoutingRules</name>
        <clientRoutingRules>
            <clientRoutingRule>
                <clientAddressRange>127.0.0.1</clientAddressRange>
                <routeIds>
                    <routeId>alpha</routeId>
                    <routeId>beta</routeId>
                </routeIds>
                <routeIdSelectionStrategy>RANDOM</routeIdSelectionStrategy>
                <!-- Traffic for the IPv4 loopback address will be routed through either alpha or beta -->
            </clientRoutingRule>
            <clientRoutingRule>
                <clientAddressRange>::1</clientAddressRange>
                <routeIds>
                    <routeId>alpha</routeId>
                    <routeId>beta</routeId>
                </routeIds>
                <routeIdSelectionStrategy>RANDOM</routeIdSelectionStrategy>
                <!-- Traffic for the IPv6 loopback address will be routed through either alpha or beta -->
            </clientRoutingRule>
            <!-- A selected route outside this sequence will be used for any other address -->
        </clientRoutingRules>
    </setting>
    
```

#### 5. 16. 2. Using Routing Rules for SOCKS5 Requests

You can specify routing rules for SOCKS5 requests in the following setting:

-   `socks5.socks5RequestRoutingRules`

A SOCKS5 request routing rule has the following additional fields:

-   `clientAddressRange` : Specifies the [address range](#5-17-1-address-range) for the client address. This field is optional.
-   `socksServerAddressRange` : Specifies the [address range](#5-17-1-address-range) for the SOCKS server address. This field is optional.
-   `method` : Specifies the negotiated [SOCKS5 method](#5-17-7-socks5-method). This field is optional.
-   `user` : Specifies the user if any after the negotiated SOCKS5 method. This field is optional.
-   `command` : Specifies the [SOCKS5 command](#5-17-6-socks5-command) of the SOCKS5 request. This field is optional.
-   `desiredDestinationAddressRange` : Specifies the [address range](#5-17-1-address-range) for the desired destination address of the SOCKS5 request. This field is optional.
-   `desiredDestinationPortRange` : Specifies the [port range](#5-17-4-port-range) for the desired destination port of the SOCKS5 request. This field is optional.

Partial command line example:

```text
    
    "--setting=socks5.socks5RequestRoutingRules=routingRule= command=CONNECT desiredDestinationPortRange=80 routeId=omega routingRule= command=CONNECT desiredDestinationPortRange=443 routeId=omega"
     
```
 
You can also specify the routing rules in the aforementioned setting as a `<socks5RequestRoutingRules/>` XML element containing a sequence of `<socks5RequestRoutingRule/>` XML elements.

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.socks5RequestRoutingRules</name>
        <socks5RequestRoutingRules>
            <socks5RequestRoutingRule>
                <command>CONNECT</command>
                <desiredDestinationPortRange>80</desiredDestinationPortRange>
                <routeIds>
                    <routeId>omega</routeId>
                </routeIds>
                <!-- Traffic for the CONNECT command for port 80 will be will be routed through omega --> 
            </socks5RequestRoutingRule>
            <socks5RequestRoutingRule>
                <command>CONNECT</command>
                <desiredDestinationPortRange>443</desiredDestinationPortRange>
                <routeIds>
                    <routeId>omega</routeId>
                </routeIds>
                <!-- Traffic for the CONNECT command for port 443 will be will be routed through omega --> 
            </socks5RequestRoutingRule>
            <!-- A selected route outside this sequence will be used for any other SOCKS5 request -->            
        </socks5RequestRoutingRules>
    </setting>
    
```

### 5. 17. Value Syntax Reference

#### 5. 17. 1. Address Range

An address range can be specified in the following formats:

-   `ADDRESS` : Range is limited to a single address expressed in `ADDRESS`. Address can be an IPv4 address, an IPv6 address, or a domain name.
-   `ADDRESS1-ADDRESS2` : Range is limited to addresses between the address expressed in `ADDRESS1` (inclusive) and the address expressed in `ADDRESS2` (inclusive). `ADDRESS1` and `ADDRESS2` must be of the same address type (IPv4 or IPv6). `ADDRESS1` and `ADDRESS2` cannot be domain names.
-   `regex:REGULAR_EXPRESSION` : Range is limited to domain names that match the regular expression expressed in `REGULAR_EXPRESSION`

#### 5. 17. 2. Firewall Rule Action

A firewall rule action can be one of the following values:

-   `ALLOW`
-   `DENY`

#### 5. 17. 3. Log Action

A log action can be one of the following values:

-   `LOG_AS_WARNING` : Log at the WARNING level
-   `LOG_AS_INFO` : Log at the INFO level

#### 5. 17. 4. Port Range

A port range can be specified in the following formats:

-   `PORT` : Range is limited to a single port number expressed in `PORT`
-   `PORT1-PORT2` : Range is limited to port numbers between the port number expressed in `PORT1` (inclusive) and the port number expressed in `PORT2` (inclusive)

#### 5. 17. 5. Selection Strategy

A selection strategy can be one of the following values:

-   `CYCLICAL` : Select the next in the cycle
-   `RANDOM` : Select the next at random

#### 5. 17. 6. SOCKS5 Command

A SOCKS5 command can be one of the following values:

-   `CONNECT`
-   `BIND`
-   `UDP_ASSOCIATE`
-   `RESOLVE`

#### 5. 17. 7. SOCKS5 Method

A SOCKS5 method can be one of the following values:

-   `NO_AUTHENTICATION_REQUIRED`
-   `USERNAME_PASSWORD`
-   `GSSAPI`

### 5. 18. Miscellaneous Notes

The following are miscellaneous notes regarding Jargyle.

#### 5. 18. 1. Multiple Settings of the Same Name

Unless otherwise stated, if a setting of the same name appears more than once on the command line or in the configuration file, then only the last setting of the same name is recognized. 

#### 5. 18. 2. The SOCKS5 RESOLVE Command

The SOCKS5 RESOLVE command specifies the type of SOCKS5 request sent by the client for the server to perform: to resolve the provided host name and reply with the resolved IPv4 or IPv6 address. At the time of this writing, the SOCKS5 RESOLVE command is an additional SOCKS5 command made for Jargyle. It is not a part of the SOCKS5 protocol specification. 

The following is the specification for defining a SOCKS5 request with the RESOLVE command and the reply to that SOCKS5 request. It is described in expressions, names, and terms that are based off of the SOCKS5 protocol specification described in RFC [1928](https://datatracker.ietf.org/doc/html/rfc1928).

In a SOCKS request, the RESOLVE command is represented as `X'04'` in the `CMD` field.  In the SOCKS request, the `ATYP` field SHOULD be `X'03'` (DOMAINNAME) and the `DST.ADDR` field SHOULD be a fully-qualified domain name with the first octet containing the number of octets of the name that follows. The `DST.PORT` field in the SOCKS request can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive).

In reply to a SOCKS request with the RESOLVE command, the `ATYP` field in the reply MUST be of any value other than `X'03'` (DOMAINNAME) and the `BND.ADDR` field in the reply MUST be the resolved address of the `DST.ADDR` field of the SOCKS request. The `BND.PORT` field in the reply can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive). If the `ATYP` field and the `DST.ADDR` field of the SOCKS request is not a fully-qualified domain name, the `ATYP` field and the `BND.ADDR` field in the reply MUST be the same as the `ATYP` field and the `DST.ADDR` field of the SOCKS request. After the reply is sent, the connection between the client and the server is then closed.

#### 5. 18. 3. The Doc XML Element

When using an existing configuration file to create a new configuration file, any XML comments from the existing configuration file cannot be transferred to the new configuration file. To preserve XML comments from one configuration file to the next configuration file, the `<doc/>` XML element can be used in the following XML elements:

-   `<clientFirewallRule/>`
-   `<clientRoutingRule/>`
-   `<setting/>`
-   `<socketSetting/>`
-   `<socks5ReplyFirewallRule/>`
-   `<socks5RequestFirewallRule/>`
-   `<socks5RequestRoutingRule/>`
-   `<socks5UdpFirewallRule/>`

Partial configuration file example:

```xml
    
    <setting>
        <name>clientFirewallRules</name>
        <clientFirewallRules>
            <clientFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <clientAddressRange>127.0.0.1</clientAddressRange>
                <doc>Allows IPv4 loopback address</doc>
            </clientFirewallRule>
            <clientFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <clientAddressRange>::1</clientAddressRange>
                <doc>Allows IPv6 loopback address</doc>
            </clientFirewallRule>
            <clientFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <doc>Denies any other client address</doc>
            </clientFirewallRule>
        </clientFirewallRules>
    </setting>
    <setting>
        <name>socks5.socks5RequestFirewallRules</name>
        <socks5RequestFirewallRules>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <command>CONNECT</command>
                <desiredDestinationPortRange>80</desiredDestinationPortRange>
                <doc>Allow any client to connect to any address on port 80</doc>
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <command>CONNECT</command>
                <desiredDestinationPortRange>443</desiredDestinationPortRange>
                <doc>Allow any client to connect to any address on port 443</doc>
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <doc>Deny any other CONNECT requests</doc>                
            </socks5RequestFirewallRule>
            <socks5RequestFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <doc>Allow any other requests</doc>
            </socks5RequestFirewallRule>
        </socks5RequestFirewallRules>
    </setting>
    <setting>
        <name>socks5.socks5ReplyFirewallRules</name>
        <socks5ReplyFirewallRules>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <serverBoundAddressRange>127.0.0.1</serverBoundAddressRange>
                <doc>Deny replies to connecting to the IPv4 loopback address</doc>
            </socks5ReplyFirewallRule>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <command>CONNECT</command>
                <serverBoundAddressRange>::1</serverBoundAddressRange>
                <doc>Deny replies to connecting to the IPv6 loopback address</doc>                
            </socks5ReplyFirewallRule>
            <socks5ReplyFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <doc>Allow any other reply</doc>
            </socks5ReplyFirewallRule>
        </socks5ReplyFirewallRules>
    </setting>
    <setting>
        <name>socks5.onUdpAssociate.outboundSocks5UdpFirewallRules</name>
        <socks5UdpFirewallRules>
            <socks5UdpFirewallRule>
                <firewallRuleAction>DENY</firewallRuleAction>
                <peerAddressRange>badpeer.com</peerAddressRange>
                <doc>Deny any UDP packets that go to badpeer.com</doc>
            </socks5UdpFirewallRule>
            <socks5UdpFirewallRule>
                <firewallRuleAction>ALLOW</firewallRuleAction>
                <doc>Allow any other UDP packets</doc>
            </socks5UdpFirewallRule>
        </socks5UdpFirewallRules>
    </setting>
    <setting>
        <name>clientRoutingRules</name>
        <clientRoutingRules>
            <clientRoutingRule>
                <clientAddressRange>127.0.0.1</clientAddressRange>
                <routeIds>
                    <routeId>alpha</routeId>
                    <routeId>beta</routeId>
                </routeIds>
                <routeIdSelectionStrategy>RANDOM</routeIdSelectionStrategy>
                <doc>Traffic for the IPv4 loopback address will be routed through either alpha or beta</doc>
            </clientRoutingRule>
            <clientRoutingRule>
                <clientAddressRange>::1</clientAddressRange>
                <routeIds>
                    <routeId>alpha</routeId>
                    <routeId>beta</routeId>
                </routeIds>
                <routeIdSelectionStrategy>RANDOM</routeIdSelectionStrategy>
                <doc>Traffic for the IPv6 loopback address will be routed through either alpha or beta</doc>
            </clientRoutingRule>
        </clientRoutingRules>
    </setting>
    <setting>
        <name>socks5.socks5RequestRoutingRules</name>
        <socks5RequestRoutingRules>
            <socks5RequestRoutingRule>
                <command>CONNECT</command>
                <desiredDestinationPortRange>80</desiredDestinationPortRange>
                <routeIds>
                    <routeId>omega</routeId>
                </routeIds>
                <doc>Traffic for the CONNECT command for port 80 will be will be routed through omega</doc> 
            </socks5RequestRoutingRule>
            <socks5RequestRoutingRule>
                <command>CONNECT</command>
                <desiredDestinationPortRange>443</desiredDestinationPortRange>
                <routeIds>
                    <routeId>omega</routeId>
                </routeIds>
                <doc>Traffic for the CONNECT command for port 443 will be will be routed through omega</doc> 
            </socks5RequestRoutingRule>            
        </socks5RequestRoutingRules>
    </setting>
    
```

## 6. Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
