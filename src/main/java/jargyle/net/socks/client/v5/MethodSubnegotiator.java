package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import jargyle.internal.net.socks.transport.v5.DefaultMethodSubnegotiationResult;
import jargyle.internal.net.socks.transport.v5.gssapiauth.GssSocket;
import jargyle.internal.net.socks.transport.v5.gssapiauth.GssapiMethodSubnegotiationResult;
import jargyle.internal.net.socks.transport.v5.gssapiauth.Message;
import jargyle.internal.net.socks.transport.v5.gssapiauth.MessageType;
import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordRequest;
import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordResponse;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.MethodSubnegotiationResult;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevel;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;

enum MethodSubnegotiator {
	
	GSSAPI_METHOD_SUBNEGOTIATOR(Method.GSSAPI) {
		
		private void establishContext(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			byte[] token = new byte[] { };
			while (!context.isEstablished()) {
				if (token == null) {
					token = new byte[] { };
				}
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
						throw new IOException(
								"server aborted process of context establishment");
					}
					token = message.getToken();
				}
			}
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			boolean necReferenceImpl = socks5Client.getProperties().getValue(
					PropertySpec.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
			ProtectionLevels protectionLevels = 
					socks5Client.getProperties().getValue(
							PropertySpec.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
			List<ProtectionLevel> protectionLevelList = 
					protectionLevels.toList(); 
			ProtectionLevel firstProtectionLevel = protectionLevelList.get(0);
			byte[] token = new byte[] { firstProtectionLevel.byteValue() };
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
				throw new IOException("server aborted protection level negotiation");
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
				protectionLevelSelection = ProtectionLevel.valueOfByte(
						token[0]);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			if (!protectionLevelList.contains(protectionLevelSelection)) {
				throw new IOException(String.format(
						"server selected %s which is not acceptable by this socket", 
						protectionLevelSelection));
			}
			return protectionLevelSelection;
		}
		
		private GSSContext newContext(
				final Socks5Client socks5Client) throws IOException {
			GSSManager manager = GSSManager.getInstance();
			String server = socks5Client.getProperties().getValue(
					PropertySpec.SOCKS5_GSSAPIAUTH_SERVICE_NAME);
			GSSName serverName = null;
			try {
				serverName = manager.createName(server, null);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			Oid mechanismOid = socks5Client.getProperties().getValue(
					PropertySpec.SOCKS5_GSSAPIAUTH_MECHANISM_OID);
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
			return context;
		}

		@Override
		public MethodSubnegotiationResult subnegotiateUsing(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			GSSContext context = this.newContext(socks5Client);
			this.establishContext(socket, context, socks5Client);
			ProtectionLevel protectionLevelSelection =
					this.negotiateProtectionLevel(
							socket, context, socks5Client);
			Optional<MessageProp> msgProp = 
					protectionLevelSelection.getMessageProp();
			GssSocket gssSocket = new GssSocket(socket, context, msgProp);
			return new GssapiMethodSubnegotiationResult(gssSocket);
		}
		
	},
	
	NO_ACCEPTABLE_METHODS_METHOD_SUBNEGOTIATOR(Method.NO_ACCEPTABLE_METHODS) {

		@Override
		public MethodSubnegotiationResult subnegotiateUsing(
				final Socket Socket, 
				final Socks5Client socks5Client) throws IOException {
			throw new IOException("no acceptable methods");
		}
		
	},
	
	NO_AUTHENTICATION_REQUIRED_METHOD_SUBNEGOTIATOR(
			Method.NO_AUTHENTICATION_REQUIRED) {

		@Override
		public MethodSubnegotiationResult subnegotiateUsing(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			return new DefaultMethodSubnegotiationResult(socket);
		}
		
	},
	
	USERNAME_PASSWORD_METHOD_SUBNEGOTIATOR(Method.USERNAME_PASSWORD) {

		@Override
		public MethodSubnegotiationResult subnegotiateUsing(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			String username = socks5Client.getProperties().getValue(
					PropertySpec.SOCKS5_USERPASSAUTH_USERNAME);
			char[] password = socks5Client.getProperties().getValue(
					PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD).getPassword();
			UsernamePasswordRequest usernamePasswordReq = 
					UsernamePasswordRequest.newInstance(username, password);
			outputStream.write(usernamePasswordReq.toByteArray());
			outputStream.flush();
			Arrays.fill(password, '\0');
			UsernamePasswordResponse usernamePasswordResp = 
					UsernamePasswordResponse.newInstanceFrom(inputStream);
			if (usernamePasswordResp.getStatus() != 
					UsernamePasswordResponse.STATUS_SUCCESS) {
				throw new IOException("invalid username password");
			}
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
	
	public abstract MethodSubnegotiationResult subnegotiateUsing(
			final Socket socket,
			final Socks5Client socks5Client) throws IOException;
	
}
