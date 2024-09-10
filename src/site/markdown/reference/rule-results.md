# Rule Results

## Page Contents

-   [General Rule Results](#general-rule-results)
    -   [bindHost](#bindhost)
    -   [bindTcpPortRange](#bindtcpportrange)
    -   [bindUdpPortRange](#bindudpportrange)
    -   [clientSocketSetting](#clientsocketsetting)
    -   [externalFacingBindHost](#externalfacingbindhost)
    -   [externalFacingBindTcpPortRange](#externalfacingbindtcpportrange)
    -   [externalFacingBindUdpPortRange](#externalfacingbindudpportrange)
    -   [externalFacingSocketSetting](#externalfacingsocketsetting)
    -   [firewallAction](#firewallaction)
    -   [firewallActionAllowLimit](#firewallactionallowlimit)
    -   [firewallActionAllowLimitReachedLogAction](#firewallactionallowlimitreachedlogaction)
    -   [firewallActionLogAction](#firewallactionlogaction)
    -   [internalFacingBindHost](#internalfacingbindhost)
    -   [internalFacingBindUdpPortRange](#internalfacingbindudpportrange)
    -   [internalFacingSocketSetting](#internalfacingsocketsetting)
    -   [routeSelectionLogAction](#routeselectionlogaction)
    -   [routeSelectionStrategy](#routeselectionstrategy)
    -   [selectableRouteId](#selectablerouteid)
    -   [socketSetting](#socketsetting)
-   [SOCKS5 Rule Results](#socks5-rule-results)
    -   [socks5.onBindRequest.inboundSocketSetting](#socks5-onbindrequest-inboundsocketsetting)
    -   [socks5.onBindRequest.listenBindHost](#socks5-onbindrequest-listenbindhost)
    -   [socks5.onBindRequest.listenBindPortRange](#socks5-onbindrequest-listenbindportrange)
    -   [socks5.onBindRequest.listenSocketSetting](#socks5-onbindrequest-listensocketsetting)
    -   [socks5.onBindRequest.relayBufferSize](#socks5-onbindrequest-relaybuffersize)
    -   [socks5.onBindRequest.relayIdleTimeout](#socks5-onbindrequest-relayidletimeout)
    -   [socks5.onBindRequest.relayInboundBandwidthLimit](#socks5-onbindrequest-relayinboundbandwidthlimit)
    -   [socks5.onBindRequest.relayOutboundBandwidthLimit](#socks5-onbindrequest-relayoutboundbandwidthlimit)
    -   [socks5.onConnectRequest.prepareTargetFacingSocket](#socks5-onconnectrequest-preparetargetfacingsocket)
    -   [socks5.onConnectRequest.relayBufferSize](#socks5-onconnectrequest-relaybuffersize)
    -   [socks5.onConnectRequest.relayIdleTimeout](#socks5-onconnectrequest-relayidletimeout)
    -   [socks5.onConnectRequest.relayInboundBandwidthLimit](#socks5-onconnectrequest-relayinboundbandwidthlimit)
    -   [socks5.onConnectRequest.relayOutboundBandwidthLimit](#socks5-onconnectrequest-relayoutboundbandwidthlimit)
    -   [socks5.onConnectRequest.targetFacingBindHost](#socks5-onconnectrequest-targetfacingbindhost)
    -   [socks5.onConnectRequest.targetFacingBindPortRange](#socks5-onconnectrequest-targetfacingbindportrange)
    -   [socks5.onConnectRequest.targetFacingConnectTimeout](#socks5-onconnectrequest-targetfacingconnecttimeout)
    -   [socks5.onConnectRequest.targetFacingSocketSetting](#socks5-onconnectrequest-targetfacingsocketsetting)
    -   [socks5.onRequest.bindHost](#socks5-onrequest-bindhost)
    -   [socks5.onRequest.bindTcpPortRange](#socks5-onrequest-bindtcpportrange)
    -   [socks5.onRequest.bindUdpPortRange](#socks5-onrequest-bindudpportrange)
    -   [socks5.onRequest.externalFacingBindHost](#socks5-onrequest-externalfacingbindhost)
    -   [socks5.onRequest.externalFacingBindTcpPortRange](#socks5-onrequest-externalfacingbindtcpportrange)
    -   [socks5.onRequest.externalFacingBindUdpPortRange](#socks5-onrequest-externalfacingbindudpportrange)
    -   [socks5.onRequest.externalFacingSocketSetting](#socks5-onrequest-externalfacingsocketsetting)
    -   [socks5.onRequest.internalFacingBindHost](#socks5-onrequest-internalfacingbindhost)
    -   [socks5.onRequest.internalFacingBindUdpPortRange](#socks5-onrequest-internalfacingbindudpportrange)
    -   [socks5.onRequest.internalFacingSocketSetting](#socks5-onrequest-internalfacingsocketsetting)
    -   [socks5.onRequest.relayBufferSize](#socks5-onrequest-relaybuffersize)
    -   [socks5.onRequest.relayIdleTimeout](#socks5-onrequest-relayidletimeout)
    -   [socks5.onRequest.relayInboundBandwidthLimit](#socks5-onrequest-relayinboundbandwidthlimit)
    -   [socks5.onRequest.relayOutboundBandwidthLimit](#socks5-onrequest-relayoutboundbandwidthlimit)
    -   [socks5.onRequest.socketSetting](#socks5-onrequest-socketsetting)
    -   [socks5.onUdpAssociateRequest.clientFacingBindHost](#socks5-onudpassociaterequest-clientfacingbindhost)
    -   [socks5.onUdpAssociateRequest.clientFacingBindPortRange](#socks5-onudpassociaterequest-clientfacingbindportrange)
    -   [socks5.onUdpAssociateRequest.clientFacingSocketSetting](#socks5-onudpassociaterequest-clientfacingsocketsetting)
    -   [socks5.onUdpAssociateRequest.peerFacingBindHost](#socks5-onudpassociaterequest-peerfacingbindhost)
    -   [socks5.onUdpAssociateRequest.peerFacingBindPortRange](#socks5-onudpassociaterequest-peerfacingbindportrange)
    -   [socks5.onUdpAssociateRequest.peerFacingSocketSetting](#socks5-onudpassociaterequest-peerfacingsocketsetting)
    -   [socks5.onUdpAssociateRequest.relayBufferSize](#socks5-onudpassociaterequest-relaybuffersize)
    -   [socks5.onUdpAssociateRequest.relayIdleTimeout](#socks5-onudpassociaterequest-relayidletimeout)
    -   [socks5.onUdpAssociateRequest.relayInboundBandwidthLimit](#socks5-onudpassociaterequest-relayinboundbandwidthlimit)
    -   [socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit](#socks5-onudpassociaterequest-relayoutboundbandwidthlimit)
    -   [socks5.request.desiredDestinationAddressRedirect](#socks5-request-desireddestinationaddressredirect)
    -   [socks5.request.desiredDestinationPortRedirect](#socks5-request-desireddestinationportredirect)
    -   [socks5.request.desiredDestinationRedirectLogAction](#socks5-request-desireddestinationredirectlogaction)

## General Rule Results

### bindHost

**Syntax:**

```text
bindHost=HOST
```

**Description:**

Specifies the binding host name or address for all sockets

**Value:** [Host](value-syntaxes.md#host)

### bindTcpPortRange

**Syntax:**

```text
bindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### bindUdpPortRange

**Syntax:**

```text
bindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port ranges)

**Value:** [Port Range](value-syntaxes.md#port-range)

### clientSocketSetting

**Syntax:**

```text
clientSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the client socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### externalFacingBindHost

**Syntax:**

```text
externalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### externalFacingBindTcpPortRange

**Syntax:**

```text
externalFacingBindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### externalFacingBindUdpPortRange

**Syntax:**

```text
externalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### externalFacingSocketSetting

**Syntax:**

```text
externalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### firewallAction

**Syntax:**

```text
firewallAction=FIREWALL_ACTION
```

**Description:**

Specifies the firewall action to take

**Value:** [Firewall Action](value-syntaxes.md#firewall-action)

### firewallActionAllowLimit

**Syntax:**

```text
firewallActionAllowLimit=NON_NEGATIVE_INTEGER
```

**Description:**

Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

**Value:** [Non-negative Integer](value-syntaxes.md#non-negative-integer)

### firewallActionAllowLimitReachedLogAction

**Syntax:**

```text
firewallActionAllowLimitReachedLogAction=LOG_ACTION
```

**Description:**

Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

**Value:** [Log Action](value-syntaxes.md#log-action)

### firewallActionLogAction

**Syntax:**

```text
firewallActionLogAction=LOG_ACTION
```

**Description:**

Specifies the logging action to take if the firewall action is applied

**Value:** [Log Action](value-syntaxes.md#log-action)

### internalFacingBindHost

**Syntax:**

```text
internalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### internalFacingBindUdpPortRange

**Syntax:**

```text
internalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### internalFacingSocketSetting

**Syntax:**

```text
internalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### routeSelectionLogAction

**Syntax:**

```text
routeSelectionLogAction=LOG_ACTION
```

**Description:**

Specifies the logging action to take if a route ID is selected

**Value:** [Log Action](value-syntaxes.md#log-action)

### routeSelectionStrategy

**Syntax:**

```text
routeSelectionStrategy=SELECTION_STRATEGY
```

**Description:**

Specifies the selection strategy for the next route ID

**Value:** [Selection Strategy](value-syntaxes.md#selection-strategy)

### selectableRouteId

**Syntax:**

```text
selectableRouteId=ROUTE_ID
```

**Description:**

Specifies the ID for a selectable route (can be specified multiple times with each rule result specifying another ID for a selectable route)

**Value:** java.lang.String

### socketSetting

**Syntax:**

```text
socketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

## SOCKS5 Rule Results

### socks5.onBindRequest.inboundSocketSetting

**Syntax:**

```text
socks5.onBindRequest.inboundSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the inbound socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onBindRequest.listenBindHost

**Syntax:**

```text
socks5.onBindRequest.listenBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the listen socket if the provided host address is all zeros

**Value:** [Host](value-syntaxes.md#host)

### socks5.onBindRequest.listenBindPortRange

**Syntax:**

```text
socks5.onBindRequest.listenBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onBindRequest.listenSocketSetting

**Syntax:**

```text
socks5.onBindRequest.listenSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the listen socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onBindRequest.relayBufferSize

**Syntax:**

```text
socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.prepareTargetFacingSocket

**Syntax:**

```text
socks5.onConnectRequest.prepareTargetFacingSocket=true|false
```

**Description:**

Specifies the boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value:** java.lang.Boolean

### socks5.onConnectRequest.relayBufferSize

**Syntax:**

```text
socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.targetFacingBindHost

**Syntax:**

```text
socks5.onConnectRequest.targetFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the target-facing socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onConnectRequest.targetFacingBindPortRange

**Syntax:**

```text
socks5.onConnectRequest.targetFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the target-facing socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onConnectRequest.targetFacingConnectTimeout

**Syntax:**

```text
socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on waiting for the target-facing socket to connect

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.targetFacingSocketSetting

**Syntax:**

```text
socks5.onConnectRequest.targetFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the target-facing socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onRequest.bindHost

**Syntax:**

```text
socks5.onRequest.bindHost=HOST
```

**Description:**

Specifies the binding host name or address for all sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.bindTcpPortRange

**Syntax:**

```text
socks5.onRequest.bindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onRequest.bindUdpPortRange

**Syntax:**

```text
socks5.onRequest.bindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onRequest.externalFacingBindHost

**Syntax:**

```text
socks5.onRequest.externalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.externalFacingBindTcpPortRange

**Syntax:**

```text
socks5.onRequest.externalFacingBindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onRequest.externalFacingBindUdpPortRange

**Syntax:**

```text
socks5.onRequest.externalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onRequest.externalFacingSocketSetting

**Syntax:**

```text
socks5.onRequest.externalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onRequest.internalFacingBindHost

**Syntax:**

```text
socks5.onRequest.internalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.internalFacingBindUdpPortRange

**Syntax:**

```text
socks5.onRequest.internalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onRequest.internalFacingSocketSetting

**Syntax:**

```text
socks5.onRequest.internalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onRequest.relayBufferSize

**Syntax:**

```text
socks5.onRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.socketSetting

**Syntax:**

```text
socks5.onRequest.socketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociateRequest.clientFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the client-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociateRequest.clientFacingBindPortRange

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onUdpAssociateRequest.clientFacingSocketSetting

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociateRequest.peerFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the peer-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociateRequest.peerFacingBindPortRange

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onUdpAssociateRequest.peerFacingSocketSetting

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociateRequest.relayBufferSize

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.request.desiredDestinationAddressRedirect

**Syntax:**

```text
socks5.request.desiredDestinationAddressRedirect=SOCKS5_ADDRESS
```

**Description:**

Specifies the desired destination address redirect for the request

**Value:** [SOCKS5 Address](value-syntaxes.md#socks5-address)

### socks5.request.desiredDestinationPortRedirect

**Syntax:**

```text
socks5.request.desiredDestinationPortRedirect=PORT
```

**Description:**

Specifies the desired destination port redirect for the request

**Value:** [Port](value-syntaxes.md#port)

### socks5.request.desiredDestinationRedirectLogAction

**Syntax:**

```text
socks5.request.desiredDestinationRedirectLogAction=LOG_ACTION
```

**Description:**

Specifies the logging action to take if the desired destination of the request is redirected

**Value:** [Log Action](value-syntaxes.md#log-action)

