package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.util.Criteria;
import jargyle.util.Criterion;
import jargyle.util.logging.LoggerHelper;

final class BindCommandWorker extends TcpBasedCommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			BindCommandWorker.class);
	
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory externalNetObjectFactory;
	private final Settings settings;
		
	public BindCommandWorker(final CommandWorkerContext context) {
		super(context);
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory extNetObjectFactory = 
				context.getExternalNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.externalNetObjectFactory = extNetObjectFactory;
		this.settings = sttngs;
	}
	
	private boolean canAllowExternalIncomingAddress(
			final String externalIncomingAddress) {
		Criteria allowedExternalIncomingAddressCriteria =
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_BIND_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA);
		Criterion criterion = 
				allowedExternalIncomingAddressCriteria.anyEvaluatesTrue(
						externalIncomingAddress);
		if (criterion == null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"External incoming address %s not allowed",
					externalIncomingAddress)));
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
		Criteria blockedExternalIncomingAddressCriteria =
				this.settings.getLastValue(
						SettingSpec.SOCKS5_ON_BIND_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA);
		criterion = blockedExternalIncomingAddressCriteria.anyEvaluatesTrue(
				externalIncomingAddress);
		if (criterion != null) {
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"External incoming address %s blocked based on the "
					+ "following criterion: %s",
					externalIncomingAddress,
					criterion)));
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
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
	private boolean configureExternalIncomingSocket(
			final Socket externalIncomingSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_BIND_EXTERNAL_INCOMING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(externalIncomingSocket);
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the external incoming socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			try {
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
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
		HostResolver hostResolver =	
				this.externalNetObjectFactory.newHostResolver();		
		ServerSocket listenSocket = null;
		Socket externalIncomingSocket = null;
		try {
			listenSocket = this.externalNetObjectFactory.newServerSocket();
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
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
				externalIncomingSocket = listenSocket.accept();
				if (!this.configureExternalIncomingSocket(
						externalIncomingSocket)) {
					return;
				}
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in waiting for an external incoming socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.GENERAL_SOCKS_SERVER_FAILURE);
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Sending %s",
						socks5Rep.toString())));
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
				return;
			} finally {
				listenSocket.close();
			}
			InetAddress externalIncomingInetAddress = 
					externalIncomingSocket.getInetAddress();
			String externalIncomingAddress = 
					externalIncomingInetAddress.getHostAddress();
			if (!this.canAllowExternalIncomingAddress(
					externalIncomingAddress)) {
				return;
			}
			serverBoundAddress = externalIncomingAddress;
			serverBoundPort = externalIncomingSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			try {
				this.passData(
						this.clientSocket,
						externalIncomingSocket, 
						settings.getLastValue(
								SettingSpec.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE).intValue(), 
						settings.getLastValue(
								SettingSpec.SOCKS5_ON_BIND_RELAY_TIMEOUT).intValue());
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in starting to pass data"), 
						e);				
			}
		} finally {
			if (externalIncomingSocket != null 
					&& !externalIncomingSocket.isClosed()) {
				externalIncomingSocket.close();
			}
			if (listenSocket != null && !listenSocket.isClosed()) {
				listenSocket.close();
			}
		}
	}

}
