package jargyle.net.socks.server.v5;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import jargyle.net.socks.server.WorkerContext;

public class Socks5WorkerContext extends WorkerContext {
	
	public Socks5WorkerContext(final WorkerContext context) {
		super(context);
	}
	
	public final void writeThenFlush(final byte[] b) throws IOException {
		Socket clientSocket = this.getClientSocket();
		OutputStream clientOutputStream = clientSocket.getOutputStream();
		clientOutputStream.write(b);
		clientOutputStream.flush();
	}

}
