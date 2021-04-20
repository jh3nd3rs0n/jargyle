package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

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
import jargyle.net.socks.transport.v5.gssapiauth.GssDatagramSocket;
import jargyle.net.socks.transport.v5.gssapiauth.GssSocket;
import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.util.logging.LoggerHelper;

final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private final DtlsDatagramSocketFactory clientDtlsDatagramSocketFactory;
	private final Socket clientSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory externalNetObjectFactory;
	private final Settings settings;
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		DtlsDatagramSocketFactory clientDtlsDatagramSockFactory =
				context.getClientDtlsDatagramSocketFactory();
		Socket clientSock = context.getClientSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory extNetObjectFactory = 
				context.getExternalNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientDtlsDatagramSocketFactory = clientDtlsDatagramSockFactory;
		this.clientSocket = clientSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.externalNetObjectFactory = extNetObjectFactory;
		this.settings = sttngs;		
	}
	
	private boolean configureClientDatagramSocket(
			final DatagramSocket clientDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(clientDatagramSock);
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				return false;
			}
			return false;
		}
		return true;
	}
	
	private boolean configureServerDatagramSocket(
			final DatagramSocket serverDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(serverDatagramSock);
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
	
	private DatagramSocket newClientDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket clientDatagramSock = null;
		try {
			clientDatagramSock = new DatagramSocket(new InetSocketAddress(
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return null;
		}
		return clientDatagramSock;
	}
	
	private DatagramSocket newServerDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_SERVER_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket serverDatagramSock = null;
		try {
			serverDatagramSock = this.externalNetObjectFactory.newDatagramSocket(
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
				this.commandWorkerContext.writeThenFlush(socks5Rep.toByteArray());
			} catch (IOException e1) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return null;
		}
		return serverDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer.ClientSocketAddress clientSocketAddress,
			final UdpRelayServer.DatagramSockets datagramSockets,
			final HostResolver hostResolver,
			final UdpRelayServer.ExternalIncomingAddressCriteria externalIncomingAddressCriteria,
			final UdpRelayServer.ExternalOutgoingAddressCriteria externalOutgoingAddressCriteria, 
			final UdpRelayServer.RelaySettings relaySettings) throws IOException {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientSocketAddress,
				datagramSockets,
				hostResolver,
				externalIncomingAddressCriteria, 
				externalOutgoingAddressCriteria,
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
		HostResolver hostResolver = 
				this.externalNetObjectFactory.newHostResolver();
		DatagramSocket serverDatagramSock = null;
		DatagramSocket clientDatagramSock = null;
		try {
			serverDatagramSock = this.newServerDatagramSocket();
			if (serverDatagramSock == null) {
				return;
			}
			if (!this.configureServerDatagramSocket(serverDatagramSock)) {
				return;
			}
			clientDatagramSock = this.newClientDatagramSocket();
			if (clientDatagramSock == null) {
				return;
			}
			if (!this.configureClientDatagramSocket(clientDatagramSock)) {
				return;
			}
			DatagramSocket clientDatagramSck = this.wrapClientDatagramSocket(
					clientDatagramSock, 
					desiredDestinationAddr, 
					desiredDestinationPrt); 
			if (clientDatagramSck == null) {
				return;
			}
			clientDatagramSock = clientDatagramSck;
			InetAddress inetAddress = clientDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			if (!serverBoundAddress.matches("[a-zA-Z1-9]")) {
				inetAddress = this.clientSocket.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			int serverBoundPort = clientDatagramSock.getLocalPort();
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
								clientDatagramSock, 
								serverDatagramSock), 
						hostResolver, 
						new UdpRelayServer.ExternalIncomingAddressCriteria(
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA)), 
						new UdpRelayServer.ExternalOutgoingAddressCriteria(
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_OUTGOING_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										SettingSpec.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_OUTGOING_ADDRESS_CRITERIA)), 
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
			if (clientDatagramSock != null && !clientDatagramSock.isClosed()) {
				clientDatagramSock.close();
			}
			if (serverDatagramSock != null && !serverDatagramSock.isClosed()) {
				serverDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientDatagramSocket(
			final DatagramSocket clientDatagramSock, 
			final String udpClientHost, 
			final int udpClientPort) {
		DatagramSocket clientDatagramSck = clientDatagramSock;
		if (this.clientDtlsDatagramSocketFactory != null) {
			try {
				clientDatagramSck = 
						this.clientDtlsDatagramSocketFactory.newDatagramSocket(
								clientDatagramSck, 
								udpClientHost, 
								udpClientPort);
			} catch (IOException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in wrapping the client-facing UDP socket"), 
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
				return null;
			}
		}
		if (this.clientSocket instanceof GssSocket) {
			GssSocket gssSocket = (GssSocket) this.clientSocket;
			try {
				clientDatagramSck = new GssDatagramSocket(
						clientDatagramSck,
						gssSocket.getGSSContext(),
						gssSocket.getMessageProp());
			} catch (SocketException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in wrapping the client-facing UDP socket"), 
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
				return null;
			}
		}
		return clientDatagramSck;
	}

}
