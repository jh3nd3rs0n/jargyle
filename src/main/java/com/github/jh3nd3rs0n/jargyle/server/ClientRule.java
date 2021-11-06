package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;

public final class ClientRule extends Rule {

	public static final class Builder 
		extends Rule.Builder<Builder, ClientRule> {
		
		public static enum Field 
			implements Rule.Builder.Field<Builder, ClientRule> {

			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new client rule.",
					usage = "ruleAction=RULE_ACTION"
			)
			RULE_ACTION("ruleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new ClientRule.Builder(RuleAction.valueOfString(
							value));
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
					doc = "Specifies the logging action to take if the rule "
							+ "applies",
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
		
		public Builder(final RuleAction rlAction) {
			super(rlAction);
			this.clientAddressRange = null;
			this.socksServerAddressRange = null;
		}
		
		@Override
		public ClientRule build() {
			return new ClientRule(this);
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
	
	private static final ClientRule DEFAULT_INSTANCE = new ClientRule.Builder(
			RuleAction.ALLOW).build();
	
	public static ClientRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<ClientRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final AddressRange socksServerAddressRange;
	
	private ClientRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		AddressRange socksServerAddrRange = builder.socksServerAddressRange;
		this.clientAddressRange = clientAddrRange;
		this.socksServerAddressRange = socksServerAddrRange;
	}

	public boolean appliesTo(
			final String clientAddress, final String socksServerAddress) {
		if (this.clientAddressRange != null
				&& !this.clientAddressRange.contains(clientAddress)) {
			return false;
		}
		if (this.socksServerAddressRange != null
				&& !this.socksServerAddressRange.contains(socksServerAddress)) {
			return false;
		}
		return true;
	}
	
	public void applyTo(
			final String clientAddress, final String socksServerAddress) {
		RuleAction ruleAction = this.getRuleAction();
		LogAction logAction = this.getLogAction();
		if (ruleAction.equals(RuleAction.ALLOW)	&& logAction != null) {
			logAction.invoke(String.format(
					"Client address %s to SOCKS server address %s is allowed "
					+ "based on the following rule: %s", 
					clientAddress,
					socksServerAddress,
					this));
		} else if (ruleAction.equals(RuleAction.DENY)) {
			if (logAction != null) {
				logAction.invoke(String.format(
						"Client address %s to SOCKS server address %s is "
						+ "denied based on the following rule: %s", 
						clientAddress,
						socksServerAddress,
						this));				
			}
			throw new RuleActionDenyException(String.format(
					"client address %s to SOCKS server address %s is denied "
					+ "based on the following rule: %s", 
					clientAddress,
					socksServerAddress,
					this));
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
		ClientRule other = (ClientRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(
				other.clientAddressRange)) {
			return false;
		}
		if (this.getRuleAction() != other.getRuleAction()) {
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
		result = prime * result + ((this.getRuleAction() == null) ? 
				0 : this.getRuleAction().hashCode());
		result = prime * result + ((this.socksServerAddressRange == null) ? 
				0 : this.socksServerAddressRange.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ruleAction=");
		builder.append(this.getRuleAction());
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
