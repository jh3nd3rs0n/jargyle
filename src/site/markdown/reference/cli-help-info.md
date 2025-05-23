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
  --enter-chaining-socks5-userpassmethod-pass
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
  --enter-chaining-socks5-userpassmethod-pass
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
        The maximum length of the queue of incoming client connections to the SOCKS server (default value is 50)

    bindHost=HOST
        The default binding host name or address for all sockets (default value is 0.0.0.0)

    bindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all sockets (default value is HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS)

    bindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all TCP sockets (default value is 0)

    bindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all UDP sockets (default value is 0)

    clientSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client socket

    doc=TEXT
        A documentation setting

    externalFacingBindHost=HOST
        The default binding host name or address for all external-facing sockets

    externalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all external-facing sockets

    externalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all external-facing TCP sockets

    externalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all external-facing UDP sockets

    externalFacingNetInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all external-facing sockets

    externalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all external-facing sockets

    internalFacingBindHost=HOST
        The default binding host name or address for all internal-facing sockets

    internalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of default acceptable binding host address types for all internal-facing sockets

    internalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all internal-facing TCP sockets

    internalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all internal-facing UDP sockets

    internalFacingNetInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all internal-facing sockets

    internalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all internal-facing sockets

    lastRouteId=ROUTE_ID
        The ID for the last and unassigned route (default value is lastRoute)

    netInterface=NETWORK_INTERFACE
        The default network interface that provides a binding host address for all sockets

    port=PORT
        The port for the SOCKS server (default value is 1080)

    routeSelectionLogAction=LOG_ACTION
        The logging action to take if a route is selected

    routeSelectionStrategy=SELECTION_STRATEGY
        The selection strategy for the next route (default value is CYCLICAL)

    rule=RULE
        A rule for the SOCKS server (default value is firewallAction=ALLOW)

    socketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all sockets

    socksServerBindHost=HOST
        The binding host name or address for the SOCKS server socket

    socksServerBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the SOCKS server socket

    socksServerBindPortRangesPORT_RANGES
        The comma separated list of binding port ranges for the SOCKS server socket

    socksServerNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the SOCKS server socket

    socksServerSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the SOCKS server socket

  CHAINING GENERAL SETTINGS:

    chaining.clientBindHost=HOST
        The binding host name or address for the client socket that is used to connect to the other SOCKS server (default value is 0.0.0.0)

    chaining.clientBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the client socket that is used to connect to the other SOCKS server (default value is HOST_IPV4_ADDRESS,HOST_IPV6_ADDRESS)

    chaining.clientBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server (default value is 0)

    chaining.clientConnectTimeout=NON_NEGATIVE_INTEGER
        The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (a timeout of 0 is interpreted as an infinite timeout) (default value is 60000)

    chaining.clientNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the client socket that is used to connect to the other SOCKS server

    chaining.clientSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server

    chaining.routeId=ROUTE_ID
        The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

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

  CHAINING SOCKS5 SETTINGS:

    chaining.socks5.gssapimethod.mechanismOid=OID
        The object ID for the GSS-API authentication mechanism to the other SOCKS5 server (default value is 1.2.840.113554.1.2.2)

    chaining.socks5.gssapimethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation (default value is false)

    chaining.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default value is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    chaining.socks5.gssapimethod.serviceName=SERVICE_NAME
        The GSS-API service name for the other SOCKS5 server

    chaining.socks5.gssapimethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is true)

    chaining.socks5.gssapimethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication with the other SOCKS5 server (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is 0)

    chaining.socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods to the other SOCKS5 server (default value is NO_AUTHENTICATION_REQUIRED)

    chaining.socks5.socks5DatagramSocket.clientInfoUnavailable=true|false
        The boolean value to indicate if the client information expected to be used to send UDP datagrams (address and port) is unavailable to be sent to the other SOCKS5 server (an address and port of all zeros is sent instead) (default value is false)

    chaining.socks5.socks5HostResolver.resolveFromSocks5Server=true|false
        The boolean value to indicate if host names are to be resolved from the other SOCKS5 server (default value is false)

    chaining.socks5.userpassmethod.password=PASSWORD
        The password to be used to access the other SOCKS5 server

    chaining.socks5.userpassmethod.username=USERNAME
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

  SOCKS5 SETTINGS:

    socks5.gssapimethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default value is false)

    socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default value is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    socks5.gssapimethod.suggestedConf=true|false
        The suggested privacy (i.e. confidentiality) state for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is true)

    socks5.gssapimethod.suggestedInteg=-2147483648-2147483647
        The suggested quality-of-protection (i.e. integrity) value for GSS-API messages sent after GSS-API authentication (applicable if the negotiated protection level is SELECTIVE_INTEG_OR_CONF) (default value is 0)

    socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods in order of preference (default value is NO_AUTHENTICATION_REQUIRED)

    socks5.onBindRequest.inboundSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the inbound socket

    socks5.onBindRequest.listenBindHost=HOST
        The binding host name or address for the listen socket if the provided host address is all zeros

    socks5.onBindRequest.listenBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the listen socket if the provided host address is all zeros

    socks5.onBindRequest.listenBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the listen socket if the provided port is zero

    socks5.onBindRequest.listenNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the listen socket if the provided host address is all zeros

    socks5.onBindRequest.listenSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the listen socket

    socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onConnectRequest.prepareTargetFacingSocket=true|false
        The boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default value is false)

    socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onConnectRequest.targetFacingBindHost=HOST
        The binding host name or address for the target-facing socket

    socks5.onConnectRequest.targetFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the target-facing socket

    socks5.onConnectRequest.targetFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the target-facing socket

    socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on waiting for the target-facing socket to connect (default value is 60000)

    socks5.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the target-facing socket

    socks5.onConnectRequest.targetFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the target-facing socket

    socks5.onRequest.externalFacingBindHost=HOST
        The binding host name or address for all external-facing sockets

    socks5.onRequest.externalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for all external-facing sockets

    socks5.onRequest.externalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all external-facing TCP sockets

    socks5.onRequest.externalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all external-facing UDP sockets

    socks5.onRequest.externalFacingNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for all external-facing sockets

    socks5.onRequest.externalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all external-facing sockets

    socks5.onRequest.internalFacingBindHost=HOST
        The binding host name or address for all internal-facing sockets

    socks5.onRequest.internalFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for all internal-facing sockets

    socks5.onRequest.internalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all internal-facing UDP sockets

    socks5.onRequest.internalFacingNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for all internal-facing sockets

    socks5.onRequest.internalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all internal-facing sockets

    socks5.onRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data (default value is 1024)

    socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data (default value is 60000)

    socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onUdpAssociateRequest.clientFacingBindHost=HOST
        The binding host name or address for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the client-facing UDP socket

    socks5.onUdpAssociateRequest.clientFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client-facing UDP socket

    socks5.onUdpAssociateRequest.peerFacingBindHost=HOST
        The binding host name or address for the peer-facing UDP socket

    socks5.onUdpAssociateRequest.peerFacingBindHostAddressTypes=HOST_ADDRESS_TYPES
        The comma separated list of acceptable binding host address types for the peer-facing UDP socket

    socks5.onUdpAssociateRequest.peerFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the peer-facing UDP socket

    socks5.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE
        The network interface that provides a binding host address for the peer-facing UDP socket

    socks5.onUdpAssociateRequest.peerFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the peer-facing UDP socket

    socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.userpassmethod.userRepository=SOCKS5_USERPASSMETHOD_USER_REPOSITORY
        The user repository used for username password authentication (default value is StringSourceUserRepository:)

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
          Specifies the binding host name or address for all sockets

      bindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all sockets (can be specified multiple times with each rule action specifying another host address type)

      bindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule action specifying another port range)

      bindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule action specifying another port ranges)

      clientSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the client socket (can be specified multiple times with each rule action specifying another socket setting)

      externalFacingBindHost=HOST
          Specifies the binding host name or address for all external-facing sockets

      externalFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)

      externalFacingBindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)

      externalFacingBindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

      externalFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all external-facing sockets

      externalFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

      firewallAction=FIREWALL_ACTION
          Specifies the firewall action to take

      firewallActionAllowLimit=NON_NEGATIVE_INTEGER
          Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

      firewallActionAllowLimitReachedLogAction=LOG_ACTION
          Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

      firewallActionLogAction=LOG_ACTION
          Specifies the logging action to take if the firewall action is applied

      internalFacingBindHost=HOST
          Specifies the binding host name or address for all internal-facing sockets

      internalFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)

      internalFacingBindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

      internalFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all internal-facing sockets

      internalFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

      netInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all sockets

      routeSelectionLogAction=LOG_ACTION
          Specifies the logging action to take if a route ID is selected

      routeSelectionStrategy=SELECTION_STRATEGY
          Specifies the selection strategy for the next route ID

      selectableRouteId=ROUTE_ID
          Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)

      socketSetting=SOCKET_SETTING
          Specifies a socket setting for all sockets (can be specified multiple times with each rule action specifying another socket setting)

    SOCKS5 RULE ACTIONS:

      socks5.onBindRequest.inboundSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the inbound socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onBindRequest.listenBindHost=HOST
          Specifies the binding host name or address for the listen socket if the provided host address is all zeros

      socks5.onBindRequest.listenBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the listen socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

      socks5.onBindRequest.listenBindPortRange=PORT_RANGE
          Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

      socks5.onBindRequest.listenNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the listen socket if the provided host address is all zeros

      socks5.onBindRequest.listenSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the listen socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onBindRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onBindRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onBindRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onBindRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onConnectRequest.prepareTargetFacingSocket=true|false
          Specifies the boolean value to indicate if the target-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

      socks5.onConnectRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onConnectRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onConnectRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onConnectRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onConnectRequest.targetFacingBindHost=HOST
          Specifies the binding host name or address for the target-facing socket

      socks5.onConnectRequest.targetFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the target-facing socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onConnectRequest.targetFacingBindPortRange=PORT_RANGE
          Specifies a binding port range for the target-facing socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onConnectRequest.targetFacingConnectTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on waiting for the target-facing socket to connect

      socks5.onConnectRequest.targetFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the target-facing socket

      socks5.onConnectRequest.targetFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the target-facing socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onRequest.externalFacingBindHost=HOST
          Specifies the binding host name or address for all external-facing sockets

      socks5.onRequest.externalFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all external-facing sockets (can be specified multiple times with each rule action specifying another host address type)

      socks5.onRequest.externalFacingBindTcpPortRange=PORT_RANGE
          Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule action specifying another port range)

      socks5.onRequest.externalFacingBindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

      socks5.onRequest.externalFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all external-facing sockets

      socks5.onRequest.externalFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onRequest.internalFacingBindHost=HOST
          Specifies the binding host name or address for all internal-facing sockets

      socks5.onRequest.internalFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for all internal-facing sockets (can be specified multiple times with each rule action specifying another host address type)

      socks5.onRequest.internalFacingBindUdpPortRange=PORT_RANGE
          Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule action specifying another port range)

      socks5.onRequest.internalFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for all internal-facing sockets

      socks5.onRequest.internalFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

      socks5.onUdpAssociateRequest.clientFacingBindHost=HOST
          Specifies the binding host name or address for the client-facing UDP socket

      socks5.onUdpAssociateRequest.clientFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onUdpAssociateRequest.clientFacingBindPortRange=PORT_RANGE
          Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onUdpAssociateRequest.clientFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the client-facing UDP socket

      socks5.onUdpAssociateRequest.clientFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onUdpAssociateRequest.peerFacingBindHost=HOST
          Specifies the binding host name or address for the peer-facing UDP socket

      socks5.onUdpAssociateRequest.peerFacingBindHostAddressType=HOST_ADDRESS_TYPE
          Specifies an acceptable binding host address type for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

      socks5.onUdpAssociateRequest.peerFacingBindPortRange=PORT_RANGE
          Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

      socks5.onUdpAssociateRequest.peerFacingNetInterface=NETWORK_INTERFACE
          Specifies the network interface that provides a binding host address for the peer-facing UDP socket

      socks5.onUdpAssociateRequest.peerFacingSocketSetting=SOCKET_SETTING
          Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

      socks5.onUdpAssociateRequest.relayBufferSize=POSITIVE_INTEGER
          Specifies the buffer size in bytes for relaying the data

      socks5.onUdpAssociateRequest.relayIdleTimeout=POSITIVE_INTEGER
          Specifies the timeout in milliseconds on relaying no data

      socks5.onUdpAssociateRequest.relayInboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

      socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit=POSITIVE_INTEGER
          Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

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

  SCHEME: socks5

    socks5
        SOCKS protocol version 5

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

  SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL: NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF|SELECTIVE_INTEG_OR_CONF

    NONE
        No per-message protection

    REQUIRED_INTEG
        Required per-message integrity

    REQUIRED_INTEG_AND_CONF
        Required per-message integrity and confidentiality

    SELECTIVE_INTEG_OR_CONF
        Selective per-message integrity or confidentiality based on local client and server configurations

  SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS: SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL2[...]]

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

  SOCKS5_USERPASSMETHOD_USER_REPOSITORY: TYPE_NAME:INITIALIZATION_STRING

    SOCKS5 USERNAME PASSWORD METHOD USER REPOSITORIES:

      FileSourceUserRepository:FILE
          User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

      StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]
          User repository that handles the storage of the users from a provided string of a comma separated list of URL encoded username and password pairs

  SOCKS_SERVER_URI: SCHEME://[USER_INFO@]HOST[:PORT]

  UNSIGNED_BYTE: 0-255

  USER_INFO: (See https://www.ietf.org/rfc/rfc2396.txt 3.2.2. Server-based Naming Authority)

```

