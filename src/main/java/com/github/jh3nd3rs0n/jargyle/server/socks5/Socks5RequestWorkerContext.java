package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Objects;

import org.slf4j.Logger;

import com.github.jh3nd3rs0n.jargyle.internal.logging.LoggerHelper;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleNotFoundException;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5ReplyFirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.Socks5ReplyFirewallRules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public class Socks5RequestWorkerContext extends Socks5WorkerContext {

	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final Socks5Request socks5Request;
	
	public Socks5RequestWorkerContext(final Socks5RequestWorkerContext other) {
		super(other);
		this.methodSubnegotiationResults = other.methodSubnegotiationResults;
		this.socks5Request = other.socks5Request; 
	}
	
	public Socks5RequestWorkerContext(
			final Socks5WorkerContext context,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		super(context);
		Objects.requireNonNull(methSubnegotiationResults);
		Objects.requireNonNull(socks5Req);
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.socks5Request = socks5Req;
	}
	
	public final boolean canAllowSocks5Reply(
			final Object worker, 
			final Rule.Context context, 
			final Logger logger) {
		Socks5ReplyFirewallRules socks5ReplyFirewallRules = 
				this.getSettings().getLastValue(
						Socks5SettingSpecConstants.SOCKS5_SOCKS5_REPLY_FIREWALL_RULES);
		Socks5ReplyFirewallRule socks5ReplyFirewallRule = null;
		try {
			socks5ReplyFirewallRule = 
					socks5ReplyFirewallRules.anyAppliesBasedOn(context);
		} catch (FirewallRuleNotFoundException e) {
			logger.error(
					LoggerHelper.objectMessage(worker, String.format(
							"Firewall rule not found for the following "
							+ "context: %s",
							context)),
					e);
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(worker, rep, logger);
			return false;			
		}
		try {
			socks5ReplyFirewallRule.applyBasedOn(context);
		} catch (FirewallRuleActionDenyException e) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.sendSocks5Reply(worker, rep, logger);
			return false;
		}
		return true;
	}
	
	public final Command getCommand() {
		return this.socks5Request.getCommand();
	}
	
	public final String getDesiredDestinationAddress() {
		return this.socks5Request.getDesiredDestinationAddress();
	}
	
	public final int getDesiredDestinationPort() {
		return this.socks5Request.getDesiredDestinationPort();
	}
	
	public final MethodSubnegotiationResults getMethodSubnegotiationResults() {
		return this.methodSubnegotiationResults;
	}
	
	public final Socks5Request getSocks5Request() {
		return this.socks5Request;
	}
	
}
