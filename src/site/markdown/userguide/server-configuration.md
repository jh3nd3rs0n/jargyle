# Server Configuration

The following are topics for configuring the server from the server API and from 
the command line.

## Page Contents

-   [Enabling SSL/TLS for TCP Traffic Between the Server and Its Clients](#enabling-ssltls-for-tcp-traffic-between-the-server-and-its-clients)
-   [Enabling DTLS for UDP Traffic Between the Server and Its Clients](#enabling-dtls-for-udp-traffic-between-the-server-and-its-clients)
-   [Using SOCKS5 Authentication](#using-socks5-authentication)
    -   [Using No Authentication](#using-no-authentication)
    -   [Using Username Password Authentication](#using-username-password-authentication)
    -   [Using GSS-API Authentication](#using-gss-api-authentication)
-   [Chaining to Another SOCKS Server](#chaining-to-another-socks-server)
    -   [Enabling SSL/TLS for TCP Traffic Between the Server and the Other SOCKS Server](#enabling-ssltls-for-tcp-traffic-between-the-server-and-the-other-socks-server)
    -   [Enabling DTLS for UDP Traffic Between the Server and the Other SOCKS Server](#enabling-dtls-for-udp-traffic-between-the-server-and-the-other-socks-server)
    -   [Chaining to the Other SOCKS Server Using SOCKS5 Authentication](#chaining-to-the-other-socks-server-using-socks5-authentication)
        -   [Chaining to the Other SOCKS Server Using No Authentication](#chaining-to-the-other-socks-server-using-no-authentication)
        -   [Chaining to the Other SOCKS Server Using Username Password Authentication](#chaining-to-the-other-socks-server-using-username-password-authentication)
        -   [Chaining to the Other SOCKS Server Using GSS-API Authentication](#chaining-to-the-other-socks-server-using-gss-api-authentication)
    -   [Resolving Host Names From the Other SOCKS5 Server](#resolving-host-names-from-the-other-socks5-server)
-   [Chaining to a Specified Chain of Other SOCKS Servers](#chaining-to-a-specified-chain-of-other-socks-servers)
-   [Chaining to Specified Chains of Other SOCKS Servers](#chaining-to-specified-chains-of-other-socks-servers)
-   [Using Rules to Manage Traffic](#using-rules-to-manage-traffic)
    -   [Rule Conditions](#rule-conditions)
    -   [Rule Actions](#rule-actions)
    -   [Allowing or Denying Traffic](#allowing-or-denying-traffic)
    -   [Allowing a Limited Number of Simultaneous Instances of Traffic](#allowing-a-limited-number-of-simultaneous-instances-of-traffic)
    -   [Routing Traffic Through a Selection of Specified Chains of SOCKS Servers](#routing-traffic-through-a-selection-of-specified-chains-of-socks-servers)
    -   [Redirecting the Desired Destination](#redirecting-the-desired-destination)
    -   [Configuring Sockets](#configuring-sockets)
    -   [Configuring Relay Settings](#configuring-relay-settings)
    -   [Limiting Relay Bandwidth](#limiting-relay-bandwidth)

<a id="enabling-ssltls-for-tcp-traffic-between-the-server-and-its-clients"></a>
## Enabling SSL/TLS for TCP Traffic Between the Server and Its Clients

You can enable SSL/TLS for TCP traffic between the server and its clients. By 
default, SSL/TLS for TCP traffic between the server and its clients is disabled. 
To enable SSL/TLS for TCP traffic between the server and its clients, you will 
need to have the setting `ssl.enabled` set to `true`. In addition, you will 
need to have the setting `ssl.keyStoreFile` to specify the server's key store 
file (this file would need to be created by Java's keytool utility). Also, you 
will need to have the setting `ssl.keyStorePassword` to specify the password 
for the server's key store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "password")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>ssl.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-ssl-key-store-pass` instead. It will provide an interactive 
prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --enter-ssl-key-store-pass
```

If you want to have the client authenticate using SSL/TLS, you will need to have 
the setting `ssl.needClientAuth` set to `true`. In addition, you will need 
to have the setting `ssl.trustStoreFile` to specify the client's key store 
file to be used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the setting 
`ssl.trustStorePassword` to specify the password for the client's trust 
store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.keyStorePassword", "password"),
            Setting.newInstanceWithParsedValue(
                "ssl.needClientAuth", "true"),
            Setting.newInstanceWithParsedValue(
                "ssl.trustStoreFile", "client.jks"),
            Setting.newInstanceWithParsedValue(
                "ssl.trustStorePassword", "drowssap")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --setting=ssl.keyStorePassword=password \
    --setting=ssl.needClientAuth=true \
    --setting=ssl.trustStoreFile=client.jks \
    --setting=ssl.trustStorePassword=drowssap
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>ssl.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>ssl.needClientAuth</name>
            <value>true</value>
        </setting>
        <setting>
            <name>ssl.trustStoreFile</name>
            <value>client.jks</value>
        </setting>
        <setting>
            <name>ssl.trustStorePassword</name>
            <value>drowssap</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-ssl-trust-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=ssl.enabled=true \
    --setting=ssl.keyStoreFile=server.jks \
    --enter-ssl-key-store-pass \
    --setting=ssl.needClientAuth=true \
    --setting=ssl.trustStoreFile=client.jks \
    --enter-ssl-trust-store-pass
```

## Enabling DTLS for UDP Traffic Between the Server and Its Clients

You can enable DTLS for UDP traffic between the server and its clients. By 
default, DTLS for UDP traffic between the server and its clients is disabled. To 
enable DTLS for UDP traffic between the server and its clients, you will need to 
have the setting `dtls.enabled` set to `true`. In addition, you will need to 
have the setting `dtls.keyStoreFile` to specify the server's key store file 
(this file would need to be created by Java's keytool utility). Also, you will 
need to have the setting `dtls.keyStorePassword` to specify the password for 
the server's key store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "dtls.keyStorePassword", "password")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=dtls.enabled=true \
    --setting=dtls.keyStoreFile=server.jks \
    --setting=dtls.keyStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>dtls.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>dtls.keyStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>dtls.keyStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-dtls-key-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=dtls.enabled=true \
    --setting=dtls.keyStoreFile=server.jks \
    --enter-dtls-key-store-pass
```

## Using SOCKS5 Authentication

The server has the following SOCKS5 authentication methods to choose from:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

From the API and from the command line, you can have one or more of the 
aforementioned authentication methods set in the setting `socks5.methods` as 
a comma separated list. 

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", 
                "NO_AUTHENTICATION_REQUIRED,GSSAPI")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI
```

In the server configuration file, you can have one or more of the aforementioned 
authentication methods set in the setting `socks5.methods` as a 
`<socks5.methods/>` XML element with one or more `<socks5.method/>` XML 
elements each specifying an authentication method.

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

If not set, the default value for the setting `socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`

### Using No Authentication

Because the default value for the setting `socks5.methods` is set to 
`NO_AUTHENTICATION_REQUIRED`, it is not required for
`NO_AUTHENTICATION_REQUIRED` to be included in the setting 
`socks5.methods`.

However, if other authentication methods are to be used in addition to 
`NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be 
included in the setting `socks5.methods`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", 
                "NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

### Using Username Password Authentication

To use username password authentication, you will need to have the setting 
`socks5.methods` to have `USERNAME_PASSWORD` included. You will also need 
to have the setting `socks5.userpassmethod.userRepository` to specify the 
type name of the user repository along with an initialization string value.

The following are two type names you can use:

-   `FileSourceUserRepository`
-   `StringSourceUserRepository`

`FileSourceUserRepository`: This user repository handles the storage of the 
SOCKS5 users from an initialization string value of a provided file of a list of 
URL encoded username and hashed password pairs. The SOCKS5 users from the file 
are loaded onto memory. Because of this, you will need at least as much memory 
as the size of the file. If the file does not exist, it will be created and 
used. If the file does exist, the existing file will be used. To manage SOCKS5 
users under a user repository, see 
[Managing SOCKS5 Users](cli.md#managing-socks5-users).

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "socks5.userpassmethod.userRepository", 
                "FileSourceUserRepository:users")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassmethod.userRepository=FileSourceUserRepository:users
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>socks5.userpassmethod.userRepository</name>
            <socks5.userpassmethod.userRepository>
                <typeName>FileSourceUserRepository</typeName>
                <initializationString>users</initializationString>
            </socks5.userpassmethod.userRepository>
        </setting>
    </settings>
</configuration>
```

`StringSourceUserRepository`: This user repository handles the storage of 
the SOCKS5 users from an initialization string value of a comma separated list 
of URL encoded username and password pairs.

Each username and password pair in the comma separated list must be of the 
following format:

```text
USERNAME:PASSWORD
```

`USERNAME` is the username and `PASSWORD` is the password.

If the username or the password contains a colon character (`:`), then each 
colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a comma character (`,`), then each 
comma character must be replaced with the URL encoding character `%2C`.

If the username or the password contains a percent sign character (`%`) not 
used for URL encoding, then each percent sign character not used for URL 
encoding must be replaced with the URL encoding character `%25`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "socks5.userpassmethod.userRepository",
                "StringSourceUserRepository:Aladdin:opensesame,Jasmine:mission%3Aimpossible")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassmethod.userRepository=StringSourceUserRepository:Aladdin:opensesame,Jasmine:mission%3Aimpossible
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>socks5.userpassmethod.userRepository</name>
            <socks5.userpassmethod.userRepository>
                <typeName>StringSourceUserRepository</typeName>
                <initializationString>Aladdin:opensesame,Jasmine:mission%3Aimpossible</initializationString>
            </socks5.userpassmethod.userRepository>
        </setting>
    </settings>
</configuration>
```

### Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting 
`socks5.methods` to have `GSSAPI` included. You will need to specify Java 
system properties to use a security mechanism that implements the GSS-API (for 
example, Kerberos is a security mechanism that implements the GSS-API).

The following are sufficient examples of using the Kerberos security mechanism:

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "javax.security.auth.useSubjectCredsOnly", "true");
        System.setProperty(
            "java.security.auth.login.config", "login.conf");
        System.setProperty("java.security.krb5.conf", "krb5.conf");
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "socks5.methods", "GSSAPI")
        ))).start();
    }
}
```

Command line example:

```bash
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
jargyle start-server --setting=socks5.methods=GSSAPI 
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!--
Before running this file, the following must be executed:
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
-->
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

The Java system property `javax.security.auth.useSubjectCredsOnly` with 
the value `false` disables JAAS-based authentication to obtain the credentials 
directly and lets the underlying security mechanism obtain them instead.

The Java system property `java.security.auth.login.config` with the value 
`login.conf` provides a JAAS configuration file for the underlying security 
mechanism.

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

In `login.conf`, `rcmd/127.0.0.1` is a service principal that is created by 
a Kerberos administrator specifically for a SOCKS5 server with the service name 
`rcmd` residing at the address `127.0.0.1`. (In a production environment, the 
address `127.0.0.1` should be replaced by the name of the machine of where the 
SOCKS5 server resides.) 

Also in `login.conf`, `rcmd.keytab` is a keytab file also created by a 
Kerberos administrator that contains the aforementioned service principal and 
its respective encrypted key.  

The Java system property `java.security.krb5.conf` with the value 
`krb5.conf` provides the Kerberos configuration file that points to the 
Kerberos Key Distribution Center (KDC) for authentication.   

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

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on 
port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, 
the address `127.0.0.1` should be replaced by the actual address or name of 
the machine of where the KDC resides. Also, in a production environment, the 
realm `EXAMPLE.COM` should be replaced by an actual realm provided by a 
Kerberos administrator.)  

## Chaining to Another SOCKS Server

You can have the server chained to another SOCKS server, meaning that its 
traffic can be routed through another SOCKS server. To have the server chained 
to another SOCKS server, you will need to specify the other SOCKS server as a 
URI in the setting `chaining.socksServerUri`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server --setting=chaining.socksServerUri=socks5://127.0.0.1:23456
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
    </settings>
</configuration>
```

Please note that the scheme in the URI specifies the SOCKS protocol to be used 
to access the other SOCKS server (`socks5`), the address or name of the
machine of where the other SOCKS server resides (`127.0.0.1`), and the port 
number of the other SOCKS server (`23456`). In the aforementioned examples, the 
SOCKS protocol version 5 is used. At this time, the only supported scheme for 
the URI format is `socks5`

<a id="enabling-ssltls-for-tcp-traffic-between-the-server-and-the-other-socks-server"></a>
### Enabling SSL/TLS for TCP Traffic Between the Server and the Other SOCKS Server

You can enable SSL/TLS for TCP traffic between the server and the other SOCKS 
server under the following condition: 

-   The other SOCKS server accepts SSL/TLS connections.

By default, SSL/TLS for TCP traffic between the server and the other SOCKS server 
is disabled. To enable SSL/TLS for TCP traffic between the server and the other 
SOCKS server, you will need to have the setting `chaining.ssl.enabled` set 
to `true`. In addition, you will need to have the setting 
`chaining.ssl.trustStoreFile` to specify the other SOCKS server's key store 
file used as a trust store (this file would need to be created by Java's keytool 
utility). Also, you will need to have the setting 
`chaining.ssl.trustStorePassword` to specify the password for the other 
SOCKS server's trust store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-ssl-trust-store-pass` instead. It will provide 
an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
```

If the other SOCKS server wants the client (the server) to authenticate using 
SSL/TLS, you will need to have the setting `chaining.ssl.keyStoreFile` to 
specify the client's key store file (this file would need to be created by 
Java's keytool utility). Also, you will need to have the setting 
`chaining.ssl.keyStorePassword` to specify the password for the client's 
key store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.keyStoreFile", "client.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.keyStorePassword", "drowssap"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.ssl.trustStorePassword", "password")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --setting=chaining.ssl.keyStorePassword=drowssap \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password    
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.ssl.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.ssl.keyStoreFile</name>
            <value>client.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.keyStorePassword</name>
            <value>drowssap</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>chaining.ssl.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.ssl.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-ssl-key-store-pass` instead. It will provide an 
interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --enter-chaining-ssl-key-store-pass \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
```

<a id="enabling-dtls-for-udp-traffic-between-the-server-and-the-other-socks-server"></a>
### Enabling DTLS for UDP Traffic Between the Server and the Other SOCKS Server

You can enable DTLS for UDP traffic between the server and the other SOCKS 
server under the following condition: 

-   The other SOCKS server accepts DTLS connections.

By default, DTLS for UDP traffic between the server and the other SOCKS server is 
disabled. To enable DTLS for UDP traffic between the server and the other SOCKS 
server, you will need to have the setting `chaining.dtls.enabled` set to 
`true`. In addition, you will need to have the setting 
`chaining.dtls.trustStoreFile` to specify the other SOCKS server's key 
store file used as a trust store (this file would need to be created by Java's 
keytool utility). Also, you will need to have the setting 
`chaining.dtls.trustStorePassword` to specify the password for the other 
SOCKS server's trust store file.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.enabled", "true"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStoreFile", "server.jks"),
            Setting.newInstanceWithParsedValue(
                "chaining.dtls.trustStorePassword", "password")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.dtls.enabled</name>
            <value>true</value>
        </setting>
        <setting>
            <name>chaining.dtls.trustStoreFile</name>
            <value>server.jks</value>
        </setting>
        <setting>
            <name>chaining.dtls.trustStorePassword</name>
            <value>password</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-dtls-trust-store-pass` instead. It will provide 
an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
```

### Chaining to the Other SOCKS Server Using SOCKS5 Authentication

The server has the following SOCKS5 authentication methods to choose from when 
chaining to the other SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

From the API and from the command line, you can have one or more of the 
aforementioned authentication methods set in the setting 
`chaining.socks5.methods` as a comma separated list. 

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods",
                "NO_AUTHENTICATION_REQUIRED,GSSAPI")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI
```

In the configuration file, you can have one or more of the aforementioned 
authentication methods set in the setting `chaining.socks5.methods` as a 
`<socks5.methods/>` XML element with one or more `<socks5.method/>` XML 
elements each specifying an authentication method.

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

If not set, the default value for the setting `chaining.socks5.methods` is 
set to `NO_AUTHENTICATION_REQUIRED`.

#### Chaining to the Other SOCKS Server Using No Authentication

Because the default value for the setting `chaining.socks5.methods` is set 
to `NO_AUTHENTICATION_REQUIRED`, it is not required for 
`NO_AUTHENTICATION_REQUIRED` to be included in the setting 
`chaining.socks5.methods`.

However, if other authentication methods are to be used in addition to 
`NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be 
included in the setting `chaining.socks5.methods`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods",
                "NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED,GSSAPI,USERNAME_PASSWORD
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                <socks5.method>GSSAPI</socks5.method>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
```

<a id="chaining-to-the-other-socks-server-using-username-password-authentication"></a>
#### Chaining to the Other SOCKS Server Using Username Password Authentication

To chain to the other SOCKS server using username password authentication, you 
will need to have the setting `chaining.socks5.methods` to have 
`USERNAME_PASSWORD` included. You will also need to have the settings 
`chaining.socks5.userpassmethod.username` and 
`chaining.socks5.userpassmethod.password` to specify the username and password 
for the other SOCKS5 server.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.username",
                "Aladdin"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.password",
                "opensesame")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.username</name>
            <value>Aladdin</value>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.password</name>
            <value>opensesame</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
    </settings>
</configuration>
```

If you do not want to have the password appear in any script or in any part of 
the command line history for security reasons, you can use the command line 
option `--enter-chaining-socks5-userpassmethod-pass` instead. It will 
provide an interactive prompt for you to enter the password.

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --enter-chaining-socks5-userpassmethod-pass
```

Instead of using the settings `chaining.socks5.userpassmethod.username` and 
`chaining.socks5.userpassmethod.password` to specify the username and password 
for the other SOCKS5 server, you can supply the username and password for the 
other SOCKS5 server as a username and password pair in the user information 
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

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://Jasmine:mission%3Aimpossible@127.0.0.1:23456")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://Jasmine:mission%3Aimpossible@127.0.0.1:23456
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://Jasmine:mission%3Aimpossible@127.0.0.1:23456</value>
        </setting>
    </settings>
</configuration>
```

There is no need to have the setting `chaining.socks5.methods` to have
`USERNAME_PASSWORD` included since it will be automatically included during 
runtime.

**Warning**: This alternative leaves the password exposed in plaintext. It is 
recommended to use the approaches mentioned earlier since they hide the 
password either by encrypting it in the server configuration file created by 
`new-server-config-file` or by obtaining the password from the interactive 
prompt from the command line option 
`--enter-chaining-socks5-userpassmethod-pass`. 

#### Chaining to the Other SOCKS Server Using GSS-API Authentication

To chain to the other SOCKS server using GSS-API authentication, you will need 
to have the setting `chaining.socks5.methods` to have `GSSAPI` included. 
You will also need to specify Java system properties to use a security 
mechanism that implements the GSS-API (for example, Kerberos is a security 
mechanism that implements the GSS-API) and you will also need to specify the 
GSS-API service name for the other SOCKS5 server.

The following are sufficient examples of using the Kerberos security mechanism:

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.setProperty(
            "javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty(
            "java.security.auth.login.config", "login.conf");
        System.setProperty("java.security.krb5.conf", "krb5.conf");
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "GSSAPI"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.gssapimethod.serviceName",
                "rcmd/127.0.0.1")
        ))).start();
    }
}
```

Command line example:

```bash
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapimethod.serviceName=rcmd/127.0.0.1 
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
<!--
Before running this file, the following must be executed:
export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
-->
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.gssapimethod.serviceName</name>
            <value>rcmd/127.0.0.1</value>
        </setting>
    </settings>
</configuration>
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

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on 
port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, 
the address `127.0.0.1` should be replaced by the actual address or name of 
the machine of where the KDC resides. Also, in a production environment, the 
realm `EXAMPLE.COM` should be replaced by an actual realm provided by a 
Kerberos administrator.)

The setting `chaining.socks5.gssapimethod.serviceName` with the value 
`rcmd/127.0.0.1` is the GSS-API service name (or the Kerberos service 
principal) for the other SOCKS server residing at the address `127.0.0.1`. (In 
a production environment, the address `127.0.0.1` should be replaced by the 
name of the machine of where the other SOCKS server resides.)

### Resolving Host Names From the Other SOCKS5 Server

Before discussing host name resolution from the other SOCKS5 server, a brief 
explanation of the server's internals:

The server uses sockets to interact with the external world.

-   Under the CONNECT request, it uses a socket that connects to the desired 
target server. In this documentation, this socket is called the target-facing 
socket.
-   Under the BIND request, it uses a socket that listens for an inbound socket. 
In this documentation, this socket is called the listen socket.
-   Under the UDP ASSOCIATE request, it uses a UDP socket that sends and 
receives datagram packets to and from peer UDP sockets. In this documentation, 
this UDP socket is called the peer-facing UDP socket.

The server also uses a host resolver to resolve host names for the 
aforementioned sockets and for 
[the RESOLVE request](../reference/socks5-resolve-request.md).

When the server is chained to another SOCKS5 server, the aforementioned sockets 
that the server uses become SOCKS5-enabled, meaning that their traffic is routed 
through the other SOCKS5 server.

It is similar for the host resolver. When the server is chained to another 
SOCKS5 server, the host resolver that the server uses becomes SOCKS5-enabled, 
meaning that it can use the other SOCKS5 server to resolve host names provided 
that the other SOCKS5 server supports handling the SOCKS5 RESOLVE request. 
However, this functionality for the host resolver is disabled by default 
making the host resolver resolve host names through the local system.

Therefore, default host name resolution from the other SOCKS5 server is 
performed but has the following limitations:

Default host name resolution from the other SOCKS5 server OCCURS ONLY...

-   ...under the CONNECT request when the target-facing socket makes an 
extemporaneous outbound connection. Preparation is omitted for the target-facing 
socket. Such preparation includes applying the specified socket settings for the 
target-facing socket, resolving the target host name before connecting, and 
setting the specified timeout in milliseconds on waiting for the target-facing 
socket to connect. The host resolver is not used in resolving the target host 
name. When the target-facing socket is SOCKS5-enabled, the target host name is 
resolved by the other SOCKS5 server and not through the local system.

Default host name resolution from the other SOCKS5 server DOES NOT OCCUR...

-   ...under the CONNECT request when the target-facing socket makes a prepared 
outbound connection. Preparation for the target-facing socket includes resolving 
the target host name before connecting. The host resolver is used in resolving 
the target host name. Because of its default functionality, the host resolver 
resolves the target host name through the local system.
-   ...under the BIND request when resolving the binding host name for the 
listen socket. The host resolver is used in resolving the binding host name for 
the listen socket. Because of its default functionality, the host resolver 
resolves the binding host name for the listen socket through the local system.
-   ...under the UDP ASSOCIATE request when resolving the host name for an 
outbound datagram packet. The host resolver is used in resolving the host name 
for an outbound datagram packet. Because of its default functionality, the host 
resolver resolves the host name for an outbound datagram packet through the 
local system.
-   ...under the RESOLVE request when resolving the provided host name. The host 
resolver is used in resolving the provided host name. Because of its default 
functionality, the host resolver resolves the provided host name through the 
local system.

If you prefer to have host name resolution from the other SOCKS5 server without 
the aforementioned limitations, you would need to set the setting 
`chaining.socks5.socks5HostResolver.resolveFromSocks5Server` to `true`. This 
setting enables the host resolver to resolve host names from the other SOCKS5 
server. This setting can only be used if the other SOCKS5 server supports 
handling the RESOLVE request.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.socks5HostResolver.resolveFromSocks5Server", 
                "true")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.socks5HostResolver.resolveFromSocks5Server</name>
            <value>true</value>
        </setting>
    </settings>
</configuration>
```

## Chaining to a Specified Chain of Other SOCKS Servers

You can have the server chained to a specified chain of other SOCKS servers, 
meaning that its traffic can be routed through the specified chain of the other 
SOCKS servers. To have the server chained to a specified chain of other SOCKS 
servers, you will need to have the setting `chaining.socksServerUri` 
specified multiple times with each setting specifying a SOCKS server as a URI.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:65432"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:34567")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
    </settings>
</configuration>
```

To specify the settings regarding a SOCKS server in the chain, the settings 
regarding a SOCKS server will need to be placed after that specified SOCKS 
server but before the next specified SOCKS server if any.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:23456"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "GSSAPI"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.gssapimethod.serviceName",
                "rcmd/127.0.0.1"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:65432"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.methods", "USERNAME_PASSWORD"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.username",
                "Aladdin"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.userpassmethod.password",
                "opensesame"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:34567"),
            Setting.newInstanceWithParsedValue(
                "chaining.socks5.socks5HostResolver.resolveFromSocks5Server", 
                "true")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapimethod.serviceName=rcmd/127.0.0.1 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassmethod.username=Aladdin \
    --setting=chaining.socks5.userpassmethod.password=opensesame \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567 \
    --setting=chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:23456</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>GSSAPI</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.gssapimethod.serviceName</name>
            <value>rcmd/127.0.0.1</value>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:65432</value>
        </setting>
        <setting>
            <name>chaining.socks5.methods</name>
            <socks5.methods>
                <socks5.method>USERNAME_PASSWORD</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.username</name>
            <value>Aladdin</value>
        </setting>
        <setting>
            <name>chaining.socks5.userpassmethod.password</name>
            <value>opensesame</value>
            <doc>If this configuration file was created by new-server-config-file, the password would be encrypted</doc>
        </setting>
        <setting>
            <name>chaining.socksServerUri</name>
            <value>socks5://127.0.0.1:34567</value>
        </setting>        
        <setting>
            <name>chaining.socks5.socks5HostResolver.resolveFromSocks5Server</name>
            <value>true</value>
        </setting>    
    </settings>
</configuration>
```

The known limitations of chaining to a specified chain of other SOCKS servers 
include the following:

-   Only TCP traffic can be routed through the chain. The server will attempt to 
route any UDP traffic through the last SOCKS server of the chain.

## Chaining to Specified Chains of Other SOCKS Servers

You can have the server chained to specified chains of other SOCKS servers, 
meaning that its traffic can be routed through one of the specified chains of 
other SOCKS servers at a time. To have the server chained to multiple 
specified chains of other SOCKS servers, you will need to have a route ID 
assigned at the end of each 
[chain](#chaining-to-a-specified-chain-of-other-socks-servers) by using the 
setting `chaining.routeId`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "delta")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
    </settings>
</configuration>
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, 
`socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned 
the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, 
`socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned 
the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, 
`socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned 
the route ID of `delta`

There is another route that is assigned a route ID. That route is the direct 
route. The direct route uses no chain to route the traffic through. It is 
assigned by default a route ID of `lastRoute`.

To omit the direct route from being included, have the last chain not assigned a 
route ID from the setting `chaining.routeId`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
    </settings>
</configuration>
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, 
`socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned 
the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, 
`socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned 
the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, 
`socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned 
by default the route ID of `lastRoute`

To change the route ID assigned to the last route, you can set the setting 
`lastRouteId` to the route ID you want assigned to the last route.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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
    --setting=lastRouteId=omega
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
    </settings>
</configuration>
```

You can also set the setting `routeSelectionStrategy` to specify the 
selection strategy for the next route. The default is `CYCLICAL`.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionStrategy", "RANDOM")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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
    --setting=lastRouteId=omega \
    --setting=routeSelectionStrategy=RANDOM
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>routeSelectionStrategy</name>
            <value>RANDOM</value>
        </setting>
    </settings>
</configuration>
```

You can also set the setting `routeSelectionLogAction` to specify the 
logging action to take if a route is selected.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionStrategy", "RANDOM"),
            Setting.newInstanceWithParsedValue(
                "routeSelectionLogAction", "LOG_AS_INFO")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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
    --setting=lastRouteId=omega \
    --setting=routeSelectionStrategy=RANDOM \
    --setting=routeSelectionLogAction=LOG_AS_INFO
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>routeSelectionStrategy</name>
            <value>RANDOM</value>
        </setting>
        <setting>
            <name>routeSelectionLogAction</name>
            <value>LOG_AS_INFO</value>
        </setting>
    </settings>
</configuration>
```

## Using Rules to Manage Traffic

A rule consists of the following:

-   Rule conditions: fields that altogether evaluates as true if they match a 
specific instance of traffic
-   Rule actions: fields that are applied if the aforementioned rule conditions 
evaluate as true for matching a specific instance of traffic

From the API and from the command line, a rule consists of a comma separated 
list of both rule conditions and rule actions. 

In the server configuration file, a rule is expressed as a `<rule/>` XML 
element with a `<ruleConditions/>` XML element and a `<ruleActions/>` XML 
element. 

See [Rule Conditions](#rule-conditions) and [Rule Actions](#rule-actions) for 
more information.

To specify a rule, you would need to have the setting `rule` specify the rule. 

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allows all forms of traffic
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
    </settings>
</configuration>
```

To specify multiple rules, you would need to have the setting `rule` specified 
multiple times with each setting specifying another rule.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allows the CONNECT request for any server on port 80 
             * or 443
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT,"
                + "socks5.request.desiredDestinationPort=80,"
                + "socks5.request.desiredDestinationPort=443,"
                + "firewallAction=ALLOW"),
            /*
             * Denies the CONNECT request for any server on any 
             * other port
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT,firewallAction=DENY"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.command=CONNECT,socks5.request.desiredDestinationPort=80,socks5.request.desiredDestinationPort=443,firewallAction=ALLOW \
    --setting=rule=socks5.request.command=CONNECT,firewallAction=DENY \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationPort</name>
                        <value>80</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationPort</name>
                        <value>443</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>DENY</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```

When a specific instance of traffic is matched by the first rule, that rule is 
applied and the rest of the rules are ignored. Therefore, it is best to have 
more specific rules specified first and have less specific rules specified 
last.

### Rule Conditions

From the API and from the command line, rule conditions consist of a comma 
separated list of rule conditions. Each rule condition consists of the syntax 
of `NAME=VALUE` where `NAME` is expressed as the name of the rule condition 
and `VALUE` is expressed as the value assigned to the rule condition. 

Partial API and command line example:

```text
clientAddress=127.0.0.1,clientAddress=0:0:0:0:0:0:0:1
```

In the server configuration file, rule conditions are expressed in a 
`<ruleConditions/>` XML element with zero to many `<ruleCondition/>` XML 
elements. Each `<ruleCondition/>` XML element contains a `<name/>` XML 
element for the name of the rule condition and the `<value/>` XML element of 
the value assigned to the rule condition.

Partial server configuration file example:

```xml
<ruleConditions>
    <ruleCondition>
        <name>clientAddress</name>
        <value>127.0.0.1</value>
    </ruleCondition>
    <ruleCondition>
        <name>clientAddress</name>
        <value>0:0:0:0:0:0:0:1</value>
    </ruleCondition>
</ruleConditions>
```

All rule conditions together are evaluated as true if each group of one or 
more rule conditions with the same name is evaluated as true. Within a group 
of one or more rule conditions with the same name, all rule conditions 
together are evaluated as true if at least one of the rule conditions within 
that group is evaluated as true. Zero rule conditions is evaluated as true.

Partial API examples:

```text
/*
 * Evaluates as true if the client address is the IPv4 loopback 
 * address or the IPv6 loopback address
 */
"clientAddress=127.0.0.1,clientAddress=0:0:0:0:0:0:0:1"
```

```text
/*
 * Evaluates as true if username password authentication was used 
 * and the user is either 'guest' or 'specialuser'
 */
"socks5.method=USERNAME_PASSWORD,socks5.user=guest,socks5.user=specialuser"
```

```text
/*
 * Evaluates as true since there are no rule conditions given
 */
""
```

A complete listing of rule conditions can be found 
[here](../reference/rule-conditions.md).

### Rule Actions

From the API and from the command line, rule actions consist of a comma 
separated list of rule actions. Each rule action consists of the syntax of 
`NAME=VALUE` where `NAME` is expressed as the name of the rule action and 
`VALUE` is expressed as the value assigned to the rule action. 

Partial API and command line example:

```text
firewallAction=ALLOW,firewallActionLogAction=LOG_AS_INFO
```

In the server configuration file, rule action are expressed in a `<ruleActions/>` 
XML element with zero to many `<ruleAction/>` XML elements. Each 
`<ruleAction/>` XML element contains a `<name/>` XML element for the name of 
the rule action and the `<value/>` XML element of the value assigned to the 
rule action.

Partial server configuration file example:

```xml
<ruleActions>
    <ruleAction>
        <name>firewallAction</name>
        <value>ALLOW</value>
    </ruleAction>
    <ruleAction>
        <name>firewallActionLogAction</name>
        <value>LOG_AS_INFO</value>
    </ruleAction>
</ruleActions>
```

Unless otherwise stated, if a rule action of the same name appears more than 
once in the comma separated list or in the `<ruleActions/>` XML element, then 
only the last rule action of the same name is recognized.

A complete listing of rule actions can be found 
[here](../reference/rule-actions.md).

### Allowing or Denying Traffic

To allow or deny a specific instance of traffic, you will need the following 
rule action:

-   `firewallAction`

The value given to the rule action must be either of the following values:

-   `ALLOW`
-   `DENY`

This rule action can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method`
-   `socks5.reply.serverBoundAddress`
-   `socks5.reply.serverBoundPort`
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.secondReply.serverBoundAddress`
-   `socks5.secondReply.serverBoundPort`
-   `socks5.udp.inbound.desiredDestinationAddress`
-   `socks5.udp.inbound.desiredDestinationPort`
-   `socks5.udp.inbound.sourceAddress`
-   `socks5.udp.inbound.sourcePort`
-   `socks5.udp.outbound.desiredDestinationAddress`
-   `socks5.udp.outbound.desiredDestinationPort`
-   `socks5.udp.outbound.sourceAddress`
-   `socks5.udp.outbound.sourcePort`
-   `socks5.user`
-   `socksServerAddress`

**Note**: for any other rule actions to be applied, the rule action 
`firewallAction` with the value of `ALLOW` must be present.

You can also specify the logging action to take if the rule action 
`firewallAction` is applied by adding the following rule action:

-   `firewallActionLogAction`

The rule action `firewallActionLogAction` is optional.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Deny any BIND or UDP ASSOCIATE requests and log as a warning 
             * message their denials
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                 "socks5.request.command=BIND,"
                 + "socks5.request.command=UDP_ASSOCIATE,"
                 + "firewallAction=DENY,"
                 + "firewallActionLogAction=LOG_AS_WARNING"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.command=BIND,socks5.request.command=UDP_ASSOCIATE,firewallAction=DENY,firewallActionLogAction=LOG_AS_WARNING \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>BIND</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>UDP_ASSOCIATE</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>DENY</value>
                    </ruleAction>
                    <ruleAction>
                        <name>firewallActionLogAction</name>
                        <value>LOG_AS_WARNING</value>
                    </ruleAction>                
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```

### Allowing a Limited Number of Simultaneous Instances of Traffic

To allow a limited number of simultaneous specific instances of traffic, you 
will need the following rule action:

-   `firewallActionAllowLimit`

The value given to the rule action must be an integer between 0 (inclusive) and 
2147483647 (inclusive)

This rule action can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method`
-   `socks5.reply.serverBoundAddress`
-   `socks5.reply.serverBoundPort`
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.secondReply.serverBoundAddress`
-   `socks5.secondReply.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if the limit on the number of 
simultaneous specific instances of traffic has been reached by adding the 
following rule action:

-   `firewallActionAllowLimitReachedLogAction`

The rule action `firewallActionAllowLimitReachedLogAction` is optional.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allow the user 'guest' from username password 
             * authentication 50 simultaneous connections and log 
             * as an informational message when the limit has been 
             * reached
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.method=USERNAME_PASSWORD,"
                + "socks5.user=guest,"
                + "firewallAction=ALLOW,"
                + "firewallActionAllowLimit=50,"
                + "firewallActionAllowLimitReachedLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.method=USERNAME_PASSWORD,socks5.user=guest,firewallAction=ALLOW,firewallActionAllowLimit=50,firewallActionAllowLimitReachedLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.method</name>
                        <value>USERNAME_PASSWORD</value>
                    </ruleCondition>
                    <ruleCondition>
                        <name>socks5.user</name>
                        <value>guest</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>firewallActionAllowLimit</name>
                        <value>50</value>
                    </ruleAction>                
                    <ruleAction>
                        <name>firewallActionAllowLimitReachedLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
    </settings>
</configuration>
```

<a id="routing-traffic-through-a-selection-of-specified-chains-of-socks-servers"></a>
### Routing Traffic Through a Selection of Specified Chains of SOCKS Servers

To route traffic through a selection of specified chains of SOCKS servers, you 
can use the following rule actions:

-   `routeSelectionStrategy`: Specifies the selection strategy for the next 
route (This rule action is optional. If this rule action is not specified, the 
setting `routeSelectionStrategy` is used.)
-   `selectableRouteId`: Specifies the ID for a selectable 
[route](#chaining-to-specified-chains-of-other-socks-servers) (This rule 
action is optional. This rule action can be specified multiple times with each 
rule action specifying another ID for a selectable route. If this rule action 
is not specified, all the routes defined by the settings `chaining.routeId` 
and `lastRouteId` are selectable.)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method` 
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if a route is selected by adding 
the following rule action:

-   `routeSelectionLogAction`

The rule action `routeSelectionLogAction` is optional. If the rule action is 
not specified, the setting `routeSelectionLogAction` is used.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11111"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11112"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:11113"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "alpha"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22221"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22222"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:22223"),
            Setting.newInstanceWithParsedValue(
                "chaining.routeId", "beta"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33331"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33332"),
            Setting.newInstanceWithParsedValue(
                "chaining.socksServerUri", 
                "socks5://127.0.0.1:33333"),
            Setting.newInstanceWithParsedValue(
                "lastRouteId", "omega"),
            /*
             * Randomly select either route 'alpha' or 'beta' when 
             * allowing the CONNECT request and log as an 
             * informational message the route that has been 
             * selected
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                "socks5.request.command=CONNECT,"
                + "firewallAction=ALLOW,"
                + "routeSelectionStrategy=RANDOM,"
                + "selectableRouteId=alpha,"
                + "selectableRouteId=beta,"
                + "routeSelectionLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else to go through route 'omega'
             */
            Setting.newInstanceWithParsedValue(
                "rule",
                "firewallAction=ALLOW,"
                + "routeSelectionStrategy=CYCLICAL,"
                + "selectableRouteId=omega")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
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
    --setting=lastRouteId=omega \
    --setting=rule=socks5.request.command=CONNECT,firewallAction=ALLOW,routeSelectionStrategy=RANDOM,selectableRouteId=alpha,selectableRouteId=beta,routeSelectionLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW,routeSelectionStrategy=CYCLICAL,selectableRouteId=omega
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
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
            <name>lastRouteId</name>
            <value>omega</value>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>routeSelectionStrategy</name>
                        <value>RANDOM</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>alpha</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>beta</value>
                    </ruleAction>
                    <ruleAction>
                        <name>routeSelectionLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>routeSelectionStrategy</name>
                        <value>CYCLICAL</value>
                    </ruleAction>
                    <ruleAction>
                        <name>selectableRouteId</name>
                        <value>omega</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```

### Redirecting the Desired Destination

To redirect the desired destination, you will need either or both of the 
following rule actions:

-   `socks5.request.desiredDestinationAddressRedirect`: Specifies the desired 
destination address redirect
-   `socks5.request.desiredDestinationPortRedirect`: Specifies the desired 
destination port redirect

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method` 
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if the desired destination is 
redirected by adding the following rule action:

-   `socks5.request.desiredDestinationRedirectLogAction`

The rule action `socks5.request.desiredDestinationRedirectLogAction` is optional.

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Redirect desired destination 'discontinuedserver.com' 
             * to 'newserver.com' and log as an informational 
             * message the redirection
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.desiredDestinationAddress=discontinuedserver.com,"
                + "firewallAction=ALLOW,"
                + "socks5.request.desiredDestinationAddressRedirect=newserver.com,"
                + "socks5.request.desiredDestinationRedirectLogAction=LOG_AS_INFO"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.desiredDestinationAddress=discontinuedserver.com,firewallAction=ALLOW,socks5.request.desiredDestinationAddressRedirect=newserver.com,socks5.request.desiredDestinationRedirectLogAction=LOG_AS_INFO \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationAddress</name>
                        <value>discontinuedserver.com</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.request.desiredDestinationAddressRedirect</name>
                        <value>newserver.com</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.request.desiredDestinationRedirectLogAction</name>
                        <value>LOG_AS_INFO</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```

### Configuring Sockets

To configure the sockets, you will need any of the following rule actions:

-   `bindHost`: Specifies the binding host name or address for all sockets 
-   `bindTcpPortRange`: Specifies a binding port range for all TCP sockets 
(This rule action can be specified multiple times with each rule action 
specifying another port range)
-   `bindUdpPortRange`: Specifies a binding port range for all UDP sockets 
(This rule action can be specified multiple times with each rule action 
specifying another port range)
-   `clientSocketSetting`: Specifies a socket setting for the client socket 
(This rule action can be specified multiple times with each rule action 
specifying another socket setting)
-   `externalFacingBindHost`: Specifies the binding host name or address 
for all external-facing sockets 
-   `externalFacingBindTcpPortRange`: Specifies a binding 
port range for all external-facing TCP 
sockets (This rule action can be specified multiple times with each rule action 
specifying another port range)
-   `externalFacingBindUdpPortRange`: Specifies a binding 
port range for all external-facing UDP 
sockets (This rule action can be specified multiple times with each rule action 
specifying another port range)
-   `externalFacingSocketSetting`: Specifies a socket setting for all 
external-facing sockets (This rule action can be specified multiple times with 
each rule action specifying another socket setting)
-   `internalFacingBindHost`: Specifies the binding host name or address for 
all internal-facing sockets
-   `internalFacingBindUdpPortRange`: Specifies a binding 
port range for all internal-facing UDP 
sockets (This rule action can be specified multiple times with each rule action 
specifying another port range)
-   `internalFacingSocketSetting`: Specifies a socket setting for all 
internal-facing sockets (This rule action can be specified multiple times with 
each rule action specifying another socket setting)
-   `socketSetting`: Specifies a socket setting for all sockets (This rule 
action can be specified multiple times with each rule action specifying another 
socket setting)
-   `socks5.onBindRequest.inboundSocketSetting`: Specifies a socket setting for 
the inbound socket (This rule action can be specified multiple times with each 
rule action specifying another socket setting)
-   `socks5.onBindRequest.listenBindHost`: Specifies the binding host name or 
address for the listen socket if the provided host address is all zeros
-   `socks5.onBindRequest.listenBindPortRange`: Specifies a binding 
port range for the listen socket if the provided port is zero (This rule 
action can be specified multiple times with each rule action specifying another 
port range)
-   `socks5.onBindRequest.listenSocketSetting`: Specifies a socket setting for 
the listen socket (This rule action can be specified multiple times with each 
rule action specifying another socket setting)
-   `socks5.onConnectRequest.prepareTargetFacingSocket`: Specifies the boolean 
value to indicate if the target-facing socket is to be prepared before 
connecting (involves applying the specified socket settings, resolving the 
target host name, and setting the specified timeout on waiting to connect)
-   `socks5.onConnectRequest.targetFacingBindHost`: Specifies the binding host 
name or address for the target-facing socket
-   `socks5.onConnectRequest.targetFacingBindPortRange`: Specifies a binding 
port range for the target-facing socket (This rule action can be specified 
multiple times with each rule action specifying another port range)
-   `socks5.onConnectRequest.targetFacingConnectTimeout`: Specifies the timeout 
in milliseconds on waiting for the target-facing socket to connect (Value must 
be an integer between 1 (inclusive) and 2147483647 (inclusive))
-   `socks5.onConnectRequest.targetFacingSocketSetting`: Specifies a socket 
setting for the target-facing socket (This rule action can be specified multiple 
times with each rule action specifying another socket setting)
-   `socks5.onRequest.bindHost`: Specifies the binding host name or address
for all sockets
-   `socks5.onRequest.bindTcpPortRange`: Specifies a binding
port range for all TCP sockets (This rule action can be specified multiple
times with each rule action specifying another port range)
-   `socks5.onRequest.bindUdpPortRange`: Specifies a binding
port range for all UDP sockets (This rule action can be specified multiple
times with each rule action specifying another port range)
-   `socks5.onRequest.externalFacingBindHost`: Specifies the binding host
name or address for all external-facing sockets
-   `socks5.onRequest.externalFacingBindTcpPortRange`: Specifies a
binding port range for all external-facing TCP sockets (This rule action can be
specified multiple times with each rule action specifying another port range)
-   `socks5.onRequest.externalFacingBindUdpPortRange`: Specifies a
binding port range for all external-facing UDP sockets (This rule action can be
specified multiple times with each rule action specifying another port range)
-   `socks5.onRequest.externalFacingSocketSetting`: Specifies a socket
setting for all external-facing sockets (This rule action can be specified
multiple times with each rule action specifying another socket setting)
-   `socks5.onRequest.internalFacingBindHost`: Specifies the binding
host name or address for all internal-facing sockets
-   `socks5.onRequest.internalFacingBindUdpPortRange`: Specifies a
binding port range for all internal-facing UDP sockets (This rule action can be
specified multiple times with each rule action specifying another port range)
-   `socks5.onRequest.internalFacingSocketSetting`: Specifies a socket
setting for all internal-facing sockets (This rule action can be specified
multiple times with each rule action specifying another socket setting)
-   `socks5.onRequest.socketSetting`: Specifies a socket setting for all
sockets (This rule action can be specified multiple times with each rule action
specifying another socket setting)
-   `socks5.onUdpAssociateRequest.clientFacingBindHost`: Specifies the binding 
host name or address for the client-facing UDP socket
-   `socks5.onUdpAssociateRequest.clientFacingBindPortRange`: Specifies a 
binding port range for the client-facing UDP socket (This rule action can be 
specified multiple times with each rule action specifying another port range)
-   `socks5.onUdpAssociateRequest.clientFacingSocketSetting`: Specifies a 
socket setting for the client-facing UDP socket (This rule action can be 
specified multiple times with each rule action specifying another socket 
setting)
-   `socks5.onUdpAssociateRequest.peerFacingBindHost`: Specifies the binding 
host name or address for the peer-facing UDP socket
-   `socks5.onUdpAssociateRequest.peerFacingBindPortRange`: Specifies a 
binding port range for the peer-facing UDP socket (This rule action can be 
specified multiple times with each rule action specifying another port range)
-   `socks5.onUdpAssociateRequest.peerFacingSocketSetting`: Specifies a socket 
setting for the peer-facing UDP socket (This rule action can be specified 
multiple times with each rule action specifying another socket setting)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method` 
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.user`
-   `socksServerAddress`

The rule action `socks5.onBindRequest.inboundSocketSetting` can also be used with 
the following rule conditions:

-   `socks5.reply.serverBoundAddress`
-   `socks5.reply.serverBoundPort`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allow the CONNECT request to connect to 
             * 'specialserver.com' and configure the target-facing 
             * socket
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT,"
                + "socks5.request.desiredDestinationAddress=specialserver.com,"
                + "firewallAction=ALLOW,"
                + "socks5.onConnectRequest.prepareTargetFacingSocket=true,"
                + "socks5.onConnectRequest.targetFacingSocketSetting=SO_RCVBUF=256,"
                + "socks5.onConnectRequest.targetFacingSocketSetting=SO_SNDBUF=256"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.command=CONNECT,socks5.request.desiredDestinationAddress=specialserver.com,firewallAction=ALLOW,socks5.onConnectRequest.prepareTargetFacingSocket=true,socks5.onConnectRequest.targetFacingSocketSetting=SO_RCVBUF=256,socks5.onConnectRequest.targetFacingSocketSetting=SO_SNDBUF=256 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationAddress</name>
                        <value>specialserver.com</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.onConnectRequest.prepareTargetFacingSocket</name>
                        <value>true</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.onConnectRequest.targetFacingSocketSetting</name>
                        <socketSetting>
                            <name>SO_RCVBUF</name>
                            <value>256</value>
                        </socketSetting>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.onConnectRequest.targetFacingSocketSetting</name>
                        <socketSetting>
                            <name>SO_SNDBUF</name>
                            <value>256</value>
                        </socketSetting>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
    </settings>
</configuration>
```

### Configuring Relay Settings

To configure the relay settings, you will need any of the following rule actions:

-   `socks5.onBindRequest.relayBufferSize`: Specifies the buffer size in bytes 
for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks5.onBindRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))
-   `socks5.onConnectRequest.relayBufferSize`: Specifies the buffer size in bytes 
for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks5.onConnectRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))
-   `socks5.onRequest.relayBufferSize`: Specifies the buffer size in bytes
for relaying the data (Value must be an integer between 1 (inclusive) and
2147483647 (inclusive))
-   `socks5.onRequest.relayIdleTimeout`: Specifies the timeout in
milliseconds on relaying no data (Value must be an integer between 1 (inclusive)
and 2147483647 (inclusive))
-   `socks5.onUdpAssociateRequest.relayBufferSize`: Specifies the buffer size in 
bytes for relaying the data (Value must be an integer between 1 (inclusive) and 
2147483647 (inclusive))
-   `socks5.onUdpAssociateRequest.relayIdleTimeout`: Specifies the timeout in 
milliseconds on relaying no data (Value must be an integer between 1 (inclusive) 
and 2147483647 (inclusive))

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method`
-   `socks5.reply.serverBoundAddress`
-   `socks5.reply.serverBoundPort`
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.secondReply.serverBoundAddress`
-   `socks5.secondReply.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allow the CONNECT request to connect to 
             * 'intermittent-idling-server.com' with a relay idle 
             * timeout of 1024000 milliseconds (1024 seconds)
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT,"
                + "socks5.request.desiredDestinationAddress=intermittent-idling-server.com,"
                + "firewallAction=ALLOW,"
                + "socks5.onConnectRequest.relayIdleTimeout=1024000"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.command=CONNECT,socks5.request.desiredDestinationAddress=intermittent-idling-server.com,firewallAction=ALLOW,socks5.onConnectRequest.relayIdleTimeout=1024000 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationAddress</name>
                        <value>intermittent-idling-server.com</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                        <name>socks5.onConnectRequest.relayIdleTimeout</name>
                        <value>1024000</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```

### Limiting Relay Bandwidth

To limit the relay bandwidth, you will need any of the following rule actions:

-   `socks5.onBindRequest.relayInboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onBindRequest.relayOutboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onConnectRequest.relayInboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onConnectRequest.relayOutboundBandwidthLimit`: Specifies the upper 
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onRequest.relayInboundBandwidthLimit`: Specifies the upper
limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onRequest.relayOutboundBandwidthLimit`: Specifies the upper
limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onUdpAssociateRequest.relayInboundBandwidthLimit`: Specifies the 
upper limit on bandwidth in bytes per second of receiving inbound data to be 
relayed
-   `socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit`: Specifies the 
upper limit on bandwidth in bytes per second of receiving outbound data to be 
relayed

The value given to any of the rule actions must be an integer between 1 
(inclusive) and 2147483647 (inclusive)

These rule actions can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.method` 
-   `socks5.reply.serverBoundAddress`
-   `socks5.reply.serverBoundPort`
-   `socks5.request.command`
-   `socks5.request.desiredDestinationAddress`
-   `socks5.request.desiredDestinationPort`
-   `socks5.secondReply.serverBoundAddress`
-   `socks5.secondReply.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

API example:

```java
package com.example;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
            /*
             * Allow the CONNECT request to connect to 
             * 'streamingwebsite.com' with an upper limit on the 
             * relay inbound and outbound bandwidth of 1024000 
             * bytes per second
             */
            Setting.newInstanceWithParsedValue(
                "rule", 
                "socks5.request.command=CONNECT,"
                + "socks5.request.desiredDestinationAddress=streamingwebsite.com,"
                + "firewallAction=ALLOW,"
                + "socks5.onConnectRequest.relayInboundBandwidthLimit=1024000,"
                + "socks5.onConnectRequest.relayOutboundBandwidthLimit=1024000"),
            /*
             * Allow anything else
             */
            Setting.newInstanceWithParsedValue(
                "rule", "firewallAction=ALLOW")
        ))).start();
    }
}
```

Command line example:

```bash
jargyle start-server \
    --setting=rule=socks5.request.command=CONNECT,socks5.request.desiredDestinationAddress=streamingwebsite.com,firewallAction=ALLOW,socks5.onConnectRequest.relayInboundBandwidthLimit=1024000,socks5.onConnectRequest.relayOutboundBandwidthLimit=1024000 \
    --setting=rule=firewallAction=ALLOW
```

Server configuration file example:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions>
                    <ruleCondition>
                        <name>socks5.request.command</name>
                        <value>CONNECT</value>
                    </ruleCondition>            
                    <ruleCondition>
                        <name>socks5.request.desiredDestinationAddress</name>
                        <value>streamingwebsite.com</value>
                    </ruleCondition>
                </ruleConditions>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                    <ruleAction>
                         <name>socks5.onConnectRequest.relayInboundBandwidthLimit</name>
                        <value>1024000</value>
                    </ruleAction>
                    <ruleAction>
                         <name>socks5.onConnectRequest.relayOutboundBandwidthLimit</name>
                        <value>1024000</value>
                    </ruleAction>                
                </ruleActions>
            </rule>
        </setting>
        <setting>
            <name>rule</name>
            <rule>
                <ruleConditions/>
                <ruleActions>
                    <ruleAction>
                        <name>firewallAction</name>
                        <value>ALLOW</value>
                    </ruleAction>
                </ruleActions>
            </rule>
        </setting>    
    </settings>
</configuration>
```
