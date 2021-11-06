package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.AddressRange;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleAction;
import com.github.jh3nd3rs0n.jargyle.server.RuleActionDenyException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestRule extends Rule {

	public static final class Builder 
		extends Rule.Builder<Builder, Socks5RequestRule> {

		public static enum Field 
			implements Rule.Builder.Field<Builder, Socks5RequestRule> {
			
			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new SOCKS5 request rule.",
					usage = "ruleAction=RULE_ACTION"
			)	
			RULE_ACTION("ruleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new Socks5RequestRule.Builder(
							RuleAction.valueOfString(value));
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
					doc = "Specifies the command type of the SOCKS5 request",
					usage = "command=SOCKS5_COMMAND"
			)
			COMMAND("command") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.command(Command.valueOfString(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the address range for the desired "
							+ "destination address of the SOCKS5 request",
					usage = "desiredDestinationAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)	
			DESIRED_DESTINATION_ADDRESS_RANGE("desiredDestinationAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.desiredDestinationAddressRange(
							AddressRange.newInstance(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the port range for the desired "
							+ "destination port of the SOCKS5 request",
					usage = "desiredDestinationPortRange=PORT|PORT1-PORT2"
			)	
			DESIRED_DESTINATION_PORT_RANGE("desiredDestinationPortRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.desiredDestinationPortRange(
							PortRange.newInstance(value));
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
		private Command command;
		private AddressRange desiredDestinationAddressRange;
		private PortRange desiredDestinationPortRange;
		
		public Builder(final RuleAction rlAction) {
			super(rlAction);
			this.clientAddressRange = null;
			this.method = null;
			this.user = null;
			this.command = null;
			this.desiredDestinationAddressRange = null;
			this.desiredDestinationPortRange = null;
		}

		@Override
		public Socks5RequestRule build() {
			return new Socks5RequestRule(this);
		}
		
		public Builder clientAddressRange(final AddressRange clientAddrRange) {
			this.clientAddressRange = clientAddrRange;
			return this;
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
		
		public Builder user(final String usr) {
			this.user = usr;
			return this;
		}
		
	}

	private static final Socks5RequestRule DEFAULT_INSTANCE = 
			new Socks5RequestRule.Builder(RuleAction.ALLOW).build();
	
	public static Socks5RequestRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5RequestRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final Method method;
	private final String user;
	private final Command command;
	private final AddressRange desiredDestinationAddressRange;
	private final PortRange desiredDestinationPortRange;
	
	private Socks5RequestRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		Command cmd = builder.command;
		AddressRange desiredDestinationAddrRange = 
				builder.desiredDestinationAddressRange;
		PortRange desiredDestinationPrtRange = 
				builder.desiredDestinationPortRange;
		this.clientAddressRange = clientAddrRange;
		this.method = meth;
		this.user = usr;
		this.command = cmd;
		this.desiredDestinationAddressRange = desiredDestinationAddrRange;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
	}
	
	public boolean appliesTo(
			final String clientAddress,
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		if (this.clientAddressRange != null 
				&& !this.clientAddressRange.contains(clientAddress)) {
			return false;
		}
		if (this.method != null 
				&& !this.method.equals(methSubnegotiationResults.getMethod())) {
			return false;
		}
		if (this.user != null 
				&& !this.user.equals(methSubnegotiationResults.getUser())) {
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
				&& !this.desiredDestinationPortRange.contains(Port.newInstance(
						socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}

	public void applyTo(
			final String clientAddress,	
			final MethodSubnegotiationResults methSubnegotiationResults,
			final Socks5Request socks5Req) {
		RuleAction ruleAction = this.getRuleAction();
		LogAction logAction = this.getLogAction();
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (ruleAction.equals(RuleAction.ALLOW)	&& logAction != null) {
			logAction.invoke(String.format(
					"SOCKS5 request from %s%s is allowed based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					clientAddress,
					possibleUser,
					this,
					socks5Req));			
		} else if (ruleAction.equals(RuleAction.DENY)) {
			if (logAction != null) {
				logAction.invoke(String.format(
						"SOCKS5 request from %s%s is denied based on the "
						+ "following rule: %s. SOCKS5 request: %s",
						clientAddress,
						possibleUser,
						this,
						socks5Req));				
			}
			throw new RuleActionDenyException(String.format(
					"SOCKS5 request from %s%s is denied based on the "
					+ "following rule: %s. SOCKS5 request: %s",
					clientAddress,
					possibleUser,
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
		if (this.clientAddressRange == null) {
			if (other.clientAddressRange != null) {
				return false;
			}
		} else if (!this.clientAddressRange.equals(other.clientAddressRange)) {
			return false;
		}
		if (this.command != other.command) {
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
		if (this.getLogAction() != other.getLogAction()) {
			return false;
		}
		if (this.method != other.method) {
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
	
	public Command getCommand() {
		return this.command;
	}
	
	public AddressRange getDesiredDestinationAddressRange() {
		return this.desiredDestinationAddressRange;
	}

	public PortRange getDesiredDestinationPortRange() {
		return this.desiredDestinationPortRange;
	}

	public Method getMethod() {
		return this.method;
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
		result = prime * result + ((this.command == null) ? 
				0 : this.command.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressRange == null) ? 
						0 : desiredDestinationAddressRange.hashCode());
		result = prime * result + ((this.desiredDestinationPortRange == null) ? 
				0 : desiredDestinationPortRange.hashCode());
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
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
		if (this.command != null) {
			builder.append(" command=");
			builder.append(this.command);
		}
		if (this.desiredDestinationAddressRange != null) {
			builder.append(" desiredDestinationAddressRange=");
			builder.append(this.desiredDestinationAddressRange);			
		}
		if (this.desiredDestinationPortRange != null) {
			builder.append(" desiredDestinationPortRange=");
			builder.append(this.desiredDestinationPortRange);
		}
		LogAction logAction = this.getLogAction();
		if (logAction != null) {
			builder.append(" logAction=");
			builder.append(logAction);			
		}
		return builder.toString();
	}

}
