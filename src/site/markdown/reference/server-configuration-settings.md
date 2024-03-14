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
    -   [chaining.socks5.clientUdpAddressAndPortUnknown](#chaining-socks5-clientudpaddressandportunknown)
    -   [chaining.socks5.gssapimethod.mechanismOid](#chaining-socks5-gssapimethod-mechanismoid)
    -   [chaining.socks5.gssapimethod.necReferenceImpl](#chaining-socks5-gssapimethod-necreferenceimpl)
    -   [chaining.socks5.gssapimethod.protectionLevels](#chaining-socks5-gssapimethod-protectionlevels)
    -   [chaining.socks5.gssapimethod.serviceName](#chaining-socks5-gssapimethod-servicename)
    -   [chaining.socks5.methods](#chaining-socks5-methods)
    -   [chaining.socks5.useResolveCommand](#chaining-socks5-useresolvecommand)
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
    -   [socks5.methods](#socks5-methods)
    -   [socks5.onBind.inboundSocketSettings](#socks5-onbind-inboundsocketsettings)
    -   [socks5.onBind.listenBindHost](#socks5-onbind-listenbindhost)
    -   [socks5.onBind.listenBindPortRanges](#socks5-onbind-listenbindportranges)
    -   [socks5.onBind.listenSocketSettings](#socks5-onbind-listensocketsettings)
    -   [socks5.onBind.relayBufferSize](#socks5-onbind-relaybuffersize)
    -   [socks5.onBind.relayIdleTimeout](#socks5-onbind-relayidletimeout)
    -   [socks5.onBind.relayInboundBandwidthLimit](#socks5-onbind-relayinboundbandwidthlimit)
    -   [socks5.onBind.relayOutboundBandwidthLimit](#socks5-onbind-relayoutboundbandwidthlimit)
    -   [socks5.onCommand.bindHost](#socks5-oncommand-bindhost)
    -   [socks5.onCommand.bindTcpPortRanges](#socks5-oncommand-bindtcpportranges)
    -   [socks5.onCommand.bindUdpPortRanges](#socks5-oncommand-bindudpportranges)
    -   [socks5.onCommand.externalFacingBindHost](#socks5-oncommand-externalfacingbindhost)
    -   [socks5.onCommand.externalFacingBindTcpPortRanges](#socks5-oncommand-externalfacingbindtcpportranges)
    -   [socks5.onCommand.externalFacingBindUdpPortRanges](#socks5-oncommand-externalfacingbindudpportranges)
    -   [socks5.onCommand.externalFacingSocketSettings](#socks5-oncommand-externalfacingsocketsettings)
    -   [socks5.onCommand.internalFacingBindHost](#socks5-oncommand-internalfacingbindhost)
    -   [socks5.onCommand.internalFacingBindUdpPortRanges](#socks5-oncommand-internalfacingbindudpportranges)
    -   [socks5.onCommand.internalFacingSocketSettings](#socks5-oncommand-internalfacingsocketsettings)
    -   [socks5.onCommand.relayBufferSize](#socks5-oncommand-relaybuffersize)
    -   [socks5.onCommand.relayIdleTimeout](#socks5-oncommand-relayidletimeout)
    -   [socks5.onCommand.relayInboundBandwidthLimit](#socks5-oncommand-relayinboundbandwidthlimit)
    -   [socks5.onCommand.relayOutboundBandwidthLimit](#socks5-oncommand-relayoutboundbandwidthlimit)
    -   [socks5.onCommand.socketSettings](#socks5-oncommand-socketsettings)
    -   [socks5.onConnect.prepareServerFacingSocket](#socks5-onconnect-prepareserverfacingsocket)
    -   [socks5.onConnect.relayBufferSize](#socks5-onconnect-relaybuffersize)
    -   [socks5.onConnect.relayIdleTimeout](#socks5-onconnect-relayidletimeout)
    -   [socks5.onConnect.relayInboundBandwidthLimit](#socks5-onconnect-relayinboundbandwidthlimit)
    -   [socks5.onConnect.relayOutboundBandwidthLimit](#socks5-onconnect-relayoutboundbandwidthlimit)
    -   [socks5.onConnect.serverFacingBindHost](#socks5-onconnect-serverfacingbindhost)
    -   [socks5.onConnect.serverFacingBindPortRanges](#socks5-onconnect-serverfacingbindportranges)
    -   [socks5.onConnect.serverFacingConnectTimeout](#socks5-onconnect-serverfacingconnecttimeout)
    -   [socks5.onConnect.serverFacingSocketSettings](#socks5-onconnect-serverfacingsocketsettings)
    -   [socks5.onUdpAssociate.clientFacingBindHost](#socks5-onudpassociate-clientfacingbindhost)
    -   [socks5.onUdpAssociate.clientFacingBindPortRanges](#socks5-onudpassociate-clientfacingbindportranges)
    -   [socks5.onUdpAssociate.clientFacingSocketSettings](#socks5-onudpassociate-clientfacingsocketsettings)
    -   [socks5.onUdpAssociate.peerFacingBindHost](#socks5-onudpassociate-peerfacingbindhost)
    -   [socks5.onUdpAssociate.peerFacingBindPortRanges](#socks5-onudpassociate-peerfacingbindportranges)
    -   [socks5.onUdpAssociate.peerFacingSocketSettings](#socks5-onudpassociate-peerfacingsocketsettings)
    -   [socks5.onUdpAssociate.relayBufferSize](#socks5-onudpassociate-relaybuffersize)
    -   [socks5.onUdpAssociate.relayIdleTimeout](#socks5-onudpassociate-relayidletimeout)
    -   [socks5.onUdpAssociate.relayInboundBandwidthLimit](#socks5-onudpassociate-relayinboundbandwidthlimit)
    -   [socks5.onUdpAssociate.relayOutboundBandwidthLimit](#socks5-onudpassociate-relayoutboundbandwidthlimit)
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

The binding host name or address for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0.0.0.0)

**Value:** [Host](value-syntaxes.md#host)

### chaining.clientBindPortRanges

**Syntax:**

```text
chaining.clientBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0)

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### chaining.clientConnectTimeout

**Syntax:**

```text
chaining.clientConnectTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### chaining.clientSocketSettings

**Syntax:**

```text
chaining.clientSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 command RESOLVE and UDP ASSOCIATE)

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

### chaining.socks5.clientUdpAddressAndPortUnknown

**Syntax:**

```text
chaining.socks5.clientUdpAddressAndPortUnknown=true|false
```

**Description:**

The boolean value to indicate that the client UDP address and port for sending UDP datagrams to the other SOCKS5 server is unknown (default is false)

**Value:** java.lang.Boolean

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

### chaining.socks5.methods

**Syntax:**

```text
chaining.socks5.methods=SOCKS5_METHODS
```

**Description:**

The comma separated list of acceptable authentication methods to the other SOCKS5 server (default is NO_AUTHENTICATION_REQUIRED)

**Value:** [SOCKS5 Methods](value-syntaxes.md#socks5-methods)

### chaining.socks5.useResolveCommand

**Syntax:**

```text
chaining.socks5.useResolveCommand=true|false
```

**Description:**

The boolean value to indicate that the RESOLVE command is to be used on the other SOCKS5 server for resolving host names (default is false)

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

### socks5.methods

**Syntax:**

```text
socks5.methods=SOCKS5_METHODS
```

**Description:**

The comma separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)

**Value:** [SOCKS5 Methods](value-syntaxes.md#socks5-methods)

### socks5.onBind.inboundSocketSettings

**Syntax:**

```text
socks5.onBind.inboundSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the inbound socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onBind.listenBindHost

**Syntax:**

```text
socks5.onBind.listenBindHost=HOST
```

**Description:**

The binding host name or address for the listen socket if the provided host address is all zeros

**Value:** [Host](value-syntaxes.md#host)

### socks5.onBind.listenBindPortRanges

**Syntax:**

```text
socks5.onBind.listenBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the listen socket if the provided port is zero

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onBind.listenSocketSettings

**Syntax:**

```text
socks5.onBind.listenSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the listen socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onBind.relayBufferSize

**Syntax:**

```text
socks5.onBind.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayIdleTimeout

**Syntax:**

```text
socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onBind.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.bindHost

**Syntax:**

```text
socks5.onCommand.bindHost=HOST
```

**Description:**

The binding host name or address for all sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.bindTcpPortRanges

**Syntax:**

```text
socks5.onCommand.bindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onCommand.bindUdpPortRanges

**Syntax:**

```text
socks5.onCommand.bindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onCommand.externalFacingBindHost

**Syntax:**

```text
socks5.onCommand.externalFacingBindHost=HOST
```

**Description:**

The binding host name or address for all external-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.externalFacingBindTcpPortRanges

**Syntax:**

```text
socks5.onCommand.externalFacingBindTcpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all external-facing TCP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onCommand.externalFacingBindUdpPortRanges

**Syntax:**

```text
socks5.onCommand.externalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all external-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onCommand.externalFacingSocketSettings

**Syntax:**

```text
socks5.onCommand.externalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all external-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onCommand.internalFacingBindHost

**Syntax:**

```text
socks5.onCommand.internalFacingBindHost=HOST
```

**Description:**

The binding host name or address for all internal-facing sockets

**Value:** [Host](value-syntaxes.md#host)

### socks5.onCommand.internalFacingBindUdpPortRanges

**Syntax:**

```text
socks5.onCommand.internalFacingBindUdpPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for all internal-facing UDP sockets

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onCommand.internalFacingSocketSettings

**Syntax:**

```text
socks5.onCommand.internalFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all internal-facing sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onCommand.relayBufferSize

**Syntax:**

```text
socks5.onCommand.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data (default is 1024)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayIdleTimeout

**Syntax:**

```text
socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onCommand.socketSettings

**Syntax:**

```text
socks5.onCommand.socketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for all sockets

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onConnect.prepareServerFacingSocket

**Syntax:**

```text
socks5.onConnect.prepareServerFacingSocket=true|false
```

**Description:**

The boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default is false)

**Value:** java.lang.Boolean

### socks5.onConnect.relayBufferSize

**Syntax:**

```text
socks5.onConnect.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayIdleTimeout

**Syntax:**

```text
socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.serverFacingBindHost

**Syntax:**

```text
socks5.onConnect.serverFacingBindHost=HOST
```

**Description:**

The binding host name or address for the server-facing socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onConnect.serverFacingBindPortRanges

**Syntax:**

```text
socks5.onConnect.serverFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the server-facing socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onConnect.serverFacingConnectTimeout

**Syntax:**

```text
socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on waiting for the server-facing socket to connect (default is 60000)

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onConnect.serverFacingSocketSettings

**Syntax:**

```text
socks5.onConnect.serverFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the server-facing socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociate.clientFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingBindHost=HOST
```

**Description:**

The binding host name or address for the client-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociate.clientFacingBindPortRanges

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the client-facing UDP socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onUdpAssociate.clientFacingSocketSettings

**Syntax:**

```text
socks5.onUdpAssociate.clientFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the client-facing UDP socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociate.peerFacingBindHost

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingBindHost=HOST
```

**Description:**

The binding host name or address for the peer-facing UDP socket

**Value:** [Host](value-syntaxes.md#host)

### socks5.onUdpAssociate.peerFacingBindPortRanges

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingBindPortRanges=PORT_RANGES
```

**Description:**

The comma separated list of binding port ranges for the peer-facing UDP socket

**Value:** [Port Ranges](value-syntaxes.md#port-ranges)

### socks5.onUdpAssociate.peerFacingSocketSettings

**Syntax:**

```text
socks5.onUdpAssociate.peerFacingSocketSettings=SOCKET_SETTINGS
```

**Description:**

The comma separated list of socket settings for the peer-facing UDP socket

**Value:** [Socket Settings](value-syntaxes.md#socket-settings)

### socks5.onUdpAssociate.relayBufferSize

**Syntax:**

```text
socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER
```

**Description:**

The buffer size in bytes for relaying the data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayIdleTimeout

**Syntax:**

```text
socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER
```

**Description:**

The timeout in milliseconds on relaying no data

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayInboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER
```

**Description:**

The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value:** [Positive Integer](value-syntaxes.md#positive-integer)

### socks5.onUdpAssociate.relayOutboundBandwidthLimit

**Syntax:**

```text
socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER
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

