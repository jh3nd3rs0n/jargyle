# Jargyle 

[![Build Status](https://travis-ci.com/jh3nd3rs0n/jargyle.svg?branch=master)](https://travis-ci.com/jh3nd3rs0n/jargyle) [![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/a444bdeb2d844ab586e01056384b432f)](https://www.codacy.com/manual/jh3nd3rs0n/jargyle?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

Jargyle is a Java SOCKS5 server. It has the following features:

-   It is a 100% implementation of the [SOCKS5 protocol specification](https://tools.ietf.org/html/rfc1928) which includes [username password authentication](https://tools.ietf.org/html/rfc1929) and [GSS-API authentication](https://tools.ietf.org/html/rfc1961).
-   It can be run with zero minimal configuration.
-   It can have its external connections be set through another SOCKS5 server.
-   It can allow or block certain client addresses and certain external incoming addresses.
-   It can allow or block certain SOCKS5 requests.

**Disclaimer:** Jargyle is a hobby project and is currently subject to breaking changes. Jargyle is currently not production ready but it aims to be.

## Contents

-   [1. Requirements](#1-requirements)
-   [2. Building](#2-building)
-   [3. Running Jargyle](#3-running-jargyle)
-   [3. 1. Usage](#3-1-usage)
-   [3. 2. Creating a Configuration File](#3-2-creating-a-configuration-file)
-   [3. 3. Supplementing a Configuration File with Command Line Options](#3-3-supplementing-a-configuration-file-with-command-line-options)
-   [3. 4. Combining Configuration Files](#3-4-combining-configuration-files)
-   [3. 5. Running Jargyle with a Configuration File](#3-5-running-jargyle-with-a-configuration-file)
-   [3. 6. Running Jargyle with a Monitored Configuration File](#3-6-running-jargyle-with-a-monitored-configuration-file)
-   [3. 7. Managing SOCKS5 Users (for Username Password Authentication)](#3-7-managing-socks5-users-for-username-password-authentication)
-   [3. 7. 1. Creating a Users File](#3-7-1-creating-a-users-file)
-   [3. 7. 2. Adding Users to an Existing Users File](#3-7-2-adding-users-to-an-existing-users-file)
-   [3. 7. 3. Removing a User from an Existing Users File](#3-7-3-removing-a-user-from-an-existing-users-file)
-   [3. 8. Using SOCKS5 Authentication](#3-8-using-socks5-authentication)
-   [3. 8. 1. Using No Authentication](#3-8-1-using-no-authentication)
-   [3. 8. 2. Using Username Password Authentication](#3-8-2-using-username-password-authentication)
-   [3. 8. 3. Using GSS-API Authentication](#3-8-3-using-gss-api-authentication)
-   [3. 9. With External Connections Set Through Another SOCKS Server](#3-9-with-external-connections-set-through-another-socks-server)
-   [3. 9. 1. Using SOCKS5 Authentication](#3-9-1-using-socks5-authentication)
-   [3. 9. 1. 1. Using No Authentication](#3-9-1-1-using-no-authentication)
-   [3. 9. 1. 2. Using Username Password Authentication](#3-9-1-2-using-username-password-authentication)
-   [3. 9. 1. 3. Using GSS-API Authentication](#3-9-1-3-using-gss-api-authentication)
-   [3. 9. 2. Using Java System Properties](#3-9-2-using-java-system-properties)
-   [3. 10. Allowing or Blocking Addresses](#3-10-allowing-or-blocking-addresses)
-   [3. 11. Allowing or Blocking SOCKS5 Requests](#3-11-allowing-or-blocking-socks5-requests)
-   [3. 12. The Comment Attribute](#3-12-the-comment-attribute)
-   [3. 13. Logging](#3-13-logging)
-   [4. TODO](#4-todo)
-   [5. Contact](#5-contact)
-   [6. Donate](#6-donate)

## 1. Requirements

-   Apache Maven&#8482; 3.3.9 or higher 
-   Java&#8482; SDK 1.8 or higher

## 2. Building

To build and package Jargyle as an executable jar file, run the following commands:

```bash

    cd jargyle
    mvn package

```

## 3. Running Jargyle 

To run Jargyle without any command line arguments, you can run the following command:

```bash

    java -jar target/jargyle-${VERSION}.jar

```

Be sure to replace `${VERSION}` with the actual version shown within the name of the executable jar file.

The aforementioned command will run Jargyle on port 1080 at address 0.0.0.0 using no authentication.

### 3. 1. Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

```text

    Usage: jargyle.server.SocksServer [OPTIONS]
           jargyle.server.SocksServer --config-file-xsd
           jargyle.server.SocksServer --help
           jargyle.server.SocksServer --monitored-config-file=FILE
           jargyle.server.SocksServer [OPTIONS] --new-config-file=FILE
           jargyle.server.SocksServer --settings-help
           jargyle.server.SocksServer --socks5-users ARGS
    
    OPTIONS:
      --config-file=FILE, -f FILE
          The configuration file
      --config-file-xsd, -x
          Print the configuration file XSD and exit
      --enter-external-client-socks5-user-pass
          Enter through an interactive prompt the username password to be used to access the external SOCKS5 server used for external connections
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
      --socks5-users
          Mode for managing SOCKS5 users (add --help for more information)
    
```

The following is a list of available settings for the SOCKS server (displayed when using the command line option `--settings-help`):

```text

    SETTINGS:
    
      allowedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed client address criteria (default is matches:.*)
    
      backlog=INTEGER_BETWEEN_0_AND_2147483647
          The maximum length of the queue of incoming connections (default is 50)
    
      blockedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked client address criteria
    
      clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client socket
    
      externalClient.bindHost=HOST
          The binding host name or address for the socket to connect to the external SOCKS server used for external connections (default is 0.0.0.0)
    
      externalClient.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting for the socket to connect to the external SOCKS server used for external connections (default is 60000)
    
      externalClient.externalServerUri=SCHEME://HOST[:PORT]
          The URI of the external SOCKS server used for external connections.
    
      externalClient.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the socket to connect to the external SOCKS server used for external connections
    
      externalClient.socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
          The space separated list of acceptable authentication methods to the external SOCKS5 server used for external connections (default is NO_AUTHENTICATION_REQUIRED)
    
      externalClient.socks5.gssapiMechanismOid=GSSAPI_MECHANISM_OID
          The object ID for the GSS-API authentication mechanism to the external SOCKS5 server used for external connections (default is 1.2.840.113554.1.2.2)
    
      externalClient.socks5.gssapiNecReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSSAPI protection level negotiation must be unprotected should the external SOCKS5 server used for external connections use the NEC reference implementation (default is false)
    
      externalClient.socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication with the external SOCKS5 server used for external connections (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      externalClient.socks5.gssapiServiceName=GSSAPI_SERVICE_NAME
          The GSS-API service name for the external SOCKS5 server used for external connections
    
      externalClient.socks5.usernamePassword=USERNAME:PASSWORD
          The username password to be used to access the external SOCKS5 server used for external connections
    
      host=HOST
          The host name or address for the SOCKS server (default is 0.0.0.0)
    
      port=INTEGER_BETWEEN_0_AND_65535
          The port for the SOCKS server (default is 1080)
    
      socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the SOCKS server
    
      socks5.allowedExternalIncomingTcpAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed SOCKS5 external incoming TCP address criteria (default is matches:.*)
    
      socks5.allowedExternalIncomingUdpAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of allowed SOCKS5 external incoming UDP address criteria (default is matches:.*)
    
      socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
          The space separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)
    
      socks5.blockedExternalIncomingTcpAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked SOCKS5 external incoming TCP address criteria
    
      socks5.blockedExternalIncomingUdpAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]
          The space separated list of blocked SOCKS5 external incoming UDP address criteria
    
      socks5.gssapiNecReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSSAPI protection level negotiation must be unprotected according to the NEC reference implementation (default is false)
    
      socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      socks5.onBind.externalIncomingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the external incoming socket
    
      socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the listen socket
    
      socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onBind.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onConnect.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.serverBindHost=HOST
          The binding host name or address for the server-facing socket (default is 0.0.0.0)
    
      socks5.onConnect.serverConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting the server-facing socket to connect (default is 60000)
    
      socks5.onConnect.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing socket
    
      socks5.onUdpAssociate.clientBindHost=HOST
          The binding host name or address for the client-facing UDP socket (default is 0.0.0.0)
    
      socks5.onUdpAssociate.clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client-facing UDP socket
    
      socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 32768)
    
      socks5.onUdpAssociate.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onUdpAssociate.serverBindHost=HOST
          The binding host name or address for the server-facing UDP socket (default is 0.0.0.0)
    
      socks5.onUdpAssociate.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing UDP socket
    
      socks5.usernamePasswordAuthenticator=CLASSNAME[:VALUE]
          The username password authenticator for the SOCKS5 server
    
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
    
    SOCKS5_AUTH_METHODS:
    
      NO_AUTHENTICATION_REQUIRED
          No authentication required
    
      GSSAPI
          GSS-API authentication
    
      USERNAME_PASSWORD
          Username password authentication
    
    SOCKS5_GSSAPI_PROTECTION_LEVELS:
    
      NONE
          No protection
    
      REQUIRED_INTEG
          Required per-message integrity
    
      REQUIRED_INTEG_AND_CONF
          Required per-message integrity and confidentiality
    
    
```

The following is the command line help for managing SOCKS5 users for username password authentication (displayed when using the command line options `--socks5-users --help`):

```text

    Usage: jargyle.server.SocksServer --socks5-users COMMAND
           jargyle.server.SocksServer --socks5-users --help
           jargyle.server.SocksServer --socks5-users --xsd
    
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

### 3. 2. Creating a Configuration File

You can create a configuration file by using the command line option `--new-config-file`

The following command creates an empty configuration file:

```bash

    java -jar target/jargyle-${VERSION}.jar --new-config-file=configuration.xml

```

`configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration/>

```

Any preceding command line options that do not terminate before the command line option `--new-config-file` will be set in the new configuration file.

The following command creates a configuration file with the port number, the number of allowed backlogged connections, and no authentication required:

```bash

    java -jar target/jargyle-${VERSION}.jar --setting=port=1234 --setting=backlog=100 --setting=socks5.authMethods=NO_AUTHENTICATION_REQUIRED --new-config-file=configuration.xml

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
                <name>socks5.authMethods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
        </settings>
    </configuration>

```
  
### 3. 3. Supplementing a Configuration File with Command Line Options

You can supplement an existing configuration file with command line options.

The following command adds one command line options before the existing configuration file and another command line option after the existing configuration file:

```bash

    java -jar target/jargyle-${VERSION}.jar --setting=clientSocketSettings=SO_TIMEOUT=500 --config-file=configuration.xml --setting=socketSettings=SO_TIMEOUT=0 --new-config-file=new_configuration.xml

```

`new_configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>clientSocketSettings</name>
                <socketSettingsValue>
                    <socketSettings>
                        <socketSetting>
                            <name>SO_TIMEOUT</name>
                            <value>500</value>
                        </socketSetting>
                    </socketSettings>
                </socketSettingsValue>
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
                <name>socks5.authMethods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>socketSettings</name>
                <socketSettingsValue>
                    <socketSettings>
                        <socketSetting>
                            <name>SO_TIMEOUT</name>
                            <value>0</value>
                        </socketSetting>
                    </socketSettings>
                </socketSettingsValue>
            </setting>
        </settings>
    </configuration>

```

### 3. 4. Combining Configuration Files

You can combine multiple configuration files into one configuration file.

The following command combines the two earlier configuration files into one:

```bash

    java -jar target/jargyle-${VERSION}.jar --config-file=configuration.xml --config-file=new_configuration.xml --new-config-file=combined_configuration.xml

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
                <name>socks5.authMethods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>clientSocketSettings</name>
                <socketSettingsValue>
                    <socketSettings>
                        <socketSetting>
                            <name>SO_TIMEOUT</name>
                            <value>500</value>
                        </socketSetting>
                    </socketSettings>
                </socketSettingsValue>
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
                <name>socks5.authMethods</name>
                <value>NO_AUTHENTICATION_REQUIRED</value>
            </setting>
            <setting>
                <name>socketSettings</name>
                <socketSettingsValue>
                    <socketSettings>
                        <socketSetting>
                            <name>SO_TIMEOUT</name>
                            <value>0</value>
                        </socketSetting>
                    </socketSettings>
                </socketSettingsValue>
            </setting>
        </settings>
    </configuration>

```

Although the redundant settings in the combined configuration file is unnecessary, the result configuration file is for demonstration purposes only.

Also, if a setting of the same name appears more than once in the configuration file, then only the last setting of the same name is recognized. 

### 3. 5. Running Jargyle with a Configuration File

To run Jargyle with a configuration file, you can use the command line option `--config-file`

```bash

    java -jar target/jargyle-${VERSION}.jar --config-file=configuration.xml

```

Also the configuration file can be supplemented with command line options and/or can be combined with multiple configuration files.

### 3. 6. Running Jargyle with a Monitored Configuration File

You can run Jargyle with a configuration file to be monitored for any changes to be applied to the running configuration.

To run Jargyle with a monitored configuration file, you can use the command line option `--monitored-config-file`

```bash

    java -jar target/jargyle-${VERSION}.jar --monitored-config-file=configuration.xml

```

Unlike the command line option `--config-file`, the monitored configuration file cannot be supplemented with command line options and cannot be combined with multiple configuration files.

The following are the settings in the monitored configuration file that will have no effect if changed during the running configuration:

-   `backlog`
-   `host`
-   `port`
-   `socketSettings` 

### 3. 7. Managing SOCKS5 Users (for Username Password Authentication)

You can manage SOCKS5 users stored in an XML file called a users file. A users file can be used for [username password authentication](#3-8-2-using-username-password-authentication).

#### 3. 7. 1. Creating a Users File

To create a users file, you would run the following command:

```bash

    java -jar target/jargyle-${VERSION}.jar --socks5-users create-new-file FILE

```

Where `FILE` would be the name for the new users file.

Once you have run the command, an interactive prompt will ask you if you want to enter a user.

```text

    java -jar target/jargyle-${VERSION}.jar --socks5-users create-new-file users.xml
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

#### 3. 7. 2. Adding Users to an Existing Users File

To add users to an existing users file, you would run the following command:

```bash

    java -jar target/jargyle-${VERSION}.jar --socks5-users add-users-to-file FILE

```

Where `FILE` would be the name for the existing users file.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you do not want to enter any more users, the updated users file will be saved.

```text

    java -jar target/jargyle-${VERSION}.jar --socks5-users add-users-to-file users.xml
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

#### 3. 7. 3. Removing a User from an Existing Users File

To remove a user from an existing users file, you would run the following command:

```bash

    java -jar target/jargyle-${VERSION}.jar --socks5-users remove-user NAME FILE

```

Where `NAME` would be the name of the user and `FILE` would be the name for the existing users file.

Once you have run the command, the user of the specified name will be removed from the existing users file.

```text

    java -jar target/jargyle-${VERSION}.jar --socks5-users remove-user Jafar users.xml
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

### 3. 8. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `socks5.authMethods` as a space separated list.

Partial command line example:

```text

    "--setting=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI"

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>GSSAPI USERNAME_PASSWORD</value>
        </setting>
    </settings>

```

If not set, the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`

#### 3. 8. 1. Using No Authentication

Because the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `socks5.authMethods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `socks5.authMethods`

Partial command line example:

```text

    "--setting=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD</value>
        </setting>
    </settings>

```

#### 3. 8. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `socks5.authMethods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```text

    --setting=socks5.authMethods=USERNAME_PASSWORD

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>USERNAME_PASSWORD</value>
        </setting>
    </settings>

```

Also, you will need to have the setting `socks5.usernamePasswordAuthenticator` to specify the name of the class that extends `jargyle.server.socks5.UsernamePasswordAuthenticator` along with a string value

The following are two provided classes you can use:

-   `jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`
-   `jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`

`jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the string value of a space separated list of USERNAME:PASSWORD pairs

Partial command line example:

```text
    
    "--setting=socks5.authMethods=USERNAME_PASSWORD" \
    "--setting=socks5.usernamePasswordAuthenticator=jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator:Aladdin:opensesame Jasmine:mission%3Aimpossible"

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>USERNAME_PASSWORD</value>
        </setting>
        <setting>
            <name>socks5.usernamePasswordAuthenticator</name>
            <usernamePasswordAuthenticatorValue>
                <usernamePasswordAuthenticator>
                    <className>jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator</className>
                    <value>Aladdin:opensesame Jasmine:mission%3Aimpossible</value>
                </usernamePasswordAuthenticator>
            </usernamePasswordAuthenticatorValue>
        </setting>
    </settings>

```

If any of the usernames or any of the passwords contain a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If any of the usernames or any of the passwords contain a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If any of the usernames or any of the passwords contain a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If any of the usernames or any of the passwords contain a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

`jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the [XML file of users](#3-7-managing-socks5-users-for-username-password-authentication) whose file name is provided as a string value

Partial command line example:

```text
    
    --setting=socks5.authMethods=USERNAME_PASSWORD \
    --setting=socks5.usernamePasswordAuthenticator=jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator:users.xml

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>USERNAME_PASSWORD</value>
        </setting>    
        <setting>
            <name>socks5.usernamePasswordAuthenticator</name>
            <usernamePasswordAuthenticatorValue>
                <usernamePasswordAuthenticator>
                    <className>jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator</className>
                    <value>users.xml</value>
                </usernamePasswordAuthenticator>
            </usernamePasswordAuthenticatorValue>
        </setting>
    </settings>

```

#### 3. 8. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `socks5.authMethods` to have `GSSAPI` included.

Partial command line example:

```text

    --setting=socks5.authMethods=GSSAPI

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>socks5.authMethods</name>
            <value>GSSAPI</value>
        </setting>
    </settings>

```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API).

The following is a sufficient example of using the Kerberos security mechanism:

```bash

    java -Djavax.security.auth.useSubjectCredsOnly=false \
	    -Djava.security.auth.login.config=login.conf \
	    -Djava.security.krb5.conf=krb5.conf \
	    -jar target/jargyle-${VERSION}.jar \
	    --setting=socks5.authMethods=GSSAPI 

```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file for the underlying security mechanism.

`login.conf`:

```text

    com.sun.security.jgss.accept  {
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
        kdc_udp_port = 12345
        kdc_tcp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)  

### 3. 9. With External Connections Set Through Another SOCKS Server

You can have Jargyle's external connections set through another SOCKS server. To have its external connections set through another SOCKS server, you will need to specify the other SOCKS server as a URI in the setting `externalClient.externalServerUri`

Partial command line example:

```text

    --setting=externalClient.externalServerUri=socks5://127.0.0.1:23456

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>externalClient.externalServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
    </settings>

```

Please note that the scheme in the URI specifies the SOCKS protocol to be used when accessing the other SOCKS server (`socks5`), the address or name of the machine of where the other SOCKS server resides (`127.0.0.1`), and the port number of the other SOCKS server (`23456`). In the aforementioned examples, the SOCKS protocol version 5 is used. At this time, the only supported scheme for the URI format is `socks5`

#### 3. 9. 1. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from for accessing the other SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `externalClient.socks5.authMethods` as a space separated list.

Partial command line example:

```text

    "--setting=externalClient.socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI"

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>externalClient.socks5.authMethods</name>
            <value>GSSAPI USERNAME_PASSWORD</value>
        </setting>
    </settings>

```

If not set, the default value for the setting `externalClient.socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`

##### 3. 9. 1. 1. Using No Authentication

Because the default value for the setting `externalClient.socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `externalClient.socks5.authMethods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `externalClient.socks5.authMethods`

Partial command line example:

```text

    "--setting=externalClient.socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>externalClient.socks5.authMethods</name>
            <value>NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD</value>
        </setting>
    </settings>
    
```

##### 3. 9. 1. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `externalClient.socks5.authMethods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```text

    --setting=externalClient.socks5.authMethods=USERNAME_PASSWORD

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>externalClient.socks5.authMethods</name>
            <value>USERNAME_PASSWORD</value>
        </setting>
    </settings>

```

To provide a username and password for the other SOCKS5 server, you can use either of the following command line options:

-   `--setting=externalClient.socks5.usernamePassword=USERNAME:PASSWORD`
-   `--enter-external-client-socks5-user-pass`

The command line option `--setting=externalClient.socks5.usernamePassword=USERNAME:PASSWORD` requires an actual username followed by a colon character (`:`) followed by an actual password.

Partial command line example:

```text
    
    --setting=externalClient.socks5.authMethods=USERNAME_PASSWORD \
    --setting=externalClient.socks5.usernamePassword=Aladdin:opensesame

```

If the username or the password contains a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If the username or the password contains a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If the username or the password contains a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

The command line option `--enter-external-client-socks5-user-pass` provides an interactive prompt for you to enter the username and password. This command line option is used for when you do not want to have the username and password appear in any script or in any part of the command line history for security reasons.

Partial command line example:

```text
    
    --setting=externalClient.socks5.authMethods=USERNAME_PASSWORD \
    --enter-external-client-socks5-user-pass

```

##### 3. 9. 1. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `externalClient.socks5.authMethods` to have `GSSAPI` included.

Partial command line example:

```text

    --setting=externalClient.socks5.authMethods=GSSAPI

```

Partial configuration file example:

```xml

    <settings>
        <setting>
            <name>externalClient.socks5.authMethods</name>
            <value>GSSAPI</value>
        </setting>
    </settings>

```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API), and you will also need to specify the GSS-API service name for the other SOCKS5 server.

The following is a sufficient example of using the Kerberos security mechanism:

```bash

    java -Djavax.security.auth.useSubjectCredsOnly=false \
	    -Djava.security.auth.login.config=login.conf \
	    -Djava.security.krb5.conf=krb5.conf \
	    -jar target/jargyle-${VERSION}.jar \
	    --setting=externalClient.externalServerUri=socks5://127.0.0.1:23456 \
	    --setting=externalClient.socks5.authMethods=GSSAPI \
	    --setting=externalClient.socks5.gssapiServiceName=rcmd/127.0.0.1 

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
        kdc_udp_port = 12345
        kdc_tcp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)

The command line option `--setting=externalClient.socks5.gssapiServiceName=rcmd/127.0.0.1` is the GSS-API service name (or the Kerberos service principal) for the other SOCKS5 server residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the name of the machine of where the other SOCKS5 server resides.)

#### 3. 9. 2. Using Java System Properties

Instead of using command line options or configuration settings, you can use the following Java system properties to set Jargyle's external connections through another SOCKS server:

-   `socksServerUri.scheme`: The scheme of the URI of the other SOCKS server used for external connections (only `socks5` is supported)
-   `socksServerUri.host`: The host name or address of the URI of the other SOCKS server used for external connections
-   `socksServerUri.port`: The port of the URI of the other SOCKS server used for external connections
-   `socksClient.bindHost`: Its usage is equivalent to the setting `externalClient.bindHost`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.connectTimeout`: Its usage is equivalent to the setting `externalClient.connectTimeout`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socketSettings`: Its usage is equivalent to the setting `externalClient.socketSettings`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.authMethods`: Its usage is equivalent to the setting `externalClient.socks5.authMethods`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.gssapiMechanismOid`: Its usage is equivalent to the setting `externalClient.socks5.gssapiMechanismOid`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.gssapiNecReferenceImpl`: Its usage is equivalent to the setting `externalClient.socks5.gssapiNecReferenceImpl`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.gssapiProtectionLevels`: Its usage is equivalent to the setting `externalClient.socks5.gssapiProtectionLevels`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.gssapiServiceName`: Its usage is equivalent to the setting `externalClient.socks5.gssapiServiceName`. See the settings help information for details (use the command line option `--settings-help`) 
-   `socksClient.socks5.username`: The username to be used in SOCKS5 username password authentication to access the other SOCKS5 server used for external connections (any special characters must be URL encoded)
-   `socksClient.socks5.password`: The password to be used in SOCKS5 username password authentication to access the other SOCKS5 server used for external connections (any special characters must be URL encoded)

### 3. 10. Allowing or Blocking Addresses

You can allow or block the following addresses:

-   Client addresses (IPv4, IPv6, and domain name addresses)
-   External incoming TCP addresses following the SOCKS5 BIND command (IPv4, IPv6, and domain name addresses)
-   External incoming UDP addresses following the SOCKS5 UDP ASSOCIATE command (IPv4, IPv6, and domain name addresses)

To allow or block an address or addresses, you will need to specify the address or addresses in any of the following settings:

-   `allowedClientAddressCriteria`
-   `blockedClientAddressCriteria`
-   `socks5.allowedExternalIncomingTcpAddressCriteria`
-   `socks5.allowedExternalIncomingUdpAddressCriteria`
-   `socks5.blockedExternalIncomingTcpAddressCriteria`
-   `socks5.blockedExternalIncomingUdpAddressCriteria`

You can specify an address or addresses in any of the aforementioned settings as either a literal expression preceded by the prefix `equals:` or a regular expression preceded by the prefix `matches:`.

Partial command line example:

```text
    
    "--setting=allowedClientAddressCriteria=equals:127.0.0.1 equals:0:0:0:0:0:0:0:1 equals:localhost" \
    "--setting=blockedClientAddressCriteria=matches:(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1|localhost)).*"
    
```

You can specify an address or addresses in any of the aforementioned settings as a `<criterion/>` XML element in the configuration file.

Partial configuration file example:

```xml
    
    <settings>
        <setting>
            <name>allowedClientAddressCriteria</name>
            <criteriaValue>
                <criteria>
                    <criterion method="equals" value="127.0.0.1"/>
                    <criterion method="equals" value="0:0:0:0:0:0:0:1"/>
                    <criterion method="equals" value="localhost"/>
                </criteria>
            </criteriaValue>
        </setting>
        <setting>
            <name>blockedClientAddressCriteria</name>
            <criteriaValue>
                <criteria>
                    <criterion method="matches" value="(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1|localhost)).*"/>
                </criteria>
            </criteriaValue>
        </setting>        
    </settings>
    
```

### 3. 11. Allowing or Blocking SOCKS5 Requests

You can allow or block SOCKS5 requests. To allow or block SOCKS5 requests, you will need to specify the SOCKS5 request or requests in any of the following settings in the configuration file:

-   `socks5.allowedSocks5RequestCriteria`
-   `socks5.blockedSocks5RequestCriteria`
 
You can specify a SOCKS5 request or requests in any of the aforementioned settings as a `<socks5RequestCriterion/>` XML element in the configuration file.

Partial configuration file example:

```xml
    
    <settings>
        <setting>
            <name>socks5.allowedSocks5RequestCriteria</name>
            <socks5RequestCriteriaValue>
                <socks5RequestCriteria>
                    <socks5RequestCriterion>
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="CONNECT"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="80" maxPort="80"/>
                            <portRange minPort="443" maxPort="443"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>
                </socks5RequestCriteria>
            </socks5RequestCriteriaValue>
        </setting>
        <setting>
            <name>socks5.blockedSocks5RequestCriteria</name>
            <socks5RequestCriteriaValue>
                <socks5RequestCriteria>
                    <socks5RequestCriterion>
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="BIND"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="0" maxPort="65535"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>
                    <socks5RequestCriterion>
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="UDP_ASSOCIATE"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="0" maxPort="65535"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>                    
                </socks5RequestCriteria>
            </socks5RequestCriteriaValue>
        </setting>        
    </settings>
    
```

### 3. 12. The Comment Attribute

When using an existing configuration file to create a new configuration file, any XML comments from the existing configuration file cannot be transferred to the new configuration file. To preserve comments  from one configuration file to the next configuration file, the `comment` attribute can be used in certain XML elements. You can use the `comment` attribute in the following XML elements:

-   `<commandCriterion/>`
-   `<criterion/>`
-   `<desiredDestinationAddressCriterion/>`
-   `<desiredDestinationPortRanges/>`
-   `<portRange/>`
-   `<setting/>`
-   `<socketSetting/>`
-   `<socks5RequestCriterion/>`
-   `<sourceAddressCriterion/>`

Partial configuration file example:

```xml
    
    <settings>
        <setting>
            <name>allowedClientAddressCriteria</name>
            <criteriaValue>
                <criteria>
                    <criterion method="equals" value="127.0.0.1" comment="IPv4 loopback address"/>
                    <criterion method="equals" value="0:0:0:0:0:0:0:1" comment="IPv6 loopback address"/>
                    <criterion method="equals" value="localhost" comment="domain name of loopback address"/>
                </criteria>
            </criteriaValue>
        </setting>
        <setting>
            <name>blockedClientAddressCriteria</name>
            <criteriaValue>
                <criteria>
                    <criterion method="matches" value="(?!(127\.0\.0\.1|0:0:0:0:0:0:0:1|localhost)).*" comment="block any address that is not a loopback address"/>
                </criteria>
            </criteriaValue>
        </setting>
        <setting>
            <name>socks5.allowedSocks5RequestCriteria</name>
            <socks5RequestCriteriaValue>
                <socks5RequestCriteria>
                    <socks5RequestCriterion comment="allow any client to connect to any address on port 80 or port 443">
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="CONNECT"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="80" maxPort="80" comment="HTTP port"/>
                            <portRange minPort="443" maxPort="443" comment="HTTPS port"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>
                </socks5RequestCriteria>
            </socks5RequestCriteriaValue>
        </setting>
        <setting>
            <name>socks5.blockedSocks5RequestCriteria</name>
            <socks5RequestCriteriaValue>
                <socks5RequestCriteria>
                    <socks5RequestCriterion comment="block any BIND requests">
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="BIND"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="0" maxPort="65535"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>
                    <socks5RequestCriterion comment="block any UDP ASSOCIATE requests">
                        <sourceAddressCriterion method="matches" value=".*"/>
                        <commandCriterion method="equals" value="UDP_ASSOCIATE"/>
                        <desiredDestinationAddressCriterion method="matches" value=".*"/>
                        <desiredDestinationPortRanges>
                            <portRange minPort="0" maxPort="65535"/>
                        </desiredDestinationPortRanges>
                    </socks5RequestCriterion>                    
                </socks5RequestCriteria>
            </socks5RequestCriteriaValue>
        </setting>
    </settings>
    
```

### 3. 13. Logging

Jargyle uses Java's built-in logging framework. There are seven logging levels in the order of highest priority to the lowest priority:

-   `SEVERE`
-   `WARNING`
-   `INFO`
-   `CONFIG`
-   `FINE`
-   `FINER`
-   `FINEST`

By default, the current level is set at `INFO` and up. This means that only logging messages of levels `INFO` and up will appear in the output.

The following are the classes that use logging:

-   `jargyle.server.Listener`
-   `jargyle.server.SocksServer`
-   `jargyle.server.SocksServerCLI`
-   `jargyle.server.TcpRelayServer$DataWorker`
-   `jargyle.server.Worker`
-   `jargyle.server.XmlFileSourceConfigurationService$ConfigurationUpdater`
-   `jargyle.server.socks5.Socks5Worker`
-   `jargyle.server.socks5.UdpRelayServer$IncomingPacketsWorker`
-   `jargyle.server.socks5.UdpRelayServer$OutgoingPacketsWorker`
-   `jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator$UsersUpdater`

To configure logging for any of the aforementioned classes, you can use a configuration file to specify the logging properties for any of the classes.

The following is a configuration file example of setting the logging level of some of the classes to `FINE` and up:

`logging.properties`:

```text
    
    jargyle.server.Listener.handlers = java.util.logging.ConsoleHandler
    jargyle.server.Listener.level = FINE    
    jargyle.server.SocksServer.handlers = java.util.logging.ConsoleHandler
    jargyle.server.SocksServer.level = FINE
    jargyle.server.SocksServerCLI.handlers = java.util.logging.ConsoleHandler
    jargyle.server.SocksServerCLI.level = FINE
    jargyle.server.Worker.handlers = java.util.logging.ConsoleHandler
    jargyle.server.Worker.level = FINE
    jargyle.server.socks5.Socks5Worker.handlers = java.util.logging.ConsoleHandler
    jargyle.server.socks5.Socks5Worker.level = FINE
    java.util.logging.ConsoleHandler.level = FINE
    
```

To use the configuration file, you can use the Java system property `java.util.logging.config.file`.

Example:

```bash
    
    java -Djava.util.logging.config.file=logging.properties -jar target/jargyle-${VERSION}.jar
    
```

## 4. TODO

-   [ ] Javadoc documentation on all types

-   [ ] Unit testing on other types

-   [ ] ~~Further documentation~~
    -   [ ] ~~Command line reference~~
    -   [ ] ~~Configuration file reference~~
    -   [ ] ~~Users file reference~~
  
## 5. Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`

## 6. Donate

[PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=BRABJQVU3S6LW&currency_code=USD&source=url)