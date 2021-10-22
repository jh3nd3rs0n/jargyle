package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class BindCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			BindCommandWorker.class);
	
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
		
	public BindCommandWorker(final CommandWorkerContext context) {
		super(context);
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = context.getNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;
	}
	
	private boolean canAllowInboundAddress(final String inboundAddress) {
		Rules inboundAddressRules = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_INBOUND_ADDRESS_RULES);
		Rule inboundAddressRule = inboundAddressRules.anyAppliesTo(inboundAddress);
		if (inboundAddressRule == null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Inbound address %s not allowed",
					inboundAddress)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		if (inboundAddressRule.getRuleAction().equals(RuleAction.BLOCK)) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Inbound address %s blocked based on the following rule: %s",
					inboundAddress,
					inboundAddressRule)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureInboundSocket(final Socket inboundSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_INBOUND_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(inboundSocket);
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in setting the inbound socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureListenSocket(final ServerSocket listenSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(listenSocket);
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in setting the listen socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}

	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();		
		ServerSocket listenSocket = null;
		Socket inboundSocket = null;
		try {
			listenSocket = this.netObjectFactory.newServerSocket();
			if (!this.configureListenSocket(listenSocket)) {
				return;
			}
			try {
				listenSocket.bind(new InetSocketAddress(
						hostResolver.resolve(this.desiredDestinationAddress),
						this.desiredDestinationPort));
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in binding the listen socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Sending %s",
						socks5Rep.toString())));
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
				return;
			}
			InetAddress inetAddress = listenSocket.getInetAddress();
			String serverBoundAddress =	inetAddress.getHostAddress();
			int serverBoundPort = listenSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			try {
				inboundSocket = listenSocket.accept();
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, 
								"Error in waiting for an inbound "
								+ "socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Sending %s",
						socks5Rep.toString())));
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
				return;
			} finally {
				listenSocket.close();
			}
			if (!this.configureInboundSocket(inboundSocket)) {
				return;
			}
			InetAddress inboundInetAddress = inboundSocket.getInetAddress();
			String inboundAddress = inboundInetAddress.getHostAddress();
			if (!this.canAllowInboundAddress(inboundAddress)) {
				return;
			}
			serverBoundAddress = inboundAddress;
			serverBoundPort = inboundSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			try {
				TcpBasedCommandWorkerHelper.passData(
						this.clientFacingSocket,
						inboundSocket, 
						this.settings.getLastValue(
								Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE).intValue(), 
						this.settings.getLastValue(
								Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT).intValue());
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in starting to pass data"), 
						e);				
			}
		} finally {
			if (inboundSocket != null && !inboundSocket.isClosed()) {
				inboundSocket.close();
			}
			if (listenSocket != null && !listenSocket.isClosed()) {
				listenSocket.close();
			}
		}
	}

}
