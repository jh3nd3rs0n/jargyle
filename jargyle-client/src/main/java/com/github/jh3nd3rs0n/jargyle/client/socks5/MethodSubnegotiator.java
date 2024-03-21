package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.UsernamePasswordResponse;
import org.ietf.jgss.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

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
					Message message = Message.newInstanceFrom(
							inStream);
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
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL).booleanValue();
			ProtectionLevels protectionLevels =
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS);
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
							new byte[]{}).toByteArray());
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
							new byte[]{}).toByteArray());
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
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME);
			GSSName serverName = manager.createName(server, null);
			Oid mechanismOid = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID);
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
	
	private static final class MethodSubnegotiators {
		
		private final Map<Method, MethodSubnegotiator> methodSubnegotiatorsMap;
		
		public MethodSubnegotiators() {
			this.methodSubnegotiatorsMap = 
					new HashMap<Method, MethodSubnegotiator>();
		}
		
		public void add(final MethodSubnegotiator value) {
			this.methodSubnegotiatorsMap.put(value.getMethod(), value);
		}
		
		public Map<Method, MethodSubnegotiator> toMap() {
			return Collections.unmodifiableMap(this.methodSubnegotiatorsMap);
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
						Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME);
				password = socks5Client.getProperties().getValue(
						Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD).getPassword();
				UsernamePasswordRequest usernamePasswordReq = 
						UsernamePasswordRequest.newInstance(username, password);
				outputStream.write(usernamePasswordReq.toByteArray());
				outputStream.flush();
				UsernamePasswordResponse usernamePasswordResp = 
						UsernamePasswordResponse.newInstanceFrom(
								inputStream);
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
	
	private static final Map<Method, MethodSubnegotiator> METHOD_SUBNEGOTIATORS_MAP;
	
	static {
		MethodSubnegotiators methodSubnegotiators = new MethodSubnegotiators();
		methodSubnegotiators.add(new GssapiMethodSubnegotiator());
		methodSubnegotiators.add(new NoAcceptableMethodsMethodSubnegotiator());
		methodSubnegotiators.add(
				new NoAuthenticationRequiredMethodSubnegotiator());
		methodSubnegotiators.add(new UsernamePasswordMethodSubnegotiator());
		METHOD_SUBNEGOTIATORS_MAP = new HashMap<Method, MethodSubnegotiator>(
				methodSubnegotiators.toMap());
	}
	
	public static MethodSubnegotiator getInstance(final Method meth) {
		MethodSubnegotiator methodSubnegotiator = METHOD_SUBNEGOTIATORS_MAP.get(
				meth);
		if (methodSubnegotiator != null) {
			return methodSubnegotiator;
		}
		String str = METHOD_SUBNEGOTIATORS_MAP.keySet().stream()
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
