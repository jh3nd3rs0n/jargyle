# Client Properties

## Contents

-   [General Properties](#general-properties)
-   [DTLS Properties](#dtls-properties)
-   [SOCKS Properties](#socks-properties)
-   [SOCKS5 Properties](#socks5-properties)
-   [SSL/TLS Properties](#ssl-tls-properties)
-   [All Properties](#all-properties)

## General Properties

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socksclient-socksserversocket-bindhost"><code>socksClient.socksServerSocket.bindHost</code></a></td><td>Host</td><td>The binding host name or address for the SOCKS server TCP socket<br/><b>Default Value:</b> <code>0.0.0.0</code></td></tr>
<tr><td><a href="#socksclient-socksserversocket-bindhostaddresstypes"><code>socksClient.socksServerSocket.bindHostAddressTypes</code></a></td><td>Host Address Types</td><td>The comma separated list of acceptable binding host address types for the SOCKS server TCP socket<br/><b>Default Value:</b> <code>HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS</code></td></tr>
<tr><td><a href="#socksclient-socksserversocket-bindportranges"><code>socksClient.socksServerSocket.bindPortRanges</code></a></td><td>Port Ranges</td><td>The comma separated list of binding port ranges for the SOCKS server TCP socket<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#socksclient-socksserversocket-connecttimeout"><code>socksClient.socksServerSocket.connectTimeout</code></a></td><td>Non-negative Integer</td><td>The timeout in milliseconds on waiting for the SOCKS server TCP socket to connect (a timeout of 0 is interpreted as an infinite timeout)<br/><b>Default Value:</b> <code>60000</code></td></tr>
<tr><td><a href="#socksclient-socksserversocket-netinterface"><code>socksClient.socksServerSocket.netInterface</code></a></td><td>Network Interface</td><td>The network interface that provides a binding host address for the SOCKS server TCP socket</td></tr>
<tr><td><a href="#socksclient-socksserversocket-preparesocket"><code>socksClient.socksServerSocket.prepareSocket</code></a></td><td>Boolean</td><td>The boolean value to indicate if the SOCKS server TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the SOCKS server host name, and setting the specified timeout on waiting to connect)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-socksserversocket-socketsettings"><code>socksClient.socksServerSocket.socketSettings</code></a></td><td>Socket Settings</td><td>The comma separated list of socket settings for the SOCKS server TCP socket</td></tr>
<tr><td><a href="#socksclient-socksserveruri"><code>socksClient.socksServerUri</code></a></td><td>SOCKS Server URI</td><td>The URI of the SOCKS server</td></tr>
</table>

## DTLS Properties

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socksclient-dtls-enabled"><code>socksClient.dtls.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if DTLS connections to the SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-dtls-enabledciphersuites"><code>socksClient.dtls.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-dtls-enabledprotocols"><code>socksClient.dtls.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-dtls-protocol"><code>socksClient.dtls.protocol</code></a></td><td>String</td><td>The protocol version for the DTLS connections to the SOCKS server<br/><b>Default Value:</b> <code>DTLSv1.2</code></td></tr>
<tr><td><a href="#socksclient-dtls-truststorefile"><code>socksClient.dtls.trustStoreFile</code></a></td><td>File</td><td>The trust store file for the DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-dtls-truststorepassword"><code>socksClient.dtls.trustStorePassword</code></a></td><td>String</td><td>The password for the trust store for the DTLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-dtls-truststoretype"><code>socksClient.dtls.trustStoreType</code></a></td><td>String</td><td>The type of trust store for the DTLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#socksclient-dtls-wrappedreceivebuffersize"><code>socksClient.dtls.wrappedReceiveBufferSize</code></a></td><td>Positive Integer</td><td>The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server</td></tr>
</table>

## SOCKS Properties

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-mechanismoid"><code>socksClient.socks.gssapiauthmethod.mechanismOid</code></a></td><td>Oid</td><td>The object ID for the GSS-API authentication mechanism to the SOCKS server<br/><b>Default Value:</b> <code>1.2.840.113554.1.2.2</code></td></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-necreferenceimpl"><code>socksClient.socks.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the SOCKS server use the NEC reference implementation<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-protectionlevels"><code>socksClient.socks.gssapiauthmethod.protectionLevels</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protection levels after GSS-API authentication with the SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.)<br/><b>Default Value:</b> <code>REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE</code></td></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-servicename"><code>socksClient.socks.gssapiauthmethod.serviceName</code></a></td><td>String</td><td>The GSS-API service name for the SOCKS server</td></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-suggestedconf"><code>socksClient.socks.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>true</code></td></tr>
<tr><td><a href="#socksclient-socks-gssapiauthmethod-suggestedinteg"><code>socksClient.socks.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)<br/><b>Default Value:</b> <code>0</code></td></tr>
<tr><td><a href="#socksclient-socks-methods"><code>socksClient.socks.methods</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable authentication methods to the SOCKS server<br/><b>Default Value:</b> <code>NO_AUTHENTICATION_REQUIRED</code></td></tr>
<tr><td><a href="#socksclient-socks-socksdatagramsocket-clientinfounavailable"><code>socksClient.socks.socksDatagramSocket.clientInfoUnavailable</code></a></td><td>Boolean</td><td>The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the SOCKS server (an address and port of all zeros is sent instead)<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-socks-sockshostresolver-resolvefromsocksserver"><code>socksClient.socks.socksHostResolver.resolveFromSocksServer</code></a></td><td>Boolean</td><td>The boolean value to indicate if host names are to be resolved from the SOCKS server<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-socks-userpassauthmethod-password"><code>socksClient.socks.userpassauthmethod.password</code></a></td><td>String</td><td>The password to be used to access the SOCKS server</td></tr>
<tr><td><a href="#socksclient-socks-userpassauthmethod-username"><code>socksClient.socks.userpassauthmethod.username</code></a></td><td>String</td><td>The username to be used to access the SOCKS server</td></tr>
</table>

## SOCKS5 Properties

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-mechanismoid"><code>socksClient.socks5.gssapiauthmethod.mechanismOid</code></a></td><td>Oid</td><td>The object ID for the GSS-API authentication mechanism to the SOCKS5 server</td></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-necreferenceimpl"><code>socksClient.socks5.gssapiauthmethod.necReferenceImpl</code></a></td><td>Boolean</td><td>The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the SOCKS5 server use the NEC reference implementation</td></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-protectionlevels"><code>socksClient.socks5.gssapiauthmethod.protectionLevels</code></a></td><td>SOCKS5 GSS-API Authentication Method Protection Levels</td><td>The comma separated list of acceptable protection levels after GSS-API authentication with the SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)</td></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-servicename"><code>socksClient.socks5.gssapiauthmethod.serviceName</code></a></td><td>String</td><td>The GSS-API service name for the SOCKS5 server</td></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-suggestedconf"><code>socksClient.socks5.gssapiauthmethod.suggestedConf</code></a></td><td>Boolean</td><td>The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#socksclient-socks5-gssapiauthmethod-suggestedinteg"><code>socksClient.socks5.gssapiauthmethod.suggestedInteg</code></a></td><td>Integer</td><td>The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)</td></tr>
<tr><td><a href="#socksclient-socks5-methods"><code>socksClient.socks5.methods</code></a></td><td>SOCKS5 Methods</td><td>The comma separated list of acceptable authentication methods to the SOCKS5 server</td></tr>
<tr><td><a href="#socksclient-socks5-socks5datagramsocket-clientinfounavailable"><code>socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable</code></a></td><td>Boolean</td><td>The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the SOCKS5 server (an address and port of all zeros is sent instead)</td></tr>
<tr><td><a href="#socksclient-socks5-socks5hostresolver-resolvefromsocks5server"><code>socksClient.socks5.socks5HostResolver.resolveFromSocks5Server</code></a></td><td>Boolean</td><td>The boolean value to indicate if host names are to be resolved from the SOCKS5 server</td></tr>
<tr><td><a href="#socksclient-socks5-userpassauthmethod-password"><code>socksClient.socks5.userpassauthmethod.password</code></a></td><td>String</td><td>The password to be used to access the SOCKS5 server</td></tr>
<tr><td><a href="#socksclient-socks5-userpassauthmethod-username"><code>socksClient.socks5.userpassauthmethod.username</code></a></td><td>String</td><td>The username to be used to access the SOCKS5 server</td></tr>
</table>

## SSL/TLS Properties

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socksclient-ssl-enabled"><code>socksClient.ssl.enabled</code></a></td><td>Boolean</td><td>The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled<br/><b>Default Value:</b> <code>false</code></td></tr>
<tr><td><a href="#socksclient-ssl-enabledciphersuites"><code>socksClient.ssl.enabledCipherSuites</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-enabledprotocols"><code>socksClient.ssl.enabledProtocols</code></a></td><td>Comma Separated Values</td><td>The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-keystorefile"><code>socksClient.ssl.keyStoreFile</code></a></td><td>File</td><td>The key store file for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-keystorepassword"><code>socksClient.ssl.keyStorePassword</code></a></td><td>String</td><td>The password for the key store for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-keystoretype"><code>socksClient.ssl.keyStoreType</code></a></td><td>String</td><td>The type of key store for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
<tr><td><a href="#socksclient-ssl-protocol"><code>socksClient.ssl.protocol</code></a></td><td>String</td><td>The protocol version for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>TLSv1.2</code></td></tr>
<tr><td><a href="#socksclient-ssl-truststorefile"><code>socksClient.ssl.trustStoreFile</code></a></td><td>File</td><td>The trust store file for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-truststorepassword"><code>socksClient.ssl.trustStorePassword</code></a></td><td>String</td><td>The password for the trust store for the SSL/TLS connections to the SOCKS server</td></tr>
<tr><td><a href="#socksclient-ssl-truststoretype"><code>socksClient.ssl.trustStoreType</code></a></td><td>String</td><td>The type of trust store for the SSL/TLS connections to the SOCKS server<br/><b>Default Value:</b> <code>PKCS12</code></td></tr>
</table>

## All Properties

<a id="socksclient-dtls-enabled"></a>
### socksClient.dtls.enabled

**Description:** The boolean value to indicate if DTLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-dtls-enabledciphersuites"></a>
### socksClient.dtls.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="socksclient-dtls-enabledprotocols"></a>
### socksClient.dtls.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="socksclient-dtls-protocol"></a>
### socksClient.dtls.protocol

**Description:** The protocol version for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `DTLSv1.2`

<a id="socksclient-dtls-truststorefile"></a>
### socksClient.dtls.trustStoreFile

**Description:** The trust store file for the DTLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="socksclient-dtls-truststorepassword"></a>
### socksClient.dtls.trustStorePassword

**Description:** The password for the trust store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-dtls-truststoretype"></a>
### socksClient.dtls.trustStoreType

**Description:** The type of trust store for the DTLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="socksclient-dtls-wrappedreceivebuffersize"></a>
### socksClient.dtls.wrappedReceiveBufferSize

**Description:** The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socksclient-socks-gssapiauthmethod-mechanismoid"></a>
### socksClient.socks.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the SOCKS server

**Value Type:** [Oid](value-types.md#oid)

**Default Value:** `1.2.840.113554.1.2.2`

<a id="socksclient-socks-gssapiauthmethod-necreferenceimpl"></a>
### socksClient.socks.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the SOCKS server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-socks-gssapiauthmethod-protectionlevels"></a>
### socksClient.socks.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE`

<a id="socksclient-socks-gssapiauthmethod-servicename"></a>
### socksClient.socks.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socks-gssapiauthmethod-suggestedconf"></a>
### socksClient.socks.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `true`

<a id="socksclient-socks-gssapiauthmethod-suggestedinteg"></a>
### socksClient.socks.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

**Default Value:** `0`

<a id="socksclient-socks-methods"></a>
### socksClient.socks.methods

**Description:** The comma separated list of acceptable authentication methods to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

**Default Value:** `NO_AUTHENTICATION_REQUIRED`

<a id="socksclient-socks-socksdatagramsocket-clientinfounavailable"></a>
### socksClient.socks.socksDatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the SOCKS server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-socks-sockshostresolver-resolvefromsocksserver"></a>
### socksClient.socks.socksHostResolver.resolveFromSocksServer

**Description:** The boolean value to indicate if host names are to be resolved from the SOCKS server

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-socks-userpassauthmethod-password"></a>
### socksClient.socks.userpassauthmethod.password

**Description:** The password to be used to access the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socks-userpassauthmethod-username"></a>
### socksClient.socks.userpassauthmethod.username

**Description:** The username to be used to access the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socks5-gssapiauthmethod-mechanismoid"></a>
### socksClient.socks5.gssapiauthmethod.mechanismOid

**Description:** The object ID for the GSS-API authentication mechanism to the SOCKS5 server

**Value Type:** [Oid](value-types.md#oid)

<a id="socksclient-socks5-gssapiauthmethod-necreferenceimpl"></a>
### socksClient.socks5.gssapiauthmethod.necReferenceImpl

**Description:** The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the SOCKS5 server use the NEC reference implementation

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socksclient-socks5-gssapiauthmethod-protectionlevels"></a>
### socksClient.socks5.gssapiauthmethod.protectionLevels

**Description:** The comma separated list of acceptable protection levels after GSS-API authentication with the SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

**Value Type:** [SOCKS5 GSS-API Authentication Method Protection Levels](value-types.md#socks5-gss-api-authentication-method-protection-levels)

<a id="socksclient-socks5-gssapiauthmethod-servicename"></a>
### socksClient.socks5.gssapiauthmethod.serviceName

**Description:** The GSS-API service name for the SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socks5-gssapiauthmethod-suggestedconf"></a>
### socksClient.socks5.gssapiauthmethod.suggestedConf

**Description:** The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socksclient-socks5-gssapiauthmethod-suggestedinteg"></a>
### socksClient.socks5.gssapiauthmethod.suggestedInteg

**Description:** The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

**Value Type:** [Integer](value-types.md#integer)

<a id="socksclient-socks5-methods"></a>
### socksClient.socks5.methods

**Description:** The comma separated list of acceptable authentication methods to the SOCKS5 server

**Value Type:** [SOCKS5 Methods](value-types.md#socks5-methods)

<a id="socksclient-socks5-socks5datagramsocket-clientinfounavailable"></a>
### socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable

**Description:** The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the SOCKS5 server (an address and port of all zeros is sent instead)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socksclient-socks5-socks5hostresolver-resolvefromsocks5server"></a>
### socksClient.socks5.socks5HostResolver.resolveFromSocks5Server

**Description:** The boolean value to indicate if host names are to be resolved from the SOCKS5 server

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socksclient-socks5-userpassauthmethod-password"></a>
### socksClient.socks5.userpassauthmethod.password

**Description:** The password to be used to access the SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socks5-userpassauthmethod-username"></a>
### socksClient.socks5.userpassauthmethod.username

**Description:** The username to be used to access the SOCKS5 server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-socksserversocket-bindhost"></a>
### socksClient.socksServerSocket.bindHost

**Description:** The binding host name or address for the SOCKS server TCP socket

**Value Type:** [Host](value-types.md#host)

**Default Value:** `0.0.0.0`

<a id="socksclient-socksserversocket-bindhostaddresstypes"></a>
### socksClient.socksServerSocket.bindHostAddressTypes

**Description:** The comma separated list of acceptable binding host address types for the SOCKS server TCP socket

**Value Type:** [Host Address Types](value-types.md#host-address-types)

**Default Value:** `HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS`

<a id="socksclient-socksserversocket-bindportranges"></a>
### socksClient.socksServerSocket.bindPortRanges

**Description:** The comma separated list of binding port ranges for the SOCKS server TCP socket

**Value Type:** [Port Ranges](value-types.md#port-ranges)

**Default Value:** `0`

<a id="socksclient-socksserversocket-connecttimeout"></a>
### socksClient.socksServerSocket.connectTimeout

**Description:** The timeout in milliseconds on waiting for the SOCKS server TCP socket to connect (a timeout of 0 is interpreted as an infinite timeout)

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

**Default Value:** `60000`

<a id="socksclient-socksserversocket-netinterface"></a>
### socksClient.socksServerSocket.netInterface

**Description:** The network interface that provides a binding host address for the SOCKS server TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socksclient-socksserversocket-preparesocket"></a>
### socksClient.socksServerSocket.prepareSocket

**Description:** The boolean value to indicate if the SOCKS server TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the SOCKS server host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-socksserversocket-socketsettings"></a>
### socksClient.socksServerSocket.socketSettings

**Description:** The comma separated list of socket settings for the SOCKS server TCP socket

**Value Type:** [Socket Settings](value-types.md#socket-settings)

<a id="socksclient-socksserveruri"></a>
### socksClient.socksServerUri

**Description:** The URI of the SOCKS server

**Value Type:** [SOCKS Server URI](value-types.md#socks-server-uri)

<a id="socksclient-ssl-enabled"></a>
### socksClient.ssl.enabled

**Description:** The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled

**Value Type:** [Boolean](value-types.md#boolean)

**Default Value:** `false`

<a id="socksclient-ssl-enabledciphersuites"></a>
### socksClient.ssl.enabledCipherSuites

**Description:** The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="socksclient-ssl-enabledprotocols"></a>
### socksClient.ssl.enabledProtocols

**Description:** The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

**Value Type:** [Comma Separated Values](value-types.md#comma-separated-values)

<a id="socksclient-ssl-keystorefile"></a>
### socksClient.ssl.keyStoreFile

**Description:** The key store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="socksclient-ssl-keystorepassword"></a>
### socksClient.ssl.keyStorePassword

**Description:** The password for the key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-ssl-keystoretype"></a>
### socksClient.ssl.keyStoreType

**Description:** The type of key store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

<a id="socksclient-ssl-protocol"></a>
### socksClient.ssl.protocol

**Description:** The protocol version for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `TLSv1.2`

<a id="socksclient-ssl-truststorefile"></a>
### socksClient.ssl.trustStoreFile

**Description:** The trust store file for the SSL/TLS connections to the SOCKS server

**Value Type:** [File](value-types.md#file)

<a id="socksclient-ssl-truststorepassword"></a>
### socksClient.ssl.trustStorePassword

**Description:** The password for the trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

<a id="socksclient-ssl-truststoretype"></a>
### socksClient.ssl.trustStoreType

**Description:** The type of trust store for the SSL/TLS connections to the SOCKS server

**Value Type:** [String](value-types.md#string)

**Default Value:** `PKCS12`

