package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.internal.client.SocksClientIOExceptionThrowingHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.DomainName;

public final class Socks5HostResolver extends HostResolver {

	private final Socks5Client socks5Client;
	
	Socks5HostResolver(final Socks5Client client) {
		this.socks5Client = client;
	}

	private Host getClientBindHost() {
		return this.socks5Client.getProperties().getValue(
				GeneralPropertySpecConstants.CLIENT_BIND_HOST);
	}

	private PortRanges getClientBindPortRanges() {
		return this.socks5Client.getProperties().getValue(
				GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES);
	}

	private PositiveInteger getClientConnectTimeout() {
		return this.socks5Client.getProperties().getValue(
				GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT);
	}

	private SocketSettings getClientSocketSettings() {
		return this.socks5Client.getProperties().getValue(
				GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS);
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		if (host == null || host.isEmpty()) {
			return InetAddress.getLoopbackAddress();
		}
		if (!(Host.newInstance(host) instanceof HostName)) {
			return InetAddress.getByName(host);
		}
		if (!this.socks5Client.getProperties().getValue(
				Socks5PropertySpecConstants.SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER)) {
			return InetAddress.getByName(host);
		}
		InetAddress inetAddress = null;
		try {
			inetAddress = this.socks5Resolve(host);
		} catch (IOException e) {
			SocksClientIOExceptionThrowingHelper.throwAsSocksClientIOException(
					e, this.socks5Client);
		}
		return inetAddress;
	}

	private InetAddress socks5Resolve(final String host) throws IOException {
		Socket socket = null;
		Socket sock = null;
		Socket sck = null;
		Reply rep;
		try {
			socket = this.socks5Client.getClientSocketBuilder().newClientSocket();
			sock = this.socks5Client.getClientSocketBuilder()
					.proceedToConfigure(socket)
					.setSocketSettings(this.getClientSocketSettings())
					.configure()
					.proceedToConnect()
					.setLocalAddress(this.getClientBindHost().toInetAddress())
					.setLocalPortRanges(this.getClientBindPortRanges())
					.setTimeout(this.getClientConnectTimeout().intValue())
					.getConnectedClientSocket();
			Method method = this.socks5Client.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation =
					this.socks5Client.doMethodSubNegotiation(method, sock);
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
