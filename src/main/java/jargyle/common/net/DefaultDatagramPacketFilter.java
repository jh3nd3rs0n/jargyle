package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;

public enum DefaultDatagramPacketFilter implements DatagramPacketFilter {

	INSTANCE;

	@Override
	public void filterAfterReceive(final DatagramPacket p) throws IOException {
	}

	@Override
	public void filterBeforeSend(final DatagramPacket p) throws IOException {
	}
	
	@Override
	public String toString() {
		return DefaultDatagramPacketFilter.class.getSimpleName();
	}
	
}
