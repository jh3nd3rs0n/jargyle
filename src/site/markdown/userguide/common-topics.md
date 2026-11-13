# Common Topics

## Contents

-   [Overview](#overview)
-   [Value Types](#value-types)
    -   [Host Address Types](#host-address-types)
    -   [Port Number Ranges](#port-number-ranges)
    -   [Socket Settings](#socket-settings)

## Overview

This document discusses topics common to [client](client-configuration.md) and 
[server](server-configuration.md) configurations.

## Value Types

The subtopics that follow discusses value types that are common in both client 
properties and server configuration settings.

### Host Address Types

From the command line and from the APIs, the specified host address types
consist of a comma separated list of the following acceptable values:

-   `HOST_IPV4_ADDRESS`: Host IPv4 address
-   `HOST_IPV6_ADDRESS`: Host IPv6 address

Host address types examples:

```text
HOST_IPV4_ADDRESS
```

```text
HOST_IPV6_ADDRESS
```

```text
HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS
```

In the server configuration file, the specified host address types are
expressed as a `<hostAddressTypes/>` XML element with `<hostAddressType>` XML
elements each containing the aforementioned acceptable values.

Host address types examples:

```xml
<hostAddressTypes>
    <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
</hostAddressTypes>
```

```xml
<hostAddressTypes>
    <hostAddressType>HOST_IPV6_ADDRESS</hostAddressType>
</hostAddressTypes>
```

```xml
<hostAddressTypes>
    <hostAddressType>HOST_IPV4_ADDRESS</hostAddressType>
    <hostAddressType>HOST_IPV6_ADDRESS</hostAddressType>
</hostAddressTypes>
```

### Port Number Ranges

From the command line, from the APIs, and in the server configuration file, a
port number range can consist of either of the following: an integer between
0 (inclusive) and 65535 (inclusive) or an integer between 0 (inclusive) and
65535 (inclusive) followed by a hyphen followed by another integer between 0
(inclusive) and 65535 (inclusive) that is greater than or equal to the
previous integer.

Port number range examples:

```text
1080
```

```text
20400-23500
```

From the command line and from the APIs, port number ranges consist of a comma
separated list of port number ranges.

Port number ranges examples:

```text
5000,6500-7500
```

```text
3500-4500,6000,9000-10000
```

In the server configuration file, port number ranges are expressed as a
`<portRanges/>` XML element with `<portRange>` XML elements each containing a
port number range.

Port number ranges examples:

```xml
<portRanges>
    <portRange>5000</portRange>
    <portRange>6500-7500</portRange>
</portRanges>
```

```xml
<portRanges>
    <portRange>3500-4500</portRange>
    <portRange>6000</portRange>
    <portRange>9000-10000</portRange>
</portRanges>
```

### Socket Settings

From the command line and from the APIs, socket settings consist of a comma
separated list of socket settings. Each socket setting consists of the syntax
of `NAME=VALUE` where `NAME` is expressed as the name of the socket setting
and `VALUE` is expressed as the value assigned to the socket setting.

Socket settings example:

```text
SO_RCVBUF=256,SO_SNDBUF=256
```

In the server configuration file, socket settings are expressed in a
`<socketSettings/>` XML element with zero to many `<socketSetting/>` XML
elements. Each `<socketSetting/>` XML element contains a `<name/>` XML
element for the name of the socket setting and the `<value/>` XML element for
the value assigned to the socket setting.

Socket settings example:

```xml
<socketSettings>
    <socketSetting>
        <name>SO_RCVBUF</name>
        <value>256</value>
    </socketSetting>
    <socketSetting>
        <name>SO_SNDBUF</name>
        <value>256</value>
    </socketSetting>
</socketSettings>
```

A complete listing of socket settings can be found 
[here](../reference/value-types.md#standard-socket-settings).