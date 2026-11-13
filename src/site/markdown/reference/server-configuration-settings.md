# Server Configuration Settings

## Contents

-   [General Settings](#general-settings)
-   [Chaining General Settings](#chaining-general-settings)
-   [Chaining DTLS Settings](#chaining-dtls-settings)
-   [Chaining SOCKS Settings](#chaining-socks-settings)
-   [Chaining SOCKS5 Settings](#chaining-socks5-settings)
-   [Chaining SSL/TLS Settings](#chaining-ssl-tls-settings)
-   [DTLS Settings](#dtls-settings)
-   [SOCKS Settings](#socks-settings)
-   [SOCKS5 Settings](#socks5-settings)
-   [SSL/TLS Settings](#ssl-tls-settings)
-   [All Settings](#all-settings)

## General Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#backlog"><code>backlog</code></a></td><td>Non-negative Integer</td><td>The maximum length of the queue of incoming client TCP connections to the SOCKS server<br/><b>Default Value:</b> <code>50</code></td></tr>
<tr><td><a href="#bindhost"><code>bindHost</code></a></td><td>Host</td><td>The default binding host name or address for all endpoints<br/><b>Default Value:</b> <code>0.0.0.0</code></td></tr>
<tr><td><a href="#bindhostaddresstypes"><code>bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all endpoints<br/><b>Default Value:</b> <code>HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS</code></td></tr>
<tr><td><a href="#bindtcpportranges"><code>bindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all TCP endpoints<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#bindudpportranges"><code>bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all UDP endpoints<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#clientendpoints-bindhost"><code>clientEndpoints.bindHost</code></a></td><td>Host</td><td>The default binding host name or address for all client endpoints</td></tr>
<tr><td><a href="#clientendpoints-bindhostaddresstypes"><code>clientEndpoints.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all client endpoints</td></tr>
<tr><td><a href="#clientendpoints-bindtcpportranges"><code>clientEndpoints.bindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all client TCP endpoints</td></tr>
<tr><td><a href="#clientendpoints-bindudpportranges"><code>clientEndpoints.bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all client UDP endpoints</td></tr>
<tr><td><a href="#clientendpoints-netinterface"><code>clientEndpoints.netInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all client endpoints</td></tr>
<tr><td><a href="#clientendpoints-socketsettings"><code>clientEndpoints.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all client endpoints</td></tr>
<tr><td><a href="#clientfacingserversocket-bindhost"><code>clientFacingServerSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the client-facing TCP server socket</td></tr>
<tr><td><a href="#clientfacingserversocket-bindhostaddresstypes"><code>clientFacingServerSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client-facing TCP server socket</td></tr>
<tr><td><a href="#clientfacingserversocket-bindportranges"><code>clientFacingServerSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client-facing TCP server socket</td></tr>
<tr><td><a href="#clientfacingserversocket-netinterface"><code>clientFacingServerSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client-facing TCP server socket</td></tr>
<tr><td><a href="#clientfacingserversocket-socketsettings"><code>clientFacingServerSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client-facing TCP server socket</td></tr>
<tr><td><a href="#clientsocket-socketsettings"><code>clientSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client TCP socket</td></tr>
<tr><td><a href="#doc"><code>doc</code></a></td><td>String</td><td>A documentation setting</td></tr>
<tr><td><a href="#lastrouteid"><code>lastRouteId</code></a></td><td>String</td><td>The ID for the last and unassigned route<br/><b>Default Value:</b> <code>lastRoute</code></td></tr>
<tr><td><a href="#netinterface"><code>netInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all endpoints</td></tr>
<tr><td><a href="#port"><code>port</code></a></td><td>Port</td><td>The port for the SOCKS server (or specifically the port for the client-facing TCP server socket)<br/><b>Default Value:</b> <code>1080</code></td></tr>
<tr><td><a href="#remoteendpoints-bindhost"><code>remoteEndpoints.bindHost</code></a></td><td>Host</td><td>The default binding host name or address for all remote endpoints</td></tr>
<tr><td><a href="#remoteendpoints-bindhostaddresstypes"><code>remoteEndpoints.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all remote endpoints</td></tr>
<tr><td><a href="#remoteendpoints-bindtcpportranges"><code>remoteEndpoints.bindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all remote TCP endpoints</td></tr>
<tr><td><a href="#remoteendpoints-bindudpportranges"><code>remoteEndpoints.bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all remote UDP endpoints</td></tr>
<tr><td><a href="#remoteendpoints-netinterface"><code>remoteEndpoints.netInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all remote endpoints</td></tr>
<tr><td><a href="#remoteendpoints-socketsettings"><code>remoteEndpoints.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all remote endpoints</td></tr>
<tr><td><a href="#routeselectionlogaction"><code>routeSelectionLogAction</code></a></td><td>Log Action</td><td>The logging action to take if a route is selected</td></tr>
<tr><td><a href="#routeselectionstrategy"><code>routeSelectionStrategy</code></a></td><td>Selection Strategy</td><td>The selection strategy for the next route<br/><b>Default Value:</b> <code>CYCLICAL</code></td></tr>
<tr><td><a href="#rule"><code>rule</code></a></td><td>Rule</td><td>A rule for the SOCKS server<br/><b>Default Value:</b> <code>firewallAction=ALLOW</code></td></tr>
<tr><td><a href="#socketsettings"><code>socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all endpoints</td></tr>
</table>

## Chaining General Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-routeid"><code>chaining.routeId</code></a></td><td>String</td><td>The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers</td></tr>
<tr><td><a href="#chaining-socksserversocket-bindhost"><code>chaining.socksServerSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the other SOCKS server TCP socket<br/><b>Default Value:</b> <code>0.0.0.0</code></td></tr>
<tr><td><a href="#chaining-socksserversocket-bindhostaddresstypes"><code>chaining.socksServerSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the other SOCKS server TCP socket<br/><b>Default Value:</b> <code>HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS</code></td></tr>
<tr><td><a href="#chaining-socksserversocket-bindportranges"><code>chaining.socksServerSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the other SOCKS server TCP socket<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#chaining-socksserversocket-connecttimeout"><code>chaining.socksServerSocket.connectTimeout</code></a></td><td>Non-negative Integer</td><td>The timeout in milliseconds on waiting for the other SOCKS server TCP socket to connect (a timeout of 0 is interpreted as an infinite timeout)<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#chaining-socksserversocket-netinterface"><code>chaining.socksServerSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the other SOCKS server TCP socket</td></tr>
<tr><td><a href="#chaining-socksserversocket-preparesocket"><code>chaining.socksServerSocket.prepareSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the other SOCKS server TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the other SOCKS server host name, and setting the specified timeout on waiting to connect)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-socksserversocket-socketsettings"><code>chaining.socksServerSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the other SOCKS server TCP socket</td></tr>
<tr><td><a href="#chaining-socksserveruri"><code>chaining.socksServerUri</code></a></td><td>SOCKS Server URI</td><td>The URI of the other SOCKS server</td></tr>
</table>

## Chaining DTLS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-dtls-enabled"><code>chaining.dtls.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if DTLS connections to the other SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-dtls-enabledciphersuites"><code>chaining.dtls.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-dtls-enabledprotocols"><code>chaining.dtls.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-dtls-protocol"><code>chaining.dtls.protocol</code></a></td><td>String</td><td>The protocol version for the DTLS connections to the other SOCKS server<br/><b>Default Value:</b> <code>DTLSv1.2</code></td></tr>
<tr><td><a href="#chaining-dtls-truststorefile"><code>chaining.dtls.trustStoreFile</code></a></td><td>File</td><td>The trust store file for the DTLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-dtls-truststorepassword"><code>chaining.dtls.trustStorePassword</code></a></td><td>String</td><td>The password for the trust store for the DTLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-dtls-truststoretype"><code>chaining.dtls.trustStoreType</code></a></td><td>String</td><td>The type of trust store for the DTLS connections to the other SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#chaining-dtls-wrappedreceivebuffersize"><code>chaining.dtls.wrappedReceiveBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the other SOCKS server</td></tr>
</table>

## Chaining SOCKS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-mechanismoid"><code>chaining.socks.gssapiauthmethod.mechanismOid</code></a></td><td>Oid</td><td>The object ID for the GSS-API authentication mechanism to the other SOCKS server<br/><b>Default Value:</b> <code>1.2.840.113554.1.2.2</code></td></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-necreferenceimpl"><code>chaining.socks.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS server use the NEC reference implementation<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-protectionlevels"><code>chaining.socks.gssapiauthmethod.protectionLevels</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.)<br/><b>Default Value:</b> <code>REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE</code></td></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-servicename"><code>chaining.socks.gssapiauthmethod.serviceName</code></a></td><td>String</td><td>The GSS-API service name for the other SOCKS server</td></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-suggestedconf"><code>chaining.socks.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>true</code></td></tr>
<tr><td><a href="#chaining-socks-gssapiauthmethod-suggestedinteg"><code>chaining.socks.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#chaining-socks-methods"><code>chaining.socks.methods</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable authentication methods to the other SOCKS server<br/><b>Default Value:</b> <code>NO_AUTHENTICATION_REQUIRED</code></td></tr>
<tr><td><a href="#chaining-socks-socksdatagramsocket-clientinfounavailable"><code>chaining.socks.socksDatagramSocket.clientInfoUnavailable</code></a></td><td>Boolean</td><td>The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS server (an address and port of all zeros is sent instead)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-socks-sockshostresolver-resolvefromsocksserver"><code>chaining.socks.socksHostResolver.resolveFromSocksServer</code></a></td><td>Boolean</td><td>The boolean value to indicate if host names are to be resolved from the other SOCKS server<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-socks-userpassauthmethod-password"><code>chaining.socks.userpassauthmethod.password</code></a></td><td>String</td><td>The password to be used to access the other SOCKS server</td></tr>
<tr><td><a href="#chaining-socks-userpassauthmethod-username"><code>chaining.socks.userpassauthmethod.username</code></a></td><td>String</td><td>The username to be used to access the other SOCKS server</td></tr>
</table>

## Chaining SOCKS5 Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-mechanismoid"><code>chaining.socks5.gssapiauthmethod.mechanismOid</code></a></td><td>Oid</td><td>The object ID for the GSS-API authentication mechanism to the other SOCKS5 server</td></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-necreferenceimpl"><code>chaining.socks5.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation</td></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-protectionlevels"><code>chaining.socks5.gssapiauthmethod.protectionLevels</code></a></td><td>SOCKS5 GSS-API Authentication Method Protection Levels</td><td>The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)</td></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-servicename"><code>chaining.socks5.gssapiauthmethod.serviceName</code></a></td><td>String</td><td>The GSS-API service name for the other SOCKS5 server</td></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-suggestedconf"><code>chaining.socks5.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#chaining-socks5-gssapiauthmethod-suggestedinteg"><code>chaining.socks5.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#chaining-socks5-methods"><code>chaining.socks5.methods</code></a></td><td>SOCKS5 Methods</td><td>The comma separated list of acceptable authentication methods to the other SOCKS5 server</td></tr>
<tr><td><a href="#chaining-socks5-socks5datagramsocket-clientinfounavailable"><code>chaining.socks5.socks5DatagramSocket.clientInfoUnavailable</code></a></td><td>Boolean</td><td>The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS5 server (an address and port of all zeros is sent instead)</td></tr>
<tr><td><a href="#chaining-socks5-socks5hostresolver-resolvefromsocks5server"><code>chaining.socks5.socks5HostResolver.resolveFromSocks5Server</code></a></td><td>Boolean</td><td>The boolean value to indicate if host names are to be resolved from the other SOCKS5 server</td></tr>
<tr><td><a href="#chaining-socks5-userpassauthmethod-password"><code>chaining.socks5.userpassauthmethod.password</code></a></td><td>String</td><td>The password to be used to access the other SOCKS5 server</td></tr>
<tr><td><a href="#chaining-socks5-userpassauthmethod-username"><code>chaining.socks5.userpassauthmethod.username</code></a></td><td>String</td><td>The username to be used to access the other SOCKS5 server</td></tr>
</table>

## Chaining SSL/TLS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-ssl-enabled"><code>chaining.ssl.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#chaining-ssl-enabledciphersuites"><code>chaining.ssl.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-enabledprotocols"><code>chaining.ssl.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-keystorefile"><code>chaining.ssl.keyStoreFile</code></a></td><td>File</td><td>The key store file for the SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-keystorepassword"><code>chaining.ssl.keyStorePassword</code></a></td><td>String</td><td>The password for the key store for the SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-keystoretype"><code>chaining.ssl.keyStoreType</code></a></td><td>String</td><td>The type of key store for the SSL/TLS connections to the other SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#chaining-ssl-protocol"><code>chaining.ssl.protocol</code></a></td><td>String</td><td>The protocol version for the SSL/TLS connections to the other SOCKS server<br/><b>Default Value:</b> <code>TLSv1.2</code></td></tr>
<tr><td><a href="#chaining-ssl-truststorefile"><code>chaining.ssl.trustStoreFile</code></a></td><td>File</td><td>The trust store file for the SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-truststorepassword"><code>chaining.ssl.trustStorePassword</code></a></td><td>String</td><td>The password for the trust store for the SSL/TLS connections to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-ssl-truststoretype"><code>chaining.ssl.trustStoreType</code></a></td><td>String</td><td>The type of trust store for the SSL/TLS connections to the other SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
</table>

## DTLS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#dtls-enabled"><code>dtls.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if DTLS connections to the SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#dtls-enabledciphersuites"><code>dtls.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#dtls-enabledprotocols"><code>dtls.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#dtls-keystorefile"><code>dtls.keyStoreFile</code></a></td><td>File</td><td>The key store file for the DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#dtls-keystorepassword"><code>dtls.keyStorePassword</code></a></td><td>String</td><td>The password for the key store for the DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#dtls-keystoretype"><code>dtls.keyStoreType</code></a></td><td>String</td><td>The type of key store for the DTLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#dtls-protocol"><code>dtls.protocol</code></a></td><td>String</td><td>The protocol version for the DTLS connections to the SOCKS server<br/><b>Default Value:</b> <code>DTLSv1.2</code></td></tr>
<tr><td><a href="#dtls-wrappedreceivebuffersize"><code>dtls.wrappedReceiveBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server</td></tr>
</table>

## SOCKS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks-gssapiauthmethod-necreferenceimpl"><code>socks.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socks-gssapiauthmethod-protectionlevels"><code>socks.gssapiauthmethod.protectionLevels</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)<br/><b>Default Value:</b> <code>REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE</code></td></tr>
<tr><td><a href="#socks-gssapiauthmethod-suggestedconf"><code>socks.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>true</code></td></tr>
<tr><td><a href="#socks-gssapiauthmethod-suggestedinteg"><code>socks.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#socks-methods"><code>socks.methods</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable authentication methods in order of preference<br/><b>Default Value:</b> <code>NO_AUTHENTICATION_REQUIRED</code></td></tr>
<tr><td><a href="#socks-onbindrequest-relaybuffersize"><code>socks.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayidletimeout"><code>socks.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayinboundbandwidthlimit"><code>socks.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onbindrequest-relayoutboundbandwidthlimit"><code>socks.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindhost"><code>socks.onBindRequest.remoteFacingServerSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindhostaddresstypes"><code>socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindportranges"><code>socks.onBindRequest.remoteFacingServerSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-netinterface"><code>socks.onBindRequest.remoteFacingServerSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-socketsettings"><code>socks.onBindRequest.remoteFacingServerSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote-facing TCP server socket</td></tr>
<tr><td><a href="#socks-onbindrequest-remotesocket-socketsettings"><code>socks.onBindRequest.remoteSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-relaybuffersize"><code>socks.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayidletimeout"><code>socks.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayinboundbandwidthlimit"><code>socks.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayoutboundbandwidthlimit"><code>socks.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindhost"><code>socks.onConnectRequest.remoteSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindhostaddresstypes"><code>socks.onConnectRequest.remoteSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindportranges"><code>socks.onConnectRequest.remoteSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-connecttimeout"><code>socks.onConnectRequest.remoteSocket.connectTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on waiting for the remote TCP socket to connect<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-netinterface"><code>socks.onConnectRequest.remoteSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-preparesocket"><code>socks.onConnectRequest.remoteSocket.prepareSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-socketsettings"><code>socks.onConnectRequest.remoteSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindhost"><code>socks.onRequest.clientEndpoints.bindHost</code></a></td><td>Host</td><td>The binding host name or address for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindhostaddresstypes"><code>socks.onRequest.clientEndpoints.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindudpportranges"><code>socks.onRequest.clientEndpoints.bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all client UDP endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-netinterface"><code>socks.onRequest.clientEndpoints.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-socketsettings"><code>socks.onRequest.clientEndpoints.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-relaybuffersize"><code>socks.onRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data<br/><b>Default Value:</b> <code>1024</code></td></tr>
<tr><td><a href="#socks-onrequest-relayidletimeout"><code>socks.onRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#socks-onrequest-relayinboundbandwidthlimit"><code>socks.onRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onrequest-relayoutboundbandwidthlimit"><code>socks.onRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindhost"><code>socks.onRequest.remoteEndpoints.bindHost</code></a></td><td>Host</td><td>The binding host name or address for all remote endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindhostaddresstypes"><code>socks.onRequest.remoteEndpoints.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for all remote endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindtcpportranges"><code>socks.onRequest.remoteEndpoints.bindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all remote TCP endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindudpportranges"><code>socks.onRequest.remoteEndpoints.bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all remote UDP endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-netinterface"><code>socks.onRequest.remoteEndpoints.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for all remote endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-socketsettings"><code>socks.onRequest.remoteEndpoints.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for all remote endpoints</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindhost"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstypes"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindportranges"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-netinterface"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-socketsettings"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relaybuffersize"><code>socks.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayidletimeout"><code>socks.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindhost"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstypes"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindportranges"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-netinterface"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-socketsettings"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-userpassauthmethod-userrepository"><code>socks.userpassauthmethod.userRepository</code></a></td><td>String</td><td>The user repository used for username password authentication<br/><b>Default Value:</b> <code>StringSourceUserRepository:</code></td></tr>
</table>

## SOCKS5 Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks5-gssapiauthmethod-necreferenceimpl"><code>socks5.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation</td></tr>
<tr><td><a href="#socks5-gssapiauthmethod-protectionlevels"><code>socks5.gssapiauthmethod.protectionLevels</code></a></td><td>SOCKS5 GSS-API Authentication Method Protection Levels</td><td>The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)</td></tr>
<tr><td><a href="#socks5-gssapiauthmethod-suggestedconf"><code>socks5.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#socks5-gssapiauthmethod-suggestedinteg"><code>socks5.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#socks5-methods"><code>socks5.methods</code></a></td><td>SOCKS5 Methods</td><td>The comma separated list of acceptable authentication methods in order of preference</td></tr>
<tr><td><a href="#socks5-onbindrequest-relaybuffersize"><code>socks5.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayidletimeout"><code>socks5.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayinboundbandwidthlimit"><code>socks5.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayoutboundbandwidthlimit"><code>socks5.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindhost"><code>socks5.onBindRequest.remoteFacingServerSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindhostaddresstypes"><code>socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindportranges"><code>socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-netinterface"><code>socks5.onBindRequest.remoteFacingServerSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-socketsettings"><code>socks5.onBindRequest.remoteFacingServerSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote-facing TCP server socket</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotesocket-socketsettings"><code>socks5.onBindRequest.remoteSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relaybuffersize"><code>socks5.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayidletimeout"><code>socks5.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayinboundbandwidthlimit"><code>socks5.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayoutboundbandwidthlimit"><code>socks5.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindhost"><code>socks5.onConnectRequest.remoteSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindhostaddresstypes"><code>socks5.onConnectRequest.remoteSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindportranges"><code>socks5.onConnectRequest.remoteSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-connecttimeout"><code>socks5.onConnectRequest.remoteSocket.connectTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on waiting for the remote TCP socket to connect</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-netinterface"><code>socks5.onConnectRequest.remoteSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-preparesocket"><code>socks5.onConnectRequest.remoteSocket.prepareSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-socketsettings"><code>socks5.onConnectRequest.remoteSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhost"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstypes"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindportranges"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-netinterface"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-socketsettings"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relaybuffersize"><code>socks5.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayidletimeout"><code>socks5.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhost"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstypes"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindportranges"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-netinterface"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-socketsettings"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-userpassauthmethod-userrepository"><code>socks5.userpassauthmethod.userRepository</code></a></td><td>SOCKS5 Username Password Authentication Method User Repository</td><td>The user repository used for username password authentication</td></tr>
</table>

## SSL/TLS Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#ssl-enabled"><code>ssl.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#ssl-enabledciphersuites"><code>ssl.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-enabledprotocols"><code>ssl.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-keystorefile"><code>ssl.keyStoreFile</code></a></td><td>File</td><td>The key store file for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-keystorepassword"><code>ssl.keyStorePassword</code></a></td><td>String</td><td>The password for the key store for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-keystoretype"><code>ssl.keyStoreType</code></a></td><td>String</td><td>The type of key store for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#ssl-needclientauth"><code>ssl.needClientAuth</code></a></td><td>Boolean</td><td>The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#ssl-protocol"><code>ssl.protocol</code></a></td><td>String</td><td>The protocol version for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>TLSv1.2</code></td></tr>
<tr><td><a href="#ssl-truststorefile"><code>ssl.trustStoreFile</code></a></td><td>File</td><td>The trust store file for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-truststorepassword"><code>ssl.trustStorePassword</code></a></td><td>String</td><td>The password for the trust store for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#ssl-truststoretype"><code>ssl.trustStoreType</code></a></td><td>String</td><td>The type of trust store for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#ssl-wantclientauth"><code>ssl.wantClientAuth</code></a></td><td>Boolean</td><td>The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>false</code></td></tr>
</table>

## All Settings

<a id="backlog"></a>
### backlog

**Description:** The maximum length of the queue of incoming client TCP connections to the SOCKS server

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

**Default Value:** `50`

<a id="bindhost"></a>
### bindHost

**Description:** The default binding host name or address for all endpoints

**Value Type:** [Host](value-types.md#host)

**Default Value:** `0.0.0.0`

<a id="bindhostaddresstypes"></a>
### bindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all endpoints

**Value Type:** [Host Address Types](value-types.md#host-address-types)

**Default Value:** `HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`

<a id="bindtcpportranges"></a>
### bindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all TCP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

<a id="bindudpportranges"></a>
### bindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all UDP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

<a id="chaining-dtls-enabled"></a>
### chaining.dtls.enabled

**Description:** The boolean value to indicate if DTLS connections to the other SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-dtls-enabledciphersuites"></a>
### chaining.dtls.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="chaining-dtls-enabledprotocols"></a>
### chaining.dtls.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="chaining-dtls-protocol"></a>
### chaining.dtls.protocol

**Description:** The protocol version for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `DTLSv1.2`

<a id="chaining-dtls-truststorefile"></a>
### chaining.dtls.trustStoreFile

**Description:** The trust store file for the DTLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="chaining-dtls-truststorepassword"></a>
### chaining.dtls.trustStorePassword

**Description:** The password for the trust store for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-dtls-truststoretype"></a>
### chaining.dtls.trustStoreType

**Description:** The type of trust store for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="chaining-dtls-wrappedreceivebuffersize"></a>
### chaining.dtls.wrappedReceiveBufferSize

**Description:** The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the other SOCKS server

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="chaining-routeid"></a>
### chaining.routeId

**Description:** The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks-gssapiauthmethod-mechanismoid"></a>
### chaining.socks.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the other SOCKS server

**Value Type:** [Oid](value-types.md#oid)

**Default Value:** `1.2.840.113554.1.2.2`

<a id="chaining-socks-gssapiauthmethod-necreferenceimpl"></a>
### chaining.socks.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-socks-gssapiauthmethod-protectionlevels"></a>
### chaining.socks.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE`

<a id="chaining-socks-gssapiauthmethod-servicename"></a>
### chaining.socks.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks-gssapiauthmethod-suggestedconf"></a>
### chaining.socks.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `true`

<a id="chaining-socks-gssapiauthmethod-suggestedinteg"></a>
### chaining.socks.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

**Default Value:** `0`

<a id="chaining-socks-methods"></a>
### chaining.socks.methods

**Description:** The comma separated list of acceptable authentication methods to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `NO_AUTHENTICATION_REQUIRED`

<a id="chaining-socks-socksdatagramsocket-clientinfounavailable"></a>
### chaining.socks.socksDatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-socks-sockshostresolver-resolvefromsocksserver"></a>
### chaining.socks.socksHostResolver.resolveFromSocksServer

**Description:** The boolean value to indicate if host names are to be resolved from the other SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-socks-userpassauthmethod-password"></a>
### chaining.socks.userpassauthmethod.password

**Description:** The password to be used to access the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks-userpassauthmethod-username"></a>
### chaining.socks.userpassauthmethod.username

**Description:** The username to be used to access the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks5-gssapiauthmethod-mechanismoid"></a>
### chaining.socks5.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the other SOCKS5 server

**Value Type:** [Oid](value-types.md#oid)

<a id="chaining-socks5-gssapiauthmethod-necreferenceimpl"></a>
### chaining.socks5.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

<a id="chaining-socks5-gssapiauthmethod-protectionlevels"></a>
### chaining.socks5.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [SOCKS5 GSS-API Authentication Method Protection Levels](value-types.md#socks5-gss-api-authentication-method-protection-levels)

<a id="chaining-socks5-gssapiauthmethod-servicename"></a>
### chaining.socks5.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks5-gssapiauthmethod-suggestedconf"></a>
### chaining.socks5.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="chaining-socks5-gssapiauthmethod-suggestedinteg"></a>
### chaining.socks5.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

<a id="chaining-socks5-methods"></a>
### chaining.socks5.methods

**Description:** The comma separated list of acceptable authentication methods to the other SOCKS5 server

**Value Type:** [SOCKS5 Methods](value-types.md#socks5-methods)

<a id="chaining-socks5-socks5datagramsocket-clientinfounavailable"></a>
### chaining.socks5.socks5DatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS5 server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="chaining-socks5-socks5hostresolver-resolvefromsocks5server"></a>
### chaining.socks5.socks5HostResolver.resolveFromSocks5Server

**Description:** The boolean value to indicate if host names are to be resolved from the other SOCKS5 server

**Value Type:** [Boolean](value-types.md#boolean)

<a id="chaining-socks5-userpassauthmethod-password"></a>
### chaining.socks5.userpassauthmethod.password

**Description:** The password to be used to access the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socks5-userpassauthmethod-username"></a>
### chaining.socks5.userpassauthmethod.username

**Description:** The username to be used to access the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="chaining-socksserversocket-bindhost"></a>
### chaining.socksServerSocket.bindHost

**Description:** The binding host name or address for the other SOCKS server TCP socket

**Value Type:** [Host](value-types.md#host)

**Default Value:** `0.0.0.0`

<a id="chaining-socksserversocket-bindhostaddresstypes"></a>
### chaining.socksServerSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the other SOCKS server TCP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

**Default Value:** `HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`

<a id="chaining-socksserversocket-bindportranges"></a>
### chaining.socksServerSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the other SOCKS server TCP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

<a id="chaining-socksserversocket-connecttimeout"></a>
### chaining.socksServerSocket.connectTimeout

**Description:** The timeout in milliseconds on waiting for the other SOCKS server TCP socket to connect (a timeout of 0 is interpreted as an infinite timeout)

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

**Default Value:** `60000`

<a id="chaining-socksserversocket-netinterface"></a>
### chaining.socksServerSocket.netInterface

**Description:** The network interface that provides a binding host address for the other SOCKS server TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="chaining-socksserversocket-preparesocket"></a>
### chaining.socksServerSocket.prepareSocket

**Description:** The boolean value to indicate if the other SOCKS server TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the other SOCKS server host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-socksserversocket-socketsettings"></a>
### chaining.socksServerSocket.socketSettings

**Description:** The comma separated list of socket settings for the other SOCKS server TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="chaining-socksserveruri"></a>
### chaining.socksServerUri

**Description:** The URI of the other SOCKS server

**Value Type:** [SOCKS Server URI](value-types.md#socks-server-uri)

<a id="chaining-ssl-enabled"></a>
### chaining.ssl.enabled

**Description:** The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="chaining-ssl-enabledciphersuites"></a>
### chaining.ssl.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="chaining-ssl-enabledprotocols"></a>
### chaining.ssl.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="chaining-ssl-keystorefile"></a>
### chaining.ssl.keyStoreFile

**Description:** The key store file for the SSL/TLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="chaining-ssl-keystorepassword"></a>
### chaining.ssl.keyStorePassword

**Description:** The password for the key store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-ssl-keystoretype"></a>
### chaining.ssl.keyStoreType

**Description:** The type of key store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="chaining-ssl-protocol"></a>
### chaining.ssl.protocol

**Description:** The protocol version for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `TLSv1.2`

<a id="chaining-ssl-truststorefile"></a>
### chaining.ssl.trustStoreFile

**Description:** The trust store file for the SSL/TLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="chaining-ssl-truststorepassword"></a>
### chaining.ssl.trustStorePassword

**Description:** The password for the trust store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="chaining-ssl-truststoretype"></a>
### chaining.ssl.trustStoreType

**Description:** The type of trust store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="clientendpoints-bindhost"></a>
### clientEndpoints.bindHost

**Description:** The default binding host name or address for all client endpoints

**Value Type:** [Host](value-types.md#host)

<a id="clientendpoints-bindhostaddresstypes"></a>
### clientEndpoints.bindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all client endpoints

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="clientendpoints-bindtcpportranges"></a>
### clientEndpoints.bindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all client TCP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="clientendpoints-bindudpportranges"></a>
### clientEndpoints.bindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all client UDP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="clientendpoints-netinterface"></a>
### clientEndpoints.netInterface

**Description:** The default network interface that provides a binding host address for all client endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="clientendpoints-socketsettings"></a>
### clientEndpoints.socketSettings

**Description:** The comma separated list of default socket settings for all client endpoints

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="clientfacingserversocket-bindhost"></a>
### clientFacingServerSocket.bindHost

**Description:** The binding host name or address for the client-facing TCP server socket

**Value Type:** [Host](value-types.md#host)

<a id="clientfacingserversocket-bindhostaddresstypes"></a>
### clientFacingServerSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client-facing TCP server socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="clientfacingserversocket-bindportranges"></a>
### clientFacingServerSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the client-facing TCP server socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="clientfacingserversocket-netinterface"></a>
### clientFacingServerSocket.netInterface

**Description:** The network interface that provides a binding host address for the client-facing TCP server socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="clientfacingserversocket-socketsettings"></a>
### clientFacingServerSocket.socketSettings

**Description:** The comma separated list of socket settings for the client-facing TCP server socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="clientsocket-socketsettings"></a>
### clientSocket.socketSettings

**Description:** The comma separated list of socket settings for the client TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="doc"></a>
### doc

**Description:** A documentation setting

**Value Type:** [String](value-types.md#string)

<a id="dtls-enabled"></a>
### dtls.enabled

**Description:** The boolean value to indicate if DTLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="dtls-enabledciphersuites"></a>
### dtls.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="dtls-enabledprotocols"></a>
### dtls.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="dtls-keystorefile"></a>
### dtls.keyStoreFile

**Description:** The key store file for the DTLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="dtls-keystorepassword"></a>
### dtls.keyStorePassword

**Description:** The password for the key store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="dtls-keystoretype"></a>
### dtls.keyStoreType

**Description:** The type of key store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="dtls-protocol"></a>
### dtls.protocol

**Description:** The protocol version for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `DTLSv1.2`

<a id="dtls-wrappedreceivebuffersize"></a>
### dtls.wrappedReceiveBufferSize

**Description:** The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="lastrouteid"></a>
### lastRouteId

**Description:** The ID for the last and unassigned route

**Value Type:** [String](value-types.md#string)

**Default Value:** `lastRoute`

<a id="netinterface"></a>
### netInterface

**Description:** The default network interface that provides a binding host address for all endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="port"></a>
### port

**Description:** The port for the SOCKS server (or specifically the port for the client-facing TCP server socket)

**Value Type:** [Port](value-types.md#port)

**Default Value:** `1080`

<a id="remoteendpoints-bindhost"></a>
### remoteEndpoints.bindHost

**Description:** The default binding host name or address for all remote endpoints

**Value Type:** [Host](value-types.md#host)

<a id="remoteendpoints-bindhostaddresstypes"></a>
### remoteEndpoints.bindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all remote endpoints

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="remoteendpoints-bindtcpportranges"></a>
### remoteEndpoints.bindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all remote TCP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="remoteendpoints-bindudpportranges"></a>
### remoteEndpoints.bindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all remote UDP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="remoteendpoints-netinterface"></a>
### remoteEndpoints.netInterface

**Description:** The default network interface that provides a binding host address for all remote endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="remoteendpoints-socketsettings"></a>
### remoteEndpoints.socketSettings

**Description:** The comma separated list of default socket settings for all remote endpoints

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="routeselectionlogaction"></a>
### routeSelectionLogAction

**Description:** The logging action to take if a route is selected

**Value Type:** [Log Action](value-types.md#log-action)

<a id="routeselectionstrategy"></a>
### routeSelectionStrategy

**Description:** The selection strategy for the next route

**Value Type:** [Selection Strategy](value-types.md#selection-strategy)

**Default Value:** `CYCLICAL`

<a id="rule"></a>
### rule

**Description:** A rule for the SOCKS server

**Value Type:** [Rule](value-types.md#rule)

**Default Value:** `firewallAction=ALLOW`

<a id="socketsettings"></a>
### socketSettings

**Description:** The comma separated list of default socket settings for all endpoints

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-gssapiauthmethod-necreferenceimpl"></a>
### socks.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socks-gssapiauthmethod-protectionlevels"></a>
### socks.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE`

<a id="socks-gssapiauthmethod-suggestedconf"></a>
### socks.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `true`

<a id="socks-gssapiauthmethod-suggestedinteg"></a>
### socks.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

**Default Value:** `0`

<a id="socks-methods"></a>
### socks.methods

**Description:** The comma separated list of acceptable authentication methods in order of preference

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `NO_AUTHENTICATION_REQUIRED`

<a id="socks-onbindrequest-relaybuffersize"></a>
### socks.onBindRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayidletimeout"></a>
### socks.onBindRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayinboundbandwidthlimit"></a>
### socks.onBindRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayoutboundbandwidthlimit"></a>
### socks.onBindRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-remotefacingserversocket-bindhost"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindHost

**Description:** The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

<a id="socks-onbindrequest-remotefacingserversocket-bindhostaddresstypes"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onbindrequest-remotefacingserversocket-bindportranges"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onbindrequest-remotefacingserversocket-netinterface"></a>
### socks.onBindRequest.remoteFacingServerSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onbindrequest-remotefacingserversocket-socketsettings"></a>
### socks.onBindRequest.remoteFacingServerSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote-facing TCP server socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onbindrequest-remotesocket-socketsettings"></a>
### socks.onBindRequest.remoteSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onconnectrequest-relaybuffersize"></a>
### socks.onConnectRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayidletimeout"></a>
### socks.onConnectRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayinboundbandwidthlimit"></a>
### socks.onConnectRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayoutboundbandwidthlimit"></a>
### socks.onConnectRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-remotesocket-bindhost"></a>
### socks.onConnectRequest.remoteSocket.bindHost

**Description:** The binding host name or address for the remote TCP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onconnectrequest-remotesocket-bindhostaddresstypes"></a>
### socks.onConnectRequest.remoteSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote TCP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onconnectrequest-remotesocket-bindportranges"></a>
### socks.onConnectRequest.remoteSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote TCP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onconnectrequest-remotesocket-connecttimeout"></a>
### socks.onConnectRequest.remoteSocket.connectTimeout

**Description:** The timeout in milliseconds on waiting for the remote TCP socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `60000`

<a id="socks-onconnectrequest-remotesocket-netinterface"></a>
### socks.onConnectRequest.remoteSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onconnectrequest-remotesocket-preparesocket"></a>
### socks.onConnectRequest.remoteSocket.prepareSocket

**Description:** The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socks-onconnectrequest-remotesocket-socketsettings"></a>
### socks.onConnectRequest.remoteSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onrequest-clientendpoints-bindhost"></a>
### socks.onRequest.clientEndpoints.bindHost

**Description:** The binding host name or address for all client endpoints

**Value Type:** [Host](value-types.md#host)

<a id="socks-onrequest-clientendpoints-bindhostaddresstypes"></a>
### socks.onRequest.clientEndpoints.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for all client endpoints

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onrequest-clientendpoints-bindudpportranges"></a>
### socks.onRequest.clientEndpoints.bindUdpPortRanges

**Description:** The comma separated list of binding port ranges for all client UDP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onrequest-clientendpoints-netinterface"></a>
### socks.onRequest.clientEndpoints.netInterface

**Description:** The network interface that provides a binding host address for all client endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onrequest-clientendpoints-socketsettings"></a>
### socks.onRequest.clientEndpoints.socketSettings

**Description:** The comma separated list of socket settings for all client endpoints

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onrequest-relaybuffersize"></a>
### socks.onRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `1024`

<a id="socks-onrequest-relayidletimeout"></a>
### socks.onRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `60000`

<a id="socks-onrequest-relayinboundbandwidthlimit"></a>
### socks.onRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-relayoutboundbandwidthlimit"></a>
### socks.onRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-remoteendpoints-bindhost"></a>
### socks.onRequest.remoteEndpoints.bindHost

**Description:** The binding host name or address for all remote endpoints

**Value Type:** [Host](value-types.md#host)

<a id="socks-onrequest-remoteendpoints-bindhostaddresstypes"></a>
### socks.onRequest.remoteEndpoints.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for all remote endpoints

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onrequest-remoteendpoints-bindtcpportranges"></a>
### socks.onRequest.remoteEndpoints.bindTcpPortRanges

**Description:** The comma separated list of binding port ranges for all remote TCP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onrequest-remoteendpoints-bindudpportranges"></a>
### socks.onRequest.remoteEndpoints.bindUdpPortRanges

**Description:** The comma separated list of binding port ranges for all remote UDP endpoints

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onrequest-remoteendpoints-netinterface"></a>
### socks.onRequest.remoteEndpoints.netInterface

**Description:** The network interface that provides a binding host address for all remote endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onrequest-remoteendpoints-socketsettings"></a>
### socks.onRequest.remoteEndpoints.socketSettings

**Description:** The comma separated list of socket settings for all remote endpoints

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindhost"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost

**Description:** The binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstypes"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindportranges"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the client-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-netinterface"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface

**Description:** The network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-socketsettings"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings

**Description:** The comma separated list of socket settings for the client-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-onudpassociaterequest-relaybuffersize"></a>
### socks.onUdpAssociateRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayidletimeout"></a>
### socks.onUdpAssociateRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayinboundbandwidthlimit"></a>
### socks.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayoutboundbandwidthlimit"></a>
### socks.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindhost"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost

**Description:** The binding host name or address for the remote-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstypes"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindportranges"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-netinterface"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-socketsettings"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks-userpassauthmethod-userrepository"></a>
### socks.userpassauthmethod.userRepository

**Description:** The user repository used for username password authentication

**Value Type:** [String](value-types.md#string)

**Default Value:** `StringSourceUserRepository:`

<a id="socks5-gssapiauthmethod-necreferenceimpl"></a>
### socks5.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socks5-gssapiauthmethod-protectionlevels"></a>
### socks5.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)

**Value Type:** [SOCKS5 GSS-API Authentication Method Protection Levels](value-types.md#socks5-gss-api-authentication-method-protection-levels)

<a id="socks5-gssapiauthmethod-suggestedconf"></a>
### socks5.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socks5-gssapiauthmethod-suggestedinteg"></a>
### socks5.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

<a id="socks5-methods"></a>
### socks5.methods

**Description:** The comma separated list of acceptable authentication methods in order of preference

**Value Type:** [SOCKS5 Methods](value-types.md#socks5-methods)

<a id="socks5-onbindrequest-relaybuffersize"></a>
### socks5.onBindRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayidletimeout"></a>
### socks5.onBindRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayinboundbandwidthlimit"></a>
### socks5.onBindRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayoutboundbandwidthlimit"></a>
### socks5.onBindRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-remotefacingserversocket-bindhost"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindHost

**Description:** The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onbindrequest-remotefacingserversocket-bindhostaddresstypes"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks5-onbindrequest-remotefacingserversocket-bindportranges"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks5-onbindrequest-remotefacingserversocket-netinterface"></a>
### socks5.onBindRequest.remoteFacingServerSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onbindrequest-remotefacingserversocket-socketsettings"></a>
### socks5.onBindRequest.remoteFacingServerSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote-facing TCP server socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks5-onbindrequest-remotesocket-socketsettings"></a>
### socks5.onBindRequest.remoteSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks5-onconnectrequest-relaybuffersize"></a>
### socks5.onConnectRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayidletimeout"></a>
### socks5.onConnectRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayinboundbandwidthlimit"></a>
### socks5.onConnectRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayoutboundbandwidthlimit"></a>
### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-remotesocket-bindhost"></a>
### socks5.onConnectRequest.remoteSocket.bindHost

**Description:** The binding host name or address for the remote TCP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onconnectrequest-remotesocket-bindhostaddresstypes"></a>
### socks5.onConnectRequest.remoteSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote TCP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks5-onconnectrequest-remotesocket-bindportranges"></a>
### socks5.onConnectRequest.remoteSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote TCP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks5-onconnectrequest-remotesocket-connecttimeout"></a>
### socks5.onConnectRequest.remoteSocket.connectTimeout

**Description:** The timeout in milliseconds on waiting for the remote TCP socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-remotesocket-netinterface"></a>
### socks5.onConnectRequest.remoteSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onconnectrequest-remotesocket-preparesocket"></a>
### socks5.onConnectRequest.remoteSocket.prepareSocket

**Description:** The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socks5-onconnectrequest-remotesocket-socketsettings"></a>
### socks5.onConnectRequest.remoteSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhost"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost

**Description:** The binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstypes"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindportranges"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the client-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-netinterface"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface

**Description:** The network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-socketsettings"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings

**Description:** The comma separated list of socket settings for the client-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks5-onudpassociaterequest-relaybuffersize"></a>
### socks5.onUdpAssociateRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayidletimeout"></a>
### socks5.onUdpAssociateRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayinboundbandwidthlimit"></a>
### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayoutboundbandwidthlimit"></a>
### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhost"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost

**Description:** The binding host name or address for the remote-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstypes"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the remote-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindportranges"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the remote-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-netinterface"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface

**Description:** The network interface that provides a binding host address for the remote-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-socketsettings"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings

**Description:** The comma separated list of socket settings for the remote-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socks5-userpassauthmethod-userrepository"></a>
### socks5.userpassauthmethod.userRepository

**Description:** The user repository used for username password authentication

**Value Type:** [SOCKS5 Username Password Authentication Method User Repository](value-types.md#socks5-username-password-authentication-method-user-repository)

<a id="ssl-enabled"></a>
### ssl.enabled

**Description:** The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="ssl-enabledciphersuites"></a>
### ssl.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="ssl-enabledprotocols"></a>
### ssl.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="ssl-keystorefile"></a>
### ssl.keyStoreFile

**Description:** The key store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="ssl-keystorepassword"></a>
### ssl.keyStorePassword

**Description:** The password for the key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="ssl-keystoretype"></a>
### ssl.keyStoreType

**Description:** The type of key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="ssl-needclientauth"></a>
### ssl.needClientAuth

**Description:** The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="ssl-protocol"></a>
### ssl.protocol

**Description:** The protocol version for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `TLSv1.2`

<a id="ssl-truststorefile"></a>
### ssl.trustStoreFile

**Description:** The trust store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="ssl-truststorepassword"></a>
### ssl.trustStorePassword

**Description:** The password for the trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="ssl-truststoretype"></a>
### ssl.trustStoreType

**Description:** The type of trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="ssl-wantclientauth"></a>
### ssl.wantClientAuth

**Description:** The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

