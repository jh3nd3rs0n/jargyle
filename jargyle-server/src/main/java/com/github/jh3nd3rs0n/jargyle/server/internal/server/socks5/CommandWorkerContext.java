package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

final class CommandWorkerContext extends Socks5WorkerContext {

	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final Socks5Request socks5Request;
	private final RuleContext socks5RequestRuleContext;
	
	public CommandWorkerContext(
			final Socks5WorkerContext context,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req,
			final RuleContext socks5ReqRuleContext) {
		super(context);
		Objects.requireNonNull(methSubnegotiationResults);
		Objects.requireNonNull(socks5Req);
		Objects.requireNonNull(socks5ReqRuleContext);
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.socks5Request = socks5Req;
		this.socks5RequestRuleContext = socks5ReqRuleContext;		
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
	
}
