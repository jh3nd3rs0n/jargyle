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
<tr><td><a href="#backlog"><code>backlog</code></a></td><td>Non-negative Integer</td><td>The maximum length of the queue of incoming client connections to the SOCKS server<br/><b>Default Value:</b> <code>50</code></td></tr>
<tr><td><a href="#bindhost"><code>bindHost</code></a></td><td>Host</td><td>The default binding host name or address for all sockets<br/><b>Default Value:</b> <code>0.0.0.0</code></td></tr>
<tr><td><a href="#bindhostaddresstypes"><code>bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all sockets<br/><b>Default Value:</b> <code>HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS</code></td></tr>
<tr><td><a href="#bindtcpportranges"><code>bindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all TCP sockets<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#bindudpportranges"><code>bindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all UDP sockets<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#clientsocketsettings"><code>clientSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client socket</td></tr>
<tr><td><a href="#doc"><code>doc</code></a></td><td>String</td><td>A documentation setting</td></tr>
<tr><td><a href="#externalfacingbindhost"><code>externalFacingBindHost</code></a></td><td>Host</td><td>The default binding host name or address for all external-facing sockets</td></tr>
<tr><td><a href="#externalfacingbindhostaddresstypes"><code>externalFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all external-facing sockets</td></tr>
<tr><td><a href="#externalfacingbindtcpportranges"><code>externalFacingBindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all external-facing TCP sockets</td></tr>
<tr><td><a href="#externalfacingbindudpportranges"><code>externalFacingBindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all external-facing UDP sockets</td></tr>
<tr><td><a href="#externalfacingnetinterface"><code>externalFacingNetInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all external-facing sockets</td></tr>
<tr><td><a href="#externalfacingsocketsettings"><code>externalFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all external-facing sockets</td></tr>
<tr><td><a href="#internalfacingbindhost"><code>internalFacingBindHost</code></a></td><td>Host</td><td>The default binding host name or address for all internal-facing sockets</td></tr>
<tr><td><a href="#internalfacingbindhostaddresstypes"><code>internalFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of default acceptable binding host address types for all internal-facing sockets</td></tr>
<tr><td><a href="#internalfacingbindtcpportranges"><code>internalFacingBindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all internal-facing TCP sockets</td></tr>
<tr><td><a href="#internalfacingbindudpportranges"><code>internalFacingBindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of default binding port ranges for all internal-facing UDP sockets</td></tr>
<tr><td><a href="#internalfacingnetinterface"><code>internalFacingNetInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all internal-facing sockets</td></tr>
<tr><td><a href="#internalfacingsocketsettings"><code>internalFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all internal-facing sockets</td></tr>
<tr><td><a href="#lastrouteid"><code>lastRouteId</code></a></td><td>String</td><td>The ID for the last and unassigned route<br/><b>Default Value:</b> <code>lastRoute</code></td></tr>
<tr><td><a href="#netinterface"><code>netInterface</code></a></td><td>Network Interface</td><td>The default network interface that provides a binding host address for all sockets</td></tr>
<tr><td><a href="#port"><code>port</code></a></td><td>Port</td><td>The port for the SOCKS server<br/><b>Default Value:</b> <code>1080</code></td></tr>
<tr><td><a href="#routeselectionlogaction"><code>routeSelectionLogAction</code></a></td><td>Log Action</td><td>The logging action to take if a route is selected</td></tr>
<tr><td><a href="#routeselectionstrategy"><code>routeSelectionStrategy</code></a></td><td>Selection Strategy</td><td>The selection strategy for the next route<br/><b>Default Value:</b> <code>CYCLICAL</code></td></tr>
<tr><td><a href="#rule"><code>rule</code></a></td><td>Rule</td><td>A rule for the SOCKS server<br/><b>Default Value:</b> <code>firewallAction=ALLOW</code></td></tr>
<tr><td><a href="#socketsettings"><code>socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of default socket settings for all sockets</td></tr>
<tr><td><a href="#socksserverbindhost"><code>socksServerBindHost</code></a></td><td>Host</td><td>The binding host name or address for the SOCKS server socket</td></tr>
<tr><td><a href="#socksserverbindhostaddresstypes"><code>socksServerBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the SOCKS server socket</td></tr>
<tr><td><a href="#socksserverbindportranges"><code>socksServerBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the SOCKS server socket</td></tr>
<tr><td><a href="#socksservernetinterface"><code>socksServerNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the SOCKS server socket</td></tr>
<tr><td><a href="#socksserversocketsettings"><code>socksServerSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the SOCKS server socket</td></tr>
</table>

