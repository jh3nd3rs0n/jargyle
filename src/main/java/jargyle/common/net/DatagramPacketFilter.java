package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;

public interface DatagramPacketFilter {

	void filterAfterReceive(DatagramPacket p) throws IOException;
	
	void filterBeforeSend(DatagramPacket p) throws IOException;
	
}
