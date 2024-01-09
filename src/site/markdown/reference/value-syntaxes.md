# Value Syntaxes

## Page Contents

-   [Address Range](#address-range)
-   [Comma Separated Values](#comma-separated-values)
-   [Digit](#digit)
-   [Firewall Action](#firewall-action)
-   [Host](#host)
-   [Log Action](#log-action)
-   [Non-negative Integer](#non-negative-integer)
-   [Performance Preferences](#performance-preferences)
-   [Port](#port)
-   [Port Range](#port-range)
-   [Port Ranges](#port-ranges)
-   [Positive Integer](#positive-integer)
-   [Rule](#rule)
-   [Scheme](#scheme)
-   [Selection Strategy](#selection-strategy)
    -   [Selection Strategies](#selection-strategies):
        -   [CYCLICAL](#cyclical)
        -   [RANDOM](#random)
-   [Socket Setting](#socket-setting)
    -   [Standard Socket Settings](#standard-socket-settings)
        -   [IP_TOS](#ip_tos)
        -   [PERF_PREFS](#perf_prefs)
        -   [SO_BROADCAST](#so_broadcast)
        -   [SO_KEEPALIVE](#so_keepalive)
        -   [SO_LINGER](#so_linger)
        -   [SO_RCVBUF](#so_rcvbuf)
        -   [SO_REUSEADDR](#so_reuseaddr)
        -   [SO_SNDBUF](#so_sndbuf)
        -   [SO_TIMEOUT](#so_timeout)
        -   [TCP_NODELAY](#tcp_nodelay)
-   [Socket Settings](#socket-settings)
-   [SOCKS5 Address](#socks5-address)
-   [SOCKS5 Command](#socks5-command)
-   [SOCKS5 GSS-API Method Protection Level](#socks5-gss-api-method-protection-level)
-   [SOCKS5 GSS-API Method Protection Levels](#socks5-gss-api-method-protection-levels)
-   [SOCKS5 Method](#socks5-method)
-   [SOCKS5 Methods](#socks5-methods)
-   [SOCKS5 Username Password Method User Repository](#socks5-username-password-method-user-repository)
    -   [SOCKS5 Username Password Method User Repositories](#socks5-username-password-method-user-repositories)
        -   [FileSourceUserRepository](#filesourceuserrepository)
        -   [StringSourceUserRepository](#stringsourceuserrepository)
-   [SOCKS Server URI](#socks-server-uri)
-   [Unsigned Byte](#unsigned-byte)

## Address Range

**Syntax:**

```text
ADDRESS|IP_ADDRESS1-IP_ADDRESS2|regex:REGULAR_EXPRESSION
```

## Comma Separated Values

**Syntax:**

```text
[VALUE1[,VALUE2[...]]]
```

**Element value:** java.lang.String

## Digit

**Syntax:**

```text
0-9
```

## Firewall Action

**Syntax:**

```text
ALLOW|DENY
```

**Values:**

-   `ALLOW`

-   `DENY`

## Host

**Syntax:**

```text
HOST_NAME|HOST_ADDRESS
```

**Description:**

A name or address of a node of a network

## Log Action

**Syntax:**

```text
LOG_AS_WARNING|LOG_AS_INFO
```

**Values:**

-   `LOG_AS_WARNING`: Log message as a warning message

-   `LOG_AS_INFO`: Log message as an informational message

## Non-negative Integer

**Syntax:**

```text
0-2147483647
```

## Performance Preferences

**Syntax:**

```text
DIGITDIGITDIGIT
```

**Description:**

Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth

**Element value:** [Digit](#digit)

## Port

**Syntax:**

```text
0-65535
```

**Description:**

An integer between 0 and 65535 (inclusive) that is assigned to uniquely identify a connection endpoint and to direct data to a host

## Port Range

**Syntax:**

```text
PORT|PORT1-PORT2
```

**Description:**

A range of port numbers between the provided minimum port number and the maximum port number (inclusive). A port range can also be one port number.

**Element value:** [Port](#port)

## Port Ranges

**Syntax:**

```text
[PORT_RANGE1[,PORT_RANGE2[...]]]
```

**Description:**

A comma separated list of port ranges.

**Element value:** [Port Range](#port-range)

## Positive Integer

**Syntax:**

```text
1-2147483647
```

## Rule

**Syntax:**

```text
[RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_RESULT1[,RULE_RESULT2[...]]
```

## Scheme

**Syntax:**

```text
socks5
```

**Values:**

-   `socks5`: SOCKS5 protocol version 5

## Selection Strategy

**Syntax:**

```text
CYCLICAL|RANDOM
```

### Selection Strategies

#### CYCLICAL

**Syntax:**

```text
CYCLICAL
```

**Description:**

Select the next in the cycle

#### RANDOM

**Syntax:**

```text
RANDOM
```

**Description:**

Select the next at random

## Socket Setting

**Syntax:**

```text
NAME=VALUE
```

**Description:**

A setting to be applied to a socket

### Standard Socket Settings

**Description:**

Standard settings applied to a socket

#### IP_TOS

**Syntax:**

```text
IP_TOS=UNSIGNED_BYTE
```

**Description:**

The type-of-service or traffic class field in the IP header for a TCP or UDP socket

**Value:** [Unsigned Byte](#unsigned-byte)

#### PERF_PREFS

**Syntax:**

```text
PERF_PREFS=PERFORMANCE_PREFERENCES
```

**Description:**

Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth

**Value:** [Performance Preferences](#performance-preferences)

#### SO_BROADCAST

**Syntax:**

```text
SO_BROADCAST=true|false
```

**Description:**

Can send broadcast datagrams

**Value:** java.lang.Boolean

#### SO_KEEPALIVE

**Syntax:**

```text
SO_KEEPALIVE=true|false
```

**Description:**

Keeps a TCP socket alive when no data has been exchanged in either direction

**Value:** java.lang.Boolean

#### SO_LINGER

**Syntax:**

```text
SO_LINGER=NONNEGATIVE_INTEGER
```

**Description:**

Linger on closing the TCP socket in seconds

**Value:** [Non-negative Integer](#non-negative-integer)

#### SO_RCVBUF

**Syntax:**

```text
SO_RCVBUF=POSITIVE_INTEGER
```

**Description:**

The receive buffer size

**Value:** [Positive Integer](#positive-integer)

#### SO_REUSEADDR

**Syntax:**

```text
SO_REUSEADDR=true|false
```

**Description:**

Can reuse socket address and port

**Value:** java.lang.Boolean

#### SO_SNDBUF

**Syntax:**

```text
SO_SNDBUF=POSITIVE_INTEGER
```

**Description:**

The send buffer size

**Value:** [Positive Integer](#positive-integer)

#### SO_TIMEOUT

**Syntax:**

```text
SO_TIMEOUT=NONNEGATIVE_INTEGER
```

**Description:**

The timeout in milliseconds on waiting for an idle socket

**Value:** [Non-negative Integer](#non-negative-integer)

#### TCP_NODELAY

**Syntax:**

```text
TCP_NODELAY=true|false
```

**Description:**

Disables Nagle's algorithm

**Value:** java.lang.Boolean

## Socket Settings

**Syntax:**

```text
[SOCKET_SETTING1[,SOCKET_SETTING2[...]]]
```

**Description:**

A comma separated list of socket settings.

**Element value:** [Socket Setting](#socket-setting)

## SOCKS5 Address

**Syntax:**

```text
DOMAINNAME|IPV4_ADDRESS|IPV6_ADDRESS
```

## SOCKS5 Command

**Syntax:**

```text
CONNECT|BIND|UDP_ASSOCIATE|RESOLVE
```

**Values:**

-   `CONNECT`: A request to the SOCKS server to connect to another server

-   `BIND`: A request to the SOCKS server to bind to another address and port in order to receive an inbound connection

-   `UDP_ASSOCIATE`: A request to the SOCKS server to associate a UDP socket for sending and receiving datagrams

-   `RESOLVE`: A request to the SOCKS server to resolve a host name

## SOCKS5 GSS-API Method Protection Level

**Syntax:**

```text
NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF
```

**Values:**

-   `NONE`: No protection

-   `REQUIRED_INTEG`: Required per-message integrity

-   `REQUIRED_INTEG_AND_CONF`: Required per-message integrity and confidentiality

## SOCKS5 GSS-API Method Protection Levels

**Syntax:**

```text
SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL2[...]]
```

**Element value:** [SOCKS5 GSS-API Method Protection Level](#socks5-gss-api-method-protection-level)

## SOCKS5 Method

**Syntax:**

```text
NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD
```

**Values:**

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required

-   `GSSAPI`: GSS-API authentication

-   `USERNAME_PASSWORD`: Username password authentication

## SOCKS5 Methods

**Syntax:**

```text
[SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]
```

**Element value:** [SOCKS5 Method](#socks5-method)

## SOCKS5 Username Password Method User Repository

**Syntax:**

```text
TYPE_NAME:INITIALIZATION_STRING
```

### SOCKS5 Username Password Method User Repositories

#### FileSourceUserRepository

**Syntax:**

```text
FileSourceUserRepository:FILE
```

**Description:**

User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

**Value:** java.io.File

#### StringSourceUserRepository

**Syntax:**

```text
StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]
```

**Description:**

User repository that handles the storage of the users from a provided string of a comma separated list of URL encoded username and password pairs

**Value:** java.lang.String

## SOCKS Server URI

**Syntax:**

```text
SCHEME://HOST[:PORT]
```

## Unsigned Byte

**Syntax:**

```text
0-255
```

