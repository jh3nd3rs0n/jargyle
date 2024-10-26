package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;

final class ResolveRequestWorker extends RequestWorker {

	private final Logger logger;
	
	public ResolveRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		super(socks5Worker, methSubNegotiationResults, req);
		this.logger = LoggerFactory.getLogger(ResolveRequestWorker.class);
	}

	@Override
	public void run() {
		HostResolver hostResolver =	
				this.getSelectedRoute().getNetObjectFactory().newHostResolver();
		InetAddress inetAddress = null;
		Reply rep = null;
		try {
			inetAddress = hostResolver.resolve(
					this.getDesiredDestinationAddress().toString());
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
			return;			
		} catch (IOException e) {
			if (ThrowableHelper.isOrHasInstanceOf(
					e, UnknownHostException.class)) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in resolving the hostname"), 
						e);
				this.sendReply(Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
				return;				
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in resolving the hostname"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return;
		}
		String serverBoundAddress = inetAddress.getHostAddress();
		Port serverBoundPort = this.getDesiredDestinationPort();
		rep = Reply.newSuccessInstance(
				Address.newInstanceFrom(serverBoundAddress),
				serverBoundPort);
		RuleContext ruleContext = this.newReplyRuleContext(rep);
		this.setRuleContext(ruleContext);
		Rule applicableRule = this.getRules().firstAppliesTo(
				this.getRuleContext());
		if (applicableRule == null) {
			this.logger.warn(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"No applicable rule found based on the following "
					+ "context: %s",
					this.getRuleContext()));				
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
			return;
		}		
		this.setApplicableRule(applicableRule);
		if (!this.canAllowReply()) {
			return;
		}		
		this.sendReply(rep);			
	}
	
}
