package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.LogMessageSource;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Route;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.net.Socket;

final class RequestHandlerContext {
    
    private final MethodSubNegotiationResults methodSubNegotiationResults;
    private final Request request;
    private final ServerEventLogger serverEventLogger;
    private final Socks5ConnectionHandlerContext socks5ConnectionHandlerContext;
    
    public RequestHandlerContext(
            final Socks5ConnectionHandlerContext handlerContext,
            final MethodSubNegotiationResults methSubNegotiationResults,
            final Request req,
            final ServerEventLogger logger) {
        this.methodSubNegotiationResults = methSubNegotiationResults;
        this.request = req;
        this.serverEventLogger = logger;
        this.socks5ConnectionHandlerContext = handlerContext;
    }

    public void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
        this.socks5ConnectionHandlerContext.addBelowAllowLimitRule(belowAllowLimitRl);
    }

    public boolean canAllowReply() {
        if (!this.hasReplyRuleCondition()) {
            return true;
        }
        Rule applicableRule = this.getApplicableRule();
        RuleContext ruleContext = this.getRuleContext();
        FirewallAction firewallAction = applicableRule.getLastRuleActionValue(
                GeneralRuleActionSpecConstants.FIREWALL_ACTION);
        if (firewallAction == null) {
            this.sendReply(
                    Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
            return false;
        }
        LogAction firewallActionLogAction =
                applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_LOG_ACTION);
        if (firewallAction.equals(FirewallAction.ALLOW)) {
            if (!this.canAllowReplyWithinLimit()) {
                return false;
            }
            if (firewallActionLogAction != null) {
                firewallActionLogAction.invoke(
                        ObjectLogMessageHelper.objectLogMessage(
                                this.getLogMessageSource(),
                                "SOCKS5 reply allowed based on the following "
                                        + "rule and context: rule: %s context: %s",
                                applicableRule,
                                ruleContext));
            }
        } else if (firewallAction.equals(FirewallAction.DENY)
                && firewallActionLogAction != null) {
            firewallActionLogAction.invoke(
                    ObjectLogMessageHelper.objectLogMessage(
                            this.getLogMessageSource(),
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
                applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
        LogAction firewallActionAllowLimitReachedLogAction =
                applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
        if (firewallActionAllowLimit != null) {
            if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
                if (firewallActionAllowLimitReachedLogAction != null) {
                    firewallActionAllowLimitReachedLogAction.invoke(
                            ObjectLogMessageHelper.objectLogMessage(
                                    this.getLogMessageSource(),
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

    public Rule getApplicableRule() {
        return this.socks5ConnectionHandlerContext.getApplicableRule();
    }

    public DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
        return this.socks5ConnectionHandlerContext.getClientFacingDtlsDatagramSocketFactory();
    }

    public Socket getClientSocket() {
        return this.socks5ConnectionHandlerContext.getClientSocket();
    }

    public Address getDesiredDestinationAddress() {
        return this.request.getDesiredDestinationAddress();
    }

    public Port getDesiredDestinationPort() {
        return this.request.getDesiredDestinationPort();
    }

    private LogMessageSource getLogMessageSource() {
        return this.socks5ConnectionHandlerContext.getLogMessageSource();
    }

    public MethodSubNegotiationResults getMethodSubNegotiationResults() {
        return this.methodSubNegotiationResults;
    }

    public RuleContext getRuleContext() {
        return this.socks5ConnectionHandlerContext.getRuleContext();
    }

    public Rules getRules() {
        return this.socks5ConnectionHandlerContext.getRules();
    }

    public Route getSelectedRoute() {
        return this.socks5ConnectionHandlerContext.getSelectedRoute();
    }

    public ServerEventLogger getServerEventLogger() {
        return this.serverEventLogger;
    }

    public Settings getSettings() {
        return this.socks5ConnectionHandlerContext.getSettings();
    }

    private boolean hasReplyRuleCondition() {
        Rule applicableRule = this.getApplicableRule();
        if (applicableRule.hasRuleCondition(
                Socks5RuleConditionSpecConstants.SOCKS5_REPLY_SERVER_BOUND_ADDRESS)) {
            return true;
        }
        return applicableRule.hasRuleCondition(
                Socks5RuleConditionSpecConstants.SOCKS5_REPLY_SERVER_BOUND_PORT);
    }

    public RuleContext newReplyRuleContext(final Reply rep) {
        return this.socks5ConnectionHandlerContext.newReplyRuleContext(rep);
    }

    public boolean sendReply(final Reply rep) {
        return this.socks5ConnectionHandlerContext.sendReply(rep);
    }

    public void setApplicableRule(final Rule applicableRl) {
        this.socks5ConnectionHandlerContext.setApplicableRule(applicableRl);
    }

    public void setLogMessageSource(final LogMessageSource source) {
        this.socks5ConnectionHandlerContext.setLogMessageSource(source);
    }

    public void setRuleContext(final RuleContext rlContext) {
        this.socks5ConnectionHandlerContext.setRuleContext(rlContext);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getClientSocket()=" +
                this.getClientSocket() +
                "]";
    }
    
}
