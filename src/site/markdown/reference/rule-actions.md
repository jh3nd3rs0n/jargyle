# Rule Actions

## Contents

-   [General Rule Actions](#general-rule-actions)
-   [SOCKS5 Rule Actions](#socks5-rule-actions)
-   [All Rule Actions](#all-rule-actions)

## General Rule Actions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#bindhost"><code>bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all sockets</td></tr>
<tr><td><a href="#bindhostaddresstype"><code>bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all sockets (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#bindtcpportrange"><code>bindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#bindudpportrange"><code>bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule action specifying another port ranges)</td></tr>
<tr><td><a href="#clientsocketsetting"><code>clientSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the client socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#externalfacingbindhost"><code>externalFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all external-facing sockets</td></tr>
<tr><td><a href="#externalfacingbindhostaddresstype"><code>externalFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#externalfacingbindtcpportrange"><code>externalFacingBindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#externalfacingbindudpportrange"><code>externalFacingBindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#externalfacingnetinterface"><code>externalFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all external-facing sockets</td></tr>
<tr><td><a href="#externalfacingsocketsetting"><code>externalFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#firewallaction"><code>firewallAction</code></a></td><td>Firewall Action</td><td>Specifies the firewall action to take</td></tr>
<tr><td><a href="#firewallactionallowlimit"><code>firewallActionAllowLimit</code></a></td><td>Non-negative Integer</td><td>Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW</td></tr>
<tr><td><a href="#firewallactionallowlimitreachedlogaction"><code>firewallActionAllowLimitReachedLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached</td></tr>
<tr><td><a href="#firewallactionlogaction"><code>firewallActionLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the firewall action is applied</td></tr>
<tr><td><a href="#internalfacingbindhost"><code>internalFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all internal-facing sockets</td></tr>
<tr><td><a href="#internalfacingbindhostaddresstype"><code>internalFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#internalfacingbindudpportrange"><code>internalFacingBindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#internalfacingnetinterface"><code>internalFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all internal-facing sockets</td></tr>
<tr><td><a href="#internalfacingsocketsetting"><code>internalFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#netinterface"><code>netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all sockets</td></tr>
<tr><td><a href="#routeselectionlogaction"><code>routeSelectionLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if a route ID is selected</td></tr>
<tr><td><a href="#routeselectionstrategy"><code>routeSelectionStrategy</code></a></td><td>Selection Strategy</td><td>Specifies the selection strategy for the next route ID</td></tr>
<tr><td><a href="#selectablerouteid"><code>selectableRouteId</code></a></td><td>String</td><td>Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)</td></tr>
<tr><td><a href="#socketsetting"><code>socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all sockets (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
</table>

## SOCKS5 Rule Actions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks5-onbindrequest-inboundsocketsetting"><code>socks5.onBindRequest.inboundSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the inbound socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindhost"><code>socks5.onBindRequest.listenBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindhostaddresstype"><code>socks5.onBindRequest.listenBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the listen socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindportrange"><code>socks5.onBindRequest.listenBindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onbindrequest-listennetinterface"><code>socks5.onBindRequest.listenNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-listensocketsetting"><code>socks5.onBindRequest.listenSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the listen socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onbindrequest-relaybuffersize"><code>socks5.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayidletimeout"><code>socks5.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayinboundbandwidthlimit"><code>socks5.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayoutboundbandwidthlimit"><code>socks5.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-preparetargetfacingsocket"><code>socks5.onConnectRequest.prepareTargetFacingSocket</code></a></td><td>Boolean</td><td>Specifies the boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relaybuffersize"><code>socks5.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayidletimeout"><code>socks5.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayinboundbandwidthlimit"><code>socks5.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayoutboundbandwidthlimit"><code>socks5.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindhost"><code>socks5.onConnectRequest.targetFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindhostaddresstype"><code>socks5.onConnectRequest.targetFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the target-facing socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindportrange"><code>socks5.onConnectRequest.targetFacingBindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the target-facing socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingconnecttimeout"><code>socks5.onConnectRequest.targetFacingConnectTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on waiting for the target-facing socket to connect</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingnetinterface"><code>socks5.onConnectRequest.targetFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingsocketsetting"><code>socks5.onConnectRequest.targetFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the target-facing socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingbindhost"><code>socks5.onRequest.externalFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all external-facing sockets</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingbindhostaddresstype"><code>socks5.onRequest.externalFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingbindtcpportrange"><code>socks5.onRequest.externalFacingBindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingbindudpportrange"><code>socks5.onRequest.externalFacingBindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingnetinterface"><code>socks5.onRequest.externalFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all external-facing sockets</td></tr>
<tr><td><a href="#socks5-onrequest-externalfacingsocketsetting"><code>socks5.onRequest.externalFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onrequest-internalfacingbindhost"><code>socks5.onRequest.internalFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all internal-facing sockets</td></tr>
<tr><td><a href="#socks5-onrequest-internalfacingbindhostaddresstype"><code>socks5.onRequest.internalFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onrequest-internalfacingbindudpportrange"><code>socks5.onRequest.internalFacingBindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onrequest-internalfacingnetinterface"><code>socks5.onRequest.internalFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all internal-facing sockets</td></tr>
<tr><td><a href="#socks5-onrequest-internalfacingsocketsetting"><code>socks5.onRequest.internalFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onrequest-relaybuffersize"><code>socks5.onRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onrequest-relayidletimeout"><code>socks5.onRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onrequest-relayinboundbandwidthlimit"><code>socks5.onRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onrequest-relayoutboundbandwidthlimit"><code>socks5.onRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindhost"><code>socks5.onUdpAssociateRequest.clientFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindhostaddresstype"><code>socks5.onUdpAssociateRequest.clientFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindportrange"><code>socks5.onUdpAssociateRequest.clientFacingBindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingnetinterface"><code>socks5.onUdpAssociateRequest.clientFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingsocketsetting"><code>socks5.onUdpAssociateRequest.clientFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindhost"><code>socks5.onUdpAssociateRequest.peerFacingBindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindhostaddresstype"><code>socks5.onUdpAssociateRequest.peerFacingBindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindportrange"><code>socks5.onUdpAssociateRequest.peerFacingBindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingnetinterface"><code>socks5.onUdpAssociateRequest.peerFacingNetInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingsocketsetting"><code>socks5.onUdpAssociateRequest.peerFacingSocketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relaybuffersize"><code>socks5.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayidletimeout"><code>socks5.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-request-desireddestinationaddressredirect"><code>socks5.request.desiredDestinationAddressRedirect</code></a></td><td>SOCKS5 Address</td><td>Specifies the desired destination address redirect for the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationportredirect"><code>socks5.request.desiredDestinationPortRedirect</code></a></td><td>Port</td><td>Specifies the desired destination port redirect for the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationredirectlogaction"><code>socks5.request.desiredDestinationRedirectLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the desired destination of the request is redirected</td></tr>
</table>

## All Rule Actions

### bindHost

**Description:** Specifies the binding host name or address for all sockets

**Value Type:** [Host](value-types.md#host)

### bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all sockets (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### bindTcpPortRange

**Description:** Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### bindUdpPortRange

**Description:** Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule action specifying another port ranges)

**Value Type:** [Port Range](value-types.md#port-range)

### clientSocketSetting

**Description:** Specifies a socket setting for the client socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### externalFacingBindHost

**Description:** Specifies the binding host name or address for all external-facing sockets

**Value Type:** [Host](value-types.md#host)

### externalFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### externalFacingBindTcpPortRange

**Description:** Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### externalFacingBindUdpPortRange

**Description:** Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### externalFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for all external-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### externalFacingSocketSetting

**Description:** Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### firewallAction

**Description:** Specifies the firewall action to take

**Value Type:** [Firewall Action](value-types.md#firewall-action)

### firewallActionAllowLimit

**Description:** Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

### firewallActionAllowLimitReachedLogAction

**Description:** Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

**Value Type:** [Log Action](value-types.md#log-action)

### firewallActionLogAction

**Description:** Specifies the logging action to take if the firewall action is applied

**Value Type:** [Log Action](value-types.md#log-action)

### internalFacingBindHost

**Description:** Specifies the binding host name or address for all internal-facing sockets

**Value Type:** [Host](value-types.md#host)

### internalFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### internalFacingBindUdpPortRange

**Description:** Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### internalFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for all internal-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### internalFacingSocketSetting

**Description:** Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### netInterface

**Description:** Specifies the network interface that provides a binding host address for all sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### routeSelectionLogAction

**Description:** Specifies the logging action to take if a route ID is selected

**Value Type:** [Log Action](value-types.md#log-action)

### routeSelectionStrategy

**Description:** Specifies the selection strategy for the next route ID

**Value Type:** [Selection Strategy](value-types.md#selection-strategy)

### selectableRouteId

**Description:** Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)

**Value Type:** [String](value-types.md#string)

### socketSetting

**Description:** Specifies a socket setting for all sockets (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onBindRequest.inboundSocketSetting

**Description:** Specifies a socket setting for the inbound socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onBindRequest.listenBindHost

**Description:** Specifies the binding host name or address for the listen socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

### socks5.onBindRequest.listenBindHostAddressType

**Description:** Specifies an acceptable binding host address type for the listen socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onBindRequest.listenBindPortRange

**Description:** Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onBindRequest.listenNetInterface

**Description:** Specifies the network interface that provides a binding host address for the listen socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onBindRequest.listenSocketSetting

**Description:** Specifies a socket setting for the listen socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onBindRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.prepareTargetFacingSocket

**Description:** Specifies the boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

### socks5.onConnectRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.targetFacingBindHost

**Description:** Specifies the binding host name or address for the target-facing socket

**Value Type:** [Host](value-types.md#host)

### socks5.onConnectRequest.targetFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for the target-facing socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onConnectRequest.targetFacingBindPortRange

**Description:** Specifies a binding port range for the target-facing socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onConnectRequest.targetFacingConnectTimeout

**Description:** Specifies the timeout in milliseconds on waiting for the target-facing socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.targetFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for the target-facing socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onConnectRequest.targetFacingSocketSetting

**Description:** Specifies a socket setting for the target-facing socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onRequest.externalFacingBindHost

**Description:** Specifies the binding host name or address for all external-facing sockets

**Value Type:** [Host](value-types.md#host)

### socks5.onRequest.externalFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onRequest.externalFacingBindTcpPortRange

**Description:** Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onRequest.externalFacingBindUdpPortRange

**Description:** Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onRequest.externalFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for all external-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onRequest.externalFacingSocketSetting

**Description:** Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onRequest.internalFacingBindHost

**Description:** Specifies the binding host name or address for all internal-facing sockets

**Value Type:** [Host](value-types.md#host)

### socks5.onRequest.internalFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onRequest.internalFacingBindUdpPortRange

**Description:** Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onRequest.internalFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for all internal-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onRequest.internalFacingSocketSetting

**Description:** Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.clientFacingBindHost

**Description:** Specifies the binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks5.onUdpAssociateRequest.clientFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onUdpAssociateRequest.clientFacingBindPortRange

**Description:** Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onUdpAssociateRequest.clientFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onUdpAssociateRequest.clientFacingSocketSetting

**Description:** Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onUdpAssociateRequest.peerFacingBindHost

**Description:** Specifies the binding host name or address for the peer-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks5.onUdpAssociateRequest.peerFacingBindHostAddressType

**Description:** Specifies an acceptable binding host address type for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

### socks5.onUdpAssociateRequest.peerFacingBindPortRange

**Description:** Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

### socks5.onUdpAssociateRequest.peerFacingNetInterface

**Description:** Specifies the network interface that provides a binding host address for the peer-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onUdpAssociateRequest.peerFacingSocketSetting

**Description:** Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

### socks5.onUdpAssociateRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.request.desiredDestinationAddressRedirect

**Description:** Specifies the desired destination address redirect for the request

**Value Type:** [SOCKS5 Address](value-types.md#socks5-address)

### socks5.request.desiredDestinationPortRedirect

**Description:** Specifies the desired destination port redirect for the request

**Value Type:** [Port](value-types.md#port)

### socks5.request.desiredDestinationRedirectLogAction

**Description:** Specifies the logging action to take if the desired destination of the request is redirected

**Value Type:** [Log Action](value-types.md#log-action)

