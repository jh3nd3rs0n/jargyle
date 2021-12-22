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
import com.github.jh3nd3rs0n.jargyle.common.net.RelayServer;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRuleNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
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
		NetObjectFactory netObjFactory = 
				context.getRoute().getNetObjectFactory();
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
	
	private Socket acceptInboundSocketFrom(final ServerSocket listenSocket) {
		Socks5Reply socks5Rep = null;
		Socket inboundSocket = null;
		try {
			inboundSocket = listenSocket.accept();
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in waiting for an inbound socket"), 
					e);
			socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
			return null;
		}
		return inboundSocket;
	}
	
	private boolean bindListenSocket(final ServerSocket listenSocket) {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();		
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
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
			return false;
		}
		return true;
	}
	
	private boolean canAllowSecondSocks5Reply(final Rule.Context context) {
		Socks5ReplyFirewallRules socks5ReplyFirewallRules = 
				this.settings.getLastValue(
						Socks5SettingSpecConstants.SOCKS5_ON_BIND_SECOND_SOCKS5_REPLY_FIREWALL_RULES);
		Socks5ReplyFirewallRule socks5ReplyFirewallRule = null;
		try {
			socks5ReplyFirewallRule = 
					socks5ReplyFirewallRules.anyAppliesBasedOn(context);
		} catch (FirewallRuleNotFoundException e) {
			LOGGER.error(
					LoggerHelper.objectMessage(this, String.format(
							"Firewall rule not found for the following "
							+ "context: %s",
							context)),
					e);
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.commandWorkerContext.sendSocks5Reply(this, rep);
			return false;			
		}
		try {
			socks5ReplyFirewallRule.applyBasedOn(context);
		} catch (FirewallRuleActionDenyException e) {
			Socks5Reply rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.commandWorkerContext.sendSocks5Reply(this, rep);
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
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
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
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
			return false;
		}
		return true;
	}
	
	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		ServerSocket listenSocket = null;
		Socket inboundSocket = null;
		try {
			listenSocket = this.netObjectFactory.newServerSocket();
			if (!this.configureListenSocket(listenSocket)) {
				return;
			}
			if (!this.bindListenSocket(listenSocket)) {
				return;
			}
			InetAddress inetAddress = listenSocket.getInetAddress();
			String serverBoundAddress =	inetAddress.getHostAddress();
			int serverBoundPort = listenSocket.getLocalPort();
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
			if (!this.commandWorkerContext.canAllowSocks5Reply(this, context)) {
				return;
			}
			if (!this.commandWorkerContext.sendSocks5Reply(this, socks5Rep)) {
				return;
			}
			inboundSocket = this.acceptInboundSocketFrom(listenSocket);
			listenSocket.close();
			if (inboundSocket == null) {
				return;
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
			context = new Socks5ReplyFirewallRule.Context(
					this.clientFacingSocket.getInetAddress().getHostAddress(),
					this.clientFacingSocket.getLocalAddress().getHostAddress(),
					this.methodSubnegotiationResults, 
					this.socks5Request, 
					socks5Rep);
			if (!this.canAllowSecondSocks5Reply(context)) {
				return;
			}			
			if (!this.commandWorkerContext.sendSocks5Reply(this, socks5Rep)) {
				return;
			}
			RelayServer.Builder builder = new RelayServer.Builder(
					this.clientFacingSocket, inboundSocket);
			builder.bufferSize(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE).intValue());
			builder.idleTimeout(this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT).intValue());
			try {
				TcpBasedCommandWorkerHelper.passData(builder);
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
