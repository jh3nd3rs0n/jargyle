package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordRequest;
import jargyle.common.net.socks5.usernamepasswordauth.UsernamePasswordResponse;

enum UsernamePasswordAuthenticator implements Authenticator {

	INSTANCE;

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
		char[] password = usernamePassword.getEncryptedPassword().getPassword();
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
			throw new IOException("invalid username/password");
		}
		return socket;
	}

	@Override
	public String toString() {
		return UsernamePasswordAuthenticator.class.getSimpleName();
	}
	
}
