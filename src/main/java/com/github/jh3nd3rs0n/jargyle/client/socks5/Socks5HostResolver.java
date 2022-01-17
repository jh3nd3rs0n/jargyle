package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.AddressType;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5HostResolver extends HostResolver {

	private final Socks5Client socks5Client;
	
	Socks5HostResolver(final Socks5Client client) {
		this.socks5Client = client;
	}
	
	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		if (host == null) {
			return InetAddress.getLoopbackAddress();
		}
		Properties properties = this.socks5Client.getProperties();
		AddressType addressType = AddressType.valueForString(host);
		if (!addressType.equals(AddressType.DOMAINNAME) || !properties.getValue(
				Socks5PropertySpecConstants.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND).booleanValue()) {
			return InetAddress.getByName(host);
		}
		Socket socket = this.socks5Client.newInternalSocket();
		this.socks5Client.configureInternalSocket(socket);
		Socket sock = this.socks5Client.getConnectedInternalSocket(
				socket, true);
		Method method = this.socks5Client.negotiateMethod(sock);
		MethodEncapsulation methodEncapsulation = 
				this.socks5Client.performMethodSubnegotiation(method, sock);
		Socket sck = methodEncapsulation.getSocket();
		Socks5Request socks5Req = Socks5Request.newInstance(
				Command.RESOLVE, 
				host, 
				0);
		this.socks5Client.sendSocks5Request(socks5Req, sck);
		Socks5Reply socks5Rep = this.socks5Client.receiveSocks5Reply(sck);
		InetAddress inetAddress = InetAddress.getByName(
				socks5Rep.getServerBoundAddress());
		return InetAddress.getByAddress(host, inetAddress.getAddress());
	}

}
