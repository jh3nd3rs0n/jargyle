package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import jargyle.client.Properties;
import jargyle.client.PropertySpec;
import jargyle.common.net.DirectSocketInterface;
import jargyle.common.net.HostnameResolver;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;

public final class Socks5HostnameResolver extends HostnameResolver {

	private final Socks5Client socks5Client;
	
	public Socks5HostnameResolver(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		AddressType addressType = AddressType.get(host);
		switch (addressType) {
		case IP_V4_ADDRESS:
		case IP_V6_ADDRESS:
			return InetAddress.getByName(host);
		case DOMAINNAME:
			break;
		default:
			throw new AssertionError(String.format(
					"unknown %s: %s", 
					AddressType.class.getName(), 
					addressType));
		}
		Properties properties = this.socks5Client.getProperties();
		if (!properties.getValue(
				PropertySpec.SOCKS5_FORWARD_HOSTNAME_RESOLUTION_ENABLED, 
				Boolean.class).booleanValue()) {
			return InetAddress.getByName(host);
		}
		SocketInterface socketInterface = new DirectSocketInterface(
				new Socket());
		SocketSettings socketSettings = properties.getValue(
				PropertySpec.SOCKET_SETTINGS, SocketSettings.class);
		socketSettings.applyTo(socketInterface);
		SocketInterface sockInterface = 
				this.socks5Client.connectToSocksServerWith(
						socketInterface, true);
		InputStream inputStream = sockInterface.getInputStream();
		OutputStream outputStream = sockInterface.getOutputStream();
		Socks5Request socks5Req = Socks5Request.newInstance(
				Command.RESOLVE, 
				addressType, 
				host, 
				0);
		outputStream.write(socks5Req.toByteArray());
		outputStream.flush();
		Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
		Reply reply = socks5Rep.getReply();
		if (!reply.equals(Reply.SUCCEEDED)) {
			throw new IOException(String.format(
					"received reply: %s", 
					reply));
		}
		return InetAddress.getByName(socks5Rep.getServerBoundAddress());
	}

}
