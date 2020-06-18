package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;

public abstract class DatagramPacketFilter {

	public abstract void filterAfterReceive(DatagramPacket p) throws IOException;
	
	public abstract void filterBeforeSend(DatagramPacket p) throws IOException;
	
}
