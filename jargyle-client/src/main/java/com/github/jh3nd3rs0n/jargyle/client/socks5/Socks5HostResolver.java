package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient.ClientSocketConnectParams;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientIOExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostName;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.AddressType;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Socks5Request;

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
		Host hst = Host.newInstance(host);
		if (!(hst instanceof HostName) || !properties.getValue(
				Socks5PropertySpecConstants.SOCKS5_USE_RESOLVE_COMMAND).booleanValue()) {
			return InetAddress.getByName(host);
		}
		Socket socket = null;
		Socket sock = null;
		Socket sck = null;
		Socks5Reply socks5Rep = null;
		try {
			socket = this.socks5Client.newClientSocket();
			ClientSocketConnectParams params = new ClientSocketConnectParams();
			sock = this.socks5Client.getConnectedClientSocket(
					socket, params);
			Method method = this.socks5Client.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.doMethodSubnegotiation(method, sock);
			sck = methodEncapsulation.getSocket();
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.RESOLVE, 
					Address.newInstance(host), 
					Port.valueOf(0));
			this.socks5Client.sendSocks5Request(socks5Req, sck);
			try {
				socks5Rep = this.socks5Client.receiveSocks5Reply(sck);
			} catch (FailureSocks5ReplyException e) {
				Reply reply = e.getFailureSocks5Reply().getReply();
				if (reply.equals(Reply.HOST_UNREACHABLE)) {
					throw new UnknownHostException(host);
				} else {
					throw e;
				}
			}
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);			
		} finally {
			if (sck != null && !sck.isClosed()) {
				sck.close();
			}
			if (sock != null && !sock.isClosed()) {
				sock.close();
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
		String serverBoundAddress = 
				socks5Rep.getServerBoundAddress().toString();
		AddressType addressType =
				socks5Rep.getServerBoundAddress().getAddressType();
		if (addressType.equals(AddressType.DOMAINNAME)) {
			throw new Socks5ClientIOException(
					this.socks5Client, 
					String.format(
							"server bound address is not an IP address. "
							+ "actual server bound address is %s", 
							serverBoundAddress));
		}
		InetAddress inetAddress = InetAddress.getByName(serverBoundAddress);
		return InetAddress.getByAddress(host, inetAddress.getAddress());
	}

}
