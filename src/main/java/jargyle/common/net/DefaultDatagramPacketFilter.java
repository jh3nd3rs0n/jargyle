package jargyle.common.net;

import java.io.IOException;
import java.net.DatagramPacket;

public final class DefaultDatagramPacketFilter extends DatagramPacketFilter {

	public DefaultDatagramPacketFilter() { }
	
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
