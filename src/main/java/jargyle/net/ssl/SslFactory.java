package jargyle.net.ssl;

public abstract class SslFactory {

	public abstract DtlsDatagramSocketFactory newDtlsDatagramSocketFactory();
	
	public abstract SslSocketFactory newSslSocketFactory();
	
}
