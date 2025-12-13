package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.*;

import java.io.IOException;
import java.net.Socket;

public final class Socks5ConnectionHandlerContext {

    private LogMessageSource logMessageSource;
    private final ServerEventLogger serverEventLogger;
    private final SocksConnectionHandlerContext socksConnectionHandlerContext;

    public Socks5ConnectionHandlerContext(
            final SocksConnectionHandlerContext handlerContext,
            final ServerEventLogger logger) {
        this.logMessageSource = null;
        this.serverEventLogger = logger;
        this.socksConnectionHandlerContext = handlerContext;
    }

    public void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
        this.socksConnectionHandlerContext.addBelowAllowLimitRule(belowAllowLimitRl);
    }

    public Rule getApplicableRule() {
        return this.socksConnectionHandlerContext.getApplicableRule();
    }

    public DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
        return this.socksConnectionHandlerContext.getClientFacingDtlsDatagramSocketFactory();
    }

    public Socket getClientSocket() {
        return this.socksConnectionHandlerContext.getClientSocket();
    }

    public Configuration getConfiguration() {
        return this.socksConnectionHandlerContext.getConfiguration();
    }

    public LogMessageSource getLogMessageSource() {
        return this.logMessageSource;
    }

    public Routes getRoutes() {
        return this.socksConnectionHandlerContext.getRoutes();
    }

    public RuleContext getRuleContext() {
        return this.socksConnectionHandlerContext.getRuleContext();
    }

    public Rules getRules() {
        return this.socksConnectionHandlerContext.getRules();
    }

    public Route getSelectedRoute() {
        return this.socksConnectionHandlerContext.getSelectedRoute();
    }

    public ServerEventLogger getServerEventLogger() {
        return this.serverEventLogger;
    }

    public Settings getSettings() {
        return this.socksConnectionHandlerContext.getSettings();
    }

    public boolean sendReply(final Reply rep) {
        this.serverEventLogger.debug(ObjectLogMessageHelper.objectLogMessage(
                this.getLogMessageSource(),
                "Sending %s",
                rep.toString()));
        try {
            this.sendToClient(rep.toByteArray());
        } catch (IOException e) {
            this.serverEventLogger.logClientIoException(
                    ObjectLogMessageHelper.objectLogMessage(
                            this.getLogMessageSource(),
                            "Error in writing SOCKS5 reply"),
                    e);
            return false;
        }
        return true;
    }

    public void sendToClient(final byte[] b) throws IOException {
        this.socksConnectionHandlerContext.sendToClient(b);
    }

    public void setApplicableRule(final Rule applicableRl) {
        this.socksConnectionHandlerContext.setApplicableRule(applicableRl);
    }

    public void setClientSocket(final Socket clientSock) {
        this.socksConnectionHandlerContext.setClientSocket(clientSock);
    }

    public void setLogMessageSource(final LogMessageSource source) {
        this.logMessageSource = source;
    }

    public void setRuleContext(final RuleContext rlContext) {
        this.socksConnectionHandlerContext.setRuleContext(rlContext);
    }

    public void setSelectedRoute(final Route selectedRte) {
        this.socksConnectionHandlerContext.setSelectedRoute(selectedRte);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getClientSocket()=" +
                this.getClientSocket() +
                "]";
    }
    
}
