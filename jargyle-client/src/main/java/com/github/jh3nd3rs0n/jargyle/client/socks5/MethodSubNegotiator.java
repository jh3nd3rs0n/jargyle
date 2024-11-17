package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.UserInfo;
import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Response;
import org.ietf.jgss.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

abstract class MethodSubNegotiator {
	
	private static final class GssapiMethodSubNegotiator
		extends MethodSubNegotiator {
		
		public GssapiMethodSubNegotiator() {
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
					outStream.write(AuthenticationMessage.newInstance(
							Token.newInstance(token)).toByteArray());
					outStream.flush();
				}
				if (!context.isEstablished()) {
					AuthenticationMessage message;
					try {
						message = AuthenticationMessage.newInstanceFromServer(
								inStream);
					} catch (AbortMessageException e) {
						throw new MethodSubNegotiationException(
								this.getMethod(),
								"server aborted process of context "
										+ "establishment");
					}
					token = message.getToken().getBytes();
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
			byte[] token = new byte[] { protectionLevels.getFirst().byteValue() };
			if (!necReferenceImpl) {
                token = context.wrap(token, 0, token.length,
                        new MessageProp(0, true));
            }
			outStream.write(ProtectionLevelNegotiationMessage.newInstance(
					Token.newInstance(token)).toByteArray());
			outStream.flush();
			ProtectionLevelNegotiationMessage message =
					ProtectionLevelNegotiationMessage.newInstanceFrom(
							inStream);
			token = message.getToken().getBytes();
			if (!necReferenceImpl) {
                token = context.unwrap(token, 0, token.length,
                        new MessageProp(0, true));
            }
			ProtectionLevel protectionLevelSelection;
			try {
				protectionLevelSelection = ProtectionLevel.valueOfByte(
						token[0]);
			} catch (IllegalArgumentException e) {
				throw new MethodSubNegotiationException(this.getMethod(), e);
			}
			if (!protectionLevels.contains(protectionLevelSelection)) {
				throw new MethodSubNegotiationException(
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
		public MethodEncapsulation subNegotiate(
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
						throw new MethodSubNegotiationException(
								this.getMethod(), ex);
					}
				}
				throw e;
			} catch (GSSException e) {
				if (context != null) {
					try {
						context.dispose();
					} catch (GSSException ex) {
						throw new MethodSubNegotiationException(
								this.getMethod(), ex);
					}
				}
				throw new MethodSubNegotiationException(this.getMethod(), e);
			}
			MessageProp msgProp = protectionLevelSelection.newMessageProp(
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG),
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF));
			return new GssapiMethodEncapsulation(socket, context, msgProp);
		}
		
	}
	
	private static final class MethodSubNegotiators {
		
		private final Map<Method, MethodSubNegotiator> methodSubNegotiatorsMap;
		
		public MethodSubNegotiators() {
			this.methodSubNegotiatorsMap = 
					new HashMap<Method, MethodSubNegotiator>();
		}
		
		public void add(final MethodSubNegotiator value) {
			this.methodSubNegotiatorsMap.put(value.getMethod(), value);
		}
		
		public Map<Method, MethodSubNegotiator> toMap() {
			return Collections.unmodifiableMap(this.methodSubNegotiatorsMap);
		}
		
	}

	private static final class NoAcceptableMethodsMethodSubNegotiator
		extends MethodSubNegotiator {
		
		public NoAcceptableMethodsMethodSubNegotiator() {
			super(Method.NO_ACCEPTABLE_METHODS);
		}

		@Override
		public MethodEncapsulation subNegotiate(
				final Socket Socket, 
				final Socks5Client socks5Client) throws IOException {
			throw new MethodSubNegotiationException(
					this.getMethod(), "no acceptable methods");
		}
		
	}
	
	private static final class NoAuthenticationRequiredMethodSubNegotiator
		extends MethodSubNegotiator {
		
		public NoAuthenticationRequiredMethodSubNegotiator() {
			super(Method.NO_AUTHENTICATION_REQUIRED);
		}

		@Override
		public MethodEncapsulation subNegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			return MethodEncapsulation.newNullInstance(socket);
		}
		
	}
	
	private static final class UsernamePasswordMethodSubNegotiator
		extends MethodSubNegotiator {
		
		public UsernamePasswordMethodSubNegotiator() {
			super(Method.USERNAME_PASSWORD);
		}

		private UsernamePassword getUsernamePassword(
				final Socks5Client socks5Client) {
			UsernamePassword usernamePassword = UsernamePassword.newInstance(
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME),
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD).getPassword());
			UserInfo userInfo = socks5Client.getSocksServerUri().getUserInfo();
			if (userInfo == null) {
				return usernamePassword;
			}
			UsernamePassword userPass =	UsernamePassword.tryNewInstanceFrom(
					userInfo.toString());
			if (userPass == null) {
				return usernamePassword;
			}
			return userPass;
		}

		@Override
		public MethodEncapsulation subNegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			UsernamePassword usernamePassword = this.getUsernamePassword(
					socks5Client);
			Request request = Request.newInstance(
					usernamePassword.getUsername(),
					usernamePassword.getPassword());
			outputStream.write(request.toByteArray());
			outputStream.flush();
			Response response =	Response.newInstanceFrom(inputStream);
			UnsignedByte status = response.getStatus();
			if (!status.equals(Response.STATUS_SUCCESS)) {
				throw new MethodSubNegotiationException(
						this.getMethod(), "invalid username password");
			}
			return MethodEncapsulation.newNullInstance(socket);
		}
		
	}
	
	private static final Map<Method, MethodSubNegotiator> METHOD_SUB_NEGOTIATORS_MAP;
	
	static {
		MethodSubNegotiators methodSubNegotiators = new MethodSubNegotiators();
		methodSubNegotiators.add(new GssapiMethodSubNegotiator());
		methodSubNegotiators.add(new NoAcceptableMethodsMethodSubNegotiator());
		methodSubNegotiators.add(
				new NoAuthenticationRequiredMethodSubNegotiator());
		methodSubNegotiators.add(new UsernamePasswordMethodSubNegotiator());
		METHOD_SUB_NEGOTIATORS_MAP = new HashMap<Method, MethodSubNegotiator>(
				methodSubNegotiators.toMap());
	}
	
	public static MethodSubNegotiator getInstance(final Method meth) {
		MethodSubNegotiator methodSubNegotiator = METHOD_SUB_NEGOTIATORS_MAP.get(
				meth);
		if (methodSubNegotiator != null) {
			return methodSubNegotiator;
		}
		String str = METHOD_SUB_NEGOTIATORS_MAP.keySet().stream()
				.map(Method::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				meth));
	}
	
	private final Method method;
	
	private MethodSubNegotiator(final Method meth) {
		this.method = meth;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public abstract MethodEncapsulation subNegotiate(
			final Socket socket,
			final Socks5Client socks5Client) throws IOException;
	
}
