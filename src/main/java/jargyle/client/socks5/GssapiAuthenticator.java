package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import jargyle.common.net.socks5.gssapiauth.GssSocket;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.socks5.gssapiauth.Message;
import jargyle.common.net.socks5.gssapiauth.MessageType;
import jargyle.common.net.socks5.gssapiauth.ProtectionLevel;

enum GssapiAuthenticator implements Authenticator {

	INSTANCE;
	
	@Override
	public Socket authenticate(
			final Socket socket, 
			final Socks5Client socks5Client) throws IOException {
		String server = socks5Client.getGssapiServiceName();
		GSSManager manager = GSSManager.getInstance();
		GSSName serverName = null;
		try {
			serverName = manager.createName(server, null);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		Oid mechanismOid = socks5Client.getGssapiMechanismOid();
		GSSContext context = null;
		try {
			context = manager.createContext(
					serverName, 
					mechanismOid,
			        null,
			        GSSContext.DEFAULT_LIFETIME);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		try {
			context.requestMutualAuth(true);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		try {
			context.requestConf(true);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		try {
			context.requestInteg(true);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		GssapiProtectionLevels gssapiProtectionLevels = 
				socks5Client.getGssapiProtectionLevels();
		boolean necReferenceImpl = socks5Client.isGssapiNecReferenceImpl();
		InputStream inStream = socket.getInputStream();
		OutputStream outStream = socket.getOutputStream();
		byte[] token = new byte[] { };
		while (!context.isEstablished()) {
			try {
				token = context.initSecContext(token, 0, token.length);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			if (token != null) {
				outStream.write(Message.newInstance(
						MessageType.AUTHENTICATION, 
						token).toByteArray());
				outStream.flush();
			}
			if (!context.isEstablished()) {
				Message message = Message.newInstanceFrom(inStream);
				if (message.getMessageType().equals(MessageType.ABORT)) {
					throw new IOException("server aborted handshake process");
				}
				token = message.getToken();
			}
		}
		List<GssapiProtectionLevel> gssapiProtectionLevelList = 
				gssapiProtectionLevels.toList(); 
		GssapiProtectionLevel firstGssapiProtectionLevel = 
				gssapiProtectionLevelList.get(0);
		token = new byte[] { 
				firstGssapiProtectionLevel.getProtectionLevel().byteValue() 
		};
		MessageProp prop = null;
		if (!necReferenceImpl) {
			prop = new MessageProp(0, true);
			try {
				token = context.wrap(token, 0, token.length, 
						new MessageProp(prop.getQOP(), prop.getPrivacy()));
			} catch (GSSException e) {
				outStream.write(Message.newInstance(
						MessageType.ABORT, 
						null).toByteArray());
				outStream.flush();
				throw new IOException(e);
			}
		}
		outStream.write(Message.newInstance(
				MessageType.PROTECTION_LEVEL_NEGOTIATION, 
				token).toByteArray());
		outStream.flush();
		Message message = Message.newInstanceFrom(inStream);
		if (message.getMessageType().equals(MessageType.ABORT)) {
			throw new IOException("server aborted handshake process");
		}
		token = message.getToken();
		if (!necReferenceImpl) {
			prop = new MessageProp(0, false);
			try {
				token = context.unwrap(token, 0, token.length, 
						new MessageProp(prop.getQOP(), prop.getPrivacy()));
			} catch (GSSException e) {
				outStream.write(Message.newInstance(
						MessageType.ABORT, 
						null).toByteArray());
				outStream.flush();
				throw new IOException(e);
			}
		}
		ProtectionLevel protectionLevelSelection = null;
		try {
			protectionLevelSelection = ProtectionLevel.valueOf(token[0]);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		GssapiProtectionLevel gssapiProtectionLevelSelection = null;
		try {
			gssapiProtectionLevelSelection = 
					GssapiProtectionLevel.valueOf(protectionLevelSelection);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		if (!gssapiProtectionLevelList.contains(
				gssapiProtectionLevelSelection)) {
			throw new IOException(String.format(
					"server selected %s which is not acceptable by this socket", 
					protectionLevelSelection));
		}
		MessageProp msgProp = gssapiProtectionLevelSelection.newMessageProp();
		Socket newSocket = new GssSocket(socket, context, msgProp);
		return newSocket;
	}

	@Override
	public String toString() {
		return GssapiAuthenticator.class.getSimpleName();
	}
	
}
