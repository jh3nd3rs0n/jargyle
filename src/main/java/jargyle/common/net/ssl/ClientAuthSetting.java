package jargyle.common.net.ssl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLServerSocket;

import jargyle.common.annotation.HelpText;

public enum ClientAuthSetting {

	@HelpText(doc = "No client authentication desired", usage = "NOT_DESIRED")
	NOT_DESIRED {
		
		@Override
		public void applyTo(final SSLServerSocket sslServerSocket) {
			sslServerSocket.setNeedClientAuth(false);
			sslServerSocket.setWantClientAuth(false);
		}
		
	},
	
	@HelpText(doc = "Client authentication requested", usage = "REQUESTED")
	REQUESTED {
		
		@Override
		public void applyTo(final SSLServerSocket sslServerSocket) {
			sslServerSocket.setWantClientAuth(true);
		}
		
	},
	
	@HelpText(doc = "Client authentication required", usage = "REQUIRED")
	REQUIRED {
		
		@Override
		public void applyTo(final SSLServerSocket sslServerSocket) {
			sslServerSocket.setNeedClientAuth(true);
		}
		
	};
	
	public static ClientAuthSetting getInstance(final String s) {
		ClientAuthSetting clientAuthSetting = null;
		try {
			clientAuthSetting = ClientAuthSetting.valueOf(s);
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			List<ClientAuthSetting> list = Arrays.asList(
					ClientAuthSetting.values());
			for (Iterator<ClientAuthSetting> iterator = list.iterator();
					iterator.hasNext();) {
				ClientAuthSetting value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected client authentication setting must be "
							+ "one of the following values: %s. actual value "
							+ "is %s",
							sb.toString(),
							s), 
					e);
		}
		return clientAuthSetting;
	}
	
	public abstract void applyTo(final SSLServerSocket sslServerSocket);
	
}
