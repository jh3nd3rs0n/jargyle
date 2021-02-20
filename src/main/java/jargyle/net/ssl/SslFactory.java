package jargyle.net.ssl;

import java.io.IOException;

public abstract class SslFactory {

	public abstract SslSocketFactory newSslSocketFactory() throws IOException;
	
}
