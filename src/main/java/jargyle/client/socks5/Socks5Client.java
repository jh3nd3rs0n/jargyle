package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.SocksClient;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.ClientMethodSelectionMessage;
import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.ServerMethodSelectionMessage;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;

public final class Socks5Client extends SocksClient {

	public static final class Builder extends SocksClient.Builder {
		
		private AuthMethods authMethods;
		private Oid gssapiMechanismOid;
		private boolean gssapiNecReferenceImpl;
		private GssapiProtectionLevels gssapiProtectionLevels;
		private String gssapiServiceName;
		private UsernamePassword usernamePassword;
		
		public Builder(final Socks5ServerUri serverUri) {
			super(serverUri);
			this.authMethods = DEFAULT_AUTH_METHODS;
			this.gssapiMechanismOid = DEFAULT_GSSAPI_MECHANISM_OID;
			this.gssapiNecReferenceImpl = DEFAULT_GSSAPI_NEC_REFERENCE_IMPL;
			this.gssapiProtectionLevels = DEFAULT_GSSAPI_PROTECTION_LEVELS;
			this.gssapiServiceName = null;
			this.usernamePassword = null;			
		}

		public Builder authMethods(final AuthMethods authMthds) {
			this.authMethods = authMthds;
			return this;
		}
		
		@Override
		public Socks5Client build() {
			return new Socks5Client(this);
		}
		
		@Override
		public Builder connectTimeout(final int i) {
			super.connectTimeout(i);
			return this;
		}
		
		@Override
		public Builder fromSystemProperties() {
			super.fromSystemProperties();
			String authMethodsProperty = System.getProperty(
					"socksClient.socks5.authMethods");
			if (authMethodsProperty != null) {
				AuthMethods authMethods = 
						AuthMethods.newInstance(authMethodsProperty);
				this.authMethods(authMethods);
			}
			String gssapiMechanismOidProperty = System.getProperty(
					"socksClient.socks5.gssapiMechanismOid");
			if (gssapiMechanismOidProperty != null) {
				Oid gssapiMechanismOid;
				try {
					gssapiMechanismOid = new Oid(gssapiMechanismOidProperty);
				} catch (GSSException e) {
					throw new IllegalArgumentException(e);
				}
				this.gssapiMechanismOid(gssapiMechanismOid);
			}
			String gssapiNecReferenceImplProperty = System.getProperty(
					"socksClient.socks5.gssapiNecReferenceImpl");
			if (gssapiNecReferenceImplProperty != null) {
				boolean gssapiNecReferenceImpl = Boolean.valueOf(
						gssapiNecReferenceImplProperty);
				this.gssapiNecReferenceImpl(gssapiNecReferenceImpl);
			}
			String gssapiProtectionLevelsProperty = System.getProperty(
					"socksClient.socks5.gssapiProtectionLevels");
			if (gssapiProtectionLevelsProperty != null) {
				GssapiProtectionLevels gssapiProtectionLevels =
						GssapiProtectionLevels.newInstance(
								gssapiProtectionLevelsProperty);
				this.gssapiProtectionLevels(gssapiProtectionLevels);
			}
			String gssapiServiceNameProperty = System.getProperty(
					"socksClient.socks5.gssapiServiceName");
			if (gssapiServiceNameProperty != null) {
				this.gssapiServiceName(gssapiServiceNameProperty);
			}
			String usernameProperty = System.getProperty(
					"socksClient.socks5.username");
			String passwordProperty = System.getProperty(
					"socksClient.socks5.password");
			if (usernameProperty != null && passwordProperty != null) {
				usernamePassword = UsernamePassword.newInstance(
						usernameProperty, passwordProperty.toCharArray());
				this.usernamePassword(usernamePassword);
			}
			return this;
		}
		
		public Builder gssapiMechanismOid(final Oid mechanismOid) {
			this.gssapiMechanismOid = mechanismOid;
			return this;
		}
		
		public Builder gssapiNecReferenceImpl(final boolean necReferenceImpl) {
			this.gssapiNecReferenceImpl = necReferenceImpl;
			return this;
		}
		
		public Builder gssapiProtectionLevels(
				final GssapiProtectionLevels protectionLevels) {
			this.gssapiProtectionLevels = protectionLevels;
			return this;
		}
		
