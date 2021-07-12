package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import jargyle.net.InetAddressHelper;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {

	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	private final Encapsulator encapsulator;
	
	public DtlsDatagramSocketFactoryImpl(
			final DtlsDatagramSocketFactory dtlsDatagramSockFactory,
			final Encapsulator encpsltr) {
		this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
		this.encapsulator = encpsltr;
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket, 
			final String peerHost, 
			final int peerPort)	throws IOException {
		DatagramSocket datagramSock = datagramSocket;
		if (!InetAddressHelper.isAllZerosHostAddress(peerHost) 
				&& peerPort > 0) {
			InetAddress peerHostInetAddress = InetAddress.getByName(
					peerHost);
			datagramSock.connect(peerHostInetAddress, peerPort);
		}
		if (datagramSock.isConnected()) {
			datagramSock = this.dtlsDatagramSocketFactory.newDatagramSocket(
					datagramSock, peerHost, peerPort);
		}
		return this.encapsulator.encapsulate(datagramSock);
	}

}
