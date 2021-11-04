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
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class BindCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			BindCommandWorker.class);
	
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	private final Socks5Request socks5Request;
		
	public BindCommandWorker(final CommandWorkerContext context) {
		super(context);
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();
		NetObjectFactory netObjFactory = context.getNetObjectFactory();
		Settings sttngs = context.getSettings();
		Socks5Request socks5Req = context.getSocks5Request();
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;
		this.socks5Request = socks5Req;
	}
	
	private boolean canAllowSecondSocks5Reply(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final Socks5Reply socks5Rep) {
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";		
		Socks5ReplyRules socks5ReplyRules = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_SECOND_SOCKS5_REPLY_RULES);
		Socks5ReplyRule socks5ReplyRule = null;
		try {
			socks5ReplyRule = socks5ReplyRules.anyAppliesTo(
					clientAddress, 
					methSubnegotiationResults, 
					socks5Req,
					socks5Rep);
		} catch (IllegalArgumentException e) {
			LOGGER.error(
					LoggerHelper.objectMessage(this, String.format(
							"Error regarding SOCKS5 reply to %s%s. "
							+ "SOCKS5 request: %s. SOCKS5 reply: %s",
							clientAddress,
							possibleUser,
							socks5Req,
							socks5Rep)),
					e);
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					rep.toString())));			
			try {
				this.commandWorkerContext.writeThenFlush(
						rep.toByteArray());
			} catch (IOException ex) {
				LOGGER.error(
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;			
		}
		try {
			socks5ReplyRule.applyTo(
					clientAddress, 
					methSubnegotiationResults, 
					socks5Req,
					socks5Rep);
		} catch (RuleActionDenyException e) {
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					rep.toString())));			
			try {
				this.commandWorkerContext.writeThenFlush(rep.toByteArray());
			} catch (IOException ex) {
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
			if (!this.canAllow(
					this.clientFacingSocket.getInetAddress().getHostAddress(), 
					this.methodSubnegotiationResults, 
					this.socks5Request, 
					socks5Rep)) {
				return;
			}
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
			serverBoundAddress = 
					inboundSocket.getInetAddress().getHostAddress();
			serverBoundPort = inboundSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			if (!this.canAllowSecondSocks5Reply(
					this.clientFacingSocket.getInetAddress().getHostAddress(), 
					this.methodSubnegotiationResults, 
					this.socks5Request, 
					socks5Rep)) {
				return;
			}			
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
