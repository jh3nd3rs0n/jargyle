package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.logging.LoggerHelper;
import jargyle.net.Host;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;

final class ConnectCommandWorker extends PassDataCommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ConnectCommandWorker.class);
	
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	
	public ConnectCommandWorker(final CommandWorkerContext context) {
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
	
	private boolean configureServerFacingSocket(
			final Socket serverFacingSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS);
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();		
		try {
			socketSettings.applyTo(serverFacingSocket);
			serverFacingSocket.bind(new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, "Error in setting the server-facing socket"), 
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
		} catch (IOException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, "Error in binding the server-facing socket"), 
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

	private Socket newServerFacingSocket() throws IOException {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();		
		Socket serverFacingSocket = null;		
		int connectTimeout = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT).intValue();		
		if (this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET)) {
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
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.HOST_UNREACHABLE);
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
				return null;
			}
		} else {
			Host bindHost = this.settings.getLastValue(
					SettingSpec.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			try {
				serverFacingSocket = this.netObjectFactory.newSocket(
						this.desiredDestinationAddress, 
						this.desiredDestinationPort, 
						bindInetAddress, 
						0);
			} catch (UnknownHostException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in creating the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(
						Reply.HOST_UNREACHABLE);
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
				return null;
			}				
		}
		return serverFacingSocket;
	}
	
	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		Socket serverFacingSocket = null;		
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
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s", 
					socks5Rep.toString())));
			this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			try {
				this.passData(
						this.clientSocket,
						serverFacingSocket, 
						this.settings.getLastValue(
								SettingSpec.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE).intValue(),
						this.settings.getLastValue(
								SettingSpec.SOCKS5_ON_CONNECT_RELAY_TIMEOUT).intValue());				
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
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
