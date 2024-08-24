package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.number.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Request;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.userpassmethod.Response;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.HashedPassword;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.User;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepository;
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
			byte[] token;
			while (!context.isEstablished()) {
				AuthenticationMessage message =
						AuthenticationMessage.newInstanceFromClient(inStream);
				token = message.getToken().getBytes();
				try {
					token = context.acceptSecContext(token, 0, token.length);
				} catch (GSSException e) {
					outStream.write(AbortMessage.INSTANCE.toByteArray());
					outStream.flush();
					throw e;
				}
				if (token == null) {
					outStream.write(AuthenticationMessage.newInstance(
							Token.newInstance(new byte[]{})).toByteArray());
					outStream.flush();
				} else {
					outStream.write(AuthenticationMessage.newInstance(
							Token.newInstance(token)).toByteArray());
					outStream.flush();
				}
			}
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket, 
				final GSSContext context,
				final Configuration configuration) 
				throws IOException, GSSException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			ProtectionLevelNegotiationMessage message =
					ProtectionLevelNegotiationMessage.newInstanceFrom(
							inStream);
			boolean necReferenceImpl = configuration.getSettings().getLastValue(
					Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL).booleanValue();
			byte[] token = message.getToken().getBytes();
			if (!necReferenceImpl) {
                token = context.unwrap(token, 0, token.length,
                        new MessageProp(0, true));
            }
			ProtectionLevel protectionLevel;
			try {
				protectionLevel = ProtectionLevel.valueOfByte(token[0]);
			} catch (IllegalArgumentException e) {
				throw new MethodSubNegotiationException(this.getMethod(), e);
			}
			ProtectionLevels protectionLevels = 
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS);
			ProtectionLevel protectionLevelChoice = protectionLevel;
			if (!protectionLevels.contains(protectionLevelChoice)) {
				protectionLevelChoice = protectionLevels.getFirst();
			}
			token = new byte[] { protectionLevelChoice.byteValue() };
			if (!necReferenceImpl) {
                token = context.wrap(token, 0, token.length,
                        new MessageProp(0, true));
            }
			outStream.write(ProtectionLevelNegotiationMessage.newInstance(
					Token.newInstance(token)).toByteArray());
			outStream.flush();
			if (socket.isClosed()) {
				throw new MethodSubNegotiationException(
						this.getMethod(), 
						String.format(
								"client %s closed due to client finding "
								+ "choice of protection level unacceptable",
								socket));
			}
			return protectionLevelChoice;
		}
		
		private GSSContext newContext() throws GSSException {
			GSSManager manager = GSSManager.getInstance();
			return manager.createContext((GSSCredential) null);
		}

		@Override
		public MethodSubNegotiationResults subNegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			GSSContext context = null;
			ProtectionLevel protectionLevelChoice = null;
			String user = null;
			try {
				context = this.newContext();
				this.establishContext(socket, context);
				protectionLevelChoice =	this.negotiateProtectionLevel(
						socket, context, configuration);
				user = context.getSrcName().toString();				
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
			MessageProp msgProp = protectionLevelChoice.newMessageProp(
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG),
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF));
			MethodEncapsulation methodEncapsulation = 
					new GssapiMethodEncapsulation(socket, context, msgProp);
			return new MethodSubNegotiationResults(
					this.getMethod(), methodEncapsulation, user);
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
		public MethodSubNegotiationResults subNegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			throw new MethodSubNegotiationException(
					this.getMethod(), 
					String.format("no acceptable methods from %s", socket));
		}
		
	}

	private static final class NoAuthenticationRequiredMethodSubNegotiator
		extends MethodSubNegotiator {

		public NoAuthenticationRequiredMethodSubNegotiator() {
			super(Method.NO_AUTHENTICATION_REQUIRED);
		}
		
		@Override
		public MethodSubNegotiationResults subNegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			MethodEncapsulation methodEncapsulation = 
					MethodEncapsulation.newNullInstance(socket); 
			return new MethodSubNegotiationResults(
					this.getMethod(), methodEncapsulation, null);
		}
		
	}

	private static final class UsernamePasswordMethodSubNegotiator
		extends MethodSubNegotiator {
		
		public UsernamePasswordMethodSubNegotiator() {
			super(Method.USERNAME_PASSWORD);
		}

		@Override
		public MethodSubNegotiationResults subNegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			String username = null;
			char[] password = null;
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				Request request = Request.newInstanceFrom(inputStream);
				Response response = null;
				username = request.getUsername();
				password = request.getPassword();
				UserRepository userRepository = 
						configuration.getSettings().getLastValue(
								Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY);
				User user = userRepository.get(username);
				if (user == null) {
					response = Response.newInstance(UnsignedByte.valueOf(
							(byte) 0x01));
					outputStream.write(response.toByteArray());
					outputStream.flush();
					throw new MethodSubNegotiationException(
							this.getMethod(), 
							String.format(
									"invalid username password from %s",
									socket));
				}
				HashedPassword hashedPassword = user.getHashedPassword();
				if (!hashedPassword.passwordEquals(password)) {
					response = Response.newInstance(UnsignedByte.valueOf(
							(byte) 0x01));
					outputStream.write(response.toByteArray());
					outputStream.flush();
					throw new MethodSubNegotiationException(
							this.getMethod(), 
							String.format(
									"invalid username password from %s",
									socket));					
				}
				response = Response.newInstance(Response.STATUS_SUCCESS);
				outputStream.write(response.toByteArray());
				outputStream.flush();
			} finally {
				if (password != null) { Arrays.fill(password, '\0'); } 
			}
			MethodEncapsulation methodEncapsulation = 
					MethodEncapsulation.newNullInstance(socket);
			return new MethodSubNegotiationResults(
					this.getMethod(), methodEncapsulation, username);
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
	
	public abstract MethodSubNegotiationResults subNegotiate(
			final Socket socket,
			final Configuration configuration) throws IOException;
	
}