		public Builder gssapiServiceName(final String serviceName) {
			this.gssapiServiceName = serviceName;
			return this;
		}
		
		@Override
		public Builder socketSettings(final SocketSettings s) {
			super.socketSettings(s);
			return this;
		}
		
		public Builder usernamePassword(final UsernamePassword usrnmPsswrd) {
			this.usernamePassword = usrnmPsswrd;
			return this;
		}
		
	}
	
	public static final AuthMethods DEFAULT_AUTH_METHODS = 
			AuthMethods.newInstance(AuthMethod.NO_AUTHENTICATION_REQUIRED);
	public static final Oid DEFAULT_GSSAPI_MECHANISM_OID;
	public static final boolean DEFAULT_GSSAPI_NEC_REFERENCE_IMPL = false;
	public static final GssapiProtectionLevels DEFAULT_GSSAPI_PROTECTION_LEVELS =
			GssapiProtectionLevels.DEFAULT_INSTANCE;
	
	static {
		try {
			DEFAULT_GSSAPI_MECHANISM_OID = new Oid("1.2.840.113554.1.2.2");
		} catch (GSSException e) {
			throw new AssertionError(e);
		}
	}
	
	private final AuthMethods authMethods;
	private final Oid gssapiMechanismOid;
	private final boolean gssapiNecReferenceImpl;
	private final GssapiProtectionLevels gssapiProtectionLevels;
	private final String gssapiServiceName;
	private final UsernamePassword usernamePassword;
	
	private Socks5Client(final Builder builder) {
		super(builder);
		AuthMethods authMthds = builder.authMethods;
		Oid mechanismOid = builder.gssapiMechanismOid;
		boolean necReferenceImpl = builder.gssapiNecReferenceImpl;
		GssapiProtectionLevels protectionLevels = 
				builder.gssapiProtectionLevels;
		String serviceName = builder.gssapiServiceName;
		UsernamePassword usrnmPsswrd = builder.usernamePassword;
		if (authMthds == null) {
			authMthds = DEFAULT_AUTH_METHODS;
		}
		if (mechanismOid == null) {
			mechanismOid = DEFAULT_GSSAPI_MECHANISM_OID;
		}
		if (protectionLevels == null) {
			protectionLevels = DEFAULT_GSSAPI_PROTECTION_LEVELS;
		}
		if (serviceName == null) {
			if (authMthds.toList().contains(AuthMethod.GSSAPI)) {
				throw new IllegalStateException(
						"GSS-API service name required");
			}
		}
		this.authMethods = authMthds;
		this.gssapiMechanismOid = mechanismOid;
		this.gssapiNecReferenceImpl = necReferenceImpl;
		this.gssapiProtectionLevels = protectionLevels;
		this.gssapiServiceName = serviceName;
		this.usernamePassword = usrnmPsswrd;		
	}

	public Socket authenticate(final Socket socket) throws IOException {
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();
		Set<Method> methods = new TreeSet<Method>();
		for (AuthMethod authMethod : this.authMethods.toList()) {
			methods.add(authMethod.getMethod());
		}
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(
						methods);
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		Method method = smsm.getMethod();
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOf(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		Socket newSocket = authenticator.authenticate(socket, this);
		return newSocket;
	}
	
	public AuthMethods getAuthMethods() {
		return this.authMethods;
	}
	
	public Oid getGssapiMechanismOid() {
		return this.gssapiMechanismOid;
	}
	
	public GssapiProtectionLevels getGssapiProtectionLevels() {
		return this.gssapiProtectionLevels;
	}
	
	public String getGssapiServiceName() {
		return this.gssapiServiceName;
	}
	
	public UsernamePassword getUsernamePassword() {
		return this.usernamePassword;
	}
	
	public boolean isGssapiNecReferenceImpl() {
		return this.gssapiNecReferenceImpl;
	}

	@Override
	public Socks5DatagramSocketFactory newDatagramSocketFactory() {
		return new Socks5DatagramSocketFactory(this);
	}

	@Override
	public Socks5ServerSocketFactory newServerSocketFactory() {
		return new Socks5ServerSocketFactory(this);
	}

	@Override
	public Socks5SocketFactory newSocketFactory() {
		return new Socks5SocketFactory(this);
	}
	
}
