# Rule Conditions

## Page Contents

-   [General Rule Conditions](#general-rule-conditions)
    -   [clientAddress](#clientaddress)
    -   [socksServerAddress](#socksserveraddress)
-   [SOCKS5 Rule Conditions](#socks5-rule-conditions)
    -   [socks5.command](#socks5-command)
    -   [socks5.desiredDestinationAddress](#socks5-desireddestinationaddress)
    -   [socks5.desiredDestinationPort](#socks5-desireddestinationport)
    -   [socks5.method](#socks5-method)
    -   [socks5.secondServerBoundAddress](#socks5-secondserverboundaddress)
    -   [socks5.secondServerBoundPort](#socks5-secondserverboundport)
    -   [socks5.serverBoundAddress](#socks5-serverboundaddress)
    -   [socks5.serverBoundPort](#socks5-serverboundport)
    -   [socks5.udp.inbound.desiredDestinationAddress](#socks5-udp-inbound-desireddestinationaddress)
    -   [socks5.udp.inbound.desiredDestinationPort](#socks5-udp-inbound-desireddestinationport)
    -   [socks5.udp.inbound.sourceAddress](#socks5-udp-inbound-sourceaddress)
    -   [socks5.udp.inbound.sourcePort](#socks5-udp-inbound-sourceport)
    -   [socks5.udp.outbound.desiredDestinationAddress](#socks5-udp-outbound-desireddestinationaddress)
    -   [socks5.udp.outbound.desiredDestinationPort](#socks5-udp-outbound-desireddestinationport)
    -   [socks5.udp.outbound.sourceAddress](#socks5-udp-outbound-sourceaddress)
    -   [socks5.udp.outbound.sourcePort](#socks5-udp-outbound-sourceport)
    -   [socks5.user](#socks5-user)

## General Rule Conditions

### clientAddress

**Syntax:**

```text
clientAddress=ADDRESS_RANGE
```

**Description:**

Specifies the client address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socksServerAddress

**Syntax:**

```text
socksServerAddress=ADDRESS_RANGE
```

**Description:**

Specifies the SOCKS server address the client connected to

**Value:** [Address Range](value-syntaxes.md#address-range)

## SOCKS5 Rule Conditions

### socks5.command

**Syntax:**

```text
socks5.command=SOCKS5_COMMAND
```

**Description:**

Specifies the SOCKS5 command

**Value:** [SOCKS5 Command](value-syntaxes.md#socks5-command)

### socks5.desiredDestinationAddress

**Syntax:**

```text
socks5.desiredDestinationAddress=ADDRESS_RANGE
```

**Description:**

Specifies the desired destination address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.desiredDestinationPort

**Syntax:**

```text
socks5.desiredDestinationPort=PORT_RANGE
```

**Description:**

Specifies the desired destination port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.method

**Syntax:**

```text
socks5.method=SOCKS5_METHOD
```

**Description:**

Specifies the negotiated SOCKS5 method

**Value:** [SOCKS5 Method](value-syntaxes.md#socks5-method)

### socks5.secondServerBoundAddress

**Syntax:**

```text
socks5.secondServerBoundAddress=ADDRESS_RANGE
```

**Description:**

Specifies the second server bound address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.secondServerBoundPort

**Syntax:**

```text
socks5.secondServerBoundPort=PORT_RANGE
```

**Description:**

Specifies the second server bound port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.serverBoundAddress

**Syntax:**

```text
socks5.serverBoundAddress=ADDRESS_RANGE
```

**Description:**

Specifies the server bound address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.serverBoundPort

**Syntax:**

```text
socks5.serverBoundPort=PORT_RANGE
```

**Description:**

Specifies the server bound port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.udp.inbound.desiredDestinationAddress

**Syntax:**

```text
socks5.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE
```

**Description:**

Specifies the UDP inbound desired destination address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.udp.inbound.desiredDestinationPort

**Syntax:**

```text
socks5.udp.inbound.desiredDestinationPort=PORT_RANGE
```

**Description:**

Specifies the UDP inbound desired destination port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.udp.inbound.sourceAddress

**Syntax:**

```text
socks5.udp.inbound.sourceAddress=ADDRESS_RANGE
```

**Description:**

Specifies the UDP inbound source address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.udp.inbound.sourcePort

**Syntax:**

```text
socks5.udp.inbound.sourcePort=PORT_RANGE
```

**Description:**

Specifies the UDP inbound source port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.udp.outbound.desiredDestinationAddress

**Syntax:**

```text
socks5.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE
```

**Description:**

Specifies the UDP outbound desired destination address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.udp.outbound.desiredDestinationPort

**Syntax:**

```text
socks5.udp.outbound.desiredDestinationPort=PORT_RANGE
```

**Description:**

Specifies the UDP outbound desired destination port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.udp.outbound.sourceAddress

**Syntax:**

```text
socks5.udp.outbound.sourceAddress=ADDRESS_RANGE
```

**Description:**

Specifies the UDP outbound source address

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.udp.outbound.sourcePort

**Syntax:**

```text
socks5.udp.outbound.sourcePort=PORT_RANGE
```

**Description:**

Specifies the UDP outbound source port

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.user

**Syntax:**

```text
socks5.user=USER
```

**Description:**

Specifies the user if any after the negotiated SOCKS5 method

**Value:** java.lang.String

