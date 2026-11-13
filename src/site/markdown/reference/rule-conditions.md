# Rule Conditions

## Contents

-   [General Rule Conditions](#general-rule-conditions)
-   [SOCKS Rule Conditions](#socks-rule-conditions)
-   [SOCKS5 Rule Conditions](#socks5-rule-conditions)
-   [All Rule Conditions](#all-rule-conditions)

## General Rule Conditions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#clientaddress"><code>clientAddress</code></a></td><td>Address Range</td><td>Specifies the client address</td></tr>
<tr><td><a href="#socksserveraddress"><code>socksServerAddress</code></a></td><td>Address Range</td><td>Specifies the SOCKS server address the client connected to</td></tr>
</table>

## SOCKS Rule Conditions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks-method"><code>socks.method</code></a></td><td>String</td><td>Specifies the negotiated method.</td></tr>
<tr><td><a href="#socks-reply-serverboundaddress"><code>socks.reply.serverBoundAddress</code></a></td><td>Address Range</td><td>Specifies the server bound address of the reply</td></tr>
<tr><td><a href="#socks-reply-serverboundport"><code>socks.reply.serverBoundPort</code></a></td><td>Port Range</td><td>Specifies the server bound port of the reply</td></tr>
<tr><td><a href="#socks-request-command"><code>socks.request.command</code></a></td><td>String</td><td>Specifies the command of the request</td></tr>
<tr><td><a href="#socks-request-desireddestinationaddress"><code>socks.request.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the desired destination address of the request</td></tr>
<tr><td><a href="#socks-request-desireddestinationport"><code>socks.request.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the desired destination port of the request</td></tr>
<tr><td><a href="#socks-secondreply-serverboundaddress"><code>socks.secondReply.serverBoundAddress</code></a></td><td>Address Range</td><td>Specifies the server bound address of the second reply (for the BIND request)</td></tr>
<tr><td><a href="#socks-secondreply-serverboundport"><code>socks.secondReply.serverBoundPort</code></a></td><td>Port Range</td><td>Specifies the server bound port of the second reply (for the BIND request)</td></tr>
<tr><td><a href="#socks-udp-inbound-desireddestinationaddress"><code>socks.udp.inbound.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the UDP inbound desired destination address</td></tr>
<tr><td><a href="#socks-udp-inbound-desireddestinationport"><code>socks.udp.inbound.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the UDP inbound desired destination port</td></tr>
<tr><td><a href="#socks-udp-inbound-sourceaddress"><code>socks.udp.inbound.sourceAddress</code></a></td><td>Address Range</td><td>Specifies the UDP inbound source address</td></tr>
<tr><td><a href="#socks-udp-inbound-sourceport"><code>socks.udp.inbound.sourcePort</code></a></td><td>Port Range</td><td>Specifies the UDP inbound source port</td></tr>
<tr><td><a href="#socks-udp-outbound-desireddestinationaddress"><code>socks.udp.outbound.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the UDP outbound desired destination address</td></tr>
<tr><td><a href="#socks-udp-outbound-desireddestinationport"><code>socks.udp.outbound.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the UDP outbound desired destination port</td></tr>
<tr><td><a href="#socks-udp-outbound-sourceaddress"><code>socks.udp.outbound.sourceAddress</code></a></td><td>Address Range</td><td>Specifies the UDP outbound source address</td></tr>
<tr><td><a href="#socks-udp-outbound-sourceport"><code>socks.udp.outbound.sourcePort</code></a></td><td>Port Range</td><td>Specifies the UDP outbound source port</td></tr>
<tr><td><a href="#socks-user"><code>socks.user</code></a></td><td>String</td><td>Specifies the user if any after the negotiated method</td></tr>
</table>

## SOCKS5 Rule Conditions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks5-method"><code>socks5.method</code></a></td><td>SOCKS5 Method</td><td>Specifies the negotiated method</td></tr>
<tr><td><a href="#socks5-reply-serverboundaddress"><code>socks5.reply.serverBoundAddress</code></a></td><td>Address Range</td><td>Specifies the server bound address of the reply</td></tr>
<tr><td><a href="#socks5-reply-serverboundport"><code>socks5.reply.serverBoundPort</code></a></td><td>Port Range</td><td>Specifies the server bound port of the reply</td></tr>
<tr><td><a href="#socks5-request-command"><code>socks5.request.command</code></a></td><td>SOCKS5 Request Command</td><td>Specifies the command of the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationaddress"><code>socks5.request.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the desired destination address of the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationport"><code>socks5.request.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the desired destination port of the request</td></tr>
<tr><td><a href="#socks5-secondreply-serverboundaddress"><code>socks5.secondReply.serverBoundAddress</code></a></td><td>Address Range</td><td>Specifies the server bound address of the second reply (for the BIND request)</td></tr>
<tr><td><a href="#socks5-secondreply-serverboundport"><code>socks5.secondReply.serverBoundPort</code></a></td><td>Port Range</td><td>Specifies the server bound port of the second reply (for the BIND request)</td></tr>
<tr><td><a href="#socks5-udp-inbound-desireddestinationaddress"><code>socks5.udp.inbound.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the UDP inbound desired destination address</td></tr>
<tr><td><a href="#socks5-udp-inbound-desireddestinationport"><code>socks5.udp.inbound.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the UDP inbound desired destination port</td></tr>
<tr><td><a href="#socks5-udp-inbound-sourceaddress"><code>socks5.udp.inbound.sourceAddress</code></a></td><td>Address Range</td><td>Specifies the UDP inbound source address</td></tr>
<tr><td><a href="#socks5-udp-inbound-sourceport"><code>socks5.udp.inbound.sourcePort</code></a></td><td>Port Range</td><td>Specifies the UDP inbound source port</td></tr>
<tr><td><a href="#socks5-udp-outbound-desireddestinationaddress"><code>socks5.udp.outbound.desiredDestinationAddress</code></a></td><td>Address Range</td><td>Specifies the UDP outbound desired destination address</td></tr>
<tr><td><a href="#socks5-udp-outbound-desireddestinationport"><code>socks5.udp.outbound.desiredDestinationPort</code></a></td><td>Port Range</td><td>Specifies the UDP outbound desired destination port</td></tr>
<tr><td><a href="#socks5-udp-outbound-sourceaddress"><code>socks5.udp.outbound.sourceAddress</code></a></td><td>Address Range</td><td>Specifies the UDP outbound source address</td></tr>
<tr><td><a href="#socks5-udp-outbound-sourceport"><code>socks5.udp.outbound.sourcePort</code></a></td><td>Port Range</td><td>Specifies the UDP outbound source port</td></tr>
<tr><td><a href="#socks5-user"><code>socks5.user</code></a></td><td>String</td><td>Specifies the user if any after the negotiated method</td></tr>
</table>

## All Rule Conditions

<a id="clientaddress"></a>
### clientAddress

**Description:** Specifies the client address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-method"></a>
### socks.method

**Description:** Specifies the negotiated method.

**Value Type:** [String](value-types.md#string)

<a id="socks-reply-serverboundaddress"></a>
### socks.reply.serverBoundAddress

**Description:** Specifies the server bound address of the reply

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-reply-serverboundport"></a>
### socks.reply.serverBoundPort

**Description:** Specifies the server bound port of the reply

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-request-command"></a>
### socks.request.command

**Description:** Specifies the command of the request

**Value Type:** [String](value-types.md#string)

<a id="socks-request-desireddestinationaddress"></a>
### socks.request.desiredDestinationAddress

**Description:** Specifies the desired destination address of the request

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-request-desireddestinationport"></a>
### socks.request.desiredDestinationPort

**Description:** Specifies the desired destination port of the request

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-secondreply-serverboundaddress"></a>
### socks.secondReply.serverBoundAddress

**Description:** Specifies the server bound address of the second reply (for the BIND request)

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-secondreply-serverboundport"></a>
### socks.secondReply.serverBoundPort

**Description:** Specifies the server bound port of the second reply (for the BIND request)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-udp-inbound-desireddestinationaddress"></a>
### socks.udp.inbound.desiredDestinationAddress

**Description:** Specifies the UDP inbound desired destination address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-udp-inbound-desireddestinationport"></a>
### socks.udp.inbound.desiredDestinationPort

**Description:** Specifies the UDP inbound desired destination port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-udp-inbound-sourceaddress"></a>
### socks.udp.inbound.sourceAddress

**Description:** Specifies the UDP inbound source address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-udp-inbound-sourceport"></a>
### socks.udp.inbound.sourcePort

**Description:** Specifies the UDP inbound source port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-udp-outbound-desireddestinationaddress"></a>
### socks.udp.outbound.desiredDestinationAddress

**Description:** Specifies the UDP outbound desired destination address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-udp-outbound-desireddestinationport"></a>
### socks.udp.outbound.desiredDestinationPort

**Description:** Specifies the UDP outbound desired destination port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-udp-outbound-sourceaddress"></a>
### socks.udp.outbound.sourceAddress

**Description:** Specifies the UDP outbound source address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks-udp-outbound-sourceport"></a>
### socks.udp.outbound.sourcePort

**Description:** Specifies the UDP outbound source port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-user"></a>
### socks.user

**Description:** Specifies the user if any after the negotiated method

**Value Type:** [String](value-types.md#string)

<a id="socks5-method"></a>
### socks5.method

**Description:** Specifies the negotiated method

**Value Type:** [SOCKS5 Method](value-types.md#socks5-method)

<a id="socks5-reply-serverboundaddress"></a>
### socks5.reply.serverBoundAddress

**Description:** Specifies the server bound address of the reply

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-reply-serverboundport"></a>
### socks5.reply.serverBoundPort

**Description:** Specifies the server bound port of the reply

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-request-command"></a>
### socks5.request.command

**Description:** Specifies the command of the request

**Value Type:** [SOCKS5 Request Command](value-types.md#socks5-request-command)

<a id="socks5-request-desireddestinationaddress"></a>
### socks5.request.desiredDestinationAddress

**Description:** Specifies the desired destination address of the request

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-request-desireddestinationport"></a>
### socks5.request.desiredDestinationPort

**Description:** Specifies the desired destination port of the request

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-secondreply-serverboundaddress"></a>
### socks5.secondReply.serverBoundAddress

**Description:** Specifies the server bound address of the second reply (for the BIND request)

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-secondreply-serverboundport"></a>
### socks5.secondReply.serverBoundPort

**Description:** Specifies the server bound port of the second reply (for the BIND request)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-udp-inbound-desireddestinationaddress"></a>
### socks5.udp.inbound.desiredDestinationAddress

**Description:** Specifies the UDP inbound desired destination address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-udp-inbound-desireddestinationport"></a>
### socks5.udp.inbound.desiredDestinationPort

**Description:** Specifies the UDP inbound desired destination port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-udp-inbound-sourceaddress"></a>
### socks5.udp.inbound.sourceAddress

**Description:** Specifies the UDP inbound source address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-udp-inbound-sourceport"></a>
### socks5.udp.inbound.sourcePort

**Description:** Specifies the UDP inbound source port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-udp-outbound-desireddestinationaddress"></a>
### socks5.udp.outbound.desiredDestinationAddress

**Description:** Specifies the UDP outbound desired destination address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-udp-outbound-desireddestinationport"></a>
### socks5.udp.outbound.desiredDestinationPort

**Description:** Specifies the UDP outbound desired destination port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-udp-outbound-sourceaddress"></a>
### socks5.udp.outbound.sourceAddress

**Description:** Specifies the UDP outbound source address

**Value Type:** [Address Range](value-types.md#address-range)

<a id="socks5-udp-outbound-sourceport"></a>
### socks5.udp.outbound.sourcePort

**Description:** Specifies the UDP outbound source port

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-user"></a>
### socks5.user

**Description:** Specifies the user if any after the negotiated method

**Value Type:** [String](value-types.md#string)

<a id="socksserveraddress"></a>
### socksServerAddress

**Description:** Specifies the SOCKS server address the client connected to

**Value Type:** [Address Range](value-types.md#address-range)

