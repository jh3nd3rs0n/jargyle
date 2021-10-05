package com.github.jh3nd3rs0n.jargyle.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.AllZerosInetAddressHelper;

public class WorkerContext {

	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final Socket clientFacingSocket;
	private final Configuration configuration;
	private final NetObjectFactory netObjectFactory;
	
	public WorkerContext(
			final Socket clientFacingSock,
			final Configuration config,
			final NetObjectFactory netObjFactory,
			final DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory) {
		Objects.requireNonNull(clientFacingSock);
		Objects.requireNonNull(config);
		Objects.requireNonNull(netObjFactory);
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientFacingSocket = clientFacingSock;
		this.configuration = config;
		this.netObjectFactory = netObjFactory;
	}
	
	public WorkerContext(final WorkerContext other) {
		this.clientFacingDtlsDatagramSocketFactory = 
				other.clientFacingDtlsDatagramSocketFactory;
		this.clientFacingSocket = other.clientFacingSocket;
		this.configuration = other.configuration;
		this.netObjectFactory = other.netObjectFactory;
	}

	public final DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		return this.clientFacingDtlsDatagramSocketFactory;
	}

	public final Socket getClientFacingSocket() {
		return this.clientFacingSocket;
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	public final NetObjectFactory getNetObjectFactory() {
		return this.netObjectFactory;
	}
	
	public final Settings getSettings() {
		return this.configuration.getSettings();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientFacingSocket=")
			.append(this.clientFacingSocket)
			.append("]");
		return builder.toString();
	}
	
	public DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock, 
			final String clientHost, 
			final int clientPort) throws IOException {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		if (!AllZerosInetAddressHelper.isAllZerosHostAddress(clientHost) 
				&& clientPort > 0) {
			InetAddress udpClientHostInetAddress = InetAddress.getByName(
					clientHost);
			clientFacingDatagramSck.connect(
					udpClientHostInetAddress, clientPort);
		}
		if (clientFacingDatagramSck.isConnected() 
				&& this.clientFacingDtlsDatagramSocketFactory != null) {
			clientFacingDatagramSck = 
					this.clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
							clientFacingDatagramSck, 
							clientHost, 
							clientPort);
		}
		return clientFacingDatagramSck;
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		OutputStream clientFacingOutputStream = 
				this.clientFacingSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
}
