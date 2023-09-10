package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

class CommandWorker extends Socks5Worker {

	private final CommandWorkerContext commandWorkerContext;
	
	protected CommandWorker(
			final Socket clientSocket, final CommandWorkerContext context) {
		super(clientSocket, context);
		this.commandWorkerContext = context;
	}
	
	protected final boolean canAllowSocks5Reply(
			final Rule applicableRl,
			final RuleContext socks5ReplyRuleContext) {
		if (applicableRl == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(rep);
			return false;
		}
		if (!this.canApplyRule(applicableRl)) {
			return true;
		}
		FirewallAction firewallAction = applicableRl.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(rep);
			return false;			
		}
		LogAction firewallActionLogAction = 
				applicableRl.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSocks5ReplyWithinLimit(
					applicableRl, socks5ReplyRuleContext)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"SOCKS5 reply allowed based on the following "
								+ "rule and context: rule: %s context: %s",
								applicableRl,
								socks5ReplyRuleContext));					
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"SOCKS5 reply denied based on the following "
							+ "rule and context: rule: %s context: %s",
							applicableRl,
							socks5ReplyRuleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		Socks5Reply rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.sendSocks5Reply(rep);
		return false;
	}
	
	private boolean canAllowSocks5ReplyWithinLimit(
			final Rule applicableRl,
			final RuleContext socks5ReplyRuleContext) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRl.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRl.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							ObjectLogMessageHelper.objectLogMessage(
									this,
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRl,
									socks5ReplyRuleContext));
				}
				Socks5Reply rep = Socks5Reply.newFailureInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.sendSocks5Reply(rep);
				return false;				
			}
			this.commandWorkerContext.addBelowAllowLimitRule(applicableRl);				
		}		
		return true;
	}
	
	private boolean canApplyRule(final Rule applicableRl) {
		if (applicableRl.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS)) {
			return true;
		}
		if (applicableRl.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_PORT)) {
			return true;
		}
		return false;
	}
	
	protected final RuleContext newSocks5ReplyRuleContext(
			final Socks5Reply socks5Rep) {
		RuleContext socks5ReplyRuleContext = new RuleContext();
		Socket clientSocket = this.getClientSocket();
		MethodSubnegotiationResults methSubnegotiationResults = 
				this.commandWorkerContext.getMethodSubnegotiationResults();
		Socks5Request socks5Req = this.commandWorkerContext.getSocks5Request();
		socks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSocket.getInetAddress().getHostAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSocket.getLocalAddress().getHostAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_METHOD, 
				methSubnegotiationResults.getMethod().toString());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_USER, 
				methSubnegotiationResults.getUser());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_COMMAND, 
				socks5Req.getCommand().toString());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS, 
				socks5Req.getDesiredDestinationAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT, 
				Port.newInstance(socks5Req.getDesiredDestinationPort()));		
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS, 
				socks5Rep.getServerBoundAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_PORT, 
				Port.newInstance(socks5Rep.getServerBoundPort()));
		return socks5ReplyRuleContext;
	}
	
}