## Chaining General Settings

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#chaining-clientbindhost"><code>chaining.clientBindHost</code></a></td><td>Host</td><td>The binding host name or address for the client socket that is used to connect to the other SOCKS server<br/><b>Default Value:</b> <code>0.0.0.0</code></td></tr>
<tr><td><a href="#chaining-clientbindhostaddresstypes"><code>chaining.clientBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client socket that is used to connect to the other SOCKS server<br/><b>Default Value:</b> <code>HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS</code></td></tr>
<tr><td><a href="#chaining-clientbindportranges"><code>chaining.clientBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#chaining-clientconnecttimeout"><code>chaining.clientConnectTimeout</code></a></td><td>Non-negative Integer</td><td>The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (a timeout of 0 is interpreted as an infinite timeout)<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#chaining-clientnetinterface"><code>chaining.clientNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client socket that is used to connect to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-clientsocketsettings"><code>chaining.clientSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server</td></tr>
<tr><td><a href="#chaining-routeid"><code>chaining.routeId</code></a></td><td>String</td><td>The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers</td></tr>
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
<tr><td><a href="#socks-onbindrequest-inboundsocketsettings"><code>socks.onBindRequest.inboundSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the inbound socket</td></tr>
<tr><td><a href="#socks-onbindrequest-listenbindhost"><code>socks.onBindRequest.listenBindHost</code></a></td><td>Host</td><td>The binding host name or address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-listenbindhostaddresstypes"><code>socks.onBindRequest.listenBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-listenbindportranges"><code>socks.onBindRequest.listenBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the listen socket if the provided port is zero</td></tr>
<tr><td><a href="#socks-onbindrequest-listennetinterface"><code>socks.onBindRequest.listenNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-listensocketsettings"><code>socks.onBindRequest.listenSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the listen socket</td></tr>
<tr><td><a href="#socks-onbindrequest-relaybuffersize"><code>socks.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayidletimeout"><code>socks.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayinboundbandwidthlimit"><code>socks.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onbindrequest-relayoutboundbandwidthlimit"><code>socks.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-preparetargetfacingsocket"><code>socks.onConnectRequest.prepareTargetFacingSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socks-onconnectrequest-relaybuffersize"><code>socks.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayidletimeout"><code>socks.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayinboundbandwidthlimit"><code>socks.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayoutboundbandwidthlimit"><code>socks.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingbindhost"><code>socks.onConnectRequest.targetFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the target-facing socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingbindhostaddresstypes"><code>socks.onConnectRequest.targetFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the target-facing socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingbindportranges"><code>socks.onConnectRequest.targetFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the target-facing socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingconnecttimeout"><code>socks.onConnectRequest.targetFacingConnectTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on waiting for the target-facing socket to connect<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingnetinterface"><code>socks.onConnectRequest.targetFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the target-facing socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-targetfacingsocketsettings"><code>socks.onConnectRequest.targetFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the target-facing socket</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingbindhost"><code>socks.onRequest.externalFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for all external-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingbindhostaddresstypes"><code>socks.onRequest.externalFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for all external-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingbindtcpportranges"><code>socks.onRequest.externalFacingBindTcpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all external-facing TCP sockets</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingbindudpportranges"><code>socks.onRequest.externalFacingBindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all external-facing UDP sockets</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingnetinterface"><code>socks.onRequest.externalFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for all external-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-externalfacingsocketsettings"><code>socks.onRequest.externalFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for all external-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-internalfacingbindhost"><code>socks.onRequest.internalFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for all internal-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-internalfacingbindhostaddresstypes"><code>socks.onRequest.internalFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for all internal-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-internalfacingbindudpportranges"><code>socks.onRequest.internalFacingBindUdpPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for all internal-facing UDP sockets</td></tr>
<tr><td><a href="#socks-onrequest-internalfacingnetinterface"><code>socks.onRequest.internalFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for all internal-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-internalfacingsocketsettings"><code>socks.onRequest.internalFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for all internal-facing sockets</td></tr>
<tr><td><a href="#socks-onrequest-relaybuffersize"><code>socks.onRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data<br/><b>Default Value:</b> <code>1024</code></td></tr>
<tr><td><a href="#socks-onrequest-relayidletimeout"><code>socks.onRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#socks-onrequest-relayinboundbandwidthlimit"><code>socks.onRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onrequest-relayoutboundbandwidthlimit"><code>socks.onRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingbindhost"><code>socks.onUdpAssociateRequest.clientFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingbindhostaddresstypes"><code>socks.onUdpAssociateRequest.clientFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingbindportranges"><code>socks.onUdpAssociateRequest.clientFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingnetinterface"><code>socks.onUdpAssociateRequest.clientFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingsocketsettings"><code>socks.onUdpAssociateRequest.clientFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-peerfacingbindhost"><code>socks.onUdpAssociateRequest.peerFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-peerfacingbindhostaddresstypes"><code>socks.onUdpAssociateRequest.peerFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-peerfacingbindportranges"><code>socks.onUdpAssociateRequest.peerFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-peerfacingnetinterface"><code>socks.onUdpAssociateRequest.peerFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-peerfacingsocketsettings"><code>socks.onUdpAssociateRequest.peerFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relaybuffersize"><code>socks.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayidletimeout"><code>socks.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
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
<tr><td><a href="#socks5-onbindrequest-inboundsocketsettings"><code>socks5.onBindRequest.inboundSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the inbound socket</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindhost"><code>socks5.onBindRequest.listenBindHost</code></a></td><td>Host</td><td>The binding host name or address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindhostaddresstypes"><code>socks5.onBindRequest.listenBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-listenbindportranges"><code>socks5.onBindRequest.listenBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the listen socket if the provided port is zero</td></tr>
<tr><td><a href="#socks5-onbindrequest-listennetinterface"><code>socks5.onBindRequest.listenNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the listen socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-listensocketsettings"><code>socks5.onBindRequest.listenSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the listen socket</td></tr>
<tr><td><a href="#socks5-onbindrequest-relaybuffersize"><code>socks5.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayidletimeout"><code>socks5.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayinboundbandwidthlimit"><code>socks5.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayoutboundbandwidthlimit"><code>socks5.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-preparetargetfacingsocket"><code>socks5.onConnectRequest.prepareTargetFacingSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relaybuffersize"><code>socks5.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayidletimeout"><code>socks5.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayinboundbandwidthlimit"><code>socks5.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayoutboundbandwidthlimit"><code>socks5.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindhost"><code>socks5.onConnectRequest.targetFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindhostaddresstypes"><code>socks5.onConnectRequest.targetFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingbindportranges"><code>socks5.onConnectRequest.targetFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingconnecttimeout"><code>socks5.onConnectRequest.targetFacingConnectTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on waiting for the target-facing socket to connect</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingnetinterface"><code>socks5.onConnectRequest.targetFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-targetfacingsocketsettings"><code>socks5.onConnectRequest.targetFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the target-facing socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindhost"><code>socks5.onUdpAssociateRequest.clientFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindhostaddresstypes"><code>socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingbindportranges"><code>socks5.onUdpAssociateRequest.clientFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingnetinterface"><code>socks5.onUdpAssociateRequest.clientFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingsocketsettings"><code>socks5.onUdpAssociateRequest.clientFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindhost"><code>socks5.onUdpAssociateRequest.peerFacingBindHost</code></a></td><td>Host</td><td>The binding host name or address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindhostaddresstypes"><code>socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingbindportranges"><code>socks5.onUdpAssociateRequest.peerFacingBindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingnetinterface"><code>socks5.onUdpAssociateRequest.peerFacingNetInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-peerfacingsocketsettings"><code>socks5.onUdpAssociateRequest.peerFacingSocketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the peer-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relaybuffersize"><code>socks5.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayidletimeout"><code>socks5.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>The timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
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

