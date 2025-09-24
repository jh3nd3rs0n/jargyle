package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public final class SocksConnectionHandlerContext {

    private final ConnectionHandlerContext connectionHandlerContext;
    private final ServerEventLogger serverEventLogger;

    SocksConnectionHandlerContext(
            final ConnectionHandlerContext handlerContext,
            final ServerEventLogger logger) {
        this.connectionHandlerContext = handlerContext;
        this.serverEventLogger = logger;
    }

    public void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
        this.connectionHandlerContext.addBelowAllowLimitRule(belowAllowLimitRl);
    }

    public Rule getApplicableRule() {
        return this.connectionHandlerContext.getApplicableRule();
    }

    public DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
        return this.connectionHandlerContext.getClientFacingDtlsDatagramSocketFactory();
    }

    public Socket getClientSocket() {
        return this.connectionHandlerContext.getClientSocket();
    }

    public Configuration getConfiguration() {
        return this.connectionHandlerContext.getConfiguration();
    }

    public Routes getRoutes() {
        return this.connectionHandlerContext.getRoutes();
    }

    public RuleContext getRuleContext() {
        return this.connectionHandlerContext.getRuleContext();
    }

    public Rules getRules() {
        return this.connectionHandlerContext.getRules();
    }

    public Route getSelectedRoute() {
        return this.connectionHandlerContext.getSelectedRoute();
    }

    public ServerEventLogger getServerEventLogger() {
        return this.serverEventLogger;
    }

    public Settings getSettings() {
        return this.connectionHandlerContext.getSettings();
    }

    public void sendToClient(final byte[] b) throws IOException {
        OutputStream clientOutputStream =
                this.getClientSocket().getOutputStream();
        clientOutputStream.write(b);
        clientOutputStream.flush();
    }

    public void setApplicableRule(final Rule applicableRl) {
        this.connectionHandlerContext.setApplicableRule(applicableRl);
    }

    public void setClientSocket(final Socket clientSock) {
        this.connectionHandlerContext.setClientSocket(clientSock);
    }

    public void setRuleContext(final RuleContext rlContext) {
        this.connectionHandlerContext.setRuleContext(rlContext);
    }

    public void setSelectedRoute(final Route selectedRte) {
        this.connectionHandlerContext.setSelectedRoute(selectedRte);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getClientSocket()=" +
                this.getClientSocket() +
                "]";
    }

}
