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
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.DomainName;

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
		Reply rep = null;
		try {
			socket = this.socks5Client.newClientSocket();
			ClientSocketConnectParams params = new ClientSocketConnectParams();
			sock = this.socks5Client.getConnectedClientSocket(
					socket, params);
			Method method = this.socks5Client.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.doMethodSubnegotiation(method, sock);
			sck = methodEncapsulation.getSocket();
			Request req = Request.newInstance(
					Command.RESOLVE, 
					Address.newInstanceFrom(host),
					Port.valueOf(0));
			this.socks5Client.sendRequest(req, sck);
			try {
				rep = this.socks5Client.receiveReply(sck);
			} catch (FailureReplyException e) {
				ReplyCode replyCode = e.getFailureReply().getReplyCode();
				if (replyCode.equals(ReplyCode.HOST_UNREACHABLE)) {
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
				rep.getServerBoundAddress().toString();
		if (rep.getServerBoundAddress() instanceof DomainName) {
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
