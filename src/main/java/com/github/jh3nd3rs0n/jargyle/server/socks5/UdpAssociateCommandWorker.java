package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosInetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	private final Socks5Request socks5Request;	
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory =
				context.getClientFacingDtlsDatagramSocketFactory();
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();		
		NetObjectFactory netObjFactory = 
				context.getRoute().getNetObjectFactory();
		Settings sttngs = context.getSettings();
		Socks5Request socks5Req = context.getSocks5Request();
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;		
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;
		this.socks5Request = socks5Req;
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private boolean configurePeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket clientFacingDatagramSock = null;
		try {
			clientFacingDatagramSock = new DatagramSocket(new InetSocketAddress(
					bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in creating the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket peerFacingDatagramSock = null;
		try {
			peerFacingDatagramSock = this.netObjectFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in creating the peer-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		}
		return peerFacingDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer.Builder builder) throws IOException {
		UdpRelayServer udpRelayServer = builder.build();
		try {
			udpRelayServer.start();
			while (!this.clientFacingSocket.isClosed() 
					&& !udpRelayServer.getState().equals(
							UdpRelayServer.State.STOPPED)) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!udpRelayServer.getState().equals(
					UdpRelayServer.State.STOPPED)) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddr = this.desiredDestinationAddress;
		if (AllZerosInetAddressHelper.isAllZerosHostAddress(
				desiredDestinationAddr)) {
			desiredDestinationAddr = 
					this.clientFacingSocket.getInetAddress().getHostAddress();
		}
		int desiredDestinationPrt = this.desiredDestinationPort;
		DatagramSocket peerFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;
		try {
			peerFacingDatagramSock = this.newPeerFacingDatagramSocket();
			if (peerFacingDatagramSock == null) {
				return;
			}
			if (!this.configurePeerFacingDatagramSocket(
					peerFacingDatagramSock)) {
				return;
			}
			clientFacingDatagramSock = this.newClientFacingDatagramSocket();
			if (clientFacingDatagramSock == null) {
				return;
			}
			if (!this.configureClientFacingDatagramSocket(
					clientFacingDatagramSock)) {
				return;
			}
			DatagramSocket clientFacingDatagramSck = 
					this.wrapClientFacingDatagramSocket(
							clientFacingDatagramSock,
							desiredDestinationAddr,
							desiredDestinationPrt); 
			if (clientFacingDatagramSck == null) {
				return;
			}
			clientFacingDatagramSock = clientFacingDatagramSck;
			InetAddress inetAddress = 
					clientFacingDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			if (AllZerosInetAddressHelper.isAllZerosHostAddress(
					serverBoundAddress)) {
				inetAddress = this.clientFacingSocket.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			FirewallRule.Context context = new Socks5ReplyFirewallRule.Context(
					this.clientFacingSocket.getInetAddress().getHostAddress(),
					this.clientFacingSocket.getLocalAddress().getHostAddress(),
					this.methodSubnegotiationResults,
					this.socks5Request,
					socks5Rep); 
			if (!this.commandWorkerContext.canAllowSocks5Reply(
					this, context, LOGGER)) {
				return;
			}			
			if (!this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER)) {
				return;
			}
			UdpRelayServer.Builder builder = new UdpRelayServer.Builder(
					desiredDestinationAddr, 
					desiredDestinationPrt,
					clientFacingDatagramSock, 
					peerFacingDatagramSock);
			builder.bufferSize(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE).intValue());
			builder.hostResolver(this.netObjectFactory.newHostResolver());
			builder.idleTimeout(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT).intValue());
			builder.inboundSocks5UdpFirewallRules(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_INBOUND_SOCKS5_UDP_FIREWALL_RULES));
			builder.methodSubnegotiationResults(this.methodSubnegotiationResults);
			builder.outboundSocks5UdpFirewallRules(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_OUTBOUND_SOCKS5_UDP_FIREWALL_RULES));
			try {
				this.passPackets(builder);
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in starting the UDP association"), 
						e);
			}
		} finally {
			if (clientFacingDatagramSock != null 
					&& !clientFacingDatagramSock.isClosed()) {
				clientFacingDatagramSock.close();
			}
			if (peerFacingDatagramSock != null 
					&& !peerFacingDatagramSock.isClosed()) {
				peerFacingDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock, 
			final String clientHost, 
			final int clientPort) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		if (!AllZerosInetAddressHelper.isAllZerosHostAddress(clientHost) 
				&& clientPort > 0) {
			InetAddress udpClientHostInetAddress = null;
			try {
				udpClientHostInetAddress = InetAddress.getByName(
						clientHost);
			} catch (UnknownHostException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, 
								String.format(
										"Error in resolving the client host %s", 
										clientHost)), 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
						Reply.HOST_UNREACHABLE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			}
			clientFacingDatagramSck.connect(
					udpClientHostInetAddress, clientPort);
		}
		if (clientFacingDatagramSck.isConnected()
				&& this.clientFacingDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						this.clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
								clientFacingDatagramSck, 
								clientHost, 
								clientPort);
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, 
								"Error in wrapping the client-facing UDP socket"), 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			}
		}
		try {
			clientFacingDatagramSck = 
					this.methodSubnegotiationResults.getDatagramSocket(
							clientFacingDatagramSck);
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in wrapping the client-facing UDP socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		}
		return clientFacingDatagramSck;
	}

}
