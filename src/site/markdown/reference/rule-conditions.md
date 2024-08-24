# Rule Conditions

## Page Contents

-   [General Rule Conditions](#general-rule-conditions)
    -   [clientAddress](#clientaddress)
    -   [socksServerAddress](#socksserveraddress)
-   [SOCKS5 Rule Conditions](#socks5-rule-conditions)
    -   [socks5.method](#socks5-method)
    -   [socks5.reply.serverBoundAddress](#socks5-reply-serverboundaddress)
    -   [socks5.reply.serverBoundPort](#socks5-reply-serverboundport)
    -   [socks5.request.command](#socks5-request-command)
    -   [socks5.request.desiredDestinationAddress](#socks5-request-desireddestinationaddress)
    -   [socks5.request.desiredDestinationPort](#socks5-request-desireddestinationport)
    -   [socks5.secondReply.serverBoundAddress](#socks5-secondreply-serverboundaddress)
    -   [socks5.secondReply.serverBoundPort](#socks5-secondreply-serverboundport)
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

### socks5.method

**Syntax:**

```text
socks5.method=SOCKS5_METHOD
```

**Description:**

Specifies the negotiated method

**Value:** [SOCKS5 Method](value-syntaxes.md#socks5-method)

### socks5.reply.serverBoundAddress

**Syntax:**

```text
socks5.reply.serverBoundAddress=ADDRESS_RANGE
```

**Description:**

Specifies the server bound address of the reply

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.reply.serverBoundPort

**Syntax:**

```text
socks5.reply.serverBoundPort=PORT_RANGE
```

**Description:**

Specifies the server bound port of the reply

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.request.command

**Syntax:**

```text
socks5.request.command=SOCKS5_REQUEST_COMMAND
```

**Description:**

Specifies the command of the request

**Value:** [SOCKS5 Request Command](value-syntaxes.md#socks5-request-command)

### socks5.request.desiredDestinationAddress

**Syntax:**

```text
socks5.request.desiredDestinationAddress=ADDRESS_RANGE
```

**Description:**

Specifies the desired destination address of the request

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.request.desiredDestinationPort

**Syntax:**

```text
socks5.request.desiredDestinationPort=PORT_RANGE
```

**Description:**

Specifies the desired destination port of the request

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.secondReply.serverBoundAddress

**Syntax:**

```text
socks5.secondReply.serverBoundAddress=ADDRESS_RANGE
```

**Description:**

Specifies the server bound address of the second reply (for the BIND command)

**Value:** [Address Range](value-syntaxes.md#address-range)

### socks5.secondReply.serverBoundPort

**Syntax:**

```text
socks5.secondReply.serverBoundPort=PORT_RANGE
```

**Description:**

Specifies the server bound port of the second reply (for the BIND command)

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

Specifies the user if any after the negotiated method

**Value:** java.lang.String

