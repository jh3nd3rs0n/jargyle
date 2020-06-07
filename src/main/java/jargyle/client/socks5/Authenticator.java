package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import jargyle.common.net.socks5.Method;
import jargyle.common.net.socks5.gssapiauth.GssSocket;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevel;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.socks5.gssapiauth.Message;
import jargyle.common.net.socks5.gssapiauth.MessageType;
import jargyle.common.net.socks5.gssapiauth.ProtectionLevel;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordRequest;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordResponse;

enum Authenticator {

	DEFAULT_AUTHENTICATOR(Method.NO_ACCEPTABLE_METHODS) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			throw new IOException("no acceptable authentication methods");
		}
		
	},
	
	GSSAPI_AUTHENTICATOR(Method.GSSAPI) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			GSSContext context = this.newContext(socks5Client);
			this.establishContext(socket, context, socks5Client);
			GssapiProtectionLevel gssapiProtectionLevelSelection =
					this.negotiateProtectionLevel(
							socket, context, socks5Client);
			MessageProp msgProp = 
					gssapiProtectionLevelSelection.newMessageProp();
			Socket newSocket = new GssSocket(socket, context, msgProp);
			return newSocket;
		}
		
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
		
		private GssapiProtectionLevel negotiateProtectionLevel(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			boolean necReferenceImpl = socks5Client.isGssapiNecReferenceImpl();
			GssapiProtectionLevels gssapiProtectionLevels = 
					socks5Client.getGssapiProtectionLevels();
			List<GssapiProtectionLevel> gssapiProtectionLevelList = 
					gssapiProtectionLevels.toList(); 
			GssapiProtectionLevel firstGssapiProtectionLevel = 
					gssapiProtectionLevelList.get(0);
			byte[] token = new byte[] { 
					firstGssapiProtectionLevel.protectionLevelValue().byteValue() 
			};
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
				protectionLevelSelection = ProtectionLevel.valueOf(token[0]);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			GssapiProtectionLevel gssapiProtectionLevelSelection = null;
			try {
				gssapiProtectionLevelSelection = GssapiProtectionLevel.valueOf(
						protectionLevelSelection);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			if (!gssapiProtectionLevelList.contains(
					gssapiProtectionLevelSelection)) {
				throw new IOException(String.format(
						"server selected %s which is not acceptable by this socket", 
						protectionLevelSelection));
			}
			return gssapiProtectionLevelSelection;
		}
		
		private GSSContext newContext(
				final Socks5Client socks5Client) throws IOException {
			String server = socks5Client.getGssapiServiceName();
			GSSManager manager = GSSManager.getInstance();
			GSSName serverName = null;
			try {
				serverName = manager.createName(server, null);
			} catch (GSSException e) {
				throw new IOException(e);
			}
			Oid mechanismOid = socks5Client.getGssapiMechanismOid();
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
		
	},
	
	PERMISSIVE_AUTHENTICATOR(Method.NO_AUTHENTICATION_REQUIRED) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			return socket;
		}
		
	},
	
	USERNAME_PASSWORD_AUTHENTICATOR(Method.USERNAME_PASSWORD) {
		
		@Override
		public Socket authenticate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			UsernamePassword usernamePassword = null;
			UsernamePasswordRequestor usernamePasswordRequestor = 
					UsernamePasswordRequestor.getDefault();
			if (usernamePasswordRequestor != null) {
				Socks5ServerUri socksServerUri = 
						(Socks5ServerUri) socks5Client.getSocksServerUri();
				String prompt = String.format(
						"Please enter username and password for %s on port %s", 
						socksServerUri.getHost(), 
						socksServerUri.getPort());
				usernamePassword = 
						usernamePasswordRequestor.requestUsernamePassword(
								socksServerUri, prompt);
			}
			if (usernamePassword == null) {
				usernamePassword = socks5Client.getUsernamePassword();
			}
			if (usernamePassword == null) {
				String username = System.getProperty("user.name");
				usernamePassword = UsernamePassword.newInstance(
						username, new char[] { });
			}
			String username = usernamePassword.getUsername();
			char[] password = 
					usernamePassword.getEncryptedPassword().getPassword();
			UsernamePasswordRequest usernamePasswordReq = 
					UsernamePasswordRequest.newInstance(
							username, 
							password);
			outputStream.write(usernamePasswordReq.toByteArray());
			outputStream.flush();
			Arrays.fill(password, '\0');
			UsernamePasswordResponse usernamePasswordResp = 
					UsernamePasswordResponse.newInstanceFrom(inputStream);
			if (usernamePasswordResp.getStatus() != 
					UsernamePasswordResponse.STATUS_SUCCESS) {
				throw new IOException("invalid username password");
			}
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
			final Socks5Client socks5Client) throws IOException;
	
	public Method methodValue() {
		return this.methodValue;
	}
	
}
