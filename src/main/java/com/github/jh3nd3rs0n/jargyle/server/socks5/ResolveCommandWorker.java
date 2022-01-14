package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallRule;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class ResolveCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ResolveCommandWorker.class);

	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;	
	private final NetObjectFactory netObjectFactory;
	private final Socks5Request socks5Request;	
	
	public ResolveCommandWorker(final CommandWorkerContext context) {
		super(context);
		Socket clientFacingSock = context.getClientFacingSocket();		
		String desiredDestinationAddr = context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();		
		NetObjectFactory netObjFactory = 
				context.getRoute().getNetObjectFactory();
		Socks5Request socks5Req = context.getSocks5Request();
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;		
		this.netObjectFactory = netObjFactory;
		this.socks5Request = socks5Req;		
	}

	@Override
	public void run() throws IOException {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();
		InetAddress inetAddress = null;
		try {
			inetAddress = hostResolver.resolve(this.desiredDestinationAddress);
		} catch (UnknownHostException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(Reply.HOST_UNREACHABLE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
			return;			
		} catch (IOException e) {
			LOGGER.error( 
					LoggerHelper.objectMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
			return;
		}
		String serverBoundAddress = inetAddress.getHostAddress();
		int serverBoundPort = desiredDestinationPort;
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				serverBoundAddress, 
				serverBoundPort);
		FirewallRule.Context context = new Socks5ReplyFirewallRule.Context(
				this.clientFacingSocket.getInetAddress().getHostAddress(),
				this.clientFacingSocket.getLocalAddress().getHostAddress(),
				this.methodSubnegotiationResults,
				this.socks5Request,
				socks5Rep); 
		if (!this.commandWorkerContext.canAllowSocks5Reply(this, context)) {
			return;
		}		
		this.commandWorkerContext.sendSocks5Reply(this, socks5Rep);
	}
	
}
