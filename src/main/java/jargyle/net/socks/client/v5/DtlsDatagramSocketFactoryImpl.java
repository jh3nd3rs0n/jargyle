package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.net.DatagramSocket;

import jargyle.net.ssl.DtlsDatagramSocketFactory;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {
	
	public DtlsDatagramSocketFactoryImpl() { }
	
	@Override
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket, 
			final String peerHost, 
			final int peerPort, 
			final boolean useClientMode)
			throws IOException {
		// TODO: Implement DtlsDatagramSocket
		return datagramSocket;
	}

}