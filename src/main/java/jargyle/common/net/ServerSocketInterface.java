package jargyle.common.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class ServerSocketInterface implements Closeable {

	public abstract SocketInterface accept() throws IOException;
	
	public abstract void bind(SocketAddress endpoint) throws IOException;
	
	public abstract void bind(
			SocketAddress endpoint, int backlog) throws IOException;
	
	@Override
	public abstract void close() throws IOException;
	
	public abstract InetAddress getInetAddress();
	
	public abstract int getLocalPort();
	
	public abstract SocketAddress getLocalSocketAddress();
	
	public abstract int getReceiveBufferSize() throws SocketException;
	
	public abstract boolean getReuseAddress() throws SocketException;
	
	public abstract int getSoTimeout() throws IOException;
	
	public abstract boolean isBound();
	
	public abstract boolean isClosed();
	
	public abstract void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth);
	
	public abstract void setReceiveBufferSize(int size) throws SocketException;
	
	public abstract void setReuseAddress(boolean on) throws SocketException;
	
	public abstract void setSoTimeout(int timeout) throws SocketException;
	
	public abstract String toString();
	
}
