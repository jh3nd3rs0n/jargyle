package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;

public final class Socks5UdpRule {

	public static final class Builder {

		@HelpText(
				doc = "Specifies the action to take. This field starts a new "
						+ "SOCKS5 UDP rule.",
				usage = "ruleAction=RULE_ACTION"
		)	
		private final RuleAction ruleAction;
		
		@HelpText(
				doc = "Specifies the address range for the client address",
				usage = "clientAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange clientAddressRange;
		
		@HelpText(
				doc = "Specifies the negotiated SOCKS5 method",
				usage = "method=SOCKS5_METHOD"
		)
		private Method method;
		
		@HelpText(
				doc = "Specifies the user if any after the negotiated SOCKS5 "
						+ "method",
				usage = "user=USER"
		)
		private String user;
		
		@HelpText(
				doc = "Specifies the address range for the peer address",
				usage = "peerAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
		)	
		private AddressRange peerAddressRange;
		
		@HelpText(
				doc = "Specifies the logging action to take if the rule applies",
				usage = "logAction=LOG_ACTION"
		)	
		private LogAction logAction;
		
		private String doc;		
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.clientAddressRange = null;
			this.method = null;
			this.user = null;
			this.peerAddressRange = null;
			this.logAction = null;
			this.doc = null;			
		}
		
		public Socks5UdpRule build() {
			return new Socks5UdpRule(this);
		}
		
		public Builder clientAddressRange(final AddressRange clientAddrRange) {
			this.clientAddressRange = clientAddrRange;
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
		
		public Builder logAction(final LogAction lgAction) {
			this.logAction = lgAction;
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

	private static enum Field {
		
		RULE_ACTION("ruleAction") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return new Socks5UdpRule.Builder(RuleAction.valueOfString(
						value));
			}
		},
		
		CLIENT_ADDRESS_RANGE("clientAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.clientAddressRange(AddressRange.newInstance(
						value));
			}
		},
		
		METHOD("method") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.method(Method.valueOfString(value));
			}
		},
		
		USER("user") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.user(value);
			}
		},
		
		PEER_ADDRESS_RANGE("peerAddressRange") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.peerAddressRange(AddressRange.newInstance(
						value));
			}
		},
		
		LOG_ACTION("logAction") {
			@Override
			public Builder set(final Builder builder, final String value) {
				return builder.logAction(LogAction.valueOfString(value));
			}
		};
		
		public static Field valueOfString(final String s) {
			for (Field value : Field.values()) {
				if (value.toString().equals(s)) {
					return value;
				}
			}
			StringBuilder sb = new StringBuilder();
			List<Field> list = Arrays.asList(Field.values());
			for (Iterator<Field> iterator = list.iterator(); 
					iterator.hasNext();) {
				Field value = iterator.next();
				sb.append(value);
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			throw new IllegalArgumentException(
					String.format(
							"expected field must be one of the following "
							+ "values: %s. actual value is %s",
							sb.toString(),
							s));
		}
		
		private final String string;
		
		private Field(final String str) {
			this.string = str;
		}
		
		public abstract Builder set(final Builder builder, final String value);
		
		@Override
		public String toString() {
			return this.string;
		}
		
	}

	private static final Socks5UdpRule DEFAULT_INSTANCE = 
			new Socks5UdpRule.Builder(RuleAction.ALLOW).build();
	
	public static Socks5UdpRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5UdpRule> newInstances(final String s) {
		List<Socks5UdpRule> socks5UdpRules = 
				new ArrayList<Socks5UdpRule>();
		String[] words = s.split(" ");
		Builder recentBuilder = null;
		Builder builder = null;
		for (Iterator<String> iterator = Arrays.asList(words).iterator();
				iterator.hasNext();) {
			String word = iterator.next();
			String[] wordElements = word.split("=", 2);
			if (wordElements.length != 2) {
				throw new IllegalArgumentException(String.format(
						"field must be in the following format: "
						+ "NAME=VALUE. actual value is %s",
						word));
			}
			Field field = Field.valueOfString(wordElements[0]);
			try {
				builder = field.set(builder, wordElements[1]);
			} catch (NullPointerException e) {
				throw new IllegalArgumentException(String.format(
						"expected field '%s'. actual value is %s", 
						Field.RULE_ACTION,
						word));				
			}
			if (recentBuilder == null) {
				recentBuilder = builder;
			}
			if (!recentBuilder.equals(builder)) {
				socks5UdpRules.add(recentBuilder.build());
				recentBuilder = builder;
			}
			if (!iterator.hasNext()) {
				socks5UdpRules.add(builder.build());
			}
		}
		return socks5UdpRules;
	}
	
	private final RuleAction ruleAction;
	private final AddressRange clientAddressRange;
	private final Method method;
	private final String user;
	private final AddressRange peerAddressRange;
	private final LogAction logAction;
	private final String doc;
	
	private Socks5UdpRule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		AddressRange clientAddrRange = builder.clientAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		AddressRange peerAddrRange = builder.peerAddressRange;
		LogAction lgAction = builder.logAction;
		String d = builder.doc;
		this.ruleAction = rlAction;
		this.clientAddressRange = clientAddrRange;
		this.method = meth;
		this.user = usr;
		this.peerAddressRange = peerAddrRange;
		this.logAction = lgAction;
		this.doc = d;		
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
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (this.ruleAction.equals(RuleAction.ALLOW)
				&& this.logAction != null) {
			this.logAction.log(String.format(
					"Traffic between client %s%s and peer %s is allowed based "
					+ "on the following rule: %s",
					clientAddress,
					possibleUser,
					peerAddress,
					this));			
		} else if (this.ruleAction.equals(RuleAction.DENY)) {
			if (this.logAction != null) {
				this.logAction.log(String.format(
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
		if (this.logAction != other.logAction) {
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
		if (this.ruleAction != other.ruleAction) {
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
	
	public String getDoc() {
		return this.doc;
	}

	public LogAction getLogAction() {
		return this.logAction;
	}
	
	public Method getMethod() {
		return this.method;
	}

	public AddressRange getPeerAddressRange() {
		return this.peerAddressRange;
	}
	
	public RuleAction getRuleAction() {
		return this.ruleAction;
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
		result = prime * result + ((this.logAction == null) ? 
				0 : this.logAction.hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result	+ ((this.peerAddressRange == null) ? 
				0 : this.peerAddressRange.hashCode());
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.user == null) ? 
				0 : this.user.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ruleAction=");
		builder.append(this.ruleAction);
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
		if (this.logAction != null) {
			builder.append(" logAction=");
			builder.append(this.logAction);			
		}
		return builder.toString();
	}

}
