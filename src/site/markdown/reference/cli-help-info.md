# Command Line Interface Help Information

## Contents

-   [Help Information](#help-information)
    -   [Help Information for manage-socks5-users](#help-information-for-manage-socks5-users)
    -   [Help Information for new-server-config-file](#help-information-for-new-server-config-file)
    -   [Help Information for start-server](#help-information-for-start-server)
    -   [Settings Help Information](#settings-help-information)

## Help Information

```text
Usage: jargyle COMMAND
       jargyle --help
       jargyle --version

COMMANDS:
  manage-socks5-users USER_REPOSITORY COMMAND
      Manage SOCKS5 users
  new-server-config-file [OPTIONS] FILE
      Create a new server configuration file based on the provided options
  start-server [OPTIONS] [MONITORED_CONFIG_FILE]
      Start the SOCKS server

OPTIONS:
  --help, -h
      Print this help and exit
  --version, -V
      Print version information and exit

```

### Help Information for manage-socks5-users

```text
Usage: jargyle manage-socks5-users USER_REPOSITORY COMMAND
       jargyle manage-socks5-users --help

USER_REPOSITORIES:
  FileSourceUserRepository:FILE
      User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

COMMANDS:
  add
      Add user(s) through an interactive prompt
  list
      List users to standard output
  remove NAME
      Remove user by name

OPTIONS:
  --help, -h
      Print this help and exit

```

### Help Information for new-server-config-file

```text
Usage: jargyle new-server-config-file [OPTIONS] FILE
       jargyle new-server-config-file --help
       jargyle new-server-config-file --settings-help

OPTIONS:
  --config-file=FILE, -f FILE
      A configuration file
  --enter-chaining-dtls-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
  --enter-chaining-socks-userpassauthmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS server
  --enter-chaining-socks5-userpassauthmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS5 server
  --enter-chaining-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
  --enter-chaining-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
  --enter-dtls-key-store-pass
      Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
  --enter-partial-encryption-pass
      Enter through an interactive prompt the partial password to be used for encryption/decryption
  --enter-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
  --enter-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
  --help, -h
      Print this help and exit
  --partial-encryption-pass=PASSWORD
      The partial password to be used for encryption/decryption
  --partial-encryption-pass-file=FILE
      The file of the partial password to be used for encryption/decryption
  --setting=NAME=VALUE, -s NAME=VALUE
      A setting for the SOCKS server
  --settings-help, -H
      Print the list of available settings for the SOCKS server and exit

```

### Help Information for start-server

```text
Usage: jargyle start-server [OPTIONS] [MONITORED_CONFIG_FILE]
       jargyle start-server --help
       jargyle start-server --settings-help

OPTIONS:
  --config-file=FILE, -f FILE
      A configuration file
  --enter-chaining-dtls-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
  --enter-chaining-socks-userpassauthmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS server
  --enter-chaining-socks5-userpassauthmethod-pass
      Enter through an interactive prompt the password to be used to access the other SOCKS5 server
  --enter-chaining-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
  --enter-chaining-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
  --enter-dtls-key-store-pass
      Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
  --enter-partial-encryption-pass
      Enter through an interactive prompt the partial password to be used for encryption/decryption
  --enter-ssl-key-store-pass
      Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
  --enter-ssl-trust-store-pass
      Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
  --help, -h
      Print this help and exit
  --partial-encryption-pass=PASSWORD
      The partial password to be used for encryption/decryption
  --partial-encryption-pass-file=FILE
      The file of the partial password to be used for encryption/decryption
  --setting=NAME=VALUE, -s NAME=VALUE
      A setting for the SOCKS server
  --settings-help, -H
      Print the list of available settings for the SOCKS server and exit

```

### Settings Help Information

```text
SETTINGS:

  GENERAL SETTINGS:

    backlog=NON_NEGATIVE_INTEGER
        The maximum length of the queue of incoming client TCP connections to the SOCKS server (default value is 50)

    bindHost=HOST
        The default binding host name or address for all endpoints (default value is 0.0.0.0)

    bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all endpoints (default value is HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS)

    bindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all TCP endpoints (default value is 0)

    bindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all UDP endpoints (default value is 0)

    clientEndpoints.bindHost=HOST
        The default binding host name or address for all client endpoints

    clientEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all client endpoints

    clientEndpoints.bindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all client TCP endpoints

    clientEndpoints.bindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all client UDP endpoints

    clientEndpoints.netInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all client endpoints

    clientEndpoints.socketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all client endpoints

    clientFacingServerSocket.bindHost=HOST
        The binding host name or address for the client-facing TCP server socket

    clientFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the client-facing TCP server socket

    clientFacingServerSocket.bindPortRangesPORT_RANGES
        The comma separated list of binding port ranges for the client-facing TCP server socket

    clientFacingServerSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the client-facing TCP server socket

    clientFacingServerSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client-facing TCP server socket

    clientSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client TCP socket

    doc=TEXT
        A documentation setting

    lastRouteId=ROUTE_ID
        The ID for the last and unassigned route (default value is lastRoute)

    netInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all endpoints

    port=PORT
        The port for the SOCKS server (or specifically the port for the client-facing TCP server socket) (default value is 1080)

    remoteEndpoints.bindHost=HOST
        The default binding host name or address for all remote endpoints

    remoteEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all remote endpoints

    remoteEndpoints.bindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all remote TCP endpoints

    remoteEndpoints.bindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all remote UDP endpoints

    remoteEndpoints.netInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all remote endpoints

    remoteEndpoints.socketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all remote endpoints

    routeSelectionLogAction=LOG_ACTION
        The logging action to take if a route is selected

    routeSelectionStrategy=SELECTION_STRATEGY
        The selection strategy for the next route (default value is CYCLICAL)

    rule=RULE
        A rule for the SOCKS server (default value is firewallAction=ALLOW)

    socketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all endpoints

  CHAINING GENERAL SETTINGS:

    chaining.routeId=ROUTE_ID
        The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

    chaining.socksServerSocket.bindHost=HOST
        The binding host name or address for the other SOCKS server TCP socket (default value is 0.0.0.0)

    chaining.socksServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the other SOCKS server TCP socket (default value is HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS)

    chaining.socksServerSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the other SOCKS server TCP socket (default value is 0)

    chaining.socksServerSocket.connectTimeout=NON_NEGATIVE_INTEGER
        The timeout in milliseconds on waiting for the other SOCKS server TCP socket to connect (a timeout of 0 is interpreted as an infinite timeout) (default value is 60000)

    chaining.socksServerSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the other SOCKS server TCP socket

    chaining.socksServerSocket.prepareSocket=true|false
        The boolean value to indicate if the other SOCKS server TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the other SOCKS server host name, and setting the specified timeout on waiting to connect) (default value is false)

    chaining.socksServerSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the other SOCKS server TCP socket

    chaining.socksServerUri=SOCKS_SERVER_URI
        The URI of the other SOCKS server

  CHAINING DTLS SETTINGS:

    chaining.dtls.enabled=true|false
        The boolean value to indicate if DTLS connections to the other SOCKS server are enabled (default value is false)

    chaining.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server

    chaining.dtls.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server

    chaining.dtls.protocol=PROTOCOL
        The protocol version for the DTLS connections to the other SOCKS server (default value is DTLSv1.2)

    chaining.dtls.trustStoreFile=FILE
        The trust store file for the DTLS connections to the other SOCKS server

    chaining.dtls.trustStorePassword=PASSWORD
        The password for the trust store for the DTLS connections to the other SOCKS server

    chaining.dtls.trustStoreType=TYPE
        The type of trust store for the DTLS connections to the other SOCKS server (default value is PKCS12)

    chaining.dtls.wrappedReceiveBufferSize=POSITIVE_INTEGER
        The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the other SOCKS server

  CHAINING SOCKS SETTINGS:

    chaining.socks.gssapiauthmethod.mechanismOid=OID
        The object ID for the GSS-API authentication mechanism to the other SOCKS server (default value is 1.2.840.113554.1.2.2)

    chaining.socks.gssapiauthmethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS server use the NEC reference implementation (default value is false)

    chaining.socks.gssapiauthmethod.protectionLevels=SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default value is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    chaining.socks.gssapiauthmethod.serviceName=SERVICE_NAME
        The GSS-API service name for the other SOCKS server

    chaining.socks.gssapiauthmethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is true)

    chaining.socks.gssapiauthmethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is 0)

    chaining.socks.methods=SOCKS_METHODS
        The comma separated list of acceptable authentication methods to the other SOCKS server (default value is NO_AUTHENTICATION_REQUIRED)

    chaining.socks.socksDatagramSocket.clientInfoUnavailable=true|false
        The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS server (an address and port of all zeros is sent instead) (default value is false)

    chaining.socks.socksHostResolver.resolveFromSocksServer=true|false
        The boolean value to indicate if host names are to be resolved from the other SOCKS server (default value is false)

    chaining.socks.userpassauthmethod.password=PASSWORD
        The password to be used to access the other SOCKS server

    chaining.socks.userpassauthmethod.username=USERNAME
        The username to be used to access the other SOCKS server

  CHAINING SOCKS5 SETTINGS:

    chaining.socks5.gssapiauthmethod.mechanismOid=OID
        The object ID for the GSS-API authentication mechanism to the other SOCKS5 server

    chaining.socks5.gssapiauthmethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation

    chaining.socks5.gssapiauthmethod.protectionLevels=SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.)

    chaining.socks5.gssapiauthmethod.serviceName=SERVICE_NAME
        The GSS-API service name for the other SOCKS5 server

    chaining.socks5.gssapiauthmethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

    chaining.socks5.gssapiauthmethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

    chaining.socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods to the other SOCKS5 server

    chaining.socks5.socks5DatagramSocket.clientInfoUnavailable=true|false
        The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS5 server (an address and port of all zeros is sent instead)

    chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true|false
        The boolean value to indicate if host names are to be resolved from the other SOCKS5 server

    chaining.socks5.userpassauthmethod.password=PASSWORD
        The password to be used to access the other SOCKS5 server

    chaining.socks5.userpassauthmethod.username=USERNAME
        The username to be used to access the other SOCKS5 server

  CHAINING SSL/TLS SETTINGS:

    chaining.ssl.enabled=true|false
        The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled (default value is false)

    chaining.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server

    chaining.ssl.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server

    chaining.ssl.keyStoreFile=FILE
        The key store file for the SSL/TLS connections to the other SOCKS server

    chaining.ssl.keyStorePassword=PASSWORD
        The password for the key store for the SSL/TLS connections to the other SOCKS server

    chaining.ssl.keyStoreType=TYPE
        The type of key store for the SSL/TLS connections to the other SOCKS server (default value is PKCS12)

    chaining.ssl.protocol=PROTOCOL
        The protocol version for the SSL/TLS connections to the other SOCKS server (default value is TLSv1.2)

    chaining.ssl.trustStoreFile=FILE
        The trust store file for the SSL/TLS connections to the other SOCKS server

    chaining.ssl.trustStorePassword=PASSWORD
        The password for the trust store for the SSL/TLS connections to the other SOCKS server

    chaining.ssl.trustStoreType=TYPE
        The type of trust store for the SSL/TLS connections to the other SOCKS server (default value is PKCS12)

  DTLS SETTINGS:

    dtls.enabled=true|false
        The boolean value to indicate if DTLS connections to the SOCKS server are enabled (default value is false)

    dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

    dtls.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

    dtls.keyStoreFile=FILE
        The key store file for the DTLS connections to the SOCKS server

    dtls.keyStorePassword=PASSWORD
        The password for the key store for the DTLS connections to the SOCKS server

    dtls.keyStoreType=TYPE
        The type of key store for the DTLS connections to the SOCKS server (default value is PKCS12)

    dtls.protocol=PROTOCOL
        The protocol version for the DTLS connections to the SOCKS server (default value is DTLSv1.2)

    dtls.wrappedReceiveBufferSize=POSITIVE_INTEGER
        The buffer size for receiving DTLS wrapped datagrams for the DTLS connections to the SOCKS server

  SOCKS SETTINGS:

    socks.gssapiauthmethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default value is false)

    socks.gssapiauthmethod.protectionLevels=SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default value is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    socks.gssapiauthmethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is true)

    socks.gssapiauthmethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is 0)

    socks.methods=SOCKS_METHODS
        The comma separated list of acceptable authentication methods in order of preference (default value is NO_AUTHENTICATION_REQUIRED)

    socks.onBindRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks.onBindRequest.remoteFacingServerSocket.bindHost=HOST
        The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

    socks.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros

    socks.onBindRequest.remoteFacingServerSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero

    socks.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

    socks.onBindRequest.remoteFacingServerSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote-facing TCP server socket

    socks.onBindRequest.remoteSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote TCP socket

    socks.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks.onConnectRequest.remoteSocket.bindHost=HOST
        The binding host name or address for the remote TCP socket

    socks.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote TCP socket

    socks.onConnectRequest.remoteSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote TCP socket

    socks.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on waiting for the remote TCP socket to connect (default value is 60000)

    socks.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote TCP socket

    socks.onConnectRequest.remoteSocket.prepareSocket=true|false
        The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default value is false)

    socks.onConnectRequest.remoteSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote TCP socket

    socks.onRequest.clientEndpoints.bindHost=HOST
        The binding host name or address for all client endpoints

    socks.onRequest.clientEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for all client endpoints

    socks.onRequest.clientEndpoints.bindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all client UDP endpoints

    socks.onRequest.clientEndpoints.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for all client endpoints

    socks.onRequest.clientEndpoints.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all client endpoints

    socks.onRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data (default value is 1024)

    socks.onRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data (default value is 60000)

    socks.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks.onRequest.remoteEndpoints.bindHost=HOST
        The binding host name or address for all remote endpoints

    socks.onRequest.remoteEndpoints.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for all remote endpoints

    socks.onRequest.remoteEndpoints.bindTcpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all remote TCP endpoints

    socks.onRequest.remoteEndpoints.bindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all remote UDP endpoints

    socks.onRequest.remoteEndpoints.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for all remote endpoints

    socks.onRequest.remoteEndpoints.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all remote endpoints

    socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST
        The binding host name or address for the client-facing UDP socket

    socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the client-facing UDP socket

    socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client-facing UDP socket

    socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the client-facing UDP socket

    socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client-facing UDP socket

    socks.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST
        The binding host name or address for the remote-facing UDP socket

    socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote-facing UDP socket

    socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote-facing UDP socket

    socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote-facing UDP socket

    socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote-facing UDP socket

    socks.userpassauthmethod.userRepository=SOCKS_USERPASSAUTHMETHOD_USER_REPOSITORY
        The user repository used for username password authentication (default value is StringSourceUserRepository:)

  SOCKS5 SETTINGS:

    socks5.gssapiauthmethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation

    socks5.gssapiauthmethod.protectionLevels=SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.)

    socks5.gssapiauthmethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

    socks5.gssapiauthmethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF)

    socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods in order of preference

    socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onBindRequest.remoteFacingServerSocket.bindHost=HOST
        The binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

    socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote-facing TCP server socket if the provided host address is all zeros

    socks5.onBindRequest.remoteFacingServerSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote-facing TCP server socket if the provided port is zero

    socks5.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

    socks5.onBindRequest.remoteFacingServerSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote-facing TCP server socket

    socks5.onBindRequest.remoteSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote TCP socket

    socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onConnectRequest.remoteSocket.bindHost=HOST
        The binding host name or address for the remote TCP socket

    socks5.onConnectRequest.remoteSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote TCP socket

    socks5.onConnectRequest.remoteSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote TCP socket

    socks5.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on waiting for the remote TCP socket to connect

    socks5.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote TCP socket

    socks5.onConnectRequest.remoteSocket.prepareSocket=true|false
        The boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

    socks5.onConnectRequest.remoteSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote TCP socket

    socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST
        The binding host name or address for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client-facing UDP socket

    socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST
        The binding host name or address for the remote-facing UDP socket

    socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the remote-facing UDP socket

    socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the remote-facing UDP socket

    socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the remote-facing UDP socket

    socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the remote-facing UDP socket

    socks5.userpassauthmethod.userRepository=SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY
        The user repository used for username password authentication

  SSL/TLS SETTINGS:

    ssl.enabled=true|false
        The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled (default value is false)

    ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

    ssl.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

    ssl.keyStoreFile=FILE
        The key store file for the SSL/TLS connections to the SOCKS server

    ssl.keyStorePassword=PASSWORD
        The password for the key store for the SSL/TLS connections to the SOCKS server

    ssl.keyStoreType=TYPE
        The type of key store for the SSL/TLS connections to the SOCKS server (default value is PKCS12)

    ssl.needClientAuth=true|false
        The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server (default value is false)

    ssl.protocol=PROTOCOL
        The protocol version for the SSL/TLS connections to the SOCKS server (default value is TLSv1.2)

    ssl.trustStoreFile=FILE
        The trust store file for the SSL/TLS connections to the SOCKS server

    ssl.trustStorePassword=PASSWORD
        The password for the trust store for the SSL/TLS connections to the SOCKS server

    ssl.trustStoreType=TYPE
        The type of trust store for the SSL/TLS connections to the SOCKS server (default value is PKCS12)

    ssl.wantClientAuth=true|false
        The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server (default value is false)

VALUE TYPES:

  ADDRESS_RANGE: ADDRESS|IP_ADDRESS1-IP_ADDRESS2|regex:REGULAR_EXPRESSION

  COMMA_SEPARATED_VALUES: [VALUE1[,VALUE2[...]]]

  FIREWALL_ACTION: ALLOW|DENY

    ALLOW

    DENY

  HOST: HOST_NAME|HOST_ADDRESS

  HOST_ADDRESS_TYPE: HOST_IPV4_ADDRESS|HOST_IPV6_ADDRESS

    HOST_IPV4_ADDRESS
        Host IPv4 address

    HOST_IPV6_ADDRESS
        Host IPv6 address

  HOST_ADDRESS_TYPES: [HOST_ADDRESS_TYPE1[,HOST_ADDRESS_TYPE2[...]]]

  LOG_ACTION: LOG_AS_WARNING|LOG_AS_INFO

    LOG_AS_WARNING
        Log message as a warning message

    LOG_AS_INFO
        Log message as an informational message

  NETWORK_INTERFACE: NETWORK_INTERFACE_NAME

  NON_NEGATIVE_INTEGER: 0-2147483647

  PORT: 0-65535

  PORT_RANGE: PORT|PORT1-PORT2

  PORT_RANGES: [PORT_RANGE1[,PORT_RANGE2[...]]]

  POSITIVE_INTEGER: 1-2147483647

  RULE: [RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_ACTION1[,RULE_ACTION2[...]]

  RULE_ACTION: NAME=VALUE

    GENERAL RULE ACTIONS:

      bindHost=HOST
          Specifies the binding host name or address for all endpoints

      bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all endpoints (can be specified multiple times with each rule action specifying another host address type)

      bindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all TCP endpoints (can be specified multiple times with each rule action specifying another port range)

      bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all UDP endpoints (can be specified multiple times with each rule action specifying another port ranges)

      clientEndpoints.bindHost=HOST
          Specifies the binding host name or address for all client endpoints

      clientEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)

      clientEndpoints.bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)

      clientEndpoints.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all client endpoints

      clientEndpoints.socketSetting=SOCKET_SETTING
          Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)

      clientSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the client TCP socket (can be specified multiple times with each rule action specifying another socket setting)

      firewallAction=FIREWALL_ACTION
          Specifies the firewall action to take

      firewallActionAllowLimit=NON_NEGATIVE_INTEGER
          Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

      firewallActionAllowLimitReachedLogAction=LOG_ACTION
          Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

      firewallActionLogAction=LOG_ACTION
          Specifies the logging action to take if the firewall action is applied

      netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all endpoints

      remoteEndpoints.bindHost=HOST
          Specifies the binding host name or address for all remote endpoints

      remoteEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)

      remoteEndpoints.bindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)

      remoteEndpoints.bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)

      remoteEndpoints.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all remote endpoints

      remoteEndpoints.socketSetting=SOCKET_SETTING
          Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)

      routeSelectionLogAction=LOG_ACTION
          Specifies the logging action to take if a route ID is selected

      routeSelectionStrategy=SELECTION_STRATEGY
          Specifies the selection strategy for the next route ID

      selectableRouteId=ROUTE_ID
          Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)

      socketSetting=SOCKET_SETTING
          Specifies a socket setting for all endpoints (can be specified multiple times with each rule action specifying another socket setting)

    SOCKS RULE ACTIONS:

      socks.onBindRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks.onBindRequest.remoteFacingServerSocket.bindHost=HOST
          Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

      socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

      socks.onBindRequest.remoteFacingServerSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

      socks.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

      socks.onBindRequest.remoteFacingServerSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)

      socks.onBindRequest.remoteSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks.onConnectRequest.remoteSocket.bindHost=HOST
          Specifies the binding host name or address for the remote TCP socket

      socks.onConnectRequest.remoteSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)

      socks.onConnectRequest.remoteSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)

      socks.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect

      socks.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote TCP socket

      socks.onConnectRequest.remoteSocket.prepareSocket=true|false
          Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

      socks.onConnectRequest.remoteSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks.onRequest.clientEndpoints.bindHost=HOST
          Specifies the binding host name or address for all client endpoints

      socks.onRequest.clientEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)

      socks.onRequest.clientEndpoints.bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)

      socks.onRequest.clientEndpoints.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all client endpoints

      socks.onRequest.clientEndpoints.socketSetting=SOCKET_SETTING
          Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)

      socks.onRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks.onRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks.onRequest.remoteEndpoints.bindHost=HOST
          Specifies the binding host name or address for all remote endpoints

      socks.onRequest.remoteEndpoints.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)

      socks.onRequest.remoteEndpoints.bindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)

      socks.onRequest.remoteEndpoints.bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)

      socks.onRequest.remoteEndpoints.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all remote endpoints

      socks.onRequest.remoteEndpoints.socketSetting=SOCKET_SETTING
          Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)

      socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST
          Specifies the binding host name or address for the client-facing UDP socket

      socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the client-facing UDP socket

      socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST
          Specifies the binding host name or address for the remote-facing UDP socket

      socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote-facing UDP socket

      socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks.request.desiredDestinationAddressRedirect=ADDRESS
          Specifies the desired destination address redirect for the request.

      socks.request.desiredDestinationPortRedirect=PORT
          Specifies the desired destination port redirect for the request

      socks.request.desiredDestinationRedirectLogAction=LOG_ACTION
          Specifies the logging action to take if the desired destination of the request is redirected

    SOCKS5 RULE ACTIONS:

      socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onBindRequest.remoteFacingServerSocket.bindHost=HOST
          Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

      socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

      socks5.onBindRequest.remoteFacingServerSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

      socks5.onBindRequest.remoteFacingServerSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

      socks5.onBindRequest.remoteFacingServerSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onBindRequest.remoteSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onConnectRequest.remoteSocket.bindHost=HOST
          Specifies the binding host name or address for the remote TCP socket

      socks5.onConnectRequest.remoteSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onConnectRequest.remoteSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onConnectRequest.remoteSocket.connectTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect

      socks5.onConnectRequest.remoteSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote TCP socket

      socks5.onConnectRequest.remoteSocket.prepareSocket=true|false
          Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

      socks5.onConnectRequest.remoteSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost=HOST
          Specifies the binding host name or address for the client-facing UDP socket

      socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the client-facing UDP socket

      socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost=HOST
          Specifies the binding host name or address for the remote-facing UDP socket

      socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange=PORT_RANGE
          Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the remote-facing UDP socket

      socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting=SOCKET_SETTING
          Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.request.desiredDestinationAddressRedirect=SOCKS5_ADDRESS
          Specifies the desired destination address redirect for the request

      socks5.request.desiredDestinationPortRedirect=PORT
          Specifies the desired destination port redirect for the request

      socks5.request.desiredDestinationRedirectLogAction=LOG_ACTION
          Specifies the logging action to take if the desired destination of the request is redirected

  RULE_CONDITION: NAME=VALUE

    GENERAL RULE CONDITIONS:

      clientAddress=ADDRESS_RANGE
          Specifies the client address

      socksServerAddress=ADDRESS_RANGE
          Specifies the SOCKS server address the client connected to

    SOCKS RULE CONDITIONS:

      socks.method=SOCKS_METHOD
          Specifies the negotiated method.

      socks.reply.serverBoundAddress=ADDRESS_RANGE
          Specifies the server bound address of the reply

      socks.reply.serverBoundPort=PORT_RANGE
          Specifies the server bound port of the reply

      socks.request.command=SOCKS_REQUEST_COMMAND
          Specifies the command of the request

      socks.request.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the desired destination address of the request

      socks.request.desiredDestinationPort=PORT_RANGE
          Specifies the desired destination port of the request

      socks.secondReply.serverBoundAddress=ADDRESS_RANGE
          Specifies the server bound address of the second reply (for the BIND request)

      socks.secondReply.serverBoundPort=PORT_RANGE
          Specifies the server bound port of the second reply (for the BIND request)

      socks.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the UDP inbound desired destination address

      socks.udp.inbound.desiredDestinationPort=PORT_RANGE
          Specifies the UDP inbound desired destination port

      socks.udp.inbound.sourceAddress=ADDRESS_RANGE
          Specifies the UDP inbound source address

      socks.udp.inbound.sourcePort=PORT_RANGE
          Specifies the UDP inbound source port

      socks.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the UDP outbound desired destination address

      socks.udp.outbound.desiredDestinationPort=PORT_RANGE
          Specifies the UDP outbound desired destination port

      socks.udp.outbound.sourceAddress=ADDRESS_RANGE
          Specifies the UDP outbound source address

      socks.udp.outbound.sourcePort=PORT_RANGE
          Specifies the UDP outbound source port

      socks.user=USER
          Specifies the user if any after the negotiated method

    SOCKS5 RULE CONDITIONS:

      socks5.method=SOCKS5_METHOD
          Specifies the negotiated method

      socks5.reply.serverBoundAddress=ADDRESS_RANGE
          Specifies the server bound address of the reply

      socks5.reply.serverBoundPort=PORT_RANGE
          Specifies the server bound port of the reply

      socks5.request.command=SOCKS5_REQUEST_COMMAND
          Specifies the command of the request

      socks5.request.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the desired destination address of the request

      socks5.request.desiredDestinationPort=PORT_RANGE
          Specifies the desired destination port of the request

      socks5.secondReply.serverBoundAddress=ADDRESS_RANGE
          Specifies the server bound address of the second reply (for the BIND request)

      socks5.secondReply.serverBoundPort=PORT_RANGE
          Specifies the server bound port of the second reply (for the BIND request)

      socks5.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the UDP inbound desired destination address

      socks5.udp.inbound.desiredDestinationPort=PORT_RANGE
          Specifies the UDP inbound desired destination port

      socks5.udp.inbound.sourceAddress=ADDRESS_RANGE
          Specifies the UDP inbound source address

      socks5.udp.inbound.sourcePort=PORT_RANGE
          Specifies the UDP inbound source port

      socks5.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE
          Specifies the UDP outbound desired destination address

      socks5.udp.outbound.desiredDestinationPort=PORT_RANGE
          Specifies the UDP outbound desired destination port

      socks5.udp.outbound.sourceAddress=ADDRESS_RANGE
          Specifies the UDP outbound source address

      socks5.udp.outbound.sourcePort=PORT_RANGE
          Specifies the UDP outbound source port

      socks5.user=USER
          Specifies the user if any after the negotiated method

  SELECTION_STRATEGY: CYCLICAL|RANDOM

    SELECTION STRATEGIES:

      CYCLICAL
          Select the next in the cycle

      RANDOM
          Select the next at random

  SOCKET_SETTING: NAME=VALUE

    STANDARD SOCKET SETTINGS:

      IP_TOS=UNSIGNED_BYTE
          The type-of-service or traffic class field in the IP header for a TCP or UDP socket

      SO_BROADCAST=true|false
          Can send broadcast datagrams

      SO_KEEPALIVE=true|false
          Keeps a TCP socket alive when no data has been exchanged in either direction

      SO_LINGER=-2147483648-2147483647
          Linger on closing the TCP socket in seconds (disabled if the number of seconds is negative)

      SO_RCVBUF=POSITIVE_INTEGER
          The receive buffer size

      SO_REUSEADDR=true|false
          Can reuse socket address and port

      SO_SNDBUF=POSITIVE_INTEGER
          The send buffer size

      SO_TIMEOUT=NON_NEGATIVE_INTEGER
          The timeout in milliseconds on waiting for an idle socket

      TCP_NODELAY=true|false
          Disables Nagle's algorithm

  SOCKET_SETTINGS: [SOCKET_SETTING1[,SOCKET_SETTING2[...]]]

  SOCKS5_ADDRESS: DOMAIN_NAME|IPV4_ADDRESS|IPV6_ADDRESS

  SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL: NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF|SELECTIVE_INTEG_OR_CONF

    NONE
        No per-message protection

    REQUIRED_INTEG
        Required per-message integrity

    REQUIRED_INTEG_AND_CONF
        Required per-message integrity and confidentiality

    SELECTIVE_INTEG_OR_CONF
        Selective per-message integrity or confidentiality based on local client and server configurations

  SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS: SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVEL2[...]]

  SOCKS5_METHOD: NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD

    NO_AUTHENTICATION_REQUIRED
        No authentication required

    GSSAPI
        GSS-API authentication

    USERNAME_PASSWORD
        Username password authentication

  SOCKS5_METHODS: [SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]

  SOCKS5_REQUEST_COMMAND: CONNECT|BIND|UDP_ASSOCIATE|RESOLVE

    CONNECT
        A request to the SOCKS server to connect to another server

    BIND
        A request to the SOCKS server to bind to another address and port in order to receive an inbound connection

    UDP_ASSOCIATE
        A request to the SOCKS server to establish an association within the UDP relay process to handle UDP datagrams

    RESOLVE
        A request to the SOCKS server to resolve a host name

  SOCKS5_USERPASSAUTHMETHOD_USER_REPOSITORY: TYPE_NAME:INITIALIZATION_STRING

    SOCKS5 USERNAME PASSWORD AUTHENTICATION METHOD USER REPOSITORIES:

      FileSourceUserRepository:FILE
          User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

      StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]
          User repository that handles the storage of the users from a provided string of a comma separated list of URL encoded username and password pairs

  SOCKS_SERVER_URI: SOCKS_SERVER_URI_SCHEME://[USER_INFO@]HOST[:PORT]

  SOCKS_SERVER_URI_SCHEME: socks5

    socks5
        SOCKS protocol version 5

  UNSIGNED_BYTE: 0-255

  USER_INFO: (See https://www.ietf.org/rfc/rfc2396.txt 3.2.2. Server-based Naming Authority)

```

