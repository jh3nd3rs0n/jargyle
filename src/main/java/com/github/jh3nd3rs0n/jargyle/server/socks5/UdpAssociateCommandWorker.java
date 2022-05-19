package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressHelper;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private Rule applicableRule;
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	private final Settings settings;
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		Rule applicableRl = context.getApplicableRule();
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory =
				context.getClientFacingDtlsDatagramSocketFactory();
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();		
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		Settings sttngs = context.getSettings();
		this.applicableRule = applicableRl;
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;		
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
		this.settings = sttngs;
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.getClientFacingSocketSettings();
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
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
		SocketSettings socketSettings = this.getPeerFacingSocketSettings();
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
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
	
	private Host getClientFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		return host;
	}
	
	private SocketSettings getClientFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs =
					new ArrayList<SocketSetting<? extends Object>>(
							socketSettings);
			return SocketSettings.newInstance(socketSttngs);
		}
		return this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
	}
	
	private Host getPeerFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
		return host;
	}
	
	private SocketSettings getPeerFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs =
					new ArrayList<SocketSetting<? extends Object>>(
							socketSettings);
			return SocketSettings.newInstance(socketSttngs);
		}
		return this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS);
	}
	
	private int getRelayBufferSize() {
		PositiveInteger relayBufferSize =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		PositiveInteger relayIdleTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host bindHost = this.getClientFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket clientFacingDatagramSock = null;
		try {
			clientFacingDatagramSock = new DatagramSocket(new InetSocketAddress(
					bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
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
		Host bindHost = this.getPeerFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket peerFacingDatagramSock = null;
		try {
			peerFacingDatagramSock = this.netObjectFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
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
		DatagramSocket peerFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;		
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddr = this.desiredDestinationAddress;
		if (AddressHelper.isAllZerosAddress(desiredDestinationAddr)) {
			desiredDestinationAddr = 
					this.clientFacingSocket.getInetAddress().getHostAddress();
		}
		int desiredDestinationPrt = this.desiredDestinationPort;
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
							clientFacingDatagramSock); 
			if (clientFacingDatagramSck == null) {
				return;
			}
			clientFacingDatagramSock = clientFacingDatagramSck;
			InetAddress inetAddress =
					clientFacingDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			RuleContext socks5ReplyRuleContext = 
					this.commandWorkerContext.newSocks5ReplyRuleContext(
							socks5Rep);
			this.applicableRule = this.rules.firstAppliesTo(
					socks5ReplyRuleContext);
			if (!this.commandWorkerContext.canAllowSocks5Reply(
					this, 
					this.applicableRule, 
					socks5ReplyRuleContext, 
					LOGGER)) {
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
			builder.bufferSize(this.getRelayBufferSize());
			builder.hostResolver(this.netObjectFactory.newHostResolver());
			builder.idleTimeout(this.getRelayIdleTimeout());
			builder.ruleContext(socks5ReplyRuleContext);
			builder.rules(this.rules);
			try {
				this.passPackets(builder);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
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
			final DatagramSocket clientFacingDatagramSock) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		if (this.clientFacingDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						this.clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
								clientFacingDatagramSck);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
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
					ObjectLogMessageHelper.objectLogMessage(
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
