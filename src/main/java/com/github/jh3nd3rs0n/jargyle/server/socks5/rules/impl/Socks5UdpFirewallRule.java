package com.github.jh3nd3rs0n.jargyle.server.socks5.rules.impl;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.AddressRange;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRule;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleAction;
import com.github.jh3nd3rs0n.jargyle.server.rules.impl.FirewallRuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.server.socks5.MethodSubnegotiationResults;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public final class Socks5UdpFirewallRule extends FirewallRule {

	public static final class Builder 
		extends FirewallRule.Builder<Builder, Socks5UdpFirewallRule>{

		public static enum Field 
			implements FirewallRule.Builder.Field<Builder, Socks5UdpFirewallRule> {

			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new SOCKS5 UDP firewall rule.",
					usage = "firewallRuleAction=FIREWALL_RULE_ACTION"
			)	
			FIREWALL_RULE_ACTION("firewallRuleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new Socks5UdpFirewallRule.Builder(
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
					doc = "Specifies the negotiated SOCKS5 method",
					usage = "method=SOCKS5_METHOD"
			)
			METHOD("method") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.method(Method.valueOfString(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the user if any after the negotiated "
							+ "SOCKS5 method",
					usage = "user=USER"
			)
			USER("user") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.user(value);
				}
			},
			
			@HelpText(
					doc = "Specifies the address range for the peer address",
					usage = "peerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			PEER_ADDRESS_RANGE("peerAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.peerAddressRange(AddressRange.newInstance(
							value));
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
		private Method method;
		private String user;
		private AddressRange peerAddressRange;
		
		public Builder(final FirewallRuleAction rlAction) {
			super(rlAction);
			this.clientAddressRange = null;
			this.method = null;
			this.user = null;
			this.peerAddressRange = null;
		}
		
		@Override
		public Socks5UdpFirewallRule build() {
			return new Socks5UdpFirewallRule(this);
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
		
		public Builder method(final Method meth) {
			this.method = meth;
			return this;
		}
		
		public Builder peerAddressRange(final AddressRange peerAddrRange) {
			this.peerAddressRange = peerAddrRange;
			return this;
		}
		
		public Builder user(final String usr) {
			this.user = usr;
			return this;
		}
		
	}

	public static final class Context extends FirewallRule.Context {
		
		private final String clientAddress;
		private final MethodSubnegotiationResults methodSubnegotiationResults;
		private final String peerAddress;
		
		public Context(
				final String clientAddr,
				final MethodSubnegotiationResults methSubnegotiationResults,
				final String peerAddr) {
			this.clientAddress = clientAddr;
			this.methodSubnegotiationResults = methSubnegotiationResults;
			this.peerAddress = peerAddr;
		}

		public String getClientAddress() {
			return this.clientAddress;
		}

		public MethodSubnegotiationResults getMethodSubnegotiationResults() {
			return this.methodSubnegotiationResults;
		}

		public String getPeerAddress() {
			return this.peerAddress;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientAddress=")
				.append(this.clientAddress)
				.append(", methodSubnegotiationResults=")
				.append(this.methodSubnegotiationResults)
				.append(", peerAddress=")
				.append(this.peerAddress)
				.append("]");
			return builder.toString();
		}
		
	}

	private static final Socks5UdpFirewallRule DEFAULT_INSTANCE = 
			new Socks5UdpFirewallRule.Builder(FirewallRuleAction.ALLOW).build();
	
	public static Socks5UdpFirewallRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5UdpFirewallRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.FIREWALL_RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final Method method;
	private final String user;
	private final AddressRange peerAddressRange;
	
	private Socks5UdpFirewallRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		AddressRange peerAddrRange = builder.peerAddressRange;
		this.clientAddressRange = clientAddrRange;
		this.method = meth;
		this.user = usr;
		this.peerAddressRange = peerAddrRange;
	}
	
	@Override
	public boolean appliesBasedOn(final Rule.Context context) {
		if (!(context instanceof Context)) {
			return false;
		}
		Context cntxt = (Context) context;
		String clientAddress = cntxt.getClientAddress();
		MethodSubnegotiationResults methSubnegotiationResults =
				cntxt.getMethodSubnegotiationResults();
		String peerAddress = cntxt.getPeerAddress();
		if (this.clientAddressRange != null
				&& !this.clientAddressRange.contains(clientAddress)) {
			return false;
		}
		if (this.method != null	&& !this.method.equals(
				methSubnegotiationResults.getMethod())) {
			return false;
		}
		if (this.user != null && !this.user.equals(
				methSubnegotiationResults.getUser())) {
			return false;
		}
		if (this.peerAddressRange != null && !this.peerAddressRange.contains(
				peerAddress)) {
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
		MethodSubnegotiationResults methSubnegotiationResults =
				cntxt.getMethodSubnegotiationResults();
		String peerAddress = cntxt.getPeerAddress();
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (firewallRuleAction.equals(FirewallRuleAction.ALLOW)	&& logAction != null) {
			logAction.invoke(String.format(
					"Traffic between client %s%s and peer %s is allowed based "
					+ "on the following firewall rule and context: %s, %s",
					clientAddress,
					possibleUser,
					peerAddress,
					this,
					context));			
		} else if (firewallRuleAction.equals(FirewallRuleAction.DENY)) {
			if (logAction != null) {
				logAction.invoke(String.format(
						"Traffic between client %s%s and peer %s is denied "
						+ "based on the following firewall rule and context: "
						+ "%s, %s",
						clientAddress,
						possibleUser,
						peerAddress,
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
		Socks5UdpFirewallRule other = (Socks5UdpFirewallRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(other.clientAddressRange)) {
			return false;
		}
		if (this.getFirewallRuleAction() != other.getFirewallRuleAction()) {
			return false;
		}
		if (this.getLogAction() != other.getLogAction()) {
			return false;
		}
		if (this.method != other.method) {
			return false;
		}
		if (this.peerAddressRange == null) {
			if (other.peerAddressRange != null) {
				return false;
			}
		} else if (!this.peerAddressRange.equals(other.peerAddressRange)) {
			return false;
		}
		if (this.user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!this.user.equals(other.user)) {
			return false;
		}
		return true;
	}

	public AddressRange getClientAddressRange() {
		return this.clientAddressRange;
	}

	public Method getMethod() {
		return this.method;
	}

	public AddressRange getPeerAddressRange() {
		return this.peerAddressRange;
	}

	public String getUser() {
		return this.user;
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
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result	+ ((this.peerAddressRange == null) ? 
				0 : this.peerAddressRange.hashCode());
		result = prime * result + ((this.user == null) ? 
				0 : this.user.hashCode());
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
		if (this.method != null) {
			builder.append(" method=");
			builder.append(this.method);			
		}
		if (this.user != null) {
			builder.append(" user=");
			builder.append(this.user);			
		}
		if (this.peerAddressRange != null) {
			builder.append(" peerAddressRange=");
			builder.append(this.peerAddressRange);			
		}
		LogAction logAction = this.getLogAction();
		if (logAction != null) {
			builder.append(" logAction=");
			builder.append(logAction);			
		}
		return builder.toString();
	}

}
