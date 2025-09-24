package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.*;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;

final class ConnectionHandler {

    private Rule applicableRule;
    private final SslSocketFactory clientSslSocketFactory;
    private final ConnectionHandlerContext connectionHandlerContext;
    private final Routes routes;
    private RuleContext ruleContext;
    private final Rules rules;
    private final ServerEventLogger serverEventLogger;
    private final Settings settings;

    public ConnectionHandler(final ConnectionHandlerContext handlerContext) {
        this.applicableRule = null;
        this.clientSslSocketFactory =
                handlerContext.getClientSslSocketFactory();
        this.connectionHandlerContext = handlerContext;
        this.routes = handlerContext.getRoutes();
        this.ruleContext = null;
        this.rules = handlerContext.getRules();
        this.serverEventLogger = handlerContext.getServerEventLogger();
        this.settings = handlerContext.getSettings();
    }

    private boolean canAllowClientSocket() {
        FirewallAction firewallAction =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION);
        if (firewallAction == null) {
            return false;
        }
        LogAction firewallActionLogAction =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_LOG_ACTION);
        if (firewallAction.equals(FirewallAction.ALLOW)) {
            if (!this.canAllowClientSocketWithinLimit()) {
                return false;
            }
            if (firewallActionLogAction != null) {
                firewallActionLogAction.invoke(String.format(
                        "Client allowed based on the following rule and "
                                + "context: rule: %s context: %s",
                        this.applicableRule,
                        this.ruleContext));
            }
        } else if (firewallAction.equals(FirewallAction.DENY)
                && firewallActionLogAction != null) {
            firewallActionLogAction.invoke(String.format(
                    "Client denied based on the following rule and context: "
                            + "rule: %s context: %s",
                    this.applicableRule,
                    this.ruleContext));
        }
        return FirewallAction.ALLOW.equals(firewallAction);
    }

    private boolean canAllowClientSocketWithinLimit() {
        NonNegativeIntegerLimit firewallActionAllowLimit =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
        LogAction firewallActionAllowLimitReachedLogAction =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
        if (firewallActionAllowLimit != null) {
            if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
                if (firewallActionAllowLimitReachedLogAction != null) {
                    firewallActionAllowLimitReachedLogAction.invoke(
                            String.format(
                                    "Allowed limit has been reached based on "
                                            + "the following rule and context: rule: "
                                            + "%s context: %s",
                                    this.applicableRule,
                                    this.ruleContext));
                }
                return false;
            }
            this.connectionHandlerContext.addBelowAllowLimitRule(this.applicableRule);
        }
        return true;
    }

    private boolean configureClientSocket(final Socket clientSock) {
        SocketSettings socketSettings = this.getClientSocketSettings();
        try {
            socketSettings.applyTo(clientSock);
        } catch (UnsupportedOperationException | SocketException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this, "Error in setting the client socket"),
                    e);
            return false;
        }
        return true;
    }

    private Routes getClientRoutes() {
        List<Route> rtes = this.applicableRule.getRuleActionValues(
                        GeneralRuleActionSpecConstants.SELECTABLE_ROUTE_ID)
                .stream()
                .map(this.routes::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!rtes.isEmpty()) {
            return Routes.of(rtes);
        }
        return this.routes;
    }

    private LogAction getClientRouteSelectionLogAction() {
        LogAction routeSelectionLogAction =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.ROUTE_SELECTION_LOG_ACTION);
        if (routeSelectionLogAction != null) {
            return routeSelectionLogAction;
        }
        return this.settings.getLastValue(
                GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
    }

    private SelectionStrategy getClientRouteSelectionStrategy() {
        SelectionStrategy routeSelectionStrategy =
                this.applicableRule.getLastRuleActionValue(
                        GeneralRuleActionSpecConstants.ROUTE_SELECTION_STRATEGY);
        if (routeSelectionStrategy != null) {
            return routeSelectionStrategy;
        }
        return this.settings.getLastValue(
                GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
    }

    private SocketSettings getClientSocketSettings() {
        return GeneralValueDerivationHelper.getClientSocketSettingsFrom(
                this.applicableRule, this.settings);
    }

    public void handleConnection() throws IOException {
        this.ruleContext = this.newClientRuleContext();
        this.applicableRule = this.rules.firstAppliesTo(this.ruleContext);
        if (this.applicableRule == null) {
            this.serverEventLogger.warn(ObjectLogMessageHelper.objectLogMessage(
                    this,
                    "No applicable rule found based on the following "
                            + "context: %s",
                    this.ruleContext));
            return;
        }
        if (!this.canAllowClientSocket()) {
            return;
        }
        try {
            Socket clientSocket = this.connectionHandlerContext.getClientSocket();
            if (!this.configureClientSocket(clientSocket)) {
                return;
            }
            clientSocket = this.wrapClientSocket(clientSocket);
            if (clientSocket == null) {
                return;
            }
            this.connectionHandlerContext.setClientSocket(clientSocket);
            this.connectionHandlerContext.setApplicableRule(this.applicableRule);
            this.connectionHandlerContext.setRuleContext(this.ruleContext);
            this.connectionHandlerContext.setSelectedRoute(this.selectClientRoute());
            SocksConnectionHandler socksConnectionHandler =
                    new SocksConnectionHandler(
                            new SocksConnectionHandlerContext(
                                    this.connectionHandlerContext,
                                    ServerEventLogger.newInstance(
                                            SocksConnectionHandler.class)));
            socksConnectionHandler.handleSocksConnection();
        } finally {
            this.connectionHandlerContext.decrementAllCurrentAllowedCounts();
            Socket clientSocket =
                    this.connectionHandlerContext.getClientSocket();
            if (!clientSocket.isClosed()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    this.serverEventLogger.warn(
                            ObjectLogMessageHelper.objectLogMessage(
                                    this,
                                    "Error upon closing connection to the "
                                            + "client"),
                            e);
                }
            }
        }
    }

    private RuleContext newClientRuleContext() {
        RuleContext clientRuleContext = new RuleContext();
        Socket clientSocket = this.connectionHandlerContext.getClientSocket();
        clientRuleContext.putRuleArgValue(
                GeneralRuleArgSpecConstants.CLIENT_ADDRESS,
                clientSocket.getInetAddress().getHostAddress());
        clientRuleContext.putRuleArgValue(
                GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS,
                clientSocket.getLocalAddress().getHostAddress());
        return clientRuleContext;
    }

    private Route selectClientRoute() {
        SelectionStrategy rteSelectionStrategy =
                this.getClientRouteSelectionStrategy();
        Routes rtes = this.getClientRoutes();
        LogAction rteSelectionLogAction =
                this.getClientRouteSelectionLogAction();
        Route selectedRte = rteSelectionStrategy.selectFrom(
                new ArrayList<>(rtes.toMap().values()));
        if (rteSelectionLogAction != null) {
            if (this.applicableRule.hasRuleAction(
                    GeneralRuleActionSpecConstants.ROUTE_SELECTION_LOG_ACTION)
                    || this.applicableRule.hasRuleAction(
                    GeneralRuleActionSpecConstants.ROUTE_SELECTION_STRATEGY)
                    || this.applicableRule.hasRuleAction(
                    GeneralRuleActionSpecConstants.SELECTABLE_ROUTE_ID)) {
                rteSelectionLogAction.invoke(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Route '%s' selected based on the following "
                                        + "rule and context: rule: %s context: %s",
                                selectedRte.getId(),
                                this.applicableRule,
                                this.ruleContext));
            } else {
                rteSelectionLogAction.invoke(
                        ObjectLogMessageHelper.objectLogMessage(
                                this,
                                "Route '%s' selected",
                                selectedRte.getId()));
            }
        }
        return selectedRte;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [connectionHandlerContext=" +
                this.connectionHandlerContext +
                "]";
    }

    private Socket wrapClientSocket(final Socket clientSock) {
        try {
            return this.clientSslSocketFactory.getSocket(
                    clientSock, null, true);
        } catch (IOException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this, "Error in wrapping the client socket"),
                    e);
            return null;
        }
    }

}
