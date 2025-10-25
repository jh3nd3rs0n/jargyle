package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.*;

import java.io.IOException;
import java.net.Socket;

final class RelayRequestHandlerContext {

    private static final int HALF_SECOND = 500;

    private final RequestHandlerContext requestHandlerContext;
    
    public RelayRequestHandlerContext(
            final RequestHandlerContext handlerContext) {
        this.requestHandlerContext = handlerContext;
    }

    public void addBelowAllowLimitRule(final Rule belowAllowLimitRl) {
        this.requestHandlerContext.addBelowAllowLimitRule(belowAllowLimitRl);
    }

    public boolean canAllowReply() {
        return this.requestHandlerContext.canAllowReply();
    }

    public Rule getApplicableRule() {
        return this.requestHandlerContext.getApplicableRule();
    }

    public Socket getClientSocket() {
        return this.requestHandlerContext.getClientSocket();
    }

    public Address getDesiredDestinationAddress() {
        return this.requestHandlerContext.getDesiredDestinationAddress();
    }

    public Port getDesiredDestinationPort() {
        return this.requestHandlerContext.getDesiredDestinationPort();
    }

    public RuleContext getRuleContext() {
        return this.requestHandlerContext.getRuleContext();
    }

    public Rules getRules() {
        return this.requestHandlerContext.getRules();
    }

    public Route getSelectedRoute() {
        return this.requestHandlerContext.getSelectedRoute();
    }

    public ServerEventLogger getServerEventLogger() {
        return this.requestHandlerContext.getServerEventLogger();
    }

    public Settings getSettings() {
        return this.requestHandlerContext.getSettings();
    }

    public RuleContext newReplyRuleContext(final Reply rep) {
        return this.requestHandlerContext.newReplyRuleContext(rep);
    }

    public void passData(final Relay relay) throws IOException {
        try {
            relay.start();
            while (!relay.getState().equals(Relay.State.STOPPED)) {
                try {
                    Thread.sleep(HALF_SECOND);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            if (!relay.getState().equals(Relay.State.STOPPED)) {
                relay.stop();
            }
        }
    }

    public boolean sendReply(final Reply rep) {
        return this.requestHandlerContext.sendReply(rep);
    }

    public void setApplicableRule(final Rule applicableRl) {
        this.requestHandlerContext.setApplicableRule(applicableRl);
    }

    public void setLogMessageSource(final LogMessageSource source) {
        this.requestHandlerContext.setLogMessageSource(source);
    }

    public void setRuleContext(final RuleContext rlContext) {
        this.requestHandlerContext.setRuleContext(rlContext);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [getClientSocket()=" +
                this.getClientSocket() +
                "]";
    }
    
}
