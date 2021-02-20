package jargyle.net.ssl;

import java.io.IOException;

public abstract class SslFactory {

	public abstract DtlsDatagramSocketFactory newDtlsDatagramSocketFactory() 
			throws IOException;
	
	public abstract SslSocketFactory newSslSocketFactory() throws IOException;
	
}
