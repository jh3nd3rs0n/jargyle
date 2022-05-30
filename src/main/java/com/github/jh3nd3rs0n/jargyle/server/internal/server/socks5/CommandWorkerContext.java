package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.net.Socket;
import java.util.Objects;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressAndPortHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

final class CommandWorkerContext extends Socks5WorkerContext {

	private final Rule applicableRule;
	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final Socks5Request socks5Request;
	private final RuleContext socks5RequestRuleContext;
	
	public CommandWorkerContext(
			final Socks5WorkerContext context,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final Rule applicableRl,
			final RuleContext socks5ReqRuleContext) {
		super(context);
		Objects.requireNonNull(methSubnegotiationResults);
		Objects.requireNonNull(socks5Req);
		Objects.requireNonNull(applicableRl);
		Objects.requireNonNull(socks5ReqRuleContext);
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.socks5Request = socks5Req;
		this.applicableRule = applicableRl;
		this.socks5RequestRuleContext = socks5ReqRuleContext;		
	}
	
	public boolean canAllowSocks5Reply(
			final Object worker,
			final Rule applicableRule,
			final RuleContext socks5ReplyRuleContext, 
			final Logger logger) {
		if (applicableRule == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(worker, rep, logger);
			return false;
		}
		boolean hasServerBoundAddressRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS);
		boolean hasServerBoundPortRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT);
		if (!hasServerBoundAddressRuleCondition 
				&& !hasServerBoundPortRuleCondition) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(worker, rep, logger);
			return false;			
		}
		LogAction firewallActionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		String serverBoundAddress = socks5ReplyRuleContext.getRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS);
		Port serverBoundPort = socks5ReplyRuleContext.getRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_PORT);		
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSocks5ReplyWithinLimit(
					worker, applicableRule, socks5ReplyRuleContext, logger)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						logger, 
						ObjectLogMessageHelper.objectLogMessage(
								worker,
								"Server bound address and port (%s) allowed "
								+ "based on the following rule and context: "
								+ "rule: %s context: %s",
								AddressAndPortHelper.toString(
										serverBoundAddress,
										serverBoundPort.intValue()),
								applicableRule,
								socks5ReplyRuleContext));					
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					logger, 
					ObjectLogMessageHelper.objectLogMessage(
							worker,
							"Server bound address and port (%s) denied based "
							+ "on the following rule and context: rule: %s "
							+ "context: %s",
							AddressAndPortHelper.toString(
									serverBoundAddress,
									serverBoundPort.intValue()),
							applicableRule,
							socks5ReplyRuleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		Socks5Reply rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.sendSocks5Reply(worker, rep, logger);
		return false;
	}
	
	private boolean canAllowSocks5ReplyWithinLimit(
			final Object worker,
			final Rule applicableRule,
			final RuleContext socks5ReplyRuleContext, 
			final Logger logger) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (firewallActionAllowLimit.hasBeenReached()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							logger, 
							ObjectLogMessageHelper.objectLogMessage(
									worker,
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									socks5ReplyRuleContext));
				}
				Socks5Reply rep = Socks5Reply.newFailureInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.sendSocks5Reply(worker, rep, logger);
				return false;				
			}
			firewallActionAllowLimit.incrementCurrentCount();
			this.addBelowAllowLimitRule(applicableRule);				
		}		
		return true;
	}
	
	public Rule getApplicableRule() {
		return this.applicableRule;
	}
	
	public Command getCommand() {
		return this.socks5Request.getCommand();
	}
	
	public String getDesiredDestinationAddress() {
		return this.socks5Request.getDesiredDestinationAddress();
	}
	
	public int getDesiredDestinationPort() {
		return this.socks5Request.getDesiredDestinationPort();
	}
	
	public MethodSubnegotiationResults getMethodSubnegotiationResults() {
		return this.methodSubnegotiationResults;
	}
	
	public Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
	public RuleContext getSocks5RequestRuleContext() {
		return this.socks5RequestRuleContext;
	}
	
	public RuleContext newSocks5ReplyRuleContext(
			final Socks5Reply socks5Rep) {
		RuleContext socks5ReplyRuleContext = new RuleContext();
		Socket clientFacingSock = this.getClientFacingSocket();
		MethodSubnegotiationResults methSubnegotiationResults = 
				this.getMethodSubnegotiationResults();
		Socks5Request socks5Req = this.getSocks5Request();
		socks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientFacingSock.getInetAddress().getHostAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientFacingSock.getLocalAddress().getHostAddress());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_METHOD, 
				methSubnegotiationResults.getMethod());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_USER, 
				methSubnegotiationResults.getUser());
		socks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_COMMAND, 
				socks5Req.getCommand());
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
