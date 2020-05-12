package jargyle.server.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;

import jargyle.common.net.socks5.gssapiauth.GssSocket;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.socks5.gssapiauth.Message;
import jargyle.common.net.socks5.gssapiauth.MessageType;
import jargyle.common.net.socks5.gssapiauth.ProtectionLevel;
import jargyle.server.Configuration;
import jargyle.server.SettingSpec;

enum GssapiAuthenticator implements Authenticator {

	INSTANCE;
	
	@Override
	public Socket authenticate(
			final Socket socket, 
			final Configuration configuration) throws IOException {
		GSSManager manager = GSSManager.getInstance();
		GSSContext context = null;
		try {
			context = manager.createContext((GSSCredential) null);
		} catch (GSSException e) {
			throw new IOException(e);
		}
		GssapiProtectionLevels gssapiProtectionLevels = 
				configuration.getSettings().getLastValue(
						SettingSpec.SOCKS5_GSSAPI_PROTECTION_LEVELS, 
						GssapiProtectionLevels.class);		
		boolean necReferenceImpl = configuration.getSettings().getLastValue(
				SettingSpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL, 
				Boolean.class).booleanValue();
		InputStream inStream = socket.getInputStream();
		OutputStream outStream = socket.getOutputStream();
		byte[] token = null;
		while (!context.isEstablished()) {
			Message message = Message.newInstanceFrom(inStream);
			if (message.getMessageType().equals(MessageType.ABORT)) {
				throw new IOException("client aborted handshake process");
			}
			token = message.getToken();
			try {
				token = context.acceptSecContext(token, 0, token.length);
			} catch (GSSException e) {
				outStream.write(Message.newInstance(
						MessageType.ABORT, 
						null).toByteArray());
				outStream.flush();
				throw new IOException(e);
			}
			if (token == null) {
				outStream.write(Message.newInstance(
						MessageType.AUTHENTICATION, 
						new byte[] { }).toByteArray());
				outStream.flush();
			} else {
				outStream.write(Message.newInstance(
						MessageType.AUTHENTICATION, 
						token).toByteArray());
				outStream.flush();
			}
		}
		Message message = Message.newInstanceFrom(inStream);
		if (message.getMessageType().equals(MessageType.ABORT)) {
			throw new IOException("client aborted handshake process");
		}
		token = message.getToken();
		MessageProp prop = null;
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
		ProtectionLevel protectionLevel = null;
		try {
			protectionLevel = ProtectionLevel.valueOf(token[0]);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		GssapiProtectionLevel gssapiProtectionLevel = null;
		try {
			gssapiProtectionLevel = GssapiProtectionLevel.valueOf(
					protectionLevel);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		List<GssapiProtectionLevel> gssapiProtectionLevelList =
				gssapiProtectionLevels.toList();
		ProtectionLevel protectionLevelChoice = null;
		if (gssapiProtectionLevelList.contains(gssapiProtectionLevel)) {
			protectionLevelChoice = 
					gssapiProtectionLevel.getProtectionLevel();
		} else {
			GssapiProtectionLevel firstGssapiProtectionLevel = 
					gssapiProtectionLevelList.get(0);
			protectionLevelChoice = 
					firstGssapiProtectionLevel.getProtectionLevel();
		}
		token = new byte[] { protectionLevelChoice.byteValue() };
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
		if (socket.isClosed()) {
			throw new SocketException("client closed due to client " 
					+ "finding choice of protection level unacceptable");
		}
		boolean privacyState = false;
		switch (protectionLevelChoice) {
		case NONE:
			break;
		case REQUIRED_INTEG:
			break;
		case REQUIRED_INTEG_AND_CONF:
			privacyState = true;
			break;
		default:
			throw new AssertionError(String.format(
					"unhandled %s: %s", 
					ProtectionLevel.class.getSimpleName(), 
					protectionLevelChoice));
		}
		ProtectionLevel level = protectionLevelChoice;
		MessageProp msgProp = null;
		if (!level.equals(ProtectionLevel.NONE)) {
			msgProp = new MessageProp(0, privacyState);
		}
		Socket newSocket = new GssSocket(socket, context, msgProp);
		return newSocket;
	}

	@Override
	public String toString() {
		return GssapiAuthenticator.class.getSimpleName();
	}
	
}
