package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonNegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;

class CommandWorker extends Socks5Worker {

	private final MethodSubNegotiationResults methodSubNegotiationResults;
	private final Request request;
	
	protected CommandWorker(
			final Socks5Worker socks5Worker,
			final MethodSubNegotiationResults methSubNegotiationResults,
			final Request req) {
		super(socks5Worker);
		Objects.requireNonNull(methSubNegotiationResults);
		Objects.requireNonNull(req);
		this.methodSubNegotiationResults = methSubNegotiationResults;
		this.request = req;
	}
	
	protected final boolean canAllowReply() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		if (!this.hasReplyRuleCondition()) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
			return false;			
		}
		LogAction firewallActionLogAction =	
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowReplyWithinLimit()) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"SOCKS5 reply allowed based on the following "
								+ "rule and context: rule: %s context: %s",
								applicableRule,
								ruleContext));					
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"SOCKS5 reply denied based on the following "
							+ "rule and context: rule: %s context: %s",
							applicableRule,
							ruleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		this.sendReply(
				Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
		return false;
	}
	
	private boolean canAllowReplyWithinLimit() {
		Rule applicableRule = this.getApplicableRule();
		RuleContext ruleContext = this.getRuleContext();
		NonNegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
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
									applicableRule,
									ruleContext));
				}
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return false;				
			}
			this.addBelowAllowLimitRule(applicableRule);				
		}		
		return true;
	}
	
	protected final Command getCommand() {
		return this.request.getCommand();
	}
	
	protected final Address getDesiredDestinationAddress() {
		return this.request.getDesiredDestinationAddress();
	}
	
	protected final Port getDesiredDestinationPort() {
		return this.request.getDesiredDestinationPort();
	}
	
	protected final MethodSubNegotiationResults getMethodSubNegotiationResults() {
		return this.methodSubNegotiationResults;
	}
	
	protected final Request getRequest() {
		return this.request;
	}
	
	private boolean hasReplyRuleCondition() {
		Rule applicableRule = this.getApplicableRule();
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_REPLY_SERVER_BOUND_ADDRESS)) {
			return true;
		}
		if (applicableRule.hasRuleCondition(
				Socks5RuleConditionSpecConstants.SOCKS5_REPLY_SERVER_BOUND_PORT)) {
			return true;
		}
		return false;
	}
	
	protected final RuleContext newReplyRuleContext(
			final Reply rep) {
		RuleContext replyRuleContext = new RuleContext(
				this.getRuleContext());
		replyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_REPLY_SERVER_BOUND_ADDRESS,
				rep.getServerBoundAddress().toString());
		replyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_REPLY_SERVER_BOUND_PORT,
				rep.getServerBoundPort());
		return replyRuleContext;
	}
	
}
