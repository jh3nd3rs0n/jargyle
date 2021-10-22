package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.ConditionPredicate;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRule {

	public static final class Builder {
		
		private final RuleAction ruleAction;
		private ConditionPredicate clientAddressConditionPredicate;
		private ConditionPredicate commandConditionPredicate;
		private ConditionPredicate desiredDestinationAddressConditionPredicate;
		private PortRanges desiredDestinationPortRanges;
		private String doc;		
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.clientAddressConditionPredicate = null;
			this.commandConditionPredicate = null;
			this.desiredDestinationAddressConditionPredicate = null;
			this.desiredDestinationPortRanges = null;
			this.doc = null;			
		}
		
		public Socks5RequestRule build() {
			return new Socks5RequestRule(this);
		}
		
		public Builder clientAddressConditionPredicate(
				final ConditionPredicate clientAddrConditionPredicate) {
			this.clientAddressConditionPredicate = clientAddrConditionPredicate;
			return this;
		}
		
		public Builder commandConditionPredicate(
				final ConditionPredicate cmdConditionPredicate) {
			this.commandConditionPredicate = cmdConditionPredicate;
			return this;
		}
		
		public Builder desiredDestinationAddressConditionPredicate(
				final ConditionPredicate desiredDestinationAddrConditionPredicate) {
			this.desiredDestinationAddressConditionPredicate = 
					desiredDestinationAddrConditionPredicate;
			return this;
		}
		
		public Builder desiredDestinationPortRanges(
				final PortRanges desiredDestinationPrtRanges) {
			this.desiredDestinationPortRanges = desiredDestinationPrtRanges;
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
		
	}
	
	private final RuleAction ruleAction;
	private final ConditionPredicate clientAddressConditionPredicate;
	private final ConditionPredicate commandConditionPredicate;
	private final ConditionPredicate desiredDestinationAddressConditionPredicate;
	private final PortRanges desiredDestinationPortRanges;
	private final String doc;
	
	private Socks5RequestRule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		ConditionPredicate clientAddrConditionPredicate = 
				builder.clientAddressConditionPredicate;
		ConditionPredicate cmdConditionPredicate = 
				builder.commandConditionPredicate;
		ConditionPredicate desiredDestinationAddrConditionPredicate = 
				builder.desiredDestinationAddressConditionPredicate;
		PortRanges desiredDestinationPrtRanges = 
				builder.desiredDestinationPortRanges;
		String d = builder.doc;
		this.ruleAction = rlAction;
		this.clientAddressConditionPredicate = clientAddrConditionPredicate;
		this.commandConditionPredicate = cmdConditionPredicate;
		this.desiredDestinationAddressConditionPredicate = 
				desiredDestinationAddrConditionPredicate;
		this.desiredDestinationPortRanges = desiredDestinationPrtRanges;
		this.doc = d;		
	}
	
	public boolean appliesTo(
			final String clientAddress,	final Socks5Request socks5Req) {
		if (this.clientAddressConditionPredicate != null 
				&& !this.clientAddressConditionPredicate.evaluate(
						clientAddress)) {
			return false;
		}
		if (this.commandConditionPredicate != null
				&& !this.commandConditionPredicate.evaluate(
						socks5Req.getCommand().toString())) {
			return false;
		}
		if (this.desiredDestinationAddressConditionPredicate != null
				&& !this.desiredDestinationAddressConditionPredicate.evaluate(
						socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (this.desiredDestinationPortRanges != null
				&& !this.desiredDestinationPortRanges.contains(Port.newInstance(
						socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
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
		Socks5RequestRule other = (Socks5RequestRule) obj;
		if (this.ruleAction != other.ruleAction) {
			return false;
		}
		if (this.clientAddressConditionPredicate == null) {
			if (other.clientAddressConditionPredicate != null) {
				return false;
			}
		} else if (!this.clientAddressConditionPredicate.equals(
				other.clientAddressConditionPredicate)) {
			return false;
		}
		if (this.commandConditionPredicate == null) {
			if (other.commandConditionPredicate != null) {
				return false;
			}
		} else if (!this.commandConditionPredicate.equals(
				other.commandConditionPredicate)) {
			return false;
		}
		if (this.desiredDestinationAddressConditionPredicate == null) {
			if (other.desiredDestinationAddressConditionPredicate != null) {
				return false;
			}
		} else if (!this.desiredDestinationAddressConditionPredicate.equals(
				other.desiredDestinationAddressConditionPredicate)) {
			return false;
		}
		if (this.desiredDestinationPortRanges == null) {
			if (other.desiredDestinationPortRanges != null) {
				return false;
			}
		} else if (!this.desiredDestinationPortRanges.equals(
				other.desiredDestinationPortRanges)) {
			return false;
		}
		return true;
	}
	
	public ConditionPredicate getClientAddressConditionPredicate() {
		return this.clientAddressConditionPredicate;
	}

	public ConditionPredicate getCommandConditionPredicate() {
		return this.commandConditionPredicate;
	}

	public ConditionPredicate getDesiredDestinationAddressConditionPredicate() {
		return this.desiredDestinationAddressConditionPredicate;
	}
	
	public PortRanges getDesiredDestinationPortRanges() {
		return this.desiredDestinationPortRanges;
	}
	
	public String getDoc() {
		return this.doc;
	}

	public RuleAction getRuleAction() {
		return this.ruleAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.clientAddressConditionPredicate == null) ? 
				0 : this.clientAddressConditionPredicate.hashCode());
		result = prime * result + ((this.commandConditionPredicate == null) ? 
				0 : this.commandConditionPredicate.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressConditionPredicate == null) ? 
						0 : this.desiredDestinationAddressConditionPredicate.hashCode());
		result = prime * result
				+ ((this.desiredDestinationPortRanges == null) ? 
						0 : this.desiredDestinationPortRanges.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [ruleAction=")
			.append(this.ruleAction)
			.append(", clientAddressConditionPredicate=")
			.append(this.clientAddressConditionPredicate)
			.append(", commandConditionPredicate=")
			.append(this.commandConditionPredicate)
			.append(", desiredDestinationAddressConditionPredicate=")
			.append(this.desiredDestinationAddressConditionPredicate)
			.append(", desiredDestinationPortRanges=")
			.append(this.desiredDestinationPortRanges)
			.append("]");
		return builder.toString();
	}

}
