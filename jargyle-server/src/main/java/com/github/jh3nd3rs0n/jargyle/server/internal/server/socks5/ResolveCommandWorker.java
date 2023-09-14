package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
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

	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final Logger logger;
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	
	public ResolveCommandWorker(
			final Socket clientSocket, final CommandWorkerContext context) {
		super(clientSocket, context);
		String desiredDestinationAddr = context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.logger = LoggerFactory.getLogger(ResolveCommandWorker.class);
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
	}

	@Override
	public void run() {
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();
		InetAddress inetAddress = null;
		Socks5Reply socks5Rep = null;		
		try {
			inetAddress = hostResolver.resolve(this.desiredDestinationAddress);
		} catch (UnknownHostException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(Reply.HOST_UNREACHABLE);
			this.sendSocks5Reply(socks5Rep);
			return;			
		} catch (IOException e) {
			this.logger.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.sendSocks5Reply(socks5Rep);
			return;
		}
		String serverBoundAddress = inetAddress.getHostAddress();
		int serverBoundPort = desiredDestinationPort;
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				serverBoundAddress, 
				serverBoundPort);
		RuleContext ruleContext = this.newSocks5ReplyRuleContext(socks5Rep);
		this.commandWorkerContext.setRuleContext(ruleContext);
		Rule applicableRule = this.rules.firstAppliesTo(
				this.commandWorkerContext.getRuleContext());
		if (applicableRule == null) {
			this.logger.error(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"No applicable rule found based on the following "
					+ "context: %s",
					this.commandWorkerContext.getRuleContext()));				
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(socks5Rep);
			return;
		}		
		this.commandWorkerContext.setApplicableRule(applicableRule);
		if (!this.canAllowSocks5Reply()) {
			return;
		}		
		this.sendSocks5Reply(socks5Rep);			
	}
	
}
