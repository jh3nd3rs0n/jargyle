# Value Types

## Page Contents

-   [Address Range](#address-range)
-   [Boolean](#boolean)
-   [Comma Separated Values](#comma-separated-values)
-   [File](#file)
-   [Firewall Action](#firewall-action)
-   [Host](#host)
-   [Host Address Type](#host-address-type)
-   [Host Address Types](#host-address-types)
-   [Integer](#integer)
-   [Log Action](#log-action)
-   [Network Interface](#network-interface)
-   [Non-negative Integer](#non-negative-integer)
-   [Oid](#oid)
-   [Port](#port)
-   [Port Range](#port-range)
-   [Port Ranges](#port-ranges)
-   [Positive Integer](#positive-integer)
-   [Rule](#rule)
-   [Scheme](#scheme)
-   [Selection Strategy](#selection-strategy)
    -   [Selection Strategies](#selection-strategies)
        -   [CYCLICAL](#cyclical)
        -   [RANDOM](#random)
-   [Socket Setting](#socket-setting)
    -   [Standard Socket Settings](#standard-socket-settings)
        -   [IP_TOS](#ip_tos)
        -   [SO_BROADCAST](#so_broadcast)
        -   [SO_KEEPALIVE](#so_keepalive)
        -   [SO_LINGER](#so_linger)
        -   [SO_RCVBUF](#so_rcvbuf)
        -   [SO_REUSEADDR](#so_reuseaddr)
        -   [SO_SNDBUF](#so_sndbuf)
        -   [SO_TIMEOUT](#so_timeout)
        -   [TCP_NODELAY](#tcp_nodelay)
-   [Socket Settings](#socket-settings)
-   [SOCKS Server URI](#socks-server-uri)
-   [SOCKS5 Address](#socks5-address)
-   [SOCKS5 GSS-API Method Protection Level](#socks5-gss-api-method-protection-level)
-   [SOCKS5 GSS-API Method Protection Levels](#socks5-gss-api-method-protection-levels)
-   [SOCKS5 Method](#socks5-method)
-   [SOCKS5 Methods](#socks5-methods)
-   [SOCKS5 Request Command](#socks5-request-command)
-   [SOCKS5 Username Password Method User Repository](#socks5-username-password-method-user-repository)
    -   [SOCKS5 Username Password Method User Repositories](#socks5-username-password-method-user-repositories)
        -   [FileSourceUserRepository](#filesourceuserrepository)
        -   [StringSourceUserRepository](#stringsourceuserrepository)
-   [String](#string)
-   [Unsigned Byte](#unsigned-byte)
-   [User Info](#user-info)

## Address Range

**Syntax:**

```text
ADDRESS|IP_ADDRESS1-IP_ADDRESS2|regex:REGULAR_EXPRESSION
```

**Description:** 

**Class:** `com.github.jh3nd3rs0n.jargyle.server.AddressRange`

## Boolean

**Syntax:**

```text
true|false
```

**Description:** A value of either of the following: true or false

**Class:** `java.lang.Boolean`

## Comma Separated Values

**Syntax:**

```text
[VALUE1[,VALUE2[...]]]
```

**Description:** A comma separated list of values

**Element Value Type:** [String](#string)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues`

## File

**Syntax:**

```text
ABSOLUTE_OR_RELATIVE_PATH_TO_FILE
```

**Description:** An absolute or relative path to a file

**Class:** `java.io.File`

## Firewall Action

**Syntax:**

```text
ALLOW|DENY
```

**Description:** 

**Values:**

-   `ALLOW` : 
-   `DENY` : 

**Class:** `com.github.jh3nd3rs0n.jargyle.server.FirewallAction`

## Host

**Syntax:**

```text
HOST_NAME|HOST_ADDRESS
```

**Description:** A name or address of a node of a network

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.Host`

## Host Address Type

**Syntax:**

```text
HOST_IPV4_ADDRESS|HOST_IPV6_ADDRESS
```

**Description:** Type of host address

**Values:**

-   `HOST_IPV4_ADDRESS` : Host IPv4 address
-   `HOST_IPV6_ADDRESS` : Host IPv6 address

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.HostAddressType`

## Host Address Types

**Syntax:**

```text
[HOST_ADDRESS_TYPE1[,HOST_ADDRESS_TYPE2[...]]]
```

**Description:** A comma separated list of host address types

**Element Value Type:** [Host Address Type](#host-address-type)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes`

## Integer

**Syntax:**

```text
-2147483648-2147483647
```

**Description:** An integer between -2147483648 and 2147483647 (inclusive)

**Class:** `java.lang.Integer`

## Log Action

**Syntax:**

```text
LOG_AS_WARNING|LOG_AS_INFO
```

**Description:** 

**Values:**

-   `LOG_AS_WARNING` : Log message as a warning message
-   `LOG_AS_INFO` : Log message as an informational message

**Class:** `com.github.jh3nd3rs0n.jargyle.server.LogAction`

## Network Interface

**Syntax:**

```text
NETWORK_INTERFACE_NAME
```

**Description:** A name of an interface between the local system and a network (for example, "le0")

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.NetInterface`

## Non-negative Integer

**Syntax:**

```text
0-2147483647
```

**Description:** An integer between 0 and 2147483647 (inclusive)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger`

## Oid

**Syntax:**

Please see the Javadocs for the following member(s) for the possible syntax for the String parameter:

-   `public org.ietf.jgss.Oid(java.lang.String) throws org.ietf.jgss.GSSException`

**Description:** Please see the Javadocs for the following class for its description: `org.ietf.jgss.Oid`

**Class:** `org.ietf.jgss.Oid`

## Port

**Syntax:**

```text
0-65535
```

**Description:** An integer between 0 and 65535 (inclusive) that is assigned to uniquely identify a connection endpoint and to direct data to a node of a network

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.Port`

## Port Range

**Syntax:**

```text
PORT|PORT1-PORT2
```

**Description:** A range of port numbers between the provided minimum port number and the maximum port number (inclusive). A port range can also be one port number.

**Element Value Type:** [Port](#port)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.PortRange`

## Port Ranges

**Syntax:**

```text
[PORT_RANGE1[,PORT_RANGE2[...]]]
```

**Description:** A comma separated list of port ranges.

**Element Value Type:** [Port Range](#port-range)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.PortRanges`

## Positive Integer

**Syntax:**

```text
1-2147483647
```

**Description:** An integer between 1 and 2147483647 (inclusive)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger`

## Rule

**Syntax:**

```text
[RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_ACTION1[,RULE_ACTION2[...]]
```

**Description:** 

**Class:** `com.github.jh3nd3rs0n.jargyle.server.Rule`

## Scheme

**Syntax:**

```text
socks5
```

**Description:** Specifies what SOCKS protocol is to be used to access the SOCKS server

**Values:**

-   `socks5` : SOCKS protocol version 5

**Class:** `com.github.jh3nd3rs0n.jargyle.client.Scheme`

## Selection Strategy

**Syntax:**

```text
CYCLICAL|RANDOM
```

**Description:** 

**Class:** `com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy`

### Selection Strategies

**Description:** 

#### CYCLICAL

**Syntax:**

```text
CYCLICAL
```

**Description:** Select the next in the cycle

#### RANDOM

**Syntax:**

```text
RANDOM
```

**Description:** Select the next at random

## Socket Setting

**Syntax:**

```text
NAME=VALUE
```

**Description:** A setting to be applied to a socket

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting`

### Standard Socket Settings

**Description:** Standard settings applied to a socket

#### IP_TOS

**Syntax:**

```text
IP_TOS=UNSIGNED_BYTE
```

**Description:** The type-of-service or traffic class field in the IP header for a TCP or UDP socket

**Value Type:** [Unsigned Byte](#unsigned-byte)

#### SO_BROADCAST

**Syntax:**

```text
SO_BROADCAST=true|false
```

**Description:** Can send broadcast datagrams

**Value Type:** [Boolean](#boolean)

#### SO_KEEPALIVE

**Syntax:**

```text
SO_KEEPALIVE=true|false
```

**Description:** Keeps a TCP socket alive when no data has been exchanged in either direction

**Value Type:** [Boolean](#boolean)

#### SO_LINGER

**Syntax:**

```text
SO_LINGER=-2147483648-2147483647
```

**Description:** Linger on closing the TCP socket in seconds (disabled if the number of seconds is negative)

**Value Type:** [Integer](#integer)

#### SO_RCVBUF

**Syntax:**

```text
SO_RCVBUF=POSITIVE_INTEGER
```

**Description:** The receive buffer size

**Value Type:** [Positive Integer](#positive-integer)

#### SO_REUSEADDR

**Syntax:**

```text
SO_REUSEADDR=true|false
```

**Description:** Can reuse socket address and port

**Value Type:** [Boolean](#boolean)

#### SO_SNDBUF

**Syntax:**

```text
SO_SNDBUF=POSITIVE_INTEGER
```

**Description:** The send buffer size

**Value Type:** [Positive Integer](#positive-integer)

#### SO_TIMEOUT

**Syntax:**

```text
SO_TIMEOUT=NON_NEGATIVE_INTEGER
```

**Description:** The timeout in milliseconds on waiting for an idle socket

**Value Type:** [Non-negative Integer](#non-negative-integer)

#### TCP_NODELAY

**Syntax:**

```text
TCP_NODELAY=true|false
```

**Description:** Disables Nagle's algorithm

**Value Type:** [Boolean](#boolean)

## Socket Settings

**Syntax:**

```text
[SOCKET_SETTING1[,SOCKET_SETTING2[...]]]
```

**Description:** A comma separated list of socket settings.

**Element Value Type:** [Socket Setting](#socket-setting)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings`

## SOCKS Server URI

**Syntax:**

```text
SCHEME://[USER_INFO@]HOST[:PORT]
```

**Description:** The URI of the SOCKS server

**Class:** `com.github.jh3nd3rs0n.jargyle.client.SocksServerUri`

## SOCKS5 Address

**Syntax:**

```text
DOMAIN_NAME|IPV4_ADDRESS|IPV6_ADDRESS
```

**Description:** An identifier or a virtual location of a node of a network

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address`

## SOCKS5 GSS-API Method Protection Level

**Syntax:**

```text
NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF|SELECTIVE_INTEG_OR_CONF
```

**Description:** Security context protection level

**Values:**

-   `NONE` : No per-message protection
-   `REQUIRED_INTEG` : Required per-message integrity
-   `REQUIRED_INTEG_AND_CONF` : Required per-message integrity and confidentiality
-   `SELECTIVE_INTEG_OR_CONF` : Selective per-message integrity or confidentiality based on local client and server configurations

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevel`

## SOCKS5 GSS-API Method Protection Levels

**Syntax:**

```text
SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL2[...]]
```

**Description:** A comma separated list of security context protection levels

**Element Value Type:** [SOCKS5 GSS-API Method Protection Level](#socks5-gss-api-method-protection-level)

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels`

## SOCKS5 Method

**Syntax:**

```text
NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD
```

**Description:** Authentication method

**Values:**

-   `NO_AUTHENTICATION_REQUIRED` : No authentication required
-   `GSSAPI` : GSS-API authentication
-   `USERNAME_PASSWORD` : Username password authentication

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method`

## SOCKS5 Methods

**Syntax:**

```text
[SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]
```

**Description:** A comma-separated list of SOCKS5 authentication methods

**Element Value Type:** [SOCKS5 Method](#socks5-method)

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods`

## SOCKS5 Request Command

**Syntax:**

```text
CONNECT|BIND|UDP_ASSOCIATE|RESOLVE
```

**Description:** Type of request to the SOCKS server

**Values:**

-   `CONNECT` : A request to the SOCKS server to connect to another server
-   `BIND` : A request to the SOCKS server to bind to another address and port in order to receive an inbound connection
-   `UDP_ASSOCIATE` : A request to the SOCKS server to establish an association within the UDP relay process to handle UDP datagrams
-   `RESOLVE` : A request to the SOCKS server to resolve a host name

**Class:** `com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command`

## SOCKS5 Username Password Method User Repository

**Syntax:**

```text
TYPE_NAME:INITIALIZATION_STRING
```

**Description:** 

**Class:** `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository`

### SOCKS5 Username Password Method User Repositories

**Description:** 

#### FileSourceUserRepository

**Syntax:**

```text
FileSourceUserRepository:FILE
```

**Description:** User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

**Value Type:** [File](#file)

#### StringSourceUserRepository

**Syntax:**

```text
StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]
```

**Description:** User repository that handles the storage of the users from a provided string of a comma separated list of URL encoded username and password pairs

**Value Type:** [String](#string)

## String

**Syntax:**

```text
[CHARACTER1[CHARACTER2[...]]]
```

**Description:** A list of characters

**Class:** `java.lang.String`

## Unsigned Byte

**Syntax:**

```text
0-255
```

**Description:** An integer between 0 and 255 (inclusive)

**Class:** `com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte`

## User Info

**Syntax:**

```text
(See https://www.ietf.org/rfc/rfc2396.txt 3.2.2. Server-based Naming Authority)
```

**Description:** Specifies the user information to be used to access the SOCKS server

**Class:** `com.github.jh3nd3rs0n.jargyle.client.UserInfo`

