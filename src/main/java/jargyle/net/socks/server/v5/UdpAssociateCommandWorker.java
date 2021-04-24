package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

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
import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		DtlsDatagramSocketFactory clientDtlsDatagramSockFactory =
				context.getClientDtlsDatagramSocketFactory();
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = context.getNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;		
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
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
	
	private boolean configureServerFacingDatagramSocket(
			final DatagramSocket serverFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(serverFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the server-facing UDP socket"), 
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
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket clientFacingDatagramSock = null;
		try {
			clientFacingDatagramSock = new DatagramSocket(new InetSocketAddress(
					bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, 
							"Error in creating the client-facing UDP socket"), 
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
			return null;
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newServerFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket serverFacingDatagramSock = null;
		try {
			serverFacingDatagramSock = this.netObjectFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.warn( 
					LoggerHelper.objectMessage(
							this, 
							"Error in creating the server-facing UDP socket"), 
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
			return null;
		}
		return serverFacingDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer.ClientSocketAddress clientSocketAddress,
			final UdpRelayServer.DatagramSockets datagramSockets,
			final HostResolver hostResolver,
			final UdpRelayServer.ExternalInboundAddressCriteria externalInboundAddressCriteria,
			final UdpRelayServer.InternalOutboundAddressCriteria internalOutboundAddressCriteria, 
			final UdpRelayServer.RelaySettings relaySettings) throws IOException {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientSocketAddress,
				datagramSockets,
				hostResolver,
				externalInboundAddressCriteria, 
				internalOutboundAddressCriteria,
				relaySettings);
		try {
			udpRelayServer.start();
			while (!this.clientSocket.isClosed() 
					&& !udpRelayServer.isStopped()) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!udpRelayServer.isStopped()) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddr = this.desiredDestinationAddress;
		if (!desiredDestinationAddr.matches("[a-zA-Z1-9]")) {
			desiredDestinationAddr = 
					this.clientSocket.getInetAddress().getHostAddress();
		}
		int desiredDestinationPrt = this.desiredDestinationPort;
		HostResolver hostResolver = this.netObjectFactory.newHostResolver();
		DatagramSocket serverFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;
		try {
			serverFacingDatagramSock = this.newServerFacingDatagramSocket();
			if (serverFacingDatagramSock == null) {
				return;
			}
			if (!this.configureServerFacingDatagramSocket(
					serverFacingDatagramSock)) {
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
							clientFacingDatagramSock,
							desiredDestinationAddr,
							desiredDestinationPrt); 
			if (clientFacingDatagramSck == null) {
				return;
			}
			clientFacingDatagramSock = clientFacingDatagramSck;
			InetAddress inetAddress = 
					clientFacingDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			if (!serverBoundAddress.matches("[a-zA-Z1-9]")) {
				inetAddress = this.clientSocket.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			LOGGER.debug(LoggerHelper.objectMessage(this, String.format(
					"Sending %s",
					socks5Rep.toString())));
			this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			try {
				this.passPackets(
						new UdpRelayServer.ClientSocketAddress(
								desiredDestinationAddr, desiredDestinationPrt),
						new UdpRelayServer.DatagramSockets(
								clientFacingDatagramSock, 
								serverFacingDatagramSock), 
						hostResolver, 
						new UdpRelayServer.ExternalInboundAddressCriteria(
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INBOUND_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INBOUND_ADDRESS_CRITERIA)), 
						new UdpRelayServer.InternalOutboundAddressCriteria(
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_INTERNAL_OUTBOUND_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_INTERNAL_OUTBOUND_ADDRESS_CRITERIA)), 
						new UdpRelayServer.RelaySettings(
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE).intValue(), 
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT).intValue()));
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in starting the UDP association"), 
						e);
			}
		} finally {
			if (clientFacingDatagramSock != null 
					&& !clientFacingDatagramSock.isClosed()) {
				clientFacingDatagramSock.close();
			}
			if (serverFacingDatagramSock != null 
					&& !serverFacingDatagramSock.isClosed()) {
				serverFacingDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock, 
			final String udpClientHost, 
			final int udpClientPort) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		if (this.clientDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						this.clientDtlsDatagramSocketFactory.newDatagramSocket(
								clientFacingDatagramSck, 
								udpClientHost, 
								udpClientPort);
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in wrapping the client-facing UDP "
								+ "socket"), 
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
				return null;
			}
		}
		if (this.clientSocket instanceof GssSocket) {
			GssSocket gssSocket = (GssSocket) this.clientSocket;
			try {
				clientFacingDatagramSck = new GssDatagramSocket(
						clientFacingDatagramSck,
						gssSocket.getGSSContext(),
						gssSocket.getMessageProp());
			} catch (SocketException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in wrapping the client-facing UDP "
								+ "socket"), 
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
				return null;
			}
		}
		return clientFacingDatagramSck;
	}

}
