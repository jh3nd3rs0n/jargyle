package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;

import jargyle.internal.net.socks.transport.v5.DefaultMethodSubnegotiationResult;
import jargyle.internal.net.socks.transport.v5.gssapiauth.GssSocket;
import jargyle.internal.net.socks.transport.v5.gssapiauth.GssapiMethodSubnegotiationResult;
import jargyle.internal.net.socks.transport.v5.gssapiauth.Message;
import jargyle.internal.net.socks.transport.v5.gssapiauth.MessageType;
import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordRequest;
import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordResponse;
import jargyle.net.socks.server.Configuration;
import jargyle.net.socks.server.SettingSpec;
import jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.MethodSubnegotiationResult;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevel;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

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
					SettingSpec.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
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
							SettingSpec.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
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
		public MethodSubnegotiationResult subnegotiateWith(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			GSSContext context = this.newContext();
			this.establishContext(socket, context, configuration);
			ProtectionLevel protectionLevelChoice =
					this.negotiateProtectionLevel(
							socket, context, configuration);
			Optional<MessageProp> msgProp = 
					protectionLevelChoice.getMessageProp();
			GssSocket gssSocket = new GssSocket(socket, context, msgProp);
			return new GssapiMethodSubnegotiationResult(gssSocket);
		}
		
	},
	
	NO_ACCEPTABLE_METHODS_METHOD_SUBNEGOTIATOR(Method.NO_ACCEPTABLE_METHODS) {

		@Override
		public MethodSubnegotiationResult subnegotiateWith(
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
		public MethodSubnegotiationResult subnegotiateWith(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			return new DefaultMethodSubnegotiationResult(socket);
		}
		
	},
	
	USERNAME_PASSWORD_METHOD_SUBNEGOTIATOR(Method.USERNAME_PASSWORD) {

		@Override
		public MethodSubnegotiationResult subnegotiateWith(
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
							SettingSpec.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR);
			if (authenticator == null) { 
				authenticator = UsernamePasswordAuthenticator.newInstance(); 
			}
			if (!authenticator.authenticate(username, password)) {
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
			return new DefaultMethodSubnegotiationResult(socket);
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
	
	public abstract MethodSubnegotiationResult subnegotiateWith(
			final Socket socket,
			final Configuration configuration) throws IOException;
	
}
