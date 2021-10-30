package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Objects;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRule {

	public static final class Builder {
		
		private final RuleAction ruleAction;
		private AddressRange sourceAddressRange;
		private Command command;
		private AddressRange desiredDestinationAddressRange;
		private PortRange desiredDestinationPortRange;
		private LogAction logAction;
		private String doc;		
		
		public Builder(final RuleAction rlAction) {
			this.ruleAction = Objects.requireNonNull(
					rlAction, "rule action must not be null");
			this.sourceAddressRange = null;
			this.command = null;
			this.desiredDestinationAddressRange = null;
			this.desiredDestinationPortRange = null;
			this.logAction = null;
			this.doc = null;			
		}
		
		public Socks5RequestRule build() {
			return new Socks5RequestRule(this);
		}
		
		public Builder command(final Command cmd) {
			this.command = cmd;
			return this;
		}
		
		public Builder desiredDestinationAddressRange(
				final AddressRange desiredDestinationAddrRange) {
			this.desiredDestinationAddressRange = desiredDestinationAddrRange;
			return this;
		}
		
		public Builder desiredDestinationPortRange(
				final PortRange desiredDestinationPrtRange) {
			this.desiredDestinationPortRange = desiredDestinationPrtRange;
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
		
		public Builder sourceAddressRange(
				final AddressRange sourceAddrRange) {
			this.sourceAddressRange = sourceAddrRange;
			return this;
		}
		
	}

	private static final Socks5RequestRule DEFAULT_INSTANCE = 
			new Socks5RequestRule.Builder(RuleAction.ALLOW).build();
	
	public static Socks5RequestRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	private final RuleAction ruleAction;
	private final AddressRange sourceAddressRange;
	private final Command command;
	private final AddressRange desiredDestinationAddressRange;
	private final PortRange desiredDestinationPortRange;
	private final LogAction logAction;
	private final String doc;
	
	private Socks5RequestRule(final Builder builder) {
		RuleAction rlAction = builder.ruleAction;
		AddressRange sourceAddrRange = builder.sourceAddressRange;
		Command cmd = builder.command;
		AddressRange desiredDestinationAddrRange = 
				builder.desiredDestinationAddressRange;
		PortRange desiredDestinationPrtRange = 
				builder.desiredDestinationPortRange;
		LogAction lgAction = builder.logAction;
		String d = builder.doc;
		this.ruleAction = rlAction;
		this.sourceAddressRange = sourceAddrRange;
		this.command = cmd;
		this.desiredDestinationAddressRange = desiredDestinationAddrRange;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
		this.logAction = lgAction;
		this.doc = d;		
	}
	
	public boolean appliesTo(
			final String sourceAddress,	final Socks5Request socks5Req) {
		if (this.sourceAddressRange != null 
				&& !this.sourceAddressRange.contains(sourceAddress)) {
			return false;
		}
		if (this.command != null
				&& !this.command.equals(socks5Req.getCommand())) {
			return false;
		}
		if (this.desiredDestinationAddressRange != null
				&& !this.desiredDestinationAddressRange.contains(
						socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (this.desiredDestinationPortRange != null
				&& !this.desiredDestinationPortRange.contains(
						Port.newInstance(socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}

	public void applyTo(
			final String sourceAddress,	final Socks5Request socks5Req) {
		if (this.ruleAction.equals(RuleAction.ALLOW)
				&& this.logAction != null) {
			this.logAction.log(String.format(
					"SOCKS5 request from %s is allowed based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					sourceAddress,
					this,
					socks5Req));			
		} else if (this.ruleAction.equals(RuleAction.DENY)) {
			if (this.logAction != null) {
				this.logAction.log(String.format(
						"SOCKS5 request from %s is denied based on the "
						+ "following rule: %s. SOCKS5 request: %s",
						sourceAddress,
						this,
						socks5Req));				
			}
			throw new RuleActionDenyException(String.format(
					"SOCKS5 request from %s is denied based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					sourceAddress,
					this,
					socks5Req));
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
		Socks5RequestRule other = (Socks5RequestRule) obj;
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
		if (this.command == null) {
			if (other.command != null) {
				return false;
			}
		} else if (!this.command.equals(other.command)) {
			return false;
		}
		if (this.desiredDestinationAddressRange == null) {
			if (other.desiredDestinationAddressRange != null) {
				return false;
			}
		} else if (!this.desiredDestinationAddressRange.equals(
				other.desiredDestinationAddressRange)) {
			return false;
		}
		if (this.desiredDestinationPortRange == null) {
			if (other.desiredDestinationPortRange != null) {
				return false;
			}
		} else if (!this.desiredDestinationPortRange.equals(
				other.desiredDestinationPortRange)) {
			return false;
		}
		return true;
	}
	
	public Command getCommand() {
		return this.command;
	}

	public AddressRange getDesiredDestinationAddressRange() {
		return this.desiredDestinationAddressRange;
	}

	public PortRange getDesiredDestinationPortRange() {
		return this.desiredDestinationPortRange;
	}
	
	public String getDoc() {
		return this.doc;
	}

	public LogAction getLogAction() {
		return this.logAction;
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
		result = prime * result + ((this.ruleAction == null) ? 
				0 : this.ruleAction.hashCode());
		result = prime * result + ((this.sourceAddressRange == null) ? 
				0 : this.sourceAddressRange.hashCode());
		result = prime * result + ((this.command == null) ? 
				0 : this.command.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressRange == null) ? 
						0 : this.desiredDestinationAddressRange.hashCode());
		result = prime * result
				+ ((this.desiredDestinationPortRange == null) ? 
						0 : this.desiredDestinationPortRange.hashCode());
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
			.append(", command=")
			.append(this.command)
			.append(", desiredDestinationAddressRange=")
			.append(this.desiredDestinationAddressRange)
			.append(", desiredDestinationPortRange=")
			.append(this.desiredDestinationPortRange)
			.append(", logAction=")
			.append(this.logAction)
			.append("]");
		return builder.toString();
	}

}
