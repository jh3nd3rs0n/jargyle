package jargyle.server.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;

import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.gssapiauth.GssSocket;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.socks5.gssapiauth.Message;
import jargyle.common.net.socks5.gssapiauth.MessageType;
import jargyle.common.net.socks5.gssapiauth.ProtectionLevel;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordRequest;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordResponse;
import jargyle.server.Configuration;
import jargyle.server.SettingSpec;

enum Authenticator {

	DEFAULT_AUTHENTICATOR(Method.NO_ACCEPTABLE_METHODS) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			throw new IOException("no acceptable authentication methods");
		}
		
	},
	
	GSSAPI_AUTHENTICATOR(Method.GSSAPI) {
		
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
			GssapiProtectionLevel gssapiProtectionLevelChoice = 
					gssapiProtectionLevel; 
			if (!gssapiProtectionLevelList.contains(
					gssapiProtectionLevelChoice)) {
				GssapiProtectionLevel firstGssapiProtectionLevel = 
						gssapiProtectionLevelList.get(0);
				gssapiProtectionLevelChoice = firstGssapiProtectionLevel;			
			}
			ProtectionLevel protectionLevelChoice = 
					gssapiProtectionLevelChoice.protectionLevelValue();
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
			MessageProp msgProp = gssapiProtectionLevelChoice.newMessageProp();
			Socket newSocket = new GssSocket(socket, context, msgProp);
			return newSocket;
		}
		
	},
	
	PERMISSIVE_AUTHENTICATOR(Method.NO_AUTHENTICATION_REQUIRED) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			return socket;
		}
		
	},
	
	USERNAME_PASSWORD_AUTHENTICATOR(Method.USERNAME_PASSWORD) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			UsernamePasswordRequest usernamePasswordReq = 
					UsernamePasswordRequest.newInstanceFrom(inputStream);
			UsernamePasswordResponse usernamePasswordResp = null;
			String username = usernamePasswordReq.getUsername();
			char[] password = usernamePasswordReq.getPassword();
			UsernamePasswordAuthenticator authenticator = 
					configuration.getSocks5UsernamePasswordAuthenticator();
			if (authenticator == null) { 
				authenticator = UsernamePasswordAuthenticator.INSTANCE; 
			}
			if (!authenticator.authenticate(username, password)) {
				usernamePasswordResp = UsernamePasswordResponse.newInstance(
						(byte) 0x01);
				outputStream.write(usernamePasswordResp.toByteArray());
				outputStream.flush();
				return null;
			}
			usernamePasswordResp = UsernamePasswordResponse.newInstance(
					UsernamePasswordResponse.STATUS_SUCCESS);
			outputStream.write(usernamePasswordResp.toByteArray());
			outputStream.flush();
			return socket;
		}
		
	};
	
	public static Authenticator valueOf(final Method meth) {
		for (Authenticator value : Authenticator.values()) {
			if (value.methodValue().equals(meth)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Authenticator> list = Arrays.asList(Authenticator.values());
		for (Iterator<Authenticator> iterator = list.iterator();
				iterator.hasNext();) {
			Authenticator value = iterator.next();
			Method method = value.methodValue();
			sb.append(method);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected method must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
						meth));
	}
	
	private final Method methodValue;
	
	private Authenticator(final Method methValue) {
		this.methodValue = methValue;
	}
	
	public abstract Socket authenticate(
			final Socket socket, 
			final Configuration configuration) throws IOException;
	
	public Method methodValue() {
		return this.methodValue;
	}
	
}
