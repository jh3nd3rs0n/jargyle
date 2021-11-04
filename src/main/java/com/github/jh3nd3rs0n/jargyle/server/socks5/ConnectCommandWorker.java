package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class ConnectCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ConnectCommandWorker.class);
	
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	private final Socks5Request socks5Request;
	
	public ConnectCommandWorker(final CommandWorkerContext context) {
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
	
	private boolean configureServerFacingSocket(
			final Socket serverFacingSocket) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS);
		Host bindHost = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();		
		try {
			socketSettings.applyTo(serverFacingSocket);
			serverFacingSocket.bind(new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
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
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		} catch (IOException e) {
			LOGGER.error( 
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
				LOGGER.error( 
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
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT).intValue();		
		if (this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET)) {
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
					LOGGER.error( 
							LoggerHelper.objectMessage(
									this, "Error in writing SOCKS5 reply"), 
							e1);					
				}
				return null;
			}
		} else {
			Host bindHost = this.settings.getLastValue(
					Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST);
			InetAddress bindInetAddress = bindHost.toInetAddress();
			try {
				serverFacingSocket = this.netObjectFactory.newSocket(
						this.desiredDestinationAddress, 
						this.desiredDestinationPort, 
						bindInetAddress, 
						0);
			} catch (UnknownHostException e) {
				LOGGER.error( 
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
					LOGGER.error( 
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
				TcpBasedCommandWorkerHelper.passData(
						this.clientFacingSocket,
						serverFacingSocket, 
						this.settings.getLastValue(
								Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE).intValue(),
						this.settings.getLastValue(
								Socks5SettingSpecConstants.SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT).intValue());				
			} catch (IOException e) {
				LOGGER.error( 
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
