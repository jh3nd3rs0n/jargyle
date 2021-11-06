package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public final class Socks5UdpRule extends Rule {

	public static final class Builder 
		extends Rule.Builder<Builder, Socks5UdpRule>{

		public static enum Field 
			implements Rule.Builder.Field<Builder, Socks5UdpRule> {

			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new SOCKS5 UDP rule.",
					usage = "ruleAction=RULE_ACTION"
			)	
			RULE_ACTION("ruleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new Socks5UdpRule.Builder(RuleAction.valueOfString(
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
		private Method method;
		private String user;
		private AddressRange peerAddressRange;
		
		public Builder(final RuleAction rlAction) {
			super(rlAction);
			this.clientAddressRange = null;
			this.method = null;
			this.user = null;
			this.peerAddressRange = null;
		}
		
		@Override
		public Socks5UdpRule build() {
			return new Socks5UdpRule(this);
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

	private static final Socks5UdpRule DEFAULT_INSTANCE = 
			new Socks5UdpRule.Builder(RuleAction.ALLOW).build();
	
	public static Socks5UdpRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5UdpRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final Method method;
	private final String user;
	private final AddressRange peerAddressRange;
	
	private Socks5UdpRule(final Builder builder) {
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
	
	public boolean appliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final String peerAddress) {
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

	public void applyTo(
			final String clientAddress,	
			final MethodSubnegotiationResults methSubnegotiationResults,
			final String peerAddress) {
		RuleAction ruleAction = this.getRuleAction();
		LogAction logAction = this.getLogAction();
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (ruleAction.equals(RuleAction.ALLOW)	&& logAction != null) {
			logAction.invoke(String.format(
					"Traffic between client %s%s and peer %s is allowed based "
					+ "on the following rule: %s",
					clientAddress,
					possibleUser,
					peerAddress,
					this));			
		} else if (ruleAction.equals(RuleAction.DENY)) {
			if (logAction != null) {
				logAction.invoke(String.format(
						"Traffic between client %s%s and peer %s is denied "
						+ "based on the following rule: %s",
						clientAddress,
						possibleUser,
						peerAddress,
						this));				
			}
			throw new RuleActionDenyException(String.format(
					"traffic between client %s%s and peer %s is denied based "
					+ "on the following rule: %s",
					clientAddress,
					possibleUser,
					peerAddress,
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
		Socks5UdpRule other = (Socks5UdpRule) obj;
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(other.clientAddressRange)) {
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
		if (this.getRuleAction() != other.getRuleAction()) {
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
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result	+ ((this.peerAddressRange == null) ? 
				0 : this.peerAddressRange.hashCode());
		result = prime * result + ((this.getRuleAction() == null) ? 
				0 : this.getRuleAction().hashCode());
		result = prime * result + ((this.user == null) ? 
				0 : this.user.hashCode());
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