### backlog

**Description:** The maximum length of the queue of incoming client connections to the SOCKS server

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

**Default Value:** `50`

### bindHost

**Description:** The default binding host name or address for all sockets

**Value Type:** [Host](value-types.md#host)

**Default Value:** `0.0.0.0`

### bindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all sockets

**Value Type:** [Host Address Types](value-types.md#host-address-types)

**Default Value:** `HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`

### bindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all TCP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

### bindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all UDP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

### chaining.clientBindHost

**Description:** The binding host name or address for the client socket that is used to connect to the other SOCKS server

**Value Type:** [Host](value-types.md#host)

**Default Value:** `0.0.0.0`

### chaining.clientBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client socket that is used to connect to the other SOCKS server

**Value Type:** [Host Address Types](value-types.md#host-address-types)

**Default Value:** `HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`

### chaining.clientBindPortRanges

**Description:** The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

### chaining.clientConnectTimeout

**Description:** The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (a timeout of 0 is interpreted as an infinite timeout)

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

**Default Value:** `60000`

### chaining.clientNetInterface

**Description:** The network interface that provides a binding host address for the client socket that is used to connect to the other SOCKS server

**Value Type:** [Network Interface](value-types.md#network-interface)

### chaining.clientSocketSettings

**Description:** The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### chaining.dtls.enabled

**Description:** The boolean value to indicate if DTLS connections to the other SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### chaining.dtls.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### chaining.dtls.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### chaining.dtls.protocol

**Description:** The protocol version for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `DTLSv1.2`

### chaining.dtls.trustStoreFile

**Description:** The trust store file for the DTLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

### chaining.dtls.trustStorePassword

**Description:** The password for the trust store for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.dtls.trustStoreType

**Description:** The type of trust store for the DTLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### chaining.dtls.wrappedReceiveBufferSize

**Description:** The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the other SOCKS server

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### chaining.routeId

**Description:** The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

**Value Type:** [String](value-types.md#string)

### chaining.socks.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the other SOCKS server

**Value Type:** [Oid](value-types.md#oid)

**Default Value:** `1.2.840.113554.1.2.2`

### chaining.socks.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### chaining.socks.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE`

### chaining.socks.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.socks.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `true`

### chaining.socks.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

**Default Value:** `0`

### chaining.socks.methods

**Description:** The comma separated list of acceptable authentication methods to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `NO_AUTHENTICATION_REQUIRED`

### chaining.socks.socksDatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### chaining.socks.userpassauthmethod.password

**Description:** The password to be used to access the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.socks.userpassauthmethod.username

**Description:** The username to be used to access the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.socks5.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the other SOCKS5 server

**Value Type:** [Oid](value-types.md#oid)

### chaining.socks5.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

### chaining.socks5.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [SOCKS5 GSS-API Authentication Method Protection Levels](value-types.md#socks5-gss-api-authentication-method-protection-levels)

### chaining.socks5.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

### chaining.socks5.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

### chaining.socks5.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

### chaining.socks5.methods

**Description:** The comma separated list of acceptable authentication methods to the other SOCKS5 server

**Value Type:** [SOCKS5 Methods](value-types.md#socks5-methods)

### chaining.socks5.socks5DatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS5 server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

### chaining.socks5.userpassauthmethod.password

**Description:** The password to be used to access the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

### chaining.socks5.userpassauthmethod.username

**Description:** The username to be used to access the other SOCKS5 server

**Value Type:** [String](value-types.md#string)

### chaining.socksServerUri

**Description:** The URI of the other SOCKS server

**Value Type:** [SOCKS Server URI](value-types.md#socks-server-uri)

### chaining.ssl.enabled

**Description:** The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### chaining.ssl.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### chaining.ssl.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### chaining.ssl.keyStoreFile

**Description:** The key store file for the SSL/TLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

### chaining.ssl.keyStorePassword

**Description:** The password for the key store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.ssl.keyStoreType

**Description:** The type of key store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### chaining.ssl.protocol

**Description:** The protocol version for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `TLSv1.2`

### chaining.ssl.trustStoreFile

**Description:** The trust store file for the SSL/TLS connections to the other SOCKS server

**Value Type:** [File](value-types.md#file)

### chaining.ssl.trustStorePassword

**Description:** The password for the trust store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

### chaining.ssl.trustStoreType

**Description:** The type of trust store for the SSL/TLS connections to the other SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### clientSocketSettings

**Description:** The comma separated list of socket settings for the client socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### doc

**Description:** A documentation setting

**Value Type:** [String](value-types.md#string)

### dtls.enabled

**Description:** The boolean value to indicate if DTLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### dtls.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### dtls.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### dtls.keyStoreFile

**Description:** The key store file for the DTLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

### dtls.keyStorePassword

**Description:** The password for the key store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

### dtls.keyStoreType

**Description:** The type of key store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### dtls.protocol

**Description:** The protocol version for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `DTLSv1.2`

### dtls.wrappedReceiveBufferSize

**Description:** The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### externalFacingBindHost

**Description:** The default binding host name or address for all external-facing sockets

**Value Type:** [Host](value-types.md#host)

### externalFacingBindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all external-facing sockets

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### externalFacingBindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all external-facing TCP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### externalFacingBindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all external-facing UDP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### externalFacingNetInterface

**Description:** The default network interface that provides a binding host address for all external-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### externalFacingSocketSettings

**Description:** The comma separated list of default socket settings for all external-facing sockets

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### internalFacingBindHost

**Description:** The default binding host name or address for all internal-facing sockets

**Value Type:** [Host](value-types.md#host)

### internalFacingBindHostAddressTypes

**Description:** The comma separated list of default acceptable binding host address types for all internal-facing sockets

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### internalFacingBindTcpPortRanges

**Description:** The comma separated list of default binding port ranges for all internal-facing TCP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### internalFacingBindUdpPortRanges

**Description:** The comma separated list of default binding port ranges for all internal-facing UDP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### internalFacingNetInterface

**Description:** The default network interface that provides a binding host address for all internal-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### internalFacingSocketSettings

**Description:** The comma separated list of default socket settings for all internal-facing sockets

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### lastRouteId

**Description:** The ID for the last and unassigned route

**Value Type:** [String](value-types.md#string)

**Default Value:** `lastRoute`

### netInterface

**Description:** The default network interface that provides a binding host address for all sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### port

**Description:** The port for the SOCKS server

**Value Type:** [Port](value-types.md#port)

**Default Value:** `1080`

### routeSelectionLogAction

**Description:** The logging action to take if a route is selected

**Value Type:** [Log Action](value-types.md#log-action)

### routeSelectionStrategy

**Description:** The selection strategy for the next route

**Value Type:** [Selection Strategy](value-types.md#selection-strategy)

**Default Value:** `CYCLICAL`

### rule

**Description:** A rule for the SOCKS server

**Value Type:** [Rule](value-types.md#rule)

**Default Value:** `firewallAction=ALLOW`

### socketSettings

**Description:** The comma separated list of default socket settings for all sockets

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### socks.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE`

### socks.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `true`

### socks.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

**Default Value:** `0`

### socks.methods

**Description:** The comma separated list of acceptable authentication methods in order of preference

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `NO_AUTHENTICATION_REQUIRED`

### socks.onBindRequest.inboundSocketSettings

**Description:** The comma separated list of socket settings for the inbound socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onBindRequest.listenBindHost

**Description:** The binding host name or address for the listen socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

### socks.onBindRequest.listenBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the listen socket if the provided host address is all zeros

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onBindRequest.listenBindPortRanges

**Description:** The comma separated list of binding port ranges for the listen socket if the provided port is zero

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onBindRequest.listenNetInterface

**Description:** The network interface that provides a binding host address for the listen socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onBindRequest.listenSocketSettings

**Description:** The comma separated list of socket settings for the listen socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onBindRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onBindRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onBindRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onBindRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onConnectRequest.prepareTargetFacingSocket

**Description:** The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### socks.onConnectRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onConnectRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onConnectRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onConnectRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onConnectRequest.targetFacingBindHost

**Description:** The binding host name or address for the target-facing socket

**Value Type:** [Host](value-types.md#host)

### socks.onConnectRequest.targetFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the target-facing socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onConnectRequest.targetFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the target-facing socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onConnectRequest.targetFacingConnectTimeout

**Description:** The timeout in milliseconds on waiting for the target-facing socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `60000`

### socks.onConnectRequest.targetFacingNetInterface

**Description:** The network interface that provides a binding host address for the target-facing socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onConnectRequest.targetFacingSocketSettings

**Description:** The comma separated list of socket settings for the target-facing socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onRequest.externalFacingBindHost

**Description:** The binding host name or address for all external-facing sockets

**Value Type:** [Host](value-types.md#host)

### socks.onRequest.externalFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for all external-facing sockets

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onRequest.externalFacingBindTcpPortRanges

**Description:** The comma separated list of binding port ranges for all external-facing TCP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onRequest.externalFacingBindUdpPortRanges

**Description:** The comma separated list of binding port ranges for all external-facing UDP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onRequest.externalFacingNetInterface

**Description:** The network interface that provides a binding host address for all external-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onRequest.externalFacingSocketSettings

**Description:** The comma separated list of socket settings for all external-facing sockets

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onRequest.internalFacingBindHost

**Description:** The binding host name or address for all internal-facing sockets

**Value Type:** [Host](value-types.md#host)

### socks.onRequest.internalFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for all internal-facing sockets

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onRequest.internalFacingBindUdpPortRanges

**Description:** The comma separated list of binding port ranges for all internal-facing UDP sockets

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onRequest.internalFacingNetInterface

**Description:** The network interface that provides a binding host address for all internal-facing sockets

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onRequest.internalFacingSocketSettings

**Description:** The comma separated list of socket settings for all internal-facing sockets

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `1024`

### socks.onRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

**Default Value:** `60000`

### socks.onRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onUdpAssociateRequest.clientFacingBindHost

**Description:** The binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks.onUdpAssociateRequest.clientFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onUdpAssociateRequest.clientFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the client-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onUdpAssociateRequest.clientFacingNetInterface

**Description:** The network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onUdpAssociateRequest.clientFacingSocketSettings

**Description:** The comma separated list of socket settings for the client-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onUdpAssociateRequest.peerFacingBindHost

**Description:** The binding host name or address for the peer-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks.onUdpAssociateRequest.peerFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the peer-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks.onUdpAssociateRequest.peerFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the peer-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks.onUdpAssociateRequest.peerFacingNetInterface

**Description:** The network interface that provides a binding host address for the peer-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks.onUdpAssociateRequest.peerFacingSocketSettings

**Description:** The comma separated list of socket settings for the peer-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks.onUdpAssociateRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onUdpAssociateRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks.userpassauthmethod.userRepository

**Description:** The user repository used for username password authentication

**Value Type:** [String](value-types.md#string)

**Default Value:** `StringSourceUserRepository:`

### socks5.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

### socks5.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)

**Value Type:** [SOCKS5 GSS-API Authentication Method Protection Levels](value-types.md#socks5-gss-api-authentication-method-protection-levels)

### socks5.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

### socks5.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

### socks5.methods

**Description:** The comma separated list of acceptable authentication methods in order of preference

**Value Type:** [SOCKS5 Methods](value-types.md#socks5-methods)

### socks5.onBindRequest.inboundSocketSettings

**Description:** The comma separated list of socket settings for the inbound socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks5.onBindRequest.listenBindHost

**Description:** The binding host name or address for the listen socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

### socks5.onBindRequest.listenBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the listen socket if the provided host address is all zeros

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks5.onBindRequest.listenBindPortRanges

**Description:** The comma separated list of binding port ranges for the listen socket if the provided port is zero

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks5.onBindRequest.listenNetInterface

**Description:** The network interface that provides a binding host address for the listen socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onBindRequest.listenSocketSettings

**Description:** The comma separated list of socket settings for the listen socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks5.onBindRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onBindRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.prepareTargetFacingSocket

**Description:** The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

### socks5.onConnectRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.targetFacingBindHost

**Description:** The binding host name or address for the target-facing socket

**Value Type:** [Host](value-types.md#host)

### socks5.onConnectRequest.targetFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the target-facing socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks5.onConnectRequest.targetFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the target-facing socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks5.onConnectRequest.targetFacingConnectTimeout

**Description:** The timeout in milliseconds on waiting for the target-facing socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onConnectRequest.targetFacingNetInterface

**Description:** The network interface that provides a binding host address for the target-facing socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onConnectRequest.targetFacingSocketSettings

**Description:** The comma separated list of socket settings for the target-facing socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks5.onUdpAssociateRequest.clientFacingBindHost

**Description:** The binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the client-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks5.onUdpAssociateRequest.clientFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the client-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks5.onUdpAssociateRequest.clientFacingNetInterface

**Description:** The network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onUdpAssociateRequest.clientFacingSocketSettings

**Description:** The comma separated list of socket settings for the client-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks5.onUdpAssociateRequest.peerFacingBindHost

**Description:** The binding host name or address for the peer-facing UDP socket

**Value Type:** [Host](value-types.md#host)

### socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the peer-facing UDP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socks5.onUdpAssociateRequest.peerFacingBindPortRanges

**Description:** The comma separated list of binding port ranges for the peer-facing UDP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socks5.onUdpAssociateRequest.peerFacingNetInterface

**Description:** The network interface that provides a binding host address for the peer-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socks5.onUdpAssociateRequest.peerFacingSocketSettings

**Description:** The comma separated list of socket settings for the peer-facing UDP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### socks5.onUdpAssociateRequest.relayBufferSize

**Description:** The buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayIdleTimeout

**Description:** The timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

### socks5.userpassauthmethod.userRepository

**Description:** The user repository used for username password authentication

**Value Type:** [SOCKS5 Username Password Authentication Method User Repository](value-types.md#socks5-username-password-authentication-method-user-repository)

### socksServerBindHost

**Description:** The binding host name or address for the SOCKS server socket

**Value Type:** [Host](value-types.md#host)

### socksServerBindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the SOCKS server socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

### socksServerBindPortRanges

**Description:** The comma separated list of binding port ranges for the SOCKS server socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

### socksServerNetInterface

**Description:** The network interface that provides a binding host address for the SOCKS server socket

**Value Type:** [Network Interface](value-types.md#network-interface)

### socksServerSocketSettings

**Description:** The comma separated list of socket settings for the SOCKS server socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

### ssl.enabled

**Description:** The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### ssl.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### ssl.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

### ssl.keyStoreFile

**Description:** The key store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

### ssl.keyStorePassword

**Description:** The password for the key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

### ssl.keyStoreType

**Description:** The type of key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### ssl.needClientAuth

**Description:** The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

### ssl.protocol

**Description:** The protocol version for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `TLSv1.2`

### ssl.trustStoreFile

**Description:** The trust store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

### ssl.trustStorePassword

**Description:** The password for the trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

### ssl.trustStoreType

**Description:** The type of trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

### ssl.wantClientAuth

**Description:** The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

