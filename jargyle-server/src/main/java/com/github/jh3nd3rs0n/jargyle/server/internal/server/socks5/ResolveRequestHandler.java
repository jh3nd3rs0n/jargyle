package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.ServerEventLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

final class ResolveRequestHandler extends RequestHandler {

	private final ServerEventLogger serverEventLogger;
    private final TcpBasedRequestHandlerContext tcpBasedRequestHandlerContext;
	
	public ResolveRequestHandler(
			final TcpBasedRequestHandlerContext handlerContext) {
		this.serverEventLogger = handlerContext.getServerEventLogger();
        this.tcpBasedRequestHandlerContext = handlerContext;
        this.tcpBasedRequestHandlerContext.setLogMessageSource(this);
	}

    @Override
    public void handleRequest() {
        HostResolver hostResolver =
                this.tcpBasedRequestHandlerContext.getSelectedRoute().getNetObjectFactory().newHostResolver();
        InetAddress inetAddress;
        Reply rep;
        try {
            inetAddress = hostResolver.resolve(
                    this.tcpBasedRequestHandlerContext.getDesiredDestinationAddress().toString());
        } catch (UnknownHostException e) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this, "Error in resolving the hostname"),
                    e);
            this.tcpBasedRequestHandlerContext.sendReply(
                    Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
            return;
        } catch (IOException e) {
            if (ThrowableHelper.isOrHasInstanceOf(
                    e, UnknownHostException.class)) {
                this.serverEventLogger.warn(
                        ObjectLogMessageHelper.objectLogMessage(
                                this, "Error in resolving the hostname"),
                        e);
                this.tcpBasedRequestHandlerContext.sendReply(
                        Reply.newFailureInstance(ReplyCode.HOST_UNREACHABLE));
                return;
            }
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this, "Error in resolving the hostname"),
                    e);
            this.tcpBasedRequestHandlerContext.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
            return;
        }
        String serverBoundAddress = inetAddress.getHostAddress();
        Port serverBoundPort =
                this.tcpBasedRequestHandlerContext.getDesiredDestinationPort();
        rep = Reply.newSuccessInstance(
                Address.newInstanceFrom(serverBoundAddress),
                serverBoundPort);
        RuleContext ruleContext =
                this.tcpBasedRequestHandlerContext.newReplyRuleContext(rep);
        this.tcpBasedRequestHandlerContext.setRuleContext(ruleContext);
        Rule applicableRule =
                this.tcpBasedRequestHandlerContext.getRules().firstAppliesTo(
                        this.tcpBasedRequestHandlerContext.getRuleContext());
        if (applicableRule == null) {
            this.serverEventLogger.warn(
                    ObjectLogMessageHelper.objectLogMessage(
                            this,
                            "No applicable rule found based on the following "
                            + "context: %s",
                            this.tcpBasedRequestHandlerContext.getRuleContext()));
            this.tcpBasedRequestHandlerContext.sendReply(
                    Reply.newFailureInstance(
                            ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
            return;
        }
        this.tcpBasedRequestHandlerContext.setApplicableRule(applicableRule);
        if (!this.tcpBasedRequestHandlerContext.canAllowReply()) {
            return;
        }
        this.tcpBasedRequestHandlerContext.sendReply(rep);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [tcpBasedRequestHandlerContext=" +
                this.tcpBasedRequestHandlerContext +
                "]";
    }

}
