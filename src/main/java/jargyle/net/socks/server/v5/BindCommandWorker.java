package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.internal.logging.LoggerHelper;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class BindCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			BindCommandWorker.class);
	
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
		
	public BindCommandWorker(final CommandWorkerContext context) {
		super(context);
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = context.getNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;
	}
	
	private boolean canAllowInboundAddress(final String inboundAddress) {
		Criteria allowedInboundAddressCriteria = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_BIND_ALLOWED_INBOUND_ADDRESS_CRITERIA);
		Criterion criterion = allowedInboundAddressCriteria.anyEvaluatesTrue(
				inboundAddress);
		if (criterion == null) {
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
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		Criteria blockedInboundAddressCriteria = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_BIND_BLOCKED_INBOUND_ADDRESS_CRITERIA);
		criterion = blockedInboundAddressCriteria.anyEvaluatesTrue(
				inboundAddress);
		if (criterion != null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Inbound address %s blocked based on the "
					+ "following criterion: %s",
					inboundAddress,
					criterion)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(
						socks5Rep.toByteArray());
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e);
			}
			return false;
		}
		return true;
	}
	
	private boolean configureListenSocket(final ServerSocket listenSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(listenSocket);
		} catch (SocketException e) {
			LOGGER.warn( 
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
				LOGGER.warn( 
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
				LOGGER.warn( 
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
				LOGGER.warn( 
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
						this.clientSocket,
						inboundSocket, 
						this.settings.getLastValue(
								SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE).intValue(), 
						this.settings.getLastValue(
								SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT).intValue());
			} catch (IOException e) {
				LOGGER.warn( 
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
