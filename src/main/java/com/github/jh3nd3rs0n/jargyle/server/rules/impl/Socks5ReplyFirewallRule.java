package com.github.jh3nd3rs0n.jargyle.server.rules.impl;

import java.util.Arrays;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.socks5.MethodSubnegotiationResults;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5ReplyFirewallRule extends FirewallRule {

	public static final class Builder 
		extends FirewallRule.Builder<Builder, Socks5ReplyFirewallRule> {

		public static enum Field 
			implements FirewallRule.Builder.Field<Builder, Socks5ReplyFirewallRule>{

			@HelpText(
					doc = "Specifies the action to take. This field starts a "
							+ "new SOCKS5 reply firewall rule.",
					usage = "firewallRuleAction=FIREWALL_RULE_ACTION"
			)	
			FIREWALL_RULE_ACTION("firewallRuleAction") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return new Socks5ReplyFirewallRule.Builder(
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
					doc = "Specifies the SOCKS5 command of the SOCKS5 request",
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
					doc = "Specifies the address range for the server bound "
							+ "address of the SOCKS5 reply",
					usage = "serverBoundAddressRange=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION"
			)		
			SERVER_BOUND_ADDRESS_RANGE("serverBoundAddressRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.serverBoundAddressRange(
							AddressRange.newInstance(value));
				}
			},
			
			@HelpText(
					doc = "Specifies the port range for the server bound port "
							+ "of the SOCKS5 reply",
					usage = "serverBoundPortRange=PORT|PORT1-PORT2"
			)		
			SERVER_BOUND_PORT_RANGE("serverBoundPortRange") {
				@Override
				public Builder set(final Builder builder, final String value) {
					return builder.serverBoundPortRange(PortRange.newInstance(
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
		private AddressRange socksServerAddressRange;
		private Method method;
		private String user;
		private Command command;
		private AddressRange desiredDestinationAddressRange;
		private PortRange desiredDestinationPortRange;
		private AddressRange serverBoundAddressRange;
		private PortRange serverBoundPortRange;
		
		public Builder(final FirewallRuleAction firewallRlAction) {
			super(firewallRlAction);
			this.clientAddressRange = null;
			this.socksServerAddressRange = null;
			this.method = null;
			this.user = null;
			this.command = null;
			this.desiredDestinationAddressRange = null;
			this.desiredDestinationPortRange = null;
			this.serverBoundAddressRange = null;
			this.serverBoundPortRange = null;
		}

		@Override
		public Socks5ReplyFirewallRule build() {
			return new Socks5ReplyFirewallRule(this);
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
		
		public Builder serverBoundAddressRange(
				final AddressRange serverBoundAddrRange) {
			this.serverBoundAddressRange = serverBoundAddrRange;
			return this;
		}
		
		public Builder serverBoundPortRange(
				final PortRange serverBoundPrtRange) {
			this.serverBoundPortRange = serverBoundPrtRange;
			return this;
		}
		
		public Builder socksServerAddressRange(
				final AddressRange socksServerAddrRange) {
			this.socksServerAddressRange = socksServerAddrRange;
			return this;
		}
		
		public Builder user(final String usr) {
			this.user = usr;
			return this;
		}
		
	}

	public static final class Context extends FirewallRule.Context {
		
		private final String clientAddress;
		private final String socksServerAddress;		
		private final MethodSubnegotiationResults methodSubnegotiationResults;
		private final Socks5Request socks5Request;
		private final Socks5Reply socks5Reply;
		
		public Context(
				final String clientAddr,
				final String socksServerAddr,
				final MethodSubnegotiationResults methSubnegotiationResults,
				final Socks5Request socks5Req,
				final Socks5Reply socks5Rep) {
			this.clientAddress = clientAddr;
			this.socksServerAddress = socksServerAddr;
			this.methodSubnegotiationResults = methSubnegotiationResults;
			this.socks5Request = socks5Req;
			this.socks5Reply = socks5Rep;
		}

		public String getClientAddress() {
			return this.clientAddress;
		}

		public MethodSubnegotiationResults getMethodSubnegotiationResults() {
			return this.methodSubnegotiationResults;
		}

		public Socks5Reply getSocks5Reply() {
			return this.socks5Reply;
		}

		public Socks5Request getSocks5Request() {
			return this.socks5Request;
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
				.append(", methodSubnegotiationResults=")
				.append(this.methodSubnegotiationResults)
				.append(", socks5Request=")
				.append(this.socks5Request)
				.append(", socks5Reply=")
				.append(this.socks5Reply)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final Socks5ReplyFirewallRule DEFAULT_INSTANCE = 
			new Socks5ReplyFirewallRule.Builder(
					FirewallRuleAction.ALLOW).build();
	
	public static Socks5ReplyFirewallRule getDefault() {
		return DEFAULT_INSTANCE;
	}
	
	public static List<Socks5ReplyFirewallRule> newInstances(final String s) {
		return Rule.newInstances(
				s, 
				Builder.Field.FIREWALL_RULE_ACTION, 
				Arrays.asList(Builder.Field.values()));
	}
	
	private final AddressRange clientAddressRange;
	private final AddressRange socksServerAddressRange;
	private final Method method;
	private final String user;
	private final Command command;
	private final AddressRange desiredDestinationAddressRange;
	private final PortRange desiredDestinationPortRange;
	private final AddressRange serverBoundAddressRange;
	private final PortRange serverBoundPortRange;
	
	private Socks5ReplyFirewallRule(final Builder builder) {
		super(builder);
		AddressRange clientAddrRange = builder.clientAddressRange;
		AddressRange socksServerAddrRange = builder.socksServerAddressRange;
		Method meth = builder.method;
		String usr = builder.user;
		Command cmd = builder.command;
		AddressRange desiredDestinationAddrRange = 
				builder.desiredDestinationAddressRange;
		PortRange desiredDestinationPrtRange = 
				builder.desiredDestinationPortRange;
		AddressRange serverBoundAddrRange = builder.serverBoundAddressRange;
		PortRange serverBoundPrtRange = builder.serverBoundPortRange;
		this.clientAddressRange = clientAddrRange;
		this.socksServerAddressRange = socksServerAddrRange;
		this.method = meth;
		this.user = usr;
		this.command = cmd;
		this.desiredDestinationAddressRange = desiredDestinationAddrRange;
		this.desiredDestinationPortRange = desiredDestinationPrtRange;
		this.serverBoundAddressRange = serverBoundAddrRange;
		this.serverBoundPortRange = serverBoundPrtRange;
	}
	
	@Override
	public boolean appliesTo(final Rule.Context context) {
		if (!(context instanceof Context)) {
			return false;
		}
		Context cntxt = (Context) context;
		if (cntxt.getFirewallRuleAction() != null) {
			return false;
		}
		MethodSubnegotiationResults methSubnegotiationResults =
				cntxt.getMethodSubnegotiationResults();
		Socks5Request socks5Req = cntxt.getSocks5Request();
		Socks5Reply socks5Rep = cntxt.getSocks5Reply();
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
				&& !this.desiredDestinationPortRange.contains(
						Port.newInstance(socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		if (this.serverBoundAddressRange != null
				&& !this.serverBoundAddressRange.contains(
						socks5Rep.getServerBoundAddress())) {
			return false;
		}
		if (this.serverBoundPortRange != null 
				&& !this.serverBoundPortRange.contains(Port.newInstance(
						socks5Rep.getServerBoundPort()))) {
			return false;
		}
		return true;
	}

	@Override
	public void applyTo(final Rule.Context context) {
		FirewallRuleAction firewallRuleAction = this.getFirewallRuleAction();
		Context cntxt = (Context) context;
		cntxt.setFirewallRuleAction(firewallRuleAction);
		LogAction logAction = this.getLogAction();
		if (logAction == null) {
			return;
		}
		String clientAddress = cntxt.getClientAddress();
		MethodSubnegotiationResults methSubnegotiationResults = 
				cntxt.getMethodSubnegotiationResults();
		String user = methSubnegotiationResults.getUser();
		String possibleUser = (user != null) ? 
				String.format(" (%s)", user) : "";
		if (firewallRuleAction.equals(FirewallRuleAction.ALLOW)) {
			logAction.invoke(String.format(
					"SOCKS5 reply to %s%s is allowed based on the following "
					+ "firewall rule and context: %s, %s",
					clientAddress,
					possibleUser,
					this,
					context));			
		} else if (firewallRuleAction.equals(FirewallRuleAction.DENY)) {
			logAction.invoke(String.format(
					"SOCKS5 reply to %s%s is denied based on the following "
					+ "firewall rule and context: %s, %s",
					clientAddress,
					possibleUser,
					this,
					context));
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
		Socks5ReplyFirewallRule other = (Socks5ReplyFirewallRule) obj;
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
		if (this.getFirewallRuleAction() != other.getFirewallRuleAction()) {
			return false;
		}
		if (this.getLogAction() != other.getLogAction()) {
			return false;
		}
		if (this.method != other.method) {
			return false;
		}
		if (this.serverBoundAddressRange == null) {
			if (other.serverBoundAddressRange != null) {
				return false;
			}
		} else if (!this.serverBoundAddressRange.equals(
				other.serverBoundAddressRange)) {
			return false;
		}
		if (this.serverBoundPortRange == null) {
			if (other.serverBoundPortRange != null) {
				return false;
			}
		} else if (!this.serverBoundPortRange.equals(
				other.serverBoundPortRange)) {
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
	
	public AddressRange getServerBoundAddressRange() {
		return this.serverBoundAddressRange;
	}

	public PortRange getServerBoundPortRange() {
		return this.serverBoundPortRange;
	}
	
	public AddressRange getSocksServerAddressRange() {
		return this.socksServerAddressRange;
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
				+ ((this.desiredDestinationAddressRange == null) ? 0 : 
					this.desiredDestinationAddressRange.hashCode());
		result = prime * result + ((this.desiredDestinationPortRange == null) ? 
				0 : this.desiredDestinationPortRange.hashCode());
		result = prime * result + ((this.getFirewallRuleAction() == null) ? 
				0 : this.getFirewallRuleAction().hashCode());
		result = prime * result + ((this.getLogAction() == null) ? 
				0 : this.getLogAction().hashCode());
		result = prime * result + ((this.method == null) ? 
				0 : this.method.hashCode());
		result = prime * result + ((this.serverBoundAddressRange == null) ? 
				0 : this.serverBoundAddressRange.hashCode());
		result = prime * result + ((this.serverBoundPortRange == null) ? 
				0 : this.serverBoundPortRange.hashCode());
		result = prime * result + ((this.socksServerAddressRange == null) ? 
				0 : this.socksServerAddressRange.hashCode());
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
		if (this.socksServerAddressRange != null) {
			builder.append(" socksServerAddressRange=");
			builder.append(this.socksServerAddressRange);
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
		if (this.serverBoundAddressRange != null) {
			builder.append(" serverBoundAddressRange=");
			builder.append(this.serverBoundAddressRange);			
		}
		if (this.serverBoundPortRange != null) {
			builder.append(" serverBoundPortRange=");
			builder.append(this.serverBoundPortRange);
		}
		LogAction logAction = this.getLogAction();
		if (logAction != null) {
			builder.append(" logAction=");
			builder.append(logAction);			
		}
		return builder.toString();
	}

}
