package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.NullMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssSocket;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssapiMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.Message;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.MessageType;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordResponse;

enum MethodSubnegotiator {

	GSSAPI_METHOD_SUBNEGOTIATOR(Method.GSSAPI) {
		
		private void establishContext(
				final Socket socket,
				final GSSContext context,
				final Configuration configuration) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			byte[] token = null;
			while (!context.isEstablished()) {
				Message message = Message.newInstanceFrom(inStream);
				if (message.getMessageType().equals(MessageType.ABORT)) {
					throw new IOException(String.format(
							"client %s aborted process of context establishment",
							socket));
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
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket, 
				final GSSContext context,
				final Configuration configuration) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			Message message = Message.newInstanceFrom(inStream);
			if (message.getMessageType().equals(MessageType.ABORT)) {
				throw new IOException(String.format(
						"client %s aborted protection level negotiation",
						socket));
			}
			boolean necReferenceImpl = configuration.getSettings().getLastValue(
					Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
			byte[] token = message.getToken();
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
				protectionLevel = ProtectionLevel.valueOfByte(token[0]);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			ProtectionLevels protectionLevels = 
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
			List<ProtectionLevel> protectionLevelList = 
					protectionLevels.toList();
			ProtectionLevel protectionLevelChoice = protectionLevel;
			if (!protectionLevelList.contains(protectionLevelChoice)) {
				ProtectionLevel firstProtectionLevel = 
						protectionLevelList.get(0);
				protectionLevelChoice = firstProtectionLevel;
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
				throw new IOException(String.format(
						"client %s closed due to client finding choice of "
						+ "protection level unacceptable",
						socket));
			}
			return protectionLevelChoice;
		}
		
		private GSSContext newContext() throws IOException {
			GSSManager manager = GSSManager.getInstance();
			GSSContext context = null;
			try {
				context = manager.createContext((GSSCredential) null);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			return context;
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			GSSContext context = this.newContext();
			this.establishContext(socket, context, configuration);
			ProtectionLevel protectionLevelChoice =
					this.negotiateProtectionLevel(
							socket, context, configuration);
			MessageProp msgProp = protectionLevelChoice.getMessageProp();
			GssSocket gssSocket = new GssSocket(socket, context, msgProp);
			return new GssapiMethodEncapsulation(gssSocket);
		}
		
	},
	
	NO_ACCEPTABLE_METHODS_METHOD_SUBNEGOTIATOR(Method.NO_ACCEPTABLE_METHODS) {

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			throw new IOException(String.format(
					"no acceptable methods from %s",
					socket));
		}
		
	},
	
	NO_AUTHENTICATION_REQUIRED_METHOD_SUBNEGOTIATOR(
			Method.NO_AUTHENTICATION_REQUIRED) {

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			return new NullMethodEncapsulation(socket);
		}
		
	},
	
	USERNAME_PASSWORD_METHOD_SUBNEGOTIATOR(Method.USERNAME_PASSWORD) {

		@Override
		public MethodEncapsulation subnegotiate(
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
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR);
			if (authenticator == null) { 
				authenticator = UsernamePasswordAuthenticator.newInstance(); 
			}
			boolean valid = authenticator.authenticate(username, password);
			Arrays.fill(password, '\0');
			if (!valid) {
				usernamePasswordResp = UsernamePasswordResponse.newInstance(
						(byte) 0x01);
				outputStream.write(usernamePasswordResp.toByteArray());
				outputStream.flush();
				throw new IOException(String.format(
						"invalid username password from %s",
						socket));
			}
			usernamePasswordResp = UsernamePasswordResponse.newInstance(
					UsernamePasswordResponse.STATUS_SUCCESS);
			outputStream.write(usernamePasswordResp.toByteArray());
			outputStream.flush();
			return new NullMethodEncapsulation(socket);
		}
		
	};
	
	public static MethodSubnegotiator valueOfMethod(final Method meth) {
		for (MethodSubnegotiator value : MethodSubnegotiator.values()) {
			if (value.methodValue().equals(meth)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<MethodSubnegotiator> list = Arrays.asList(
				MethodSubnegotiator.values());
		for (Iterator<MethodSubnegotiator> iterator = list.iterator();
				iterator.hasNext();) {
			MethodSubnegotiator value = iterator.next();
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
	
	private MethodSubnegotiator(final Method methValue) {
		this.methodValue = methValue;
	}
	
	public Method methodValue() {
		return this.methodValue;
	}
	
	public abstract MethodEncapsulation subnegotiate(
			final Socket socket,
			final Configuration configuration) throws IOException;
	
}
