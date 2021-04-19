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
import jargyle.net.socks.transport.v5.Reply;
import jargyle.net.socks.transport.v5.Socks5Reply;
import jargyle.net.socks.transport.v5.Socks5Request;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

public final class Socks5HostResolver extends HostResolver {

	private final Properties properties;
	private final Socks5Client socks5Client;
	
	public Socks5HostResolver(final Socks5Client client) {
		Properties props = client.getProperties();
		this.properties = props;
		this.socks5Client = client;
	}
	
	private boolean canServerResolveHostName(final String hostName) {
		if (!this.properties.getValue(
				PropertySpec.SOCKS5_RESOLVE_RESOLVE_HOST_NAMES_THROUGH_SERVER).booleanValue()) {
			return false;
		}
		Criteria serverResolvableHostNameCriteria = this.properties.getValue(
				PropertySpec.SOCKS5_RESOLVE_SERVER_RESOLVABLE_HOST_NAME_CRITERIA);
		Criterion criterion = serverResolvableHostNameCriteria.anyEvaluatesTrue(
				hostName);
		if (criterion == null) {
			return false;
		}
		Criteria systemResolvableHostNameCriteria = this.properties.getValue(
				PropertySpec.SOCKS5_RESOLVE_SYSTEM_RESOLVABLE_HOST_NAME_CRITERIA);
		criterion = systemResolvableHostNameCriteria.anyEvaluatesTrue(hostName);
		if (criterion != null) {
			return false;
		}
		return true;
	}
	
	@Override
	public InetAddress resolve(final String host) throws IOException {
		if (host == null) {
			return InetAddress.getLoopbackAddress();
		}
		AddressType addressType = AddressType.valueForAddress(host);
		if (!addressType.equals(AddressType.DOMAINNAME)
				|| !this.canServerResolveHostName(host)) {
			return InetAddress.getByName(host);
		}
		Socket socket = this.socks5Client.newInternalSocket();
		this.socks5Client.configureInternalSocket(socket);
		Socket sock = this.socks5Client.getConnectedInternalSocket(socket, true);
		InputStream inputStream = sock.getInputStream();
		OutputStream outputStream = sock.getOutputStream();
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
		return InetAddress.getByName(socks5Rep.getServerBoundAddress());
	}

}
