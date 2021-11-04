package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosInetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class UdpAssociateCommandWorker extends CommandWorker {

	private static final int HALF_SECOND = 500;

	private static final Logger LOGGER = LoggerFactory.getLogger(
			UdpAssociateCommandWorker.class);
	
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final NetObjectFactory netObjectFactory;
	private final Settings settings;
	private final Socks5Request socks5Request;	
	
	public UdpAssociateCommandWorker(final CommandWorkerContext context) {
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
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
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
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return false;
		}
		return true;
	}
	
	private boolean configurePeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		SocketSettings socketSettings = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_SOCKET_SETTINGS);
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
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
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket clientFacingDatagramSock = null;
		try {
			clientFacingDatagramSock = new DatagramSocket(new InetSocketAddress(
					bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
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
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in writing SOCKS5 reply"), 
						e1);
			}
			return null;
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket() {
		Host bindHost = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_PEER_FACING_BIND_HOST);
		InetAddress bindInetAddress = bindHost.toInetAddress();
		DatagramSocket peerFacingDatagramSock = null;
		try {
			peerFacingDatagramSock = this.netObjectFactory.newDatagramSocket(
					new InetSocketAddress(bindInetAddress, 0));
		} catch (SocketException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, 
							"Error in creating the peer-facing UDP socket"), 
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
			return null;
		}
		return peerFacingDatagramSock;
	}

	private void passPackets(
			final String clientAddr,
			final int clientPrt,
			final DatagramSocket clientFacingDatagramSock,
			final DatagramSocket peerFacingDatagramSock,
			final CommandWorkerContext context) throws IOException {
		UdpRelayServer udpRelayServer = new UdpRelayServer(
				clientAddr,
				clientPrt,
				clientFacingDatagramSock,
				peerFacingDatagramSock,
				context);
		try {
			udpRelayServer.start();
			while (!this.clientFacingSocket.isClosed() 
					&& !udpRelayServer.getState().equals(
							UdpRelayServer.State.STOPPED)) {
				try {
					Thread.sleep(HALF_SECOND);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} finally {
			if (!udpRelayServer.getState().equals(
					UdpRelayServer.State.STOPPED)) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		String desiredDestinationAddr = this.desiredDestinationAddress;
		if (AllZerosInetAddressHelper.isAllZerosHostAddress(
				desiredDestinationAddr)) {
			desiredDestinationAddr = 
					this.clientFacingSocket.getInetAddress().getHostAddress();
		}
		int desiredDestinationPrt = this.desiredDestinationPort;
		DatagramSocket peerFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;
		try {
			peerFacingDatagramSock = this.newPeerFacingDatagramSocket();
			if (peerFacingDatagramSock == null) {
				return;
			}
			if (!this.configurePeerFacingDatagramSocket(
					peerFacingDatagramSock)) {
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
			if (AllZerosInetAddressHelper.isAllZerosHostAddress(
					serverBoundAddress)) {
				inetAddress = this.clientFacingSocket.getLocalAddress();
				serverBoundAddress = inetAddress.getHostAddress();
			}
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
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
				this.passPackets(
						desiredDestinationAddr, 
						desiredDestinationPrt,
						clientFacingDatagramSock, 
						peerFacingDatagramSock, 
						this.commandWorkerContext);
			} catch (IOException e) {
				LOGGER.error( 
						LoggerHelper.objectMessage(
								this, "Error in starting the UDP association"), 
						e);
			}
		} finally {
			if (clientFacingDatagramSock != null 
					&& !clientFacingDatagramSock.isClosed()) {
				clientFacingDatagramSock.close();
			}
			if (peerFacingDatagramSock != null 
					&& !peerFacingDatagramSock.isClosed()) {
				peerFacingDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock, 
			final String clientHost, 
			final int clientPort) {
		DatagramSocket clientFacingDatagramSck = null;
		try {
			clientFacingDatagramSck = 
					this.commandWorkerContext.wrapClientFacingDatagramSocket(
							clientFacingDatagramSock, 
							clientHost, 
							clientPort);
		} catch (IOException e) {
			LOGGER.error( 
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
		return clientFacingDatagramSck;
	}

}
