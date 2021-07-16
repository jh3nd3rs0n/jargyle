package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jargyle.internal.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.internal.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;

final class Socks5ClientHelper {

	public static MethodSubnegotiationResult negotiateUsing(
			final Socket connectedInternalSocket,
			final Socks5Client client) throws IOException {
		InputStream inputStream = connectedInternalSocket.getInputStream();
		OutputStream outputStream = connectedInternalSocket.getOutputStream();
		Methods methods = client.getProperties().getValue(
				PropertySpec.SOCKS5_METHODS);
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(methods);
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		Method method = smsm.getMethod();
		MethodSubnegotiator methodSubnegotiator = null;
		try {
			methodSubnegotiator = MethodSubnegotiator.valueOfMethod(method);
		} catch (IllegalArgumentException e) {
			throw new AssertionError(e);
		}
		return methodSubnegotiator.subnegotiateUsing(
				connectedInternalSocket, client);		
	}
	
	private Socks5ClientHelper() { }
	
}
