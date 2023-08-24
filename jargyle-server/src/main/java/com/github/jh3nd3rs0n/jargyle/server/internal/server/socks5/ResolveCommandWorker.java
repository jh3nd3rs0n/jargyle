package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;

final class ResolveCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			ResolveCommandWorker.class);

	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	
	public ResolveCommandWorker(final CommandWorkerContext context) {
		super(context);
		String desiredDestinationAddr = context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
	}

	@Override
	public void run() throws IOException {
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();
		InetAddress inetAddress = null;
		Socks5Reply socks5Rep = null;		
		Rule applicableRule = this.commandWorkerContext.getApplicableRule();
		try {
			inetAddress = hostResolver.resolve(this.desiredDestinationAddress);
		} catch (UnknownHostException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(Reply.HOST_UNREACHABLE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return;			
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return;
		}
		String serverBoundAddress = inetAddress.getHostAddress();
		int serverBoundPort = desiredDestinationPort;
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				serverBoundAddress, 
				serverBoundPort);
		RuleContext socks5ReplyRuleContext = 
				this.commandWorkerContext.newSocks5ReplyRuleContext(
						socks5Rep);
		applicableRule = this.rules.firstAppliesTo(socks5ReplyRuleContext);
		if (!this.commandWorkerContext.canAllowSocks5Reply(
				this, applicableRule, socks5ReplyRuleContext, LOGGER)) {
			return;
		}		
		this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);			
	}
	
}
