package jargyle.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public abstract class DatagramSocketInterface implements Closeable {
	
	public abstract void bind(SocketAddress addr) throws SocketException;
	
	public abstract void close();
	
	public abstract void connect(InetAddress address, int port);
	
	public abstract void connect(SocketAddress addr) throws SocketException;
	
	public abstract void disconnect();
	
	public abstract boolean getBroadcast() throws SocketException;
	
	public abstract InetAddress getInetAddress();
	
	public abstract InetAddress getLocalAddress();
	
	public abstract int getLocalPort();
	
	public abstract SocketAddress getLocalSocketAddress();
	
	public abstract int getPort();
	
	public abstract int getReceiveBufferSize() throws SocketException;
	
	public abstract SocketAddress getRemoteSocketAddress();
	
	public abstract boolean getReuseAddress() throws SocketException;
	
	public abstract int getSendBufferSize() throws SocketException;
	
	public abstract int getSoTimeout() throws SocketException;
	
	public abstract int getTrafficClass() throws SocketException;
	
	public abstract boolean isBound();
	
	public abstract boolean isClosed();
	
	public abstract boolean isConnected();
	
	public abstract void receive(DatagramPacket p) throws IOException;
	
	public abstract void send(DatagramPacket p) throws IOException;
	
	public abstract void setBroadcast(boolean on) throws SocketException;
	
	public abstract void setReceiveBufferSize(int size) throws SocketException;
	
	public abstract void setReuseAddress(boolean on) throws SocketException;
	
	public abstract void setSendBufferSize(int size) throws SocketException;
	
	public abstract void setSoTimeout(int timeout) throws SocketException;
	
	public abstract void setTrafficClass(int tc) throws SocketException;

}
