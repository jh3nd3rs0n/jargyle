package com.github.jh3nd3rs0n.jargyle.server;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;

public final class Rule {

	public static final class Builder {
		
		private final RuleAction ruleAction;
		private AddressRange sourceAddressRange;
		private AddressRange destinationAddressRange;
		private String doc;
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.sourceAddressRange = null;
			this.destinationAddressRange = null;
			this.doc = null;
		}
		
		public Rule build() {
			return new Rule(this);
		}
		
		public Builder destinationAddressRange(
				final AddressRange destinationAddrRange) {
			this.destinationAddressRange = destinationAddrRange;
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
		
		public Builder sourceAddressRange(
				final AddressRange sourceAddrRange) {
			this.sourceAddressRange = sourceAddrRange;
			return this;
		}
	}
	
	private static final Rule DEFAULT_INSTANCE = new Rule.Builder(
			RuleAction.ALLOW).build();
	
	public static Rule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	private final RuleAction ruleAction;
	private final AddressRange sourceAddressRange;
	private final AddressRange destinationAddressRange;
	private final String doc;
	
	private Rule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		AddressRange sourceAddrRange = builder.sourceAddressRange;
		AddressRange destinationAddrRange = builder.destinationAddressRange;
		String d = builder.doc;
		this.sourceAddressRange = sourceAddrRange;
		this.destinationAddressRange = destinationAddrRange;
		this.ruleAction = rlAction;
		this.doc = d;		
	}

	public boolean appliesTo(
			final String sourceAddress, final String destinationAddress) {
		if (this.sourceAddressRange != null
				&& !this.sourceAddressRange.contains(sourceAddress)) {
			return false;
		}
		if (this.destinationAddressRange != null
				&& !this.destinationAddressRange.contains(destinationAddress)) {
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
		Rule other = (Rule) obj;
		if (this.destinationAddressRange == null) {
			if (other.destinationAddressRange != null) {
				return false;
			}
		} else if (!this.destinationAddressRange.equals(
				other.destinationAddressRange)) {
			return false;
		}
		if (this.ruleAction != other.ruleAction) {
			return false;
		}
		if (this.sourceAddressRange == null) {
			if (other.sourceAddressRange != null) {
				return false;
			}
		} else if (!this.sourceAddressRange.equals(
				other.sourceAddressRange)) {
			return false;
		}
		return true;
	}

	public AddressRange getDestinationAddressRange() {
		return this.destinationAddressRange;
	}

	public String getDoc() {
		return this.doc;
	}

	public RuleAction getRuleAction() {
		return this.ruleAction;
	}
	
	public AddressRange getSourceAddressRange() {
		return this.sourceAddressRange;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.destinationAddressRange == null) ? 
				0 : this.destinationAddressRange.hashCode());
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.sourceAddressRange == null) ? 
				0 : this.sourceAddressRange.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [ruleAction=")
			.append(this.ruleAction)
			.append(", sourceAddressRange=")
			.append(this.sourceAddressRange)
			.append(", destinationAddressRange=")
			.append(this.destinationAddressRange)
			.append("]");
		return builder.toString();
	}

}
