package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

final class ResolveCommandWorker extends CommandWorker {

	private final Logger logger;
	
	public ResolveCommandWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubnegotiationResults methSubnegotiationResults, 
			final Socks5Request socks5Req) {
		super(socks5Worker, methSubnegotiationResults, socks5Req);
		this.logger = LoggerFactory.getLogger(ResolveCommandWorker.class);
	}

	@Override
	public void run() {
		HostResolver hostResolver =	
				this.getSelectedRoute().getNetObjectFactory().newHostResolver();
		InetAddress inetAddress = null;
		Socks5Reply socks5Rep = null;		
		try {
			inetAddress = hostResolver.resolve(
					this.getDesiredDestinationAddress());
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
		int serverBoundPort = this.getDesiredDestinationPort();
		socks5Rep = Socks5Reply.newInstance(
				Reply.SUCCEEDED, 
				serverBoundAddress, 
				serverBoundPort);
		RuleContext ruleContext = this.newSocks5ReplyRuleContext(socks5Rep);
		this.setRuleContext(ruleContext);
		Rule applicableRule = this.getRules().firstAppliesTo(
				this.getRuleContext());
		if (applicableRule == null) {
			this.logger.error(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"No applicable rule found based on the following "
					+ "context: %s",
					this.getRuleContext()));				
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(socks5Rep);
			return;
		}		
		this.setApplicableRule(applicableRule);
		if (!this.canAllowSocks5Reply()) {
			return;
		}		
		this.sendSocks5Reply(socks5Rep);			
	}
	
}
