package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import jargyle.net.HostResolver;
import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.AddressType;
import jargyle.net.socks.transport.v5.Command;
import jargyle.net.socks.transport.v5.MethodSubnegotiationResult;
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.net.socks.transport.v5.Socks5Request;

public final class Socks5HostResolver extends HostResolver {

	private final Socks5Client socks5Client;
	
	public Socks5HostResolver(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		if (host == null) {
			return InetAddress.getLoopbackAddress();
		}
		Properties properties = this.socks5Client.getProperties();
		AddressType addressType = AddressType.valueForString(host);
		if (!addressType.equals(AddressType.DOMAINNAME) || !properties.getValue(
				PropertySpec.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND).booleanValue()) {
			return InetAddress.getByName(host);
		}
		Socket socket = this.socks5Client.newInternalSocket();
		this.socks5Client.configureInternalSocket(socket);
		Socket sock = this.socks5Client.getConnectedInternalSocket(
				socket, true);
		MethodSubnegotiationResult methodSubnegotiationResult = 
				this.socks5Client.negotiateUsing(sock);
		Socket sck = methodSubnegotiationResult.getSocket();
		InputStream inputStream = sck.getInputStream();
		OutputStream outputStream = sck.getOutputStream();
		Socks5Request socks5Req = Socks5Request.newInstance(
				Command.RESOLVE, 
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
		InetAddress inetAddress = InetAddress.getByName(
				socks5Rep.getServerBoundAddress());
		return InetAddress.getByAddress(host, inetAddress.getAddress());
	}

}
