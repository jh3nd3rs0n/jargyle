package com.github.jh3nd3rs0n.jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.HostResolver;
import com.github.jh3nd3rs0n.jargyle.net.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Settings;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Reply;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.net.ssl.DtlsDatagramSocketFactory;

final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodEncapsulation methodEncapsulation;
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
		super(context);
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory =
				context.getClientFacingDtlsDatagramSocketFactory();
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodEncapsulation methEncapsulation = 
				context.getMethodEncapsulation();
		NetObjectFactory netObjFactory = context.getNetObjectFactory();
		Settings sttngs = context.getSettings();
		this.clientFacingDtlsDatagramSocketFactory = clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodEncapsulation = methEncapsulation; 
		this.netObjectFactory = netObjFactory;
		this.settings = sttngs;		
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_SOCKET_SETTINGS);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
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
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_BIND_HOST);
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
			final UdpRelayServer.ClientDatagramSocketAddress clientDatagramSocketAddress,
			final UdpRelayServer.DatagramSockets datagramSockets,
			final HostResolver hostResolver,
			final UdpRelayServer.InboundAddressCriteria inboundAddressCriteria,
			final UdpRelayServer.OutboundAddressCriteria outboundAddressCriteria, 
			final UdpRelayServer.RelaySettings relaySettings) throws IOException {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientDatagramSocketAddress,
				datagramSockets,
				hostResolver,
				inboundAddressCriteria, 
				outboundAddressCriteria,
				relaySettings);
		try {
			udpRelayServer.start();
			while (!this.clientFacingSocket.isClosed() 
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
		if (InetAddressHelper.isAllZerosHostAddress(desiredDestinationAddr)) {
			desiredDestinationAddr = 
					this.clientFacingSocket.getInetAddress().getHostAddress();
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
			if (InetAddressHelper.isAllZerosHostAddress(serverBoundAddress)) {
				inetAddress = this.clientFacingSocket.getLocalAddress();
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
						new UdpRelayServer.ClientDatagramSocketAddress(
								desiredDestinationAddr, desiredDestinationPrt),
						new UdpRelayServer.DatagramSockets(
								clientFacingDatagramSock, 
								serverFacingDatagramSock), 
						hostResolver, 
						new UdpRelayServer.InboundAddressCriteria(
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_INBOUND_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_INBOUND_ADDRESS_CRITERIA)), 
						new UdpRelayServer.OutboundAddressCriteria(
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_OUTBOUND_ADDRESS_CRITERIA), 
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_OUTBOUND_ADDRESS_CRITERIA)), 
						new UdpRelayServer.RelaySettings(
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE).intValue(), 
								this.settings.getLastValue(
										Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT).intValue()));
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
		if (!InetAddressHelper.isAllZerosHostAddress(udpClientHost) 
				&& udpClientPort > 0) {
			InetAddress udpClientHostInetAddress = null;
			try {
				udpClientHostInetAddress = InetAddress.getByName(udpClientHost);
			} catch (UnknownHostException e) {
				LOGGER.warn( 
						LoggerHelper.objectMessage(
								this, 
								"Error in determining the IP address for the "
								+ "client-facing UDP socket to connect"), 
						e);
				Socks5Reply socks5Rep = Socks5Reply.newErrorInstance(
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
			clientFacingDatagramSck.connect(
					udpClientHostInetAddress, udpClientPort);
		}
		if (clientFacingDatagramSck.isConnected() 
				&& this.clientFacingDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						this.clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
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
		try {
			clientFacingDatagramSck = 
					this.methodEncapsulation.getDatagramSocket(
							clientFacingDatagramSck);
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
		return clientFacingDatagramSck;
	}

}
