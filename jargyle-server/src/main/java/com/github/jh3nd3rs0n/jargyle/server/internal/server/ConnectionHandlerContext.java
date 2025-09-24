package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.*;

import java.net.Socket;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

final class ConnectionHandlerContext {

    private Rule applicableRule;
    private final Set<Rule> belowAllowLimitRules;
    private Socket clientSocket;
    private final ConfiguredObjects configuredObjects;
    private RuleContext ruleContext;
    private Route selectedRoute;
    private final ServerEventLogger serverEventLogger;

    public ConnectionHandlerContext(
            final Socket clientSock,
            final ConfiguredObjects configuredObjs,
            final ServerEventLogger logger) {
        this.applicableRule = null;
        this.belowAllowLimitRules = new HashSet<>();
        this.clientSocket = clientSock;
        this.configuredObjects = configuredObjs;
        this.ruleContext = null;
        this.selectedRoute = null;
        this.serverEventLogger = logger;
    }

    public void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
        FirewallAction firewallAction =
                belowAllowLimitRl.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION);
        if (firewallAction == null
                || !firewallAction.equals(FirewallAction.ALLOW)) {
            throw new IllegalArgumentException(String.format(
                    "rule must have a rule action of a firewall action of %s",
                    FirewallAction.ALLOW));
        }
        NonNegativeIntegerLimit firewallActionAllowLimit =
                belowAllowLimitRl.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
        if (firewallActionAllowLimit == null) {
            throw new IllegalArgumentException(
                    "rule must have a rule action of a firewall action allow "
                            + "limit");
        }
        this.belowAllowLimitRules.add(Objects.requireNonNull(
                belowAllowLimitRl));
    }

    public void decrementAllCurrentAllowedCounts() {
        for (Rule belowAllowLimitRule : this.belowAllowLimitRules) {
            FirewallAction firewallAction =
                    belowAllowLimitRule.getLastRuleActionValue(
                            GeneralRuleActionSpecConstants.FIREWALL_ACTION);
            NonNegativeIntegerLimit firewallActionAllowLimit =
                    belowAllowLimitRule.getLastRuleActionValue(
                            GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
            if (firewallAction != null
                    && firewallAction.equals(FirewallAction.ALLOW)
                    && firewallActionAllowLimit != null) {
                firewallActionAllowLimit.decrementCurrentCount();
            }
        }
    }

    public Rule getApplicableRule() {
        return this.applicableRule;
    }

    public DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
        return this.configuredObjects.getClientFacingDtlsDatagramSocketFactory();
    }

    public SslSocketFactory getClientSslSocketFactory() {
        return this.configuredObjects.getClientSslSocketFactory();
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public Configuration getConfiguration() {
        return this.configuredObjects.getConfiguration();
    }

    public Routes getRoutes() {
        return this.configuredObjects.getRoutes();
    }

    public RuleContext getRuleContext() {
        return this.ruleContext;
    }

    public Rules getRules() {
        return this.configuredObjects.getRules();
    }

    public Route getSelectedRoute() {
        return this.selectedRoute;
    }

    public ServerEventLogger getServerEventLogger() {
        return this.serverEventLogger;
    }

    public Settings getSettings() {
        return this.configuredObjects.getConfiguration().getSettings();
    }

    public void setApplicableRule(final Rule applicableRl) {
        this.applicableRule = applicableRl;
    }

    public void setClientSocket(final Socket clientSock) {
        this.clientSocket = clientSock;
    }

    public void setRuleContext(final RuleContext rlContext) {
        this.ruleContext = Objects.requireNonNull(rlContext);
    }

    public void setSelectedRoute(final Route selectedRte) {
        this.selectedRoute = Objects.requireNonNull(selectedRte);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [clientSocket=" +
                this.clientSocket +
                "]";
    }

}
