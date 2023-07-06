package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssapiMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.Message;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.MessageType;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordResponse;

abstract class MethodSubnegotiator {
	
	private static final class GssapiMethodSubnegotiator 
		extends MethodSubnegotiator {
		
		public GssapiMethodSubnegotiator() {
			super(Method.GSSAPI);
		}
		
		private void establishContext(
				final Socket socket, final GSSContext context) 
				throws IOException, GSSException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			byte[] token = new byte[] { };
			while (!context.isEstablished()) {
				if (token == null) {
					token = new byte[] { };
				}
				token = context.initSecContext(token, 0, token.length);
				if (token != null) {
					outStream.write(Message.newInstance(
							MessageType.AUTHENTICATION, 
							token).toByteArray());
					outStream.flush();
				}
				if (!context.isEstablished()) {
					Message message = Message.newInstanceFrom(inStream);
					if (message.getMessageType().equals(MessageType.ABORT)) {
						throw new MethodSubnegotiationException(
								this.getMethod(), 
								"server aborted process of context "
								+ "establishment");
					}
					token = message.getToken();
				}
			}
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) 
				throws IOException, GSSException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			boolean necReferenceImpl = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
			ProtectionLevels protectionLevels =
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
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
					throw e;
				}
			}
			outStream.write(Message.newInstance(
					MessageType.PROTECTION_LEVEL_NEGOTIATION, 
					token).toByteArray());
			outStream.flush();
			Message message = Message.newInstanceFrom(inStream);
			if (message.getMessageType().equals(MessageType.ABORT)) {
				throw new MethodSubnegotiationException(
						this.getMethod(), 
						"server aborted protection level negotiation");
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
					throw e;
				}
			}
			ProtectionLevel protectionLevelSelection = null;
			try {
				protectionLevelSelection = ProtectionLevel.valueOfByte(
						token[0]);
			} catch (IllegalArgumentException e) {
				throw new MethodSubnegotiationException(this.getMethod(), e);
			}
			if (!protectionLevelList.contains(protectionLevelSelection)) {
				throw new MethodSubnegotiationException(
						this.getMethod(), 
						String.format(
								"server selected %s which is not acceptable "
								+ "by this socket",
								protectionLevelSelection));
			}
			return protectionLevelSelection;
		}
		
		private GSSContext newContext(
				final Socks5Client socks5Client) throws GSSException {
			GSSManager manager = GSSManager.getInstance();
			String server = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME);
			GSSName serverName = manager.createName(server, null);
			Oid mechanismOid = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_MECHANISM_OID);
			GSSContext context = manager.createContext(
					serverName, 
					mechanismOid,
			        null,
			        GSSContext.DEFAULT_LIFETIME);
			context.requestMutualAuth(true);
			context.requestConf(true);
			context.requestInteg(true);			
			return context;
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			GSSContext context = null;
			ProtectionLevel protectionLevelSelection = null;
			try {
				context = this.newContext(socks5Client);
				this.establishContext(socket, context);
				protectionLevelSelection = this.negotiateProtectionLevel(
						socket, context, socks5Client);
			} catch (IOException e) {
				if (context != null) {
					try {
						context.dispose();
					} catch (GSSException ex) {
						throw new MethodSubnegotiationException(
								this.getMethod(), ex);
					}
				}
				throw e;
			} catch (GSSException e) {
				if (context != null) {
					try {
						context.dispose();
					} catch (GSSException ex) {
						throw new MethodSubnegotiationException(
								this.getMethod(), ex);
					}
				}
				throw new MethodSubnegotiationException(this.getMethod(), e);				
			}
			MessageProp msgProp = protectionLevelSelection.getMessageProp();
			return new GssapiMethodEncapsulation(socket, context, msgProp);
		}
		
	}

	private static final class NoAcceptableMethodsMethodSubnegotiator 
		extends MethodSubnegotiator {
		
		public NoAcceptableMethodsMethodSubnegotiator() {
			super(Method.NO_ACCEPTABLE_METHODS);
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket Socket, 
				final Socks5Client socks5Client) throws IOException {
			throw new MethodSubnegotiationException(
					this.getMethod(), "no acceptable methods");
		}
		
	}
	
	private static final class NoAuthenticationRequiredMethodSubnegotiator
		extends MethodSubnegotiator {
		
		public NoAuthenticationRequiredMethodSubnegotiator() {
			super(Method.NO_AUTHENTICATION_REQUIRED);
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			return MethodEncapsulation.newNullInstance(socket);
		}
		
	}
	
	private static final class UsernamePasswordMethodSubnegotiator
		extends MethodSubnegotiator {
		
		public UsernamePasswordMethodSubnegotiator() {
			super(Method.USERNAME_PASSWORD);
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			byte status = 1;
			char[] password = null;
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				String username = socks5Client.getProperties().getValue(
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME);
				password = socks5Client.getProperties().getValue(
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD).getPassword();
				UsernamePasswordRequest usernamePasswordReq = 
						UsernamePasswordRequest.newInstance(username, password);
				outputStream.write(usernamePasswordReq.toByteArray());
				outputStream.flush();
				UsernamePasswordResponse usernamePasswordResp = 
						UsernamePasswordResponse.newInstanceFrom(inputStream);
				status = usernamePasswordResp.getStatus();
			} finally {
				if (password != null) { Arrays.fill(password, '\0'); }
			}
			if (status != UsernamePasswordResponse.STATUS_SUCCESS) {
				throw new MethodSubnegotiationException(
						this.getMethod(), "invalid username password");
			}
			return MethodEncapsulation.newNullInstance(socket);
		}
		
	}
	
	private static final Map<Method, MethodSubnegotiator> METHOD_SUBNEGOTIATOR_MAP;
	
	static {
		METHOD_SUBNEGOTIATOR_MAP = new HashMap<Method, MethodSubnegotiator>();
		MethodSubnegotiator gssapiMethodSubnegotiator = 
				new GssapiMethodSubnegotiator();
		METHOD_SUBNEGOTIATOR_MAP.put(
				gssapiMethodSubnegotiator.getMethod(), 
				gssapiMethodSubnegotiator);
		MethodSubnegotiator noAcceptableMethodsMethodSubnegotiator =
				new NoAcceptableMethodsMethodSubnegotiator();
		METHOD_SUBNEGOTIATOR_MAP.put(
				noAcceptableMethodsMethodSubnegotiator.getMethod(),
				noAcceptableMethodsMethodSubnegotiator);
		MethodSubnegotiator noAuthenticationRequiredMethodSubnegotiator =
				new NoAuthenticationRequiredMethodSubnegotiator();
		METHOD_SUBNEGOTIATOR_MAP.put(
				noAuthenticationRequiredMethodSubnegotiator.getMethod(),
				noAuthenticationRequiredMethodSubnegotiator);
		MethodSubnegotiator usernamePasswordMethodSubnegotiator =
				new UsernamePasswordMethodSubnegotiator();
		METHOD_SUBNEGOTIATOR_MAP.put(
				usernamePasswordMethodSubnegotiator.getMethod(),
				usernamePasswordMethodSubnegotiator);
	}
	
	public static MethodSubnegotiator getInstance(final Method meth) {
		MethodSubnegotiator methodSubnegotiator = METHOD_SUBNEGOTIATOR_MAP.get(
				meth);
		if (methodSubnegotiator != null) {
			return methodSubnegotiator;
		}
		String str = METHOD_SUBNEGOTIATOR_MAP.keySet().stream()
				.map(Method::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				meth));
	}
	
	private final Method method;
	
	private MethodSubnegotiator(final Method meth) {
		this.method = meth;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public abstract MethodEncapsulation subnegotiate(
			final Socket socket,
			final Socks5Client socks5Client) throws IOException;
	
}
