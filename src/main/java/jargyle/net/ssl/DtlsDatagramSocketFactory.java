package jargyle.net.ssl;

import java.io.IOException;
import java.net.DatagramSocket;

public abstract class DtlsDatagramSocketFactory {

	public abstract DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket,
			final String peerHost,
			final int peerPort) throws IOException;
	
}
