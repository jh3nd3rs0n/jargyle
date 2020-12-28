package jargyle.common.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class SocketInterface implements Closeable {
	
	public abstract void bind(SocketAddress bindpoint) throws IOException;
	
	@Override
	public abstract void close() throws IOException;
	
	public abstract void connect(SocketAddress endpoint) throws IOException;
	
	public abstract void connect(
			SocketAddress endpoint, int timeout) throws IOException;
	
	public abstract InetAddress getInetAddress();
	
	public abstract InputStream getInputStream() throws IOException;
	
	public abstract boolean getKeepAlive() throws SocketException;
	
	public abstract InetAddress getLocalAddress();
	
	public abstract int getLocalPort();
	
	public abstract SocketAddress getLocalSocketAddress();
	
	public abstract boolean getOOBInline() throws SocketException;
	
	public abstract OutputStream getOutputStream() throws IOException;
	
	public abstract int getPort();
	
	public abstract int getReceiveBufferSize() throws SocketException;
	
	public abstract SocketAddress getRemoteSocketAddress();
	
	public abstract boolean getReuseAddress() throws SocketException;
	
	public abstract int getSendBufferSize() throws SocketException;
	
	public abstract int getSoLinger() throws SocketException;
	
	public abstract int getSoTimeout() throws SocketException;
	
	public abstract boolean getTcpNoDelay() throws SocketException;
	
	public abstract int getTrafficClass() throws SocketException;
	
	public abstract boolean isBound();
	
	public abstract boolean isClosed();
	
	public abstract boolean isConnected();
	
	public abstract boolean isInputShutdown();
	
	public abstract boolean isOutputShutdown();
	
	public abstract void sendUrgentData(int data) throws IOException;
	
	public abstract void setKeepAlive(boolean on) throws SocketException;
	
	public abstract void setOOBInline(boolean on) throws SocketException;
	
	public abstract void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth);
	
	public abstract void setReceiveBufferSize(int size) throws SocketException;
	
	public abstract void setReuseAddress(boolean on) throws SocketException;
	
	public abstract void setSendBufferSize(int size) throws SocketException;
	
	public abstract void setSoLinger(boolean on, int linger) throws SocketException;
	
	public abstract void setSoTimeout(int timeout) throws SocketException;
	
	public abstract void setTcpNoDelay(boolean on) throws SocketException;
	
	public abstract void setTrafficClass(int tc) throws SocketException;
	
	public abstract void shutdownInput() throws IOException;
	
	public abstract void shutdownOutput() throws IOException;

}
