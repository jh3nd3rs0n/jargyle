package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.RelayServer;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class ConnectCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ConnectCommandWorker.class);

	private Rule applicableRule;
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	private final Settings settings;
	
	public ConnectCommandWorker(final CommandWorkerContext context) {
		super(context);
		Rule applicableRl = context.getApplicableRule();
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		Settings sttngs = context.getSettings();
		this.applicableRule = applicableRl;
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
		this.settings = sttngs;	
	}
	
	private boolean canPrepareServerFacingSocket() {
		Boolean b = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		if (b != null) {
			return b.booleanValue();
		}
		b = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET);
		return b.booleanValue();
	}

	private boolean configureServerFacingSocket(
			final Socket serverFacingSocket) {
		SocketSettings socketSettings = this.getServerFacingSocketSettings();
		Host bindHost = this.getServerFacingBindHost();
		InetAddress bindInetAddress = bindHost.toInetAddress();		
		try {
			socketSettings.applyTo(serverFacingSocket);
			serverFacingSocket.bind(new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in binding the server-facing socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private int getRelayBufferSize() {
		PositiveInteger relayBufferSize =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		PositiveInteger relayIdleTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private Host getServerFacingBindHost() {
		Host host = this.applicableRule.getLastRuleResultValue(
				Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		return host;
	}
	
	private int getServerFacingConnectTimeout() {
		PositiveInteger connectTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		if (connectTimeout != null) {
			return connectTimeout.intValue();
		}
		connectTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT);
		return connectTimeout.intValue();
	}
	
	private SocketSettings getServerFacingSocketSettings() {
		List<SocketSetting<Object>> socketSettings =
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs =
					new ArrayList<SocketSetting<? extends Object>>(
							socketSettings);
			return SocketSettings.newInstance(socketSttngs);
		}
		return this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS);
	}
	
	private Socket newServerFacingSocket() {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();		
		Socket serverFacingSocket = null;		
		int connectTimeout = this.getServerFacingConnectTimeout();		
		if (this.canPrepareServerFacingSocket()) {
			serverFacingSocket = netObjectFactory.newSocket();
			if (!this.configureServerFacingSocket(serverFacingSocket)) {
				return null;
			}
			try {
				serverFacingSocket.connect(new InetSocketAddress(
						hostResolver.resolve(this.desiredDestinationAddress),
						this.desiredDestinationPort),
						connectTimeout);
			} catch (UnknownHostException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.HOST_UNREACHABLE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			} catch (SocketException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.NETWORK_UNREACHABLE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;				
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			}
		} else {
			Host bindHost = this.getServerFacingBindHost();
			InetAddress bindInetAddress = bindHost.toInetAddress();
			try {
				serverFacingSocket = this.netObjectFactory.newSocket(
						this.desiredDestinationAddress, 
						this.desiredDestinationPort, 
						bindInetAddress, 
						0);
			} catch (UnknownHostException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.HOST_UNREACHABLE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			} catch (SocketException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.NETWORK_UNREACHABLE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;				
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newFailureInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				this.commandWorkerContext.sendSocks5Reply(
						this, socks5Rep, LOGGER);
				return null;
			}				
		}
		return serverFacingSocket;
	}
	
	@Override
	public void run() throws IOException {
		Socket serverFacingSocket = null;
		Socks5Reply socks5Rep = null;
		try {
			serverFacingSocket = this.newServerFacingSocket();
			if (serverFacingSocket == null) {
				return;
			}
			String serverBoundAddress = 
					serverFacingSocket.getInetAddress().getHostAddress();
			int serverBoundPort = serverFacingSocket.getPort();
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
			RelayServer.Builder builder = new RelayServer.Builder(
					this.clientFacingSocket, serverFacingSocket);
			builder.bufferSize(this.getRelayBufferSize());
			builder.idleTimeout(this.getRelayIdleTimeout());
			try {
				TcpBasedCommandWorkerHelper.passData(builder);				
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting to pass data"), 
						e);
			}
		} finally {
			if (serverFacingSocket != null && !serverFacingSocket.isClosed()) {
				serverFacingSocket.close();
			}
		}
	}

}
