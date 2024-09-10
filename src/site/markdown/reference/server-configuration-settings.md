# Server Configuration Settings

## Page Contents

-   [General Settings](#general-settings)
    -   [backlog](#backlog)
    -   [bindHost](#bindhost)
    -   [bindTcpPortRanges](#bindtcpportranges)
    -   [bindUdpPortRanges](#bindudpportranges)
    -   [clientSocketSettings](#clientsocketsettings)
    -   [doc](#doc)
    -   [externalFacingBindHost](#externalfacingbindhost)
    -   [externalFacingBindTcpPortRanges](#externalfacingbindtcpportranges)
    -   [externalFacingBindUdpPortRanges](#externalfacingbindudpportranges)
    -   [externalFacingSocketSettings](#externalfacingsocketsettings)
    -   [internalFacingBindHost](#internalfacingbindhost)
    -   [internalFacingBindTcpPortRanges](#internalfacingbindtcpportranges)
    -   [internalFacingBindUdpPortRanges](#internalfacingbindudpportranges)
    -   [internalFacingSocketSettings](#internalfacingsocketsettings)
    -   [lastRouteId](#lastrouteid)
    -   [port](#port)
    -   [routeSelectionLogAction](#routeselectionlogaction)
    -   [routeSelectionStrategy](#routeselectionstrategy)
    -   [rule](#rule)
    -   [socketSettings](#socketsettings)
    -   [socksServerBindHost](#socksserverbindhost)
    -   [socksServerBindPortRanges](#socksserverbindportranges)
    -   [socksServerSocketSettings](#socksserversocketsettings)
-   [Chaining General Settings](#chaining-general-settings)
    -   [chaining.clientBindHost](#chaining-clientbindhost)
    -   [chaining.clientBindPortRanges](#chaining-clientbindportranges)
    -   [chaining.clientConnectTimeout](#chaining-clientconnecttimeout)
    -   [chaining.clientSocketSettings](#chaining-clientsocketsettings)
    -   [chaining.routeId](#chaining-routeid)
    -   [chaining.socksServerUri](#chaining-socksserveruri)
-   [Chaining DTLS Settings](#chaining-dtls-settings)
    -   [chaining.dtls.enabled](#chaining-dtls-enabled)
    -   [chaining.dtls.enabledCipherSuites](#chaining-dtls-enabledciphersuites)
    -   [chaining.dtls.enabledProtocols](#chaining-dtls-enabledprotocols)
    -   [chaining.dtls.maxPacketSize](#chaining-dtls-maxpacketsize)
    -   [chaining.dtls.protocol](#chaining-dtls-protocol)
    -   [chaining.dtls.trustStoreFile](#chaining-dtls-truststorefile)
    -   [chaining.dtls.trustStorePassword](#chaining-dtls-truststorepassword)
    -   [chaining.dtls.trustStoreType](#chaining-dtls-truststoretype)
-   [Chaining SOCKS5 Settings](#chaining-socks5-settings)
    -   [chaining.socks5.gssapimethod.mechanismOid](#chaining-socks5-gssapimethod-mechanismoid)
    -   [chaining.socks5.gssapimethod.necReferenceImpl](#chaining-socks5-gssapimethod-necreferenceimpl)
    -   [chaining.socks5.gssapimethod.protectionLevels](#chaining-socks5-gssapimethod-protectionlevels)
    -   [chaining.socks5.gssapimethod.serviceName](#chaining-socks5-gssapimethod-servicename)
    -   [chaining.socks5.gssapimethod.suggestedConf](#chaining-socks5-gssapimethod-suggestedconf)
    -   [chaining.socks5.gssapimethod.suggestedInteg](#chaining-socks5-gssapimethod-suggestedinteg)
    -   [chaining.socks5.methods](#chaining-socks5-methods)
    -   [chaining.socks5.socks5DatagramSocket.actualAddressAndPortUnknown](#chaining-socks5-socks5datagramsocket-actualaddressandportunknown)
    -   [chaining.socks5.socks5HostResolver.resolveFromSocks5Server](#chaining-socks5-socks5hostresolver-resolvefromsocks5server)
    -   [chaining.socks5.userpassmethod.password](#chaining-socks5-userpassmethod-password)
    -   [chaining.socks5.userpassmethod.username](#chaining-socks5-userpassmethod-username)
-   [Chaining SSL/TLS Settings](#chaining-ssl-tls-settings)
    -   [chaining.ssl.enabled](#chaining-ssl-enabled)
    -   [chaining.ssl.enabledCipherSuites](#chaining-ssl-enabledciphersuites)
    -   [chaining.ssl.enabledProtocols](#chaining-ssl-enabledprotocols)
    -   [chaining.ssl.keyStoreFile](#chaining-ssl-keystorefile)
    -   [chaining.ssl.keyStorePassword](#chaining-ssl-keystorepassword)
    -   [chaining.ssl.keyStoreType](#chaining-ssl-keystoretype)
    -   [chaining.ssl.protocol](#chaining-ssl-protocol)
    -   [chaining.ssl.trustStoreFile](#chaining-ssl-truststorefile)
    -   [chaining.ssl.trustStorePassword](#chaining-ssl-truststorepassword)
    -   [chaining.ssl.trustStoreType](#chaining-ssl-truststoretype)
-   [DTLS Settings](#dtls-settings)
    -   [dtls.enabled](#dtls-enabled)
    -   [dtls.enabledCipherSuites](#dtls-enabledciphersuites)
    -   [dtls.enabledProtocols](#dtls-enabledprotocols)
    -   [dtls.keyStoreFile](#dtls-keystorefile)
    -   [dtls.keyStorePassword](#dtls-keystorepassword)
    -   [dtls.keyStoreType](#dtls-keystoretype)
    -   [dtls.maxPacketSize](#dtls-maxpacketsize)
    -   [dtls.protocol](#dtls-protocol)
-   [SOCKS5 Settings](#socks5-settings)
    -   [socks5.gssapimethod.necReferenceImpl](#socks5-gssapimethod-necreferenceimpl)
    -   [socks5.gssapimethod.protectionLevels](#socks5-gssapimethod-protectionlevels)
    -   [socks5.gssapimethod.suggestedConf](#socks5-gssapimethod-suggestedconf)
    -   [socks5.gssapimethod.suggestedInteg](#socks5-gssapimethod-suggestedinteg)
    -   [socks5.methods](#socks5-methods)
    -   [socks5.onBindRequest.inboundSocketSettings](#socks5-onbindrequest-inboundsocketsettings)
    -   [socks5.onBindRequest.listenBindHost](#socks5-onbindrequest-listenbindhost)
    -   [socks5.onBindRequest.listenBindPortRanges](#socks5-onbindrequest-listenbindportranges)
    -   [socks5.onBindRequest.listenSocketSettings](#socks5-onbindrequest-listensocketsettings)
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
    -   [socks5.onConnectRequest.targetFacingBindPortRanges](#socks5-onconnectrequest-targetfacingbindportranges)
    -   [socks5.onConnectRequest.targetFacingConnectTimeout](#socks5-onconnectrequest-targetfacingconnecttimeout)
    -   [socks5.onConnectRequest.targetFacingSocketSettings](#socks5-onconnectrequest-targetfacingsocketsettings)
    -   [socks5.onRequest.bindHost](#socks5-onrequest-bindhost)
    -   [socks5.onRequest.bindTcpPortRanges](#socks5-onrequest-bindtcpportranges)
    -   [socks5.onRequest.bindUdpPortRanges](#socks5-onrequest-bindudpportranges)
    -   [socks5.onRequest.externalFacingBindHost](#socks5-onrequest-externalfacingbindhost)
    -   [socks5.onRequest.externalFacingBindTcpPortRanges](#socks5-onrequest-externalfacingbindtcpportranges)
    -   [socks5.onRequest.externalFacingBindUdpPortRanges](#socks5-onrequest-externalfacingbindudpportranges)
    -   [socks5.onRequest.externalFacingSocketSettings](#socks5-onrequest-externalfacingsocketsettings)
    -   [socks5.onRequest.internalFacingBindHost](#socks5-onrequest-internalfacingbindhost)
    -   [socks5.onRequest.internalFacingBindUdpPortRanges](#socks5-onrequest-internalfacingbindudpportranges)
    -   [socks5.onRequest.internalFacingSocketSettings](#socks5-onrequest-internalfacingsocketsettings)
    -   [socks5.onRequest.relayBufferSize](#socks5-onrequest-relaybuffersize)
    -   [socks5.onRequest.relayIdleTimeout](#socks5-onrequest-relayidletimeout)
    -   [socks5.onRequest.relayInboundBandwidthLimit](#socks5-onrequest-relayinboundbandwidthlimit)
    -   [socks5.onRequest.relayOutboundBandwidthLimit](#socks5-onrequest-relayoutboundbandwidthlimit)
    -   [socks5.onRequest.socketSettings](#socks5-onrequest-socketsettings)
    -   [socks5.onUdpAssociateRequest.clientFacingBindHost](#socks5-onudpassociaterequest-clientfacingbindhost)
    -   [socks5.onUdpAssociateRequest.clientFacingBindPortRanges](#socks5-onudpassociaterequest-clientfacingbindportranges)
    -   [socks5.onUdpAssociateRequest.clientFacingSocketSettings](#socks5-onudpassociaterequest-clientfacingsocketsettings)
    -   [socks5.onUdpAssociateRequest.peerFacingBindHost](#socks5-onudpassociaterequest-peerfacingbindhost)
    -   [socks5.onUdpAssociateRequest.peerFacingBindPortRanges](#socks5-onudpassociaterequest-peerfacingbindportranges)
    -   [socks5.onUdpAssociateRequest.peerFacingSocketSettings](#socks5-onudpassociaterequest-peerfacingsocketsettings)
    -   [socks5.onUdpAssociateRequest.relayBufferSize](#socks5-onudpassociaterequest-relaybuffersize)
    -   [socks5.onUdpAssociateRequest.relayIdleTimeout](#socks5-onudpassociaterequest-relayidletimeout)
    -   [socks5.onUdpAssociateRequest.relayInboundBandwidthLimit](#socks5-onudpassociaterequest-relayinboundbandwidthlimit)
    -   [socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit](#socks5-onudpassociaterequest-relayoutboundbandwidthlimit)
    -   [socks5.userpassmethod.userRepository](#socks5-userpassmethod-userrepository)
-   [SSL/TLS Settings](#ssl-tls-settings)
    -   [ssl.enabled](#ssl-enabled)
    -   [ssl.enabledCipherSuites](#ssl-enabledciphersuites)
    -   [ssl.enabledProtocols](#ssl-enabledprotocols)
    -   [ssl.keyStoreFile](#ssl-keystorefile)
    -   [ssl.keyStorePassword](#ssl-keystorepassword)
    -   [ssl.keyStoreType](#ssl-keystoretype)
    -   [ssl.needClientAuth](#ssl-needclientauth)
    -   [ssl.protocol](#ssl-protocol)
    -   [ssl.trustStoreFile](#ssl-truststorefile)
    -   [ssl.trustStorePassword](#ssl-truststorepassword)
    -   [ssl.trustStoreType](#ssl-truststoretype)
    -   [ssl.wantClientAuth](#ssl-wantclientauth)

## General Settings

### backlog

**Syntax:**

```text
backlog=NON_NEGATIVE_INTEGER
```

**Description:**

The maximum length of the queue of incoming client connections to the SOCKS server (default is 50)

**Value:** [Non-negative Integer](value-syntaxes.md#non-negative-integer)

### bindHost

**Syntax:**

```text
bindHost=HOST
```

**Description:**

The default binding host name or address for all sockets (default is 0.0.0.0)

**Value:** [Host](value-syntaxes.md#host)

### bindTcpPortRanges

**Syntax:**

```text
bindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all TCP sockets (default is 0)

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### bindUdpPortRanges

**Syntax:**

```text
bindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all UDP sockets (default is 0)

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### clientSocketSettings

**Syntax:**

```text
clientSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the client socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### doc

**Syntax:**

```text
doc=TEXT
```

**Description:**

A documentation setting

**Value:** java.lang.String

### externalFacingBindHost

**Syntax:**

```text
externalFacingBindHost=HOST
```

**Description:**

The default binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### externalFacingBindTcpPortRanges

**Syntax:**

```text
externalFacingBindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all external-facing TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### externalFacingBindUdpPortRanges

**Syntax:**

```text
externalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all external-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### externalFacingSocketSettings

**Syntax:**

```text
externalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of default socket settings for all external-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### internalFacingBindHost

**Syntax:**

```text
internalFacingBindHost=HOST
```

**Description:**

The default binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### internalFacingBindTcpPortRanges

**Syntax:**

```text
internalFacingBindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all internal-facing TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### internalFacingBindUdpPortRanges

**Syntax:**

```text
internalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of default binding port ranges for all internal-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### internalFacingSocketSettings

**Syntax:**

```text
internalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of default socket settings for all internal-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### lastRouteId

**Syntax:**

```text
lastRouteId=ROUTE_ID
```

**Description:**

The ID for the last and unassigned route (default is lastRoute)

**Value:** java.lang.String

### port

**Syntax:**

```text
port=PORT
```

**Description:**

The port for the SOCKS server

**Value:** [Port](value-syntaxes.md#port)

### routeSelectionLogAction

**Syntax:**

```text
routeSelectionLogAction=LOG_ACTION
```

**Description:**

The logging action to take if a route is selected

**Value:** [Log Action](value-syntaxes.md#log-action)

### routeSelectionStrategy

**Syntax:**

```text
routeSelectionStrategy=SELECTION_STRATEGY
```

**Description:**

The selection strategy for the next route (default is CYCLICAL)

**Value:** [Selection Strategy](value-syntaxes.md#selection-strategy)

### rule

**Syntax:**

```text
rule=RULE
```

**Description:**

A rule for the SOCKS server (default is firewallAction=ALLOW)

**Value:** [Rule](value-syntaxes.md#rule)

### socketSettings

**Syntax:**

```text
socketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of default socket settings for all sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socksServerBindHost

**Syntax:**

```text
socksServerBindHost=HOST
```

**Description:**

The binding host name or address for the SOCKS server socket

**Value:** [Host](value-syntaxes.md#host)

### socksServerBindPortRanges

**Syntax:**

```text
socksServerBindPortRangesPORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the SOCKS server socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socksServerSocketSettings

**Syntax:**

```text
socksServerSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the SOCKS server socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

## Chaining General Settings

### chaining.clientBindHost

**Syntax:**

```text
chaining.clientBindHost=HOST
```

**Description:**

The binding host name or address for the client socket that is used to connect to the other SOCKS server (default is 0.0.0.0)

**Value:** [Host](value-syntaxes.md#host)

### chaining.clientBindPortRanges

**Syntax:**

```text
chaining.clientBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server (default is 0)

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### chaining.clientConnectTimeout

**Syntax:**

```text
chaining.clientConnectTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### chaining.clientSocketSettings

**Syntax:**

```text
chaining.clientSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### chaining.routeId

**Syntax:**

```text
chaining.routeId=ROUTE_ID
```

**Description:**

The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

**Value:** java.lang.String

### chaining.socksServerUri

**Syntax:**

```text
chaining.socksServerUri=SOCKS_SERVER_URI
```

**Description:**

The URI of the other SOCKS server

**Value:** [SOCKS Server URI](value-syntaxes.md#socks-server-uri)

## Chaining DTLS Settings

### chaining.dtls.enabled

**Syntax:**

```text
chaining.dtls.enabled=true|false
```

**Description:**

The boolean value to indicate if DTLS connections to the other SOCKS server are enabled (default is false)

**Value:** java.lang.Boolean

### chaining.dtls.enabledCipherSuites

**Syntax:**

```text
chaining.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### chaining.dtls.enabledProtocols

**Syntax:**

```text
chaining.dtls.enabledProtocols=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### chaining.dtls.maxPacketSize

**Syntax:**

```text
chaining.dtls.maxPacketSize=POSITIVE_INTEGER
```

**Description:**

The maximum packet size for the DTLS connections to the other SOCKS server (default is 32768)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### chaining.dtls.protocol

**Syntax:**

```text
chaining.dtls.protocol=PROTOCOL
```

**Description:**

The protocol version for the DTLS connections to the other SOCKS server (default is DTLSv1.2)

**Value:** java.lang.String

### chaining.dtls.trustStoreFile

**Syntax:**

```text
chaining.dtls.trustStoreFile=FILE
```

**Description:**

The trust store file for the DTLS connections to the other SOCKS server

**Value:** java.io.File

### chaining.dtls.trustStorePassword

**Syntax:**

```text
chaining.dtls.trustStorePassword=PASSWORD
```

**Description:**

The password for the trust store for the DTLS connections to the other SOCKS server

**Value:** java.lang.String

### chaining.dtls.trustStoreType

**Syntax:**

```text
chaining.dtls.trustStoreType=TYPE
```

**Description:**

The type of trust store file for the DTLS connections to the other SOCKS server (default is PKCS12)

**Value:** java.lang.String

## Chaining SOCKS5 Settings

### chaining.socks5.gssapimethod.mechanismOid

**Syntax:**

```text
chaining.socks5.gssapimethod.mechanismOid=OID
```

**Description:**

The object ID for the GSS-API authentication mechanism to the other SOCKS5 server (default is 1.2.840.113554.1.2.2)

**Value:** org.ietf.jgss.Oid

### chaining.socks5.gssapimethod.necReferenceImpl

**Syntax:**

```text
chaining.socks5.gssapimethod.necReferenceImpl=true|false
```

**Description:**

The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation (default is false)

**Value:** java.lang.Boolean

### chaining.socks5.gssapimethod.protectionLevels

**Syntax:**

```text
chaining.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
```

**Description:**

The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

**Value:** [SOCKS5 GSS-API Method Protection Levels](value-syntaxes.md#socks5-gss-api-method-protection-levels)

### chaining.socks5.gssapimethod.serviceName

**Syntax:**

```text
chaining.socks5.gssapimethod.serviceName=SERVICE_NAME
```

**Description:**

The GSS-API service name for the other SOCKS5 server

**Value:** java.lang.String

### chaining.socks5.gssapimethod.suggestedConf

**Syntax:**

```text
chaining.socks5.gssapimethod.suggestedConf=true|false
```

**Description:**

The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default is true)

**Value:** java.lang.Boolean

### chaining.socks5.gssapimethod.suggestedInteg

**Syntax:**

```text
chaining.socks5.gssapimethod.suggestedInteg=-2147483648-2147483647
```

**Description:**

The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default is 0)

**Value:** java.lang.Integer

### chaining.socks5.methods

**Syntax:**

```text
chaining.socks5.methods=SOCKS5_METHODS
```

**Description:**

The comma separated list of acceptable authentication methods to the other SOCKS5 server (default is NO_AUTHENTICATION_REQUIRED)

**Value:** [SOCKS5 Methods](value-syntaxes.md#socks5-methods)

### chaining.socks5.socks5DatagramSocket.actualAddressAndPortUnknown

**Syntax:**

```text
chaining.socks5.socks5DatagramSocket.actualAddressAndPortUnknown=true|false
```

**Description:**

The boolean value to indicate that the actual address and port for sending UDP datagrams to the other SOCKS5 server is unknown (default is false)

**Value:** java.lang.Boolean

### chaining.socks5.socks5HostResolver.resolveFromSocks5Server

**Syntax:**

```text
chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true|false
```

**Description:**

The boolean value to indicate that host names are to be resolved from the other SOCKS5 server (default is false)

**Value:** java.lang.Boolean

### chaining.socks5.userpassmethod.password

**Syntax:**

```text
chaining.socks5.userpassmethod.password=PASSWORD
```

**Description:**

The password to be used to access the other SOCKS5 server

**Value:** java.lang.String

### chaining.socks5.userpassmethod.username

**Syntax:**

```text
chaining.socks5.userpassmethod.username=USERNAME
```

**Description:**

The username to be used to access the other SOCKS5 server

**Value:** java.lang.String

## Chaining SSL/TLS Settings

### chaining.ssl.enabled

**Syntax:**

```text
chaining.ssl.enabled=true|false
```

**Description:**

The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled (default is false)

**Value:** java.lang.Boolean

### chaining.ssl.enabledCipherSuites

**Syntax:**

```text
chaining.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### chaining.ssl.enabledProtocols

**Syntax:**

```text
chaining.ssl.enabledProtocols=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### chaining.ssl.keyStoreFile

**Syntax:**

```text
chaining.ssl.keyStoreFile=FILE
```

**Description:**

The key store file for the SSL/TLS connections to the other SOCKS server

**Value:** java.io.File

### chaining.ssl.keyStorePassword

**Syntax:**

```text
chaining.ssl.keyStorePassword=PASSWORD
```

**Description:**

The password for the key store for the SSL/TLS connections to the other SOCKS server

**Value:** java.lang.String

### chaining.ssl.keyStoreType

**Syntax:**

```text
chaining.ssl.keyStoreType=TYPE
```

**Description:**

The type of key store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)

**Value:** java.lang.String

### chaining.ssl.protocol

**Syntax:**

```text
chaining.ssl.protocol=PROTOCOL
```

**Description:**

The protocol version for the SSL/TLS connections to the other SOCKS server (default is TLSv1.2)

**Value:** java.lang.String

### chaining.ssl.trustStoreFile

**Syntax:**

```text
chaining.ssl.trustStoreFile=FILE
```

**Description:**

The trust store file for the SSL/TLS connections to the other SOCKS server

**Value:** java.io.File

### chaining.ssl.trustStorePassword

**Syntax:**

```text
chaining.ssl.trustStorePassword=PASSWORD
```

**Description:**

The password for the trust store for the SSL/TLS connections to the other SOCKS server

**Value:** java.lang.String

### chaining.ssl.trustStoreType

**Syntax:**

```text
chaining.ssl.trustStoreType=TYPE
```

**Description:**

The type of trust store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)

**Value:** java.lang.String

## DTLS Settings

### dtls.enabled

**Syntax:**

```text
dtls.enabled=true|false
```

**Description:**

The boolean value to indicate if DTLS connections to the SOCKS server are enabled (default is false)

**Value:** java.lang.Boolean

### dtls.enabledCipherSuites

**Syntax:**

```text
dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### dtls.enabledProtocols

**Syntax:**

```text
dtls.enabledProtocols=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### dtls.keyStoreFile

**Syntax:**

```text
dtls.keyStoreFile=FILE
```

**Description:**

The key store file for the DTLS connections to the SOCKS server

**Value:** java.io.File

### dtls.keyStorePassword

**Syntax:**

```text
dtls.keyStorePassword=PASSWORD
```

**Description:**

The password for the key store for the DTLS connections to the SOCKS server

**Value:** java.lang.String

### dtls.keyStoreType

**Syntax:**

```text
dtls.keyStoreType=TYPE
```

**Description:**

The type of key store file for the DTLS connections to the SOCKS server (default is PKCS12)

**Value:** java.lang.String

### dtls.maxPacketSize

**Syntax:**

```text
dtls.maxPacketSize=POSITIVE_INTEGER
```

**Description:**

The maximum packet size for the DTLS connections to the SOCKS server (default is 32768)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### dtls.protocol

**Syntax:**

```text
dtls.protocol=PROTOCOL
```

**Description:**

The protocol version for the DTLS connections to the SOCKS server (default is DTLSv1.2)

**Value:** java.lang.String

## SOCKS5 Settings

### socks5.gssapimethod.necReferenceImpl

**Syntax:**

```text
socks5.gssapimethod.necReferenceImpl=true|false
```

**Description:**

The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default is false)

**Value:** java.lang.Boolean

### socks5.gssapimethod.protectionLevels

**Syntax:**

```text
socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
```

**Description:**

The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

**Value:** [SOCKS5 GSS-API Method Protection Levels](value-syntaxes.md#socks5-gss-api-method-protection-levels)

### socks5.gssapimethod.suggestedConf

**Syntax:**

```text
socks5.gssapimethod.suggestedConf=true|false
```

**Description:**

The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default is true)

**Value:** java.lang.Boolean

### socks5.gssapimethod.suggestedInteg

**Syntax:**

```text
socks5.gssapimethod.suggestedInteg=-2147483648-2147483647
```

**Description:**

The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default is 0)

**Value:** java.lang.Integer

### socks5.methods

**Syntax:**

```text
socks5.methods=SOCKS5_METHODS
```

**Description:**

The comma separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)

**Value:** [SOCKS5 Methods](value-syntaxes.md#socks5-methods)

### socks5.onBindRequest.inboundSocketSettings

**Syntax:**

```text
socks5.onBindRequest.inboundSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the inbound socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onBindRequest.listenBindHost

**Syntax:**

```text
socks5.onBindRequest.listenBindHost=HOST
```

**Description:**

The binding host name or address for the listen socket if the provided host address is all zeros

**Value:** [Host](value-syntaxes.md#host)

### socks5.onBindRequest.listenBindPortRanges

**Syntax:**

```text
socks5.onBindRequest.listenBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the listen socket if the provided port is zero

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onBindRequest.listenSocketSettings

**Syntax:**

```text
socks5.onBindRequest.listenSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the listen socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onBindRequest.relayBufferSize

**Syntax:**

```text
socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBindRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.prepareTargetFacingSocket

**Syntax:**

```text
socks5.onConnectRequest.prepareTargetFacingSocket=true|false
```

**Description:**

The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default is false)

**Value:** java.lang.Boolean

### socks5.onConnectRequest.relayBufferSize

**Syntax:**

```text
socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.targetFacingBindHost

**Syntax:**

```text
socks5.onConnectRequest.targetFacingBindHost=HOST
```

**Description:**

The binding host name or address for the target-facing socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onConnectRequest.targetFacingBindPortRanges

**Syntax:**

```text
socks5.onConnectRequest.targetFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the target-facing socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onConnectRequest.targetFacingConnectTimeout

**Syntax:**

```text
socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on waiting for the target-facing socket to connect (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnectRequest.targetFacingSocketSettings

**Syntax:**

```text
socks5.onConnectRequest.targetFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the target-facing socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onRequest.bindHost

**Syntax:**

```text
socks5.onRequest.bindHost=HOST
```

**Description:**

The binding host name or address for all sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.bindTcpPortRanges

**Syntax:**

```text
socks5.onRequest.bindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onRequest.bindUdpPortRanges

**Syntax:**

```text
socks5.onRequest.bindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onRequest.externalFacingBindHost

**Syntax:**

```text
socks5.onRequest.externalFacingBindHost=HOST
```

**Description:**

The binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.externalFacingBindTcpPortRanges

**Syntax:**

```text
socks5.onRequest.externalFacingBindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all external-facing TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onRequest.externalFacingBindUdpPortRanges

**Syntax:**

```text
socks5.onRequest.externalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all external-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onRequest.externalFacingSocketSettings

**Syntax:**

```text
socks5.onRequest.externalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all external-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onRequest.internalFacingBindHost

**Syntax:**

```text
socks5.onRequest.internalFacingBindHost=HOST
```

**Description:**

The binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onRequest.internalFacingBindUdpPortRanges

**Syntax:**

```text
socks5.onRequest.internalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all internal-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onRequest.internalFacingSocketSettings

**Syntax:**

```text
socks5.onRequest.internalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all internal-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onRequest.relayBufferSize

**Syntax:**

```text
socks5.onRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data (default is 1024)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onRequest.socketSettings

**Syntax:**

```text
socks5.onRequest.socketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociateRequest.clientFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingBindHost=HOST
```

**Description:**

The binding host name or address for the client-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociateRequest.clientFacingBindPortRanges

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the client-facing UDP socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onUdpAssociateRequest.clientFacingSocketSettings

**Syntax:**

```text
socks5.onUdpAssociateRequest.clientFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the client-facing UDP socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociateRequest.peerFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingBindHost=HOST
```

**Description:**

The binding host name or address for the peer-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociateRequest.peerFacingBindPortRanges

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the peer-facing UDP socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onUdpAssociateRequest.peerFacingSocketSettings

**Syntax:**

```text
socks5.onUdpAssociateRequest.peerFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the peer-facing UDP socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociateRequest.relayBufferSize

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayIdleTimeout

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.userpassmethod.userRepository

**Syntax:**

```text
socks5.userpassmethod.userRepository=SOCKS5_USERPASSMETHOD_USER_REPOSITORY
```

**Description:**

The user repository used for username password authentication (default is StringSourceUserRepository:)

**Value:** [SOCKS5 Username Password Method User Repository](value-syntaxes.md#socks5-username-password-method-user-repository)

## SSL/TLS Settings

### ssl.enabled

**Syntax:**

```text
ssl.enabled=true|false
```

**Description:**

The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled (default is false)

**Value:** java.lang.Boolean

### ssl.enabledCipherSuites

**Syntax:**

```text
ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### ssl.enabledProtocols

**Syntax:**

```text
ssl.enabledProtocols=COMMA_SEPARATED_VALUES
```

**Description:**

The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

**Value:** [Comma Separated Values](value-syntaxes.md#comma-separated-values)

### ssl.keyStoreFile

**Syntax:**

```text
ssl.keyStoreFile=FILE
```

**Description:**

The key store file for the SSL/TLS connections to the SOCKS server

**Value:** java.io.File

### ssl.keyStorePassword

**Syntax:**

```text
ssl.keyStorePassword=PASSWORD
```

**Description:**

The password for the key store for the SSL/TLS connections to the SOCKS server

**Value:** java.lang.String

### ssl.keyStoreType

**Syntax:**

```text
ssl.keyStoreType=TYPE
```

**Description:**

The type of key store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)

**Value:** java.lang.String

### ssl.needClientAuth

**Syntax:**

```text
ssl.needClientAuth=true|false
```

**Description:**

The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server (default is false)

**Value:** java.lang.Boolean

### ssl.protocol

**Syntax:**

```text
ssl.protocol=PROTOCOL
```

**Description:**

The protocol version for the SSL/TLS connections to the SOCKS server (default is TLSv1.2)

**Value:** java.lang.String

### ssl.trustStoreFile

**Syntax:**

```text
ssl.trustStoreFile=FILE
```

**Description:**

The trust store file for the SSL/TLS connections to the SOCKS server

**Value:** java.io.File

### ssl.trustStorePassword

**Syntax:**

```text
ssl.trustStorePassword=PASSWORD
```

**Description:**

The password for the trust store for the SSL/TLS connections to the SOCKS server

**Value:** java.lang.String

### ssl.trustStoreType

**Syntax:**

```text
ssl.trustStoreType=TYPE
```

**Description:**

The type of trust store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)

**Value:** java.lang.String

### ssl.wantClientAuth

**Syntax:**

```text
ssl.wantClientAuth=true|false
```

**Description:**

The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server (default is false)

**Value:** java.lang.Boolean

