package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;

public final class ClientFirewallRule extends FirewallRule {

	public static final class Builder 
		extends FirewallRule.Builder<Builder, ClientFirewallRule> {
		
		public static enum Field 
			implements FirewallRule.Builder.Field<Builder, ClientFirewallRule> {

			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new client firewall rule.",
					usage = "firewallRuleAction=FIREWALL_RULE_ACTION"
			)
			FIREWALL_RULE_ACTION("firewallRuleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new ClientFirewallRule.Builder(
							FirewallRuleAction.valueOfString(value));
				}
			},

			@HelpText(
					doc = "Specifies the address range for the client address",
					usage = "clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			CLIENT_ADDRESS_RANGE("clientAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.clientAddressRange(AddressRange.newInstance(
							value));
				}
			},

			@HelpText(
					doc = "Specifies the address range for the SOCKS server "
							+ "address",
					usage = "socksServerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			SOCKS_SERVER_ADDRESS_RANGE("socksServerAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.socksServerAddressRange(
							AddressRange.newInstance(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the logging action to take if the "
							+ "firewall rule is applied",
					usage = "logAction=LOG_ACTION"
			)	
			LOG_ACTION("logAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.logAction(LogAction.valueOfString(value));
				}
			};
			
			private final String string;
			
			private Field(final String str) {
				this.string = str;
			}
			
			@Override
			public boolean isRepresentedBy(final String s) {
				return this.string.equals(s);
			}
			
			@Override
			public String toString() {
				return this.string;
			}
			
		}
		
		private AddressRange clientAddressRange;
		private AddressRange socksServerAddressRange;
		
		public Builder(final FirewallRuleAction firewallRlAction) {
			super(firewallRlAction);
			this.clientAddressRange = null;
			this.socksServerAddressRange = null;
		}
		
		@Override
		public ClientFirewallRule build() {
			return new ClientFirewallRule(this);
		}
		
		public Builder clientAddressRange(final AddressRange clientAddrRange) {
			this.clientAddressRange = clientAddrRange;
			return this;
		}
		
		@Override
		public Builder doc(final String d) {
			super.doc(d);
			return this;
		}
		
		@Override
		public Builder logAction(final LogAction lgAction) {
			super.logAction(lgAction);
			return this;
		}
		
		public Builder socksServerAddressRange(
				final AddressRange socksServerAddrRange) {
			this.socksServerAddressRange = socksServerAddrRange;
			return this;
		}
		
	}
	
	public static final class Context extends FirewallRule.Context {

		private final String clientAddress;
		private final String socksServerAddress;
		
		public Context(final String clientAddr, final String socksServerAddr) {
			this.clientAddress = clientAddr;
			this.socksServerAddress = socksServerAddr;
		}

		public String getClientAddress() {
			return this.clientAddress;
		}

		public String getSocksServerAddress() {
			return this.socksServerAddress;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientAddress=")
				.append(this.clientAddress)
				.append(", socksServerAddress=")
				.append(this.socksServerAddress)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final ClientFirewallRule DEFAULT_INSTANCE = new ClientFirewallRule.Builder(
			FirewallRuleAction.ALLOW).build();
	
	public static ClientFirewallRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<ClientFirewallRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.FIREWALL_RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final AddressRange socksServerAddressRange;
	
	private ClientFirewallRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		AddressRange socksServerAddrRange = builder.socksServerAddressRange;
		this.clientAddressRange = clientAddrRange;
		this.socksServerAddressRange = socksServerAddrRange;
	}
	
	@Override
	public boolean appliesBasedOn(final Rule.Context context) {
		if (!(context instanceof Context)) {
			return false;
		}
		Context cntxt = (Context) context;
		if (this.clientAddressRange != null
				&& !this.clientAddressRange.contains(
						cntxt.getClientAddress())) {
			return false;
		}
		if (this.socksServerAddressRange != null
				&& !this.socksServerAddressRange.contains(
						cntxt.getSocksServerAddress())) {
			return false;
		}
		return true;
	}

	@Override
	public void applyBasedOn(final Rule.Context context) {
		FirewallRuleAction firewallRuleAction = this.getFirewallRuleAction();
		LogAction logAction = this.getLogAction();
		Context cntxt = (Context) context;		
		String clientAddress = cntxt.getClientAddress();
		String socksServerAddress = cntxt.getSocksServerAddress();
		if (firewallRuleAction.equals(FirewallRuleAction.ALLOW)	
				&& logAction != null) {
			logAction.invoke(String.format(
					"Client address %s to SOCKS server address %s is allowed "
					+ "based on the following firewall rule and context: %s, "
					+ "%s", 
					clientAddress,
					socksServerAddress,
					this,
					context));
		} else if (firewallRuleAction.equals(FirewallRuleAction.DENY)) {
			if (logAction != null) {
				logAction.invoke(String.format(
						"Client address %s to SOCKS server address %s is "
						+ "denied based on the following firewall rule and "
						+ "context: %s, %s", 
						clientAddress,
						socksServerAddress,
						this,
						context));				
			}
			throw new FirewallRuleActionDenyException(this, cntxt);
		}		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ClientFirewallRule other = (ClientFirewallRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(
				other.clientAddressRange)) {
			return false;
		}
		if (this.getFirewallRuleAction() != other.getFirewallRuleAction()) {
			return false;
		}
		if (this.getLogAction() != other.getLogAction()) {
			return false;
		}
		if (this.socksServerAddressRange == null) {
			if (other.socksServerAddressRange != null) {
				return false;
			}
		} else if (!this.socksServerAddressRange.equals(
				other.socksServerAddressRange)) {
			return false;
		}
		return true;
	}

	public AddressRange getClientAddressRange() {
		return this.clientAddressRange;
	}
	
	public AddressRange getSocksServerAddressRange() {
		return this.socksServerAddressRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientAddressRange == null) ? 
				0 : this.clientAddressRange.hashCode());
		result = prime * result + ((this.getFirewallRuleAction() == null) ? 
				0 : this.getFirewallRuleAction().hashCode());
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.socksServerAddressRange == null) ? 
				0 : this.socksServerAddressRange.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("firewallRuleAction=");
		builder.append(this.getFirewallRuleAction());
		if (this.clientAddressRange != null) {
			builder.append(" clientAddressRange=");
			builder.append(this.clientAddressRange);
		}
		if (this.socksServerAddressRange != null) {
			builder.append(" socksServerAddressRange=");
			builder.append(this.socksServerAddressRange);
		}
		LogAction logAction = this.getLogAction();
		if (logAction != null) {
			builder.append(" logAction=");
			builder.append(logAction);
		}
		return builder.toString();
	}

}
