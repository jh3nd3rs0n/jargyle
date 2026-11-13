# Rule Actions

## Contents

-   [General Rule Actions](#general-rule-actions)
-   [SOCKS Rule Actions](#socks-rule-actions)
-   [SOCKS5 Rule Actions](#socks5-rule-actions)
-   [All Rule Actions](#all-rule-actions)

## General Rule Actions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#bindhost"><code>bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all endpoints</td></tr>
<tr><td><a href="#bindhostaddresstype"><code>bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all endpoints (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#bindtcpportrange"><code>bindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all TCP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#bindudpportrange"><code>bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all UDP endpoints (can be specified multiple times with each rule action specifying another port ranges)</td></tr>
<tr><td><a href="#clientendpoints-bindhost"><code>clientEndpoints.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all client endpoints</td></tr>
<tr><td><a href="#clientendpoints-bindhostaddresstype"><code>clientEndpoints.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#clientendpoints-bindudpportrange"><code>clientEndpoints.bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#clientendpoints-netinterface"><code>clientEndpoints.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all client endpoints</td></tr>
<tr><td><a href="#clientendpoints-socketsetting"><code>clientEndpoints.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#clientsocket-socketsetting"><code>clientSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the client TCP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#firewallaction"><code>firewallAction</code></a></td><td>Firewall Action</td><td>Specifies the firewall action to take</td></tr>
<tr><td><a href="#firewallactionallowlimit"><code>firewallActionAllowLimit</code></a></td><td>Non-negative Integer</td><td>Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW</td></tr>
<tr><td><a href="#firewallactionallowlimitreachedlogaction"><code>firewallActionAllowLimitReachedLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached</td></tr>
<tr><td><a href="#firewallactionlogaction"><code>firewallActionLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the firewall action is applied</td></tr>
<tr><td><a href="#netinterface"><code>netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all endpoints</td></tr>
<tr><td><a href="#remoteendpoints-bindhost"><code>remoteEndpoints.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all remote endpoints</td></tr>
<tr><td><a href="#remoteendpoints-bindhostaddresstype"><code>remoteEndpoints.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#remoteendpoints-bindtcpportrange"><code>remoteEndpoints.bindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#remoteendpoints-bindudpportrange"><code>remoteEndpoints.bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#remoteendpoints-netinterface"><code>remoteEndpoints.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all remote endpoints</td></tr>
<tr><td><a href="#remoteendpoints-socketsetting"><code>remoteEndpoints.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#routeselectionlogaction"><code>routeSelectionLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if a route ID is selected</td></tr>
<tr><td><a href="#routeselectionstrategy"><code>routeSelectionStrategy</code></a></td><td>Selection Strategy</td><td>Specifies the selection strategy for the next route ID</td></tr>
<tr><td><a href="#selectablerouteid"><code>selectableRouteId</code></a></td><td>String</td><td>Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)</td></tr>
<tr><td><a href="#socketsetting"><code>socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all endpoints (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
</table>

## SOCKS Rule Actions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks-onbindrequest-relaybuffersize"><code>socks.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayidletimeout"><code>socks.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onbindrequest-relayinboundbandwidthlimit"><code>socks.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onbindrequest-relayoutboundbandwidthlimit"><code>socks.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindhost"><code>socks.onBindRequest.remoteFacingServerSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindhostaddresstype"><code>socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-bindportrange"><code>socks.onBindRequest.remoteFacingServerSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-netinterface"><code>socks.onBindRequest.remoteFacingServerSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks-onbindrequest-remotefacingserversocket-socketsetting"><code>socks.onBindRequest.remoteFacingServerSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onbindrequest-remotesocket-socketsetting"><code>socks.onBindRequest.remoteSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onconnectrequest-relaybuffersize"><code>socks.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayidletimeout"><code>socks.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayinboundbandwidthlimit"><code>socks.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-relayoutboundbandwidthlimit"><code>socks.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindhost"><code>socks.onConnectRequest.remoteSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindhostaddresstype"><code>socks.onConnectRequest.remoteSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-bindportrange"><code>socks.onConnectRequest.remoteSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-connecttimeout"><code>socks.onConnectRequest.remoteSocket.connectTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-netinterface"><code>socks.onConnectRequest.remoteSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote TCP socket</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-preparesocket"><code>socks.onConnectRequest.remoteSocket.prepareSocket</code></a></td><td>Boolean</td><td>Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)</td></tr>
<tr><td><a href="#socks-onconnectrequest-remotesocket-socketsetting"><code>socks.onConnectRequest.remoteSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindhost"><code>socks.onRequest.clientEndpoints.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindhostaddresstype"><code>socks.onRequest.clientEndpoints.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-bindudpportrange"><code>socks.onRequest.clientEndpoints.bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-netinterface"><code>socks.onRequest.clientEndpoints.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all client endpoints</td></tr>
<tr><td><a href="#socks-onrequest-clientendpoints-socketsetting"><code>socks.onRequest.clientEndpoints.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onrequest-relaybuffersize"><code>socks.onRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onrequest-relayidletimeout"><code>socks.onRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onrequest-relayinboundbandwidthlimit"><code>socks.onRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onrequest-relayoutboundbandwidthlimit"><code>socks.onRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindhost"><code>socks.onRequest.remoteEndpoints.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for all remote endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindhostaddresstype"><code>socks.onRequest.remoteEndpoints.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindtcpportrange"><code>socks.onRequest.remoteEndpoints.bindTcpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-bindudpportrange"><code>socks.onRequest.remoteEndpoints.bindUdpPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-netinterface"><code>socks.onRequest.remoteEndpoints.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for all remote endpoints</td></tr>
<tr><td><a href="#socks-onrequest-remoteendpoints-socketsetting"><code>socks.onRequest.remoteEndpoints.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindhost"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstype"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-bindportrange"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-netinterface"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-clientfacingdatagramsocket-socketsetting"><code>socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relaybuffersize"><code>socks.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayidletimeout"><code>socks.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindhost"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstype"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-bindportrange"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-netinterface"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks-onudpassociaterequest-remotefacingdatagramsocket-socketsetting"><code>socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks-request-desireddestinationaddressredirect"><code>socks.request.desiredDestinationAddressRedirect</code></a></td><td>String</td><td>Specifies the desired destination address redirect for the request.</td></tr>
<tr><td><a href="#socks-request-desireddestinationportredirect"><code>socks.request.desiredDestinationPortRedirect</code></a></td><td>Port</td><td>Specifies the desired destination port redirect for the request</td></tr>
<tr><td><a href="#socks-request-desireddestinationredirectlogaction"><code>socks.request.desiredDestinationRedirectLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the desired destination of the request is redirected</td></tr>
</table>

## SOCKS5 Rule Actions

<table>
<tr><th>Name</th><th>Value Type</th><th>Description</th></tr>
<tr><td><a href="#socks5-onbindrequest-relaybuffersize"><code>socks5.onBindRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayidletimeout"><code>socks5.onBindRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayinboundbandwidthlimit"><code>socks5.onBindRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-relayoutboundbandwidthlimit"><code>socks5.onBindRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindhost"><code>socks5.onBindRequest.remoteFacingServerSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindhostaddresstype"><code>socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-bindportrange"><code>socks5.onBindRequest.remoteFacingServerSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-netinterface"><code>socks5.onBindRequest.remoteFacingServerSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotefacingserversocket-socketsetting"><code>socks5.onBindRequest.remoteFacingServerSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onbindrequest-remotesocket-socketsetting"><code>socks5.onBindRequest.remoteSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relaybuffersize"><code>socks5.onConnectRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayidletimeout"><code>socks5.onConnectRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayinboundbandwidthlimit"><code>socks5.onConnectRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-relayoutboundbandwidthlimit"><code>socks5.onConnectRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindhost"><code>socks5.onConnectRequest.remoteSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindhostaddresstype"><code>socks5.onConnectRequest.remoteSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-bindportrange"><code>socks5.onConnectRequest.remoteSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-connecttimeout"><code>socks5.onConnectRequest.remoteSocket.connectTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-netinterface"><code>socks5.onConnectRequest.remoteSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote TCP socket</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-preparesocket"><code>socks5.onConnectRequest.remoteSocket.prepareSocket</code></a></td><td>Boolean</td><td>Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)</td></tr>
<tr><td><a href="#socks5-onconnectrequest-remotesocket-socketsetting"><code>socks5.onConnectRequest.remoteSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhost"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstype"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-bindportrange"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-netinterface"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the client-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-clientfacingdatagramsocket-socketsetting"><code>socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relaybuffersize"><code>socks5.onUdpAssociateRequest.relayBufferSize</code></a></td><td>Positive Integer</td><td>Specifies the buffer size in bytes for relaying the data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayidletimeout"><code>socks5.onUdpAssociateRequest.relayIdleTimeout</code></a></td><td>Positive Integer</td><td>Specifies the timeout in milliseconds on relaying no data</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayinboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayInboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-relayoutboundbandwidthlimit"><code>socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit</code></a></td><td>Positive Integer</td><td>Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhost"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost</code></a></td><td>Host</td><td>Specifies the binding host name or address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstype"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType</code></a></td><td>Host Address Type</td><td>Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-bindportrange"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange</code></a></td><td>Port Range</td><td>Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-netinterface"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface</code></a></td><td>Network Interface</td><td>Specifies the network interface that provides a binding host address for the remote-facing UDP socket</td></tr>
<tr><td><a href="#socks5-onudpassociaterequest-remotefacingdatagramsocket-socketsetting"><code>socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting</code></a></td><td>Socket Setting</td><td>Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)</td></tr>
<tr><td><a href="#socks5-request-desireddestinationaddressredirect"><code>socks5.request.desiredDestinationAddressRedirect</code></a></td><td>SOCKS5 Address</td><td>Specifies the desired destination address redirect for the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationportredirect"><code>socks5.request.desiredDestinationPortRedirect</code></a></td><td>Port</td><td>Specifies the desired destination port redirect for the request</td></tr>
<tr><td><a href="#socks5-request-desireddestinationredirectlogaction"><code>socks5.request.desiredDestinationRedirectLogAction</code></a></td><td>Log Action</td><td>Specifies the logging action to take if the desired destination of the request is redirected</td></tr>
</table>

## All Rule Actions

<a id="bindhost"></a>
### bindHost

**Description:** Specifies the binding host name or address for all endpoints

**Value Type:** [Host](value-types.md#host)

<a id="bindhostaddresstype"></a>
### bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all endpoints (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="bindtcpportrange"></a>
### bindTcpPortRange

**Description:** Specifies a binding port range for all TCP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="bindudpportrange"></a>
### bindUdpPortRange

**Description:** Specifies a binding port range for all UDP endpoints (can be specified multiple times with each rule action specifying another port ranges)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="clientendpoints-bindhost"></a>
### clientEndpoints.bindHost

**Description:** Specifies the binding host name or address for all client endpoints

**Value Type:** [Host](value-types.md#host)

<a id="clientendpoints-bindhostaddresstype"></a>
### clientEndpoints.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="clientendpoints-bindudpportrange"></a>
### clientEndpoints.bindUdpPortRange

**Description:** Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="clientendpoints-netinterface"></a>
### clientEndpoints.netInterface

**Description:** Specifies the network interface that provides a binding host address for all client endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="clientendpoints-socketsetting"></a>
### clientEndpoints.socketSetting

**Description:** Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="clientsocket-socketsetting"></a>
### clientSocket.socketSetting

**Description:** Specifies a socket setting for the client TCP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="firewallaction"></a>
### firewallAction

**Description:** Specifies the firewall action to take

**Value Type:** [Firewall Action](value-types.md#firewall-action)

<a id="firewallactionallowlimit"></a>
### firewallActionAllowLimit

**Description:** Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

**Value Type:** [Non-negative Integer](value-types.md#non-negative-integer)

<a id="firewallactionallowlimitreachedlogaction"></a>
### firewallActionAllowLimitReachedLogAction

**Description:** Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

**Value Type:** [Log Action](value-types.md#log-action)

<a id="firewallactionlogaction"></a>
### firewallActionLogAction

**Description:** Specifies the logging action to take if the firewall action is applied

**Value Type:** [Log Action](value-types.md#log-action)

<a id="netinterface"></a>
### netInterface

**Description:** Specifies the network interface that provides a binding host address for all endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="remoteendpoints-bindhost"></a>
### remoteEndpoints.bindHost

**Description:** Specifies the binding host name or address for all remote endpoints

**Value Type:** [Host](value-types.md#host)

<a id="remoteendpoints-bindhostaddresstype"></a>
### remoteEndpoints.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="remoteendpoints-bindtcpportrange"></a>
### remoteEndpoints.bindTcpPortRange

**Description:** Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="remoteendpoints-bindudpportrange"></a>
### remoteEndpoints.bindUdpPortRange

**Description:** Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="remoteendpoints-netinterface"></a>
### remoteEndpoints.netInterface

**Description:** Specifies the network interface that provides a binding host address for all remote endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="remoteendpoints-socketsetting"></a>
### remoteEndpoints.socketSetting

**Description:** Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="routeselectionlogaction"></a>
### routeSelectionLogAction

**Description:** Specifies the logging action to take if a route ID is selected

**Value Type:** [Log Action](value-types.md#log-action)

<a id="routeselectionstrategy"></a>
### routeSelectionStrategy

**Description:** Specifies the selection strategy for the next route ID

**Value Type:** [Selection Strategy](value-types.md#selection-strategy)

<a id="selectablerouteid"></a>
### selectableRouteId

**Description:** Specifies the ID for a selectable route (can be specified multiple times with each rule action specifying another ID for a selectable route)

**Value Type:** [String](value-types.md#string)

<a id="socketsetting"></a>
### socketSetting

**Description:** Specifies a socket setting for all endpoints (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onbindrequest-relaybuffersize"></a>
### socks.onBindRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayidletimeout"></a>
### socks.onBindRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayinboundbandwidthlimit"></a>
### socks.onBindRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-relayoutboundbandwidthlimit"></a>
### socks.onBindRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onbindrequest-remotefacingserversocket-bindhost"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindHost

**Description:** Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

<a id="socks-onbindrequest-remotefacingserversocket-bindhostaddresstype"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onbindrequest-remotefacingserversocket-bindportrange"></a>
### socks.onBindRequest.remoteFacingServerSocket.bindPortRange

**Description:** Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onbindrequest-remotefacingserversocket-netinterface"></a>
### socks.onBindRequest.remoteFacingServerSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onbindrequest-remotefacingserversocket-socketsetting"></a>
### socks.onBindRequest.remoteFacingServerSocket.socketSetting

**Description:** Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onbindrequest-remotesocket-socketsetting"></a>
### socks.onBindRequest.remoteSocket.socketSetting

**Description:** Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onconnectrequest-relaybuffersize"></a>
### socks.onConnectRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayidletimeout"></a>
### socks.onConnectRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayinboundbandwidthlimit"></a>
### socks.onConnectRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-relayoutboundbandwidthlimit"></a>
### socks.onConnectRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-remotesocket-bindhost"></a>
### socks.onConnectRequest.remoteSocket.bindHost

**Description:** Specifies the binding host name or address for the remote TCP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onconnectrequest-remotesocket-bindhostaddresstype"></a>
### socks.onConnectRequest.remoteSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onconnectrequest-remotesocket-bindportrange"></a>
### socks.onConnectRequest.remoteSocket.bindPortRange

**Description:** Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onconnectrequest-remotesocket-connecttimeout"></a>
### socks.onConnectRequest.remoteSocket.connectTimeout

**Description:** Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onconnectrequest-remotesocket-netinterface"></a>
### socks.onConnectRequest.remoteSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onconnectrequest-remotesocket-preparesocket"></a>
### socks.onConnectRequest.remoteSocket.prepareSocket

**Description:** Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socks-onconnectrequest-remotesocket-socketsetting"></a>
### socks.onConnectRequest.remoteSocket.socketSetting

**Description:** Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onrequest-clientendpoints-bindhost"></a>
### socks.onRequest.clientEndpoints.bindHost

**Description:** Specifies the binding host name or address for all client endpoints

**Value Type:** [Host](value-types.md#host)

<a id="socks-onrequest-clientendpoints-bindhostaddresstype"></a>
### socks.onRequest.clientEndpoints.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all client endpoints (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onrequest-clientendpoints-bindudpportrange"></a>
### socks.onRequest.clientEndpoints.bindUdpPortRange

**Description:** Specifies a binding port range for all client UDP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onrequest-clientendpoints-netinterface"></a>
### socks.onRequest.clientEndpoints.netInterface

**Description:** Specifies the network interface that provides a binding host address for all client endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onrequest-clientendpoints-socketsetting"></a>
### socks.onRequest.clientEndpoints.socketSetting

**Description:** Specifies a socket setting for all client endpoints (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onrequest-relaybuffersize"></a>
### socks.onRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-relayidletimeout"></a>
### socks.onRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-relayinboundbandwidthlimit"></a>
### socks.onRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-relayoutboundbandwidthlimit"></a>
### socks.onRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onrequest-remoteendpoints-bindhost"></a>
### socks.onRequest.remoteEndpoints.bindHost

**Description:** Specifies the binding host name or address for all remote endpoints

**Value Type:** [Host](value-types.md#host)

<a id="socks-onrequest-remoteendpoints-bindhostaddresstype"></a>
### socks.onRequest.remoteEndpoints.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for all remote endpoints (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onrequest-remoteendpoints-bindtcpportrange"></a>
### socks.onRequest.remoteEndpoints.bindTcpPortRange

**Description:** Specifies a binding port range for all remote TCP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onrequest-remoteendpoints-bindudpportrange"></a>
### socks.onRequest.remoteEndpoints.bindUdpPortRange

**Description:** Specifies a binding port range for all remote UDP endpoints (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onrequest-remoteendpoints-netinterface"></a>
### socks.onRequest.remoteEndpoints.netInterface

**Description:** Specifies the network interface that provides a binding host address for all remote endpoints

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onrequest-remoteendpoints-socketsetting"></a>
### socks.onRequest.remoteEndpoints.socketSetting

**Description:** Specifies a socket setting for all remote endpoints (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindhost"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost

**Description:** Specifies the binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstype"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-bindportrange"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange

**Description:** Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-netinterface"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onudpassociaterequest-clientfacingdatagramsocket-socketsetting"></a>
### socks.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting

**Description:** Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-onudpassociaterequest-relaybuffersize"></a>
### socks.onUdpAssociateRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayidletimeout"></a>
### socks.onUdpAssociateRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayinboundbandwidthlimit"></a>
### socks.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-relayoutboundbandwidthlimit"></a>
### socks.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindhost"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost

**Description:** Specifies the binding host name or address for the remote-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstype"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-bindportrange"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange

**Description:** Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-netinterface"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks-onudpassociaterequest-remotefacingdatagramsocket-socketsetting"></a>
### socks.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting

**Description:** Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks-request-desireddestinationaddressredirect"></a>
### socks.request.desiredDestinationAddressRedirect

**Description:** Specifies the desired destination address redirect for the request.

**Value Type:** [String](value-types.md#string)

<a id="socks-request-desireddestinationportredirect"></a>
### socks.request.desiredDestinationPortRedirect

**Description:** Specifies the desired destination port redirect for the request

**Value Type:** [Port](value-types.md#port)

<a id="socks-request-desireddestinationredirectlogaction"></a>
### socks.request.desiredDestinationRedirectLogAction

**Description:** Specifies the logging action to take if the desired destination of the request is redirected

**Value Type:** [Log Action](value-types.md#log-action)

<a id="socks5-onbindrequest-relaybuffersize"></a>
### socks5.onBindRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayidletimeout"></a>
### socks5.onBindRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayinboundbandwidthlimit"></a>
### socks5.onBindRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-relayoutboundbandwidthlimit"></a>
### socks5.onBindRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onbindrequest-remotefacingserversocket-bindhost"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindHost

**Description:** Specifies the binding host name or address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onbindrequest-remotefacingserversocket-bindhostaddresstype"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote-facing TCP server socket if the provided host address is all zeros (can be specified multiple times with each rule specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks5-onbindrequest-remotefacingserversocket-bindportrange"></a>
### socks5.onBindRequest.remoteFacingServerSocket.bindPortRange

**Description:** Specifies a binding port range for the remote-facing TCP server socket if the provided port is zero (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-onbindrequest-remotefacingserversocket-netinterface"></a>
### socks5.onBindRequest.remoteFacingServerSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote-facing TCP server socket if the provided host address is all zeros

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onbindrequest-remotefacingserversocket-socketsetting"></a>
### socks5.onBindRequest.remoteFacingServerSocket.socketSetting

**Description:** Specifies a socket setting for the remote-facing TCP server socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks5-onbindrequest-remotesocket-socketsetting"></a>
### socks5.onBindRequest.remoteSocket.socketSetting

**Description:** Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks5-onconnectrequest-relaybuffersize"></a>
### socks5.onConnectRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayidletimeout"></a>
### socks5.onConnectRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayinboundbandwidthlimit"></a>
### socks5.onConnectRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-relayoutboundbandwidthlimit"></a>
### socks5.onConnectRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-remotesocket-bindhost"></a>
### socks5.onConnectRequest.remoteSocket.bindHost

**Description:** Specifies the binding host name or address for the remote TCP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onconnectrequest-remotesocket-bindhostaddresstype"></a>
### socks5.onConnectRequest.remoteSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote TCP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks5-onconnectrequest-remotesocket-bindportrange"></a>
### socks5.onConnectRequest.remoteSocket.bindPortRange

**Description:** Specifies a binding port range for the remote TCP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-onconnectrequest-remotesocket-connecttimeout"></a>
### socks5.onConnectRequest.remoteSocket.connectTimeout

**Description:** Specifies the timeout in milliseconds on waiting for the remote TCP socket to connect

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onconnectrequest-remotesocket-netinterface"></a>
### socks5.onConnectRequest.remoteSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote TCP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onconnectrequest-remotesocket-preparesocket"></a>
### socks5.onConnectRequest.remoteSocket.prepareSocket

**Description:** Specifies the boolean value to indicate if the remote TCP socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

**Value Type:** [Boolean](value-types.md#boolean)

<a id="socks5-onconnectrequest-remotesocket-socketsetting"></a>
### socks5.onConnectRequest.remoteSocket.socketSetting

**Description:** Specifies a socket setting for the remote TCP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhost"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHost

**Description:** Specifies the binding host name or address for the client-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindhostaddresstype"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the client-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-bindportrange"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.bindPortRange

**Description:** Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-netinterface"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the client-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onudpassociaterequest-clientfacingdatagramsocket-socketsetting"></a>
### socks5.onUdpAssociateRequest.clientFacingDatagramSocket.socketSetting

**Description:** Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks5-onudpassociaterequest-relaybuffersize"></a>
### socks5.onUdpAssociateRequest.relayBufferSize

**Description:** Specifies the buffer size in bytes for relaying the data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayidletimeout"></a>
### socks5.onUdpAssociateRequest.relayIdleTimeout

**Description:** Specifies the timeout in milliseconds on relaying no data

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayinboundbandwidthlimit"></a>
### socks5.onUdpAssociateRequest.relayInboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-relayoutboundbandwidthlimit"></a>
### socks5.onUdpAssociateRequest.relayOutboundBandwidthLimit

**Description:** Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

**Value Type:** [Positive Integer](value-types.md#positive-integer)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhost"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHost

**Description:** Specifies the binding host name or address for the remote-facing UDP socket

**Value Type:** [Host](value-types.md#host)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindhostaddresstype"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindHostAddressType

**Description:** Specifies an acceptable binding host address type for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another host address type)

**Value Type:** [Host Address Type](value-types.md#host-address-type)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-bindportrange"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.bindPortRange

**Description:** Specifies a binding port range for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another port range)

**Value Type:** [Port Range](value-types.md#port-range)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-netinterface"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.netInterface

**Description:** Specifies the network interface that provides a binding host address for the remote-facing UDP socket

**Value Type:** [Network Interface](value-types.md#network-interface)

<a id="socks5-onudpassociaterequest-remotefacingdatagramsocket-socketsetting"></a>
### socks5.onUdpAssociateRequest.remoteFacingDatagramSocket.socketSetting

**Description:** Specifies a socket setting for the remote-facing UDP socket (can be specified multiple times with each rule action specifying another socket setting)

**Value Type:** [Socket Setting](value-types.md#socket-setting)

<a id="socks5-request-desireddestinationaddressredirect"></a>
### socks5.request.desiredDestinationAddressRedirect

**Description:** Specifies the desired destination address redirect for the request

**Value Type:** [SOCKS5 Address](value-types.md#socks5-address)

<a id="socks5-request-desireddestinationportredirect"></a>
### socks5.request.desiredDestinationPortRedirect

**Description:** Specifies the desired destination port redirect for the request

**Value Type:** [Port](value-types.md#port)

<a id="socks5-request-desireddestinationredirectlogaction"></a>
### socks5.request.desiredDestinationRedirectLogAction

**Description:** Specifies the logging action to take if the desired destination of the request is redirected

**Value Type:** [Log Action](value-types.md#log-action)

