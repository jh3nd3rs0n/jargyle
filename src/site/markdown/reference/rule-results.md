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
    -   [socks5.desiredDestinationAddressRedirect](#socks5-desireddestinationaddressredirect)
    -   [socks5.desiredDestinationPortRedirect](#socks5-desireddestinationportredirect)
    -   [socks5.desiredDestinationRedirectLogAction](#socks5-desireddestinationredirectlogaction)
    -   [socks5.onBind.inboundSocketSetting](#socks5-onbind-inboundsocketsetting)
    -   [socks5.onBind.listenBindHost](#socks5-onbind-listenbindhost)
    -   [socks5.onBind.listenBindPortRange](#socks5-onbind-listenbindportrange)
    -   [socks5.onBind.listenSocketSetting](#socks5-onbind-listensocketsetting)
    -   [socks5.onBind.relayBufferSize](#socks5-onbind-relaybuffersize)
    -   [socks5.onBind.relayIdleTimeout](#socks5-onbind-relayidletimeout)
    -   [socks5.onBind.relayInboundBandwidthLimit](#socks5-onbind-relayinboundbandwidthlimit)
    -   [socks5.onBind.relayOutboundBandwidthLimit](#socks5-onbind-relayoutboundbandwidthlimit)
    -   [socks5.onCommand.bindHost](#socks5-oncommand-bindhost)
    -   [socks5.onCommand.bindTcpPortRange](#socks5-oncommand-bindtcpportrange)
    -   [socks5.onCommand.bindUdpPortRange](#socks5-oncommand-bindudpportrange)
    -   [socks5.onCommand.externalFacingBindHost](#socks5-oncommand-externalfacingbindhost)
    -   [socks5.onCommand.externalFacingBindTcpPortRange](#socks5-oncommand-externalfacingbindtcpportrange)
    -   [socks5.onCommand.externalFacingBindUdpPortRange](#socks5-oncommand-externalfacingbindudpportrange)
    -   [socks5.onCommand.externalFacingSocketSetting](#socks5-oncommand-externalfacingsocketsetting)
    -   [socks5.onCommand.internalFacingBindHost](#socks5-oncommand-internalfacingbindhost)
    -   [socks5.onCommand.internalFacingBindUdpPortRange](#socks5-oncommand-internalfacingbindudpportrange)
    -   [socks5.onCommand.internalFacingSocketSetting](#socks5-oncommand-internalfacingsocketsetting)
    -   [socks5.onCommand.relayBufferSize](#socks5-oncommand-relaybuffersize)
    -   [socks5.onCommand.relayIdleTimeout](#socks5-oncommand-relayidletimeout)
    -   [socks5.onCommand.relayInboundBandwidthLimit](#socks5-oncommand-relayinboundbandwidthlimit)
    -   [socks5.onCommand.relayOutboundBandwidthLimit](#socks5-oncommand-relayoutboundbandwidthlimit)
    -   [socks5.onCommand.socketSetting](#socks5-oncommand-socketsetting)
    -   [socks5.onConnect.prepareServerFacingSocket](#socks5-onconnect-prepareserverfacingsocket)
    -   [socks5.onConnect.relayBufferSize](#socks5-onconnect-relaybuffersize)
    -   [socks5.onConnect.relayIdleTimeout](#socks5-onconnect-relayidletimeout)
    -   [socks5.onConnect.relayInboundBandwidthLimit](#socks5-onconnect-relayinboundbandwidthlimit)
    -   [socks5.onConnect.relayOutboundBandwidthLimit](#socks5-onconnect-relayoutboundbandwidthlimit)
    -   [socks5.onConnect.serverFacingBindHost](#socks5-onconnect-serverfacingbindhost)
    -   [socks5.onConnect.serverFacingBindPortRange](#socks5-onconnect-serverfacingbindportrange)
    -   [socks5.onConnect.serverFacingConnectTimeout](#socks5-onconnect-serverfacingconnecttimeout)
    -   [socks5.onConnect.serverFacingSocketSetting](#socks5-onconnect-serverfacingsocketsetting)
    -   [socks5.onUdpAssociate.clientFacingBindHost](#socks5-onudpassociate-clientfacingbindhost)
    -   [socks5.onUdpAssociate.clientFacingBindPortRange](#socks5-onudpassociate-clientfacingbindportrange)
    -   [socks5.onUdpAssociate.clientFacingSocketSetting](#socks5-onudpassociate-clientfacingsocketsetting)
    -   [socks5.onUdpAssociate.peerFacingBindHost](#socks5-onudpassociate-peerfacingbindhost)
    -   [socks5.onUdpAssociate.peerFacingBindPortRange](#socks5-onudpassociate-peerfacingbindportrange)
    -   [socks5.onUdpAssociate.peerFacingSocketSetting](#socks5-onudpassociate-peerfacingsocketsetting)
    -   [socks5.onUdpAssociate.relayBufferSize](#socks5-onudpassociate-relaybuffersize)
    -   [socks5.onUdpAssociate.relayIdleTimeout](#socks5-onudpassociate-relayidletimeout)
    -   [socks5.onUdpAssociate.relayInboundBandwidthLimit](#socks5-onudpassociate-relayinboundbandwidthlimit)
    -   [socks5.onUdpAssociate.relayOutboundBandwidthLimit](#socks5-onudpassociate-relayoutboundbandwidthlimit)

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

### socks5.desiredDestinationAddressRedirect

**Syntax:**

```text
socks5.desiredDestinationAddressRedirect=SOCKS5_ADDRESS
```

**Description:**

Specifies the desired destination address redirect

**Value:** [SOCKS5 Address](value-syntaxes.md#socks5-address)

### socks5.desiredDestinationPortRedirect

**Syntax:**

```text
socks5.desiredDestinationPortRedirect=PORT
```

**Description:**

Specifies the desired destination port redirect

**Value:** [Port](value-syntaxes.md#port)

### socks5.desiredDestinationRedirectLogAction

**Syntax:**

```text
socks5.desiredDestinationRedirectLogAction=LOG_ACTION
```

**Description:**

Specifies the logging action to take if the desired destination is redirected

**Value:** [Log Action](value-syntaxes.md#log-action)

### socks5.onBind.inboundSocketSetting

**Syntax:**

```text
socks5.onBind.inboundSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the inbound socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onBind.listenBindHost

**Syntax:**

```text
socks5.onBind.listenBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the listen socket if the provided host address is all zeros

**Value:** [Host](value-syntaxes.md#host)

### socks5.onBind.listenBindPortRange

**Syntax:**

```text
socks5.onBind.listenBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onBind.listenSocketSetting

**Syntax:**

```text
socks5.onBind.listenSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the listen socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onBind.relayBufferSize

**Syntax:**

```text
socks5.onBind.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayIdleTimeout

**Syntax:**

```text
socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.bindHost

**Syntax:**

```text
socks5.onCommand.bindHost=HOST
```

**Description:**

Specifies the binding host name or address for all sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.bindTcpPortRange

**Syntax:**

```text
socks5.onCommand.bindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onCommand.bindUdpPortRange

**Syntax:**

```text
socks5.onCommand.bindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onCommand.externalFacingBindHost

**Syntax:**

```text
socks5.onCommand.externalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.externalFacingBindTcpPortRange

**Syntax:**

```text
socks5.onCommand.externalFacingBindTcpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onCommand.externalFacingBindUdpPortRange

**Syntax:**

```text
socks5.onCommand.externalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onCommand.externalFacingSocketSetting

**Syntax:**

```text
socks5.onCommand.externalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onCommand.internalFacingBindHost

**Syntax:**

```text
socks5.onCommand.internalFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.internalFacingBindUdpPortRange

**Syntax:**

```text
socks5.onCommand.internalFacingBindUdpPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onCommand.internalFacingSocketSetting

**Syntax:**

```text
socks5.onCommand.internalFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onCommand.relayBufferSize

**Syntax:**

```text
socks5.onCommand.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayIdleTimeout

**Syntax:**

```text
socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.socketSetting

**Syntax:**

```text
socks5.onCommand.socketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onConnect.prepareServerFacingSocket

**Syntax:**

```text
socks5.onConnect.prepareServerFacingSocket=true|false
```

**Description:**

Specifies the boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value:** java.lang.Boolean

### socks5.onConnect.relayBufferSize

**Syntax:**

```text
socks5.onConnect.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayIdleTimeout

**Syntax:**

```text
socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.serverFacingBindHost

**Syntax:**

```text
socks5.onConnect.serverFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the server-facing socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onConnect.serverFacingBindPortRange

**Syntax:**

```text
socks5.onConnect.serverFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the server-facing socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onConnect.serverFacingConnectTimeout

**Syntax:**

```text
socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on waiting for the server-facing socket to connect

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.serverFacingSocketSetting

**Syntax:**

```text
socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the server-facing socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociate.clientFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the client-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociate.clientFacingBindPortRange

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onUdpAssociate.clientFacingSocketSetting

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociate.peerFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingBindHost=HOST
```

**Description:**

Specifies the binding host name or address for the peer-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociate.peerFacingBindPortRange

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingBindPortRange=PORT_RANGE
```

**Description:**

Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

**Value:** [Port Range](value-syntaxes.md#port-range)

### socks5.onUdpAssociate.peerFacingSocketSetting

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING
```

**Description:**

Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

**Value:** [Socket Setting](value-syntaxes.md#socket-setting)

### socks5.onUdpAssociate.relayBufferSize

**Syntax:**

```text
socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

Specifies the buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayIdleTimeout

**Syntax:**

```text
socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

Specifies the timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

