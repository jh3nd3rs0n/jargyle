package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.Host;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.Settings;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.util.LoggerHelper;

final class ConnectCommandWorker extends TcpBasedCommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ConnectCommandWorker.class);
	
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory externalNetObjectFactory;
	private final Settings settings;
	
	public ConnectCommandWorker(final CommandWorkerContext context) {
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
	
	private boolean configureServerSocket(final Socket serverSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS);
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();		
		try {
			socketSettings.applyTo(serverSocket);
			serverSocket.bind(new InetSocketAddress(bindInetAddress, 0));
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
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
		Socket serverSocket = null;		
		int connectTimeout = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT).intValue();
		try {
			serverSocket = externalNetObjectFactory.newSocket();
			if (!this.configureServerSocket(serverSocket)) {
				return;
			}
			try {
				serverSocket.connect(new InetSocketAddress(
						hostResolver.resolve(this.desiredDestinationAddress),
						this.desiredDestinationPort),
						connectTimeout);
			} catch (UnknownHostException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in connecting the server-facing socket"), 
						e);
				socks5Rep = Socks5Reply.newErrorInstance(Reply.HOST_UNREACHABLE);
				LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
						"Sending %s",
						socks5Rep.toString())));
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
				return;
			}
			String serverBoundAddress = 
					serverSocket.getInetAddress().getHostAddress();
			int serverBoundPort = serverSocket.getPort();
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
						serverSocket, 
						settings.getLastValue(
								SettingSpec.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE).intValue(),
						settings.getLastValue(
								SettingSpec.SOCKS5_ON_CONNECT_RELAY_TIMEOUT).intValue());				
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in starting to pass data"), 
						e);
			}
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		}
	}

}
