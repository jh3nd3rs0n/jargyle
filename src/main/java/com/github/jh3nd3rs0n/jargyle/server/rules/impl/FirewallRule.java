package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;

public abstract class FirewallRule extends Rule {

	public static abstract class Builder<B extends Builder<B, R>, R extends FirewallRule> 
		extends Rule.Builder<B, R> {

		public static interface Field<B extends Builder<B, R>, R extends FirewallRule>
			extends Rule.Builder.Field<B, R> {
			
		}
		
		private final FirewallRuleAction firewallRuleAction;
		
		public Builder(final FirewallRuleAction firewallRlAction) {
			this.firewallRuleAction = Objects.requireNonNull(
					firewallRlAction, "firewall rule action must not be null");
		}
		
		public abstract R build();
		
		public B doc(final String d) {
			super.doc(d);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
		public B logAction(final LogAction lgAction) {
			super.logAction(lgAction);
			@SuppressWarnings("unchecked")
			B builder = (B) this;
			return builder;
		}
		
		public final FirewallRuleAction firewallRuleAction() {
			return this.firewallRuleAction;
		}
		
	}
	
	public static abstract class Context extends Rule.Context {

		private FirewallRuleAction firewallRuleAction;
		
		public Context() {
			this.firewallRuleAction = null;
		}
		
		public final FirewallRuleAction getFirewallRuleAction() {
			return this.firewallRuleAction;
		}
		
		public final void setFirewallRuleAction(
				final FirewallRuleAction firewallRlAction) {
			this.firewallRuleAction = firewallRlAction;
		}
		
	}
	
	private final FirewallRuleAction firewallRuleAction;

	protected FirewallRule(final Builder<?, ?> builder) {
		super(builder);
		FirewallRuleAction firewallRlAction = builder.firewallRuleAction();
		this.firewallRuleAction = firewallRlAction;
	}
	
	public final FirewallRuleAction getFirewallRuleAction() {
		return this.firewallRuleAction;
	}
	
}
